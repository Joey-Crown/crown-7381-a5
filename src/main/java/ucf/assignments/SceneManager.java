package ucf.assignments;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Map;

public class SceneManager {

    Map<String, Scene> scenes;

    void load() {
        ItemModel itemModel = new ItemModel();

        MainWindowController mainWindowController = new MainWindowController(itemModel, this);

        Parent root;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
        loader.setController(mainWindowController);

        try {
            root = loader.load();
            scenes.put("MainWindow", new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public Scene getScene(String name) {
        return scenes.get(name);
    }
}
