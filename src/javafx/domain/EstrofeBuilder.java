package javafx.domain;

import exportacao.EstruturaVersificacao;
import exportacao.Sentenca;
import javafx.utilities.Utils;
import javafx.utilities.VersoException;
import model.Estrofe;
import model.Metrica;
import model.Rima;
import model.TipoSimilaridade;

import java.util.ArrayList;
import java.util.List;

public final class EstrofeBuilder {
    private Metrica metrica;
    private Integer numDeVersos;
    private Boolean compararEntreVersos;
    private List<Sentenca> sentencasSilaba1;
    private List<Sentenca> sentencasSilaba2;
    private TipoSimilaridade tipoSimilaridade;

    EstrofeBuilder() {
        this.compararEntreVersos = false;
    }

    public void setNumDeVersos(Integer numDeVersos) {
        this.numDeVersos = numDeVersos;
    }
    
    public void setMetrica(Metrica metrica) {
        this.metrica = metrica;
    }

    public void setSentencas(List<Sentenca> sentencasSilaba1, List<Sentenca> sentencasSilaba2) {
        this.sentencasSilaba1 = sentencasSilaba1;
        this.sentencasSilaba2 = sentencasSilaba2;
    }

    public void setTipoSimilaridade(TipoSimilaridade tipoSimilaridade) {
        this.tipoSimilaridade = tipoSimilaridade;
    }

    public void setCompararRimas(Boolean compararRimas) {
        this.compararEntreVersos = compararRimas;
    }

    public Estrofe build(Rima rima) throws VersoException {
        System.out.println("\nStart " + this.getClass().getSimpleName() + ".build()");
        System.out.println("Rima: " + rima + ", Metrica: " + metrica + ", numDeVersos: " + numDeVersos + ", compararEntreVersos: " + compararEntreVersos + ", similaridade: " + tipoSimilaridade + ", sizeSentencas1: " + sentencasSilaba1.size() + ", sizeSentencas2: " + sentencasSilaba2.size());

        Estrofe novoEstrofe = new Estrofe(); 
        novoEstrofe.setRima(rima);

        VersoBuilder versoBuilder = new VersoBuilder(); 
        versoBuilder.setMetrica(this.metrica); 

        System.out.println("###################### Nível de Similaridade ####################################");
        List<EstruturaVersificacao> versos = new ArrayList<>(); 
        for (int i = 0; i < numDeVersos; i++) {
            EstruturaVersificacao novoVerso;
            List<Sentenca> sentencas = obterSetencasPorRima(rima, i);
            versoBuilder.setSentencas(sentencas);

            if (i == 0) {
                novoVerso = versoBuilder.gerarVersoAleatorio();
            } else {              
                EstruturaVersificacao versoComparacao = compararEntreVersos ? versos.get(versos.size() - 1) : versos.get(0);
                versoBuilder.setVersoComparacao(versoComparacao);
                if (this.tipoSimilaridade == TipoSimilaridade.ESQUEMA_RITMICO)
                    novoVerso = versoBuilder.gerarVersoPorEsquemaRitimico();
                else
                    novoVerso = versoBuilder.gerarVersoPorPosicaoTonica();
            }

            System.out.println("Sentença: " + novoVerso.getSentecaEscandida() + "\t-   " + novoVerso.getPosicaoDasTonicas());
            System.out.println("Nota: " + novoVerso.getIndiceSimilaridade());
            novoVerso.setIsUsed(true);
            versos.add(novoVerso);
        }
        
        novoEstrofe.setVersos(versos);
        System.out.println("\nFinish " + this.getClass().getSimpleName() + ".build()");
        return novoEstrofe;
    }
    
    private List<Sentenca> obterSetencasPorRima(Rima rima, Integer index) {
        switch (rima) {
            case CRUZADA:
                return index % 2 == 0 ? sentencasSilaba1 : sentencasSilaba2;
            case EMPARELHADA:
                int metade = (numDeVersos + 1) / 2;
                return index < metade ? sentencasSilaba1 : sentencasSilaba2;
            case INTERPOLADA:
                return index % 3 == 0 ? sentencasSilaba1 : sentencasSilaba2;
            case MISTA:
                int random = Utils.random().nextInt(2);
                return random == 0 ? sentencasSilaba1 : sentencasSilaba2;
            default:
                throw new RuntimeException("É necessário informar um tipo de rima");
        }
    }
}
