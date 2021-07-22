package ucf.assignments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.lang.reflect.Array;
import java.util.List;

public class ItemModel {

    ObservableList<Item> inventory;
    Item currentlySelected;

    public ItemModel() {
        this.inventory = FXCollections.observableArrayList();
    }

    public ItemModel(List<Item> itemList) {
        this.inventory = FXCollections.observableArrayList(itemList);
    }

    public void setCurrentlySelected(Item item) {
        this.currentlySelected = item;
    }

    public Item getCurrentlySelected() {
        return this.currentlySelected;
    }

    void editSelectedItem(Item newItem) {
        int index = this.inventory.indexOf(this.currentlySelected);
        this.inventory.set(index, newItem);
    }

    void deleteSelectedItem() {
        this.inventory.remove(currentlySelected);
    }

    ObservableList<Item> searchInventory(String property, String contains, ItemModel itemModel) {
        ObservableList<Item> searchedList = FXCollections.observableArrayList();

        for (Item item: itemModel.inventory) {
            if (property.equals("Serial Number")) {
                if (item.getSerialNumber().startsWith(contains)) {
                    searchedList.add(item);
                }
            }
            if (property.equals("Name")) {
                if (item.getName().startsWith(contains)) {
                    searchedList.add(item);
                }
            }
            if (property.equals("Price")) {
                if (item.getValue() == Double.parseDouble(contains)) {
                    searchedList.add(item);
                }
            }
        }
        return searchedList;
    }


}
