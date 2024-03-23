package pacman;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This is the app class which extends application.
 */

public class App extends Application {
    /**
     * This method is overriden from Application and sets up the scene with the root pane and dimensions.
     * @param stage
     */
    @Override
    public void start(Stage stage) {
        // Create top-level object, set up the scene, and show the stage here.
        PaneOrganizer organizer = new PaneOrganizer();
        stage.setTitle("PacMan");
        Scene scene = new Scene(organizer.getRoot(), Constants.SCENE_WIDTH,Constants.SCENE_HEIGHT);
        stage.setScene(scene);
        stage.show();
    }

    /*
    * Here is the mainline! No need to change this.
    */

    public static void main(String[] argv) {
        // launch is a method inherited from Application
        launch(argv);
    }
}
