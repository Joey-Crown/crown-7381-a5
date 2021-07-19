package ucf.assignments;

import com.google.gson.Gson;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    public static ItemModel itemModel = new ItemModel();
    private SceneManager sceneManager;

    public Dialog notValidInput = new Dialog();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO do the scenes have to be loaded through here?
        displayList();

        itemsTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    public void displayList() {
        itemsTableView.setItems(itemModel.inventory);

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
            notValidInput.setTitle("Invalid Input.");
            notValidInput.setContentText("Value field must be a number to 2 decimal places");
            notValidInput.show();
            return;
        }

        if (Item.verifySerialNumberFormat(serialNumber) && value > 0) {
            Item newItem = new Item(name, serialNumber, value);
            itemModel.inventory.add(newItem);
        }

    }

    public void onEditItemClick(ActionEvent actionEvent) {
        Item oldItem = itemsTableView.getSelectionModel().getSelectedItem();
        Stage stage = new Stage();
        stage.setTitle("Add New Item");
        stage.setScene(sceneManager.getScene("EditItem"));
        stage.show();
    }

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
            if (fileManager.saveAsCSV(itemModel, data, fileName)) {
              System.out.println("File Saved Successfully");
          } else {
              System.out.println("Problem Saving File");
          }
        // format data to HTML and save
        } else if (fileName.endsWith(".html")) {
            //TODO
            // Format data from Observable list to HTML
        // format data to JSON and save
        } else if (fileName.endsWith(".json")) {

            if (fileManager.saveAsJson(itemModel, data, fileName)) {
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

        displayList();
    }
}
