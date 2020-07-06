package javafx;

import javafx.application.Application;
import javafx.domain.SentencasHelper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GraficoApp extends Application {

    private static Stage mStage;
    private static SentencasHelper helper;

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("view/FXMLGrafico.fxml"));

        mStage = stage;
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Gráficos");
        stage.show();
        stage.centerOnScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getStage() {
        return mStage;
    }

    public static SentencasHelper getHelper() {
        return helper;
    }

    public static void setHelper(SentencasHelper helper) {
        GraficoApp.helper = helper;
    }
}