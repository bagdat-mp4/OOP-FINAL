package gui;

import controllers.AdminController;
import core.DataStore;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
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

import java.util.List;


public class AdminDashboard extends BaseDashboard {

    private final Admin admin;
    private final AdminController controller = new AdminController();

    public AdminDashboard(Stage stage, Admin admin) {
        super(stage, admin);
        this.admin = admin;
    }

    @Override
    public void show() {
        root = new javafx.scene.layout.BorderPane();
        root.setStyle("-fx-background-color: #f5f6fa;");
        root.setTop(createNavBar("Admin Portal"));

        VBox sidebar = new VBox(5);
        sidebar.setPadding(new Insets(20, 10, 20, 10));
        sidebar.setStyle("-fx-background-color: #16213e; -fx-min-width: 240;");

        VBox userInfo = new VBox(5);
        userInfo.setPadding(new Insets(10, 10, 20, 10));
        Label nameLabel = new Label(admin.getFirstName() + " " + admin.getLastName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        nameLabel.setTextFill(Color.WHITE);
        Label roleLabel = new Label("Administrator");
        roleLabel.setTextFill(Color.web("#aaaaaa"));
        userInfo.getChildren().addAll(nameLabel, roleLabel);

        Button dashBtn = createMenuButton("Dashboard", "");
        Button usersBtn = createMenuButton("Manage Users", "");
        Button logsBtn = createMenuButton("System Logs", "");
        Button createUserBtn = createMenuButton("Create User", "");
        Button setPassBtn     = createMenuButton("Set Password", "");
        Button techSupportBtn = createMenuButton("Tech Support", "");

        sidebar.getChildren().addAll(userInfo, new Separator(), dashBtn, usersBtn, logsBtn, createUserBtn, setPassBtn, techSupportBtn);

        if (core.DataStore.getInstance().isResearcher(admin)) {
            Button researchBtn = createMenuButton("Researcher Mode", "");
            researchBtn.setStyle(researchBtn.getStyle() + "-fx-text-fill: #f39c12;");
            sidebar.getChildren().add(researchBtn);
            researchBtn.setOnAction(e -> new ResearcherDashboard(stage, admin, this::show).show());
        }

        addResearcherRequestButton(sidebar);
        root.setLeft(sidebar);

        showDashboardContent();

        dashBtn.setOnAction(e -> showDashboardContent());
        usersBtn.setOnAction(e -> showManageUsers());
        logsBtn.setOnAction(e -> showSystemLogs());
        createUserBtn.setOnAction(e -> showCreateUser());
        setPassBtn.setOnAction(e -> showSetPassword());
        techSupportBtn.setOnAction(e -> showTechSupportForm());

        Scene scene = new Scene(root, 1100, 700);
        stage.setScene(scene);
        stage.setTitle("Admin Portal - " + admin.getFirstName());
        stage.show();
    }

    private void showDashboardContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        List<User> users = DataStore.getInstance().getUsers();
        long studentCount = users.stream().filter(u -> u instanceof Student).count();
        long teacherCount = users.stream().filter(u -> u instanceof Teacher).count();
        int courseCount = DataStore.getInstance().getCourses().size();

        HBox cards = new HBox(15);
        cards.getChildren().addAll(
            createCard("Total Users", String.valueOf(users.size()), "#4a90d9"),
            createCard("Students", String.valueOf(studentCount), "#27ae60"),
            createCard("Teachers", String.valueOf(teacherCount), "#f39c12"),
            createCard("Courses", String.valueOf(courseCount), "#8e44ad")
        );

        content.getChildren().addAll(
            createSectionTitle("Welcome, Admin " + admin.getFirstName() + "!"),
            cards
        );
        setContent(content);
    }

    private void showManageUsers() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle("Manage Users"));

        TableView<User> table = new TableView<>();
        table.setStyle("-fx-background-radius: 10;");

        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getFirstName() + " " + data.getValue().getLastName()));
        nameCol.setPrefWidth(200);

        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getEmail()));
        emailCol.setPrefWidth(250);

        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            resolveRole(data.getValue())));
        roleCol.setPrefWidth(150);

        TableColumn<User, Void> actionCol = new TableColumn<>("Action");
        actionCol.setPrefWidth(120);
        actionCol.setCellFactory(col -> new TableCell<>() {
            final Button btn = new Button("Remove");
            {
                btn.setStyle(
                    "-fx-background-color: #e74c3c; -fx-text-fill: white;" +
                    "-fx-background-radius: 6; -fx-cursor: hand;"
                );
                btn.setOnAction(e -> {
                    User u = getTableView().getItems().get(getIndex());
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Confirm Removal");
                    confirm.setHeaderText(null);
                    confirm.setContentText("Remove user: " + u.getEmail() + "?");
                    confirm.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            controller.removeUser(u.getEmail());
                            showManageUsers();
                        }
                    });
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        table.getColumns().addAll(nameCol, emailCol, roleCol, actionCol);
        table.setItems(FXCollections.observableArrayList(DataStore.getInstance().getUsers()));
        table.setPrefHeight(400);

        content.getChildren().add(table);
        setContent(content);
    }

    private void showSystemLogs() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle("System Logs"));

        TableView<String> table = new TableView<>();
        table.setStyle("-fx-background-radius: 10;");

        TableColumn<String, String> logCol = new TableColumn<>("Log Entry");
        logCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()));
        logCol.setPrefWidth(800);

        table.getColumns().add(logCol);
        table.setItems(FXCollections.observableArrayList(controller.getLogFiles()));
        table.setPrefHeight(450);

        content.getChildren().add(table);
        setContent(content);
    }

    private void showCreateUser() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle("Create New User"));

        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First name");
        firstNameField.setPrefWidth(250);

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last name");
        lastNameField.setPrefWidth(250);

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setPrefWidth(250);

        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.setItems(FXCollections.observableArrayList(
            "STUDENT", "TEACHER", "MANAGER", "TECHSUPPORT", "GRADUATESTUDENT"
        ));
        roleCombo.setPromptText("Select role");
        roleCombo.setPrefWidth(250);

        Label resultLabel = new Label("");

        Button createBtn = new Button("Create User");
        createBtn.setStyle(
            "-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14;" +
            "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 10 20;"
        );

        createBtn.setOnAction(e -> {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String email = emailField.getText().trim();
            String role = roleCombo.getValue();

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || role == null) {
                resultLabel.setText("Please fill all fields");
                resultLabel.setTextFill(Color.ORANGE);
                return;
            }

            try {
                controller.createUser(email, firstName, lastName, role);
                resultLabel.setText("User created successfully! Default password: password123");
                resultLabel.setTextFill(Color.web("#27ae60"));
                firstNameField.clear();
                lastNameField.clear();
                emailField.clear();
                roleCombo.setValue(null);
            } catch (Exception ex) {
                resultLabel.setText("Error: " + ex.getMessage());
                resultLabel.setTextFill(Color.RED);
            }
        });

        form.addRow(0, new Label("First Name:"), firstNameField);
        form.addRow(1, new Label("Last Name:"), lastNameField);
        form.addRow(2, new Label("Email:"), emailField);
        form.addRow(3, new Label("Role:"), roleCombo);
        form.addRow(4, createBtn, resultLabel);

        content.getChildren().add(form);
        setContent(content);
    }

    private void showSetPassword() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle("Set User Password"));

        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        ComboBox<User> userCombo = new ComboBox<>();
        userCombo.setItems(FXCollections.observableArrayList(DataStore.getInstance().getUsers()));
        userCombo.setPromptText("Select user");
        userCombo.setPrefWidth(280);
        userCombo.setConverter(new javafx.util.StringConverter<User>() {
            @Override public String toString(User u) {
                return u == null ? "" : u.getFirstName() + " " + u.getLastName() + " (" + u.getEmail() + ")";
            }
            @Override public User fromString(String s) { return null; }
        });

        javafx.scene.control.PasswordField newPassField = new javafx.scene.control.PasswordField();
        newPassField.setPromptText("New password");
        newPassField.setPrefWidth(280);

        javafx.scene.control.PasswordField confirmPassField = new javafx.scene.control.PasswordField();
        confirmPassField.setPromptText("Confirm password");
        confirmPassField.setPrefWidth(280);

        Label resultLabel = new Label("");

        Button applyBtn = new Button("Apply");
        applyBtn.setStyle(
            "-fx-background-color: #4a90d9; -fx-text-fill: white; -fx-font-size: 14;" +
            "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 10 20;"
        );

        applyBtn.setOnAction(e -> {
            User selected = userCombo.getValue();
            String pass = newPassField.getText();
            String confirm = confirmPassField.getText();

            if (selected == null) {
                resultLabel.setText("Please select a user.");
                resultLabel.setTextFill(Color.ORANGE);
                return;
            }
            if (pass.isBlank()) {
                resultLabel.setText("Password cannot be empty.");
                resultLabel.setTextFill(Color.ORANGE);
                return;
            }
            if (!pass.equals(confirm)) {
                resultLabel.setText("Passwords do not match.");
                resultLabel.setTextFill(Color.RED);
                return;
            }
            controller.setPassword(selected.getEmail(), pass);
            resultLabel.setText("Password updated for " + selected.getFirstName() + "!");
            resultLabel.setTextFill(Color.web("#27ae60"));
            newPassField.clear();
            confirmPassField.clear();
        });

        form.addRow(0, new Label("User:"), userCombo);
        form.addRow(1, new Label("New Password:"), newPassField);
        form.addRow(2, new Label("Confirm:"), confirmPassField);
        form.addRow(3, applyBtn, resultLabel);

        content.getChildren().add(form);
        setContent(content);
    }

    private String resolveRole(User u) {
        if (u instanceof Admin) return "Admin";
        if (u instanceof GraduateStudent) return "Graduate Student";
        if (u instanceof Student) return "Student";
        if (u instanceof Teacher) return "Teacher";
        if (u instanceof Manager) return "Manager";
        if (u instanceof TechSupportSpecialist) return "Tech Support";
        return "User";
    }
}
