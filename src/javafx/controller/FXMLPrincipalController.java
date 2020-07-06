package javafx.controller;

import exportacao.Sentenca;
import exportacao.SentencasInput;
import javafx.AvaliacaoApp;
import javafx.GraficoApp;
import javafx.MainApp;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.domain.Avaliacao;
import javafx.domain.PoemaBuilder;
import javafx.domain.SentencasHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.utilities.*;
import model.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class FXMLPrincipalController implements Initializable {
    public ComboBox<Rima> cbRima;
    public ComboBox<String> cbSilaba1;
    public ComboBox<String> cbSilaba2;
    public ComboBox<String> cbSilaba3;
    public ComboBox<String> cbSilaba4;
    public ComboBox<Metrica> cbMetrica;
    public ComboBox<Integer> cbQtVersos;
    public ComboBox<Integer> cbQtEstrofes;
    public ComboBox<TipoSimilaridade> cbSimiliaridade;
    public ComboBox<Metrica> cbFiltroSentenas;
    public ComboBox<String> cbSegmentoSentenca;
    public ComboBox<String> cbCompararEntreVersos;

    public TableView<HelperTableRitmos.ModeloFrequencia> tvRitimo;
    public TableView<HelperTableSilabas.ModelFrequencia> tvSilabas;

    public TextArea taPoema;
    public TextArea taSentencasVersificadas;

    public Label lbScoreTotal;
    public Label lbScoreFonetica;
    public Label lbScoreTonicaSimilar;
    public Label lbScoreEsquemaRitimico;
    public Label lbScoreAcentuacao;

    public Button btGerarPoema;
    public VBox vbScorePoema;

    public MenuItem menuGraficos;
    public MenuItem menuSalvarPoema;
    public MenuItem menuExibirAvaliacao;
    public CheckBox cbUsaQuatroSilabas;
    public HBox hbSilaba3e4;

    private HelperTableSilabas tableSilabas;
    private HelperTableRitmos tableRitmos;
    private SentencasHelper helper;
    private Avaliacao avaliacao;
    private Poema poema;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            this.cbQtEstrofes.getItems().addAll(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
            this.cbQtVersos.getItems().addAll(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
            this.cbSegmentoSentenca.getItems().addAll("Sentença escandida", "Segmento");
            this.cbSegmentoSentenca.getSelectionModel().selectFirst();
            this.cbCompararEntreVersos.getItems().addAll("Primeiro verso", "Entre versos");
            this.cbCompararEntreVersos.getSelectionModel().selectFirst();
            this.cbSimiliaridade.getItems().addAll(TipoSimilaridade.getAll());
            this.cbSimiliaridade.getSelectionModel().selectFirst();
        });
        this.tableSilabas = new HelperTableSilabas(this.tvSilabas);
        this.tableSilabas.definirTabela();
        this.tableRitmos = new HelperTableRitmos(this.tvRitimo);
        this.tableRitmos.definirTabelaRitmo();

//        this.abrirArquivo(new File("xml/CONFRONTOS.XML"));
    }

    /**
     * Método resposável por abrir um arquivo e popular os combobox e area de texto
     */
    private void abrirArquivo(File file) {
        SentencasInput input = new SentencasInput(null);
        List<Sentenca> sentencas = input.ler(file);

        this.helper = new SentencasHelper(sentencas);

        popularComboBoxRima();
        pouplarComboBoxMetrica();
        exibirSentencasVersificadas();
        popularComboBoxFiltroSentencas();
        popularTabelaPadroesRitimicos(Metrica.TODAS);

        //ao abrir o arquvivo habilita os combobox
        this.cbRima.setDisable(false);
        this.cbMetrica.setDisable(false);
        this.cbSilaba1.setDisable(false);
        this.cbSilaba2.setDisable(false);
        this.cbSilaba3.setDisable(false);
        this.cbSilaba4.setDisable(false);
        this.cbQtVersos.setDisable(false);
        this.cbQtEstrofes.setDisable(false);
        this.btGerarPoema.setDisable(false);
        this.cbSimiliaridade.setDisable(false);
        this.cbFiltroSentenas.setDisable(false);
        this.cbSegmentoSentenca.setDisable(false);
        this.cbCompararEntreVersos.setDisable(false);

        //habilita menus
        this.menuGraficos.setDisable(false);
    }

    /**
     * Preenche o combobox filtro de sentenaças com as metricas disponiveis no helper
     */
    private void popularComboBoxFiltroSentencas() {
        Platform.runLater(() -> {
            this.cbFiltroSentenas.getItems().clear();
            this.cbFiltroSentenas.getItems().addAll(this.helper.obterMetricas());
            this.cbFiltroSentenas.getSelectionModel().selectFirst();
        });
    }

    /**
     * Preenche o combobox rima com as rimas disponiveis para utilização
     */
    private void popularComboBoxRima() {
        Platform.runLater(() -> {
            this.cbRima.getItems().clear();
            this.cbRima.getItems().addAll(this.helper.obterRimas());
            this.cbRima.getSelectionModel().selectFirst();
        });
    }

    /**
     * Preenche o combobox metrica com as metricas disponibilizadas pelo helper
     */
    private void pouplarComboBoxMetrica() {
        Platform.runLater(() -> {
            this.cbMetrica.getItems().clear();
            this.cbMetrica.getItems().addAll(this.helper.obterMetricas());
            this.cbMetrica.getSelectionModel().selectFirst();
        });
    }

    /**
     * Exibe todas as sentenças versificadas, processadas pelo helper, na textArea taSentencasVersificadas
     */
    private void exibirSentencasVersificadas() {
        this.taSentencasVersificadas.setText(this.helper.mostrarSentencas());
    }

    /***
     * Carrega a tabela tableSilabas e tableRitimos com as sentenças silabicas processadas pelo helper
     * @param metrica a metrica ser usada para obter as sentenças
     * */
    private void popularTabelaPadroesRitimicos(Metrica metrica) {
        this.tableSilabas.removerTodos();
        this.tableRitmos.removerTodos();
        List<UnidadeDeVersificacao> padroes = helper.getPadroesRitmicos(metrica);
        Map<String, Integer> sentencas = this.helper.getSentencasSilabas(metrica);

        padroes.forEach((uv) -> this.tableRitmos.adicionarItens(uv.getPadrao(), uv.getQuantidade()));
        sentencas.forEach((silaba, quantidade) -> this.tableSilabas.adicionarItem(silaba, quantidade));
    }

    /***
     * Preenche os itens do combobox silaba1 utilizando o auxilio da classe MostraSentençasHelper
     **/
    @FXML
    public void actionPopularComboBoxSilabas() {
        //enquando todos os combobox estiverem escolhidos não preenche as silabas
        if (this.cbQtEstrofes.getValue() == null) return;
        if (this.cbQtVersos.getValue() == null) return;
        if (this.cbMetrica.getValue() == null) return;

        int numEstrofes = this.cbQtEstrofes.getValue();
        int numVersos = this.cbQtVersos.getValue();
        Metrica metrica = this.cbMetrica.getValue();

        List<String> itens = this.helper.getSilabasDisponiveis(numEstrofes, numVersos, metrica, usaQuatroSilabas());
        adicionarItensComboBoxSilabas(cbSilaba1, itens);
        adicionarItensComboBoxSilabas(cbSilaba2, itens);
        adicionarItensComboBoxSilabas(cbSilaba3, itens);
        adicionarItensComboBoxSilabas(cbSilaba4, itens);
    }

    private void adicionarItensComboBoxSilabas(ComboBox<String> comboBox, List<String> items) {
        Platform.runLater(() -> {
            comboBox.getItems().clear();
            comboBox.getItems().addAll(items);
        });
    }

    /**
     * Ação disparada ao abrir um arquivo para ser processado. Se o arquivo for selecionado com sucesso, chama o
     * metodo abrirArquivo.
     */
    @FXML
    public void actionAbrirArquivo() {
        JFileChooser abrir = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XML", "xml");
        abrir.setDialogTitle("Gerador de Poema by ANA CLEYGE");
        abrir.setFileFilter(filter);
        int result = abrir.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = abrir.getSelectedFile();
            abrirArquivo(file);
        }
    }

    /**
     * Ação disparada ao clicar no item do menu sair, a aplicação é finalizada.
     */
    @FXML
    public void actionSair() {
        MainApp.getStage().close();
    }

    /**
     * Ação disparada ao clicar no item do menu exibir avaliaçao. Abrirá uma nova janela com os dados da avaliação
     */
    @FXML
    public void actionExibirAvalicao() throws IOException {
        AvaliacaoApp.setAvalicacao(avaliacao);
        new AvaliacaoApp().start(new Stage());
    }

    @FXML
    public void actionExibirGraficos() throws IOException {
        GraficoApp.setHelper(helper);
        new GraficoApp().start(new Stage());
    }

    /**
     * Ação disparada ao escolher um item no combobox filtro de sentença
     */
    @FXML
    public void actionComboBoxFiltroSentenca() {
        Metrica metrica = this.cbFiltroSentenas.getSelectionModel().getSelectedItem();
        this.popularTabelaPadroesRitimicos(metrica);
    }

    /**
     * Ação disparada ao escolher um item no combobox silaba1
     */
    @FXML
    public void actionEscolheSilaba1() {
        String itemSelecionado = this.cbSilaba1.getSelectionModel().getSelectedItem();
        String silaba = this.helper.escolherSilaba(this.cbSilaba1.getItems(), itemSelecionado);
        Platform.runLater(() -> this.cbSilaba1.getSelectionModel().select(silaba));
    }

    /**
     * Ação disparada ao escolher um item no combobox silaba2
     */
    @FXML
    public void actionEscolheSilaba2() {
        String itemSelecionado = this.cbSilaba2.getSelectionModel().getSelectedItem();
        String silaba = this.helper.escolherSilaba(this.cbSilaba2.getItems(), itemSelecionado);
        Platform.runLater(() -> this.cbSilaba2.getSelectionModel().select(silaba));
    }

    /**
     * Ação disparada ao escolher um item no combobox silaba3
     */
    @FXML
    public void actionEscolheSilaba3() {
        String itemSelecionado = this.cbSilaba3.getSelectionModel().getSelectedItem();
        String silaba = this.helper.escolherSilaba(this.cbSilaba3.getItems(), itemSelecionado);
        Platform.runLater(() -> this.cbSilaba3.getSelectionModel().select(silaba));
    }

    /**
     * Ação disparada ao escolher um item no combobox silaba4
     */
    @FXML
    public void actionEscolheSilaba4() {
        String itemSelecionado = this.cbSilaba4.getSelectionModel().getSelectedItem();
        String silaba = this.helper.escolherSilaba(this.cbSilaba4.getItems(), itemSelecionado);
        Platform.runLater(() -> this.cbSilaba4.getSelectionModel().select(silaba));
    }

    /**
     * Ação disparada ao clicar no botão gerar o poema.
     * Gera o poema em uma thread separada da tread principal, para que não trave a interface enquanto estiver
     * realizado o processamento do poema.
     */
    @FXML
    public void actionGerarPoema() {
        this.taPoema.clear();
        Integer numEstrofes = this.cbQtEstrofes.getValue();
        Integer numVersos = this.cbQtVersos.getValue();
        String silaba1 = this.cbSilaba1.getValue();
        String silaba2 = this.cbSilaba2.getValue();
        String silaba3 = this.cbSilaba3.getValue();
        String silaba4 = this.cbSilaba4.getValue();
        Metrica metrica = this.cbMetrica.getValue();
        TipoSimilaridade tipoSimilaridade = this.cbSimiliaridade.getValue();
        Rima rima = this.cbRima.getValue();
        Boolean compararEntreVersos = isComparacaoEntreVersos();
        this.btGerarPoema.setDisable(true);

        Task taskGerarEstrofeFinal = new Task() {
            @Override
            protected Poema call() throws PoemaException {
                vbScorePoema.setVisible(false);

                PoemaBuilder builder = new PoemaBuilder(helper.getHp());
                builder.setComparacaoEntreVersos(compararEntreVersos);
                builder.setTipoSimilaridade(tipoSimilaridade);
                builder.setNumEstrofes(numEstrofes);
                builder.setNumDeVersos(numVersos);
                builder.setMetrica(metrica);
                builder.setRima(rima);
                builder.setSilaba1(silaba1);
                builder.setSilaba2(silaba2);
                builder.setSilaba3(silaba3);
                builder.setSilaba4(silaba4);

                return builder.gerarPoema();
            }

            @Override
            protected void succeeded() {
                poema = (Poema) getValue();

                exibirPoema();
                gerarAvaliacao();
                btGerarPoema.setDisable(false);
                menuExibirAvaliacao.setDisable(false);
                menuSalvarPoema.setDisable(false);
            }

            @Override
            protected void failed() {
                btGerarPoema.setDisable(false);
                menuExibirAvaliacao.setDisable(false);
                menuSalvarPoema.setDisable(false);

                getException().printStackTrace();

                Alert alert = new Alert(Alert.AlertType.ERROR, getException().getMessage(), ButtonType.OK);
                alert.setHeaderText("Não foi possível gerar o poema");
                alert.show();
            }
        };

        Thread thread = new Thread(taskGerarEstrofeFinal);
        thread.setDaemon(true);
        thread.start();
    }

    private void gerarAvaliacao() {
        Task taskAvaliacao = new Task() {
            @Override
            protected Avaliacao call() {
                Boolean compracaoEntreVersos = cbCompararEntreVersos.getSelectionModel().getSelectedItem().equals("Entre versos");
                Avaliacao avaliacao = new Avaliacao(poema, compracaoEntreVersos);
                avaliacao.avaliarPoema();
                return avaliacao;
            }

            @Override
            protected void succeeded() {
                //habilitar visualizacao dos labels de score
                avaliacao = (Avaliacao) getValue();
                vbScorePoema.setVisible(true);
                Platform.runLater(() -> {

                    String scoreTotal = String.format("%.2f", avaliacao.getScoreTotal());
                    String scoreFonetica = String.format("%.2f", avaliacao.getScoreFonetica());
                    String scoreTonicaSimilar = String.format("%.2f", avaliacao.getScoreTonicaSimilares());
                    String scoreEsquemaRitimico = String.format("%.2f", avaliacao.getScoreEsquemaRitimico());
                    String scoreAcentuacao = String.format("%.2f", avaliacao.getScoreAcentuacao());

                    String stb = "\n\nResultado da avaliação" +
                            "Fonética = " + scoreFonetica + "\n" +
                            "Tônicas Similares = " + scoreTonicaSimilar + "\n" +
                            "Esquema Rítimico = " + scoreEsquemaRitimico + "\n" +
                            "Acentuação = " + scoreAcentuacao + "\n" +
                            "Score Total = " + scoreTotal + " \n";

                    LogPrintable.clean();
                    LogPrintable.stringBefore(stb);

                    lbScoreTotal.setText(scoreTotal);
                    lbScoreFonetica.setText(scoreFonetica);
                    lbScoreTonicaSimilar.setText(scoreTonicaSimilar);
                    lbScoreEsquemaRitimico.setText(scoreEsquemaRitimico);
                    lbScoreAcentuacao.setText(scoreAcentuacao);
                });
            }

            @Override
            protected void failed() {
                getException().printStackTrace();
            }
        };
        Thread thread = new Thread(taskAvaliacao);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    public void actionSalvarPoema() {
        JFileChooser arquivoSalvar = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivo de Texto", "txt");
        arquivoSalvar.setDialogTitle("Salvar Poema");
        arquivoSalvar.setFileFilter(filter);
        int resultado = arquivoSalvar.showSaveDialog(arquivoSalvar);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            try {
                File file = arquivoSalvar.getSelectedFile();
                String lineSeparator = System.getProperty("line.separator");
                boolean existeArquivo = file.exists();

                StringBuilder bufferSaida = new StringBuilder();

                if (!existeArquivo) {
                    //se for um novo arquivo, adiciona a extensão do arquivo
                    file = new File(file.getPath().concat(".txt"));
                } else {
                    //se não for um novo arquivo adiciona um separador de poemas
                    bufferSaida.append("----------------------------").append(lineSeparator).append(lineSeparator);
                }
                //altera a quebra de linha pelo caracter new line utilizado no sistema operacional
                bufferSaida.append(taPoema.getText().replaceAll("\n", lineSeparator));
                BufferedWriter bf = new BufferedWriter(new FileWriter(file, existeArquivo));
                bf.append(bufferSaida.toString());
                LogPrintable.stringBefore("Processamento da avaliação");
                bf.append(LogPrintable.getLogString());
                bf.close();
            } catch (IOException io) {
                System.out.println("Erro ao salvar arquivo");
            }
        }

    }

    /***
     * Exibe o poema na área de texto
     * Se exibirSegmento for true, a exibição do poema será pelo segmento, caso contrário, será pela sentença escandida
     * */
    @FXML
    private void exibirPoema() {
        if (this.poema != null) {
            boolean exibirSegmento = this.cbSegmentoSentenca.getSelectionModel().getSelectedItem().contains("Segmento");
            StringBuilder stringBuilder = new StringBuilder();
            this.poema.getEstrofes().forEach(estrofe -> {
                estrofe.getVersos().forEach(s -> {
                    if (exibirSegmento) {
                        stringBuilder.append(Utils.removeCharEspecial(s.getSegmento()));
                    } else {
                        stringBuilder.append(s.getSentecaEscandida())
                                .append(" -> ").append(s.getMetrica())
                                .append(" -> ").append(s.getPosicaoDasTonicas());
                    }
                    stringBuilder.append("\n");
                });
                stringBuilder.append("\n");
            });

            Platform.runLater(() -> taPoema.setText(stringBuilder.toString()));
        }
    }

    /**
     * Verifica se a comparacao escolhida pelo usuário é entre versos ou não.
     *
     * @return true se é para usar comparação entre versos, false caso não sejá para utilizar.
     */
    private boolean isComparacaoEntreVersos() {
        return cbCompararEntreVersos.getSelectionModel().getSelectedItem().equals("Entre versos");
    }

    /**
     * Verifica se o usuario escolheu 4 silabas ou somente 2.
     * Para usar 4 silabas o usuário deve preencher os 4 combobox de silabas, caso contrário o sistema só utiliza 2
     *
     * @return true se o usuario escolheu 4, false se ele escolheu 2
     */
    private boolean usaQuatroSilabas() {
        return this.cbUsaQuatroSilabas.isSelected();
    }

    public void mudarParaQuatroSilabas() {
        this.hbSilaba3e4.setDisable(!usaQuatroSilabas());
        this.actionPopularComboBoxSilabas();
    }
}