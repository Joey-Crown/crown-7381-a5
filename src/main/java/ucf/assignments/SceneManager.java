package ucf.assignments;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SceneManager {

    Map<String, Scene> scenes = new HashMap<>();

    void load() {
        ItemModel itemModel = new ItemModel();


        EditItemController editItemController = new EditItemController(itemModel, this);
        MainWindowController mainWindowController = new MainWindowController(itemModel, this, editItemController);

        Parent root;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
        loader.setController(mainWindowController);

        try {
            root = loader.load();
            scenes.put("MainWindow", new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //TODO fix error loading edit window scene with its controller
        loader = new FXMLLoader(getClass().getResource("EditItemWindow.fxml"));
        loader.setController(editItemController);

        try {
            root = loader.load();
            scenes.put("EditWindow", new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Scene getScene(String name) {
        return scenes.get(name);
    }
}
