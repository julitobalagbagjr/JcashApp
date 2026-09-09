package jcash.ui;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import jcash.model.Account;
import jcash.model.User;
import jcash.service.AuthService;

public class AccountInformationPageFX {

    private final AuthService authService = new AuthService();

    public void show(Stage stage, User user) {

        // Get account information
        Account account = authService.getAccount(user);

        Label title = new Label("Account Information");
        title.getStyleClass().add("page-title");

        Label subtitle = new Label(
                "Your JCash account details"
        );
        subtitle.getStyleClass().add("page-subtitle");

        Label nameLabel = new Label("Full Name");
        nameLabel.getStyleClass().add("info-label");

        Label nameValue = new Label(user.getName());
        nameValue.getStyleClass().add("info-value");

        Label mobileLabel = new Label("Mobile Number");
        mobileLabel.getStyleClass().add("info-label");

        Label mobileValue = new Label(user.getNumber());
        mobileValue.getStyleClass().add("info-value");

        Label emailLabel = new Label("Email");
        emailLabel.getStyleClass().add("info-label");

        Label emailValue = new Label(user.getEmail());
        emailValue.getStyleClass().add("info-value");

        BigDecimal balance = account.getBalance();

        Label balanceLabel = new Label("Current Balance");
        balanceLabel.getStyleClass().add("info-label");

        Label balanceValue = new Label(
                "₱" + String.format("%,.2f", balance)
        );
        balanceValue.getStyleClass().add("info-balance");

        DateTimeFormatter dateFormatter =
                DateTimeFormatter.ofPattern("MMMM d, yyyy");

        Label createdLabel = new Label("Account Created");
        createdLabel.getStyleClass().add("info-label");

        Label createdValue = new Label(
                account.getCreatedAt().format(dateFormatter)
        );
        createdValue.getStyleClass().add("info-value");

        // Account information card
        VBox informationCard = new VBox(
                8,
                nameLabel,
                nameValue,
                mobileLabel,
                mobileValue,
                emailLabel,
                emailValue,
                balanceLabel,
                balanceValue,
                createdLabel,
                createdValue
        );

        informationCard.getStyleClass().add("information-card");

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
                15,
                title,
                subtitle,
                informationCard,
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

        stage.setTitle("Account Information");
        stage.setScene(scene);
        stage.show();
    }
}
