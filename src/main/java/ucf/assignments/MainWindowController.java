package ucf.assignments;

import com.google.gson.Gson;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainWindowController {

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

    private final SceneManager sceneManager;
    public static ItemModel itemModel;
    private FileManager fileManager;
    private Serializer serializer;

    public MainWindowController(ItemModel itemModel, SceneManager sceneManager) {
        this.itemModel = itemModel;
        this.sceneManager = sceneManager;
    }

    public Dialog notValidInput = new Dialog();

    public void initialize() {
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

    public void onAddItemClick () {
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

    public void onEditItemClick() {
        Item oldItem = itemsTableView.getSelectionModel().getSelectedItem();
        Stage stage = new Stage();
        stage.setTitle("Add New Item");
        stage.setScene(sceneManager.getScene("EditItem"));
        stage.show();
    }

    public void onSaveAsClick() {
        FileChooser saveFileChooser = new FileChooser();
        saveFileChooser.setTitle("Save File As...");

        saveFileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV", "*.csv"),
                new FileChooser.ExtensionFilter("HTML", "*.html"),
                new FileChooser.ExtensionFilter("JSON", "*.json"));

        String fileName = saveFileChooser.showSaveDialog(new Stage()).getAbsolutePath();
        String data = "";

        //TODO Consider doing data processing inside the file manager class

        // format data to CSV
        if (fileName.endsWith(".csv")) {
            data = "Serial Number, Name, Price";
            for (Item item : itemModel.inventory) {
                data += item.getSerialNumber() + "," + item.getName() + "," + String.valueOf(item.getValue()) + "\n";
            }
          if (fileManager.saveAsCSV(fileName, data)) {
              System.out.println("File Saved Successfully");
          } else {
              System.out.println("Problem Saving File");
          }
        // format data to HTML
        } else if (fileName.endsWith(".html")) {
            //TODO
            // Format data from Observable list to HTML
        // format data to JSON
        } else if (fileName.endsWith(".json")) {
            Gson gson = new Gson();
            List<Serializer> serializedList = new ArrayList<Serializer>();
            for (Item item: itemModel.inventory) {
                serializedList.add(serializer.serializeItem(item));
            }
            data = gson.toJson(serializedList);
            if (fileManager.saveAsJson(fileName, data)) {
                System.out.println("File Saved Successfully");
            } else {
                System.out.println("Problem Saving File");
            }

        }


    }
}
