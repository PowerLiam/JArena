package Transferable;

public class Volition {
    private int face;
    private int move;

    public Volition(int face, int move) {
        this.face = face;
        this.move = move;
    }

    public Volition(int face) {
        this.face = face;
    }

    public int getFace() {
        return face;
    }

    public int getMove() {
        return move;
    }
}
