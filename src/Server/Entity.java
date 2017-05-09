package server;
import global.Constants;
import global.Position;
import transferable.Volition;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Entity implements Serializable{
    public Position currentPosition;
    public int facing;
    private int facingVolition;
    protected int health;
    public boolean alive = true;
    private boolean hasMovementVolition = false;
    private boolean hasShootingVolition = false;
    private boolean hasFacingVolition = false;
    public boolean isPlayer;
    public int id = -1;

    private transient ArrayList<EntityActionListener> listeners = new ArrayList<>();

    public abstract void shoot();

    public boolean hasMovementVolition() {
        return hasMovementVolition;
    }

    public void setMovementVolition() {
        this.hasMovementVolition = true;
    }

    public boolean hasShootingVolition() {
        return hasShootingVolition;
    }

    public void setShootingVolition() {
        this.hasShootingVolition = true;
    }

    public boolean hasFacingVolition() {
        return hasFacingVolition;
    }

    public void setFacingVolition(int toFace) {
        this.hasFacingVolition = true;
        this.facingVolition = toFace;
    }

    public void setVolition(Volition v){
        this.hasFacingVolition = v.isFacingVolition();
        this.hasMovementVolition = v.isMovementVolition();
        this.hasShootingVolition = v.isShootingVolition();
        this.facingVolition = v.getFacingVolition();
    }

    public void setId(int id){
        this.id = id;
    }

    public boolean isPlayer() {
        return isPlayer;
    }

    public Position getPosition() {
        return currentPosition;
    }

    public void addActionListener(EntityActionListener x){
        listeners.add(x);
    }

    public void takeDamage() {
        health--;
        if(health < 1) die();
    }

    public int facing() {
        return facing;
    }

    public void changeDirection(int facingDirection){
        facing = facingDirection;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }


    public boolean fulfillVolition(){
        if(!alive) throw new IllegalStateException("Dead things have no volition.");
        if(this.hasMovementVolition()){
            this.move();
            this.hasMovementVolition = false;
            return true;
        }
        else if(this.hasShootingVolition()){
            this.shoot();
            return true;
        }
        else if(this.hasFacingVolition()){
            this.facing = facingVolition;
            this.facingVolition = -1; //This should NEVER be accessed when its value is -1, a check for hasFacingVolition() should precede getter of this value
            return true;
        }
        else{
            return false;
        }
    }

    public void setPosition(Position p){
        currentPosition = p;
    }

    public void aboutFace(){
        switch(facing){
            case(Constants.FACING_NORTH) :
                facing = Constants.FACING_SOUTH;
                break;
            case(Constants.FACING_SOUTH) :
                facing = Constants.FACING_NORTH;
                break;
            case(Constants.FACING_EAST) :
                facing = Constants.FACING_WEST;
                break;
            case(Constants.FACING_WEST) :
                facing = Constants.FACING_EAST;
                break;
        }
    }

    public void move() {
        switch(facing){
            case(Constants.FACING_NORTH) :
                if(currentPosition.y + 1 < Constants.BOUNDARY_Y)
                    currentPosition.y++;
                break;
            case(Constants.FACING_SOUTH) :
                if(currentPosition.y - 1 >= 0)
                    currentPosition.y--;
                break;
            case(Constants.FACING_EAST) :
                if(currentPosition.x + 1 < Constants.BOUNDARY_X)
                    currentPosition.x++;
                break;
            case(Constants.FACING_WEST) :
                if(currentPosition.x - 1 >= 0)
                    currentPosition.x--;
                break;
        }
        for(EntityActionListener x : listeners) x.move(this, currentPosition);
    }

    protected void die(){
        alive = false;
        for(EntityActionListener x : listeners) x.die(this);
    }
}