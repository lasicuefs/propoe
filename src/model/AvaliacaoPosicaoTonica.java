package model;

public class AvaliacaoPosicaoTonica {
    private String posicaoRitimo1;
    private String posicaoRitimo2;
    private String coincidentes;

    public AvaliacaoPosicaoTonica(String posicaoRitimo1,
                                  String posicaoRitimo2,
                                  String coincidentes) {
        this.posicaoRitimo1 = posicaoRitimo1;
        this.posicaoRitimo2 = posicaoRitimo2;
        this.coincidentes = coincidentes;
    }

    public String getPosicaoRitimo1() {
        return posicaoRitimo1;
    }

    public String getPosicaoRitimo2() {
        return posicaoRitimo2;
    }

    public String getCoincidentes() {
        return coincidentes;
    }
}