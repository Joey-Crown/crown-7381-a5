package ucf.assignments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ItemModel {

    ObservableList<Item> inventory;

    public ItemModel() {
        this.inventory = FXCollections.observableArrayList();
    }
}
