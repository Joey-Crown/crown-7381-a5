package ucf.assignments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileManagerTest {

    //CLEAR SAVED FILES FROM RESOURCE FOLDER BEFORE TESTING
    // The save functions will not overwrite existing files.
    // delete "testFileSave.txt", "testHTML.html", "testJson.json", and "testTSV.tsv"
    // in the resource folder after each test.
    // files with "LOAD" in their name are for testing loading, do not delete these.

    FileManager fileManager = new FileManager();

    @Test
    void saveAsTSV() {
        ItemModel itemModelTest = new ItemModel();
        itemModelTest.inventory = createTestList();
        String goodFilePath = "src/test/resources/ucf/assignments/testTSV.tsv";
        String badFilePath = "not/a/real/path";
        Assertions.assertFalse(fileManager.saveAsTSV(itemModelTest, badFilePath));
        Assertions.assertTrue(fileManager.saveAsTSV(itemModelTest, goodFilePath));
    }

    @Test
    void saveAsHTML() {
        ItemModel itemModelTest = new ItemModel();
        itemModelTest.inventory = createTestList();
        String goodFilePath = "src/test/resources/ucf/assignments/testHTML.html";
        String badFilePath = "not/a/real/path";
        Assertions.assertFalse(fileManager.saveAsHTML(itemModelTest, badFilePath));
        Assertions.assertTrue(fileManager.saveAsHTML(itemModelTest, goodFilePath));
    }

    @Test
    void saveAsJson() {
        ItemModel itemModelTest = new ItemModel();
        itemModelTest.inventory = createTestList();
        String goodFilePath = "src/test/resources/ucf/assignments/testJson.json";
        String badFilePath = "not/a/real/path";
        Assertions.assertFalse(fileManager.saveAsJson(itemModelTest, badFilePath));
        Assertions.assertTrue(fileManager.saveAsJson(itemModelTest, goodFilePath));
    }

    @Test
    void saveToFile() {
        String badFilePath = "not/a/real/path";
        String goodFilePath = "src/test/resources/ucf/assignments/testFileSave.txt";
        String data = "Testing saving text to file";
        Assertions.assertFalse(fileManager.saveToFile(badFilePath, data));
        Assertions.assertTrue(fileManager.saveToFile(goodFilePath, data));
    }


    @Test
    void loadTSVFile() {
        ItemModel itemModelTest = new ItemModel();
        itemModelTest.inventory = createTestList();
        String fileName = "src/test/resources/ucf/assignments/LOADtestTSV.tsv";
        ItemModel loadedItemModel = fileManager.loadTSVFile(fileName);

        // loop through all items in the inventory and compare with loaded list
        for (int i = 0; i < itemModelTest.inventory.size(); i++) {
            Assertions.assertEquals(itemModelTest.inventory.get(i).getName(), loadedItemModel.inventory.get(i).getName());
            Assertions.assertEquals(itemModelTest.inventory.get(i).getSerialNumber(), loadedItemModel.inventory.get(i).getSerialNumber());
            Assertions.assertEquals(itemModelTest.inventory.get(i).getValue(), loadedItemModel.inventory.get(i).getValue());
        }

    }

    @Test
    void loadHTMLFile() {
        ItemModel itemModelTest = new ItemModel();
        itemModelTest.inventory = createTestList();
        String fileName = "src/test/resources/ucf/assignments/LOADtestHTML.html";
        ItemModel loadedItemModel = fileManager.loadHTMLFile(fileName);

        // loop through all items in the inventory and compare with loaded list
        for (int i = 0; i < itemModelTest.inventory.size(); i++) {
            Assertions.assertEquals(itemModelTest.inventory.get(i).getName(), loadedItemModel.inventory.get(i).getName());
            Assertions.assertEquals(itemModelTest.inventory.get(i).getSerialNumber(), loadedItemModel.inventory.get(i).getSerialNumber());
            Assertions.assertEquals(itemModelTest.inventory.get(i).getValue(), loadedItemModel.inventory.get(i).getValue());
        }
    }

    @Test
    void loadJSONFile() {
        ItemModel itemModelTest = new ItemModel();
        itemModelTest.inventory = createTestList();
        String fileName = "src/test/resources/ucf/assignments/LOADtestJson.json";
        ItemModel loadedItemModel = fileManager.loadJSONFile(fileName);

        // loop through all items in the inventory and compare with loaded list
        for (int i = 0; i < itemModelTest.inventory.size(); i++) {
            Assertions.assertEquals(itemModelTest.inventory.get(i).getName(), loadedItemModel.inventory.get(i).getName());
            Assertions.assertEquals(itemModelTest.inventory.get(i).getSerialNumber(), loadedItemModel.inventory.get(i).getSerialNumber());
            Assertions.assertEquals(itemModelTest.inventory.get(i).getValue(), loadedItemModel.inventory.get(i).getValue());
        }
    }

    private ObservableList<Item> createTestList() {

        List<Item> testList = new ArrayList<Item>();
        testList.add(new Item("XBOX123456", "Xbox Series S", 299.99));
        testList.add(new Item("PS51234567", "Playstation 5", 499.99));
        testList.add(new Item("SWITCH1234", "Nintendo Switch", 299.99));
        testList.add(new Item("PS49712738", "Playstation 4", 299.99));
        testList.add(new Item("3DS7563421", "Nintendo 3DS", 99.99));
        testList.add(new Item("XSX7615342", "Xbox Series X", 499.99));

        return FXCollections.observableArrayList(testList);
    }
}