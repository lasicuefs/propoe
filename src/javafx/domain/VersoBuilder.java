package javafx.domain;

import exportacao.EstruturaVersificacao;
import exportacao.Sentenca;
import javafx.utilities.Utils;
import javafx.utilities.VersoException;
import model.Metrica;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"SimplifyStreamApiCallChains", "ComparatorMethodParameterNotUsed", "OptionalGetWithoutIsPresent", "WeakerAccess"})
public class VersoBuilder {

    private Metrica metrica;
    private List<Sentenca> sentencas;
    private EstruturaVersificacao versoComparacao;

    public void setVersoComparacao(EstruturaVersificacao versoComparacao) {
        this.versoComparacao = versoComparacao;
    }

    public void setMetrica(Metrica metrica) {
        this.metrica = metrica;
    }

    public void setSentencas(List<Sentenca> sentencas) {
        this.sentencas = sentencas;
    }

    public EstruturaVersificacao gerarVersoAleatorio() throws VersoException {
        List<Sentenca> sentencasSorteadas = this.sentencas.stream()
                .filter(sentenca -> sentenca.getEstruturaDeVesificacao()
                        .stream()
                        .noneMatch(EstruturaVersificacao::isIsUsed))
                .filter(sentenca -> sentenca.getEstruturaDeVesificacao()
                        .stream()
                        .anyMatch(verso -> this.metrica == Metrica.TODAS || this.metrica == verso.getMetrica()))
                .sorted((s1, s2) -> Utils.random().nextInt(2) - 1)
                .collect(Collectors.toList());

        if (sentencasSorteadas.isEmpty())
            throw new VersoException("Não foi encontrado sentenças suficientes para gerar o verso aleatóriamente");

        return sentencasSorteadas.get(0).getEstruturaDeVesificacao()
                .stream()
                .filter(verso -> this.metrica == Metrica.TODAS || this.metrica == verso.getMetrica())
                .sorted((s1, s2) -> Utils.random().nextInt(2) - 1)
                .findFirst().get();
    }
    
    public EstruturaVersificacao gerarVersoPorEsquemaRitimico() throws VersoException {       
        List<EstruturaVersificacao> versosDisponiveis = obterVersosDisponiveis();      
        calcularSimilaridade(versosDisponiveis);
        versosDisponiveis.sort(ordenarVersosPorSimilaridade());
        EstruturaVersificacao versoEscolhido = versosDisponiveis.get(0);
        List<EstruturaVersificacao> versosComMesmoIndice = versosDisponiveis
                .stream()
                .filter(verso -> versoEscolhido.getIndiceSimilaridade() == verso.getIndiceSimilaridade())
                .collect(Collectors.toList());
        
        if (versosComMesmoIndice.size() > 1) {
            return versosComMesmoIndice
                    .stream()
                    .filter(verso -> verso.getAcentuacao() == this.versoComparacao.getAcentuacao()) 
                    .sorted((s1, s2) -> Utils.random().nextInt(2) - 1) 
                    .findFirst() 
                    .orElse(versoEscolhido); 
        }

        return versoEscolhido;
    }

    public EstruturaVersificacao gerarVersoPorPosicaoTonica() throws VersoException {
        List<EstruturaVersificacao> versosFiltrados = obterVersosDisponiveis()
                .parallelStream()
                .filter(verso -> verso.getAcentuacao() == this.versoComparacao.getAcentuacao())
                .collect(Collectors.toList());

        if (versosFiltrados.size() >= 1) {
            calcularSimilaridade(versosFiltrados);
            return versosFiltrados
                    .stream()
                    .sorted(ordenarVersosPorSimilaridade())
                    .findFirst().get();
        } else {
            System.out.println ("Não foi possível gerar o verso por posicionamento de tônica. A silaba não tem versos com a mesma acentuação disponíveis.");
            throw new VersoException();
        }
    }

    private void calcularSimilaridade(List<EstruturaVersificacao> estruturas) {
        estruturas.parallelStream().forEach(estrutura -> {
            String tonicaEstrutura = estrutura.getPosicaoDasTonicas();
            float indice = calcularsimilaridadeDoPadrao(this.versoComparacao.getPosicaoDasTonicas(), tonicaEstrutura);
            estrutura.setIndiceSimilaridade(indice);
        });
    }

    private List<EstruturaVersificacao> obterVersosDisponiveis() throws VersoException {
        List<EstruturaVersificacao> versosDisponiveis = this.sentencas.parallelStream()                
                .filter(sentenca -> sentenca.getEstruturaDeVesificacao().stream().noneMatch(EstruturaVersificacao::isIsUsed))               
                .flatMap(sentenca -> sentenca.getEstruturaDeVesificacao().stream())             
                .filter(verso -> this.metrica == Metrica.TODAS || this.metrica == verso.getMetrica())
                .collect(Collectors.toList());

        if (versosDisponiveis.isEmpty()) {
            System.out.println ("Não existe versos disponíveis para uso");
            throw new VersoException();
        }

        return versosDisponiveis;
    }

    private Comparator<EstruturaVersificacao> ordenarVersosPorSimilaridade() {
        return (v1, v2) -> {
            float indiceV1 = v1.getIndiceSimilaridade();
            float indiceV2 = v2.getIndiceSimilaridade();
            return Float.compare(indiceV2, indiceV1);
        };
    }

    private static void marcarPosicoes(String[] posicoes, int[] vetorASerMarcado) { //analisar código
        for (String i : posicoes) {
            int aux = Integer.parseInt(i);
            vetorASerMarcado[aux - 1] = 1;
        }
    }

    private static void inicializaVetor(int[] vetor) {
        for (int i = 0; i < vetor.length; i++) {
            vetor[i] = 0;
        }
    }

    public static float calcularsimilaridadeDoPadrao(String padraoOrigem, String padraoVerificar) {
        if (padraoOrigem.equals(padraoVerificar)) {
            return 1;
        }
        float score;
        String origem[] = padraoOrigem.split(" ");
        int vetorOrigem[] = new int[Integer.parseInt(origem[origem.length - 1])];
        inicializaVetor(vetorOrigem);
        marcarPosicoes(origem, vetorOrigem);
        String aVerificar[] = padraoVerificar.split(" ");
        int[] vetorVerificar = new int[Integer.parseInt(aVerificar[aVerificar.length - 1])];
        inicializaVetor(vetorVerificar);
        marcarPosicoes(aVerificar, vetorVerificar);
        HashSet<String> uniao = new HashSet<>();
        uniao.addAll(Arrays.asList(origem));
        uniao.addAll(Arrays.asList(aVerificar));
        float qtdUniao = uniao.size();
        int equivalencias = 0;
        for (int i = 0; i < vetorOrigem.length && i < vetorVerificar.length; i++) {
            if (vetorOrigem[i] == 1 && vetorVerificar[i] == 1) {
                equivalencias++;
            }
        }
        score = equivalencias / qtdUniao;
        return score;
    }
}
