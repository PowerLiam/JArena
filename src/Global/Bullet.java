package Global;

import Server.Arena;

import java.io.Serializable;
import java.util.ArrayList;

public class Bullet extends Entity implements KillListener, Serializable{
    private Player firedBy;
    private ArrayList<KillListener> killListeners = new ArrayList<>();

    public Bullet(Player firedBy, Position currentPosition,int facing, int health) {
        if (currentPosition.x <= 0 || currentPosition.y <= 0)
            throw new IllegalArgumentException("Position must be positive.");
        if (health < 0) throw new IllegalArgumentException("Health must be positive and greater than zero.");
        this.firedBy = firedBy;
        this.currentPosition = currentPosition;
        this.facing = facing;
        this.health = health;
        this.isPlayer = false;
    }

    public Bullet(Player firedBy) {
        if (health < 0) throw new IllegalArgumentException("Health must be positive and greater than zero.");
        this.firedBy = firedBy;
        this.currentPosition = firedBy.getPosition();
        this.health = 1;
        this.facing = Constants.FACING_NORTH;

        this.isPlayer = false;
    }

    public Bullet(Player firedBy, int health) {
        if (health < 0) throw new IllegalArgumentException("Health must be positive and greater than zero.");
        this.firedBy = firedBy;
        this.currentPosition = firedBy.getPosition();
        this.health = health;
        this.facing = Constants.FACING_NORTH;

        this.isPlayer = false;
    }

    public Bullet(Player firedBy, Position currentPosition, int facing) {
        if (currentPosition.x <= 0 || currentPosition.y <= 0)
            throw new IllegalArgumentException("Position must be positive.");
        if (health < 0) throw new IllegalArgumentException("Health must be positive and greater than zero.");
        this.firedBy = firedBy;
        this.health = 1;
        this.currentPosition = currentPosition;
        this.facing = facing;
        this.isPlayer = false;
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

    @Override
    public boolean hasMovementVolition(){
        return true; //Bullets don't wait for anyone!
    }

    @Override
    public boolean hasShootingVolition(){
        return false; //Avoid breaking the universe
    }

    @Override
    public boolean hasFacingVolition(){
        return false; //Bullets are not conscious (yet).
    }
}

