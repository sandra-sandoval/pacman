package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

/**
 * This is a wrapper class of Dot that implements the Collidable interface.
 */
public class Dot implements Collidable {
    private Circle dot;
    private Pane gamePane;

    /**
     * This is the Dot constructor that takes in a pane. Ir sets up the dot's dimensions and color as well adds it
     * to the pane.
     * @param gamePane
     */
    public Dot(Pane gamePane) {
        this.gamePane = gamePane;
        this.dot = new Circle(Constants.DOT_RADIUS, Constants.DOT_COLOR);
        gamePane.getChildren().add(dot);

    }

    /**
     * This method sets the dots' center to the center at the given row and column.
     * @param row
     * @param col
     */
    public void setLoc(int row, int col) {
        //sets it at the center of the square.
        double centerX = col + Constants.SQUARE_DIMENSION/2;
        double centerY = row + Constants.SQUARE_DIMENSION/2;

        this.dot.setCenterX(centerX);
        this.dot.setCenterY(centerY);
    }

    /**
     * This method is overriden by the collidable interface. When the dot collides with pacman, this method is called
     * and the dot is removed from the pane.
     * @param game
     */

    @Override
    public void ifCollide(Game game){
        this.gamePane.getChildren().remove(this.dot);
    }

    /**
     * This method is overriden by the collidable interface. It returns the amount of points that a dot is worth when
     * it collides with Pacman.
     * @param game
     * @return
     */
    @Override
    public int addToScore(Game game) {
        return  Constants.DOT_POINTS;
    }
}
