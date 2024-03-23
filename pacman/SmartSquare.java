package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

/**
 * This is the SmartSquare wrapper class that conatins an arraylist.
 */
public class SmartSquare {
    private Rectangle square;
    private ArrayList<Collidable> collidable;
    private Color color;

    /**
     * Class constructor that sets up the smart square's dimensions and color. It instantiates an arraylist of type
     * collidable. T
     * @param gamePane
     */
    public SmartSquare(Pane gamePane){
        this.square = new Rectangle(Constants.SQUARE_DIMENSION, Constants.SQUARE_DIMENSION);
        this.square.setFill(Constants.EMPTY_SPACE_COLOR);
        this.collidable = new ArrayList<>();
        gamePane.getChildren().add(square);


    }

    /**
     * this method sets the location of the smart square depending on the passed in x and y.
     * @param x
     * @param y
     */
    public void setLocations(int x, int y) {
        this.square.setX(x);
        this.square.setY(y);

    }

    /**
     * This method sets the color of the smart square to the passed in color and makes the instance variavle keep track of it.
     * @param color
     */

    public void setColor (Color color){
        this.color = color;
        this.square.setFill(color);
    }

    /**
     * returns the color of the smart square.
     * @return
     */
    public Color getColor (){
        return this.color;
}

    /**
     * method to set up teh walls of the game.
     */
    public void setWall() {
        this.setColor(Constants.WALL_COLOR);
    }

    /**
     * This method gets the element arraylist of type of collidable so that it can accessed elsewhere (such as in game class).
     * @return
     */
    public ArrayList<Collidable> elementArrayList(){
        return collidable;
    }

}

