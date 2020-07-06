package javafx.domain;

import exportacao.EstruturaVersificacao;
import exportacao.Sentenca;
import javafx.utilities.Utils;
import javafx.utilities.VersoException;
import model.Estrofe;
import model.Metrica;
import model.Rima;
import model.TipoSimilaridade;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ana Cleyge
 */
public final class EstrofeBuilder {

    //Metrica do estrofe
    private Metrica metrica;
    //Numero de versos que o estrofe terá
    private Integer numDeVersos;
    //Se é para comparar entre versos ou somente o primeiro
    private Boolean compararEntreVersos;
    //Listas de sentenças da silaba 1 e 2
    private List<Sentenca> sentencasSilaba1;
    private List<Sentenca> sentencasSilaba2;
    //tipo de calculo de similaridade dos versos
    private TipoSimilaridade tipoSimilaridade;

    EstrofeBuilder() {
        this.compararEntreVersos = false;
    }

    /**
     * Define o número de versos que o estrofe deve gerar.
     *
     * @param numDeVersos int com a quantidade de versos.
     */
    public void setNumDeVersos(Integer numDeVersos) {
        this.numDeVersos = numDeVersos;
    }

    /**
     * Define o tipo de metrica utilizada que o verso deverá conter.
     *
     * @param metrica {@link Metrica}
     */
    public void setMetrica(Metrica metrica) {
        this.metrica = metrica;
    }

    /**
     * Define as sentenças que serão utilizadas para gerar os versos da estrofe.
     *
     * @param sentencasSilaba1 Lista com as senteças da silaba1.
     * @param sentencasSilaba2 Lista com as senteças da silaba2.
     */
    public void setSentencas(List<Sentenca> sentencasSilaba1, List<Sentenca> sentencasSilaba2) {
        this.sentencasSilaba1 = sentencasSilaba1;
        this.sentencasSilaba2 = sentencasSilaba2;
    }

    /**
     * Define o tipo de similaridade utilizada para gerar um novo verso.
     *
     * @param tipoSimilaridade {@link TipoSimilaridade}
     */
    public void setTipoSimilaridade(TipoSimilaridade tipoSimilaridade) {
        this.tipoSimilaridade = tipoSimilaridade;
    }

    /**
     * Define se ao realizar o calculo de similaridade, se deverá utilizar como verso de comparação o primeiro verso gerado (false)
     * ou utilizar sempre o ultimo verso gerado (true)
     */
    public void setCompararRimas(Boolean compararRimas) {
        this.compararEntreVersos = compararRimas;
    }

    /**
     * Método resposável por gerar um novo estrofe.
     *
     * @param rima {@link Rima} utilizada para gerar a mistura de silabas do verso.
     */
    public Estrofe build(Rima rima) throws VersoException {
        System.out.println("\nStart " + this.getClass().getSimpleName() + ".build()");
        System.out.println("Rima: " + rima + ", Metrica: " + metrica + ", numDeVersos: " + numDeVersos + ", compararEntreVersos: " + compararEntreVersos + ", similaridade: " + tipoSimilaridade + ", sizeSentencas1: " + sentencasSilaba1.size() + ", sizeSentencas2: " + sentencasSilaba2.size());

        Estrofe novoEstrofe = new Estrofe(); //cria um novo objeto Estrofe
        novoEstrofe.setRima(rima);

        VersoBuilder versoBuilder = new VersoBuilder(); //Cria um construtor de verso
        versoBuilder.setMetrica(this.metrica); //define o tipo de metrica que os versos devem conter.

        System.out.println("###################### Nível de Similaridade ####################################");
        List<EstruturaVersificacao> versos = new ArrayList<>(); //lista dos versos gerados
        for (int i = 0; i < numDeVersos; i++) {
            EstruturaVersificacao novoVerso;
            //lista que contem as sentenças onde serão obtidas o verso. Chama o metodo obter sentença por rima, pois ele
            //que é o resposável por fazer a mistura de silabas de cada verso.
            List<Sentenca> sentencas = obterSetencasPorRima(rima, i);
            //define a sentença no contrutor de verso.
            versoBuilder.setSentencas(sentencas);

            //se for o primeiro verso da estrofe, gera um novo verso aleatóriamente
            if (i == 0) {
                novoVerso = versoBuilder.gerarVersoAleatorio();
            } else {
                //se não for o primeiro verso deve ser informado um verso para servir de base de calculo de similaridade
                //Caso comparação entre versos sejá false, o verso de comparação será sempre o primeiro gerado,
                //caso contrário será sempre o último gerado.
                EstruturaVersificacao versoComparacao = compararEntreVersos ? versos.get(versos.size() - 1) : versos.get(0);
                versoBuilder.setVersoComparacao(versoComparacao);

                //verifica o tipo de calculo de similaridade e solicita ao contrutor que gere um verso de acodo com esse tipo.
                if (this.tipoSimilaridade == TipoSimilaridade.ESQUEMA_RITMICO)
                    novoVerso = versoBuilder.gerarVersoPorEsquemaRitimico();
                else
                    novoVerso = versoBuilder.gerarVersoPorPosicaoTonica();
            }

            System.out.println("Sentença: " + novoVerso.getSentecaEscandida() + "\t-   " + novoVerso.getPosicaoDasTonicas());
            System.out.println("Nota: " + novoVerso.getIndiceSimilaridade());
            //marca o novo verso como utilizado e adiciona-o à lista de versos.
            novoVerso.setIsUsed(true);
            versos.add(novoVerso);
        }

        //adiciona a lista de versos no estrofe e retorna o novo estrofe criado.
        novoEstrofe.setVersos(versos);
        System.out.println("\nFinish " + this.getClass().getSimpleName() + ".build()");
        return novoEstrofe;
    }

    /***
     * Obtem uma lista de sentenças a ser utilizada nos versos de acordo com uma rima.
     * Utiliza um pequeno cálculo para obter o padrão de senteças.
     *
     * @param rima {@link Rima} escolhida pelo usuário
     * @param index {@link Integer} indice do verso que está sendo criado
     *
     * @return {@link List<Sentenca>}
     * */
    private List<Sentenca> obterSetencasPorRima(Rima rima, Integer index) {
        switch (rima) {
            case CRUZADA:
                return index % 2 == 0 ? sentencasSilaba1 : sentencasSilaba2;
            case EMPARELHADA:
                int metade = (numDeVersos + 1) / 2;
                return index < metade ? sentencasSilaba1 : sentencasSilaba2;
            case INTERPOLADA:
                return index % 3 == 0 ? sentencasSilaba1 : sentencasSilaba2;
            case MISTA:
                int random = Utils.random().nextInt(2);
                return random == 0 ? sentencasSilaba1 : sentencasSilaba2;
            default:
                throw new RuntimeException("É necessário informar um tipo de rima");
        }
    }
}