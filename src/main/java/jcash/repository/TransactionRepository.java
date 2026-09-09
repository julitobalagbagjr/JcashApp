package jcash.repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jcash.database.DatabaseConnection;
import jcash.model.Transaction;

public class TransactionRepository {

    public boolean saveTransaction(
            Connection connection,
            String referenceNo,
            int senderAccountId,
            int receiverAccountId,
            BigDecimal amount
    ) {

        String sql = """
                INSERT INTO transactions (
                    reference_no,
                    sender_account_id,
                    receiver_account_id,
                    amount
                )
                VALUES (?, ?, ?, ?)
                """;

        try (
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, referenceNo);
            statement.setInt(2, senderAccountId);
            statement.setInt(3, receiverAccountId);
            statement.setBigDecimal(4, amount);

            int rowsAffected =
                    statement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Transaction> findByAccountId(int accountId) {

        String sql = """
                SELECT
                    id,
                    reference_no,
                    sender_account_id,
                    receiver_account_id,
                    amount,
                    created_at
                FROM transactions
                WHERE sender_account_id = ?
                   OR receiver_account_id = ?
                ORDER BY created_at DESC
                """;

        List<Transaction> transactions =
                new ArrayList<>();

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, accountId);
            statement.setInt(2, accountId);

            // Get the account's transactions
            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                Transaction transaction =
                        new Transaction(
                                resultSet.getInt("id"),
                                resultSet.getString("reference_no"),
                                resultSet.getInt(
                                        "sender_account_id"
                                ),
                                resultSet.getInt(
                                        "receiver_account_id"
                                ),
                                resultSet.getBigDecimal("amount"),
                                resultSet.getTimestamp(
                                                "created_at"
                                        )
                                        .toLocalDateTime()
                        );

                transactions.add(transaction);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }
}