package ucf.assignments;

import com.google.gson.Gson;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileManager {

    boolean saveAsCSV(ItemModel itemModel, String fileName) {
        String data = "Serial Number, Name, Price";
        for (Item item : itemModel.inventory) {
            data += item.getSerialNumber() + "," + item.getName() + "," + String.valueOf(item.getValue()) + "\n";
        }
        if(saveToFile(fileName, data)) {
            return true;
        } else {
            return false;
        }

    }

    boolean saveAsHTML(ItemModel itemModel, String fileName) {
        StringBuilder data = new StringBuilder();
        for (Item item : itemModel.inventory) {
            Serializer serializedItem = new Serializer(item.getSerialNumber(), item.getName(), item.getValue());
            data.append(serializedItem.serializeToHTML());
        }

        String HTML = getBoilerplateHTML();
        HTML = HTML.replace("TABLEDATA", data);

        saveToFile(fileName, HTML);
        return true;
    }

    boolean saveAsJson(ItemModel itemModel, String fileName) {
        Gson gson = new Gson();
        List<Serializer> serializedList = new ArrayList<Serializer>();
        for (Item item: itemModel.inventory) {
            Serializer serializedItem = new Serializer(item.getSerialNumber(), item.getName(), item.getValue());
            serializedList.add(serializedItem);
        }
        String data = gson.toJson(serializedList);

        if(saveToFile(fileName, data)) {
            return true;
        } else {
            return false;
        }
    }

    boolean saveToFile(String fileName, String data) {
        File file = new File(fileName);
        try {
            if (!file.createNewFile()) return false;
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    ItemModel loadCSVFile(String fileName) {
        List<Item> loadedItems = new ArrayList<>();

        try {
            File file = new File(fileName);
            Scanner readFile = new Scanner(file);
            readFile.hasNextLine();
            while(readFile.hasNextLine()) {
                String[] data = readFile.nextLine().split(",");
                Item newItem = new Item(data[0], data[1], Double.parseDouble(data[2]));
                loadedItems.add(newItem);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new ItemModel(loadedItems);
    }

    // TODO do testing on this function
    ItemModel loadHTMLFile(String fileName) {
        List<Item> listOfItems = new ArrayList<>();

        try {
            File file = new File(fileName);

            Scanner readFile = new Scanner(file);

            while(readFile.hasNextLine()) {

                String data = readFile.nextLine();

                if (data.contains("<tr>")) {
                    String[] tableColumns = new String[3];

                    for (int i = 0; i < 3; i++) {
                        readFile.hasNextLine();
                        String tableCell = readFile.nextLine();
                        tableCell = tableCell.replace("  \t\t\t<td>", "");
                        tableCell = tableCell.replace("</td>\n", "");
                        tableColumns[i] = tableCell;
                    }
                    // check that the data isn't the table heading
                    if (!tableColumns[2].contains("Price")) {
                        listOfItems.add(new Item(tableColumns[0], tableColumns[1], Double.parseDouble(tableColumns[2])));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return  new ItemModel(listOfItems);
    }

    ItemModel loadJSONFile(String fileName) {
        List<Item> listOfItems = new ArrayList<>();

        try {
            Gson gson = new Gson();
            File file = new File(fileName);
            Reader readFromFile = Files.newBufferedReader(file.toPath());
            Serializer[] loadedItems = gson.fromJson(readFromFile, Serializer[].class);

            for (int i = 0; i < loadedItems.length; i++) {
                listOfItems.add(new Item(
                        loadedItems[i].serialNumber,
                        loadedItems[i].name,
                        loadedItems[i].value));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ItemModel(listOfItems);
    }

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
