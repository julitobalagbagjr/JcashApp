package jcash.ui;

import java.math.BigDecimal;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import jcash.model.Transaction;
import jcash.model.User;
import jcash.service.TransactionService;

public class TransferPageFX {

    private final TransactionService transactionService =
            new TransactionService();

    public void show(Stage stage, User user) {

        Label title = new Label("Send Money");
        title.getStyleClass().add("page-title");

        Label instruction = new Label(
                "Send money to another JCash user"
        );
        instruction.getStyleClass().add("page-subtitle");

        Label recipientLabel = new Label(
                "Recipient Mobile Number"
        );
        recipientLabel.getStyleClass().add("field-label");

        TextField recipientField = new TextField();
        recipientField.setPromptText("09XXXXXXXXX");
        recipientField.getStyleClass().add("text-field");

        Label amountLabel = new Label("Amount");
        amountLabel.getStyleClass().add("field-label");

        TextField amountField = new TextField();
        amountField.setPromptText("₱0.00");
        amountField.getStyleClass().add("text-field");

        Label message = new Label();
        message.getStyleClass().add("error-message");

        Button sendButton = new Button("SEND MONEY");
        sendButton.setMaxWidth(Double.MAX_VALUE);
        sendButton.getStyleClass().add("login-button");

        Button backButton = new Button("BACK");
        backButton.setMaxWidth(Double.MAX_VALUE);
        backButton.getStyleClass().add("dashboard-button");

        // Handle money transfer
        sendButton.setOnAction(event -> {

            String recipientNumber =
                    recipientField.getText().trim();

            String amountText =
                    amountField.getText().trim();

            message.setText("");

            if (recipientNumber.isEmpty()) {

                message.setText(
                        "Please enter the recipient mobile number."
                );

                return;
            }

            if (amountText.isEmpty()) {

                message.setText(
                        "Please enter an amount."
                );

                return;
            }

            BigDecimal amount;

            try {
                amount = new BigDecimal(amountText);
            } catch (NumberFormatException e) {

                message.setText(
                        "Please enter a valid amount."
                );

                return;
            }

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {

                message.setText(
                        "Amount must be greater than zero."
                );

                return;
            }

            if (amount.scale() > 2) {

                message.setText(
                        "Amount can only have 2 decimal places."
                );

                return;
            }

            Transaction transaction =
                    transactionService.transfer(
                            user,
                            recipientNumber,
                            amount
                    );

            if (transaction != null) {

                new TransferReceiptPageFX().show(
                        stage,
                        user,
                        recipientNumber,
                        transaction
                );

            } else {

                message.setText(
                        "Transfer failed. Check the recipient and your balance."
                );
            }
        });

        // Return to dashboard
        backButton.setOnAction(event ->
                new jcash.JCashApplication()
                        .showDashboard(stage, user)
        );

        // Main layout
        VBox layout = new VBox(
                12,
                title,
                instruction,
                recipientLabel,
                recipientField,
                amountLabel,
                amountField,
                message,
                sendButton,
                backButton
        );

        layout.setPadding(
                new Insets(30, 25, 25, 25)
        );

        layout.setAlignment(Pos.TOP_CENTER);
        layout.getStyleClass().add("dashboard");

        Scene scene = new Scene(
                layout,
                390,
                700
        );

        scene.getStylesheets().add(
                getClass()
                        .getResource("/style.css")
                        .toExternalForm()
        );

        stage.setTitle("Send Money");
        stage.setScene(scene);
        stage.show();
    }
}
