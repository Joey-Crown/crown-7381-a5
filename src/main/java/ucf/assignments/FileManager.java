package ucf.assignments;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {

    boolean saveAsCSV(String fileName, String data) {
        File file = new File(fileName);
        try {
            if (!file.createNewFile()) return false;
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    boolean saveAsHTML(String fileName, String data) {

    }

    boolean saveAsJson(String fileName, String data) {

    }
}
