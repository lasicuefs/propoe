package javafx.utilities;

import model.Acentuacao;
import model.Metrica;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("WeakerAccess")
public abstract class Utils {
    public static Random random() {
        return new Random();
    }

    /***
     * Recebe posições das silabas tonicas e utiliza o valor da ultima silaba tônica para definir a metrica
     *
     * @param posicaoDasTonicas {@link String} contento a posição das silabas
     *
     * @return {@link Metrica} a metrica extraida das posições
     * */
    public static Metrica obterMetricaDoVerso(String posicaoDasTonicas) {

        String[] posicoes = posicaoDasTonicas.split(" ");
        Integer posicao = Integer.parseInt(posicoes[posicoes.length - 1]);

        return Metrica.fromId(posicao);
    }

    /**
     * Extair um a ultima silaba de uma sentença, seja ela uma palavra ou uma sentença escandida
     *
     * @param sentenca sentença
     * @return String com a silaba
     */
    public static String extrairUltimaSilabaDeUmaSentenca(String sentenca) {
        if (sentenca == null || sentenca.isEmpty())
            throw new RuntimeException("A sentença nao pode ser nula ou vazia");

        sentenca = removeCharEspecial(sentenca);

        int start = sentenca.lastIndexOf('/') + 1;
        int end = sentenca.length();

        return sentenca.substring(start, end);
    }

    /**
     * Remove qualquer caracter especial do inico ou fim de uma string. Previne a senteças terminadas com (..., /, -, #)
     *
     * @param string {@link String} a ser tratada
     * @return {@link String} com os caracteres especiais removidos
     */
    public static String removeCharEspecial(String string) {
        if (string == null || string.isEmpty())
            throw new RuntimeException("A string nao pode ser nula ou vazia");

        return string.replaceAll("^[.,/#!$%^&*;:{}=\\-_`~()]*|[.,/#!$%^&*;:{}=\\-_`~()\\s]*$", "");
    }
     public static String limparSentencas(String sentenca){
        return sentenca.replaceAll("^[^A-Za-z]*|[^A-Za-z]*$", "");
    }

    public static String reverterOrdermString(String string) {
        return new StringBuffer(string).reverse().toString();
    }

    /**
     * Obtem a ultima palavra de uma sentença escândida.
     *
     * @return String com a ultima palavra
     */
    public static String extrairUltimaPalavraDeSentenca(String sentencaEscandida) {
        String[] palavras = sentencaEscandida.trim().split(" "); //quebra a sentenca em palavras
        return palavras[palavras.length - 1]; //obtem a ultima palavra
    }

    /**
     * Contas a quantidade de tonicas da interesecao de dois posicionamento de tonicas
     *
     * @param tonicasA array contendo as posicao de tonicas
     * @param tonicasB array contendo as posicao de tonicas
     * @return int numero de tonicas da intersecao
     */
    public static int contarInteresecaoDeTonicas(String[] tonicasA, String[] tonicasB) {
        return (int) Stream.concat(Arrays.stream(tonicasA), Arrays.stream(tonicasB))
                .collect(Collectors.groupingBy(strings -> strings, Collectors.counting()))
                .entrySet()
                .stream()
                .filter(item -> item.getValue() > 1)
                .count();
    }

    public static double contarUnicaoDeTonicas(String[] tonicasA, String[] tonicasB) {
        return Stream.concat(Arrays.stream(tonicasA), Arrays.stream(tonicasB))
                .collect(Collectors.groupingBy(strings -> strings, Collectors.counting()))
                .entrySet()
                .size();
    }

    /**
     * Obtem a acentuação da palavra
     *
     * @param palavra A palavra a ser analisada, a mesma deve vir em forma de sentenca escandida
     * @return {@link model.Acentuacao} A acentuação da palavra; {@code null} Caso não exista.
     */
    public static Acentuacao obterAcentuacaoDaPalavra(String palavra) {
        String[] silabas = palavra.split("/"); //quebra a palavra em silabas

        for (int i = (silabas.length - 1); i >= 0; i--) {
            String silaba = silabas[i];
            //A posição invertida da silaba
            int rank = silabas.length - i;

            if (silaba.contains("#")) {
                switch (rank) {
                    case 1:
                        return Acentuacao.AGUDA;
                    case 2:
                        return Acentuacao.GRAVE;
                    default:
                        return Acentuacao.ESDRUXULA;
                }
            }
        }
        return null;
    }
}
