package model;

public enum Fonetica {
    CONSOANTE("Consoante"),
    TOANTE("Toante"),
    ALITERANTE("Aliterante");

    private final String descricao;

    Fonetica(String descricao) {
        this.descricao = descricao;
    }

    public String getDescription() {
        return this.descricao;
    }

    @Override
    public String toString() {
        return this.descricao;
    }
}