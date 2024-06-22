package org.example.eiscuno.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Represents the main stage of the Uno game application.
 * This stage displays the game interface to the user.
 */
public class GameUnoStage extends Stage {

    /**
     * Constructs a new instance of GameUnoStage.
     *
     * @throws IOException if an error occurs while loading the FXML file for the game interface.
     */
    public GameUnoStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/eiscuno/game-uno-view.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new IOException("Error while loading FXML file", e);
        }
        Scene scene = new Scene(root);
        getIcons().add(
                new Image(
                        String.valueOf(getClass().getResource("/org/example/eiscuno/favicon.png"))));
        setTitle("JLI Uno");
        setScene(scene);
        setResizable(false);
        show();
    }

    /**
     * Closes the instance of GameUnoStage.
     * This method is used to clean up resources when the game stage is no longer needed.
     */
    public static void deleteInstance() {
        GameUnoStageHolder.INSTANCE.close();
        GameUnoStageHolder.INSTANCE = null;
    }

    /**
     * Retrieves the singleton instance of GameUnoStage.
     *
     * @return the singleton instance of GameUnoStage.
     * @throws IOException if an error occurs while creating the instance.
     */
    public static GameUnoStage getInstance() throws IOException {
        return GameUnoStageHolder.INSTANCE != null ?
                GameUnoStageHolder.INSTANCE :
                (GameUnoStageHolder.INSTANCE = new GameUnoStage());
    }

    /**
     * Holder class for the singleton instance of GameUnoStage.
     * This class ensures lazy initialization of the singleton instance.
     */
    private static class GameUnoStageHolder {
        private static GameUnoStage INSTANCE;
    }
}
