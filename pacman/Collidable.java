package pacman;

/**
 * This is the Collidable interface that declares the collide and add to score methods. It is implemented by Ghost,
 * Dot, and Energizer classes.
 */
public interface Collidable {
    /**
     * This is the ifCollide method that each collidable element overrides and calls when they collide with Pacman.
     * @param game
     */
    void ifCollide(Game game);
    /**
     * This is the addToScore method that each collidable element overrides and calls to add to the score after colliding.
     * @param game
     */
    int addToScore(Game game);
}
