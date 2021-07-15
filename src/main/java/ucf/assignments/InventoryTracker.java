package ucf.assignments;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class InventoryTracker extends Application {
/*
Write a program that tracks your personal inventory. The program should allow you to enter an item, a serial number, and estimated value. The program should then be able to print out a tabular report in both HTML and CSV formats that looks like this:

Value     Serial Number   Name
$399.00   AXB124AXY       Xbox One
$599.99   S40AZBDE4       Samsung TV

Constraints

    Ensure that your application meets the following requirements specification:
        The application shall be a GUI application

        The user shall be able to store at least 100 inventory items
        The user shall be able to provide a file name to which the inventory list will be saved
        The user shall be able to select the format of the saved inventory list
        The user shall be able to save their list of inventory items to external storage as a CSV file
        The user shall be able to save their list of inventory items to external storage as a HTML file
        The user shall be able to save their list of inventory items to external storage as a JSON file
        The user shall be able to select the format of the saved inventory list
        The user shall be able to load a list of inventory items from previously saved by the application as a CSV file
        The user shall be able to load a list of inventory items from previously saved by the application as a HTML file
        The user shall be able to load a list of inventory items from previously saved by the application as a JSON file

        The user shall be able to sort the inventory items by value
        The user shall be able to sort inventory items by serial number
        The user shall be able to sort inventory items by name

        The user shall be able to search for an inventory item by serial number
        The user shall be able to search for an inventory item by name

        Each inventory item shall have a value representing its monetary value in US dollars
        Each inventory item shall have a serial number in the format of XXXXXXXXXX where X can be either a letter or digit
        The serial number of an inventory item shall be unique.
        Each inventory item shall have a name consisting of at least 2 characters

        The user shall be able to add a new inventory item
        The user shall be able to remove an existing inventory item
        The user shall be able to edit the value of an existing inventory item
        The user shall be able to edit the serial number of an existing inventory item
        The user shall be able to edit the name of an existing inventory item
        The user shall be able to print all inventory items in a tabular format as defined in the following figure:

            Value     Serial Number    Name
            $399.00   AXB124AXY        Xbox One
            $599.99   S40AZBDE4        Samsung TV

        If a new item has the same serial number as an existing item, the application shall prevent the user from adding the new item.

        Challenges

        Alter the program so that it can store photos. If you're creating this application for a mobile device, allow the user to take a picture with the camera.
        Allow the items to be searchable.
 */

    public static void main(String[] args) {
        launch(args);
    }

    // opens the fxml file to display GUI to user
    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));

            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.setTitle("Todo List App");
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
