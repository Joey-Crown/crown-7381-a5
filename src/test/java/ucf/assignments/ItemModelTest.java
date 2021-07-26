/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Joseph Crown
 */
package ucf.assignments;

import javafx.collections.ObservableList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemModelTest {



    @Test
    void checkUniqueSerial() {
        ItemModel itemModelTesting = new ItemModel();
        itemModelTesting.inventory.add(new Item("PS51234567", "Playstation 5", 499.99));
        String duplicateSerialNumber = "PS51234567";
        String uniqueSerialNumber = "JOE3216789";
        Assertions.assertEquals(false, itemModelTesting.checkUniqueSerial(duplicateSerialNumber));
        Assertions.assertEquals(true, itemModelTesting.checkUniqueSerial(uniqueSerialNumber));
    }

    @Test
    void editSelectedItem() {
        ItemModel itemModelTesting = new ItemModel();
        itemModelTesting.inventory.add(new Item("PS51234567", "Playstation 5", 499.99));
        itemModelTesting.currentlySelected = itemModelTesting.inventory.get(0);
        Item newItem  = new Item("SWITCH1234", "Nintendo Switch", 299.99);
        itemModelTesting.editSelectedItem(newItem);
        Assertions.assertEquals(newItem, itemModelTesting.inventory.get(0));
    }

    @Test
    void deleteSelectedItem() {
        ItemModel itemModelTesting = new ItemModel();
        itemModelTesting.inventory.add(new Item("PS51234567", "Playstation 5", 499.99));
        itemModelTesting.currentlySelected = itemModelTesting.inventory.get(0);
        itemModelTesting.deleteSelectedItem();
        Assertions.assertEquals(0, itemModelTesting.inventory.size());
    }

    @Test
    void searchInventory() {
        ItemModel itemModelTesting = new ItemModel();
        itemModelTesting.inventory.add(new Item("PS51234567", "Playstation 5", 499.99));
        ObservableList<Item> newList = itemModelTesting.searchInventory("Serial Number", "PS5", itemModelTesting);
        Assertions.assertEquals("PS51234567", newList.get(0).getSerialNumber());
    }
}