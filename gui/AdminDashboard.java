package gui;

import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.scene.paint.Color;
import javafx.collections.*;
import models.*;
import core.DataStore;
import controllers.*;
import enums.*;

import java.util.*;


public class AdminDashboard extends BaseDashboard {
    private Admin admin;
    private AdminController controller = new AdminController();

    public AdminDashboard(Stage stage, Admin admin) {
        super(stage, admin);
        this.admin = admin;
    }

    @Override
    public void show() {
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
        Label roleLabel = new Label(" Administrator");
        roleLabel.setTextFill(Color.web("#aaaaaa"));
        userInfo.getChildren().addAll(nameLabel, roleLabel);

        Button dashBtn = createMenuButton("Dashboard", "");
        Button usersBtn = createMenuButton("Manage Users", "");
        Button logsBtn = createMenuButton("System Logs", "");
        Button createUserBtn = createMenuButton("Create User", "");

        sidebar.getChildren().addAll(userInfo, new Separator(), dashBtn, usersBtn, logsBtn, createUserBtn);
        root.setLeft(sidebar);

        showDashboardContent();

        dashBtn.setOnAction(e -> showDashboardContent());
        usersBtn.setOnAction(e -> showManageUsers());
        logsBtn.setOnAction(e -> showSystemLogs());
        createUserBtn.setOnAction(e -> showCreateUser());

        Scene scene = new Scene(root, 1100, 700);
        stage.setScene(scene);
        stage.setTitle("Admin Portal — " + admin.getFirstName());
        stage.show();
    }

    private void showDashboardContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        Label title = createSectionTitle("Welcome, Admin " + admin.getFirstName() + "! ");

        HBox cards = new HBox(15);
        List<User> users = DataStore.getInstance().getUsers();
        long studentCount = users.stream().filter(u -> u instanceof Student).count();
        long teacherCount = users.stream().filter(u -> u instanceof Teacher).count();
        int courseCount = DataStore.getInstance().getCourses().size();

        cards.getChildren().addAll(
            createCard("Total Users", String.valueOf(users.size()), "#4a90d9"),
            createCard("Students", String.valueOf(studentCount), "#27ae60"),
            createCard("Teachers", String.valueOf(teacherCount), "#f39c12"),
            createCard("Courses", String.valueOf(courseCount), "#8e44ad")
        );

        content.getChildren().addAll(title, cards);
        setContent(content);
    }

    private void showManageUsers() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle(" Manage Users"));

        TableView<User> table = new TableView<>();
        table.setStyle("-fx-background-radius: 10;");

        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getFirstName() + " " + data.getValue().getLastName()));
        nameCol.setPrefWidth(200);

        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));
        emailCol.setPrefWidth(250);

        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(data -> {
            User u = data.getValue();
            String role = "User";
            if (u instanceof Admin) role = "Admin";
            else if (u instanceof GraduateStudent) role = "Graduate Student";
            else if (u instanceof Student) role = "Student";
            else if (u instanceof Teacher) role = "Teacher";
            else if (u instanceof Manager) role = "Manager";
            else if (u instanceof TechSupportSpecialist) role = "Tech Support";
            return new javafx.beans.property.SimpleStringProperty(role);
        });
        roleCol.setPrefWidth(150);

        TableColumn<User, Void> actionCol = new TableColumn<>("Action");
        actionCol.setPrefWidth(120);
        actionCol.setCellFactory(col -> new TableCell<>() {
            final Button btn = new Button("Remove");
            {
                btn.setStyle(
                    "-fx-background-color: #e74c3c;" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 6;" +
                    "-fx-cursor: hand;"
                );
                btn.setOnAction(e -> {
                    User user = getTableView().getItems().get(getIndex());
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Confirm Removal");
                    confirm.setHeaderText(null);
                    confirm.setContentText("Remove user: " + user.getEmail() + "?");
                    confirm.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            controller.removeUser(user.getEmail());
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
        content.getChildren().add(createSectionTitle(" System Logs"));

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
        content.getChildren().add(createSectionTitle(" Create New User"));

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

        Button createBtn = new Button(" Create User");
        createBtn.setStyle(
            "-fx-background-color: #27ae60;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 10 20;"
        );

        createBtn.setOnAction(e -> {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String email = emailField.getText().trim();
            String role = roleCombo.getValue();

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || role == null) {
                resultLabel.setText(" Please fill all fields");
                resultLabel.setTextFill(Color.ORANGE);
                return;
            }

            try {
                controller.createUser(email, firstName, lastName, role);
                resultLabel.setText(" User created successfully! Default password: password123");
                resultLabel.setTextFill(Color.web("#27ae60"));
                firstNameField.clear();
                lastNameField.clear();
                emailField.clear();
                roleCombo.setValue(null);
            } catch (Exception ex) {
                resultLabel.setText(" Error: " + ex.getMessage());
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
}
