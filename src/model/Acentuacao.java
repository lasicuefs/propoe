package model;

public enum Acentuacao {
    AGUDA("Aguda"),
    GRAVE("Grave"),
    ESDRUXULA("Esdrúxula");

    private final String descricao;

    Acentuacao(String descricao) {
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
