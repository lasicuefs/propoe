package exportacao;

import java.util.ArrayList;
import java.util.List;

public class Sentenca {

    @SuppressWarnings("unused")
    private String segmento;
    private Integer link;
    private Integer numeroDaFrase;

    private List<EstruturaVersificacao> estruturaDeVesificacao;

    public String getSegmento() {
        return segmento;
    }

    public void setSegmento(String segmento) {
        this.segmento = segmento;
    }
    
    
    public Integer getLink(){
        return link;
    }
    
    public Integer getNumeroDaFrase(){
        return numeroDaFrase;
    }

    public List<EstruturaVersificacao> getEstruturaDeVesificacao() {
        if (estruturaDeVesificacao == null) {
            estruturaDeVesificacao = new ArrayList<>();
        }
        return estruturaDeVesificacao;
    }
}
