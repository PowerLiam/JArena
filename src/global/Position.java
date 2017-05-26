package global;

import java.io.Serializable;

public class Position implements Serializable {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Position check) {
        if (check.x == x && check.y == y) {
            return true;
        }
        return false;
    }

    public boolean checkBoundaries() {
        return !(x > Constants.BOUNDARY_X || y > Constants.BOUNDARY_Y || x < 0 || y < 0);
    }

    public synchronized int getX() {
        return x;
    }

    public synchronized void setX(int x) {
        this.x = x;
    }

    public synchronized int getY() {
        return y;
    }

    public synchronized void setY(int y) {
        this.y = y;
    }
}
