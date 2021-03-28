package exportacao;

import model.Acentuacao;
import model.Metrica;

public class EstruturaVersificacao {

    private String segmento;
    @SuppressWarnings("unused")
    private Integer numeroDeSilabas;
    @SuppressWarnings("unused")
    private String posicaoDasTonicas;
    @SuppressWarnings("unused")
    private String sentecaEscandida;
    private Metrica metrica;
    private boolean isUsed = false;
    private float indiceSimilaridade;
    private Acentuacao acentuacao;

    public EstruturaVersificacao() {
    }

    public EstruturaVersificacao(String segmento, Integer numeroDeSilabas, String posicaoDasTonicas, String sentecaEscandida, Metrica metrica, boolean isUsed, float indiceSimilaridade) {
        this.segmento = segmento;
        this.numeroDeSilabas = numeroDeSilabas;
        this.posicaoDasTonicas = posicaoDasTonicas;
        this.sentecaEscandida = sentecaEscandida;
        this.metrica = metrica;
        this.isUsed = isUsed;
        this.indiceSimilaridade = indiceSimilaridade;
    }

    public Integer getNumeroDeSilabas() {
        return numeroDeSilabas;
    }

    public String getPosicaoDasTonicas() {
        return posicaoDasTonicas;
    }

    public String getSentecaEscandida() {
        return sentecaEscandida;
    }
    public void setSentecaEscandida(String sentecaEscandida) {
        this.sentecaEscandida = sentecaEscandida;
    }
    public boolean isIsUsed() {
        return isUsed;
    }

    public void setIsUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

    public float getIndiceSimilaridade() {
        return indiceSimilaridade;
    }

    public void setIndiceSimilaridade(float indiceSimilaridade) {
        this.indiceSimilaridade = indiceSimilaridade;
    }

    public String getSegmento() {
        return this.segmento;
    }

    public void setSegmento(String segmento) {
        this.segmento = segmento;
    }

    public Metrica getMetrica() {
        return metrica;
    }

    public void setMetrica(Metrica metrica) {
        this.metrica = metrica;
    }

    public Acentuacao getAcentuacao() {
        return acentuacao;
    }

    public void setAcentuacao(Acentuacao acentuacao) {
        this.acentuacao = acentuacao;
    }
}
