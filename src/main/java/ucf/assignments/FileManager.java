/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Joseph Crown
 */
package ucf.assignments;

import com.google.gson.Gson;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileManager {

    // takes an ItemModel and the name of a file and saves inventory as a TSV file
    boolean saveAsTSV(ItemModel itemModel, String fileName) {
        String data = "Serial Number\tName\tPrice\n";

        //loops through each item in the ItemModel's inventory, building a string of data
        for (Item item : itemModel.inventory) {
            data += item.getSerialNumber() + "\t" + item.getName() + "\t" + String.valueOf(item.getValue()) + "\n";
        }

        // calls save to file to save string 'data' to the file directory provided
        // returns true if file saved successfully, false if failure
        if(saveToFile(fileName, data)) {
            return true;
        } else {
            return false;
        }

    }

    // takes an ItemModel and the name of a file and saves inventory as a HTML file
    boolean saveAsHTML(ItemModel itemModel, String fileName) {
        StringBuilder data = new StringBuilder();

        // loops through each item in the ItemModel's inventory
        // uses a customized 'Serializer' class to generate a string in the HTML format
        for (Item item : itemModel.inventory) {
            Serializer serializedItem = new Serializer(item.getSerialNumber(), item.getName(), item.getValue());
            data.append(serializedItem.serializeToHTML());
        }

        // this string gets the basic outline of an html document
        // adds the serialized inventory data to html file in a tabular format
        String HTML = getBoilerplateHTML();
        HTML = HTML.replace("TABLEDATA", data);

        // saves to file using HTML string
        // returns true if file saved successfully, false if failure
        if(saveToFile(fileName, HTML)) {
            return true;
        } else {
            return false;
        }
    }

    // takes an ItemModel and the name of a file and saves inventory as a HTML file
    boolean saveAsJson(ItemModel itemModel, String fileName) {

        // Uses gson serializer to parse data into JSON format
        Gson gson = new Gson();
        List<Serializer> serializedList = new ArrayList<Serializer>();

        // loops through items in inventory
        // uses customized serialized class to generate primitives from an ObservableList object
        for (Item item: itemModel.inventory) {
            Serializer serializedItem = new Serializer(item.getSerialNumber(), item.getName(), item.getValue());
            serializedList.add(serializedItem);
        }

        // use gson to write inventory data to JSON format
        String data = gson.toJson(serializedList);

        // saves data string to file
        // returns true if file saved successfully, false if failure
        if(saveToFile(fileName, data)) {
            return true;
        } else {
            return false;
        }
    }

    // opens file at specified directory
    // creates file if the file doesn't already exist
    // writes data string to file
    // returns true if successful, false if something goes wrong
    boolean saveToFile(String fileName, String data) {
        File file = new File(fileName);
        try {
            // save functions will not overwrite existing files
            if (!file.createNewFile()) return false;
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(data);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // loads inventory from a saved TSV file
    ItemModel loadTSVFile(String fileName) {
        List<Item> loadedItems = new ArrayList<>();

        try {

            // open file from provided directory, and use scanner to read it
            File file = new File(fileName);
            Scanner readFile = new Scanner(file);
            readFile.hasNextLine();

            // reads each line of file
            while(readFile.hasNextLine()) {

                // splits the line of data into a string array separated by a 'tab'
                String[] data = readFile.nextLine().split("\t");

                // data from string array used to initialize a new Item then add it to an inventory
                Item newItem = new Item(data[0], data[1], Double.parseDouble(data[2]));
                loadedItems.add(newItem);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // returns new inventory
        return new ItemModel(loadedItems);
    }

    // loads inventory from a saved HTML file
    ItemModel loadHTMLFile(String fileName) {
        List<Item> listOfItems = new ArrayList<>();

        try {

            // opens file from provided directory, reads it with a scanner
            File file = new File(fileName);
            Scanner readFile = new Scanner(file);

            // reads each line of the file
            while(readFile.hasNextLine()) {

                String data = readFile.nextLine();

                // when a HTML element '<tr>' is reached, it then reads the next three lines
                // parses the string for values and then adds to string array
                if (data.contains("<tr>")) {
                    String[] tableColumns = new String[3];

                    for (int i = 0; i < 3; i++) {
                        readFile.hasNextLine();
                        String tableCell = readFile.nextLine();
                        tableCell = tableCell.replace("  \t\t\t<td>", "");
                        tableCell = tableCell.replace("</td>", "");
                        tableColumns[i] = tableCell;
                    }
                    // check that the data isn't the table heading
                    // adds new item to the List
                    if (!tableColumns[2].contains("Price")) {
                        listOfItems.add(new Item(tableColumns[0], tableColumns[1], Double.parseDouble(tableColumns[2])));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // returns new ItemModel made from List
        return  new ItemModel(listOfItems);
    }

    // loads inventory from a saved HTML file
    ItemModel loadJSONFile(String fileName) {
        List<Item> listOfItems = new ArrayList<>();

        try {

            // uses Gson to parse loaded JSON file
            // creates an array of serialized items from file
            Gson gson = new Gson();
            File file = new File(fileName);
            Reader readFromFile = Files.newBufferedReader(file.toPath());
            Serializer[] loadedItems = gson.fromJson(readFromFile, Serializer[].class);

            // loops through loadedItems array and adds items to List
            for (int i = 0; i < loadedItems.length; i++) {
                listOfItems.add(new Item(
                        loadedItems[i].serialNumber,
                        loadedItems[i].name,
                        loadedItems[i].value));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // returns an ItemModel initialized with the list
        return new ItemModel(listOfItems);
    }

    // returns the basic html document that can have tabular data added to it
    String getBoilerplateHTML() {
        String boilerplateHTML = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "\n" +
                "   <head>\n" +
                "      <title>Inventory</title>\n" +
                "   </head>\n" +
                "\t\n" +
                "   <body>\n" +
                "      <table border = \"1\">\n" +
                "         <tr>\n" +
                "            <th>Serial Number</th>\n" +
                "            <th>Name</th>\n" +
                "            <th>Price</th>\n" +
                "         </tr>\n" +
                "TABLEDATA" + //where table data will be placed in the file
                "      </table>\n" +
                "   </body>\n" +
                "   \n" +
                "</html>";

        return boilerplateHTML;
    }


}
