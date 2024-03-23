package pacman;

/**
 * This is the Direction enum class that contains the UP, DOWN, LEFT, and RIGHT enums.
 */
public enum Direction {
    UP, DOWN, LEFT, RIGHT;


    /**
     * This method returns the opposite direction of the given direction. This is used when checking that the ghost does
     * not make a 180 turn.
     * @return
     */
    public Direction opposite() {
        switch (this) {
            case UP:
                return DOWN;
            case RIGHT:
                return LEFT;
            case LEFT:
                return RIGHT;
            default:
                return UP;
        }
    }

    /**
     * This method gets the next column based on the given column and direction that it is moving in.
     * @param
     * @return
     */
   public int shiftX(int x) {
       switch (this) {
           case LEFT:
               if (x == Constants.LEFT_TUNNEL) {
                   x = Constants.RIGHT_TUNNEL;
               }
              return x-1;
           case RIGHT:
               if (x == Constants.RIGHT_TUNNEL) {
                   x = Constants.LEFT_TUNNEL;
               }
              return  x+1;
           default:
               return x;

       }
   }

    /**
     * This method gets the next row based on the given row and direction that it is moving in.
     * @param y
     * @return
     */
    public int shiftY(int y){
            switch(this){
                case UP:
                    return y-1;
                case DOWN:
                    return y+1;
                default:
                    return y;
            }

    }




}
