package jcash;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import jcash.model.Account;
import jcash.model.User;
import jcash.service.AuthService;

public class JCashApplication extends Application {

    private final AuthService authService = new AuthService();

    @Override
    public void start(Stage stage) {

        // JCash logo
        Circle logoCircle = new Circle(35);
        logoCircle.getStyleClass().add("logo-circle");

        Label logoText = new Label("J");
        logoText.getStyleClass().add("logo-text");

        StackPane logo = new StackPane(
                logoCircle,
                logoText
        );

        // App title
        Label title = new Label("JCASH");
        title.getStyleClass().add("title");

        Label subtitle = new Label("Mobile Banking");
        subtitle.getStyleClass().add("subtitle");

        // Login form
        Label welcome = new Label("Welcome back!");
        welcome.getStyleClass().add("welcome");

        Label instruction = new Label(
                "Login to access your account"
        );
        instruction.getStyleClass().add("instruction");

        Label mobileLabel = new Label("Mobile Number");
        mobileLabel.getStyleClass().add("field-label");

        TextField mobileField = new TextField();
        mobileField.setPromptText("09XXXXXXXXX");
        mobileField.getStyleClass().add("text-field");

        Label pinLabel = new Label("PIN");
        pinLabel.getStyleClass().add("field-label");

        PasswordField pinField = new PasswordField();
        pinField.setPromptText("Enter your 4-digit PIN");
        pinField.getStyleClass().add("password-field");

        Button forgotPinButton = new Button("Forgot PIN?");
        forgotPinButton.getStyleClass().add("forgot-pin-button");

        forgotPinButton.setOnAction(event -> {
            new jcash.ui.ResetPinPageFX()
                    .show(stage);
        });

        Label errorMessage = new Label();
        errorMessage.getStyleClass().add("error-message");

        Button loginButton = new Button("LOGIN");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.getStyleClass().add("login-button");

        // Handle login
        loginButton.setOnAction(event -> {

            String number = mobileField.getText().trim();
            String pin = pinField.getText().trim();

            errorMessage.setText("");

            if (number.isEmpty() || pin.isEmpty()) {
                errorMessage.setText(
                        "Please enter your mobile number and PIN."
                );
                return;
            }

            User user = authService.login(number, pin);

            if (user != null) {
                showDashboard(stage, user);
            } else {
                errorMessage.setText(
                        "Invalid mobile number or PIN."
                );
                pinField.clear();
            }
        });

        // Sign up
        Label signupText = new Label(
                "Don't have an account?"
        );
        signupText.getStyleClass().add("signup-text");

        Button signupButton = new Button("SIGN UP");
        signupButton.getStyleClass().add("signup-button");

        signupButton.setOnAction(event -> {
            new jcash.ui.SignupPageFX()
                    .show(stage);
        });

        VBox signupBox = new VBox(
                3,
                signupText,
                signupButton
        );
        signupBox.setAlignment(Pos.CENTER);

        // Login card
        VBox loginCard = new VBox();

        loginCard.setSpacing(10);
        loginCard.setPadding(
                new Insets(25, 25, 25, 25)
        );

        loginCard.getStyleClass().add("login-card");

        loginCard.getChildren().addAll(
                welcome,
                instruction,
                mobileLabel,
                mobileField,
                pinLabel,
                pinField,
                forgotPinButton,
                errorMessage,
                loginButton,
                signupBox
        );

        // Main layout
        VBox root = new VBox();

        root.setSpacing(15);
        root.setPadding(
                new Insets(30, 25, 25, 25)
        );
        root.setAlignment(Pos.TOP_CENTER);

        root.getChildren().addAll(
                logo,
                title,
                subtitle,
                loginCard
        );

        // Login scene
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

        stage.setTitle("JCash Mobile Banking");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    // Dashboard
    public void showDashboard(Stage stage, User user) {

        Account account =
                authService.getAccount(user);

        // Header
        Label appTitle = new Label("JCASH");
        appTitle.getStyleClass().add("dashboard-title");

        Label greeting = new Label(
                "Hello, " + user.getName() + "!"
        );
        greeting.getStyleClass().add("dashboard-greeting");

        // Balance
        Label balanceLabel = new Label(
                "Available Balance"
        );
        balanceLabel.getStyleClass().add("balance-label");

        Label balance = new Label(
                "₱" + String.format(
                        "%,.2f",
                        account.getBalance()
                )
        );
        balance.getStyleClass().add("balance-amount");

        Label balanceHint = new Label(
                "Your current account balance"
        );
        balanceHint.getStyleClass().add("balance-hint");

        VBox balanceCard = new VBox(
                7,
                balanceLabel,
                balance,
                balanceHint
        );
        balanceCard.getStyleClass().add(
                "balance-card"
        );

        // Quick actions
        Label menuTitle = new Label(
                "Quick Actions"
        );
        menuTitle.getStyleClass().add(
                "menu-title"
        );

        Button sendMoneyButton =
                new Button("Send Money");

        sendMoneyButton.setPrefWidth(160);
        sendMoneyButton.setMinWidth(160);
        sendMoneyButton.setMaxWidth(160);

        sendMoneyButton.getStyleClass().add(
                "action-button"
        );

        sendMoneyButton.setOnAction(event -> {
            new jcash.ui.TransferPageFX()
                    .show(stage, user);
        });

        Button changePinButton =
                new Button("Change PIN");

        changePinButton.setPrefWidth(160);
        changePinButton.setMinWidth(160);
        changePinButton.setMaxWidth(160);

        changePinButton.getStyleClass().add(
                "action-button"
        );

        changePinButton.setOnAction(event -> {
            new jcash.ui.ChangePinPageFX()
                    .show(stage, user);
        });

        Button historyButton =
                new Button("Transaction\nHistory");

        historyButton.setWrapText(true);
        historyButton.setPrefWidth(160);
        historyButton.setMinWidth(160);
        historyButton.setMaxWidth(160);

        historyButton.getStyleClass().add(
                "action-button"
        );

        historyButton.setOnAction(event -> {
            new jcash.ui.TransactionHistoryPageFX()
                    .show(stage, user);
        });

        Button accountButton =
                new Button("Account\nInformation");

        accountButton.setWrapText(true);
        accountButton.setPrefWidth(160);
        accountButton.setMinWidth(160);
        accountButton.setMaxWidth(160);

        accountButton.getStyleClass().add(
                "action-button"
        );

        accountButton.setOnAction(event -> {
            new jcash.ui.AccountInformationPageFX()
                    .show(stage, user);
        });

        HBox quickActionsRow1 = new HBox(
                12,
                sendMoneyButton,
                changePinButton
        );
        quickActionsRow1.setAlignment(
                Pos.CENTER
        );

        HBox quickActionsRow2 = new HBox(
                12,
                historyButton,
                accountButton
        );
        quickActionsRow2.setAlignment(
                Pos.CENTER
        );

        VBox quickActions = new VBox(
                12,
                quickActionsRow1,
                quickActionsRow2
        );
        quickActions.setAlignment(
                Pos.CENTER
        );

        // Logout
        Button logoutButton =
                new Button("Logout");

        logoutButton.getStyleClass().add(
                "bottom-nav-button"
        );

        logoutButton.setPrefWidth(200);
        logoutButton.setMinWidth(200);
        logoutButton.setMaxWidth(200);

        logoutButton.setOnAction(event -> {
            start(stage);
        });

        // Dashboard content
        VBox content = new VBox(
                15,
                appTitle,
                greeting,
                balanceCard,
                menuTitle,
                quickActions
        );

        content.setMaxHeight(
                Region.USE_PREF_SIZE
        );
        content.setAlignment(
                Pos.TOP_CENTER
        );

        content.setPadding(
                new Insets(30, 0, 20, 0)
        );

        HBox bottomArea = new HBox(
                logoutButton
        );

        bottomArea.setAlignment(Pos.CENTER);
        bottomArea.setPadding(
                new Insets(10, 0, 0, 0)
        );

        // Dashboard layout
        BorderPane dashboard =
                new BorderPane();

        dashboard.setCenter(content);
        dashboard.setBottom(bottomArea);

        dashboard.setPadding(
                new Insets(0, 25, 10, 25)
        );

        dashboard.getStyleClass().add(
                "dashboard"
        );

        // Dashboard scene
        Scene scene = new Scene(
                dashboard,
                390,
                700
        );

        scene.getStylesheets().add(
                getClass()
                        .getResource("/style.css")
                        .toExternalForm()
        );

        stage.setTitle("JCash Dashboard");
        stage.setScene(scene);
        stage.show();
    }
}