import javafx.application.Application;
import javafx.stage.Stage;
import gui.LoginScreen;


public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        core.DataStore.getInstance();
        new LoginScreen(primaryStage).show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
