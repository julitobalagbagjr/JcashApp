package jcash.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import jcash.database.DatabaseConnection;
import jcash.model.Account;
import jcash.model.Transaction;
import jcash.model.User;
import jcash.repository.AccountRepository;
import jcash.repository.TransactionRepository;
import jcash.repository.UserRepository;
import jcash.util.ReferenceNumberGenerator;

public class TransactionService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService() {
        userRepository = new UserRepository();
        accountRepository = new AccountRepository();
        transactionRepository = new TransactionRepository();
    }

    public Transaction transfer(
            User sender,
            String receiverNumber,
            BigDecimal amount
    ) {

        // Find the receiver
        User receiver =
                userRepository.findByNumber(receiverNumber);

        if (receiver == null) {
            return null;
        }

        // Prevent sending money to the same account
        if (sender.getId() == receiver.getId()) {
            return null;
        }

        // Check the transfer amount
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }

        // Get sender account
        Account senderAccount =
                accountRepository.findByUserId(sender.getId());

        if (senderAccount == null) {
            return null;
        }

        // Get receiver account
        Account receiverAccount =
                accountRepository.findByUserId(receiver.getId());

        if (receiverAccount == null) {
            return null;
        }

        // Check sender balance
        if (senderAccount.getBalance().compareTo(amount) < 0) {
            return null;
        }

        String referenceNo =
                ReferenceNumberGenerator.generate();

        // Update balances and save the transaction together
        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            connection.setAutoCommit(false);

            boolean senderUpdated =
                    accountRepository.decreaseBalance(
                            connection,
                            senderAccount.getId(),
                            amount
                    );

            if (!senderUpdated) {
                connection.rollback();
                return null;
            }

            boolean receiverUpdated =
                    accountRepository.increaseBalance(
                            connection,
                            receiverAccount.getId(),
                            amount
                    );

            if (!receiverUpdated) {
                connection.rollback();
                return null;
            }

            boolean transactionSaved =
                    transactionRepository.saveTransaction(
                            connection,
                            referenceNo,
                            senderAccount.getId(),
                            receiverAccount.getId(),
                            amount
                    );

            if (!transactionSaved) {
                connection.rollback();
                return null;
            }

            connection.commit();

            return new Transaction(
                    0,
                    referenceNo,
                    senderAccount.getId(),
                    receiverAccount.getId(),
                    amount,
                    LocalDateTime.now()
            );

        } catch (SQLException e) {

            e.printStackTrace();
            return null;
        }
    }

    public List<Transaction> getTransactionHistory(User user) {

        // Get the user's account
        Account account =
                accountRepository.findByUserId(user.getId());

        if (account == null) {
            return List.of();
        }

        return transactionRepository.findByAccountId(
                account.getId()
        );
    }
}