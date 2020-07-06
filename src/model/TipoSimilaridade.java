package model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum TipoSimilaridade {
    ESQUEMA_RITMICO("Esquema Rítmico"),
    POSICIONAMENTO_TONICA("Posicionamento de Tônica");

    private final String descricao;

    TipoSimilaridade(String descricao) {
        this.descricao = descricao;
    }

    public String getDescription() {
        return this.descricao;
    }

    public static List<TipoSimilaridade> getAll() {
        return Arrays.stream(TipoSimilaridade.values()).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return this.descricao;
    }
}
