package pacman;

/**
 * This is the Modes enum class that contains all the modes that are available on the game.
 */

public enum Modes {
    CHASE, SCATTER, FRIGHTENED;

    /**
     * This method switches through the enums and returns the duration of each mode.
     * @return
     */
    public int getDuration() {
        switch (this) {
            case CHASE:
                return Constants.CHASE_TIMER;
            case SCATTER:
                return Constants.SCATTER_TIMER;
            case FRIGHTENED:
                return Constants.FRIGHTENED_TIMER;
        }
        return 0;
    }

}
