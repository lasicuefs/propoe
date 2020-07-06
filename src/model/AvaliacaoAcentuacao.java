package model;

public class AvaliacaoAcentuacao {

    private final String palavra;
    private final Acentuacao acentuacao;

    public AvaliacaoAcentuacao(String palavra, Acentuacao acentuacao) {
        this.palavra = palavra;
        this.acentuacao = acentuacao;
    }

    public String getPalavra() {
        return palavra;
    }

    public Acentuacao getAcentuacao() {
        return acentuacao;
    }
}