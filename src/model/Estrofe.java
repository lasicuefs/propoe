
package model;

import exportacao.EstruturaVersificacao;

import java.util.ArrayList;
import java.util.List;

public class Estrofe {

    private List<EstruturaVersificacao> versos;
    private Rima rima;

    public Estrofe() {
        this.versos = new ArrayList<>();
    }

    public List<EstruturaVersificacao> getVersos() {
        return versos;
    }

    public void addVerso(EstruturaVersificacao verso) {
        this.versos.add(verso);
    }

    public void setVersos(List<EstruturaVersificacao> versos) {
        this.versos = versos;
    }

    public Rima getRima() {
        return rima;
    }

    public void setRima(Rima rima) {
        this.rima = rima;
    }
}