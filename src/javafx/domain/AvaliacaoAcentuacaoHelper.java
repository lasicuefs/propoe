package javafx.domain;

import exportacao.EstruturaVersificacao;
import javafx.utilities.Utils;
import model.Acentuacao;
import model.AvaliacaoAcentuacao;
import model.Estrofe;
import model.Poema;

import java.util.*;
import java.util.stream.Stream;

public class AvaliacaoAcentuacaoHelper {

    private final Poema poema;
    private double score = 0;
    private final List<AvaliacaoAcentuacao> relatorio;

    public AvaliacaoAcentuacaoHelper(Poema poema) {
        this.poema = poema;
        this.relatorio = new ArrayList<>();
    }
    
    public List<AvaliacaoAcentuacao> getResultado() {
        return this.relatorio;
    }
  
    public double getScore() {
        return score;
    }

    public void avaliarAcentuacaoDeVersos() {
        System.out.println("\n------------------------------------------------------------------");
        System.out.println("Start " + this.getClass().getSimpleName() + ".avaliarAcentuacaoDeVersos()");

        double totalPontos = 0;
        double totoalCombinacoes = 0;

        System.out.println("totalPontos = " + totalPontos);
        System.out.println("totoalCombinacoes = " + totoalCombinacoes);

        List<Estrofe> estrofes = poema.getEstrofes();
        for (Estrofe estrofe : estrofes) {
            System.out.println("Avaliando estrofe " + estrofes.indexOf(estrofe));
            List<EstruturaVersificacao> versos = estrofe.getVersos();
            double pontosDoEstrofe = 0;
            int comparacoes = 0;

            Map<String, Map<Acentuacao, Integer>> acentuacoesPorSilaba = agruparAcentuacaoPorSilaba(versos);

            for (String silaba : acentuacoesPorSilaba.keySet()) {
                Map<Acentuacao, Integer> mapAcentuacao = acentuacoesPorSilaba.get(silaba);
                //obtem o somatório de acentuação similar
                double pontosDaSilaba = calcularPontosDeAcentuacaoSimilar(mapAcentuacao.entrySet().stream());
                comparacoes += calcularComparacoes(mapAcentuacao.entrySet().stream());
                pontosDoEstrofe += pontosDaSilaba;
            }
            System.out.println("Pontos = " + pontosDoEstrofe + ", Combinações = " + comparacoes);
            totoalCombinacoes += comparacoes;
            totalPontos += pontosDoEstrofe;
        }
        System.out.println("Total de combinacoes do poema = " + totoalCombinacoes);
        System.out.println("Total de pontos do poema = " + totalPontos);
        
        this.score = totalPontos / totoalCombinacoes;
        System.out.println("Score total = " + this.score);
        System.out.println("Finish" + this.getClass().getSimpleName() + ".avaliarAcentuacaoDeVersos()");
        System.out.println("------------------------------------------------------------------");
    }

    private Map<String, Map<Acentuacao, Integer>> agruparAcentuacaoPorSilaba(List<EstruturaVersificacao> versos) {
        Map<String, Map<Acentuacao, Integer>> acentuacoesPorSilaba = new HashMap<>();

        for (int i = 0; i < versos.size(); i++) {
            EstruturaVersificacao verso = versos.get(i);
            String ultimaPalavra = Utils.extrairUltimaPalavraDeSentenca(verso.getSentecaEscandida());
            String ultimaSilaba = Utils.extrairUltimaSilabaDeUmaSentenca(ultimaPalavra);
            Acentuacao acentuacao = Utils.obterAcentuacaoDaPalavra(ultimaPalavra);

            System.out.println("Verso = " + i + ", Ultima palavra = " + ultimaPalavra + ", Ultima Silaba = " + ultimaSilaba + ", Acentuacao = " + acentuacao);
            
            relatorio.add(new AvaliacaoAcentuacao(ultimaPalavra, acentuacao));

            if (acentuacoesPorSilaba.containsKey(ultimaSilaba)) {
                acentuacoesPorSilaba.get(ultimaSilaba).merge(acentuacao, 1, Integer::sum);
            } else {
                Map<Acentuacao, Integer> mAcentuacao = new HashMap<>();
                mAcentuacao.put(acentuacao, 1);
                acentuacoesPorSilaba.put(ultimaSilaba, mAcentuacao);
            }
        }
        return acentuacoesPorSilaba;
    }

    private double calcularPontosDeAcentuacaoSimilar(Stream<Map.Entry<Acentuacao, Integer>> stream) {
        return stream.filter(Objects::nonNull)
                .filter(item -> item.getValue() > 1)
                .mapToDouble(item -> item.getValue() - 1)
                .sum();
    }

    private int calcularComparacoes(Stream<Map.Entry<Acentuacao, Integer>> stream) {
        return stream.filter(Objects::nonNull)
                .mapToInt(Map.Entry::getValue)
                .sum() - 1;
    }
}
