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
    
    public AvaliacaoAcentuacaoHelper getAvaliacaoAcentuacao() {
        return avaliacaoAcentuacao;
    }

    public AvaliacaoFoneticaHelper getAvaliacaoFonetica() {
        return avaliacaoFonetica;
    }
 
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

    public double getScoreFonetica() {
        return this.avaliacaoFonetica.getScore();
    }

    public double getScoreTonicaSimilares() {
        return this.avaliacaoPosicaoTonica.getScoreCoincidenciaDeSilabas();
    }

    public double getScoreEsquemaRitimico() {
        return this.avaliacaoPosicaoTonica.getScorePosicao();
    }
    
    public double getScoreAcentuacao() {
        return this.avaliacaoAcentuacao.getScore();
    }
    
    public void avaliarPoema() {
        avaliacaoPosicaoTonica.avaliar();
        avaliacaoFonetica.avalicaoFonetica();
        avaliacaoAcentuacao.avaliarAcentuacaoDeVersos();

        scoreTotal += avaliacaoPosicaoTonica.getScore();
        scoreTotal += avaliacaoFonetica.getScore();
        scoreTotal += avaliacaoAcentuacao.getScore();
    }
}
