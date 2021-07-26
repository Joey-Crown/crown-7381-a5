package ucf.assignments;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Item {

    public SimpleStringProperty serialNumber = new SimpleStringProperty();
    public SimpleStringProperty name = new SimpleStringProperty();
    public SimpleDoubleProperty value = new SimpleDoubleProperty();

    public Item(String serialNumber, String name, double value) {
        setSerialNumber(serialNumber);
        setName(name);
        setValue(value);
    }

    /*
    setters and getters
     */

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

    // checks that a provided serial number is in the "XXXXXXXXXX" format
    public static boolean verifySerialNumberFormat(String serialNumber) {

        // serial number must be 10 characters long
        if (serialNumber.length() != 10) {
            return false;
        }

        // converts string to character array
        char[] serialNumberArray = serialNumber.toCharArray();

        // loops through string to check it is alphanumerical data
        // returns false if data isn't a valid input
        for (int i = 0; i < serialNumberArray.length; i++) {
            if (!Character.isDigit(serialNumberArray[i]) && !Character.isLetter(serialNumberArray[i])) {
                return false;
            }
        }
        return true;
    }

    // checks that the name value is between 2-256 characters
    public static boolean verifyNameFormat(String name) {
        if (name.length() >= 2 && name.length() <= 256) {
            return true;
        }
        return false;
    }

    // checks if item value is in a format that can be displayed as a price
    public static boolean verifyValueFormat(String value) {
        // checks that string can be parsed as a double
        try {
             Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return false;
        }
        String[] decimalPlaces = value.split("\\.");

        // if string has more than 2 digits in the decimals place, it is an invalid input
        if (decimalPlaces.length == 2) {
            return decimalPlaces[1].length() <= 2;
        } else return decimalPlaces.length == 1;
    }
}

