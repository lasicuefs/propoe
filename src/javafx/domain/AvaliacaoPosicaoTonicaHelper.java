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
  
    public double getScoreCoincidenciaDeSilabas() {
        return scoreCoincidenciaDeSilabas;
    }
    
    public List<AvaliacaoPosicaoTonica> getResultado() {
        return relatorio;
    }

    public double getScorePosicao() {
        return scorePosicao;
    }

    public double getScore() {
        return scoreCoincidenciaDeSilabas + scorePosicao;
    }

    public void avaliar() {
        System.out.println("\n------------------------------------------------------------------");
        System.out.println("Iniciando " + this.getClass().getSimpleName() + ".avaliar()");
        if (compararEntreVersos)
            System.out.println("Usando comparação entre versos.");

        int numCombinacoes = 0;

        for (int i = 0; i < this.poema.getEstrofes().size(); i++) {
            System.out.println("\nAvaliando estrofe " + i);
           
            Estrofe estrofe = this.poema.getEstrofes().get(i);
           
            List<EstruturaVersificacao> versos = estrofe.getVersos();
           
            for (int j = 1; j < versos.size(); j++) {
                
                EstruturaVersificacao verso1 = compararEntreVersos ? versos.get(j - 1) : versos.get(0);               
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
               
                double jaccard = interesecao / uniao;
                System.out.println("Intersecao = " + interesecao + " União = " + uniao + " (interesecao/uniao) = " + jaccard);
                this.scorePosicao += jaccard;

                List<String> silabasCoincidentes = new ArrayList<>();
               
                for (String tonicaV1 : tonicasV1) {
                    int posicaoTonica = Integer.parseInt(tonicaV1) - 1;
                  
                    if (temTonica(tonicaV1, tonicasV2)) {
                        String silaba1 = silabas1[posicaoTonica];
                        String silaba2 = silabas2[posicaoTonica];
                       
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

                this.scoreCoincidenciaDeSilabas += silabasCoincidentes.size() / tamanhoMaiorTonica;
                System.out.println("Numero de Silabas coincidentes = " + silabasCoincidentes.size());
                this.relatorio.add(new AvaliacaoPosicaoTonica(s1, s2, s3));
            }          
        }

        System.out.println("\nScore de Posicao = " + scorePosicao);
        System.out.println("Numero de Combinações = " + numCombinacoes);

        this.scorePosicao = (scorePosicao / numCombinacoes);
        this.scoreCoincidenciaDeSilabas /= numCombinacoes;

        System.out.println(String.format("ScorePosicao: (%f), scoreCoincidenciaDeSilabas: (%f), RitimosIguais: (%d).",
                scorePosicao, scoreCoincidenciaDeSilabas, numCombinacoes));
        System.out.println("Finalizando " + this.getClass().getSimpleName() + ".avaliar()");
        System.out.println("------------------------------------------------------------------");
    }

    private boolean temTonica(String posicao, String[] tonicas) {
        for (String tonica : tonicas) {
            if (tonica.equals(posicao)) return true;
        }

        return false;
    }
}
