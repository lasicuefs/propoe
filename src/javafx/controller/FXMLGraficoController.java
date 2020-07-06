package javafx.controller;

import com.github.javafx.charts.zooming.ZoomManager;
import javafx.GraficoApp;
import javafx.application.Platform;
import javafx.domain.SentencasHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import model.Metrica;
import model.UnidadeDeVersificacao;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class FXMLGraficoController implements Initializable {

    @FXML
    public ComboBox<String> cbTipoGrafico;
    @FXML
    public ComboBox<Metrica> cbFiltroMetrica;
    @FXML
    public BarChart<String, Number> grafico;
    @FXML
    public Button btVisualizarGrafico;
    @FXML
    public AnchorPane content;

    private XYChart.Series<String, Number> series;
    private CategoryAxis xAxis;
    private StackPane pane;

    private SentencasHelper helper;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.helper = GraficoApp.getHelper();

        //preencher combobox's
        Platform.runLater(() -> cbTipoGrafico.getItems().addAll("Padrões Rítimicos", "Sentenças"));
        Platform.runLater(() -> cbFiltroMetrica.getItems().addAll(helper.obterMetricas()));

        this.xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Quantidade");

        this.series = new XYChart.Series<>();

        this.grafico = new BarChart<>(xAxis, yAxis);

        this.pane = new StackPane();
        this.pane.getChildren().add(this.grafico);
        this.content.getChildren().add(pane);

        AnchorPane.setTopAnchor(pane, 0.0);
        AnchorPane.setBottomAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
    }

    @FXML
    public void actionVisualizarGrafico() {
        String tipo = cbTipoGrafico.getSelectionModel().getSelectedItem();
        Metrica metrica = cbFiltroMetrica.getSelectionModel().getSelectedItem();

        //previne clicar no botão sem escolher um tipo
        if (tipo == null || metrica == null) return;

//        grafico.getData().clear();

        if (tipo.equals("Sentenças")) {
            Map<String, Integer> sentencasSilabas = helper.getSentencasSilabas(metrica);
            sentencasSilabas.forEach((key, value) -> series.getData().add(new XYChart.Data<>(key, value)));
        } else {
            List<UnidadeDeVersificacao> padroesRitmicos = helper.getPadroesRitmicos(metrica);
            padroesRitmicos.forEach(item -> series.getData().add(new XYChart.Data<>(item.getPadrao(), item.getQuantidade())));
        }

        this.xAxis.setLabel(tipo);

        new ZoomManager(pane, grafico, series);
    }
}