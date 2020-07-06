package javafx.domain;

import exportacao.EstruturaVersificacao;
import exportacao.Sentenca;
import javafx.collections.ObservableList;
import javafx.utilities.Utils;
import model.Acentuacao;
import model.Metrica;
import model.Rima;
import model.UnidadeDeVersificacao;

import java.util.*;

/**
 * @author Ana Cleyge
 */
public class SentencasHelper {

    private final HashMap<String, List<Sentenca>> hp = new HashMap<>();

    public SentencasHelper(List<Sentenca> sentencas) {
        this.processarSentencas(sentencas);
    }

    public HashMap<String, List<Sentenca>> getHp() {
        return hp;
    }

    /***
     * Recebe a lista de senteças e preenche o {@link HashMap} hp, calcula a metrica da unidade de versificação.
     * Define também o segmento da sentença, na estrutura de versificação.
     *
     * @param sentencas {@link List<Sentenca>} lista com as sentenças a serem processadas
     * */
    private void processarSentencas(List<Sentenca> sentencas) {
        for (Sentenca sentenca : sentencas) {
            //Adiciona na estrutura de versificação a métrica extraida da posição das tônicas.
            //Corrige o bug do arquivo XML que vem com o número de silabas diferente da ultima silaba tônica
            sentenca.getEstruturaDeVesificacao().forEach(es -> {
                Metrica metrica = Utils.obterMetricaDoVerso(es.getPosicaoDasTonicas());
                String palavra = Utils.extrairUltimaPalavraDeSentenca(es.getSentecaEscandida());
                Acentuacao acentuacao = Utils.obterAcentuacaoDaPalavra(palavra);
                es.setSegmento(sentenca.getSegmento());
                es.setMetrica(metrica);
                es.setAcentuacao(acentuacao);
            });

            StringTokenizer st = new StringTokenizer(sentenca.getEstruturaDeVesificacao().get(0).getSentecaEscandida());
            String aux = "";
            while (st.hasMoreElements()) {
                aux = st.nextToken();
            }

            String silaba[] = aux.split("/");
            String key = silaba[silaba.length - 1];

            if (!(this.hp.containsKey(key))) {
                ArrayList<Sentenca> sentencasAux = new ArrayList<>();
                sentencasAux.add(sentenca);
                this.hp.put(key, sentencasAux);
            } else {
                this.hp.get(key).add(sentenca);
            }
        }
    }

    /**
     * Metódo resposável por obter as sentenças processadas, organizar em uma string padronizada.
     *
     * @return {@link String} uma string com as sentenças à serem exibida na interface.
     */
    public String mostrarSentencas() {
        StringBuilder sb = new StringBuilder();
        for (String chave : this.hp.keySet()) {
            sb.append("Versos terminados com: ").append(chave).append("\n");
            List<Sentenca> tempSentenca = this.hp.get(chave);
            for (Sentenca sentenca : tempSentenca) {
                for (EstruturaVersificacao ev : sentenca.getEstruturaDeVesificacao()) {
                    sb.append("Escansão: ").append(ev.getSentecaEscandida()).append("\n")
                            .append("Metrica: ").append(ev.getMetrica()).append("\n")
                            .append("Número de Silabas: ").append(ev.getNumeroDeSilabas()).append("\n")
                            .append("Posição das Tônicas: ").append(ev.getPosicaoDasTonicas()).append("\n");
                }
            }
        }

        return sb.toString();
    }

    /**
     * Este método realiza a contagem de quantas sentenças contem a mesma silaba. Utiliza um HasMap<String, Integer>
     * A string é a silaba e o integer é a quantidade de vezes que ela foi encontrada. A contagem leva em consideração a
     * métrica passada por parâmetro.
     *
     * @param metrica {@link Metrica} silabas que tiverem a metrica informada por par ametro.
     * @return hasmap com as silabas e a sua frequência.
     */
    public Map<String, Integer> getSentencasSilabas(Metrica metrica) {
        Map<String, Integer> silabas = new HashMap<>();

        this.hp.entrySet().parallelStream()
                .filter(item -> (!(item.getKey().matches("\\d*\\W*"))))
                .forEach(item -> {
                    int size;
                    if (metrica == Metrica.TODAS)
                        size = item.getValue().size();
                    else
                        size = calculaQuatidadeMetrica(metrica, item.getValue());

                    if (size > 1)
                        silabas.put(item.getKey(), size);
                });
        System.out.println("\n-------------------------------------------------------------------");
        System.out.println("Frêquencia de sentenças");
        silabas.forEach((s, integer) -> System.out.println(s + "\t" + integer));
        System.out.println("-------------------------------------------------------------------\n");
        System.out.println("Frêquencia de sentenças Bruta");
        System.out.println("-------------------------------------------------------------------\n");
        this.hp.forEach((silaba, sentencas) -> sentencas.forEach(sentenca -> {
            sentenca.getEstruturaDeVesificacao().forEach(verso -> {
                System.out.println(silaba + "\t" + sentenca.getSegmento() + "\t" + verso.getMetrica() + "\t" + verso.getSentecaEscandida());
            });
        }));
        System.out.println("-------------------------------------------------------------------\n");

        return silabas;
    }

    /**
     * Percorre o hasmap hp e faz a contagem de quantas estruturas de versificação tem a mesma posição de tônica. A contagem
     * leva em consideração a métrica passada por parâmetro.
     * Preenche o HasMap padroesRitimicos com o rítimo e a quantidade encontrada.
     * Após realizar a contagem, transforma o HasMap em uma Lista com {@link UnidadeDeVersificacao} e calcula a similaridade
     * do verso.
     *
     * @param metrica {@link Metrica} silabas que tiverem a metrica informada por parametro.
     * @return {@link List<UnidadeDeVersificacao>} lista com as unidades de versificação prontas.
     */
    public List<UnidadeDeVersificacao> getPadroesRitmicos(Metrica metrica) {
        Map<String, Integer> padroesRitmicos = new HashMap<>();

        //percorre o hashmap e conta quantas estruturas de versificação tem a mesma posição das tonicas
        this.hp.entrySet().stream()
                .filter(item -> (!(item.getKey().matches("\\d*\\W*"))))
                .filter(item -> item.getValue().size() > 1)
                .forEach(item -> item.getValue()
                        .forEach(sentenca -> sentenca.getEstruturaDeVesificacao()
                                .forEach(estrutura -> {
                                    if (metrica == Metrica.TODAS || estrutura.getMetrica() == metrica)
                                        padroesRitmicos.merge(estrutura.getPosicaoDasTonicas(), 1, Integer::sum);
                                })));

        System.out.println("------------------Padrões Ritmicos de Sentenças------------------");
        System.out.println("Ritmo \t" + "Quantidade \t" + "Similaridade \n");
        //Cria uma lista com as unidades de versificações para calcular ordenar e calcular a similiaridade
        List<UnidadeDeVersificacao> unidadesDeVersificacoes = new ArrayList<>();
        if (!padroesRitmicos.isEmpty()) {
            padroesRitmicos.forEach((padrao, quantidade) -> unidadesDeVersificacoes.add(new UnidadeDeVersificacao(padrao, quantidade)));
            //Ordena as unidades pela quantidade
            unidadesDeVersificacoes.sort((o1, o2) -> Integer.compare(o2.getQuantidade(), o1.getQuantidade()));
            //Calcula a similidade das unidades
            String similar = unidadesDeVersificacoes.get(0).getPadrao();
            unidadesDeVersificacoes
                    .stream()
                    .peek((UnidadeDeVersificacao uv) -> uv
                            .setSimilaridade(VersoBuilder.calcularsimilaridadeDoPadrao(similar, uv.getPadrao())))
                    .forEach((UnidadeDeVersificacao uv) -> System.out.println(uv.getPadrao() + "\t\t" + uv.getQuantidade() + "\t\t\t" + uv.getSimilaridade()));

            System.out.println("------------------Padrões Ritmicos de Sentenças------------------");
        }
        return unidadesDeVersificacoes;
    }

    /**
     * Método que obtem uma lista de silabas disponíveis atraves da analise de silabas do hasmap.
     *
     * @param numEstrofes      quantidade de estrofes escolhida pelo usuário.
     * @param numVersos        quantidade de versos escolhido pelo usuáro.
     * @param metrica          {@link Metrica} silabas que tiverem a metrica informada por par
     *                         ametro.
     * @param usaQuatroSilabas se o usuário escolheu 2 silabas ou 4 silabas para gerar o poema
     * @return {@link List<String>} uma lista contendo as silabas de satisfazem os parametros
     */
    public List<String> getSilabasDisponiveis(int numEstrofes, int numVersos, Metrica metrica, boolean usaQuatroSilabas) {
        int qntSilabas = usaQuatroSilabas ? 4 : 2;
        int quantidadeVersos = ((numEstrofes * numVersos) + 1) / qntSilabas;

        Set<String> silabas2 = new HashSet<>();

        this.hp.entrySet().stream()
                .filter(item -> (!(item.getKey().matches("\\d*\\W*"))))
                .filter(item -> item.getValue().size() > 1)
                .forEach(item -> {
                    String silaba = item.getKey();
                    if (tonicaMetrica(silaba, quantidadeVersos, metrica))
                        silabas2.add(silaba);
                });
        List<String> silabas = new ArrayList<>(silabas2);
        //Só adiciona a opção aleatória, caso exista mais de uma silaba disponível
        if (silabas.size() > 1)
            silabas.add(0, "Aleatória");
        return silabas;
    }

    /**
     * Escolhe uma silaba de uma lista passada por parametro. Caso a silaba informada no parametro selected for Aleatória
     * será sorteado uma silaba entre as da lista. Caso não seja aleatório, retona o proprio parametro selected.
     *
     * @param selected {@link String} com a silaba selecionada.
     * @param silabas  {@link ObservableList<String>} lista contendo as silabas a serem utilizadas
     * @return String a silaba escolhida.
     */
    public String escolherSilaba(ObservableList<String> silabas, String selected) {
        if (selected == null)
            return "";

        if (selected.equals("Aleatória")) {

            //TODO: Nova implementação para previnir um infinity lopping, quando existe somente 1 item na lista
            if (silabas.size() <= 2) {
                return silabas.get(1);
            }

            Random sorteio = new Random();
            int randonNum = 0;

            while (randonNum == 0) {
                randonNum = sorteio.nextInt(silabas.size() - 1);
            }

            return silabas.get(randonNum);
        }

        //exibe as sentenças das silabas escolihdas na tela
        System.out.println(String.format("Silaba escolhida: %s", selected));
        this.hp.get(selected).stream().forEach(s -> System.out.println(s.getSegmento()));
        System.out.println("\n\n\n");

        return selected;
    }

    /**
     * Obtem as metricas contidas nas sentenças
     */
    public List<Metrica> obterMetricas() {
        Set<Metrica> metricas = new HashSet<>();
        this.hp.forEach((key, sentencas) -> sentencas.forEach(sentenca -> sentenca.getEstruturaDeVesificacao()
                .forEach(ev -> metricas.add(ev.getMetrica()))));
        List<Metrica> metricasList = new ArrayList<>(metricas);
        metricasList.sort(Comparator.comparingInt(Metrica::getId));
        metricasList.add(0, Metrica.TODAS);
        return metricasList;
    }

    public List<Rima> obterRimas() {
        return Rima.getAll();
    }

    private int calculaQuatidadeMetrica(Metrica metrica, List<Sentenca> sentencas) {
        int quantidade;

        if (metrica == Metrica.TODAS) {
            quantidade = sentencas.size();
        } else {
            quantidade = (int) sentencas.parallelStream()
                    .filter(sentenca -> sentenca.getEstruturaDeVesificacao().stream()
                            .anyMatch(versificacao -> versificacao.getMetrica() == metrica))
                    .count();
        }
        return quantidade;
    }

    private boolean tonicaMetrica(String silaba, int quantidade, Metrica metrica) {
        List<Sentenca> sentencas = this.hp.get(silaba);
        int qtd = calculaQuatidadeMetrica(metrica, sentencas);
        return qtd >= quantidade;
    }
}