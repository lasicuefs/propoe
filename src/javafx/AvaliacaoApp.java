package javafx;

import javafx.application.Application;
import javafx.domain.Avaliacao;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AvaliacaoApp extends Application {

    private static Stage mStage;
    private static Avaliacao mAvaliacao;

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("view/FXMLAvaliacao.fxml"));

        mStage = stage;
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Avaliação do Poema");
        stage.show();
        stage.centerOnScreen();
        //stage.maximizedProperty();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void setAvalicacao(Avaliacao avaliacao) {
        mAvaliacao = avaliacao;
    }

    public static Avaliacao getAvaliacao() {
        return mAvaliacao;
    }

    public static Stage getStage() {
        return mStage;
    }
}