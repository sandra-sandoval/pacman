package pacman;

import cs15.fnl.pacmanSupport.CS15SquareType;
import cs15.fnl.pacmanSupport.CS15SupportMap;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * This is the game class which handles most of the game logic including mode switching, timeline of ghost and pacman
 * movement, and score updating.
 */
public class Game {
    private SmartSquare[][] mapSquares;
    private Pane gamePane;
    private Timeline timeline;
    private Pacman pacman;
    private Direction currDirection;
    private Dot dot;
    private Energizer energizer;

    private Ghost blinky;
    private Ghost pinky;
    private Ghost clyde;
    private Ghost inky;
    private Ghost[] ghosts;
    private int row;
    private int col;
    private int pacScore;
    private int numLives;
    private Label livesLabel;
    private Label scoreLabel;
    private int counter;
    private Queue<Ghost> ghostPen;
    private int modesCounter;
    private Modes currentMode;

    /**
     * This is the game class constructor that instantiates 2d map of smartsquares, the ghost pen queue, and initializes
     * the current direction, mode, and mode counter. It also calls the timeline, generate map, and set up ghost
     * @param gamePane
     * @param scoreLabel
     * @param livesLabel
     */
    public Game(Pane gamePane, Label scoreLabel, Label livesLabel) {
        this.mapSquares = new SmartSquare[(int) Constants.NUM_ROW][(int) Constants.NUM_COL];
        this.gamePane = gamePane;
        this.scoreLabel = scoreLabel;
        this.livesLabel = livesLabel;
        this.pacScore = Constants.STARTING_SCORE;
        this.numLives = Constants.STARTING_NUM_LIVES;
        this.ghostPen = new LinkedList<>();

        this.generateMap();
        this.handleKeyPressed();
        this.currDirection = null;
        this.counter = 0;
        this.modesCounter = Constants.CHASE_TIMER;
        this.currentMode = Modes.CHASE;
        this.setUpGhostArray();
        this.moveTimeline();



    }

    /**
     * This method sets up the ghost array that contains all instances of Ghost.
     */
    public void setUpGhostArray(){
        this.ghosts = new Ghost[4];
        this.ghosts[0] = this.blinky;
        this.ghosts[1] = this.pinky;
        this.ghosts[2] = this.inky;
        this.ghosts[3] = this.clyde;
    }

    /**
     * This method generates the board map with a 2d array of smart squares based from the information given from
     * the support map. It uses a switch statement based on the 2d array of cs15 type. Based on each case, that element
     * is instantiates and added to that row anc col.
     */
    public void generateMap() {
        CS15SquareType[][] square = CS15SupportMap.getSupportMap();
        //instantiates a 2d array of smartsquares that makes up the map.
        this.mapSquares = new SmartSquare[Constants.NUM_ROW][Constants.NUM_COL];

        for (int row = 0; row < Constants.NUM_ROW; row++) {
            for (int col = 0; col < Constants.NUM_COL; col++) {
                SmartSquare squares = new SmartSquare(this.gamePane);
                squares.setLocations(col * Constants.SQUARE_DIMENSION, row * Constants.SQUARE_DIMENSION);
                this.mapSquares[row][col] = squares;
            }
        }
        for (int i = 0; i < Constants.NUM_ROW; i++) {
            for (int j = 0; j < Constants.NUM_COL; j++) {

                this.row = (i * Constants.SQUARE_DIMENSION);
                this.col = (j * Constants.SQUARE_DIMENSION);
                //switches through the 2d array of the cs15 square type.
                switch (square[i][j]) {
                    case DOT:
                        //instiates the dot and adds it to the element arraylist at that given row and col.
                        this.dot = new Dot(this.gamePane);
                        this.mapSquares[i][j].elementArrayList().add(dot);
                        //sets it location at that row and ocl.
                        dot.setLoc(row, col);
                        break;

                    case ENERGIZER:
                        this.energizer = new Energizer(this.gamePane);
                        this.mapSquares[i][j].elementArrayList().add(energizer);
                        energizer.setLoc(row, col);
                        break;

                    case WALL:
                        this.mapSquares[i][j].setWall();
                        break;

                    case PACMAN_START_LOCATION:
                        this.pacman = new Pacman(this.gamePane);
                        //sets pacman's location to the center of that squares' row and col
                        pacman.setLoc(col + Constants.SQUARE_CENTER, row + Constants.SQUARE_CENTER);
                        break;

                    case GHOST_START_LOCATION:
                        //instiates all ghosts with its given color.
                        this.pinky = new Ghost(this.gamePane, Constants.PINKY_COLOR, this.mapSquares);
                        pinky.setLoc(row, col);
                        //each ghost is added to that specific square's element array list
                        this.mapSquares[i][j].elementArrayList().add(pinky);
                        //added to the ghost pen.
                        this.ghostPen.add(this.pinky);

                        this.inky = new Ghost(this.gamePane, Constants.INKY_COLOR, this.mapSquares);
                        //sets ghosts location based on the given ghost's location from the support map
                        inky.setLoc(row, (col - Constants.SQUARE_DIMENSION));
                        this.mapSquares[i][j].elementArrayList().add(inky);
                        this.ghostPen.add(this.inky);

                        this.blinky = new Ghost(this.gamePane, Constants.BLINKY_COLOR, this.mapSquares);
                        blinky.setLoc((row - Constants.BLINKY_GHOST_OFFSET), col);
                        this.mapSquares[i][j].elementArrayList().add(blinky);

                        this.clyde = new Ghost(this.gamePane, Constants.CLYDE_COLOR, this.mapSquares);
                        clyde.setLoc(row, (col + Constants.SQUARE_DIMENSION));
                        this.mapSquares[i][j].elementArrayList().add(clyde);
                        this.ghostPen.add(this.clyde);
                        break;

                    case FREE:
                        this.mapSquares[i][j].setColor(Constants.EMPTY_SPACE_COLOR);
                        break;
                }
            }
        }

    }

    /**
     * This method instantiates and sets up the timeline and sets the key handling.
     */

    public void moveTimeline() {
        this.gamePane.setOnKeyPressed((KeyEvent e) -> this.onKeyPressed(e.getCode()));
        KeyFrame keyframe = new KeyFrame(Duration.seconds(Constants.TIMELINE_TIMER),
                (ActionEvent e) -> this.handleTimeline());
        this.timeline = new Timeline(keyframe);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }

    /**
     * This method handles the timeline, it is called on the timeline and calls all the methods that must be called
     * throughout the timeline such as pacman and ghosts' movement.
     *
     */
    public void handleTimeline() {
        pacman.movePacman(this.currDirection, this.mapSquares);
        this.collide();
        pacman.wrapper(this.currDirection);
        this.switchMode();
//        this.update();
        this.collide();
        this.counter++;
        if (this.counter == Constants.RELEASE_GHOSTS_TIMER) {
            this.releaseGhosts();
            this.counter = 0;
        }
        this.modesCounter--;

    }

    /**
     * This method releases the ghosts from the ghost pen if the ghost pen is not empty.
     */

    public void releaseGhosts() {
        if (!this.ghostPen.isEmpty()) {
            //when removed from the pen, ghosts are set to outside the pen (where blinky starts off)
            this.ghostPen.remove().setLoc(Constants.GHOST_ROW - Constants.BLINKY_GHOST_OFFSET, Constants.GHOST_COL);
        }

    }

    /**
     * This method sets the game to frightened mode by updating the mode to frightened and counter to the frightened
     * duration.
     */
    public void setFrightened() {
        this.currentMode = Modes.FRIGHTENED;
        this.modesCounter = Modes.FRIGHTENED.getDuration();
        ;
    }

    /**
     * This is the frightened mode method which loops through each ghost in the ghost array and changes their color
     * and moves them based on the random direction returned from the getRandomDir method in the ghost class.
     */
    public void frightenedMode() {
        for (Ghost ghost : ghosts) {
            ghost.setColor(Constants.FRIGHTENED_COLOR);
            ghost.move(ghost.getRandDir());
        }
        ;
    }

    /**
     * This method is called when the game is in chase mode. It creates board coordinates based on pacman's location
     * and its surrounding spaces which each ghost takes in.
     */
    public void chaseMode() {
        BoardCoordinate pacmanLoc = new BoardCoordinate(pacman.getCol(), pacman.getRow(), true);
        blinky.move(blinky.ghostBFS(pacmanLoc));
        BoardCoordinate clydeTarget = new BoardCoordinate(pacman.getCol() + 2, pacman.getRow(), true);
        clyde.move(clyde.ghostBFS(clydeTarget));
        BoardCoordinate pinkyTarget = new BoardCoordinate(pacman.getCol() , pacman.getRow()-4, true);
        pinky.move(pinky.ghostBFS(pinkyTarget));
        BoardCoordinate inkyTarget = new BoardCoordinate(pacman.getCol() - 3, pacman.getRow() + 1, true);
        inky.move(inky.ghostBFS(inkyTarget));
    }

    /**
     * This is the scatter method which creates a BoardCoordinate for each corner that the a ghost will target. Each ghost
     * calls their move method and passes in the direction taht is returned from bfs method.
     */
    public void scatterMode() {
        BoardCoordinate upperRightCorner = new BoardCoordinate(Constants.BOTTOM_CORNER, Constants.BOTTOM_CORNER, true);
        //move method based on closest direction to target (upper right corner).
        blinky.move(blinky.ghostBFS(upperRightCorner));
        BoardCoordinate lowerRightCorner = new BoardCoordinate(Constants.TOP_CORNER, Constants.BOTTOM_CORNER, true);
        pinky.move(pinky.ghostBFS(lowerRightCorner));
        BoardCoordinate upperLeftCorner = new BoardCoordinate(Constants.BOTTOM_CORNER, Constants.TOP_CORNER, true);
        inky.move(inky.ghostBFS(upperLeftCorner));
        BoardCoordinate bottomLefftCorner = new BoardCoordinate(Constants.TOP_CORNER, Constants.TOP_CORNER, true);
        clyde.move(clyde.ghostBFS(bottomLefftCorner));

    }

    /**
     * This method switches the mode that the game is currently in through a switch method. In each case, the
     * move method for that certain mode is called and the mode counter is checked to set the mode to the next mode and the
     * counter to that duration.
     */

    public void switchMode() {

        switch (this.currentMode) {
            case CHASE:
                this.chaseMode();
                if (modesCounter == 0) {
                    this.currentMode = Modes.SCATTER;
                    this.modesCounter = Modes.SCATTER.getDuration();
                }
                break;
            case SCATTER:
                this.scatterMode();
                if (modesCounter == 0) {
                    this.currentMode = Modes.CHASE;
                    this.modesCounter = Modes.CHASE.getDuration();
                }
                break;
            case FRIGHTENED:
                this.frightenedMode();
                if (modesCounter == 0) {
                    blinky.setColor(Color.RED);
                    pinky.setColor(Color.PINK);
                    inky.setColor(Color.CYAN);
                    clyde.setColor(Color.ORANGE);
                    this.currentMode = Modes.CHASE;
                    this.modesCounter = Modes.CHASE.getDuration();

                }
                break;
        }

    }

    /**
     * This method is called when a ghost and pacman collide in chase or scatter mode. It adds the ghosts back into the pen (the queue)
     * and sets all the ghosts anc pacman to their to their start location.
     */
    public void resetGhostPen() {
        ghostPen.add(pinky);
        ghostPen.add(inky);
        ghostPen.add(clyde);
        pinky.setLoc(Constants.PINKY_ROW, Constants.PINKY_COL);
        inky.setLoc(Constants.INKY_ROW, Constants.INKY_COL);
        clyde.setLoc(Constants.CLYDE_ROW, Constants.CLYDE_COL);
        blinky.setLoc(Constants.BLINKY_ROW, Constants.BLINKY_COL);

        pacman.setX(Constants.PACMAN_STARTX);
        pacman.setY(Constants.PACMAN_STARTY);
        this.currentMode = Modes.CHASE;
    }

    /**
     * This method returns the queue of ghosts (ghost pen).
     * @return
     */
    public Queue<Ghost> getGhostPen(){
        return this.ghostPen;
}

    /**
     * This method sets the focus to the gamepane so key input is accurately consumed.
     */
    public void handleKeyPressed() {
        this.gamePane.setFocusTraversable(true);
    }

    /**
     * This method sets the current direction to a given direction based on key input through a switch method and if the
     * move is valid (not a wall).
     * @param code
     */
    public void onKeyPressed(KeyCode code) {
        switch (code) {
            case LEFT:
                if (pacman.canMove(-1, 0, this.mapSquares)) {
                    this.currDirection = Direction.LEFT;
                }
                break;
            case RIGHT:
                if (pacman.canMove(1, 0, this.mapSquares)) {
                    this.currDirection = Direction.RIGHT;
                }
                break;
            case UP:
                if (pacman.canMove(0, -1, this.mapSquares)) {
                    this.currDirection = Direction.UP;
                }
                break;
            case DOWN:
                if (pacman.canMove(0, 1, this.mapSquares)) {
                    this.currDirection = Direction.DOWN;
                }
                break;
            default:
                break;
        }
    }

    /**
     * returns the current mode that the game is in. This is used when deciding how the ghost should collide with pacman.
     * @return
     */
    public Modes getCurrentMode() {
        return this.currentMode;
    }

    /**
     * This is the collide method which both checks for collisions and calls the ifCOllide method of each element in the
     * collisable list of the current mapsquare pacman is in.
     */
    public void collide() {
        List<Collidable> collidableList =  mapSquares[pacman.getCol()][pacman.getRow()].elementArrayList();
        for (int i = 0; i<collidableList.size(); i++) {
            collidableList.get(i).ifCollide(this);
            this.pacScore += collidableList.get(i).addToScore(this);
            //sets the text of the score label to represent the points added to the score.
            scoreLabel.setText("score: " + this.pacScore);


        }
        //clears the array list after collision.
        collidableList.clear();
    }

}


