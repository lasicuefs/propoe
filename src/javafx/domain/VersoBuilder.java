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

/**
 * Classe resposável por escolher uma Estrutura de versificação ideal, dentro de uma lista de sentenças, baseando-se
 * nos parrametros de metricas e de similaridade
 *
 * @author Ana Cleyge
 */
@SuppressWarnings({"SimplifyStreamApiCallChains", "ComparatorMethodParameterNotUsed", "OptionalGetWithoutIsPresent", "WeakerAccess"})
public class VersoBuilder {

    //Metrica utilizada para obter uma estrutura de versificacao (Número de silabas do verso)
    private Metrica metrica;
    //Lista de sentencas de versificação que contem os versos a serem buscados
    private List<Sentenca> sentencas;
    private EstruturaVersificacao versoComparacao;

    /**
     * Define uma estrutura de versificação para servir como base de comparação para os proximos versos a serem gerados.
     *
     * @param versoComparacao {@link EstruturaVersificacao} que servirá de comparação.
     */
    public void setVersoComparacao(EstruturaVersificacao versoComparacao) {
        this.versoComparacao = versoComparacao;
    }

    /**
     * Define a metrica do novo verso
     *
     * @param metrica {@link Metrica}
     */
    public void setMetrica(Metrica metrica) {
        this.metrica = metrica;
    }

    /**
     * Define uma lista com as sentenças da qual o verso deverá ser extraido.
     *
     * @param sentencas List<Sentenca>
     */
    public void setSentencas(List<Sentenca> sentencas) {
        this.sentencas = sentencas;
    }

    /**
     * Método resposável por gerar um verso aleatóriamente. Utilizado para gerar primeiro verso de uma estrofe,
     * no qual não é ncessário realizar nenhuma comparação com versos anteriores.
     * Utiliza o metétdo para obter uma Stream com as sentenças que não foram utilizadas e
     * que atendam a {@link #metrica}.
     * Depois realiza a operação de sort, para ordenar as sentenças aleatóriamente, e buscar a primeira {@link Sentenca} após
     * a ordenação.
     * Caso não seja possǘeil encontar uma sentença, será lançada uma exception {@link VersoException}, para informar
     * que não houve sentenças disponível para a geração do verso.
     * <p>
     * Após a sentença ser escolhida, procura na lista de {@link EstruturaVersificacao} da sentença, uma estrutura que seja
     * compatível com a {@link #metrica} definida. Ordena aleatóriamete e se caso haja duas estruturas com uma mesma metrica, ou se
     * for ecolhido a métrica {@link Metrica#TODAS} sejá escolhido uma metrica aleatóriamente.
     *
     * @return EstruturaVersificacao verso gerado.
     */
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

    /**
     * Metódo resposável por gerar um verso usando o calculo de similaridade por esquema rítimico.
     * Obtem a lista de versos disponíveis usando o método {@link #obterVersosDisponiveis()}, realiza o calculo de
     * similaridade da lista de versos, e ordena os versos pelo maior indice de similaridade do verso usando o método
     * {@link #ordenarVersosPorSimilaridade()}.
     * O primeiro verso da lista é o mais similar ao {@link #versoComparacao}. Após obter o primeiro verso, verifica se
     * existem mais versos com o mesmo indice de similaridade. Caso não exista, será retornado o primeiro verso.
     * Caso exista, será filtrado os versos que tenham a mesma acentuação do verso {@link #versoComparacao} e obtém o
     * primeiro aleatóriamente.
     */
    public EstruturaVersificacao gerarVersoPorEsquemaRitimico() throws VersoException {
        //Obtém os versos disponíveis para uso
        List<EstruturaVersificacao> versosDisponiveis = obterVersosDisponiveis();
        //Calcula a similaridade da lista de versos
        calcularSimilaridade(versosDisponiveis);
        //Ordena a lista pelo maior indice de similaridade
        versosDisponiveis.sort(ordenarVersosPorSimilaridade());
        //O primeiro verso da lista é o mais similar ao versoComparacao.
        EstruturaVersificacao versoEscolhido = versosDisponiveis.get(0);
        //Verifica se existem versos com o mesmo indíce do verso escolhido.
        List<EstruturaVersificacao> versosComMesmoIndice = versosDisponiveis
                .stream()
                .filter(verso -> versoEscolhido.getIndiceSimilaridade() == verso.getIndiceSimilaridade())
                .collect(Collectors.toList());
        /* Se hover vários versos com o mesmo indice. Passa para a segunda verificação, na qual será verificado se o verso
         * tem o mesma acentuação do verso de comparação. Está representado na primeira condição.
         * */
        if (versosComMesmoIndice.size() > 1) {
            return versosComMesmoIndice
                    .stream()
                    .filter(verso -> verso.getAcentuacao() == this.versoComparacao.getAcentuacao()) //filtra os versos que contem a mesma acentuação do verso de comparação
                    .sorted((s1, s2) -> Utils.random().nextInt(2) - 1) //ordena aleatóriamente
                    .findFirst() //obtem o primeiro
                    .orElse(versoEscolhido); //se não houver nenhum verso na lista, retonar o primeiro verso escolhido
        }

        return versoEscolhido;
    }


    /**
     * Método resposável por gerar um novo verso levando em consideração a acentuação do verso {@link #versoComparacao}
     * Primeiro, obtem a lista de versos disponíveis, e filtra os que tem a mesma acentuação do {@link #versoComparacao}
     * Se houver versos na lista #versosFiltrados, faz o calculo da similaridade desses versos, ordena-os aleatóriamente
     * e obtem o primeiro verso. Caso não encontre nenhum verso, será lançado uma {@link VersoException}.
     */
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
            System.out.println("Não foi possível gerar o verso por posicionamento de tônica. A silaba não tem versos com a mesma acentuação disponíveis.");
            throw new VersoException();
        }
    }

    /**
     * Faz o cálculo da similaridade da lista de versos passado por parâmetro. O calculo usando o método
     * {@link #calcularsimilaridadeDoPadrao(String, String)} leva em consideração a posição de tônica do
     * {@link #versoComparacao} com o padrão de cada verso da lista. Após realizar o calculo define o indice de similaridade
     * no verso.
     *
     * @param estruturas lista de versos a serem calculado.
     */
    private void calcularSimilaridade(List<EstruturaVersificacao> estruturas) {
        estruturas.parallelStream().forEach(estrutura -> {
            String tonicaEstrutura = estrutura.getPosicaoDasTonicas();
            float indice = calcularsimilaridadeDoPadrao(this.versoComparacao.getPosicaoDasTonicas(), tonicaEstrutura);
            estrutura.setIndiceSimilaridade(indice);
        });
    }

    /**
     * Método resposável por obter uma lista de versos disponíveis para busca de um novo verso.
     * Filtra somente sentenças que não tenham versos que já foram utilizados, depois converte a lista sentenças em
     * lista de versos, e filtra os versos que contenham a {@link #metrica}.
     *
     * @return versosDisponiveis. a lista de versos disponíveis.
     */
    private List<EstruturaVersificacao> obterVersosDisponiveis() throws VersoException {
        List<EstruturaVersificacao> versosDisponiveis = this.sentencas.parallelStream()
                //filtra todas as sentenças que não foram utilizadas
                .filter(sentenca -> sentenca.getEstruturaDeVesificacao().stream().noneMatch(EstruturaVersificacao::isIsUsed))
                //transfroma a lista de sentenças em lista de estrutura de versificação
                .flatMap(sentenca -> sentenca.getEstruturaDeVesificacao().stream())
                //filtra os versos que atendem a metrica informada
                .filter(verso -> this.metrica == Metrica.TODAS || this.metrica == verso.getMetrica())
                //converte a stream em uma lista
                .collect(Collectors.toList());

        if (versosDisponiveis.isEmpty()) {
            System.out.println("Não existe versos disponíveis para uso");
            throw new VersoException();
        }

        return versosDisponiveis;
    }

    /**
     * Método retorna um objeto {@link Comparator<EstruturaVersificacao>} no qual realiza a comparação entre dois versos
     * usando o indice de similaridade do maior para o menor.
     */
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