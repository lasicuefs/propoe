package model;

import javafx.utilities.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Rima {
    MISTA(0, "Mista"),
    CRUZADA(1, "Cruzada"),
    EMPARELHADA(2, "Emparelhada"),
    INTERPOLADA(3, "Interpolada");

    private final Integer id;
    private final String descricao;

    Rima(Integer id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return this.descricao;
    }
    
    public static Rima getRandom() {
        List<Rima> rimas = getAllSemMista();
        return rimas.get(Utils.random().nextInt(rimas.size()));
    }

    private static List<Rima> getAllSemMista() {
        List<Rima> rimas = new ArrayList<>();
        for (Rima r : Rima.values())
            if (r != Rima.MISTA) rimas.add(r);
        return rimas;
    }

    public static List<Rima> getAll() {
        return Arrays.asList(Rima.values());
    }

    @Override
    public String toString() {
        return this.descricao;
    }
}
