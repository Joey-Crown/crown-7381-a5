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

    @FXML
    private TextField editSerialNumberField;

    @FXML
    private TextField editNameField;

    @FXML
    private TextField editPriceField;

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

    public void setTextFieldData(Item item) {

        editSerialNumberField.setText(item.getSerialNumber());
        editNameField.setText(item.getName());
        editPriceField.setText(String.format("%.2f", item.getValue()));
    }

    public void initialize() {

        editSerialNumberField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!Item.verifySerialNumberFormat(editSerialNumberField.getText())) {
                    serialNumberLabel.setText("Not a valid Serial Number");
                    serialNumberLabel.setStyle("-fx-text-fill: red");
                } else {
                    serialNumberLabel.setText("Serial Number");
                    serialNumberLabel.setStyle("-fx-text-fill: black");
                }
            }
        });

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

    public void onSaveEditButtonClick(ActionEvent actionEvent) {
        String newSerialNumber = editSerialNumberField.getText();
        String newName = editNameField.getText();
        double newPrice;
        try {
            newPrice = Double.parseDouble(editPriceField.getText());
        } catch (NumberFormatException e) {
            return;
        }
        if (Item.verifySerialNumberFormat(newSerialNumber)) {
            Item newItem = new Item(newName, newSerialNumber, newPrice);
            itemModel.editSelectedItem(newItem);
        } else {
            return;
        }

        saveEditButton.getScene().getWindow().hide();
    }

    public void onCloseButtonClick(ActionEvent actionEvent) {
        cancelButton.getScene().getWindow().hide();
    }


}
