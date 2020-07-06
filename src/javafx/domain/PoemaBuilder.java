package javafx.domain;

import exportacao.Sentenca;
import javafx.utilities.PoemaException;
import javafx.utilities.VersoException;
import model.*;

import java.util.List;
import java.util.Map;


/**
 * @author Ana Cleyge
 */
public class PoemaBuilder {

    private Map<String, List<Sentenca>> hp;
    private TipoSimilaridade tipoSimilaridade;
    private Boolean compararRimas;
    private Integer numEstrofes;
    private Integer numDeVersos;
    private Metrica metrica;
    private String silaba1;
    private String silaba2;
    private String silaba3;
    private String silaba4;
    private Rima rima;

    public PoemaBuilder(Map<String, List<Sentenca>> hp) {
        this.hp = hp;
    }

    /**
     * Define se o poema deve realizar comparação entre rimas ou não
     *
     * @param compararRimas boolean com true para comparar entre versos e false para comparar com o primeiro verso.
     */
    public void setComparacaoEntreVersos(boolean compararRimas) {
        this.compararRimas = compararRimas;
    }

    /**
     * Define o número de estrofes que o poema deve conter.
     *
     * @param numEstrofes int com a quantidade de estrofes a ser gerado.
     */
    public void setNumEstrofes(int numEstrofes) {
        this.numEstrofes = numEstrofes;
    }

    /**
     * Define o número de versos que cada estrofe deve conter.
     *
     * @param numDeVersos int com a quantidade de. versos
     */
    public void setNumDeVersos(int numDeVersos) {
        this.numDeVersos = numDeVersos;
    }

    /**
     * Define o tipo de rima usada para gerar o estrofe
     *
     * @param rima {@link Rima}
     */
    public void setRima(Rima rima) {
        this.rima = rima;
    }

    /**
     * Define o tipo de metrica utilizada para gerar os verso.
     *
     * @param metrica {@link Metrica}
     */
    public void setMetrica(Metrica metrica) {
        this.metrica = metrica;
    }

    /**
     * Define qual a silaba 1.
     *
     * @param silaba1 {@link String} com a silaba 1
     */
    public void setSilaba1(String silaba1) {
        this.silaba1 = silaba1;
    }

    /**
     * Define qual a silaba 2.
     *
     * @param silaba2 {@link String} com a silaba 2.
     */
    public void setSilaba2(String silaba2) {
        this.silaba2 = silaba2;
    }

    /**
     * Define qual a silaba 3.
     *
     * @param silaba3 {@link String} com a silaba 3
     */
    public void setSilaba3(String silaba3) {
        this.silaba3 = silaba3;
    }

    /**
     * Define qual a silaba 4.
     *
     * @param silaba4 {@link String} com a silaba 4.
     */
    public void setSilaba4(String silaba4) {
        this.silaba4 = silaba4;
    }

    /**
     * Define o tipo de similaridade que o poema deve utilizar para gerar o proximo verso.
     *
     * @param tipoSimilaridade {@link TipoSimilaridade}
     */
    public void setTipoSimilaridade(TipoSimilaridade tipoSimilaridade) {
        this.tipoSimilaridade = tipoSimilaridade;
    }

    /**
     * Método resposável por gerar o poema.
     */
    public Poema gerarPoema() throws PoemaException {
        System.out.println("\nStart " + this.getClass().getSimpleName() + ".gerarPoema().");
        System.out.println("Rima: " + rima + ", Metrica: " + metrica + ", numEstrofes: " + numEstrofes +
                ",numVersos: " + numDeVersos + ", silaba1: " + silaba1 + ", silaba2: " + silaba2 +
                ", silaba3: " + silaba3 + ", silaba4: " + silaba4);

        Poema poema = new Poema();
        //Sempre que for gerar um novo poema, as estruturas de versificações devem estar sem informação de uso e similiaridade
        redefinirEstruturasUtilizadas();
        //Instancia um novo construtor de estrofes e define a comparação entre rimas, numero de versos, tipo de similaridade e metrica.
        EstrofeBuilder estrofeBuilder = new EstrofeBuilder();
        estrofeBuilder.setCompararRimas(this.compararRimas);
        estrofeBuilder.setNumDeVersos(this.numDeVersos);
        estrofeBuilder.setTipoSimilaridade(this.tipoSimilaridade);
        estrofeBuilder.setMetrica(this.metrica);

        //Estrutura reposável por gerar o números de estrofes da variavel numEstrofe.
        for (int i = 0; i < this.numEstrofes; i++) {
            //Verifica se é necessário mudar as sentenças ao gerar o proximo estrofe.
            //Situação necessária quando utilizam-se 4 silabas
            if (precisaMudarSentencas()) {
                if (i % 2 == 0) { //estrofes pares utiliza silabas 1 e 2
                    estrofeBuilder.setSentencas(this.hp.get(this.silaba1), this.hp.get(this.silaba2));
                } else { //estofes impares utiliza silabas 3 e 4
                    estrofeBuilder.setSentencas(this.hp.get(this.silaba3), this.hp.get(this.silaba4));
                }
            } else { //caso não precise mudar, sempre obtem a silaba 1 e 2
                estrofeBuilder.setSentencas(this.hp.get(this.silaba1), this.hp.get(this.silaba2));
            }
            //após definir as sentenças que o construtor de estrofe irá utilizar. Chama o método build passando a rima
            //como parâmetro e obtendo um novo estrofe. Após o novo estrofe gerado. Adiciona o estrofe ao poema.
            Estrofe estrofe;

            int count = 0;
            while (true) {
                System.out.println("Gerando estrofe tentativa " + count);
                if (count > 20) {
                    throw new PoemaException("Foram realizadas " + count + " de tentativas de gerar estrofe.");
                }
                count++;

                try {
                    estrofe = estrofeBuilder.build(rima);
                    break;
                } catch (VersoException e) {
                    System.out.println("Erro ao gerar um estrofe, tentando gerar novamente. ");
                }
            }

            poema.addEstrofe(estrofe);
        }

        System.out.println("End " + this.getClass().getSimpleName() + ".build().\n");
        return poema;
    }

    /**
     * Garante que não havera informações obsoletas durante a geração de um novo poema
     */
    private void redefinirEstruturasUtilizadas() {
        if (this.silaba1 != null) redefinirSentencas(this.hp.get(this.silaba1));
        if (this.silaba2 != null) redefinirSentencas(this.hp.get(this.silaba2));
        if (this.silaba3 != null) redefinirSentencas(this.hp.get(this.silaba3));
        if (this.silaba4 != null) redefinirSentencas(this.hp.get(this.silaba4));
    }

    /**
     * Remove a informação das estruturas de versificação, que já foram utilizadas e o seu indice de similiaridade
     */
    private void redefinirSentencas(List<Sentenca> sentencas) {
        if (sentencas == null) return;
        sentencas
                .parallelStream().forEach(sentenca -> sentenca.getEstruturaDeVesificacao()
                .parallelStream().forEach(estruturaVersificacao -> {
                    estruturaVersificacao.setIsUsed(false);
                    estruturaVersificacao.setIndiceSimilaridade(0);
                }));
    }

    /**
     * Verifica se é nécessário realizar a mudança de dupla de sequência de silabas a cada estrofe gerado.
     * Pra situações que o usuário escolheu 4 silabas.
     * Sentenças vazias ou nulas indica que o usuário não escolheu a silaba.
     *
     * @return true se é necessário mudar, false se não for necessário mudar.
     */
    private boolean precisaMudarSentencas() {
        return (this.silaba3 != null && !this.hp.get(this.silaba3).isEmpty()) && (this.silaba4 != null && !this.hp.get(this.silaba4).isEmpty());
    }
}