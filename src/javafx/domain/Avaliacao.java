package javafx.domain;

import model.Poema;

public final class Avaliacao {

    private final AvaliacaoAcentuacaoHelper avaliacaoAcentuacao;
    private final AvaliacaoFoneticaHelper avaliacaoFonetica;
    private final AvaliacaoPosicaoTonicaHelper avaliacaoPosicaoTonica;
    private double scoreTotal = 0;

    public Avaliacao(Poema poema, Boolean compararEntreVersos) {
        if (poema == null)
            throw new RuntimeException("Para realizar a avaliação o poema não pode ser null");
        avaliacaoAcentuacao = new AvaliacaoAcentuacaoHelper(poema);
        avaliacaoFonetica = new AvaliacaoFoneticaHelper(poema);
        avaliacaoPosicaoTonica = new AvaliacaoPosicaoTonicaHelper(poema);
        avaliacaoPosicaoTonica.setCompararEntreVersos(compararEntreVersos);
    }

    /**
     * Retorna a classe que realiza a avaliação de acentuação.
     *
     * @return AvaliacaoAcentuacaoHelper
     */
    public AvaliacaoAcentuacaoHelper getAvaliacaoAcentuacao() {
        return avaliacaoAcentuacao;
    }

    /**
     * Retorna a classe que realiza a avaliação fonetica.
     *
     * @return AvaliacaoFoneticaHelper
     */
    public AvaliacaoFoneticaHelper getAvaliacaoFonetica() {
        return avaliacaoFonetica;
    }

    /**
     * Retorna a classe que realiza a avaliação de posicionamento de tônica e esquema ritimico.
     *
     * @return AvaliacaoPosicaoTonicaHelper
     */
    public AvaliacaoPosicaoTonicaHelper getAvaliacaoPosicaoTonica() {
        return avaliacaoPosicaoTonica;
    }

    /**
     * Obtem a soma do score total
     *
     * @return double
     */
    public double getScoreTotal() {
        return scoreTotal;
    }

    /**
     * Obtem o valor do score de fonetica.
     *
     * @return double
     */
    public double getScoreFonetica() {
        return this.avaliacaoFonetica.getScore();
    }

    /**
     * Obtem o valor do score de tonica similares.
     *
     * @return double
     */
    public double getScoreTonicaSimilares() {
        return this.avaliacaoPosicaoTonica.getScoreCoincidenciaDeSilabas();
    }

    /**
     * Obtem o valor do score de esquema rítimico.
     *
     * @return double
     */
    public double getScoreEsquemaRitimico() {
        return this.avaliacaoPosicaoTonica.getScorePosicao();
    }

    /**
     * Obtem o valor do score de acentuação.
     *
     * @return double
     */
    public double getScoreAcentuacao() {
        return this.avaliacaoAcentuacao.getScore();
    }

    /**
     * Processa a avaliação do poema e calcula o somatório dos scores.
     */
    public void avaliarPoema() {
        avaliacaoPosicaoTonica.avaliar();
        avaliacaoFonetica.avalicaoFonetica();
        avaliacaoAcentuacao.avaliarAcentuacaoDeVersos();

        scoreTotal += avaliacaoPosicaoTonica.getScore();
        scoreTotal += avaliacaoFonetica.getScore();
        scoreTotal += avaliacaoAcentuacao.getScore();
    }
}