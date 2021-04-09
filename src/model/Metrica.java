package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public enum Metrica {
    TODAS(0, "Todas as Metricas"),
    MONOSSILABO(1, "Monossilabo"),
    DISSILABO(2, "Dissílabo"),
    TRISSILABO(3, "Trissílabo"),
    TETRASSILABO(4, "Tetrassílabo"),
    PENTASSILABO(5, "Pentassílabo"),
    HEXASSILABO(6, "Hexassílabo"),
    HEPTASSILABO(7, "Heptassílabo"),
    OCTOSSILABO(8, "Octossílabo"),
    ENEASSILABO(9, "Eneassílabo"),
    DECASSILABO(10, "Decassílabo"),
    HENDECASSILABO(11, "Hendecassílabo"),
    DODECASSILABO(12, "Dodecassílabo"),
    BARBARO(13, "Barbáro");

    private Integer id;
    private String descricao;

    Metrica(Integer id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return this.descricao;
    }

    public static Metrica fromId(Integer id) {

        if (id >= 13)
            return Metrica.BARBARO;

        return Metrica.values()[id];
    }

    public static List<Metrica> getAll() {
        List<Metrica> types = new ArrayList<>();
        Collections.addAll(types, Metrica.values());

        return types;
    }

    @Override
    public String toString() {
        return this.descricao;
    }
}
