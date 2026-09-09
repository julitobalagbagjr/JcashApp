package jcash.ui;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import jcash.model.Account;
import jcash.model.Transaction;
import jcash.model.User;
import jcash.service.AuthService;
import jcash.service.TransactionService;

public class TransactionHistoryPageFX {

    private final AuthService authService = new AuthService();
    private final TransactionService transactionService =
            new TransactionService();

    public void show(Stage stage, User user) {

        Label title = new Label("Transaction History");
        title.getStyleClass().add("page-title");

        Label subtitle = new Label(
                "Your recent transactions"
        );
        subtitle.getStyleClass().add("page-subtitle");

        VBox transactionList = new VBox(10);
        transactionList.setMaxWidth(Double.MAX_VALUE);

        ScrollPane scrollPane = new ScrollPane(transactionList);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );
        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );
        scrollPane.setMaxHeight(480);
        scrollPane.setPrefHeight(480);
        scrollPane.setStyle(
                "-fx-background-color: transparent;"
                        + "-fx-background: transparent;"
        );

        // Get transaction history
        Account account = authService.getAccount(user);

        if (account != null) {

            List<Transaction> transactions =
                    transactionService.getTransactionHistory(user);

            if (transactions.isEmpty()) {

                Label emptyMessage = new Label(
                        "No transactions yet."
                );

                emptyMessage.getStyleClass().add(
                        "page-subtitle"
                );

                transactionList.getChildren().add(
                        emptyMessage
                );

            } else {

                DateTimeFormatter formatter =
                        DateTimeFormatter.ofPattern(
                                "MMM d, yyyy h:mm a"
                        );

                for (Transaction transaction : transactions) {

                    boolean sent =
                            transaction.getSenderAccountId()
                                    == account.getId();

                    Label typeLabel = new Label(
                            sent ? "SENT" : "RECEIVED"
                    );

                    typeLabel.getStyleClass().add(
                            sent
                                    ? "transaction-sent"
                                    : "transaction-received"
                    );

                    BigDecimal amount =
                            transaction.getAmount();

                    Label amountLabel = new Label(
                            (sent ? "-₱" : "+₱")
                                    + String.format(
                                    "%,.2f",
                                    amount
                            )
                    );

                    amountLabel.getStyleClass().add(
                            "transaction-amount"
                    );

                    Label dateLabel = new Label(
                            transaction
                                    .getCreatedAt()
                                    .format(formatter)
                    );

                    dateLabel.getStyleClass().add(
                            "transaction-date"
                    );

                    Label referenceLabel = new Label(
                            "Ref: "
                                    + transaction.getReferenceNo()
                    );

                    referenceLabel.getStyleClass().add(
                            "transaction-reference"
                    );

                    VBox transactionCard = new VBox(
                            5,
                            typeLabel,
                            amountLabel,
                            dateLabel,
                            referenceLabel
                    );

                    transactionCard.getStyleClass().add(
                            "transaction-card"
                    );

                    transactionList.getChildren().add(
                            transactionCard
                    );
                }
            }
        }

        // Back to dashboard
        Button backButton = new Button("BACK");
        backButton.setMaxWidth(Double.MAX_VALUE);
        backButton.getStyleClass().add("dashboard-button");

        backButton.setOnAction(event ->
                new jcash.JCashApplication()
                        .showDashboard(stage, user)
        );

        // Main layout
        VBox root = new VBox(
                15,
                title,
                subtitle,
                scrollPane,
                backButton
        );

        root.setPadding(
                new Insets(30, 25, 25, 25)
        );

        root.setAlignment(Pos.TOP_CENTER);
        root.getStyleClass().add("dashboard");

        Scene scene = new Scene(
                root,
                390,
                700
        );

        scene.getStylesheets().add(
                getClass()
                        .getResource("/style.css")
                        .toExternalForm()
        );

        stage.setTitle("Transaction History");
        stage.setScene(scene);
        stage.show();
    }
}
