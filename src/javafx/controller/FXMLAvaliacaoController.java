package javafx.controller;

import javafx.AvaliacaoApp;
import javafx.domain.Avaliacao;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class FXMLAvaliacaoController implements Initializable {

    @FXML
    public TextArea taFonetica;
    @FXML
    public TextArea taAcentuacao;
    @FXML
    private TextArea taTonica;
    @FXML
    private Label lbScoreTonica;
    @FXML
    private Label lbScoreAcentuacao;
    @FXML
    private Label lbScoreFonetica;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Avaliacao avaliacao = AvaliacaoApp.getAvaliacao();

        //Preenche a tabela de fonetica
        StringBuilder builderFonetica = new StringBuilder();
        avaliacao.getAvaliacaoFonetica()
                .getResultado()
                .forEach(fonetica -> builderFonetica
                        .append(fonetica.getPalavra1()).append(" >> ")
                        .append(fonetica.getPalavra2()).append(" >> ")
                        .append(fonetica.getLetrasIguais()).append("\n"));
        taFonetica.setText(builderFonetica.toString());
        lbScoreFonetica.setText(String.format("Score: %.2f", avaliacao.getAvaliacaoFonetica().getScore()));

        //Preenche a tabela de acentuação
        StringBuilder builderAcentuacao = new StringBuilder();
        avaliacao.getAvaliacaoAcentuacao()
                .getResultado()
                .forEach(acentuacao -> builderAcentuacao
                        .append(acentuacao.getPalavra()).append(" >> ")
                        .append(acentuacao.getAcentuacao()).append("\n"));
        taAcentuacao.setText(builderAcentuacao.toString());
        lbScoreAcentuacao.setText(String.format("Score: %.2f", avaliacao.getAvaliacaoAcentuacao().getScore()));

        //Preenche a tabela de posição de Tônica
        StringBuilder buider = new StringBuilder();
        avaliacao.getAvaliacaoPosicaoTonica()
                .getResultado()
                .forEach(tonica -> buider
                        .append(tonica.getPosicaoRitimo1()).append("\n")
                        .append(tonica.getPosicaoRitimo2()).append("\n")
                        .append(tonica.getCoincidentes()).append("\n\n"));
        taTonica.setText(buider.toString());
        lbScoreTonica.setText(String.format("Score: %.2f", avaliacao.getAvaliacaoPosicaoTonica().getScore()));
    }
}