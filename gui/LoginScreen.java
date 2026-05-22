package gui;

import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.scene.paint.Color;
import controllers.AuthController;
import models.*;

/**
 * Login screen with modern dark theme design
 */
public class LoginScreen {
    private Stage stage;

    public LoginScreen(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1a1a2e;");
        root.setPadding(new Insets(0));

        // Card container
        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(40));
        card.setMaxWidth(400);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0, 0, 5);"
        );

        // Title
        Label title = new Label("🎓 KBTU University");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#1a1a2e"));

        Label subtitle = new Label("Research System");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setTextFill(Color.web("#666666"));

        // Email field
        Label emailLabel = new Label("Email");
        emailLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        TextField emailField = new TextField();
        emailField.setPromptText("example@uni.kz");
        emailField.setStyle(
            "-fx-padding: 10;" +
            "-fx-border-color: #ddd;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-font-size: 14;"
        );
        emailField.setPrefWidth(320);

        // Password field
        Label passLabel = new Label("Password");
        passLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        PasswordField passField = new PasswordField();
        passField.setPromptText("••••••••");
        passField.setStyle(emailField.getStyle());
        passField.setPrefWidth(320);

        // Error label
        Label errorLabel = new Label("");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font("Arial", 12));

        // Login button
        Button loginBtn = new Button("Login");
        loginBtn.setPrefWidth(320);
        loginBtn.setPrefHeight(45);
        loginBtn.setStyle(
            "-fx-background-color: #4a90d9;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
        );

        // Hover effects
        loginBtn.setOnMouseEntered(e -> loginBtn.setStyle(
            "-fx-background-color: #357abd;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
        ));
        loginBtn.setOnMouseExited(e -> loginBtn.setStyle(
            "-fx-background-color: #4a90d9;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
        ));

        // Hint for test accounts
        Label hint = new Label("Test: admin@uni.kz / admin123");
        hint.setFont(Font.font("Arial", 11));
        hint.setTextFill(Color.web("#aaaaaa"));

        // Login action
        loginBtn.setOnAction(e -> {
            String email = emailField.getText().trim();
            String password = passField.getText().trim();

            if (email.isEmpty() || password.isEmpty()) {
                errorLabel.setText("⚠ Please fill all fields");
                return;
            }

            AuthController auth = new AuthController();
            User user = auth.login(email, password);

            if (user == null) {
                errorLabel.setText("❌ Invalid email or password");
                passField.clear();
            } else {
                openDashboard(user);
            }
        });

        // Enter key support
        passField.setOnAction(e -> loginBtn.fire());

        // Separator
        Separator separator = new Separator();
        separator.setPrefWidth(300);

        card.getChildren().addAll(
            title, subtitle,
            separator,
            emailLabel, emailField,
            passLabel, passField,
            errorLabel,
            loginBtn,
            hint
        );

        root.getChildren().add(card);

        Scene scene = new Scene(root, 600, 500);
        stage.setTitle("KBTU University System");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void openDashboard(User user) {
        // Route to appropriate dashboard based on role
        if (user instanceof Admin) {
            new AdminDashboard(stage, (Admin) user).show();
        } else if (user instanceof GraduateStudent) {
            new StudentDashboard(stage, (Student) user).show();
        } else if (user instanceof Student) {
            new StudentDashboard(stage, (Student) user).show();
        } else if (user instanceof Teacher) {
            new TeacherDashboard(stage, (Teacher) user).show();
        } else if (user instanceof Manager) {
            new ManagerDashboard(stage, (Manager) user).show();
        } else if (user instanceof TechSupportSpecialist) {
            new TechSupportDashboard(stage, (TechSupportSpecialist) user).show();
        }
    }
}
