package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

/**
 * This is the Pacman class. It has the move method for pacman and methods to set/get its location so that it can be accessed
 * elsewhere.
 */

public class Pacman {
    private Circle pacMan;
    private Pane gamePane;

    /**
     * This is the pacman constructor that sets pacman's dimensions and color as well as adds it to the pane.
     * @param gamePane
     */
    public Pacman(Pane gamePane) {
        this.gamePane = gamePane;
        this.pacMan = new Circle(Constants.PACMAN_RADIUS, Constants.PACMAN_COLOR);
        gamePane.getChildren().add(pacMan);
    }

    /**
     * This method sets pacman's location at the start of the game when generating the map.
     * @param col
     * @param row
     */
    public void setLoc(int col, int row) {
        this.setX(col);
        this.setY(row);
    }

    /**
     * This method sets pacman's x location based on the value that is passed in.
     * @param x
     */
    public void setX(int x) {
        this.pacMan.setCenterX(x);
    }

    /**
     * This is a getter method for pacman's x location.
     * @return
     */
    public double getXLoc() {
        return this.pacMan.getCenterX();
    }

    /**
     * This method sets pacman's y lcoation.
     * @param y
     */
    public void setY(int y) {
        this.pacMan.setCenterY(y);
    }

    /**
     * This method is a getter method for pacman's y location.
     * @return
     */
    public double getYLoc() {
        return this.pacMan.getCenterY();
    }

    /**
     * This method is a getter method for pacman's row.
     * @return
     */
    public int getRow() {
        int row = (int) pacMan.getCenterX() / Constants.SQUARE_DIMENSION;
        return row;
    }
    /**
     * This method is a getter method for pacman's col.
     * @return
     */
    public int getCol() {
        int col = (int) pacMan.getCenterY() / Constants.SQUARE_DIMENSION;
        return col;
    }

    /**
     * This method checks for move validity. It checks that at the next mapsquare that pacman moves to is not a wall.
     * @param dx
     * @param dy
     * @param mapSquares
     * @return
     */
    public Boolean canMove(int dx, int dy, SmartSquare[][] mapSquares) {
        if (mapSquares[((int) this.getYLoc() / Constants.SQUARE_DIMENSION) + dy][(int) (this.getXLoc() / Constants.SQUARE_DIMENSION) + dx].getColor() == Constants.WALL_COLOR) {
            return false;
        }
        return true;
    }

    /**
     * This method moves pacman by checking that given the direction pacman is moving in, it can move to the mapsquare
     * above/below/right/left of it. If it is able to, its location will be set to that location.
     * @param currDirection
     * @param mapSquares
     */
    public void movePacman(Direction currDirection, SmartSquare[][] mapSquares) {
        if (currDirection == Direction.UP && this.canMove(0, -1, mapSquares)) {
            this.setY((int) this.getYLoc() - Constants.SQUARE_DIMENSION);
        }
        if (currDirection == Direction.DOWN && this.canMove(0, 1,mapSquares)) {
            this.setY((int) this.getYLoc() + Constants.SQUARE_DIMENSION);
        }
        if (currDirection == Direction.LEFT && this.canMove(-1, 0, mapSquares)) {
            this.setX((int) this.getXLoc() - Constants.SQUARE_DIMENSION);
        }
        if (currDirection == Direction.RIGHT && this.canMove(1, 0,mapSquares)) {
            this.setX((int) this.getXLoc() + Constants.SQUARE_DIMENSION);
        }
    }

    /**
     * This is the wrapper method that checks that if pacman's location is at one of the tunnels and its durection is heading
     * toward it, then it will be set to the other tunnel.
     * @param currDirection
     */
    public void wrapper(Direction currDirection) {
        if (this.getCol() == Constants.TUNNEL_COL  && this.getRow() == Constants.RIGHT_TUNNEL && currDirection == Direction.RIGHT) {
            this.setLoc(Constants.LEFT_TUNNEL +Constants.SQUARE_DIMENSION/2, Constants.TUNNEL_COL * (Constants.SQUARE_DIMENSION) + Constants.SQUARE_DIMENSION / 2);
        }

        if (this.getCol() == Constants.TUNNEL_COL && this.getRow()==Constants.LEFT_TUNNEL && currDirection == Direction.LEFT) {
            this.setLoc(Constants.RIGHT_TUNNEL *(Constants.SQUARE_DIMENSION)+ Constants.SQUARE_DIMENSION/2, Constants.TUNNEL_COL*(Constants.SQUARE_DIMENSION)+Constants.SQUARE_DIMENSION/2);
        }

    }


}

