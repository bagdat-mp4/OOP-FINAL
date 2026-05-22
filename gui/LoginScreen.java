package gui;

import controllers.AuthController;
import exceptions.InvalidCredentialsException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import models.Admin;
import models.GraduateStudent;
import models.Manager;
import models.Student;
import models.Teacher;
import models.TechSupportSpecialist;
import models.User;


public class LoginScreen {

    private static final String BTN_NORMAL =
        "-fx-background-color: #4a90d9; -fx-text-fill: white; -fx-font-size: 16;" +
        "-fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;";
    private static final String BTN_HOVER =
        "-fx-background-color: #357abd; -fx-text-fill: white; -fx-font-size: 16;" +
        "-fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;";
    private static final String FIELD_STYLE =
        "-fx-padding: 10; -fx-border-color: #ddd; -fx-border-radius: 8;" +
        "-fx-background-radius: 8; -fx-font-size: 14;";

    private final Stage stage;
    private final AuthController auth = new AuthController();

    public LoginScreen(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1a1a2e;");
        root.setPadding(new Insets(0));

        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(40));
        card.setMaxWidth(400);
        card.setStyle(
            "-fx-background-color: white; -fx-background-radius: 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0, 0, 5);"
        );

        Label title = new Label("KBTU University");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#1a1a2e"));

        Label subtitle = new Label("Research System");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setTextFill(Color.web("#666666"));

        Label emailLabel = new Label("Email");
        emailLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        TextField emailField = new TextField();
        emailField.setPromptText("example@uni.kz");
        emailField.setStyle(FIELD_STYLE);
        emailField.setPrefWidth(320);

        Label passLabel = new Label("Password");
        passLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        PasswordField passField = new PasswordField();
        passField.setPromptText("password");
        passField.setStyle(FIELD_STYLE);
        passField.setPrefWidth(320);

        Label errorLabel = new Label("");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font("Arial", 12));

        Button loginBtn = new Button("Login");
        loginBtn.setPrefWidth(320);
        loginBtn.setPrefHeight(45);
        loginBtn.setStyle(BTN_NORMAL);
        loginBtn.setOnMouseEntered(e -> loginBtn.setStyle(BTN_HOVER));
        loginBtn.setOnMouseExited(e -> loginBtn.setStyle(BTN_NORMAL));

        Label hint = new Label("Test: admin@uni.kz / admin123");
        hint.setFont(Font.font("Arial", 11));
        hint.setTextFill(Color.web("#aaaaaa"));

        loginBtn.setOnAction(e -> {
            String email = emailField.getText().trim();
            String password = passField.getText().trim();
            if (email.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please fill all fields");
                return;
            }
            try {
                openDashboard(auth.login(email, password));
            } catch (InvalidCredentialsException ex) {
                errorLabel.setText(ex.getMessage());
                passField.clear();
            }
        });

        passField.setOnAction(e -> loginBtn.fire());

        card.getChildren().addAll(
            title, subtitle,
            new Separator(),
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
        if (user instanceof Admin) {
            new AdminDashboard(stage, (Admin) user).show();
        } else if (user instanceof GraduateStudent) {
            new GraduateStudentDashboard(stage, (GraduateStudent) user).show();
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
