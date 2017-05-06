package Global;

import Server.Arena;

import java.util.ArrayList;

public class Bullet extends Entity implements KillListener{
    private Player firedBy;
    private ArrayList<KillListener> killListeners = new ArrayList<>();
    public boolean isPlayer = false;
    public final boolean hasVolition = true; //Can't stop, won't stop!   TODO: Does this work? The superclass attempts to modify the value of its hasVolition variable, and this will be checked in an Entity context.

    public Bullet(Player firedBy, Position currentPosition, Arena container, int facing, int health) {
        if (currentPosition.x <= 0 || currentPosition.y <= 0)
            throw new IllegalArgumentException("Position must be positive.");
        if (health < 0) throw new IllegalArgumentException("Health must be positive and greater than zero.");
        this.firedBy = firedBy;
        this.currentPosition = currentPosition;
        this.facing = facing;
        this.health = health;
        this.container = container;
    }

    public Bullet(Player firedBy, Arena container) {
        if (health < 0) throw new IllegalArgumentException("Health must be positive and greater than zero.");
        this.firedBy = firedBy;
        this.currentPosition = firedBy.getPosition();
        this.health = 1;
        this.facing = Constants.FACING_NORTH;
        this.container = container;
    }

    public Bullet(Player firedBy, int health, Arena container) {
        if (health < 0) throw new IllegalArgumentException("Health must be positive and greater than zero.");
        this.firedBy = firedBy;
        this.currentPosition = firedBy.getPosition();
        this.health = health;
        this.facing = Constants.FACING_NORTH;
        this.container = container;
    }

    public Bullet(Player firedBy, Position currentPosition, Arena container, int facing) {
        if (currentPosition.x <= 0 || currentPosition.y <= 0)
            throw new IllegalArgumentException("Position must be positive.");
        if (health < 0) throw new IllegalArgumentException("Health must be positive and greater than zero.");
        this.firedBy = firedBy;
        this.health = 1;
        this.currentPosition = currentPosition;
        this.facing = facing;
        this.container = container;
    }

    public void addKillListener(KillListener listener){
        killListeners.add(listener);
    }

    @Override
    public void move(){ //Causes bullets to die when they reach the edge of the Arena
        Position previous = currentPosition;
        super.move();
        if(currentPosition.equals(previous)) this.die();
    }

    @Override
    public void kill() {
        //I've killed an opponent! Let's tell my player.
        for(KillListener x : killListeners)x.kill();
    }
}

