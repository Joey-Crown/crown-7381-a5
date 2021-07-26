package ucf.assignments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.lang.reflect.Array;
import java.util.HashSet;
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

    /*
        setters and getters
     */

    public void setCurrentlySelected(Item item) {
        this.currentlySelected = item;
    }

    public Item getCurrentlySelected() {
        return this.currentlySelected;
    }

    // loops through items in inventory
    // checks if the provided serial number matches any others in the inventory
    boolean checkUniqueSerial(String serialNumber) {

        for (Item item : this.inventory) {

            if (item.getSerialNumber().equals(serialNumber)) {
                return false;
            }

        }
        return true;
    }

    // replaces currently selected item with provided item
    void editSelectedItem(Item newItem) {
        int index = this.inventory.indexOf(this.currentlySelected);
        this.inventory.set(index, newItem);
    }

    // removes currently selected item from the inventory
    void deleteSelectedItem() {
        this.inventory.remove(currentlySelected);
    }

    // takes the item property, the searched string, the ItemModel and finds matching item
    ObservableList<Item> searchInventory(String property, String contains, ItemModel itemModel) {
        ObservableList<Item> searchedList = FXCollections.observableArrayList();

        // adds items to searched list based on search property and if property value starts with String 'contain'
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
        // return new list
        return searchedList;
    }


}
