package gui;

import controllers.EmployeeMessageController;
import controllers.TeacherController;
import core.DataStore;
import enums.UrgencyLevel;
import models.RecommendationLetter;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import models.Course;
import models.Student;
import models.Teacher;
import models.User;


public class TeacherDashboard extends BaseDashboard {

    private final Teacher teacher;
    private final TeacherController controller = new TeacherController();

    public TeacherDashboard(Stage stage, Teacher teacher) {
        super(stage, teacher);
        this.teacher = teacher;
    }

    @Override
    public void show() {
        root = new javafx.scene.layout.BorderPane();
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
        Label titleLabel = new Label(teacher.getTitle() != null ? teacher.getTitle().toString() : "Teacher");
        titleLabel.setTextFill(Color.web("#aaaaaa"));
        Label ratingLabel = new Label("Rating: " + String.format("%.1f", teacher.getRating()));
        ratingLabel.setTextFill(Color.web("#f39c12"));
        userInfo.getChildren().addAll(nameLabel, titleLabel, ratingLabel);

        Button dashBtn        = createMenuButton("Dashboard", "");
        Button coursesBtn     = createMenuButton("My Courses", "");
        Button marksBtn       = createMenuButton("Put Marks", "");
        Button markReportBtn  = createMenuButton("Mark Report", "");
        Button attendanceBtn  = createMenuButton("Mark Attendance", "");
        Button attReportBtn   = createMenuButton("Attendance Report", "");
        Button letterBtn      = createMenuButton("Recommendation Letter", "");
        Button newsBtn        = createMenuButton("News", "");
        Button complaintBtn   = createMenuButton("Send Complaint", "");
        Button sendMsgBtn     = createMenuButton("Send Message", "");
        Button inboxBtn       = createMenuButton("Inbox", "");
        Button techSupportBtn = createMenuButton("Tech Support", "");

        sidebar.getChildren().addAll(userInfo, new Separator(),
            dashBtn, coursesBtn, marksBtn, markReportBtn,
            attendanceBtn, attReportBtn, letterBtn,
            newsBtn, complaintBtn, sendMsgBtn, inboxBtn, techSupportBtn);

        if (core.DataStore.getInstance().isResearcher(teacher)) {
            Button researchBtn = createMenuButton("Researcher Mode", "");
            researchBtn.setStyle(researchBtn.getStyle() + "-fx-text-fill: #f39c12;");
            sidebar.getChildren().add(researchBtn);
            researchBtn.setOnAction(e -> new ResearcherDashboard(stage, teacher, this::show).show());
        }

        addResearcherRequestButton(sidebar);
        root.setLeft(sidebar);

        showDashboardContent();

        dashBtn.setOnAction(e -> showDashboardContent());
        coursesBtn.setOnAction(e -> showCourses());
        marksBtn.setOnAction(e -> showPutMarks());
        markReportBtn.setOnAction(e -> showMarkReport());
        attendanceBtn.setOnAction(e -> showMarkAttendance());
        attReportBtn.setOnAction(e -> showAttendanceReport());
        letterBtn.setOnAction(e -> showWriteLetter());
        newsBtn.setOnAction(e -> showNews());
        complaintBtn.setOnAction(e -> showComplaintForm());
        sendMsgBtn.setOnAction(e -> showSendMessage());
        inboxBtn.setOnAction(e -> showInbox());
        techSupportBtn.setOnAction(e -> showTechSupportForm());

        Scene scene = new Scene(root, 1100, 700);
        stage.setScene(scene);
        stage.setTitle("Teacher Portal - " + teacher.getFirstName());
        stage.show();
    }

    private void showDashboardContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        long studentCount = teacher.getActiveCourses().stream()
            .flatMap(c -> c.getEnrolledStudents().stream())
            .distinct()
            .count();

        HBox cards = new HBox(15);
        cards.getChildren().addAll(
            createCard("My Courses", String.valueOf(teacher.getActiveCourses().size()), "#4a90d9"),
            createCard("Students", String.valueOf((int) studentCount), "#27ae60"),
            createCard("Rating", String.format("%.1f", teacher.getRating()), "#f39c12")
        );

        content.getChildren().addAll(
            createSectionTitle("Welcome, Prof. " + teacher.getFirstName() + "!"),
            cards
        );
        setContent(content);
    }

    private void showCourses() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle("My Courses"));

        VBox coursesList = new VBox(10);
        for (Course c : teacher.getActiveCourses()) {
            VBox card = new VBox(8);
            card.setPadding(new Insets(15));
            card.setStyle(
                "-fx-background-color: white; -fx-background-radius: 10;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);"
            );
            Label courseName = new Label(c.getCourseCode() + " - " + c.getName());
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
        content.getChildren().add(createSectionTitle("Put Marks"));

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
        scoreField.setPromptText("Score");
        typeCombo.setOnAction(e -> {
            String t = typeCombo.getValue();
            if (t == null) return;
            scoreField.setPromptText("Score  (" + controller.getScoreRange(t) + ")");
        });

        Label resultLabel = new Label("");

        Button submitBtn = new Button("Save Mark");
        submitBtn.setStyle(
            "-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14;" +
            "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 10 20;"
        );

        submitBtn.setOnAction(e -> {
            User u = DataStore.getInstance().findUserByEmail(studentEmail.getText().trim());
            if (!(u instanceof Student)) {
                resultLabel.setText("Student not found!");
                resultLabel.setTextFill(Color.RED);
                return;
            }
            if (courseCombo.getValue() == null || typeCombo.getValue() == null) {
                resultLabel.setText("Please select course and type");
                resultLabel.setTextFill(Color.ORANGE);
                return;
            }
            try {
                double score = Double.parseDouble(scoreField.getText().trim());
                String error = controller.getMarkError(
                    (Student) u, courseCombo.getValue(), typeCombo.getValue(), score);
                if (!error.isEmpty()) {
                    resultLabel.setText(error);
                    resultLabel.setTextFill(Color.RED);
                } else {
                    controller.putMark((Student) u, courseCombo.getValue(), typeCombo.getValue(), score);
                    resultLabel.setText("Mark saved successfully!");
                    resultLabel.setTextFill(Color.web("#27ae60"));
                }
            } catch (NumberFormatException ex) {
                resultLabel.setText("Invalid number!");
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

    private void showInbox() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle("Inbox"));

        if (teacher.getInbox().isEmpty()) {
            Label empty = new Label("No messages.");
            empty.setTextFill(Color.web("#999999"));
            content.getChildren().add(empty);
        } else {
            VBox list = new VBox(8);
            for (models.Message m : teacher.getInbox()) {
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

    private void showComplaintForm() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle("Send Complaint to Dean"));

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

        Button sendBtn = new Button("Send Complaint");
        sendBtn.setStyle(
            "-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14;" +
            "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 10 20;"
        );

        sendBtn.setOnAction(e -> {
            User u = DataStore.getInstance().findUserByEmail(studentEmail.getText().trim());
            if (!(u instanceof Student)) {
                resultLabel.setText("Student not found!");
                resultLabel.setTextFill(Color.RED);
                return;
            }
            if (urgencyCombo.getValue() == null) {
                resultLabel.setText("Select urgency level!");
                resultLabel.setTextFill(Color.ORANGE);
                return;
            }
            controller.sendComplaint(teacher, (Student) u, complaintText.getText(), urgencyCombo.getValue());
            resultLabel.setText("Complaint sent to Dean!");
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

    private void showMarkReport() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle("Mark Report"));
        TextArea area = new TextArea(controller.generateMarkReport(teacher));
        area.setEditable(false);
        area.setPrefRowCount(20);
        area.setStyle("-fx-font-family: monospace; -fx-font-size: 13;");
        content.getChildren().add(area);
        setContent(content);
    }

    private void showMarkAttendance() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle("Mark Attendance"));

        ComboBox<Course> courseCombo = new ComboBox<>();
        courseCombo.setItems(javafx.collections.FXCollections.observableArrayList(teacher.getActiveCourses()));
        courseCombo.setPromptText("Select course");
        courseCombo.setPrefWidth(300);

        VBox studentList = new VBox(8);
        Label resultLabel = new Label("");

        courseCombo.setOnAction(e -> {
            studentList.getChildren().clear();
            Course c = courseCombo.getValue();
            if (c == null) return;
            for (Student s : c.getEnrolledStudents()) {
                javafx.scene.layout.HBox row = new javafx.scene.layout.HBox(15);
                row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                Label name = new Label(s.getFirstName() + " " + s.getLastName());
                name.setPrefWidth(200);
                Button presentBtn = new Button("Present");
                presentBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 14;");
                Button absentBtn  = new Button("Absent");
                absentBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 14;");
                presentBtn.setOnAction(ev -> { controller.markAttendance(c, s, true);  resultLabel.setText("Saved: " + s.getFirstName() + " PRESENT"); resultLabel.setTextFill(Color.web("#27ae60")); });
                absentBtn.setOnAction(ev ->  { controller.markAttendance(c, s, false); resultLabel.setText("Saved: " + s.getFirstName() + " ABSENT");  resultLabel.setTextFill(Color.web("#e74c3c")); });
                row.getChildren().addAll(name, presentBtn, absentBtn);
                studentList.getChildren().add(row);
            }
            if (c.getEnrolledStudents().isEmpty()) studentList.getChildren().add(new Label("No students enrolled."));
        });

        content.getChildren().addAll(new Label("Course:"), courseCombo, studentList, resultLabel);
        setContent(content);
    }

    private void showAttendanceReport() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle("Attendance Report"));

        ComboBox<Course> courseCombo = new ComboBox<>();
        courseCombo.setItems(javafx.collections.FXCollections.observableArrayList(teacher.getActiveCourses()));
        courseCombo.setPromptText("Select course");
        courseCombo.setPrefWidth(300);

        TextArea area = new TextArea();
        area.setEditable(false);
        area.setPrefRowCount(15);
        area.setStyle("-fx-font-family: monospace; -fx-font-size: 13;");

        courseCombo.setOnAction(e -> {
            Course c = courseCombo.getValue();
            if (c != null) area.setText(controller.getAttendanceReport(c));
        });

        content.getChildren().addAll(new Label("Course:"), courseCombo, area);
        setContent(content);
    }

    private void showWriteLetter() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle("Write Recommendation Letter"));

        VBox form = new VBox(12);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        TextField emailField = new TextField();
        emailField.setPromptText("Student email");
        emailField.setPrefWidth(300);

        TextField purposeField = new TextField();
        purposeField.setPromptText("Purpose (Graduate School / Scholarship / Job)");
        purposeField.setPrefWidth(300);

        Label resultLabel = new Label("");

        Button writeBtn = new Button("Generate & Save Letter");
        writeBtn.setStyle("-fx-background-color: #4a90d9; -fx-text-fill: white; -fx-font-size: 14; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 10 20;");

        TextArea preview = new TextArea();
        preview.setEditable(false);
        preview.setPrefRowCount(12);
        preview.setStyle("-fx-font-family: monospace;");

        writeBtn.setOnAction(e -> {
            User u = DataStore.getInstance().findUserByEmail(emailField.getText().trim());
            if (!(u instanceof Student)) { resultLabel.setText("Student not found!"); resultLabel.setTextFill(Color.RED); return; }
            String purpose = purposeField.getText().trim();
            if (purpose.isEmpty()) { resultLabel.setText("Please enter purpose."); resultLabel.setTextFill(Color.ORANGE); return; }
            Student s = (Student) u;
            String body = "I am writing to recommend " + s.getFirstName() + " " + s.getLastName() + " for " + purpose + ".\n\n" +
                "During their studies, " + s.getFirstName() + " has demonstrated exceptional dedication. " +
                "Their current GPA is " + String.format("%.2f", s.getGPA()) + "/4.0.\n\n" +
                "I highly recommend " + s.getFirstName() + " without reservation.";
            RecommendationLetter letter = new RecommendationLetter(teacher, s, body, purpose);
            DataStore.getInstance().addLetter(letter);
            preview.setText(letter.toString());
            resultLabel.setText("Letter saved!");
            resultLabel.setTextFill(Color.web("#27ae60"));
        });

        form.getChildren().addAll(new Label("Student Email:"), emailField, new Label("Purpose:"), purposeField, writeBtn, resultLabel);
        content.getChildren().addAll(form, preview);
        setContent(content);
    }

    private void showNews() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle("University News"));
        VBox list = new VBox(8);
        DataStore.getInstance().getNews().forEach(n -> {
            VBox card = new VBox(4);
            card.setPadding(new Insets(12));
            String border = n.isPinned() ? "#f39c12" : "#4a90d9";
            card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-border-color: " + border + "; -fx-border-width: 0 0 0 4;");
            Label title = new Label((n.isPinned() ? "[PINNED] " : "") + n.getTitle());
            title.setFont(Font.font("Arial", FontWeight.BOLD, 13));
            Label body = new Label(n.getContent());
            body.setWrapText(true);
            card.getChildren().addAll(title, body);
            list.getChildren().add(card);
        });
        content.getChildren().add(list.getChildren().isEmpty() ? new Label("No news.") : list);
        setContent(content);
    }

    private void showSendMessage() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle("Send Message to Employee"));

        VBox form = new VBox(12);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        TextField toField = new TextField();
        toField.setPromptText("Recipient email");
        toField.setPrefWidth(300);

        TextArea msgArea = new TextArea();
        msgArea.setPromptText("Message...");
        msgArea.setPrefRowCount(5);

        Label resultLabel = new Label("");

        Button sendBtn = new Button("Send");
        sendBtn.setStyle("-fx-background-color: #4a90d9; -fx-text-fill: white; -fx-font-size: 14; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 10 20;");
        sendBtn.setOnAction(e -> {
            User receiver = DataStore.getInstance().findUserByEmail(toField.getText().trim());
            if (!(receiver instanceof models.Employee)) { resultLabel.setText("Employee not found!"); resultLabel.setTextFill(Color.RED); return; }
            String msg = msgArea.getText().trim();
            if (msg.isEmpty()) { resultLabel.setText("Message cannot be empty."); resultLabel.setTextFill(Color.ORANGE); return; }
            new EmployeeMessageController().sendEmployeeMessage(teacher, receiver, msg);
            resultLabel.setText("Message sent!");
            resultLabel.setTextFill(Color.web("#27ae60"));
            toField.clear(); msgArea.clear();
        });

        form.getChildren().addAll(new Label("To:"), toField, new Label("Message:"), msgArea, sendBtn, resultLabel);
        content.getChildren().add(form);
        setContent(content);
    }
}
