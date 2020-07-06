package javafx.domain;

import exportacao.EstruturaVersificacao;
import javafx.utilities.Utils;
import model.AvaliacaoPosicaoTonica;
import model.Estrofe;
import model.Poema;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AvaliacaoPosicaoTonicaHelper {

    private Poema poema;
    private double scorePosicao = 0;
    private double scoreCoincidenciaDeSilabas = 0;
    private boolean compararEntreVersos = false;
    private List<AvaliacaoPosicaoTonica> relatorio;

    public AvaliacaoPosicaoTonicaHelper(Poema poema) {
        this.poema = poema;
        relatorio = new ArrayList<>();
    }

    public void setCompararEntreVersos(Boolean compararEntreVersos) {
        this.compararEntreVersos = compararEntreVersos;
    }

    /**
     * Retorna o score de silabas coincidentes.
     *
     * @return double o score.
     */
    public double getScoreCoincidenciaDeSilabas() {
        return scoreCoincidenciaDeSilabas;
    }

    /**
     * Ontem o resultado da avaliação de posição de tonica.
     *
     * @return relatorio.
     */
    public List<AvaliacaoPosicaoTonica> getResultado() {
        return relatorio;
    }

    /**
     * Obtém o score de posição (Esquema rítimico)
     *
     * @return double o score
     */
    public double getScorePosicao() {
        return scorePosicao;
    }

    /**
     * Retona o score da avaliação fonética.
     *
     * @return float com o valor do score
     */
    public double getScore() {
        return scoreCoincidenciaDeSilabas + scorePosicao;
    }


    /**
     * Método resposável por realizar a avaliação do poema.
     */
    public void avaliar() {
        System.out.println("\n------------------------------------------------------------------");
        System.out.println("Iniciando " + this.getClass().getSimpleName() + ".avaliar()");
        if (compararEntreVersos)
            System.out.println("Usando comparação entre versos.");

        //Número de combinações de ocorrências de verso.
        int numCombinacoes = 0;

        //percorre pelos estrofes do poema para realizar a avaliação
        for (int i = 0; i < this.poema.getEstrofes().size(); i++) {
            System.out.println("\nAvaliando estrofe " + i);
            //obtem o estrofe o poema que está na posicao (i) da lista
            Estrofe estrofe = this.poema.getEstrofes().get(i);
            //obtem a lista de versos do estrofe
            List<EstruturaVersificacao> versos = estrofe.getVersos();

            //percorre a lista de versos do estrofe para realizar a avaliação. Inicia a busca a partir da segunda posicao.
            for (int j = 1; j < versos.size(); j++) {
                /*Caso o usuário tenha gerado o poema por comparação entre versos. o primeiro verso vai ser sempre
                o indice (j - 1), de forma que sempre realize a coparação de 0-1, 1-2...
                se não houver comparação entre versos, o verso1 será sempre o primeiro da lista indice (0).*/
                EstruturaVersificacao verso1 = compararEntreVersos ? versos.get(j - 1) : versos.get(0);
                //o verso dois será sempre a posição atual do vetor
                EstruturaVersificacao verso2 = versos.get(j);

                String[] tonicasV1 = verso1.getPosicaoDasTonicas().split(" ");
                String[] tonicasV2 = verso2.getPosicaoDasTonicas().split(" ");
                String[] silabas1 = verso1.getSentecaEscandida().replace(" ", "").split("/");
                String[] silabas2 = verso2.getSentecaEscandida().replace(" ", "").split("/");

                System.out.println("\nVerso1 {Tonicas =" + Arrays.toString(tonicasV1) + ", Silabas = " + Arrays.toString(silabas1) + "}");
                System.out.println("Verso2 {Tonicas =" + Arrays.toString(tonicasV2) + ", Silabas = " + Arrays.toString(silabas2) + "}");

                double interesecao = Utils.contarInteresecaoDeTonicas(tonicasV1, tonicasV2);
                double uniao = Utils.contarUnicaoDeTonicas(tonicasV1, tonicasV2);
                numCombinacoes++;
                //calcula a divisao da intersecao pela união e soma ao score de posicionamento total
                double jaccard = interesecao / uniao;
                System.out.println("Intersecao = " + interesecao + " União = " + uniao + " (interesecao/uniao) = " + jaccard);
                this.scorePosicao += jaccard;

                //conta as silabas coincidentes
                List<String> silabasCoincidentes = new ArrayList<>();
                /*  Percorre por todas posicoes das tonicas do verso 1 para verificar se existe a mesma posicao de tonica no verso2.
                    Caso exista posicao de tonica igual no verso 2, será comparado se ambas as silabas são iguais,
                    caso sejá será acrescido 1 ponto ao score
                 */
                for (String tonicaV1 : tonicasV1) {
                    int posicaoTonica = Integer.parseInt(tonicaV1) - 1;

                    //se existir tonica igual no vetor1 e vetor2, deverá verificar se as silabas são iguais
                    if (temTonica(tonicaV1, tonicasV2)) {
                        String silaba1 = silabas1[posicaoTonica];
                        String silaba2 = silabas2[posicaoTonica];
                        //se for iguais, adiciona na lista
                        if (silaba1.equals(silaba2)) {
                            silabasCoincidentes.add(silaba1);
                            System.out.println("Silabas (" + silaba1 + ") coincidentes: na posicao1 = " + posicaoTonica);
                        }
                    }
                }

                String s1 = String.format("%s - %s", Arrays.toString(tonicasV1), Arrays.toString(silabas1));
                String s2 = String.format("%s - %s", Arrays.toString(tonicasV2), Arrays.toString(silabas2));
                String s3 = silabasCoincidentes.toString();

                float tamanhoMaiorTonica = tonicasV1.length > tonicasV2.length ? tonicasV1.length : tonicasV2.length;

                //atualiza o score de silabas conicidentes
                this.scoreCoincidenciaDeSilabas += silabasCoincidentes.size() / tamanhoMaiorTonica;
                System.out.println("Numero de Silabas coincidentes = " + silabasCoincidentes.size());
                this.relatorio.add(new AvaliacaoPosicaoTonica(s1, s2, s3));
            }
            //Soma o número de cobinações dos estrofes. Este número de cobinações é a quantidade de comparações dos versos
            //Exemplo: verso1 - verso2 (1 comparação) verso2 - verso3 (2) comparações.
            //Será sempre o número de versos do estrofe - 1.
        }

        System.out.println("\nScore de Posicao = " + scorePosicao);
        System.out.println("Numero de Combinações = " + numCombinacoes);

        //realiza o calculo do score de posição que o somatório dele divido pelo número de combinações.
        this.scorePosicao = (scorePosicao / numCombinacoes);
        this.scoreCoincidenciaDeSilabas /= numCombinacoes;

        System.out.println(String.format("ScorePosicao: (%f), scoreCoincidenciaDeSilabas: (%f), RitimosIguais: (%d).",
                scorePosicao, scoreCoincidenciaDeSilabas, numCombinacoes));
        System.out.println("Finalizando " + this.getClass().getSimpleName() + ".avaliar()");
        System.out.println("------------------------------------------------------------------");
    }

    /**
     * Verifica ve existe posição de tonica igual dentro de um vetor de tonicas.
     *
     * @param posicao A posição que será pesquisada.
     * @param tonicas O array de posição de tonicas.
     * @return boolean. true Se existir uma posição de tonica no vetor, igual a passada por parâmetro.
     * Retorna false quando não encontrar uma posição igual.
     */
    private boolean temTonica(String posicao, String[] tonicas) {
        for (String tonica : tonicas) {
            if (tonica.equals(posicao)) return true;
        }

        return false;
    }
}