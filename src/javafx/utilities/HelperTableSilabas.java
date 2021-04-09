package javafx.utilities;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Arrays;

public class HelperTableSilabas {

    private TableView<ModelFrequencia> tableViewSilabas;

    private ObservableList<ModelFrequencia> data = null;

    public HelperTableSilabas(TableView<ModelFrequencia> tableViewSilabas) {
        this.tableViewSilabas = tableViewSilabas;
    }

    public void definirTabela() {
        tableViewSilabas.getColumns().remove(0);
        tableViewSilabas.getColumns().remove(0);

        data = FXCollections.observableArrayList();

        TableColumn<ModelFrequencia, String> silaba = new TableColumn<>("Sílaba");
        silaba.setMinWidth(100);
        silaba.setCellValueFactory(new PropertyValueFactory<>("chave"));

        TableColumn<ModelFrequencia, String> quantidade = new TableColumn<>("Quantidade");
        quantidade.setMinWidth(100);
        quantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        tableViewSilabas.setItems(data);
        tableViewSilabas.getColumns().addAll(Arrays.asList(silaba, quantidade));

    }

    public void adicionarItem(String chave, Integer valor) {
        data.add(new ModelFrequencia(chave, valor));
    }

    public void removerTodos(){
        if(data != null){
            data.clear();
        }
    }

    public void setTableViewSilabas(TableView<ModelFrequencia> tableViewSilabas) {
        this.tableViewSilabas = tableViewSilabas;
    }

    public static class ModelFrequencia {

        private final SimpleStringProperty chave;
        private final SimpleIntegerProperty quantidade;

        private ModelFrequencia(String chave, Integer quantidade) {
            this.chave = new SimpleStringProperty(chave);
            this.quantidade = new SimpleIntegerProperty(quantidade);
        }

        public String getChave() {
            return chave.get();
        }

        public Integer getQuantidade() {
            return quantidade.get();
        }

        public void setChave(String chave) {
            this.chave.set(chave);
        }

        public void setQuantidade(Integer chave) {
            this.quantidade.set(chave);
        }

    }
}
