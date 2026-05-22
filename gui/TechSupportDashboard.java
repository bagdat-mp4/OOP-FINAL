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


public class TechSupportDashboard extends BaseDashboard {
    private TechSupportSpecialist techSupport;
    private TechSupportController controller = new TechSupportController();

    public TechSupportDashboard(Stage stage, TechSupportSpecialist techSupport) {
        super(stage, techSupport);
        this.techSupport = techSupport;
    }

    @Override
    public void show() {
        root.setStyle("-fx-background-color: #f5f6fa;");
        root.setTop(createNavBar("Tech Support Portal"));

        VBox sidebar = new VBox(5);
        sidebar.setPadding(new Insets(20, 10, 20, 10));
        sidebar.setStyle("-fx-background-color: #16213e; -fx-min-width: 240;");

        VBox userInfo = new VBox(5);
        userInfo.setPadding(new Insets(10, 10, 20, 10));
        Label nameLabel = new Label(techSupport.getFirstName() + " " + techSupport.getLastName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        nameLabel.setTextFill(Color.WHITE);
        Label roleLabel = new Label("🛠️ Tech Support");
        roleLabel.setTextFill(Color.web("#aaaaaa"));
        userInfo.getChildren().addAll(nameLabel, roleLabel);

        Button dashBtn = createMenuButton("Dashboard", "");
        Button requestsBtn = createMenuButton("View Requests", "");
        Button usersBtn = createMenuButton("All Users", "");

        sidebar.getChildren().addAll(userInfo, new Separator(), dashBtn, requestsBtn, usersBtn);
        root.setLeft(sidebar);

        showDashboardContent();

        dashBtn.setOnAction(e -> showDashboardContent());
        requestsBtn.setOnAction(e -> showRequests());
        usersBtn.setOnAction(e -> showAllUsers());

        Scene scene = new Scene(root, 1100, 700);
        stage.setScene(scene);
        stage.setTitle("Tech Support Portal — " + techSupport.getFirstName());
        stage.show();
    }

    private void showDashboardContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        Label title = createSectionTitle("Welcome, Tech Support " + techSupport.getFirstName() + "! 🛠️");

        HBox cards = new HBox(15);
        List<TechSupportRequest> requests = DataStore.getInstance().getTechSupportRequests();
        long newCount = requests.stream().filter(o -> o.getStatus() == RequestStatus.NEW).count();
        long acceptedCount = requests.stream().filter(o -> o.getStatus() == RequestStatus.ACCEPTED).count();
        long rejectedCount = requests.stream().filter(o -> o.getStatus() == RequestStatus.REJECTED).count();

        cards.getChildren().addAll(
            createCard("Total Requests", String.valueOf(requests.size()), "#4a90d9"),
            createCard("New", String.valueOf(newCount), "#f39c12"),
            createCard("Accepted", String.valueOf(acceptedCount), "#27ae60"),
            createCard("Rejected", String.valueOf(rejectedCount), "#e74c3c")
        );

        content.getChildren().addAll(title, cards);
        setContent(content);
    }

    private void showRequests() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle(" Tech Support Requests"));

        TableView<TechSupportRequest> table = new TableView<>();
        table.setStyle("-fx-background-radius: 10;");

        TableColumn<TechSupportRequest, String> userCol = new TableColumn<>("User");
        userCol.setCellValueFactory(data -> {
            User u = data.getValue().getSender();
            return new javafx.beans.property.SimpleStringProperty(
                u != null ? u.getFirstName() + " " + u.getLastName() : "Unknown");
        });
        userCol.setPrefWidth(150);

        TableColumn<TechSupportRequest, String> descCol = new TableColumn<>("Issue");
        descCol.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getIssue()));
        descCol.setPrefWidth(300);

        TableColumn<TechSupportRequest, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getStatus().toString()));
        statusCol.setPrefWidth(100);

        TableColumn<TechSupportRequest, Void> acceptCol = new TableColumn<>("Accept");
        acceptCol.setPrefWidth(100);
        acceptCol.setCellFactory(col -> new TableCell<>() {
            final Button btn = new Button(" Accept");
            {
                btn.setStyle(
                    "-fx-background-color: #27ae60;" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 6;" +
                    "-fx-cursor: hand;"
                );
                btn.setOnAction(e -> {
                    TechSupportRequest request = getTableView().getItems().get(getIndex());
                    controller.changeOrderStatus(request, RequestStatus.ACCEPTED);
                    showRequests();
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    TechSupportRequest request = getTableView().getItems().get(getIndex());
                    setGraphic(request.getStatus() == RequestStatus.NEW ? btn : null);
                }
            }
        });

        TableColumn<TechSupportRequest, Void> rejectCol = new TableColumn<>("Reject");
        rejectCol.setPrefWidth(100);
        rejectCol.setCellFactory(col -> new TableCell<>() {
            final Button btn = new Button(" Reject");
            {
                btn.setStyle(
                    "-fx-background-color: #e74c3c;" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 6;" +
                    "-fx-cursor: hand;"
                );
                btn.setOnAction(e -> {
                    TechSupportRequest request = getTableView().getItems().get(getIndex());
                    controller.changeOrderStatus(request, RequestStatus.REJECTED);
                    showRequests();
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    TechSupportRequest request = getTableView().getItems().get(getIndex());
                    setGraphic(request.getStatus() == RequestStatus.NEW ? btn : null);
                }
            }
        });

        table.getColumns().addAll(userCol, descCol, statusCol, acceptCol, rejectCol);
        table.setItems(FXCollections.observableArrayList(DataStore.getInstance().getTechSupportRequests()));
        table.setPrefHeight(400);

        content.getChildren().add(table);
        setContent(content);
    }

    private void showAllUsers() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle(" All Users"));

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

        TableColumn<User, String> langCol = new TableColumn<>("Language");
        langCol.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getLanguage() != null ? data.getValue().getLanguage().toString() : "N/A"));
        langCol.setPrefWidth(100);

        table.getColumns().addAll(nameCol, emailCol, roleCol, langCol);
        table.setItems(FXCollections.observableArrayList(DataStore.getInstance().getUsers()));
        table.setPrefHeight(400);

        content.getChildren().add(table);
        setContent(content);
    }
}
