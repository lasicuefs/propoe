package exportacao;

import java.util.ArrayList;
import java.util.List;

public class Sentenca {

    @SuppressWarnings("unused")
    private String segmento;

    private List<EstruturaVersificacao> estruturaDeVesificacao;

    public String getSegmento() {
        return segmento;
    }

    public List<EstruturaVersificacao> getEstruturaDeVesificacao() {
        if (estruturaDeVesificacao == null) {
            estruturaDeVesificacao = new ArrayList<>();
        }
        return estruturaDeVesificacao;
    }
}