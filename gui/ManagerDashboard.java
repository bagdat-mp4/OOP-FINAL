package gui;

import controllers.ManagerController;
import core.DataStore;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import models.Course;
import models.Manager;
import models.Message;
import models.News;
import models.Student;
import models.Teacher;
import models.User;

import java.util.ArrayList;
import java.util.List;


public class ManagerDashboard extends BaseDashboard {

    private final Manager manager;
    private final ManagerController controller = new ManagerController();

    public ManagerDashboard(Stage stage, Manager manager) {
        super(stage, manager);
        this.manager = manager;
    }

    @Override
    public void show() {
        root = new javafx.scene.layout.BorderPane();
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
        Label roleLabel = new Label("Manager");
        roleLabel.setTextFill(Color.web("#aaaaaa"));
        Label typeLabel = new Label("Type: " + (manager.getManagerType() != null ? manager.getManagerType() : "General"));
        typeLabel.setTextFill(Color.web("#aaaaaa"));
        userInfo.getChildren().addAll(nameLabel, roleLabel, typeLabel);

        Button dashBtn = createMenuButton("Dashboard", "");
        Button coursesBtn = createMenuButton("All Courses", "");
        Button studentsBtn = createMenuButton("Student Reports", "");
        Button newsBtn = createMenuButton("Manage News", "");
        Button officialMsgBtn = createMenuButton("Broadcast Message", "");
        Button inboxBtn = createMenuButton("Inbox", "");
        Button complaintsBtn    = createMenuButton("Complaints", "");
        Button techSupportBtn   = createMenuButton("Tech Support", "");
        Button researchReqBtn   = createMenuButton("Researcher Requests", "");

        sidebar.getChildren().addAll(userInfo, new Separator(), dashBtn, coursesBtn, studentsBtn,
            newsBtn, officialMsgBtn, inboxBtn, complaintsBtn, researchReqBtn, techSupportBtn);

        if (core.DataStore.getInstance().isResearcher(manager)) {
            Button researchBtn = createMenuButton("Researcher Mode", "");
            researchBtn.setStyle(researchBtn.getStyle() + "-fx-text-fill: #f39c12;");
            sidebar.getChildren().add(researchBtn);
            researchBtn.setOnAction(e -> new ResearcherDashboard(stage, manager, this::show).show());
        }

        root.setLeft(sidebar);

        showDashboardContent();

        dashBtn.setOnAction(e -> showDashboardContent());
        coursesBtn.setOnAction(e -> showAllCourses());
        studentsBtn.setOnAction(e -> showStudentReports());
        newsBtn.setOnAction(e -> showManageNews());
        officialMsgBtn.setOnAction(e -> showOfficialMessage());
        inboxBtn.setOnAction(e -> showInbox());
        complaintsBtn.setOnAction(e -> showComplaints());
        researchReqBtn.setOnAction(e -> showResearcherRequests());
        techSupportBtn.setOnAction(e -> showTechSupportForm());

        Scene scene = new Scene(root, 1100, 700);
        stage.setScene(scene);
        stage.setTitle("Manager Portal - " + manager.getFirstName());
        stage.show();
    }

    private void showDashboardContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        List<Course> courses = DataStore.getInstance().getCourses();
        List<User> users = DataStore.getInstance().getUsers();
        long studentCount = users.stream().filter(u -> u instanceof Student).count();
        long teacherCount = users.stream().filter(u -> u instanceof Teacher).count();

        HBox cards = new HBox(15);
        cards.getChildren().addAll(
            createCard("Courses", String.valueOf(courses.size()), "#4a90d9"),
            createCard("Students", String.valueOf(studentCount), "#27ae60"),
            createCard("Teachers", String.valueOf(teacherCount), "#f39c12"),
            createCard("News", String.valueOf(DataStore.getInstance().getNews().size()), "#8e44ad")
        );

        content.getChildren().addAll(
            createSectionTitle("Welcome, Manager " + manager.getFirstName() + "!"),
            cards
        );
        setContent(content);
    }

    private void showAllCourses() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle("All Courses"));

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
            List<Teacher> instructors = data.getValue().getLectureInstructors();
            String teacherName = instructors.isEmpty()
                ? "None"
                : instructors.get(0).getFirstName() + " " + instructors.get(0).getLastName();
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
        content.getChildren().add(createSectionTitle("Student Academic Reports"));

        TableView<Student> table = new TableView<>();
        table.setStyle("-fx-background-radius: 10;");

        TableColumn<Student, String> nameCol = new TableColumn<>("Student Name");
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getFirstName() + " " + data.getValue().getLastName()));
        nameCol.setPrefWidth(200);

        TableColumn<Student, String> majorCol = new TableColumn<>("Major");
        majorCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getMajor()));
        majorCol.setPrefWidth(150);

        TableColumn<Student, Integer> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(
            data.getValue().getYearOfStudy()).asObject());
        yearCol.setPrefWidth(80);

        TableColumn<Student, Double> gpaCol = new TableColumn<>("GPA");
        gpaCol.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(
            data.getValue().getGPA()).asObject());
        gpaCol.setPrefWidth(80);

        TableColumn<Student, Integer> creditsCol = new TableColumn<>("Credits");
        creditsCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(
            data.getValue().getCurrentCredits()).asObject());
        creditsCol.setPrefWidth(80);

        TableColumn<Student, Integer> failCol = new TableColumn<>("Fails");
        failCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(
            data.getValue().getFailCount()).asObject());
        failCol.setPrefWidth(80);

        table.getColumns().addAll(nameCol, majorCol, yearCol, gpaCol, creditsCol, failCol);

        List<Student> students = new ArrayList<>();
        for (User u : DataStore.getInstance().getUsers()) {
            if (u instanceof Student) students.add((Student) u);
        }
        table.setItems(FXCollections.observableArrayList(students));
        table.setPrefHeight(400);

        content.getChildren().add(table);
        setContent(content);
    }

    private void showManageNews() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle("Manage University News"));

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

        Button publishBtn = new Button("Publish News");
        publishBtn.setStyle(
            "-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14;" +
            "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 10 20;"
        );

        publishBtn.setOnAction(e -> {
            String title = titleField.getText().trim();
            String topic = topicField.getText().trim();
            String newsContent = contentArea.getText().trim();

            if (title.isEmpty() || topic.isEmpty() || newsContent.isEmpty()) {
                resultLabel.setText("Please fill all fields");
                resultLabel.setTextFill(Color.ORANGE);
                return;
            }

            controller.addNews(title, newsContent, pinnedCheck.isSelected());
            resultLabel.setText("News published successfully!");
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
            pinnedCheck, publishBtn, resultLabel
        );

        Label existingTitle = createSectionTitle("Published News");
        VBox newsList = new VBox(10);
        for (News n : DataStore.getInstance().getNews()) {
            HBox item = new HBox(15);
            item.setPadding(new Insets(12));
            item.setAlignment(Pos.CENTER_LEFT);
            item.setStyle(
                "-fx-background-color: white; -fx-background-radius: 8;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);"
            );
            Label newsLabel = new Label((n.isPinned() ? "[PINNED] " : "") + "[" + n.getTopic() + "] " + n.getTitle());
            newsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
            item.getChildren().add(newsLabel);
            newsList.getChildren().add(item);
        }

        content.getChildren().addAll(form, existingTitle, newsList);
        setContent(content);
    }

    private void showInbox() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle("Inbox"));

        List<Message> messages = controller.getInbox(manager);

        if (messages.isEmpty()) {
            Label empty = new Label("No messages.");
            empty.setTextFill(Color.web("#999999"));
            content.getChildren().add(empty);
        } else {
            VBox list = new VBox(8);
            for (Message m : messages) {
                VBox card = new VBox(5);
                card.setPadding(new Insets(12));
                card.setStyle(
                    "-fx-background-color: white; -fx-background-radius: 8;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);"
                );
                Label from = new Label("From: " + m.getSender().getFirstName() + " " + m.getSender().getLastName());
                from.setFont(Font.font("Arial", FontWeight.BOLD, 13));
                from.setTextFill(Color.web("#4a90d9"));
                Label text = new Label(m.getText());
                text.setWrapText(true);
                card.getChildren().addAll(from, text);
                list.getChildren().add(card);
            }
            content.getChildren().add(list);
        }

        setContent(content);
    }

    private void showComplaints() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle("Teacher Complaints"));

        List<Message> complaints = controller.getComplaints(manager);

        if (complaints.isEmpty()) {
            Label empty = new Label("No complaints.");
            empty.setTextFill(Color.web("#999999"));
            content.getChildren().add(empty);
        } else {
            VBox list = new VBox(8);
            for (Message m : complaints) {
                VBox card = new VBox(5);
                card.setPadding(new Insets(12));
                card.setStyle(
                    "-fx-background-color: white; -fx-background-radius: 8;" +
                    "-fx-border-color: #e74c3c; -fx-border-width: 0 0 0 4;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);"
                );
                Label from = new Label("From: " + m.getSender().getFirstName() + " " + m.getSender().getLastName());
                from.setFont(Font.font("Arial", FontWeight.BOLD, 13));
                from.setTextFill(Color.web("#e74c3c"));
                Label text = new Label(m.getText());
                text.setWrapText(true);
                card.getChildren().addAll(from, text);
                list.getChildren().add(card);
            }
            content.getChildren().add(list);
        }

        setContent(content);
    }

    private void showResearcherRequests() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle("Researcher Requests"));

        List<User> requests = controller.getResearcherRequests();

        if (requests.isEmpty()) {
            Label empty = new Label("No pending requests.");
            empty.setTextFill(Color.web("#999999"));
            content.getChildren().add(empty);
            setContent(content);
            return;
        }

        VBox list = new VBox(10);
        for (User u : new java.util.ArrayList<>(requests)) {
            HBox row = new HBox(15);
            row.setPadding(new Insets(15));
            row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            row.setStyle(
                "-fx-background-color: white; -fx-background-radius: 10;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);"
            );

            Label name = new Label(u.getFirstName() + " " + u.getLastName());
            name.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            name.setPrefWidth(200);

            Label email = new Label(u.getEmail());
            email.setTextFill(Color.web("#666666"));
            email.setPrefWidth(200);

            javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
            javafx.scene.layout.HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

            Button acceptBtn = new Button("Accept");
            acceptBtn.setStyle(
                "-fx-background-color: #27ae60; -fx-text-fill: white;" +
                "-fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 16;"
            );
            acceptBtn.setOnAction(e -> {
                controller.acceptResearcherRequest(u);
                showResearcherRequests();
            });

            Button rejectBtn = new Button("Reject");
            rejectBtn.setStyle(
                "-fx-background-color: #e74c3c; -fx-text-fill: white;" +
                "-fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 16;"
            );
            rejectBtn.setOnAction(e -> {
                controller.rejectResearcherRequest(u);
                showResearcherRequests();
            });

            row.getChildren().addAll(name, email, spacer, acceptBtn, rejectBtn);
            list.getChildren().add(row);
        }

        content.getChildren().add(list);
        setContent(content);
    }

    private void showOfficialMessage() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle("Send Official Message"));

        VBox form = new VBox(15);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        TextArea msgArea = new TextArea();
        msgArea.setPromptText("e.g. Exam room 301 booked for CS101 on June 5");
        msgArea.setPrefRowCount(5);
        msgArea.setPrefWidth(500);

        Label resultLabel = new Label("");

        Button sendBtn = new Button("Send to All");
        sendBtn.setStyle(
            "-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14;" +
            "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 10 20;"
        );
        sendBtn.setOnAction(e -> {
            String msg = msgArea.getText().trim();
            if (!msg.isEmpty()) {
                controller.broadcastMessage(manager, msg);
                resultLabel.setText("Broadcast sent to all employees!");
                resultLabel.setTextFill(Color.web("#27ae60"));
                msgArea.clear();
            } else {
                resultLabel.setText("Please enter a message");
                resultLabel.setTextFill(Color.ORANGE);
            }
        });

        form.getChildren().addAll(new Label("Official Message:"), msgArea, sendBtn, resultLabel);
        content.getChildren().add(form);
        setContent(content);
    }
}
