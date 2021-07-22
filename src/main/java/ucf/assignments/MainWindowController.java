package ucf.assignments;

import com.google.gson.Gson;
import com.sun.tools.javac.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    @FXML
    private ResourceBundle resources;

    // menu bar elements
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu fileMenu;
    @FXML
    private MenuItem saveFile;

    // search bar elements
    @FXML
    private ChoiceBox<String> searchOptions;
    @FXML
    private TextField searchField;

    // Table elements
    @FXML
    private TableView<Item> itemsTableView;
    @FXML
    private TableColumn<Item, String> itemsSerialNumberColumn;
    @FXML
    private TableColumn<Item, String> itemsNameColumn;
    @FXML
    private TableColumn<Item, Double> itemsValueColumn;

    // context menu elements
    @FXML
    private ContextMenu tableContextMenu;
    @FXML
    private MenuItem contextMenuAddItem;
    @FXML
    private MenuItem contextMenuDeleteItem;
    @FXML
    private MenuItem contextMenuEditItem;

    // add item elements
    @FXML
    private TextField serialNumberField;
    @FXML
    private TextField itemNameField;
    @FXML
    private TextField itemPriceField;
    @FXML
    private Button addItemButton;

    // delete button element
    @FXML
    private Button deleteItemButton;

    // edit item element
    @FXML
    private Button editItemButton;

    public static ItemModel itemModel;
    private final SceneManager sceneManager;
    private EditItemController editItemController;

    public MainWindowController(ItemModel itemModel, SceneManager sceneManager, EditItemController editItemController) {
        this.itemModel = itemModel;
        this.sceneManager = sceneManager;
        this.editItemController = editItemController;
    }

    public Alert notValidInput = new Alert(Alert.AlertType.ERROR);

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        itemModel.inventory.addAll(new Item("Xbox Series S", "XBOX123456", 299.99),
                new Item("Playstation 5", "PS51234567", 499.99),
                new Item("Nintendo Switch", "SWITCH1234", 299.99),
                new Item("Playstation 4", "PS49712738", 299.99),
                new Item("Nintendo 3DS", "3DS7563421", 99.99),
                new Item("Xbox Series X", "XSX7615342", 499.99));
        displayList(itemModel.inventory);

        itemsTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        itemsTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Item>() {
            @Override
            public void changed(ObservableValue<? extends Item> observable, Item oldValue, Item newValue) {
                if (!itemsTableView.getSelectionModel().isEmpty()) {
                    itemModel.setCurrentlySelected(newValue);
                }
            }
        });

        //searchOptions = new ChoiceBox<String>(FXCollections.observableArrayList("Serial Number", "Name", "Price"));
        searchOptions.setItems(FXCollections.observableArrayList("Serial Number", "Name", "Price"));
        searchOptions.setValue("Serial Number");

        searchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    ObservableList<Item> searchedList = FXCollections.observableArrayList();
                    if (!searchField.getText().isEmpty()) {
                        searchedList = itemModel.searchInventory(searchOptions.getValue(), newValue, itemModel);
                        displayList(searchedList);
                    } else {
                        displayList(itemModel.inventory);
                    }
            }
        });
    }

    public void displayList(ObservableList<Item> inventory) {
        itemsTableView.setItems(inventory);

        itemsSerialNumberColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("serialNumber"));
        itemsNameColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        itemsValueColumn.setCellValueFactory(new PropertyValueFactory<Item, Double>("value"));
        itemsValueColumn.setCellFactory(tc -> new TableCell<Item, Double>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", value));
                }
            }

        });
    }

    public void onAddItemClick (ActionEvent actionEvent) {
        String serialNumber = serialNumberField.getText();
        String name = itemNameField.getText();
        double value;
        try {
            value = Float.parseFloat(itemPriceField.getText());
        } catch (NumberFormatException e) {
            showAlertWindow("Error Adding Item", "Price must be a number with no more than two decimals.");
            return;
        }

        if (Item.verifySerialNumberFormat(serialNumber) && value > 0) {
            Item newItem = new Item(name, serialNumber, value);
            itemModel.inventory.add(newItem);
        } else {
            showAlertWindow("Error Adding Item", "Check serial number format (\"XXXXXXXXXX\").");
        }

    }

    public void onEditItemClick(ActionEvent actionEvent) {
        if (itemModel.currentlySelected != null) {
            Stage stage = new Stage();
            stage.setTitle("Edit Item");
            Scene scene = sceneManager.getScene("EditWindow");
            editItemController.setTextFieldData(itemModel.currentlySelected);
            stage.setScene(scene);
            stage.show();
        } else {
            showAlertWindow("No selected Item", "Select an item to edit.");
        }
    }

    //TODO change CSV to TSV in saving and loading

    public void onSaveAsClick(ActionEvent actionEvent) {
        FileChooser saveFileChooser = new FileChooser();
        saveFileChooser.setTitle("Save File As...");

        saveFileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV", "*.csv"),
                new FileChooser.ExtensionFilter("HTML", "*.html"),
                new FileChooser.ExtensionFilter("JSON", "*.json"));

        String fileName = saveFileChooser.showSaveDialog(new Stage()).getAbsolutePath();
        String data = "";

        // format data to CSV and save
        FileManager fileManager = new FileManager();
        if (fileName.endsWith(".csv")) {
            if (fileManager.saveAsCSV(itemModel, fileName)) {
              System.out.println("File Saved Successfully");
          } else {
              System.out.println("Problem Saving File");
          }
        // format data to HTML and save
        } else if (fileName.endsWith(".html")) {
            if (fileManager.saveAsHTML(itemModel, fileName)) {
                System.out.println("File Saved Successfully");
            } else {
                System.out.println("Problem Saving File");
            }
        // format data to JSON and save
        } else if (fileName.endsWith(".json")) {

            if (fileManager.saveAsJson(itemModel, fileName)) {
                System.out.println("File Saved Successfully");
            } else {
                System.out.println("Problem Saving File");
            }
        }
    }

    public void onLoadInventoryClick(ActionEvent actionEvent) {
        FileChooser loadFileChooser = new FileChooser();
        loadFileChooser.setTitle("Choose File to Load");

        loadFileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV", "*.csv"),
                new FileChooser.ExtensionFilter("HTML", "*.html"),
                new FileChooser.ExtensionFilter("JSON", "*.json"));

        String fileName = loadFileChooser.showOpenDialog(new Stage()).getAbsolutePath();

        FileManager loadFile = new FileManager();
        if (fileName.endsWith(".csv")) {
            itemModel = loadFile.loadCSVFile(fileName);
        } else if (fileName.endsWith(".html")) {
            itemModel = loadFile.loadHTMLFile(fileName);
        } else if (fileName.endsWith(".json")) {
            itemModel = loadFile.loadJSONFile(fileName);
        }

        displayList(itemModel.inventory);
    }

    public void onDeleteItemClick(ActionEvent actionEvent) {
        itemModel.deleteSelectedItem();
    }

    private void showAlertWindow(String title, String text) {
        notValidInput.setTitle(title);
        notValidInput.setContentText(text);
        notValidInput.showAndWait();
    }
}
