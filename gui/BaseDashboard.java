package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import models.TechSupportRequest;
import models.User;


public abstract class BaseDashboard {

    private static final String MENU_BTN_NORMAL =
        "-fx-background-color: transparent; -fx-text-fill: #cccccc; -fx-font-size: 14;" +
        "-fx-cursor: hand; -fx-padding: 0 20; -fx-background-radius: 8;";
    private static final String MENU_BTN_HOVER =
        "-fx-background-color: #2d2d5e; -fx-text-fill: white; -fx-font-size: 14;" +
        "-fx-cursor: hand; -fx-padding: 0 20; -fx-background-radius: 8;";

    protected Stage stage;
    protected User user;
    protected BorderPane root;

    public BaseDashboard(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
        this.root = new BorderPane();
    }

    protected HBox createNavBar(String title) {
        HBox navbar = new HBox();
        navbar.setAlignment(Pos.CENTER_LEFT);
        navbar.setPadding(new Insets(15, 25, 15, 25));
        navbar.setSpacing(15);
        navbar.setStyle("-fx-background-color: #1a1a2e;");

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titleLabel.setTextFill(Color.WHITE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label userLabel = new Label(user.getFirstName() + " " + user.getLastName());
        userLabel.setTextFill(Color.web("#aaaaaa"));
        userLabel.setFont(Font.font("Arial", 13));

        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle(
            "-fx-background-color: #e74c3c; -fx-text-fill: white;" +
            "-fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 8 16;"
        );
        logoutBtn.setOnAction(e -> {
            core.DataStore.getInstance().save();
            new LoginScreen(stage).show();
        });

        navbar.getChildren().addAll(titleLabel, spacer, userLabel, logoutBtn);
        return navbar;
    }

    protected Button createMenuButton(String text, String icon) {
        Button btn = new Button(icon + "  " + text);
        btn.setPrefWidth(220);
        btn.setPrefHeight(45);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle(MENU_BTN_NORMAL);
        btn.setOnMouseEntered(e -> btn.setStyle(MENU_BTN_HOVER));
        btn.setOnMouseExited(e -> btn.setStyle(MENU_BTN_NORMAL));
        return btn;
    }

    protected Label createSectionTitle(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        label.setTextFill(Color.web("#1a1a2e"));
        return label;
    }

    protected VBox createCard(String title, String value, String color) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(20));
        card.setMinWidth(180);
        card.setStyle(
            "-fx-background-color: " + color + "; -fx-background-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);"
        );

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        valueLabel.setTextFill(Color.WHITE);

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", 13));
        titleLabel.setTextFill(Color.web("#ffffff99"));

        card.getChildren().addAll(valueLabel, titleLabel);
        return card;
    }

    protected void setContent(Node content) {
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f5f6fa; -fx-background-color: #f5f6fa;");
        root.setCenter(scrollPane);
    }

    protected void addResearcherRequestButton(VBox sidebar) {
        core.DataStore ds = core.DataStore.getInstance();
        if (ds.isResearcher(user)) return;

        if (ds.hasResearcherRequest(user)) {
            Button pending = createMenuButton("Request Pending...", "");
            pending.setDisable(true);
            pending.setOpacity(0.5);
            sidebar.getChildren().add(pending);
        } else {
            Button reqBtn = createMenuButton("Become Researcher", "");
            reqBtn.setStyle(reqBtn.getStyle().replace("#cccccc", "#27ae60"));
            reqBtn.setOnAction(e -> {
                ds.addResearcherRequest(user);
                reqBtn.setText("  Request Pending...");
                reqBtn.setDisable(true);
                reqBtn.setOpacity(0.5);
            });
            sidebar.getChildren().add(reqBtn);
        }
    }

    protected void showTechSupportForm() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.getChildren().add(createSectionTitle("Submit Tech Support Request"));

        VBox form = new VBox(12);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        TextArea issueField = new TextArea();
        issueField.setPromptText("Describe your issue...");
        issueField.setPrefRowCount(5);
        issueField.setWrapText(true);

        Label resultLabel = new Label("");

        Button sendBtn = new Button("Submit Request");
        sendBtn.setStyle(
            "-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14;" +
            "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 10 20;"
        );

        sendBtn.setOnAction(e -> {
            String issue = issueField.getText().trim();
            if (issue.isEmpty()) {
                resultLabel.setText("Please describe your issue.");
                resultLabel.setTextFill(Color.ORANGE);
                return;
            }
            core.DataStore.getInstance().addTechRequest(new TechSupportRequest(user, issue));
            core.DataStore.getInstance().log(user, "Submitted tech support request");
            resultLabel.setText("Request submitted successfully!");
            resultLabel.setTextFill(Color.web("#27ae60"));
            issueField.clear();
        });

        form.getChildren().addAll(new Label("Issue description:"), issueField, sendBtn, resultLabel);
        content.getChildren().add(form);
        setContent(content);
    }

    public abstract void show();
}
