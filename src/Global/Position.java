package Global;

/**
 * Created by brownl on 5/4/2017.
 */
public class Position {
    public int x;
    public int y;
    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public boolean equals(Position check){
        if(check.x == x && check.y == y){
            return true;
        }
        return false;
    }
}
