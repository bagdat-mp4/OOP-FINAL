package gui;

import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.scene.paint.Color;
import javafx.collections.*;
import models.*;
import core.DataStore;
import controllers.*;
import enums.*;


public class TeacherDashboard extends BaseDashboard {
    private Teacher teacher;
    private TeacherController controller = new TeacherController();

    public TeacherDashboard(Stage stage, Teacher teacher) {
        super(stage, teacher);
        this.teacher = teacher;
    }

    @Override
    public void show() {
        root.setStyle("-fx-background-color: #f5f6fa;");
        root.setTop(createNavBar("Teacher Portal"));

        VBox sidebar = new VBox(5);
        sidebar.setPadding(new Insets(20, 10, 20, 10));
        sidebar.setStyle("-fx-background-color: #16213e; -fx-min-width: 240;");

        VBox userInfo = new VBox(5);
        userInfo.setPadding(new Insets(10, 10, 20, 10));
        Label nameLabel = new Label(teacher.getFirstName() + " " + teacher.getLastName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        nameLabel.setTextFill(Color.WHITE);
        Label titleLabel = new Label("👨‍🏫 " + (teacher.getTitle() != null ? teacher.getTitle() : "Teacher"));
        titleLabel.setTextFill(Color.web("#aaaaaa"));
        Label ratingLabel = new Label(" Rating: " + String.format("%.1f", teacher.getRating()));
        ratingLabel.setTextFill(Color.web("#f39c12"));
        userInfo.getChildren().addAll(nameLabel, titleLabel, ratingLabel);

        Button dashBtn = createMenuButton("Dashboard", "");
        Button coursesBtn = createMenuButton("My Courses", "📚");
        Button marksBtn = createMenuButton("Put Marks", "️");
        Button complaintBtn = createMenuButton("Send Complaint", "");

        sidebar.getChildren().addAll(userInfo, new Separator(), dashBtn, coursesBtn, marksBtn, complaintBtn);
        root.setLeft(sidebar);

        showDashboardContent();

        dashBtn.setOnAction(e -> showDashboardContent());
        coursesBtn.setOnAction(e -> showCourses());
        marksBtn.setOnAction(e -> showPutMarks());
        complaintBtn.setOnAction(e -> showComplaintForm());

        Scene scene = new Scene(root, 1100, 700);
        stage.setScene(scene);
        stage.setTitle("Teacher Portal — " + teacher.getFirstName());
        stage.show();
    }

    private void showDashboardContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        Label title = createSectionTitle("Welcome, Prof. " + teacher.getFirstName() + "! 👋");

        HBox cards = new HBox(15);
        int studentCount = teacher.getActiveCourses().stream()
            .mapToInt(c -> c.getEnrolledStudents().size()).sum();
        cards.getChildren().addAll(
            createCard("My Courses", String.valueOf(teacher.getActiveCourses().size()), "#4a90d9"),
            createCard("Students", String.valueOf(studentCount), "#27ae60"),
            createCard("Rating", String.format("%.1f", teacher.getRating()), "#f39c12")
        );

        content.getChildren().addAll(title, cards);
        setContent(content);
    }

    private void showCourses() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle("📚 My Courses"));

        VBox coursesList = new VBox(10);
        for (Course c : teacher.getActiveCourses()) {
            VBox card = new VBox(8);
            card.setPadding(new Insets(15));
            card.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 10;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);"
            );
            Label courseName = new Label(c.getCourseCode() + " — " + c.getName());
            courseName.setFont(Font.font("Arial", FontWeight.BOLD, 15));
            Label info = new Label("Credits: " + c.getCredits() + " | Students: " + c.getEnrolledStudents().size() + " | Type: " + c.getType());
            info.setTextFill(Color.web("#666666"));
            card.getChildren().addAll(courseName, info);
            coursesList.getChildren().add(card);
        }

        content.getChildren().add(coursesList);
        setContent(content);
    }

    private void showPutMarks() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle("️ Put Marks"));

        
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        TextField studentEmail = new TextField();
        studentEmail.setPromptText("Student email");
        studentEmail.setPrefWidth(250);

        ComboBox<Course> courseCombo = new ComboBox<>();
        courseCombo.setItems(FXCollections.observableArrayList(teacher.getActiveCourses()));
        courseCombo.setPromptText("Select course");
        courseCombo.setPrefWidth(250);

        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.setItems(FXCollections.observableArrayList("AT1", "AT2", "FINAL"));
        typeCombo.setPromptText("Mark type");

        TextField scoreField = new TextField();
        scoreField.setPromptText("Score (0-100)");

        Label resultLabel = new Label("");

        Button submitBtn = new Button("💾 Save Mark");
        submitBtn.setStyle(
            "-fx-background-color: #27ae60;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 10 20;"
        );

        submitBtn.setOnAction(e -> {
            User u = DataStore.getInstance().findUserByEmail(studentEmail.getText().trim());
            if (!(u instanceof Student)) {
                resultLabel.setText(" Student not found!");
                resultLabel.setTextFill(Color.RED);
                return;
            }
            if (courseCombo.getValue() == null || typeCombo.getValue() == null) {
                resultLabel.setText(" Please select course and type");
                resultLabel.setTextFill(Color.ORANGE);
                return;
            }
            try {
                double score = Double.parseDouble(scoreField.getText().trim());
                controller.putMark((Student) u, courseCombo.getValue(), typeCombo.getValue(), score);
                resultLabel.setText(" Mark saved successfully!");
                resultLabel.setTextFill(Color.web("#27ae60"));
            } catch (NumberFormatException ex) {
                resultLabel.setText(" Invalid score!");
                resultLabel.setTextFill(Color.RED);
            }
        });

        form.addRow(0, new Label("Student Email:"), studentEmail);
        form.addRow(1, new Label("Course:"), courseCombo);
        form.addRow(2, new Label("Mark Type:"), typeCombo);
        form.addRow(3, new Label("Score:"), scoreField);
        form.addRow(4, submitBtn, resultLabel);

        content.getChildren().add(form);
        setContent(content);
    }

    private void showComplaintForm() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle(" Send Complaint to Dean"));

        VBox form = new VBox(15);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        TextField studentEmail = new TextField();
        studentEmail.setPromptText("Student email");

        TextArea complaintText = new TextArea();
        complaintText.setPromptText("Complaint details...");
        complaintText.setPrefRowCount(4);

        ComboBox<UrgencyLevel> urgencyCombo = new ComboBox<>();
        urgencyCombo.setItems(FXCollections.observableArrayList(UrgencyLevel.values()));
        urgencyCombo.setPromptText("Urgency Level");

        Label resultLabel = new Label("");

        Button sendBtn = new Button(" Send Complaint");
        sendBtn.setStyle(
            "-fx-background-color: #e74c3c;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 10 20;"
        );

        sendBtn.setOnAction(e -> {
            User u = DataStore.getInstance().findUserByEmail(studentEmail.getText().trim());
            if (!(u instanceof Student)) {
                resultLabel.setText(" Student not found!");
                resultLabel.setTextFill(Color.RED);
                return;
            }
            if (urgencyCombo.getValue() == null) {
                resultLabel.setText(" Select urgency level!");
                resultLabel.setTextFill(Color.ORANGE);
                return;
            }
            controller.sendComplaint((Student) u, complaintText.getText(), urgencyCombo.getValue());
            resultLabel.setText(" Complaint sent to Dean!");
            resultLabel.setTextFill(Color.web("#27ae60"));
        });

        form.getChildren().addAll(
            new Label("Student Email:"), studentEmail,
            new Label("Complaint:"), complaintText,
            new Label("Urgency:"), urgencyCombo,
            sendBtn, resultLabel
        );

        content.getChildren().add(form);
        setContent(content);
    }
}
