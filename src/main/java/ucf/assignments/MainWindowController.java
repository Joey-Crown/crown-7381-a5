package ucf.assignments;

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

import java.net.URL;
import java.util.Optional;
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
    @FXML
    private MenuItem loadFile;
    @FXML
    private MenuItem quitApplication;

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

        itemModel.inventory.addAll(new Item("XBOX123456", "Xbox Series S", 299.99),
                new Item("PS51234567", "Playstation 5", 499.99),
                new Item("SWITCH1234", "Nintendo Switch", 299.99),
                new Item("PS49712738", "Playstation 4", 299.99),
                new Item("3DS7563421", "Nintendo 3DS", 99.99),
                new Item("XSX7615342", "Xbox Series X", 499.99));
        displayList(itemModel.inventory);

        itemsTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // listens for change in selection on tableview, sets selected item to itemModel.currentlySelected
        itemsTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Item>() {
            @Override
            public void changed(ObservableValue<? extends Item> observable, Item oldValue, Item newValue) {
                if (!itemsTableView.getSelectionModel().isEmpty()) {
                    itemModel.setCurrentlySelected(newValue);
                }
            }
        });

        searchOptions.setItems(FXCollections.observableArrayList("Serial Number", "Name", "Price"));
        searchOptions.setValue("Serial Number");

        // listens for change in the text of the search bar, then searches for items and displays them
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

    // takes the inventory property from itemModel and displays items
    public void displayList(ObservableList<Item> inventory) {
        itemsTableView.setItems(inventory);

        itemsSerialNumberColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("serialNumber"));
        itemsNameColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        itemsValueColumn.setCellValueFactory(new PropertyValueFactory<Item, Double>("value"));

        // format value property as a price (e.g. "199.99")
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

    // when "Add Item" button is clicked, data from text fields is processed and added to the inventory
    public void onAddItemClick (ActionEvent actionEvent) {

        // parse data from text fields
        String serialNumber = serialNumberField.getText();
        String name = itemNameField.getText();
        double value;
        try {
            value = Float.parseFloat(itemPriceField.getText());
        } catch (NumberFormatException e) {
            showAlertWindow("Error Adding Item", "Price must be a number with no more than two decimals.");
            return;
        }

        // verify that values from text fields meet inventory item requirements
        // initialize new Item object with parsed data
        // add item to inventory
        if (Item.verifySerialNumberFormat(serialNumber) && Item.verifyNameFormat(name) &&
                Item.verifyValueFormat(itemPriceField.getText()) && itemModel.checkUniqueSerial(serialNumber)) {
            Item newItem = new Item(name, serialNumber, value);
            itemModel.inventory.add(newItem);
        } else {

            // alerts will be displayed for each invalid input detailing what the requiements are

            if (!Item.verifySerialNumberFormat(serialNumber)) {
                showAlertWindow("Error Adding Item", "Check serial number format (\"XXXXXXXXXX\").");
            }

            if (!Item.verifyNameFormat(name)) {
                showAlertWindow("Error Adding Item", "Name must be between 2-256 characters long.");
            }

            if (!Item.verifyValueFormat(itemPriceField.getText())) {
                showAlertWindow("Error Adding Item", "Price must be a dollar value (e.g. \"4.00\", \"4\").");
            }

            if (!itemModel.checkUniqueSerial(serialNumber)) {
                showAlertWindow("Error Adding Item", "Serial Number already exists in inventory.");
            }
        }

    }

    // when "Edit Item" is clicked, an edit item window is initialized and populated with the info of the selected item
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

    // when "Save As" menu item is clicked, opens a file chooser
    // takes file name form user input and the file format chosen
    // saves inventory in that file format
    public void onSaveAsClick(ActionEvent actionEvent) {
        FileChooser saveFileChooser = new FileChooser();
        saveFileChooser.setTitle("Save File As...");

        saveFileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TSV (*.TSV)", "*.tsv"),
                new FileChooser.ExtensionFilter("HTML (*.HTML)", "*.html"),
                new FileChooser.ExtensionFilter("JSON (*.JSON)", "*.json"));

        String fileName;

        // get file name and directory from user
        try {
            fileName = saveFileChooser.showSaveDialog(new Stage()).getAbsolutePath();
        } catch (NullPointerException e) {
            return;
        }

        // format data to CSV and save
        FileManager fileManager = new FileManager();
        if (fileName.endsWith(".tsv")) {
            if (fileManager.saveAsTSV(itemModel, fileName)) {
              System.out.println("File Saved Successfully");
          } else {
              showAlertWindow("File Error", "Error saving file.");
          }

        // format data to HTML and save
        } else if (fileName.endsWith(".html")) {
            if (fileManager.saveAsHTML(itemModel, fileName)) {
                System.out.println("File Saved Successfully");
            } else {
                showAlertWindow("File Error", "Error saving file.");
            }

        // format data to JSON and save
        } else if (fileName.endsWith(".json")) {

            if (fileManager.saveAsJson(itemModel, fileName)) {
                System.out.println("File Saved Successfully");
            } else {
                showAlertWindow("File Error", "Error saving file.");
            }
        }
    }

    // when "Load" menu item is clicked, provides user with file chooser
    // takes user selected file and loads file based on its extension
    public void onLoadInventoryClick(ActionEvent actionEvent) {
        FileChooser loadFileChooser = new FileChooser();
        loadFileChooser.setTitle("Choose File to Load");

        loadFileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TSV (*.TSV)", "*.tsv"),
                new FileChooser.ExtensionFilter("HTML (*.HTML)", "*.html"),
                new FileChooser.ExtensionFilter("JSON (*.JSON)", "*.json"));

        String fileName;

        // get filename from user through file chooser
        try {
            fileName = loadFileChooser.showOpenDialog(new Stage()).getAbsolutePath();
        } catch (NullPointerException e) {
            return;
        }

        // check file extension and call the associated load method
        FileManager loadFile = new FileManager();
        if (fileName.endsWith(".tsv")) {
            itemModel = loadFile.loadTSVFile(fileName);
        } else if (fileName.endsWith(".html")) {
            itemModel = loadFile.loadHTMLFile(fileName);
        } else if (fileName.endsWith(".json")) {
            itemModel = loadFile.loadJSONFile(fileName);
        }

        // display list
        displayList(itemModel.inventory);
    }

    // when "Quit" menu item is clicked, provides a confirmation dialogue
    // if user clicks "Yes", application closes, if "Cancel" user returns to application.
    public void onQuitApplicationClick() {
        Alert closeAlert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to quit the application?",
                ButtonType.YES,
                ButtonType.CANCEL);
        closeAlert.setTitle("Quit");
       Optional<ButtonType> confirm = closeAlert.showAndWait();
        if (confirm.get() == ButtonType.YES) {
            Stage closeStage = (Stage) sceneManager.getScene("MainWindow").getWindow();
            closeStage.close();
        }
    }

    // takes currently selected item and removes it from the inventory
    public void onDeleteItemClick(ActionEvent actionEvent) {
        itemModel.deleteSelectedItem();
    }

    // method to set the title and text of an alert window
    // displays alert to user
    private void showAlertWindow(String title, String text) {
        notValidInput.setTitle(title);
        notValidInput.setContentText(text);
        notValidInput.showAndWait();
    }
}
