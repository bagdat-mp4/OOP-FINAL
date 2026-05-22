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
import exceptions.*;

import java.util.*;


public class StudentDashboard extends BaseDashboard {
    private Student student;
    private StudentController controller = new StudentController();

    public StudentDashboard(Stage stage, Student student) {
        super(stage, student);
        this.student = student;
    }

    @Override
    public void show() {
        root.setStyle("-fx-background-color: #f5f6fa;");

        
        root.setTop(createNavBar("Student Portal"));

        
        VBox sidebar = new VBox(5);
        sidebar.setPadding(new Insets(20, 10, 20, 10));
        sidebar.setStyle("-fx-background-color: #16213e; -fx-min-width: 240;");

        
        VBox userInfo = new VBox(5);
        userInfo.setPadding(new Insets(10, 10, 20, 10));
        Label nameLabel = new Label(student.getFirstName() + " " + student.getLastName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        nameLabel.setTextFill(Color.WHITE);
        Label majorLabel = new Label("📚 " + student.getMajor());
        majorLabel.setTextFill(Color.web("#aaaaaa"));
        Label yearLabel = new Label(" Year " + student.getYearOfStudy());
        yearLabel.setTextFill(Color.web("#aaaaaa"));
        userInfo.getChildren().addAll(nameLabel, majorLabel, yearLabel);

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #2d2d5e;");

        Button dashBtn = createMenuButton("Dashboard", "");
        Button coursesBtn = createMenuButton("Available Courses", "");
        Button myCoursesBtn = createMenuButton("My Courses", "");
        Button transcriptBtn = createMenuButton("Transcript", "");
        Button organizationsBtn = createMenuButton("My Organizations", "");
        Button newsBtn = createMenuButton("News", "");

        sidebar.getChildren().addAll(userInfo, sep, dashBtn, coursesBtn, myCoursesBtn, transcriptBtn, organizationsBtn, newsBtn);
        root.setLeft(sidebar);

        
        showDashboardContent();

        
        dashBtn.setOnAction(e -> showDashboardContent());
        coursesBtn.setOnAction(e -> showAvailableCourses());
        myCoursesBtn.setOnAction(e -> showMyCourses());
        transcriptBtn.setOnAction(e -> showTranscript());
        organizationsBtn.setOnAction(e -> showMyOrganizations());
        newsBtn.setOnAction(e -> showNews());

        Scene scene = new Scene(root, 1100, 700);
        stage.setScene(scene);
        stage.setTitle("Student Portal — " + student.getFirstName());
        stage.setResizable(true);
        stage.show();
    }

    private void showDashboardContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        Label title = createSectionTitle("Welcome, " + student.getFirstName() + "! 👋");

        
        HBox cards = new HBox(15);
        cards.getChildren().addAll(
            createCard("Total Credits", String.valueOf(student.getCurrentCredits()), "#4a90d9"),
            createCard("GPA", String.format("%.2f", student.getGPA()), "#27ae60"),
            createCard("Fail Count", String.valueOf(student.getFailCount()), "#e74c3c"),
            createCard("Year", String.valueOf(student.getYearOfStudy()), "#8e44ad")
        );

        
        Label newsTitle = createSectionTitle(" Latest News");
        VBox newsList = new VBox(10);
        List<News> news = DataStore.getInstance().getNews();
        int count = 0;
        for (News n : news) {
            if (count++ >= 3) break;
            HBox newsItem = new HBox(10);
            newsItem.setPadding(new Insets(12));
            newsItem.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 8;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);"
            );
            if (n.getIsPinned()) {
                Label pin = new Label("");
                newsItem.getChildren().add(pin);
            }
            Label newsLabel = new Label("[" + n.getTopic() + "] " + n.getTitle());
            newsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
            newsItem.getChildren().add(newsLabel);
            newsList.getChildren().add(newsItem);
        }

        content.getChildren().addAll(title, cards, newsTitle, newsList);
        setContent(content);
    }

    private void showAvailableCourses() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle(" Available Courses"));

        
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

        
        TableColumn<Course, Void> actionCol = new TableColumn<>("Action");
        actionCol.setPrefWidth(120);
        actionCol.setCellFactory(col -> new TableCell<>() {
            final Button btn = new Button("Register");
            {
                btn.setStyle(
                    "-fx-background-color: #27ae60;" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 6;" +
                    "-fx-cursor: hand;"
                );
                btn.setOnAction(e -> {
                    Course course = getTableView().getItems().get(getIndex());
                    try {
                        controller.registerForCourse(student, course);
                        showAlert(" Success", "Registered for: " + course.getName(), Alert.AlertType.INFORMATION);
                    } catch (CreditLimitException ex) {
                        showAlert(" Credit Limit", ex.getMessage(), Alert.AlertType.ERROR);
                    } catch (MaxFailedReachedException ex) {
                        showAlert(" Failed Limit", ex.getMessage(), Alert.AlertType.ERROR);
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        table.getColumns().addAll(codeCol, nameCol, creditsCol, typeCol, actionCol);
        table.setItems(FXCollections.observableArrayList(DataStore.getInstance().getCourses()));
        table.setPrefHeight(400);

        content.getChildren().add(table);
        setContent(content);
    }

    private void showMyCourses() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle(" My Registered Courses"));

        VBox coursesList = new VBox(10);
        if (student.getTranscript().isEmpty()) {
            Label empty = new Label("No courses registered yet.");
            empty.setTextFill(Color.web("#999999"));
            coursesList.getChildren().add(empty);
        } else {
            for (Course c : student.getTranscript().keySet()) {
                HBox item = new HBox(20);
                item.setPadding(new Insets(15));
                item.setAlignment(Pos.CENTER_LEFT);
                item.setStyle(
                    "-fx-background-color: white;" +
                    "-fx-background-radius: 10;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);"
                );
                Label courseLabel = new Label(c.getCourseCode() + " — " + c.getName());
                courseLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                Label creditsLabel = new Label(c.getCredits() + " credits");
                creditsLabel.setTextFill(Color.web("#4a90d9"));
                item.getChildren().addAll(courseLabel, creditsLabel);
                coursesList.getChildren().add(item);
            }
        }

        content.getChildren().add(coursesList);
        setContent(content);
    }

    private void showTranscript() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle(" My Transcript"));

        
        HBox stats = new HBox(15);
        stats.getChildren().addAll(
            createCard("GPA", String.format("%.2f", student.getGPA()), "#4a90d9"),
            createCard("Credits", String.valueOf(student.getCurrentCredits()), "#27ae60")
        );

        
        TableView<Map.Entry<Course, Mark>> table = new TableView<>();

        TableColumn<Map.Entry<Course, Mark>, String> courseCol = new TableColumn<>("Course");
        courseCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
            d.getValue().getKey().getName()));
        courseCol.setPrefWidth(200);

        TableColumn<Map.Entry<Course, Mark>, String> at1Col = new TableColumn<>("AT1");
        at1Col.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
            String.valueOf(d.getValue().getValue().getFirstAttestation())));
        at1Col.setPrefWidth(80);

        TableColumn<Map.Entry<Course, Mark>, String> at2Col = new TableColumn<>("AT2");
        at2Col.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
            String.valueOf(d.getValue().getValue().getSecondAttestation())));
        at2Col.setPrefWidth(80);

        TableColumn<Map.Entry<Course, Mark>, String> finalCol = new TableColumn<>("Final");
        finalCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
            String.valueOf(d.getValue().getValue().getFinalExam())));
        finalCol.setPrefWidth(80);

        TableColumn<Map.Entry<Course, Mark>, String> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
            String.format("%.1f", d.getValue().getValue().getTotal())));
        totalCol.setPrefWidth(80);

        TableColumn<Map.Entry<Course, Mark>, String> letterCol = new TableColumn<>("Grade");
        letterCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
            d.getValue().getValue().getLetter()));
        letterCol.setPrefWidth(60);

        table.getColumns().addAll(courseCol, at1Col, at2Col, finalCol, totalCol, letterCol);
        table.setItems(FXCollections.observableArrayList(student.getTranscript().entrySet()));
        table.setPrefHeight(350);

        content.getChildren().addAll(stats, table);
        setContent(content);
    }

    private void showMyOrganizations() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle(" Student Organizations"));

        
        VBox form = new VBox(10);
        form.setPadding(new Insets(15));
        form.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        TextField orgNameField = new TextField();
        orgNameField.setPromptText("Organization name");
        CheckBox isHeadCheck = new CheckBox("I am the head");
        Button joinBtn = new Button("Join / Create");
        joinBtn.setStyle(
            "-fx-background-color: #27ae60;" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 6;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 8 20;"
        );
        Label result = new Label("");
        joinBtn.setOnAction(e -> {
            String orgName = orgNameField.getText().trim();
            if (!orgName.isEmpty()) {
                controller.joinOrganization(student, orgName, isHeadCheck.isSelected());
                result.setText(" Joined: " + orgName);
                result.setTextFill(Color.web("#27ae60"));
                orgNameField.clear();
                isHeadCheck.setSelected(false);
                
                showMyOrganizations();
            } else {
                result.setText("Please enter organization name");
                result.setTextFill(Color.web("#e74c3c"));
            }
        });
        form.getChildren().addAll(new Label("Join Organization:"), orgNameField, isHeadCheck, joinBtn, result);

        content.getChildren().add(form);
        content.getChildren().add(createSectionTitle("My Organizations"));

        VBox orgList = new VBox(10);
        List<StudentOrganization> organizations = student.getOrganizations();

        if (organizations == null || organizations.isEmpty()) {
            Label empty = new Label("You are not a member of any organization yet.");
            empty.setTextFill(Color.web("#999999"));
            empty.setFont(Font.font("Arial", 14));
            orgList.getChildren().add(empty);
        } else {
            for (StudentOrganization org : organizations) {
                VBox orgCard = new VBox(10);
                orgCard.setPadding(new Insets(20));
                orgCard.setStyle(
                    "-fx-background-color: white;" +
                    "-fx-background-radius: 10;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);"
                );

                
                Label nameLabel = new Label(org.getName());
                nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
                nameLabel.setTextFill(Color.web("#16213e"));

                
                HBox headBox = new HBox(10);
                headBox.setAlignment(Pos.CENTER_LEFT);
                Label headIcon = new Label("");
                headIcon.setFont(Font.font(14));
                Label headLabel = new Label("Head: " +
                    (org.getHead() != null ?
                        org.getHead().getFirstName() + " " + org.getHead().getLastName() :
                        "Not assigned"));
                headLabel.setFont(Font.font("Arial", 13));
                headLabel.setTextFill(Color.web("#555555"));
                headBox.getChildren().addAll(headIcon, headLabel);

                
                HBox memberBox = new HBox(10);
                memberBox.setAlignment(Pos.CENTER_LEFT);
                Label memberIcon = new Label("");
                memberIcon.setFont(Font.font(14));
                Label memberLabel = new Label("Members: " + org.getMembers().size());
                memberLabel.setFont(Font.font("Arial", 13));
                memberLabel.setTextFill(Color.web("#555555"));
                memberBox.getChildren().addAll(memberIcon, memberLabel);

                
                if (org.getHead() != null && org.getHead().equals(student)) {
                    Label roleLabel = new Label(" You are the head of this organization");
                    roleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
                    roleLabel.setTextFill(Color.web("#f39c12"));
                    orgCard.getChildren().addAll(nameLabel, headBox, memberBox, roleLabel);
                } else {
                    orgCard.getChildren().addAll(nameLabel, headBox, memberBox);
                }

                orgList.getChildren().add(orgCard);
            }

            
            HBox stats = new HBox(15);
            stats.setPadding(new Insets(10, 0, 0, 0));
            stats.getChildren().add(
                createCard("Total Organizations", String.valueOf(organizations.size()), "#8e44ad")
            );
            content.getChildren().add(stats);
        }

        content.getChildren().add(orgList);
        setContent(content);
    }

    private void showNews() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle(" University News"));

        VBox newsList = new VBox(10);
        List<News> news = DataStore.getInstance().getNews();

        
        news.stream().filter(News::getIsPinned).forEach(n -> newsList.getChildren().add(createNewsCard(n)));
        news.stream().filter(n -> !n.getIsPinned()).forEach(n -> newsList.getChildren().add(createNewsCard(n)));

        content.getChildren().add(newsList);
        setContent(content);
    }

    private VBox createNewsCard(News news) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(15));
        String borderColor = news.getIsPinned() ? "#f39c12" : "#4a90d9";
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: " + borderColor + ";" +
            "-fx-border-width: 0 0 0 4;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);"
        );

        Label titleLabel = new Label((news.getIsPinned() ? " " : "") + news.getTitle());
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Label topicLabel = new Label("Topic: " + news.getTopic());
        topicLabel.setTextFill(Color.web("#888888"));
        topicLabel.setFont(Font.font("Arial", 12));

        Label contentLabel = new Label(news.getContent());
        contentLabel.setWrapText(true);

        card.getChildren().addAll(titleLabel, topicLabel, contentLabel);
        return card;
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
