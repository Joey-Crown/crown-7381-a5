package ucf.assignments;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Item {

    public SimpleStringProperty name = new SimpleStringProperty();
    public SimpleStringProperty serialNumber = new SimpleStringProperty();
    public SimpleDoubleProperty value = new SimpleDoubleProperty();

    public Item(String name, String serialNumber, double value) {
        setName(name);
        setSerialNumber(serialNumber);
        setValue(value);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getSerialNumber() {
        return serialNumber.get();
    }

    public SimpleStringProperty serialNumberProperty() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber.set(serialNumber);
    }

    public double getValue() {
        return value.get();
    }

    public SimpleDoubleProperty valueProperty() {
        return value;
    }

    public void setValue(double value) {
        this.value.set(value);
    }

    public static boolean verifySerialNumberFormat(String serialNumber) {
        if (serialNumber.length() != 10) {
            System.out.println("Serial number must be 10 characters in length.");
            return false;
        }

        char[] serialNumberArray = serialNumber.toCharArray();

        for (int i = 0; i < serialNumberArray.length; i++) {
            if (!Character.isDigit(serialNumberArray[i]) || !Character.isLetter(serialNumberArray[i])) {
                System.out.println("Serial number in incorrect format.");
                return false;
            }
        }
        return true;
    }
}
