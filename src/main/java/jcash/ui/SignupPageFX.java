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

import jcash.service.AuthService;
import jcash.util.InputValidator;

public class SignupPageFX {

    private final AuthService authService = new AuthService();

    public void show(Stage stage) {

        // Title
        Label title = new Label("Create Account");
        title.getStyleClass().add("page-title");

        Label subtitle = new Label(
                "Create your JCash mobile banking account"
        );
        subtitle.getStyleClass().add("page-subtitle");

        // Name
        Label nameLabel = new Label("Name");
        nameLabel.getStyleClass().add("field-label");

        TextField nameField = new TextField();
        nameField.setPromptText("Enter your name");
        nameField.getStyleClass().add("text-field");

        // Email
        Label emailLabel = new Label("Email");
        emailLabel.getStyleClass().add("field-label");

        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.getStyleClass().add("text-field");

        // Mobile number
        Label mobileLabel = new Label("Mobile Number");
        mobileLabel.getStyleClass().add("field-label");

        TextField mobileField = new TextField();
        mobileField.setPromptText("09XXXXXXXXX");
        mobileField.getStyleClass().add("text-field");

        // PIN
        Label pinLabel = new Label("PIN");
        pinLabel.getStyleClass().add("field-label");

        PasswordField pinField = new PasswordField();
        pinField.setPromptText("Enter your 4-digit PIN");
        pinField.getStyleClass().add("password-field");

        // Confirm PIN
        Label confirmPinLabel = new Label("Confirm PIN");
        confirmPinLabel.getStyleClass().add("field-label");

        PasswordField confirmPinField = new PasswordField();
        confirmPinField.setPromptText("Confirm your 4-digit PIN");
        confirmPinField.getStyleClass().add("password-field");

        Label message = new Label();
        message.setWrapText(true);

        // Sign up button
        Button signupButton = new Button("SIGN UP");
        signupButton.setMaxWidth(Double.MAX_VALUE);
        signupButton.getStyleClass().add("login-button");

        // Handle sign up
        signupButton.setOnAction(event -> {

            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String number = mobileField.getText().trim();
            String pin = pinField.getText().trim();
            String confirmPin = confirmPinField.getText().trim();

            message.setText("");

            // Check empty fields
            if (name.isEmpty()
                    || email.isEmpty()
                    || number.isEmpty()
                    || pin.isEmpty()
                    || confirmPin.isEmpty()) {

                message.getStyleClass().clear();
                message.getStyleClass().add("error-message");

                message.setText(
                        "Please complete all fields."
                );

                return;
            }

            // Check email
            if (!InputValidator.isValidEmail(email)) {

                message.getStyleClass().clear();
                message.getStyleClass().add("error-message");

                message.setText(
                        "Please enter a valid email."
                );

                return;
            }

            // Check mobile number
            if (!InputValidator.isValidMobileNumber(number)) {

                message.getStyleClass().clear();
                message.getStyleClass().add("error-message");

                message.setText(
                        "Mobile number must start with 09 "
                                + "and contain 11 digits."
                );

                return;
            }

            // Check PIN
            if (!InputValidator.isValidPIN(pin)) {

                message.getStyleClass().clear();
                message.getStyleClass().add("error-message");

                message.setText(
                        "PIN must contain exactly 4 digits."
                );

                return;
            }

            // Check PIN confirmation
            if (!pin.equals(confirmPin)) {

                message.getStyleClass().clear();
                message.getStyleClass().add("error-message");

                message.setText(
                        "PINs do not match."
                );

                return;
            }

            // Create account
            boolean success = authService.signup(
                    name,
                    email,
                    number,
                    pin
            );

            if (success) {

                message.getStyleClass().clear();
                message.getStyleClass().add("success-message");

                message.setText(
                        "Account created successfully!"
                );

                nameField.clear();
                emailField.clear();
                mobileField.clear();
                pinField.clear();
                confirmPinField.clear();

            } else {

                message.getStyleClass().clear();
                message.getStyleClass().add("error-message");

                message.setText(
                        "Unable to create account. "
                                + "The mobile number may already "
                                + "be registered."
                );
            }
        });

        // Back to login
        Button backButton = new Button("BACK TO LOGIN");
        backButton.setMaxWidth(Double.MAX_VALUE);
        backButton.getStyleClass().add("login-button");

        backButton.setOnAction(event ->
                new jcash.JCashApplication()
                        .start(stage)
        );

        // Main layout
        VBox layout = new VBox(
                10,
                title,
                subtitle,
                nameLabel,
                nameField,
                emailLabel,
                emailField,
                mobileLabel,
                mobileField,
                pinLabel,
                pinField,
                confirmPinLabel,
                confirmPinField,
                message,
                signupButton,
                backButton
        );

        layout.setPadding(
                new Insets(25, 25, 25, 25)
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

        stage.setTitle("JCash - Sign Up");
        stage.setScene(scene);
        stage.show();
    }
}