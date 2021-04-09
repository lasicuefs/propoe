package javafx.domain;

import exportacao.Sentenca;
import javafx.utilities.PoemaException;
import javafx.utilities.VersoException;
import model.*;

import java.util.List;
import java.util.Map;

public class PoemaBuilder {

    private Map<String, List<Sentenca>> hp;
    private TipoSimilaridade tipoSimilaridade;
    private Boolean compararRimas;
    private Integer numEstrofes;
    private Integer numDeVersos;
    private Metrica metrica;
    private String silaba1;
    private String silaba2;
    private String silaba3;
    private String silaba4;
    private Rima rima;

    public PoemaBuilder(Map<String, List<Sentenca>> hp) {
        this.hp = hp;
    }

    public void setComparacaoEntreVersos(boolean compararRimas) {
        this.compararRimas = compararRimas;
    }

    public void setNumEstrofes(int numEstrofes) {
        this.numEstrofes = numEstrofes;
    }

    public void setNumDeVersos(int numDeVersos) {
        this.numDeVersos = numDeVersos;
    }

    public void setRima(Rima rima) {
        this.rima = rima;
    }

    public void setMetrica(Metrica metrica) {
        this.metrica = metrica;
    }

    public void setSilaba1(String silaba1) {
        this.silaba1 = silaba1;
    }

    public void setSilaba2(String silaba2) {
        this.silaba2 = silaba2;
    }

    public void setSilaba3(String silaba3) {
        this.silaba3 = silaba3;
    }

    public void setSilaba4(String silaba4) {
        this.silaba4 = silaba4;
    }

    public void setTipoSimilaridade(TipoSimilaridade tipoSimilaridade) {
        this.tipoSimilaridade = tipoSimilaridade;
    }

    public Poema gerarPoema() throws PoemaException {
        System.out.println("\nStart " + this.getClass().getSimpleName() + ".gerarPoema().");
        System.out.println("Rima: " + rima + ", Metrica: " + metrica + ", numEstrofes: " + numEstrofes +
                ",numVersos: " + numDeVersos + ", silaba1: " + silaba1 + ", silaba2: " + silaba2 +
                ", silaba3: " + silaba3 + ", silaba4: " + silaba4);

        Poema poema = new Poema();
    
        redefinirEstruturasUtilizadas();
        EstrofeBuilder estrofeBuilder = new EstrofeBuilder();
        estrofeBuilder.setCompararRimas(this.compararRimas);
        estrofeBuilder.setNumDeVersos(this.numDeVersos);
        estrofeBuilder.setTipoSimilaridade(this.tipoSimilaridade);
        estrofeBuilder.setMetrica(this.metrica);

        for (int i = 0; i < this.numEstrofes; i++) {
         
            if (precisaMudarSentencas()) {
                if (i % 2 == 0) { 
                    estrofeBuilder.setSentencas(this.hp.get(this.silaba1), this.hp.get(this.silaba2));
                } else { 
                    estrofeBuilder.setSentencas(this.hp.get(this.silaba3), this.hp.get(this.silaba4));
                }
            } else { 
                estrofeBuilder.setSentencas(this.hp.get(this.silaba1), this.hp.get(this.silaba2));
            }
        
            Estrofe estrofe;

            int count = 0;
            while (true) {
                System.out.println("Gerando estrofe tentativa " + count);
                if (count > 20) {
                    throw new PoemaException("Foram realizadas " + count + " de tentativas de gerar estrofe.");
                }
                count++;

                try {
                    estrofe = estrofeBuilder.build(rima);
                    break;
                } catch (VersoException e) {
                    System.out.println("Erro ao gerar um estrofe, tentando gerar novamente. ");
                }
            }

            poema.addEstrofe(estrofe);
        }

        System.out.println("End " + this.getClass().getSimpleName() + ".build().\n");
        return poema;
    }
    
    private void redefinirEstruturasUtilizadas() {
        if (this.silaba1 != null) redefinirSentencas(this.hp.get(this.silaba1));
        if (this.silaba2 != null) redefinirSentencas(this.hp.get(this.silaba2));
        if (this.silaba3 != null) redefinirSentencas(this.hp.get(this.silaba3));
        if (this.silaba4 != null) redefinirSentencas(this.hp.get(this.silaba4));
    }

    private void redefinirSentencas(List<Sentenca> sentencas) {
        if (sentencas == null) return;
        sentencas
                .parallelStream().forEach(sentenca -> sentenca.getEstruturaDeVesificacao()
                .parallelStream().forEach(estruturaVersificacao -> {
                    estruturaVersificacao.setIsUsed(false);
                    estruturaVersificacao.setIndiceSimilaridade(0);
                }));
    }
   
    private boolean precisaMudarSentencas() {
        return (this.silaba3 != null && !this.hp.get(this.silaba3).isEmpty()) && (this.silaba4 != null && !this.hp.get(this.silaba4).isEmpty());
    }
}
