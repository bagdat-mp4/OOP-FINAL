package gui;

import controllers.StudentController;
import core.DataStore;
import exceptions.LowHIndexException;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import models.GraduateStudent;
import models.ResearchPaper;
import models.ResearcherDecorator;
import models.User;


public class GraduateStudentDashboard extends StudentDashboard {

    private final GraduateStudent gradStudent;

    public GraduateStudentDashboard(Stage stage, GraduateStudent gradStudent) {
        super(stage, gradStudent);
        this.gradStudent = gradStudent;
    }

    @Override
    public void show() {
        root = new javafx.scene.layout.BorderPane();
        root.setStyle("-fx-background-color: #f5f6fa;");
        root.setTop(createNavBar("Graduate Student Portal"));

        VBox sidebar = new VBox(5);
        sidebar.setPadding(new Insets(20, 10, 20, 10));
        sidebar.setStyle("-fx-background-color: #16213e; -fx-min-width: 240;");

        VBox userInfo = new VBox(5);
        userInfo.setPadding(new Insets(10, 10, 20, 10));
        Label nameLabel = new Label(gradStudent.getFirstName() + " " + gradStudent.getLastName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        nameLabel.setTextFill(Color.WHITE);
        Label majorLabel = new Label(gradStudent.getMajor());
        majorLabel.setTextFill(Color.web("#aaaaaa"));
        Label supLabel = new Label("Supervisor: " + (gradStudent.getSupervisor() != null
            ? getSupervisorName() : "Not assigned"));
        supLabel.setTextFill(Color.web("#f39c12"));
        userInfo.getChildren().addAll(nameLabel, majorLabel, supLabel);

        Button dashBtn       = createMenuButton("Dashboard", "");
        Button coursesBtn    = createMenuButton("Available Courses", "");
        Button myCoursesBtn  = createMenuButton("My Courses", "");
        Button transcriptBtn = createMenuButton("Transcript", "");
        Button newsBtn       = createMenuButton("News", "");
        Button supervisorBtn = createMenuButton("Set Supervisor", "");
        Button diplomaBtn    = createMenuButton("Diploma Projects", "");
        Button addDiplomaBtn = createMenuButton("Add Diploma Project", "");

        sidebar.getChildren().addAll(userInfo, new Separator(),
            dashBtn, coursesBtn, myCoursesBtn, transcriptBtn, newsBtn,
            new Separator(), supervisorBtn, diplomaBtn, addDiplomaBtn);

        if (DataStore.getInstance().isResearcher(gradStudent)) {
            Button researchBtn = createMenuButton("Researcher Mode", "");
            researchBtn.setStyle(researchBtn.getStyle() + "-fx-text-fill: #f39c12;");
            sidebar.getChildren().add(researchBtn);
            researchBtn.setOnAction(e -> new ResearcherDashboard(stage, gradStudent, this::show).show());
        }

        addResearcherRequestButton(sidebar);
        root.setLeft(sidebar);

        showDashboardContent();

        dashBtn.setOnAction(e -> showDashboardContent());
        coursesBtn.setOnAction(e -> showAvailableCourses());
        myCoursesBtn.setOnAction(e -> showMyCourses());
        transcriptBtn.setOnAction(e -> showTranscript());
        newsBtn.setOnAction(e -> showNews());
        supervisorBtn.setOnAction(e -> showSetSupervisor());
        diplomaBtn.setOnAction(e -> showDiplomaProjects());
        addDiplomaBtn.setOnAction(e -> showAddDiplomaProject());

        Scene scene = new Scene(root, 1100, 700);
        stage.setScene(scene);
        stage.setTitle("Graduate Student Portal — " + gradStudent.getFirstName());
        stage.show();
    }

    private void showSetSupervisor() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle("Set Supervisor"));

        VBox form = new VBox(12);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        Label currentSup = new Label("Current: " + (gradStudent.getSupervisor() != null
            ? getSupervisorName() : "Not assigned"));
        currentSup.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        currentSup.setTextFill(Color.web("#4a90d9"));

        ComboBox<User> researcherCombo = new ComboBox<>();
        researcherCombo.setItems(javafx.collections.FXCollections.observableArrayList(
            DataStore.getInstance().getResearcherMap().keySet()
        ));
        researcherCombo.setPromptText("Select researcher");
        researcherCombo.setPrefWidth(300);
        researcherCombo.setConverter(new javafx.util.StringConverter<User>() {
            @Override public String toString(User u) {
                if (u == null) return "";
                ResearcherDecorator rd = DataStore.getInstance().getResearcher(u);
                double h = rd != null ? rd.calculateHIndex() : 0;
                return u.getFirstName() + " " + u.getLastName() + " (H-Index: " + (int) h + ")";
            }
            @Override public User fromString(String s) { return null; }
        });

        Label resultLabel = new Label("");

        Button setBtn = new Button("Set as Supervisor");
        setBtn.setStyle(
            "-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14;" +
            "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 10 20;"
        );

        setBtn.setOnAction(e -> {
            User selected = researcherCombo.getValue();
            if (selected == null) {
                resultLabel.setText("Please select a researcher.");
                resultLabel.setTextFill(Color.ORANGE);
                return;
            }
            ResearcherDecorator rd = DataStore.getInstance().getResearcher(selected);
            if (rd == null) {
                resultLabel.setText("Selected user is not a researcher.");
                resultLabel.setTextFill(Color.RED);
                return;
            }
            try {
                gradStudent.setSupervisor(rd);
                resultLabel.setText("Supervisor set: " + selected.getFirstName() + " " + selected.getLastName());
                resultLabel.setTextFill(Color.web("#27ae60"));
                currentSup.setText("Current: " + getSupervisorName());
            } catch (LowHIndexException ex) {
                resultLabel.setText(ex.getMessage());
                resultLabel.setTextFill(Color.RED);
            }
        });

        form.getChildren().addAll(currentSup, new Label("Select Researcher:"), researcherCombo, setBtn, resultLabel);
        content.getChildren().add(form);
        setContent(content);
    }

    private void showDiplomaProjects() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle("Diploma Projects"));

        if (gradStudent.getDiplomaProjects().isEmpty()) {
            Label empty = new Label("No diploma projects yet.");
            empty.setTextFill(Color.web("#999999"));
            content.getChildren().add(empty);
        } else {
            VBox list = new VBox(10);
            for (ResearchPaper p : gradStudent.getDiplomaProjects()) {
                VBox card = new VBox(6);
                card.setPadding(new Insets(15));
                card.setStyle(
                    "-fx-background-color: white; -fx-background-radius: 10;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);"
                );
                Label title = new Label(p.getTitle());
                title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                Label info = new Label("Journal: " + p.getJournal() + "  |  Citations: " + p.getCitations());
                info.setTextFill(Color.web("#666666"));
                card.getChildren().addAll(title, info);
                list.getChildren().add(card);
            }
            content.getChildren().add(list);
        }
        setContent(content);
    }

    private void showAddDiplomaProject() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle("Add Diploma Project"));

        if (gradStudent.getSupervisor() == null) {
            Label warn = new Label("You must set a supervisor before adding a diploma project.");
            warn.setTextFill(Color.web("#e74c3c"));
            warn.setFont(Font.font("Arial", 14));
            content.getChildren().add(warn);
            setContent(content);
            return;
        }

        VBox form = new VBox(12);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        javafx.scene.control.TextField titleField   = new javafx.scene.control.TextField();
        titleField.setPromptText("Project title");   titleField.setPrefWidth(300);
        javafx.scene.control.TextField journalField = new javafx.scene.control.TextField();
        journalField.setPromptText("Journal / Conference"); journalField.setPrefWidth(300);
        javafx.scene.control.TextField doiField     = new javafx.scene.control.TextField();
        doiField.setPromptText("DOI");               doiField.setPrefWidth(300);
        javafx.scene.control.TextField pagesField   = new javafx.scene.control.TextField();
        pagesField.setPromptText("Pages (> 0)");     pagesField.setPrefWidth(300);

        Label resultLabel = new Label("");

        Button addBtn = new Button("Add Project");
        addBtn.setStyle(
            "-fx-background-color: #8e44ad; -fx-text-fill: white; -fx-font-size: 14;" +
            "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 10 20;"
        );

        addBtn.setOnAction(e -> {
            String title   = titleField.getText().trim();
            String journal = journalField.getText().trim();
            String doi     = doiField.getText().trim();
            try {
                int pages = Integer.parseInt(pagesField.getText().trim());
                if (title.isEmpty() || journal.isEmpty() || doi.isEmpty() || pages <= 0) {
                    resultLabel.setText("All fields required. Pages must be > 0.");
                    resultLabel.setTextFill(Color.ORANGE);
                    return;
                }
                ResearchPaper project = new ResearchPaper(title, journal, doi, pages, new java.util.Date());
                gradStudent.getDiplomaProjects().add(project);
                resultLabel.setText("Diploma project added: " + title);
                resultLabel.setTextFill(Color.web("#27ae60"));
                titleField.clear(); journalField.clear(); doiField.clear(); pagesField.clear();
            } catch (NumberFormatException ex) {
                resultLabel.setText("Pages must be a number.");
                resultLabel.setTextFill(Color.RED);
            }
        });

        form.getChildren().addAll(
            new Label("Title:"), titleField,
            new Label("Journal/Conference:"), journalField,
            new Label("DOI:"), doiField,
            new Label("Pages:"), pagesField,
            addBtn, resultLabel
        );
        content.getChildren().add(form);
        setContent(content);
    }

    private String getSupervisorName() {
        if (gradStudent.getSupervisor() instanceof ResearcherDecorator) {
            User u = ((ResearcherDecorator) gradStudent.getSupervisor()).getOriginalUser();
            return u.getFirstName() + " " + u.getLastName();
        }
        return "Unknown";
    }
}
