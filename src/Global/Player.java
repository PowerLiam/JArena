package Global;

import Server.Arena;

import java.io.Serializable;

public class Player extends Entity implements KillListener, Serializable{
    public int numberOfKills = 0;

    public Player(Position currentPosition,int facing, int health) {
        if (currentPosition.x <= 0 || currentPosition.y <= 0)
            throw new IllegalArgumentException("Position must be positive.");
        if (health < 0) throw new IllegalArgumentException("Health must be positive and greater than zero.");
        this.currentPosition = currentPosition;
        this.facing = facing;
        this.health = health;
        this.isPlayer = true;
    }

    public Player() {
        if (health < 0) throw new IllegalArgumentException("Health must be positive and greater than zero.");
        this.currentPosition = new Position(0, 0);
        this.health = 5;
        this.facing = Constants.FACING_NORTH;
        this.isPlayer = true;
    }

    public Player(int health) {
        if (health < 0) throw new IllegalArgumentException("Health must be positive and greater than zero.");
        this.currentPosition = new Position(0, 0);
        this.health = health;
        this.facing = Constants.FACING_NORTH;
        this.isPlayer = true;
    }

    public Player(Position currentPosition, int facing) {
        if (currentPosition.x <= 0 || currentPosition.y <= 0)
            throw new IllegalArgumentException("Position must be positive.");
        if (health < 0) throw new IllegalArgumentException("Health must be positive and greater than zero.");
        this.health = 5;
        this.currentPosition = currentPosition;
        this.facing = facing;
        this.isPlayer = true;
    }

    @Override
    public void shoot(){
        int bulletPosX = -1;
        int bulletPosY = -1;
        switch(facing){
            case(Constants.FACING_NORTH) :
                if(currentPosition.y + 1 < Constants.BOUNDARY_Y) {
                    bulletPosY = currentPosition.y + 1;
                    bulletPosX = currentPosition.x;
                }
                break;
            case(Constants.FACING_SOUTH) :
                if(currentPosition.y - 1 >= 0) {
                    bulletPosY = currentPosition.y - 1;
                    bulletPosX = currentPosition.x;
                }
                break;
            case(Constants.FACING_EAST) :
                if(currentPosition.x + 1 < Constants.BOUNDARY_X) {
                    bulletPosY = currentPosition.y;
                    bulletPosX = currentPosition.x + 1;
                }
                break;
            case(Constants.FACING_WEST) :
                if(currentPosition.x - 1 >= 0) {
                    bulletPosY = currentPosition.y;
                    bulletPosX = currentPosition.x - 1;
                }
                break;
        }
        Position bulletPosition = new Position(bulletPosX, bulletPosY);
        Bullet toShoot = new Bullet(this, bulletPosition, facing);
        //Now, we wait for mass destruction!
    }

    @Override
    public void kill() {
        //My bullet made a kill! Let's add to my kill count.
        numberOfKills++;
    }
}