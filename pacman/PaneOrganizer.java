package pacman;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * This is the pane organzier class
 */

public class PaneOrganizer {
    private BorderPane root;
    private HBox sideBox;
    private int pacScore;
    private int numLives;
    private Label scoreLabel;

    /**
     * this is the pane organizer constructor that instantiates a root pane, side box and inititializes the score and lives.
     * It sets the button pane at the top of the root pane.
     */
    public PaneOrganizer(){
        this.root = new BorderPane();
        HBox buttonPane = new HBox();
        this.sideBox = new HBox();
        this.pacScore = 0;
        this.numLives = 3;
        this.root.setTop(buttonPane);
        this.setsUpGamePane();
        this.setUpButtons(buttonPane);





    }

    /**
     * This method creates labels for the lives and score and adds it to the game. It creates a pane that is added to the root
     * and the game is added to that gamepane.
     */
    public void setsUpGamePane(){

        Label lives = new Label("Lives: " + this.numLives);
        Label score = new Label("Score: "+this.pacScore);
        Pane gamePane = new Pane();
        this.root.setCenter(gamePane);
        //pane takes in the pane and score and lives since it will keep track of pacman's lives and score.
        new Game(gamePane, score, lives);

        //add the labels to the sidebox which is set to the bottom of the root pane.
        this.sideBox.getChildren().addAll(lives, score);
        this.sideBox.setSpacing(Constants.SIDE_BOX_SPACING);
        this.sideBox.setStyle("-fx-font: italic bold 20px roboto, serif;-fx-text-alignment: center;");
        this.root.setBottom(this.sideBox);
    }

    /**
     * This is the method to set up the quit button.
     * @param buttonPane
     */
    public void setUpButtons(HBox buttonPane) {
        Button quit = new Button("Quit");
        buttonPane.getChildren().addAll(quit);
        quit.setOnAction((ActionEvent e) -> System.exit(0));
        //done so that it does not affect other user input
        quit.setFocusTraversable(false);

    }

    /**
     * This is a getter method for the root pane.
     * @return
     */
    public BorderPane getRoot(){
        return root;
    }

}
