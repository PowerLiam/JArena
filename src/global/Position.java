package global;

import java.io.Serializable;

public class Position implements Serializable{
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

    public boolean checkBoundaries(){
        return !(x > Constants.BOUNDARY_X || y > Constants.BOUNDARY_Y || x < 0 || y < 0);
    }
}
