package server;
import global.Constants;
import global.Position;
import transferable.ClientInformation;

import java.io.Serializable;

public class Player extends Entity implements KillListener, Serializable{
    public int numberOfKills = 0;
    ClientInformation myInfo;

    public Player(Position currentPosition, int facing, int health, ClientInformation myInfo) {
        this.myInfo = myInfo;
        if (currentPosition.getX() <= 0 || currentPosition.getY() <= 0)
            throw new IllegalArgumentException("Position must be positive.");
        if (health < 0) throw new IllegalArgumentException("Health must be positive and greater than zero.");
        this.currentPosition = currentPosition;
        this.facing = facing;
        this.health = health;
        this.isPlayer = true;
    }

    public Player(ClientInformation myInfo) {
        this.myInfo = myInfo;
        if (health < 0) throw new IllegalArgumentException("Health must be positive and greater than zero.");
        this.currentPosition = new Position(0, 0);
        this.health = 5;
        this.facing = Constants.FACING_NORTH;
        this.isPlayer = true;
    }

    public Player(int health, ClientInformation myInfo) {
        this.myInfo = myInfo;
        if (health < 0) throw new IllegalArgumentException("Health must be positive and greater than zero.");
        this.currentPosition = new Position(0, 0);
        this.health = health;
        this.facing = Constants.FACING_NORTH;
        this.isPlayer = true;
    }

    public Player(Position currentPosition, int facing, ClientInformation myInfo) {
        this.myInfo = myInfo;
        if (currentPosition.getX() <= 0 || currentPosition.getY() <= 0)
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
                if(currentPosition.getY() + 1 < Constants.BOUNDARY_Y) {
                    bulletPosY = currentPosition.getY();
                    bulletPosX = currentPosition.getX();
                }
                break;
            case(Constants.FACING_SOUTH) :
                if(currentPosition.getY() - 1 >= 0) {
                    bulletPosY = currentPosition.getY();
                    bulletPosX = currentPosition.getX();
                }
                break;
            case(Constants.FACING_EAST) :
                if(currentPosition.getX() + 1 < Constants.BOUNDARY_X) {
                    bulletPosY = currentPosition.getY();
                    bulletPosX = currentPosition.getX();
                }
                break;
            case(Constants.FACING_WEST) :
                if(currentPosition.getX() - 1 >= 0) {
                    bulletPosY = currentPosition.getY();
                    bulletPosX = currentPosition.getX();
                }
                break;
        }
        if(!(bulletPosX == 0 || bulletPosY == 0)) {
            Position bulletPosition = new Position(bulletPosX, bulletPosY);
            Bullet toShoot = new Bullet(this, bulletPosition, facing);
            toShoot.addKillListener(this);
            for (EntityActionListener x : listeners) x.shotBullet(toShoot);
            //Now, we wait for mass destruction!
        }
    }

    @Override
    public void kill() {
        //My bullet made a kill! Let's add to my kill count.
        numberOfKills++;
    }
}