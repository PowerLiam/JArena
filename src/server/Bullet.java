package server;

import global.Constants;
import global.Position;

import java.io.Serializable;
import java.util.ArrayList;

public class Bullet extends Entity implements KillListener, Serializable {
    private Player firedBy;
    private ArrayList<KillListener> killListeners = new ArrayList<>();
    private boolean willDieNextCycle = false;

    public Bullet(Player firedBy, Position currentPosition, int facing, int health) {
        if (currentPosition.getX() <= 0 || currentPosition.getY() <= 0)
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
        if (currentPosition.getX() < 0 || currentPosition.getY() < 0)
            throw new IllegalArgumentException("Bullet Position cannot be Negative");
        if (health < 0) throw new IllegalArgumentException("Health must be positive and greater than zero.");
        this.firedBy = firedBy;
        this.health = 1;
        this.currentPosition = currentPosition;
        this.facing = facing;
        this.isPlayer = false;
    }

    public void addKillListener(KillListener listener) {
        killListeners.add(listener);
    }

    @Override
    public void move() { //Causes bullets to die when they reach the edge of the Arena
        super.move();
        if ((currentPosition.getX() == Constants.BOUNDARY_X && this.getFacing() == Constants.FACING_EAST)
                || (currentPosition.getY() == Constants.BOUNDARY_Y && this.getFacing() == Constants.FACING_NORTH)
                || (currentPosition.getX() == 0 && this.getFacing() == Constants.FACING_WEST)
                || (currentPosition.getY() == 0 && this.getFacing() == Constants.FACING_SOUTH)) {
            if (willDieNextCycle) {
                die();
            } else willDieNextCycle = true;
        }
    }

    @Override
    public void kill() {
        //I've killed an opponent! Let's tell my player.
        for (KillListener x : killListeners) x.kill();
    }

    @Override
    public void shoot() {
        throw new IllegalStateException("A bullet cannot shoot itself! Call this from a Player instance.");
    }

    @Override
    public boolean hasMovementVolition() {
        return true; //Bullets don't wait for anyone!
    }

    @Override
    public boolean hasShootingVolition() {
        return false; //Avoid breaking the universe
    }

    @Override
    public boolean hasFacingVolition() {
        return false; //Bullets are not conscious (yet).
    }
}

