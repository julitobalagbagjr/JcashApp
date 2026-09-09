package jcash.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import jcash.model.User;
import jcash.service.AuthService;

public class ResetPinPageFX {

    private final AuthService authService = new AuthService();

    public void show(Stage stage) {

        // Account verification
        Label title = new Label("RESET PIN");
        title.getStyleClass().add("page-title");

        Label subtitle = new Label(
                "Verify your account to reset your PIN"
        );
        subtitle.getStyleClass().add("page-subtitle");

        Label mobileLabel = new Label("Mobile Number");
        mobileLabel.getStyleClass().add("field-label");

        TextField mobileField = new TextField();
        mobileField.setPromptText("09XXXXXXXXX");
        mobileField.getStyleClass().add("text-field");

        Label emailLabel = new Label("Email Address");
        emailLabel.getStyleClass().add("field-label");

        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.getStyleClass().add("text-field");

        Label errorMessage = new Label();
        errorMessage.getStyleClass().add("error-message");

        Button continueButton = new Button("CONTINUE");
        continueButton.setMaxWidth(Double.MAX_VALUE);
        continueButton.getStyleClass().add("login-button");

        // Verify recovery details
        continueButton.setOnAction(event -> {

            String number = mobileField.getText().trim();
            String email = emailField.getText().trim();

            errorMessage.setText("");

            if (number.isEmpty() || email.isEmpty()) {

                errorMessage.setText(
                        "Please enter your mobile number and email."
                );

                return;
            }

            User user = authService.verifyRecoveryDetails(
                    number,
                    email
            );

            if (user == null) {

                errorMessage.setText(
                        "Mobile number or email is incorrect."
                );

                return;
            }

            showNewPINPage(stage, user);
        });

        // Back to login
        Button backButton = new Button("BACK");
        backButton.getStyleClass().add("forgot-pin-button");

        backButton.setOnAction(event ->
                new jcash.JCashApplication()
                        .start(stage)
        );

        // Verification card
        VBox card = new VBox(
                10,
                title,
                subtitle,
                mobileLabel,
                mobileField,
                emailLabel,
                emailField,
                errorMessage,
                continueButton,
                backButton
        );

        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(25));
        card.setMaxWidth(340);
        card.getStyleClass().add("login-card");

        // Main layout
        VBox root = new VBox(card);

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

        stage.setTitle("JCash - Reset PIN");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void showNewPINPage(
            Stage stage,
            User user
    ) {

        // New PIN form
        Label title = new Label("RESET PIN");
        title.getStyleClass().add("page-title");

        Label subtitle = new Label(
                "Create your new 4-digit PIN"
        );
        subtitle.getStyleClass().add("page-subtitle");

        Label newPinLabel = new Label("New PIN");
        newPinLabel.getStyleClass().add("field-label");

        PasswordField newPinField = new PasswordField();
        newPinField.setPromptText(
                "Enter your new 4-digit PIN"
        );
        newPinField.getStyleClass().add("password-field");

        Label confirmPinLabel = new Label("Confirm PIN");
        confirmPinLabel.getStyleClass().add("field-label");

        PasswordField confirmPinField = new PasswordField();
        confirmPinField.setPromptText(
                "Confirm your new PIN"
        );
        confirmPinField.getStyleClass().add("password-field");

        Label message = new Label();
        message.getStyleClass().add("error-message");

        Button resetButton = new Button("RESET PIN");
        resetButton.setMaxWidth(Double.MAX_VALUE);
        resetButton.getStyleClass().add("login-button");

        // Reset PIN
        resetButton.setOnAction(event -> {

            String newPIN = newPinField.getText().trim();
            String confirmPIN = confirmPinField.getText().trim();

            message.setText("");

            if (newPIN.isEmpty() || confirmPIN.isEmpty()) {

                message.setText(
                        "Please enter and confirm your new PIN."
                );

                return;
            }

            if (!newPIN.matches("\\d{4}")) {

                message.setText(
                        "PIN must be exactly 4 digits."
                );

                return;
            }

            if (!newPIN.equals(confirmPIN)) {

                message.setText(
                        "PINs do not match."
                );

                return;
            }

            boolean updated = authService.resetPIN(
                    user,
                    newPIN
            );

            if (!updated) {

                message.setText(
                        "Unable to reset PIN. Please try again."
                );

                return;
            }

            showSuccessPage(stage);
        });

        // Back to verification
        Button backButton = new Button("BACK");
        backButton.getStyleClass().add("forgot-pin-button");

        backButton.setOnAction(event ->
                show(stage)
        );

        // PIN card
        VBox card = new VBox(
                10,
                title,
                subtitle,
                newPinLabel,
                newPinField,
                confirmPinLabel,
                confirmPinField,
                message,
                resetButton,
                backButton
        );

        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(25));
        card.setMaxWidth(340);
        card.getStyleClass().add("login-card");

        // Main layout
        VBox root = new VBox(card);

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

        stage.setTitle("JCash - Reset PIN");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void showSuccessPage(Stage stage) {

        // Success message
        Label title = new Label("✓");
        title.getStyleClass().add("receipt-success-icon");

        Label successTitle = new Label(
                "PIN RESET SUCCESSFUL"
        );
        successTitle.getStyleClass().add("receipt-title");

        Label message = new Label(
                "Your PIN has been updated.\n"
                        + "You can now log in with your new PIN."
        );
        message.setWrapText(true);
        message.getStyleClass().add("page-subtitle");

        // Return to login
        Button loginButton = new Button("BACK TO LOGIN");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.getStyleClass().add("login-button");

        loginButton.setOnAction(event ->
                new jcash.JCashApplication()
                        .start(stage)
        );

        // Success card
        VBox card = new VBox(
                15,
                title,
                successTitle,
                message,
                loginButton
        );

        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(30));
        card.setMaxWidth(340);
        card.getStyleClass().add("receipt-card");

        // Main layout
        VBox root = new VBox(card);

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

        stage.setTitle("JCash - PIN Reset");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
