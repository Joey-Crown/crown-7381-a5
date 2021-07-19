package ucf.assignments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.lang.reflect.Array;
import java.util.List;

public class ItemModel {

    ObservableList<Item> inventory;

    public ItemModel() {
        this.inventory = FXCollections.observableArrayList();
    }

    public ItemModel(List<Item> itemList) {
        this.inventory = FXCollections.observableArrayList(itemList);
    }
}
