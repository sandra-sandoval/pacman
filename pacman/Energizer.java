package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

/**
 * This is the wrapper class for the Energizer that implements the collidable interface.
 */
public class Energizer implements Collidable {
    private Circle energizer;
    private Pane gamePane;

    /**
     * This is the Energizer constructor that sets the energizer's dimensions and color. It adds the energizer to the
     * pane.
     * @param gamePane
     */
    public Energizer(Pane gamePane) {
        this.gamePane = gamePane;
        this.energizer = new Circle(Constants.ENERGIZER_RADIUS, Constants.ENERGIZER_COLOR);
        gamePane.getChildren().add(energizer);

    }

    /**
     * This method sets the energizer's location to the square's center given the current row and column of the square.
     * @param row
     * @param col
     */
    public void setLoc(int row, int col) {
        double centerX = col + Constants.SQUARE_DIMENSION / 2;
        double centerY = row + Constants.SQUARE_DIMENSION / 2;

        this.energizer.setCenterX(centerX);
        this.energizer.setCenterY(centerY);
    }

    /**
     * This method overrides the ifCollide method from the collidable interface. This method is called when Pacman
     * collides with it. It removes the energizer from the pane and sets frightened mode.
     * @param game
     */
    @Override
    public void ifCollide(Game game) {
        this.gamePane.getChildren().remove(this.energizer);
        game.setFrightened();
    }

    /**
     * This method overrides the addToScore method from the collidable interface. It returns the amount of points
     * that a energizer is worth.
     * @param game
     * @return
     */
    @Override
    public int addToScore(Game game) {
        return Constants.ENERGIZER_POINTS;

    }
}
