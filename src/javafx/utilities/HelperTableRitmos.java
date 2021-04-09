package javafx.utilities;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Arrays;

public class HelperTableRitmos {

    private TableView<ModeloFrequencia> tableViewRitmo;

    private ObservableList<ModeloFrequencia> data = null;

    public HelperTableRitmos(TableView<ModeloFrequencia> tableViewRitmo) {
        this.tableViewRitmo = tableViewRitmo;
    }

    public void definirTabelaRitmo() {
        tableViewRitmo.getColumns().remove(0);
        tableViewRitmo.getColumns().remove(0);

        data = FXCollections.observableArrayList();

        TableColumn<ModeloFrequencia, String> ritmo = new TableColumn<>("Ritmo");
        ritmo.setMinWidth(100);
        ritmo.setCellValueFactory(new PropertyValueFactory<>("ritmo"));

        TableColumn<ModeloFrequencia, String> quantidade = new TableColumn<>("Quantidade");
        quantidade.setMinWidth(100);
        quantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        tableViewRitmo.setItems(data);
        tableViewRitmo.getColumns().addAll(Arrays.asList(ritmo, quantidade));

    }

    public void adicionarItens(String ritmo, Integer quantidade) {
        data.add(new ModeloFrequencia(ritmo, quantidade));
    }

    public void removerTodos() {
        if (data != null) {
            data.clear();
        }
    }

    public static class ModeloFrequencia {

        private final SimpleStringProperty ritmo;
        private final SimpleIntegerProperty quantidade;

        private ModeloFrequencia(String ritmo, Integer quantidade) {
            this.ritmo = new SimpleStringProperty(ritmo);
            this.quantidade = new SimpleIntegerProperty(quantidade);
        }

        public String getRitmo() {
            return ritmo.get();
        }

        public Integer getQuantidade() {
            return quantidade.get();
        }

        public void setRitmo(String ritmo) {
            this.ritmo.set(ritmo);
        }

        public void setQuantidade(Integer quantidade) {
            this.quantidade.set(quantidade);
        }

    }
}
