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
   
    public List<AvaliacaoFonetica> getResultado() {
        return relatorio;
    }

    public double getScore() {
        return score;
    }

    public void avalicaoFonetica() {
        System.out.println("\n------------------------------------------------------------------");
        System.out.println("Iniciando " + this.getClass().getSimpleName() + ".avalicaoFonetica()");

        double numTotalDeComparacoes = 0;
        double resultadoTotal = 0;

        List<Estrofe> estrofes = poema.getEstrofes();
        for (Estrofe estrofe : estrofes) {
            System.out.println("Avaliando estrofe " + estrofes.indexOf(estrofe));

            Map<String, List<EstruturaVersificacao>> map = estrofe.getVersos()
                    .stream()
                    .collect(Collectors.groupingBy(verso -> Utils.extrairUltimaSilabaDeUmaSentenca(verso.getSentecaEscandida())));
          
            for (String key : map.keySet()) {
                List<EstruturaVersificacao> versos = map.get(key);

                if (versos.size() <= 1)
                    continue;

                for (int j = 1; j < versos.size(); j++) {                    
                    numTotalDeComparacoes++;                   
                    EstruturaVersificacao verso1 = versos.get(j - 1);
                    EstruturaVersificacao verso2 = versos.get(j);
                 
                    String palavra1 = Utils.extrairUltimaPalavraDeSentenca(verso1.getSegmento());
                    String palavra2 = Utils.extrairUltimaPalavraDeSentenca(verso2.getSegmento());
                    
                    palavra1 = Utils.removeCharEspecial(palavra1);
                    palavra2 = Utils.removeCharEspecial(palavra2);

                    System.out.println("\nComparando: " + palavra1 + " com " + palavra2);
                    
                    String palvraAux;
                    if (palavra2.length() < palavra1.length()) {
                        System.out.println("Palavra 2 menor que palavra 1. Invertendo posições");                        
                        palvraAux = palavra2;
                        palavra2 = palavra1;
                        palavra1 = palvraAux;
                    }

                    List<Character> letrasIguais = new ArrayList<>();                  
                    char[] letrasPalavra1 = Utils.reverterOrdermString(palavra1).toCharArray();
                    char[] letrasPalavra2 = Utils.reverterOrdermString(palavra2).toCharArray();
             
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
