package jcash.ui;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import jcash.model.Transaction;
import jcash.model.User;

public class TransferReceiptPageFX {

    public void show(
            Stage stage,
            User user,
            String recipientNumber,
            Transaction transaction
    ) {

        // Success message
        Label success = new Label("✓");
        success.getStyleClass().add("receipt-success-icon");

        Label title = new Label("Transfer Successful");
        title.getStyleClass().add("receipt-title");

        Label subtitle = new Label(
                "Your money has been sent successfully."
        );
        subtitle.getStyleClass().add("page-subtitle");

        // Amount
        Label amountLabel = new Label("Amount Sent");
        amountLabel.getStyleClass().add("receipt-label");

        BigDecimal amount = transaction.getAmount();

        Label amountValue = new Label(
                "₱" + String.format("%,.2f", amount)
        );
        amountValue.getStyleClass().add("receipt-amount");

        // Recipient
        Label recipientLabel = new Label(
                "Recipient Mobile Number"
        );
        recipientLabel.getStyleClass().add("receipt-label");

        Label recipientValue = new Label(recipientNumber);
        recipientValue.getStyleClass().add("receipt-value");

        // Reference number
        Label referenceLabel = new Label(
                "Reference Number"
        );
        referenceLabel.getStyleClass().add("receipt-label");

        Label referenceValue = new Label(
                transaction.getReferenceNo()
        );
        referenceValue.getStyleClass().add("receipt-value");

        // Date and time
        Label dateLabel = new Label("Date & Time");
        dateLabel.getStyleClass().add("receipt-label");

        LocalDateTime dateTime =
                transaction.getCreatedAt();

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "MMMM d, yyyy h:mm a"
                );

        Label dateValue = new Label(
                dateTime.format(formatter)
        );
        dateValue.getStyleClass().add("receipt-value");

        // Return to dashboard
        Button doneButton = new Button("DONE");
        doneButton.setMaxWidth(Double.MAX_VALUE);
        doneButton.getStyleClass().add("login-button");

        doneButton.setOnAction(event ->
                new jcash.JCashApplication()
                        .showDashboard(stage, user)
        );

        // Receipt card
        VBox receiptCard = new VBox(
                10,
                success,
                title,
                subtitle,
                amountLabel,
                amountValue,
                recipientLabel,
                recipientValue,
                referenceLabel,
                referenceValue,
                dateLabel,
                dateValue,
                doneButton
        );

        receiptCard.setAlignment(Pos.CENTER);
        receiptCard.setPadding(new Insets(30));
        receiptCard.getStyleClass().add("receipt-card");

        // Main layout
        VBox root = new VBox(receiptCard);

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(25));
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

        stage.setTitle("Transfer Receipt");
        stage.setScene(scene);
        stage.show();
    }
}