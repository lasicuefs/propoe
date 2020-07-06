
package model;

import java.util.ArrayList;
import java.util.List;

public class Poema {

    private List<Estrofe> estrofes;

    public Poema() {
        estrofes = new ArrayList<>();
    }

    public List<Estrofe> getEstrofes() {
        return estrofes;
    }

    public void addEstrofe(Estrofe estrofe) {
        this.estrofes.add(estrofe);
    }
}