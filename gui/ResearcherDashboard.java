package gui;

import controllers.ResearcherController;
import core.DataStore;
import enums.CitationFormat;
import exceptions.NotAResearcherException;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.ResearchPaper;
import models.ResearchProject;
import models.ResearcherDecorator;
import models.User;

import java.util.Date;
import java.util.List;


public class ResearcherDashboard extends BaseDashboard {

    private final User researcher;
    private final ResearcherController controller = new ResearcherController();
    private final Runnable onBack;

    public ResearcherDashboard(Stage stage, User researcher, Runnable onBack) {
        super(stage, researcher);
        this.researcher = researcher;
        this.onBack = onBack;
    }

    @Override
    public void show() {
        root = new javafx.scene.layout.BorderPane();
        root.setStyle("-fx-background-color: #f5f6fa;");
        root.setTop(createNavBar("Researcher Portal"));

        VBox sidebar = buildSidebar();
        root.setLeft(sidebar);

        showDashboard();

        Scene scene = new Scene(root, 1100, 700);
        stage.setScene(scene);
        stage.setTitle("Researcher Portal — " + researcher.getFirstName());
        stage.show();
    }

    private VBox buildSidebar() {
        ResearcherDecorator rd = DataStore.getInstance().getResearcher(researcher);

        VBox sidebar = new VBox(5);
        sidebar.setPadding(new Insets(20, 10, 20, 10));
        sidebar.setStyle("-fx-background-color: #16213e; -fx-min-width: 240;");

        VBox userInfo = new VBox(5);
        userInfo.setPadding(new Insets(10, 10, 20, 10));
        Label nameLabel = new Label(researcher.getFirstName() + " " + researcher.getLastName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        nameLabel.setTextFill(Color.WHITE);
        Label hLabel = new Label("H-Index: " + (rd != null ? (int) rd.calculateHIndex() : 0));
        hLabel.setTextFill(Color.web("#f39c12"));
        userInfo.getChildren().addAll(nameLabel, hLabel);

        Button dashBtn     = createMenuButton("Dashboard", "");
        Button addPaperBtn = createMenuButton("Add Paper", "");
        Button papersBtn   = createMenuButton("My Papers", "");
        Button projectsBtn = createMenuButton("My Projects", "");
        Button newProjBtn  = createMenuButton("New Project", "");

        Button backBtn = new Button("Back to Main");
        backBtn.setPrefWidth(220);
        backBtn.setPrefHeight(40);
        backBtn.setStyle(
            "-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 13;" +
            "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 0 20;"
        );

        dashBtn.setOnAction(e -> showDashboard());
        addPaperBtn.setOnAction(e -> showAddPaper());
        papersBtn.setOnAction(e -> showPapers("citations"));
        projectsBtn.setOnAction(e -> showProjects());
        newProjBtn.setOnAction(e -> showNewProject());
        backBtn.setOnAction(e -> onBack.run());

        sidebar.getChildren().addAll(
            userInfo, new Separator(),
            dashBtn, addPaperBtn, papersBtn, projectsBtn, newProjBtn,
            new Separator(), backBtn
        );
        return sidebar;
    }

    private void showDashboard() {
        ResearcherDecorator rd = DataStore.getInstance().getResearcher(researcher);
        int papers    = rd != null ? rd.getPapers().size()    : 0;
        int projects  = rd != null ? rd.getProjects().size()  : 0;
        int hIndex    = rd != null ? (int) rd.calculateHIndex() : 0;
        int citations = rd != null ? rd.getPapers().stream().mapToInt(ResearchPaper::getCitations).sum() : 0;

        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        HBox cards = new HBox(15);
        cards.getChildren().addAll(
            createCard("H-Index",         String.valueOf(hIndex),   "#4a90d9"),
            createCard("Papers",          String.valueOf(papers),   "#27ae60"),
            createCard("Projects",        String.valueOf(projects), "#8e44ad"),
            createCard("Total Citations", String.valueOf(citations), "#f39c12")
        );

        content.getChildren().addAll(
            createSectionTitle("Researcher Dashboard — " + researcher.getFirstName()),
            cards
        );
        setContent(content);
    }

    private void showAddPaper() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle("Add Research Paper"));

        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        TextField titleField   = field("Paper title");
        TextField journalField = field("Journal name");
        TextField doiField     = field("e.g. 10.1000/xyz123");
        TextField pagesField   = field("Number of pages (> 0)");
        TextField citField     = field("Initial citations (≥ 0)");
        Label resultLabel = new Label("");

        Button addBtn = new Button("Publish Paper");
        addBtn.setStyle(
            "-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14;" +
            "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 10 20;"
        );

        addBtn.setOnAction(e -> {
            try {
                int pages     = Integer.parseInt(pagesField.getText().trim());
                int citations = Integer.parseInt(citField.getText().trim());

                ResearchPaper paper = new ResearchPaper(
                    titleField.getText().trim(), journalField.getText().trim(),
                    doiField.getText().trim(), pages, new Date()
                );
                paper.setCitations(citations);

                String validationError = controller.validatePaper(paper);
                if (!validationError.isEmpty()) {
                    resultLabel.setText(validationError);
                    resultLabel.setTextFill(Color.RED);
                    return;
                }

                if (controller.addResearchPaper(researcher, paper)) {
                    resultLabel.setText("Paper published successfully!");
                    resultLabel.setTextFill(Color.web("#27ae60"));
                    titleField.clear(); journalField.clear(); doiField.clear();
                    pagesField.clear(); citField.clear();
                    root.setLeft(buildSidebar());
                } else {
                    resultLabel.setText("Error: user is not a researcher.");
                    resultLabel.setTextFill(Color.RED);
                }
            } catch (NumberFormatException ex) {
                resultLabel.setText("Pages and citations must be whole numbers.");
                resultLabel.setTextFill(Color.RED);
            }
        });

        form.addRow(0, new Label("Title:"),     titleField);
        form.addRow(1, new Label("Journal:"),   journalField);
        form.addRow(2, new Label("DOI:"),       doiField);
        form.addRow(3, new Label("Pages:"),     pagesField);
        form.addRow(4, new Label("Citations:"), citField);
        form.addRow(5, addBtn, resultLabel);

        content.getChildren().add(form);
        setContent(content);
    }

    private void showPapers(String sortBy) {
        VBox content = new VBox(15);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle("My Research Papers"));

        HBox sortBar = new HBox(10);
        sortBar.getChildren().addAll(
            new Label("Sort: "),
            sortBtn("By Citations", "#4a90d9", () -> showPapers("citations")),
            sortBtn("By Date",      "#27ae60", () -> showPapers("date")),
            sortBtn("By Pages",     "#8e44ad", () -> showPapers("pages"))
        );

        ResearcherDecorator rd = DataStore.getInstance().getResearcher(researcher);
        if (rd == null || rd.getPapers().isEmpty()) {
            content.getChildren().addAll(sortBar, emptyLabel("No papers yet."));
            setContent(content);
            return;
        }

        List<ResearchPaper> papers = controller.getSortedPapers(rd.getPapers(), sortBy);

        TableView<ResearchPaper> table = new TableView<>();
        table.setStyle("-fx-background-radius: 10;");

        TableColumn<ResearchPaper, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getTitle()));
        titleCol.setPrefWidth(240);

        TableColumn<ResearchPaper, String> journalCol = new TableColumn<>("Journal");
        journalCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getJournal()));
        journalCol.setPrefWidth(170);

        TableColumn<ResearchPaper, Integer> citCol = new TableColumn<>("Citations");
        citCol.setCellValueFactory(d -> new javafx.beans.property.SimpleIntegerProperty(
            d.getValue().getCitations()).asObject());
        citCol.setPrefWidth(90);

        TableColumn<ResearchPaper, Integer> pagesCol = new TableColumn<>("Pages");
        pagesCol.setCellValueFactory(d -> new javafx.beans.property.SimpleIntegerProperty(
            d.getValue().getPages()).asObject());
        pagesCol.setPrefWidth(70);

        TableColumn<ResearchPaper, String> doiCol = new TableColumn<>("DOI");
        doiCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getDoi()));
        doiCol.setPrefWidth(160);

        TableColumn<ResearchPaper, Void> citationCol = new TableColumn<>("Citation");
        citationCol.setPrefWidth(90);
        citationCol.setCellFactory(col -> new TableCell<>() {
            final Button btn = new Button("BibTeX");
            { btn.setStyle("-fx-background-color: #4a90d9; -fx-text-fill: white;" +
                          "-fx-background-radius: 4; -fx-cursor: hand; -fx-padding: 4 10;");
              btn.setOnAction(e -> showCitationPopup(getTableView().getItems().get(getIndex()))); }
            @Override protected void updateItem(Void v, boolean empty) {
                super.updateItem(v, empty);
                setGraphic(empty ? null : btn);
            }
        });

        table.getColumns().addAll(titleCol, journalCol, citCol, pagesCol, doiCol, citationCol);
        table.setItems(FXCollections.observableArrayList(papers));
        table.setPrefHeight(400);

        content.getChildren().addAll(sortBar, table);
        setContent(content);
    }

    private void showCitationPopup(ResearchPaper paper) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Citation — " + paper.getTitle());

        VBox box = new VBox(12);
        box.setPadding(new Insets(20));

        TextArea plain = new TextArea(paper.getCitation(CitationFormat.PLAIN_TEXT));
        plain.setEditable(false);
        plain.setPrefRowCount(3);
        plain.setWrapText(true);

        TextArea bibtex = new TextArea(paper.getCitation(CitationFormat.BIBTEX));
        bibtex.setEditable(false);
        bibtex.setPrefRowCount(8);

        box.getChildren().addAll(new Label("Plain Text:"), plain, new Label("BibTeX:"), bibtex);
        popup.setScene(new Scene(box, 520, 360));
        popup.show();
    }

    private void showProjects() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle("My Research Projects"));

        ResearcherDecorator rd = DataStore.getInstance().getResearcher(researcher);
        if (rd == null || rd.getProjects().isEmpty()) {
            content.getChildren().add(emptyLabel("No projects yet."));
            setContent(content);
            return;
        }

        VBox list = new VBox(10);
        for (ResearchProject p : rd.getProjects()) {
            VBox card = new VBox(6);
            card.setPadding(new Insets(15));
            card.setStyle(
                "-fx-background-color: white; -fx-background-radius: 10;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 6, 0, 0, 2);"
            );
            Label topic = new Label(p.getTopic());
            topic.setFont(Font.font("Arial", FontWeight.BOLD, 15));
            Label stats = new Label("Participants: " + p.getParticipants().size() +
                "   Papers: " + p.getPublishedPapers().size());
            stats.setTextFill(Color.web("#666666"));
            card.getChildren().addAll(topic, stats);
            list.getChildren().add(card);
        }

        content.getChildren().add(list);
        setContent(content);
    }

    private void showNewProject() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle("Create Research Project"));

        VBox form = new VBox(12);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        TextField topicField = field("Project topic");
        Label resultLabel = new Label("");

        Button createBtn = new Button("Create Project");
        createBtn.setStyle(
            "-fx-background-color: #8e44ad; -fx-text-fill: white; -fx-font-size: 14;" +
            "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 10 20;"
        );

        createBtn.setOnAction(e -> {
            String topic = topicField.getText().trim();
            if (topic.isEmpty()) {
                resultLabel.setText("Please enter a topic.");
                resultLabel.setTextFill(Color.ORANGE);
                return;
            }
            ResearchProject p = controller.createResearchProject(topic);
            try {
                controller.joinProject(p, researcher);
                resultLabel.setText("Project created: " + topic);
                resultLabel.setTextFill(Color.web("#27ae60"));
                topicField.clear();
                root.setLeft(buildSidebar());
            } catch (NotAResearcherException ex) {
                resultLabel.setText("Error: " + ex.getMessage());
                resultLabel.setTextFill(Color.RED);
            }
        });

        form.getChildren().addAll(new Label("Topic:"), topicField, createBtn, resultLabel);
        content.getChildren().add(form);
        setContent(content);
    }

    private TextField field(String prompt) {
        TextField f = new TextField();
        f.setPromptText(prompt);
        f.setPrefWidth(300);
        return f;
    }

    private Button sortBtn(String label, String color, Runnable action) {
        Button btn = new Button(label);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white;" +
            "-fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 14;");
        btn.setOnAction(e -> action.run());
        return btn;
    }

    private Label emptyLabel(String text) {
        Label l = new Label(text);
        l.setTextFill(Color.web("#999999"));
        l.setFont(Font.font("Arial", 14));
        return l;
    }
}
