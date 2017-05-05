package Global;

import Transferable.Position;

/**
 * Created by brownl on 5/4/2017.
 */
public class Player implements Entity {
    private Position myPos;
    private int facing = Constants.FACING_NORTH;


    @Override
    public Position getPosition() {
        return myPos;
    }

    public void changeDirection(int newdir){
        facing = newdir;
    }
}
