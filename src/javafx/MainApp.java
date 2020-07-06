package javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    private static Stage mStage;

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("view/FXMLPrincipal.fxml"));

        mStage = stage;
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("PROPOE (Prose to Poem Automatic Generator");
        stage.show();
        stage.centerOnScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getStage() {
        return mStage;
    }
}
