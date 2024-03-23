package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This is the ghost wrapper class that implements the Collidable interface. It contains the BFS and move methods that
 * allow the ghosts to chase pacman.
 */
public class Ghost implements Collidable {
    private Rectangle ghost;
    private Pane gamePane;
    private Color color;
    private Direction ghostDir;
    private SmartSquare[][] mapSquares;


    /**
     * This is the ghost constructor that takes in a pane, color, and mapSquares to set the different ghosts'
     * characteristics. It adds the ghost to the pane and sets initializes the ghosts' direction to down.
     * @param gamePane
     * @param color
     * @param mapSquares
     */
    public Ghost(Pane gamePane, Color color, SmartSquare[][] mapSquares) {
        this.mapSquares = mapSquares;
        this.gamePane = gamePane;
        this.color = color;
        this.ghost = new Rectangle(Constants.SQUARE_DIMENSION, Constants.SQUARE_DIMENSION);
        this.ghost.setFill(color);
        this.gamePane.getChildren().add(ghost);

        this.ghostDir = Direction.DOWN;
    }

    /**
     * This is the Breadth First Search method that takes in the target. It calls the helper method to adds the
     * valid neighbors to the queue and calculates and checks that the current distances is less than the closest distance.
     * if so, it will set the closest distance to that distance and direction and  return the closest direction.
     * @param Target
     * @return
     */
    public Direction ghostBFS(BoardCoordinate Target) {
        Direction closestDirection = null;

        Direction[][] currDirection1 = new Direction[Constants.NUM_ROW][Constants.NUM_COL];

        BoardCoordinate current = new BoardCoordinate(this.getRow(), this.getCol(), false);

        Queue<BoardCoordinate> queue = new LinkedList<>();
        this.addValidNeighbors(current, currDirection1, true, queue);

        Double closestDistance = Double.POSITIVE_INFINITY;

        while (!queue.isEmpty()) {
            current = queue.remove();
            int y = current.getRow();
            int x = current.getColumn();
            int targetY = Target.getRow();
            int targetX = Target.getColumn();
            double currDistance = Math.hypot(x - targetX, y - targetY);

            if (currDistance < closestDistance) {
                closestDistance = currDistance;
                closestDirection = currDirection1[y][x];

            }
            this.addValidNeighbors(current, currDirection1, false, queue);

        }
        return closestDirection;
    }

    /**
     * This the add valid neighbors method that takes in he ghost' current board coordinate, the 2d directions array, a boolean
     * checking if it is the first, and a queue.
     * @param current
     * @param directions
     * @param isFirst
     * @param queue
     */
    public void addValidNeighbors(BoardCoordinate current, Direction[][] directions, Boolean isFirst, Queue queue) {
        Direction currentDir = directions[current.getRow()][current.getColumn()];

        for (Direction dir : Direction.values()) {
            if (this.ghostDir.opposite() != dir) {
                //gets the next row and column based on the direction that the ghost is moving in.
                int x = dir.shiftX(current.getColumn());
                int y = dir.shiftY(current.getRow());
                // checks that the neighboring row and col is not a wall is null at the direction array.
                if (this.mapSquares[y][x].getColor() != Constants.WALL_COLOR && directions[y][x] == null) {
                //creates a board coordinate of the neighboring row and col and adds it to the queue.
                    BoardCoordinate boardCoord = new BoardCoordinate(y, x, false);
                    queue.add(boardCoord);
                    //if the neighboring row and col is the first, its direction will be set to that direction.
                    if (isFirst) {
                        directions[y][x] = dir;
                    }
                    //if the neighboring row and col is not the first, the direction will be set to the cirrent direction
                    //at the ghost's row and col.
                    else {
                        directions[y][x] = currentDir;
                    }
                }

            }
        }
    }

    /**
     * This is the move method that switches through the direction that is passed in. Given a specific direction,
     * the ghost will be move accordingly after being removed from the old mapsquare's element arraylist. After moving, the
     * ghost will be added to the new map square's array list. The direction will be set to that new direction.
     * @param dir
     */

    public void move(Direction dir) {
        mapSquares[this.getRow()][this.getCol()].elementArrayList().remove(this);
        switch (dir) {
            case UP:
                this.ghost.setY(this.ghost.getY() - Constants.SQUARE_DIMENSION);
                this.ghostDir = Direction.UP;
                mapSquares[this.getRow()][this.getCol()].elementArrayList().add(this);
                break;
            case DOWN:
                this.ghost.setY(this.ghost.getY() + Constants.SQUARE_DIMENSION);
                this.ghostDir = Direction.DOWN;
                mapSquares[this.getRow()][this.getCol()].elementArrayList().add(this);
                break;

            case LEFT:
                //checks if the ghost has reached the tunnel and if it has, then it will be set to the other tunnel.
                if (this.getCol() == Constants.LEFT_TUNNEL) {
                    this.ghost.setX(Constants.RIGHT_TUNNEL);
                } else {
                    this.ghost.setX(this.ghost.getX() - Constants.SQUARE_DIMENSION);
                    this.ghostDir = Direction.LEFT;
                    mapSquares[this.getRow()][this.getCol()].elementArrayList().add(this);
                }
                break;
            case RIGHT:
                //checks if the ghost has reached the tunnel and if it has, then it will be set to the other tunnel.
                if (this.getCol() == Constants.RIGHT_TUNNEL) {
                    this.ghost.setX(Constants.LEFT_TUNNEL);
                } else {
                    this.ghost.setX(this.ghost.getX() + Constants.SQUARE_DIMENSION);
                    this.ghostDir = Direction.RIGHT;
                    mapSquares[this.getRow()][this.getCol()].elementArrayList().add(this);
                }
                break;
            default:
                break;
        }
    }

    public Color getColor() {
        return this.color;
    }

    /**
     * sets the color of the ghost. This is used when setting the ghost to frighetened mode and returning it back to its
     * orginial color.
     * @param color
     */
    public void setColor(Color color) {
        this.ghost.setFill(color);
    }

    /**
     * This method sets the ghosts' location given the row and col.
     * @param row
     * @param col
     */
    public void setLoc(int row, int col) {
        double xLoc = col;
        double yLoc = row;

        this.ghost.setX(xLoc);
        this.ghost.setY(yLoc);
    }

    /**
     * this method sets the y location of the ghost
     * @param y
     */
    public void setY(int y) {
        this.ghost.setY(y);
    }

    /**
     * this method sets the x location of the ghost
     * @param x
     */
    public void setX(int x) {
        this.ghost.setX(x);
    }

    /**
     * This is the getRow method that returns the current row that the ghost is in.
     * @return
     */
    public int getRow() {
        //calculates the row that the ghost is in based on the y location.
        int row = (int) this.ghost.getY() / Constants.SQUARE_DIMENSION;
        return row;
    }
    /**
     * This is the getCol method that returns the current col that the ghost is in.
     * @return
     */
    public int getCol() {
        //calculates the col that the ghost is in based on its x location.
        int col = (int) this.ghost.getX() / Constants.SQUARE_DIMENSION;
        return col;
    }

    /**
     * This is the method that returns a valid random direction for the ghost to move in during frighetened mode.
     * @return
     */
    public Direction getRandDir() {
        ArrayList<Direction> array = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            int x = dir.shiftX(this.getCol());
            int y = dir.shiftY(this.getRow());
            //adds all viable directions to the arraylist- not a wall and not the opposite direction.
            if (this.mapSquares[y][x].getColor() != Constants.WALL_COLOR && this.ghostDir.opposite() != dir) {
                array.add(dir);
            }
        }
        //calculates random number based on the array size.
        int randInt = (int) (Math.random() * array.size());
        //returns the direction from teh arraylist at that random integer.
        return array.get(randInt);
    }

    /**
     * This is the ifCollide method that is overriden from the Collidable interface. It takes in game to check
     * the current mode and call the appropriate collide methods.
     * @param game
     */
    @Override
    public void ifCollide(Game game) {
        if(game.getCurrentMode()==Modes.FRIGHTENED){
            game.getGhostPen().add(this);
            this.ghost.setX(275);
            this.ghost.setY(250);
        }
        if(game.getCurrentMode()==Modes.SCATTER || game.getCurrentMode() ==Modes.CHASE){
            game.resetGhostPen();
        }
    }

    /**
     * This is the addToScore method that returns the score points that a ghost is worth when pacman collides with it
     * during frightened mode.
     * @param game
     * @return
     */

    @Override
    public int addToScore(Game game) {
        if (game.getCurrentMode() == Modes.FRIGHTENED) {
            return Constants.GHOST_POINTS;
        }
        return 0;
    }

    /**
     * This is the livesLost method that returns the amount of lives that pacman would lose if it collides with a ghost.
     * @return
     */

    public int livesLost() {
        return Constants.LIVES_LOST;
    }


}









