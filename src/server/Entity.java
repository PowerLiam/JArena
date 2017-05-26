package server;

import global.Constants;
import global.Position;
import transferable.Volition;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Entity implements Serializable {
    public Position currentPosition;
    public int id = -1;
    protected int facing;
    protected int facingVolition;
    protected int health;
    protected boolean alive = true;
    protected boolean hasMovementVolition = false;
    protected boolean hasShootingVolition = false;
    protected boolean hasFacingVolition = false;
    protected boolean isPlayer;
    protected boolean isWall = false;
    protected transient ArrayList<EntityActionListener> listeners = new ArrayList<>();

    public abstract void shoot();

    public synchronized boolean hasMovementVolition() {
        return hasMovementVolition;
    }

    public synchronized void setMovementVolition() {
        this.hasMovementVolition = true;
    }

    public synchronized boolean hasShootingVolition() {
        return hasShootingVolition;
    }

    public synchronized void setShootingVolition() {
        this.hasShootingVolition = true;
    }

    public synchronized boolean hasFacingVolition() {
        return hasFacingVolition;
    }

    public synchronized void setFacingVolition(int toFace) {
        this.hasFacingVolition = true;
        this.facingVolition = toFace;
    }

    public synchronized boolean isWall() {
        return isWall;
    }

    public synchronized boolean isPlayer() {
        return isPlayer;
    }

    public synchronized void setVolition(Volition v) {
        this.hasFacingVolition = v.isFacingVolition();
        this.hasMovementVolition = v.isMovementVolition();
        this.hasShootingVolition = v.isShootingVolition();
        this.facingVolition = v.getFacingVolition();
    }

    public synchronized void setId(int id) {
        this.id = id;
    }

    public synchronized Position getPosition() {
        return currentPosition;
    }

    public synchronized void setPosition(Position p) {
        currentPosition.setX(p.getX());
        currentPosition.setY(p.getY());
    }

    public synchronized void addActionListener(EntityActionListener x) {
        listeners.add(x);
    }

    public synchronized void takeDamage() {
        health--;
        if (health < 1) die();
    }

    public synchronized int facing() {
        return facing;
    }

    public synchronized void changeDirection(int facingDirection) {
        facing = facingDirection;
    }

    public synchronized int getHealth() {
        return health;
    }

    public synchronized void setHealth(int health) {
        this.health = health;
    }

    public synchronized boolean fulfillVolition() {
        //System.out.print("Entity is ");
        if (!alive) this.die();
        else {
            //Volition Priority: Face, Move, Shoot. If ever set to multiple, accept in order of priority. (This should never happen)
            if (this.hasFacingVolition()) {
                this.facing = facingVolition;
                //Reset back to default
                this.facingVolition = -1; //This should NEVER be accessed when its value is -1, a check for hasFacingVolition() should precede getter of this value
                this.hasFacingVolition = false;
                // System.out.println("changing facing volition to " + this.facing + ".");
                return true;
            } else if (this.hasMovementVolition()) {
                this.move();
                //Reset back to default
                this.hasMovementVolition = false;
                return true;
            } else if (this.hasShootingVolition()) {
                this.shoot();
                //Reset back to default
                this.hasShootingVolition = false;
                //System.out.println("changing shooting volition.");
                return true;
            } else {
                //System.out.println("not changing.");
                return false;
            }
        }
        return false;
    }

    public synchronized void aboutFace() {
        switch (facing) {
            case (Constants.FACING_NORTH):
                facing = Constants.FACING_SOUTH;
                break;
            case (Constants.FACING_SOUTH):
                facing = Constants.FACING_NORTH;
                break;
            case (Constants.FACING_EAST):
                facing = Constants.FACING_WEST;
                break;
            case (Constants.FACING_WEST):
                facing = Constants.FACING_EAST;
                break;
        }
    }

    public synchronized void move() {
        switch (facing) {
            case (Constants.FACING_NORTH):
                if (currentPosition.getY() + 1 <= Constants.BOUNDARY_Y)
                    currentPosition.setY(currentPosition.getY() + 1);
                break;
            case (Constants.FACING_SOUTH):
                if (currentPosition.getY() - 1 >= 0)
                    currentPosition.setY(currentPosition.getY() - 1);
                break;
            case (Constants.FACING_EAST):
                if (currentPosition.getX() + 1 <= Constants.BOUNDARY_X)
                    currentPosition.setX(currentPosition.getX() + 1);
                break;
            case (Constants.FACING_WEST):
                if (currentPosition.getX() - 1 >= 0)
                    currentPosition.setX(currentPosition.getX() - 1);
                break;
        }
        //System.out.println("changing position: " + currentPosition.getX() + "," + currentPosition.getY());
    }

    protected void die() {
        alive = false;
        for (EntityActionListener x : listeners) x.die(this);
    }

    @Override
    public synchronized String toString() {
        String type;
        if (isPlayer) type = "Player";
        else if (isWall) type = "Wall";
        else type = "Bullet";
        return ("E[ Position: (" + currentPosition.getX() + "," + currentPosition.getY() + ") Type: " + type + "]");
    }

    public synchronized void setCurrentPosition(Position currentPosition) {
        this.currentPosition = currentPosition;
    }

    public synchronized int getFacing() {
        return facing;
    }

    public synchronized void setFacing(int facing) {
        this.facing = facing;
    }

    public synchronized boolean isAlive() {
        return alive;
    }

    public synchronized void setAlive(boolean alive) {
        this.alive = alive;
    }

    public synchronized boolean isHasMovementVolition() {
        return hasMovementVolition;
    }

    public synchronized void setHasMovementVolition(boolean hasMovementVolition) {
        this.hasMovementVolition = hasMovementVolition;
    }

    public synchronized boolean isHasShootingVolition() {
        return hasShootingVolition;
    }

    public synchronized void setHasShootingVolition(boolean hasShootingVolition) {
        this.hasShootingVolition = hasShootingVolition;
    }

    public synchronized boolean isHasFacingVolition() {
        return hasFacingVolition;
    }

    public synchronized void setHasFacingVolition(boolean hasFacingVolition) {
        this.hasFacingVolition = hasFacingVolition;
    }
}