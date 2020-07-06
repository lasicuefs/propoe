package model;

public class UnidadeDeVersificacao {

    private String padrao;
    private int quantidade;
    private float similaridade;

    public UnidadeDeVersificacao(String padrao, int quantidade) {
        this.padrao = padrao;
        this.quantidade = quantidade;
    }

    public String getPadrao() {
        return padrao;
    }

    public void setPadrao(String padrao) {
        this.padrao = padrao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public float getSimilaridade() {
        return similaridade;
    }

    public void setSimilaridade(float similaridade) {
        this.similaridade = similaridade;
    }
}