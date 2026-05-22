package gui;

import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.scene.paint.Color;
import models.User;


public abstract class BaseDashboard {
    protected Stage stage;
    protected User user;
    protected BorderPane root;
    protected VBox contentArea;

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

        Label titleLabel = new Label(" " + title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titleLabel.setTextFill(Color.WHITE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label userLabel = new Label(" " + user.getFirstName() + " " + user.getLastName());
        userLabel.setTextFill(Color.web("#aaaaaa"));
        userLabel.setFont(Font.font("Arial", 13));

        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle(
            "-fx-background-color: #e74c3c;" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 6;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 8 16;"
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
        btn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #cccccc;" +
            "-fx-font-size: 14;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 0 20;" +
            "-fx-background-radius: 8;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle(
            "-fx-background-color: #2d2d5e;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 0 20;" +
            "-fx-background-radius: 8;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #cccccc;" +
            "-fx-font-size: 14;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 0 20;" +
            "-fx-background-radius: 8;"
        ));
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
            "-fx-background-color: " + color + ";" +
            "-fx-background-radius: 12;" +
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

    
    public abstract void show();
}
