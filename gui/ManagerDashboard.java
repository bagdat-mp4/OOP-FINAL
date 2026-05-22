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

/**
 * Manager Dashboard with course management and academic reports
 */
public class ManagerDashboard extends BaseDashboard {
    private Manager manager;
    private ManagerController controller = new ManagerController();

    public ManagerDashboard(Stage stage, Manager manager) {
        super(stage, manager);
        this.manager = manager;
    }

    @Override
    public void show() {
        root.setStyle("-fx-background-color: #f5f6fa;");
        root.setTop(createNavBar("Manager Portal"));

        VBox sidebar = new VBox(5);
        sidebar.setPadding(new Insets(20, 10, 20, 10));
        sidebar.setStyle("-fx-background-color: #16213e; -fx-min-width: 240;");

        VBox userInfo = new VBox(5);
        userInfo.setPadding(new Insets(10, 10, 20, 10));
        Label nameLabel = new Label(manager.getFirstName() + " " + manager.getLastName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        nameLabel.setTextFill(Color.WHITE);
        Label roleLabel = new Label("📊 Manager");
        roleLabel.setTextFill(Color.web("#aaaaaa"));
        Label typeLabel = new Label("Type: " + (manager.getManagerType() != null ? manager.getManagerType() : "General"));
        typeLabel.setTextFill(Color.web("#aaaaaa"));
        userInfo.getChildren().addAll(nameLabel, roleLabel, typeLabel);

        Button dashBtn = createMenuButton("Dashboard", "🏠");
        Button coursesBtn = createMenuButton("All Courses", "📚");
        Button studentsBtn = createMenuButton("Student Reports", "📊");
        Button newsBtn = createMenuButton("Manage News", "📰");
        Button officialMsgBtn = createMenuButton("Official Message", "📢");

        sidebar.getChildren().addAll(userInfo, new Separator(), dashBtn, coursesBtn, studentsBtn, newsBtn, officialMsgBtn);
        root.setLeft(sidebar);

        showDashboardContent();

        dashBtn.setOnAction(e -> showDashboardContent());
        coursesBtn.setOnAction(e -> showAllCourses());
        studentsBtn.setOnAction(e -> showStudentReports());
        newsBtn.setOnAction(e -> showManageNews());
        officialMsgBtn.setOnAction(e -> showOfficialMessage());

        Scene scene = new Scene(root, 1100, 700);
        stage.setScene(scene);
        stage.setTitle("Manager Portal — " + manager.getFirstName());
        stage.show();
    }

    private void showDashboardContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        Label title = createSectionTitle("Welcome, Manager " + manager.getFirstName() + "! 📊");

        HBox cards = new HBox(15);
        List<Course> courses = DataStore.getInstance().getCourses();
        List<User> users = DataStore.getInstance().getUsers();
        long studentCount = users.stream().filter(u -> u instanceof Student).count();
        long teacherCount = users.stream().filter(u -> u instanceof Teacher).count();

        cards.getChildren().addAll(
            createCard("Courses", String.valueOf(courses.size()), "#4a90d9"),
            createCard("Students", String.valueOf(studentCount), "#27ae60"),
            createCard("Teachers", String.valueOf(teacherCount), "#f39c12"),
            createCard("News", String.valueOf(DataStore.getInstance().getNews().size()), "#8e44ad")
        );

        content.getChildren().addAll(title, cards);
        setContent(content);
    }

    private void showAllCourses() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle("📚 All Courses"));

        TableView<Course> table = new TableView<>();
        table.setStyle("-fx-background-radius: 10;");

        TableColumn<Course, String> codeCol = new TableColumn<>("Code");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        codeCol.setPrefWidth(100);

        TableColumn<Course, String> nameCol = new TableColumn<>("Course Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(250);

        TableColumn<Course, Integer> creditsCol = new TableColumn<>("Credits");
        creditsCol.setCellValueFactory(new PropertyValueFactory<>("credits"));
        creditsCol.setPrefWidth(80);

        TableColumn<Course, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setPrefWidth(120);

        TableColumn<Course, String> teacherCol = new TableColumn<>("Teacher");
        teacherCol.setCellValueFactory(data -> {
            String teacherName = data.getValue().getLectureInstructors().isEmpty()
                ? "None"
                : data.getValue().getLectureInstructors().get(0).getFirstName() + " " + data.getValue().getLectureInstructors().get(0).getLastName();
            return new javafx.beans.property.SimpleStringProperty(teacherName);
        });
        teacherCol.setPrefWidth(180);

        TableColumn<Course, Integer> enrolledCol = new TableColumn<>("Enrolled");
        enrolledCol.setCellValueFactory(data ->
            new javafx.beans.property.SimpleIntegerProperty(
                data.getValue().getEnrolledStudents().size()).asObject());
        enrolledCol.setPrefWidth(80);

        table.getColumns().addAll(codeCol, nameCol, creditsCol, typeCol, teacherCol, enrolledCol);
        table.setItems(FXCollections.observableArrayList(DataStore.getInstance().getCourses()));
        table.setPrefHeight(400);

        content.getChildren().add(table);
        setContent(content);
    }

    private void showStudentReports() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle("📊 Student Academic Reports"));

        TableView<Student> table = new TableView<>();
        table.setStyle("-fx-background-radius: 10;");

        TableColumn<Student, String> nameCol = new TableColumn<>("Student Name");
        nameCol.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getFirstName() + " " + data.getValue().getLastName()));
        nameCol.setPrefWidth(200);

        TableColumn<Student, String> majorCol = new TableColumn<>("Major");
        majorCol.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getMajor()));
        majorCol.setPrefWidth(150);

        TableColumn<Student, Integer> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(data ->
            new javafx.beans.property.SimpleIntegerProperty(
                data.getValue().getYearOfStudy()).asObject());
        yearCol.setPrefWidth(80);

        TableColumn<Student, Double> gpaCol = new TableColumn<>("GPA");
        gpaCol.setCellValueFactory(data ->
            new javafx.beans.property.SimpleDoubleProperty(
                data.getValue().getGPA()).asObject());
        gpaCol.setPrefWidth(80);

        TableColumn<Student, Integer> creditsCol = new TableColumn<>("Credits");
        creditsCol.setCellValueFactory(data ->
            new javafx.beans.property.SimpleIntegerProperty(
                data.getValue().getCurrentCredits()).asObject());
        creditsCol.setPrefWidth(80);

        TableColumn<Student, Integer> failCol = new TableColumn<>("Fails");
        failCol.setCellValueFactory(data ->
            new javafx.beans.property.SimpleIntegerProperty(
                data.getValue().getFailCount()).asObject());
        failCol.setPrefWidth(80);

        table.getColumns().addAll(nameCol, majorCol, yearCol, gpaCol, creditsCol, failCol);

        List<Student> students = new ArrayList<>();
        for (User u : DataStore.getInstance().getUsers()) {
            if (u instanceof Student) {
                students.add((Student) u);
            }
        }
        table.setItems(FXCollections.observableArrayList(students));
        table.setPrefHeight(400);

        content.getChildren().add(table);
        setContent(content);
    }

    private void showManageNews() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle("📰 Manage University News"));

        // Create news form
        VBox form = new VBox(15);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        TextField titleField = new TextField();
        titleField.setPromptText("News title");
        titleField.setPrefWidth(400);

        TextField topicField = new TextField();
        topicField.setPromptText("Topic (e.g., Academic, Event, Announcement)");
        topicField.setPrefWidth(400);

        TextArea contentArea = new TextArea();
        contentArea.setPromptText("News content...");
        contentArea.setPrefRowCount(4);
        contentArea.setPrefWidth(400);

        CheckBox pinnedCheck = new CheckBox("Pin this news");
        pinnedCheck.setTextFill(Color.web("#333333"));

        Label resultLabel = new Label("");

        Button publishBtn = new Button("📤 Publish News");
        publishBtn.setStyle(
            "-fx-background-color: #27ae60;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 10 20;"
        );

        publishBtn.setOnAction(e -> {
            String title = titleField.getText().trim();
            String topic = topicField.getText().trim();
            String newsContent = contentArea.getText().trim();

            if (title.isEmpty() || topic.isEmpty() || newsContent.isEmpty()) {
                resultLabel.setText("⚠ Please fill all fields");
                resultLabel.setTextFill(Color.ORANGE);
                return;
            }

            controller.addNews(title, newsContent, pinnedCheck.isSelected());
            resultLabel.setText("✅ News published successfully!");
            resultLabel.setTextFill(Color.web("#27ae60"));
            titleField.clear();
            topicField.clear();
            contentArea.clear();
            pinnedCheck.setSelected(false);
        });

        form.getChildren().addAll(
            new Label("Title:"), titleField,
            new Label("Topic:"), topicField,
            new Label("Content:"), contentArea,
            pinnedCheck,
            publishBtn, resultLabel
        );

        // Existing news list
        Label existingTitle = createSectionTitle("📋 Published News");
        VBox newsList = new VBox(10);
        for (News n : DataStore.getInstance().getNews()) {
            HBox item = new HBox(15);
            item.setPadding(new Insets(12));
            item.setAlignment(Pos.CENTER_LEFT);
            item.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 8;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);"
            );
            if (n.getIsPinned()) {
                Label pin = new Label("📌");
                item.getChildren().add(pin);
            }
            Label newsLabel = new Label("[" + n.getTopic() + "] " + n.getTitle());
            newsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
            item.getChildren().add(newsLabel);
            newsList.getChildren().add(item);
        }

        content.getChildren().addAll(form, existingTitle, newsList);
        setContent(content);
    }

    private void showOfficialMessage() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle("📢 Send Official Message"));

        VBox form = new VBox(15);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        TextArea msgArea = new TextArea();
        msgArea.setPromptText("e.g. Exam room 301 booked for CS101 on June 5");
        msgArea.setPrefRowCount(5);
        msgArea.setPrefWidth(500);

        Label resultLabel = new Label("");

        Button sendBtn = new Button("📤 Send to All");
        sendBtn.setStyle(
            "-fx-background-color: #e74c3c;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 10 20;"
        );
        sendBtn.setOnAction(e -> {
            String msg = msgArea.getText().trim();
            if (!msg.isEmpty()) {
                GlobalMessage gm = new GlobalMessage(manager, msg);
                DataStore.getInstance().addMessage(new Message(manager, manager, msg));
                resultLabel.setText("✅ Message sent to all employees!");
                resultLabel.setTextFill(Color.web("#27ae60"));
                msgArea.clear();
            } else {
                resultLabel.setText("⚠ Please enter a message");
                resultLabel.setTextFill(Color.ORANGE);
            }
        });

        form.getChildren().addAll(
            new Label("Official Message:"), msgArea, sendBtn, resultLabel
        );

        content.getChildren().add(form);
        setContent(content);
    }
}
