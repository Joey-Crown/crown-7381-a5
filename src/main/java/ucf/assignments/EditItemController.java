package ucf.assignments;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;


public class EditItemController {
    @FXML
    private Label serialNumberLabel;
    @FXML
    private Label priceLabel;

    // text fields for changing Item data
    @FXML
    private TextField editSerialNumberField;
    @FXML
    private TextField editNameField;
    @FXML
    private TextField editPriceField;

    // button elements
    @FXML
    private Button saveEditButton;
    @FXML
    private Button cancelButton;

    private ItemModel itemModel;
    private SceneManager sceneManager;


    public EditItemController(ItemModel itemModel, SceneManager sceneManager) {
        this.itemModel = itemModel;
        this.sceneManager = sceneManager;
    }

    // uses the currently selected item to populate textfields with the already stored data
    public void setTextFieldData(Item item) {

        editSerialNumberField.setText(item.getSerialNumber());
        editNameField.setText(item.getName());
        editPriceField.setText(String.format("%.2f", item.getValue()));
    }

    public void initialize() {

        // listens for changes made to the text in the text field for serial number
        // gives responsive feedback to user of whether the text currently in the field is a valid input
        editSerialNumberField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!Item.verifySerialNumberFormat(editSerialNumberField.getText())) {
                    serialNumberLabel.setText("Not a valid Serial Number");
                    serialNumberLabel.setStyle("-fx-text-fill: red");
                } else if (!itemModel.checkUniqueSerial(editSerialNumberField.getText())) {
                    serialNumberLabel.setText("Duplicate Serial Number");
                    serialNumberLabel.setStyle("-fx-text-fill: red");
                } else {
                    serialNumberLabel.setText("Serial Number");
                    serialNumberLabel.setStyle("-fx-text-fill: black");
                }
            }
        });

        // listens for changes made to the text in the text field for name
        // checks if name is valid and if not displays to user the input is not valid
        editNameField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!Item.verifyNameFormat(editNameField.getText())) {
                    serialNumberLabel.setText("Not a valid Name");
                    serialNumberLabel.setStyle("-fx-text-fill: red");
                } else {
                    serialNumberLabel.setText("Name");
                    serialNumberLabel.setStyle("-fx-text-fill: black");
                }
            }
        });

        // listens for change in price text field
        // warns user when invalid price is entered
        editPriceField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!Item.verifyValueFormat(editPriceField.getText())) {
                    priceLabel.setText("Not a valid Price");
                    priceLabel.setStyle("-fx-text-fill: red");
                } else {
                    priceLabel.setText("Price");
                    priceLabel.setStyle("-fx-text-fill: black");
                }
            }
        });
    }

    // clicking the "Save Edit" button takes the information currently stored in the text fields
    // parses the "price" for a double value
    // if all values are valid inputs:
    // uses the currentlySelected ItemModel property and new input data to call editSelectedItem in class ItemModel
    // replaces currently selected item with item populated with new data
    public void onSaveEditButtonClick(ActionEvent actionEvent) {
        String newSerialNumber = editSerialNumberField.getText();
        String newName = editNameField.getText();
        double newPrice;
        try {
            newPrice = Double.parseDouble(editPriceField.getText());
        } catch (NumberFormatException e) {
            return;
        }
        if (Item.verifySerialNumberFormat(newSerialNumber) && itemModel.checkUniqueSerial(newSerialNumber)) {
            Item newItem = new Item(newSerialNumber, newName, newPrice);
            itemModel.editSelectedItem(newItem);
        } else {
            return;
        }

        saveEditButton.getScene().getWindow().hide();
    }

    // clicking the cancel button hides the window and doesn't make any changes to the selected item
    public void onCloseButtonClick(ActionEvent actionEvent) {
        cancelButton.getScene().getWindow().hide();
    }


}
