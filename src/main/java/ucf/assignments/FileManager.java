package ucf.assignments;

import com.google.gson.Gson;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileManager {

    boolean saveAsCSV(ItemModel itemModel, String data, String fileName) {
        data = "Serial Number, Name, Price";
        for (Item item : itemModel.inventory) {
            data += item.getSerialNumber() + "," + item.getName() + "," + String.valueOf(item.getValue()) + "\n";
        }
        if(saveToFile(fileName, data)) {
            return true;
        } else {
            return false;
        }

    }

    boolean saveAsHTML(String fileName, String data) {

        return true;
    }

    boolean saveAsJson(ItemModel itemModel, String data, String fileName) {
        Gson gson = new Gson();
        List<Serializer> serializedList = new ArrayList<Serializer>();
        for (Item item: itemModel.inventory) {
            Serializer serializedItem = new Serializer(item.getSerialNumber(), item.getName(), item.getValue());
            serializedList.add(serializedItem);
        }
        data = gson.toJson(serializedList);

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

    ItemModel loadHTMLFile(String fileName) {

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


}
