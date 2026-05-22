import javafx.application.Application;
import javafx.stage.Stage;
import gui.LoginScreen;

/**
 * JavaFX Entry Point for KBTU University System
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Initialize DataStore with seed data
        core.DataStore.getInstance();

        // Launch login screen
        new LoginScreen(primaryStage).show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
