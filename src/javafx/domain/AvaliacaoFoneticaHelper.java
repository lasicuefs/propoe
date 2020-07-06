package javafx.domain;

import exportacao.EstruturaVersificacao;
import javafx.utilities.Utils;
import model.AvaliacaoFonetica;
import model.Estrofe;
import model.Poema;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
public final class AvaliacaoFoneticaHelper {

    private Poema poema;
    private double score = 0;
    private List<AvaliacaoFonetica> relatorio;

    public AvaliacaoFoneticaHelper(Poema poema) {
        this.poema = poema;
        this.relatorio = new ArrayList<>();
    }

    /**
     * Ontem o resultado da avaliação fonetica do poema
     */
    public List<AvaliacaoFonetica> getResultado() {
        return relatorio;
    }

    /**
     * Retona o score da avaliação fonética.
     *
     * @return Double com o valor do score
     */
    public double getScore() {
        return score;
    }

    /**
     * Realiza a avaliação fonetica do poema
     */
    public void avalicaoFonetica() {
        System.out.println("\n------------------------------------------------------------------");
        System.out.println("Iniciando " + this.getClass().getSimpleName() + ".avalicaoFonetica()");

        double numTotalDeComparacoes = 0;
        double resultadoTotal = 0;

        List<Estrofe> estrofes = poema.getEstrofes();
        for (Estrofe estrofe : estrofes) {
            System.out.println("Avaliando estrofe " + estrofes.indexOf(estrofe));

            //Agrupa os versos separdos pela ultima silaba. Facilita a avaliação, sendo que só pode ser avaliado os versos
            //com mesmo fim de silabaNesse caso, temos soma todos esses resultados e divide pelo número de coincidências, 3/5 =
            Map<String, List<EstruturaVersificacao>> map = estrofe.getVersos()
                    .stream()
                    .collect(Collectors.groupingBy(verso -> Utils.extrairUltimaSilabaDeUmaSentenca(verso.getSentecaEscandida())));

            //percore o map
            for (String key : map.keySet()) {
                List<EstruturaVersificacao> versos = map.get(key);

                //lista de versos menor que 2 não tem como realizar comparação. Isso evita da estouro de array
                if (versos.size() <= 1)
                    continue;

                for (int j = 1; j < versos.size(); j++) {
                    /*cada interação de comparação entre os versos incrementa a váriavel numComparações, para que
                     *seja possivél saber quantas comparações foram realizadas e realizar o calculo */
                    numTotalDeComparacoes++;
                    //Sempre compara o verso anterior com o proximo (0-1) (1-2) ...
                    EstruturaVersificacao verso1 = versos.get(j - 1);
                    EstruturaVersificacao verso2 = versos.get(j);
                    //obtém a ultima palavra de cada verso
                    String palavra1 = Utils.extrairUltimaPalavraDeSentenca(verso1.getSegmento());
                    String palavra2 = Utils.extrairUltimaPalavraDeSentenca(verso2.getSegmento());
                    //remove caracteres especiais
                    palavra1 = Utils.removeCharEspecial(palavra1);
                    palavra2 = Utils.removeCharEspecial(palavra2);

                    System.out.println("\nComparando: " + palavra1 + " com " + palavra2);
                    /* Se a palavra 2 for menor que a palavra 1, então, troca de posição, para que a palavra 2 seja
                     * a palavra que vai servir de base para o calculo. por padrão a palvra 1 é a base.
                     * */
                    String palvraAux;
                    if (palavra2.length() < palavra1.length()) {
                        System.out.println("Palavra 2 menor que palavra 1. Invertendo posições");
                        //troca a posição das palavras
                        palvraAux = palavra2;
                        palavra2 = palavra1;
                        palavra1 = palvraAux;
                    }

                    List<Character> letrasIguais = new ArrayList<>();
                    //inverte a ordem das letras da palavra, para realizar a contagem de traz para frente.
                    char[] letrasPalavra1 = Utils.reverterOrdermString(palavra1).toCharArray();
                    char[] letrasPalavra2 = Utils.reverterOrdermString(palavra2).toCharArray();
                    //conta o número de letras iguais de mesma posição e caso sejam iguais adiciona na lista.
                    for (int x = 0; x < letrasPalavra1.length; x++) {
                        if (letrasPalavra1[x] == letrasPalavra2[x])
                            letrasIguais.add(letrasPalavra1[x]);
                    }

                    int tamanhoMenorPalavra = obterTamanhoDaMenorPalavra(palavra1, palavra2);

                    double numLetrasIguais = letrasIguais.size();
                    double resultado = numLetrasIguais / tamanhoMenorPalavra;
                    resultadoTotal += resultado;
                    System.out.println("Letras iguais = " + letrasIguais.toString());
                    System.out.println("Numero de letras iguais = " + numLetrasIguais);
                    System.out.println("Numero de letras da menor palavra = " + tamanhoMenorPalavra);
                    System.out.println("Resultado (numLetrasIguais/numLetrasMenorPalavra) = " + resultado);
                    //adiciona as palavras e letras ao relatório
                    relatorio.add(new AvaliacaoFonetica(palavra1, palavra2, letrasIguais.toString()));
                }
            }
        }

        this.score = resultadoTotal / numTotalDeComparacoes;

        System.out.println("\nNumero total de comparações = " + numTotalDeComparacoes);
        System.out.println("Resultado total = " + resultadoTotal);
        System.out.println("Score = " + this.score);
        System.out.println("------------------------------------------------------------------");
    }

    @SuppressWarnings("ManualMinMaxCalculation")
    private int obterTamanhoDaMenorPalavra(String palavra1, String palavra2) {
        int p1 = palavra1.length();
        int p2 = palavra2.length();
        return p1 <= p2 ? p1 : p2;
    }
}