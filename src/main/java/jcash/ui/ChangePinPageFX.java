package jcash.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import jcash.model.User;
import jcash.service.AuthService;

public class ChangePinPageFX {

    private final AuthService authService = new AuthService();

    public void show(Stage stage, User user) {

        // Title
        Label title = new Label("Change PIN");
        title.getStyleClass().add("page-title");

        Label subtitle = new Label(
                "Update your 4-digit security PIN"
        );
        subtitle.getStyleClass().add("page-subtitle");

        // Current PIN
        Label currentPinLabel = new Label("Current PIN");
        currentPinLabel.getStyleClass().add("field-label");

        PasswordField currentPinField = new PasswordField();
        currentPinField.setPromptText("Enter current PIN");
        currentPinField.getStyleClass().add("password-field");

        // New PIN
        Label newPinLabel = new Label("New PIN");
        newPinLabel.getStyleClass().add("field-label");

        PasswordField newPinField = new PasswordField();
        newPinField.setPromptText("Enter new 4-digit PIN");
        newPinField.getStyleClass().add("password-field");

        // Confirm PIN
        Label confirmPinLabel = new Label("Confirm New PIN");
        confirmPinLabel.getStyleClass().add("field-label");

        PasswordField confirmPinField = new PasswordField();
        confirmPinField.setPromptText("Confirm new PIN");
        confirmPinField.getStyleClass().add("password-field");

        Label message = new Label();

        // Change PIN button
        Button changeButton = new Button("CHANGE PIN");
        changeButton.setMaxWidth(Double.MAX_VALUE);
        changeButton.getStyleClass().add("login-button");

        // Handle PIN change
        changeButton.setOnAction(event -> {

            String currentPin =
                    currentPinField.getText().trim();

            String newPin =
                    newPinField.getText().trim();

            String confirmPin =
                    confirmPinField.getText().trim();

            message.setText("");

            // Check empty fields
            if (currentPin.isEmpty()
                    || newPin.isEmpty()
                    || confirmPin.isEmpty()) {

                message.getStyleClass().clear();
                message.getStyleClass().add("error-message");

                message.setText(
                        "Please complete all fields."
                );

                return;
            }

            // Check PIN format
            if (!currentPin.matches("\\d{4}")
                    || !newPin.matches("\\d{4}")
                    || !confirmPin.matches("\\d{4}")) {

                message.getStyleClass().clear();
                message.getStyleClass().add("error-message");

                message.setText(
                        "PIN must contain exactly 4 digits."
                );

                return;
            }

            // Check confirmation
            if (!newPin.equals(confirmPin)) {

                message.getStyleClass().clear();
                message.getStyleClass().add("error-message");

                message.setText(
                        "New PINs do not match."
                );

                return;
            }

            // Make sure the new PIN is different
            if (currentPin.equals(newPin)) {

                message.getStyleClass().clear();
                message.getStyleClass().add("error-message");

                message.setText(
                        "New PIN must be different from current PIN."
                );

                return;
            }

            // Change PIN
            boolean success = authService.changePIN(
                    user,
                    currentPin,
                    newPin
            );

            if (success) {

                message.getStyleClass().clear();
                message.getStyleClass().add("success-message");

                message.setText(
                        "PIN changed successfully!"
                );

                currentPinField.clear();
                newPinField.clear();
                confirmPinField.clear();

            } else {

                message.getStyleClass().clear();
                message.getStyleClass().add("error-message");

                message.setText(
                        "Current PIN is incorrect."
                );
            }
        });

        // Back to dashboard
        Button backButton = new Button("BACK");
        backButton.setMaxWidth(Double.MAX_VALUE);
        backButton.getStyleClass().add("dashboard-button");

        backButton.setOnAction(event ->
                new jcash.JCashApplication()
                        .showDashboard(stage, user)
        );

        // Main layout
        VBox layout = new VBox(
                12,
                title,
                subtitle,
                currentPinLabel,
                currentPinField,
                newPinLabel,
                newPinField,
                confirmPinLabel,
                confirmPinField,
                message,
                changeButton,
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

        stage.setTitle("Change PIN");
        stage.setScene(scene);
        stage.show();
    }
}
