package ucf.assignments;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ResourceBundle;

public class MainWindowController {

    @FXML
    private ResourceBundle resources;

    // menu bar elements
    @FXML
    private MenuBar menuBar;

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

    public Dialog notValidInput = new Dialog();

    public static ItemModel storedInventory = new ItemModel();
    public static Item currentlySelected;

    public void initialize() {
        itemsTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        itemsTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Item>() {
            @Override
            public void changed(ObservableValue<? extends Item> observable, Item oldValue, Item newValue) {
                currentlySelected = newValue;
            }
        });
    }

    public void displayList() {
        itemsTableView.setItems(storedInventory.inventory);

        itemsSerialNumberColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("serialNumber"));
        itemsNameColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        itemsValueColumn.setCellValueFactory(new PropertyValueFactory<Item, Double>("value"));
        itemsValueColumn.setCellFactory(tc -> new TableCell<Item, Double>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty) {
                    setText("NO VALUE");
                } else {
                    setText(String.format("%.2f", value));
                }
            }

        });
    }

    public void onAddItemClick () {
        String serialNumber = serialNumberField.getText();
        String name = itemNameField.getText();
        double value= -1;
        try {
            value = Float.parseFloat(itemPriceField.getText());
        } catch (NumberFormatException e) {
            notValidInput.setTitle("Invalid Input.");
            notValidInput.setContentText("Value field must be a number to 2 decimal places");
            notValidInput.show();
            return;
        }

        if (Item.verifySerialNumberFormat(serialNumber) && value > 1) {
            Item newItem = new Item(name, serialNumber, value);
            storedInventory.inventory.add(newItem);
        }

    }


}
