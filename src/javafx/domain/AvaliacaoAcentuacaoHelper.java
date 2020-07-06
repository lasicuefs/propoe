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

    /**
     * Ontem o resultado da avaliação fonetica do poema
     */
    public List<AvaliacaoAcentuacao> getResultado() {
        return this.relatorio;
    }

    /**
     * Retona o score da avaliação fonética.
     *
     * @return float com o valor do score
     */
    public double getScore() {
        return score;
    }

    /**
     * Metódo responsável de realizar a avaliação do poema.
     */
    public void avaliarAcentuacaoDeVersos() {
        System.out.println("\n------------------------------------------------------------------");
        System.out.println("Start " + this.getClass().getSimpleName() + ".avaliarAcentuacaoDeVersos()");

        double totalPontos = 0;
        double totoalCombinacoes = 0;

        System.out.println("totalPontos = " + totalPontos);
        System.out.println("totoalCombinacoes = " + totoalCombinacoes);

        //Percorre todos os estrofes
        List<Estrofe> estrofes = poema.getEstrofes();
        for (Estrofe estrofe : estrofes) {
            System.out.println("Avaliando estrofe " + estrofes.indexOf(estrofe));
            List<EstruturaVersificacao> versos = estrofe.getVersos();
            double pontosDoEstrofe = 0;
            int comparacoes = 0;

            //Obtém a lista de acentuação agrupada por silabas
            Map<String, Map<Acentuacao, Integer>> acentuacoesPorSilaba = agruparAcentuacaoPorSilaba(versos);

            //Inicia a contagem de acentuacao
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
        //realiza o calculo pontos/combinacoes
        this.score = totalPontos / totoalCombinacoes;
        System.out.println("Score total = " + this.score);
        System.out.println("Finish" + this.getClass().getSimpleName() + ".avaliarAcentuacaoDeVersos()");
        System.out.println("------------------------------------------------------------------");
    }

    /**
     * Recebe uma lista com os versos, agrupa por silabas e conta as acentuações por silabas.
     */
    private Map<String, Map<Acentuacao, Integer>> agruparAcentuacaoPorSilaba(List<EstruturaVersificacao> versos) {
        Map<String, Map<Acentuacao, Integer>> acentuacoesPorSilaba = new HashMap<>();

        for (int i = 0; i < versos.size(); i++) {
            EstruturaVersificacao verso = versos.get(i);
            String ultimaPalavra = Utils.extrairUltimaPalavraDeSentenca(verso.getSentecaEscandida());
            String ultimaSilaba = Utils.extrairUltimaSilabaDeUmaSentenca(ultimaPalavra);
            Acentuacao acentuacao = Utils.obterAcentuacaoDaPalavra(ultimaPalavra);

            System.out.println("Verso = " + i + ", Ultima palavra = " + ultimaPalavra + ", Ultima Silaba = " + ultimaSilaba + ", Acentuacao = " + acentuacao);

            //adiciona a acentuacao da palavra na lista do relatorio
            relatorio.add(new AvaliacaoAcentuacao(ultimaPalavra, acentuacao));

            /*Se no mapSilabas já existir uma (chave) igual a do ultima silaba do verso atual, incrimenta a quantidade no mapSilabas de acentuacao.
            Caso não exista, cria um novo mapSilabas de acentuacao e adiciona à nova chave (ultima silaba do verso)
             */
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

    /**
     * Calcula o valor da baseado nas ocorrências. Realiza o somatório de pontos para as
     * ocorrencias de acentuações iguais.
     * {2 ocorrencias = 1 ponto}
     * {3 ocorrencias = 2 ponto}
     * Por ultimo obtem o somatório de pontos
     * <p>
     * Filtra somente as acentuacoes que tem valores maiores que 1, ou seja, acentuações que aparecem no minimo
     * 2 vezes para uma mesma silaba. Depois obtem a pontuação da acentuações filtradas (valor - 1).
     */
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