package model;

public class AvaliacaoFonetica {

    private String palavra1;
    private String palavra2;
    private String letrasIguais;

    public AvaliacaoFonetica(String palavra1, String palavra2, String letrasIguais) {
        this.palavra1 = palavra1;
        this.palavra2 = palavra2;
        this.letrasIguais = letrasIguais;
    }

    public String getPalavra1() {
        return palavra1;
    }

    public String getPalavra2() {
        return palavra2;
    }

    public String getLetrasIguais() {
        return letrasIguais;
    }
}