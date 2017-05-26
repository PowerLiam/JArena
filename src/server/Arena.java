package server;
import global.*;
import transferable.Update;

import java.io.Serializable;
import java.util.ArrayList;

public class Arena implements EntityActionListener, Serializable{
    public String title = "Java Battle Arena";
    protected ArrayList<Entity> entities = new ArrayList<>(); //Maintained to inform client renders
    protected ArrayList<Player> players = new ArrayList<>(); //Maintained to inform scoreboard
    protected ArrayList<Bullet> bullets = new ArrayList<>(); //Maintained to inform scoreboard
    Update sendToClients;
    public int totalPlayers = 0;
    protected int livingPlayers = 0;
    Server running;
    static int id = 0;

    final Object lock = new Object();


    public Arena(String title, Server running) {
        this.running = running;
        this.title = title;
    }

    public void add(Entity entity){
        synchronized (lock) {
            entity.addActionListener(this);
            entities.add(entity);
            if (entity.isPlayer) {
                players.add((Player) entity);
                totalPlayers++;
                livingPlayers++;
            } else if (entity.isWall()) {
                //TODO: See if this is necessary
            } else {
                bullets.add((Bullet) entity);
            }
        }
    }


    public boolean cycle() { //Returning true signals that the game is over
        double sysNanoTimeStart = System.currentTimeMillis();
        synchronized (lock) {
               // for (Entity e : entities) {
                //    e.fulfillVolition();
               // }
            for(int i = 0; i < entities.size(); i++){
                //System.out.println("Fulfilling volition of " + i + ", " + entities.get(i).toString());
                entities.get(i).fulfillVolition();
            }
                validateEntities();
                refreshSendables();
        }
        double sysNanoTimeEnd = System.currentTimeMillis();
        if(Math.abs(sysNanoTimeEnd - sysNanoTimeStart) / 1000000 < 100){
            try {
                Thread.sleep( 100 - (int)(Math.abs(sysNanoTimeEnd - sysNanoTimeStart) / 1000000));
            } catch (InterruptedException e) {
                System.err.println("done waiting");
            }
        }
        running.updateAllClientListeners(sendToClients);
        if(players.size() == 1){
            return true;
        }
        return false;
    }

    public void refreshSendables(){
        sendToClients = new Update(entities);
    }

    public int getId(){
        id++;
        return id;

    }

    public void validateEntities(){ //To be called after all entities have fulfilled volition
        //Evaluates all damage
        ArrayList<Player> playersToTakeDamage = new ArrayList<>();
        ArrayList<Bullet> bulletsToTakeDamage = new ArrayList<>();
        ArrayList<Player> killTracking;
        for(Bullet b : bullets){
            killTracking = new ArrayList<>();
            boolean gotAHit = false;
            for(Player p : players){
                if(b.getPosition().equals(p.getPosition())){
                    gotAHit = true;
                    playersToTakeDamage.add(p);
                    killTracking.add(p);
                }
            }
            if(gotAHit){
                bulletsToTakeDamage.add(b);
                for(Player p : killTracking){
                    if(p.getHealth() == 1) b.kill();
                }
            }
        }

        for(Player toHurt : playersToTakeDamage){
            toHurt.takeDamage();
        }
        for(Bullet toHurt: bulletsToTakeDamage){
            toHurt.takeDamage();
        }

        //Damage has been evaluated, let's resolve multiple entities in the same position
        while(!playersAreValidated()) {
            ArrayList<Position> playerPositions = new ArrayList<>();
            for (Player p : players) {
                if (!playerPositions.contains(p.getPosition())) {
                    playerPositions.add(p.getPosition());
                }
            }
            for(Position pos : playerPositions){
                int pcount = 0;
                ArrayList <Player> inCurSquare = new ArrayList<Player>();
                for(Player p : players){
                    if(p.getPosition().equals(pos)){
                        inCurSquare.add(p);
                        pcount++;
                    }
                }
                if(pcount > 1) {
                    //This checks if there was more than one entity in that position
                    if(inCurSquare.get(0).getFacing() == inCurSquare.get(1).getFacing()){
                        inCurSquare.get(0).aboutFace();
                    }
                    for(Player p : inCurSquare){  //Bounce the players
                        p.aboutFace();
                        p.move();
                        p.aboutFace();
                    }
                }
            }
        }
    }

    public boolean playersAreValidated(){
        ArrayList<Position> playerPositions = new ArrayList<>();
        for(Player p : players){
            if(!playerPositions.contains(p.getPosition())){
                playerPositions.add(p.getPosition());
            }
        }
        for(Position pos : playerPositions){
            int pcount = 0;
            for(Player p : players){
                if(p.getPosition().equals(pos)){
                    pcount++;
                }
            }
            if(pcount > 1) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void die(Entity toMourn) {
        synchronized (lock) {
            entities.remove(toMourn);
            if (toMourn.isPlayer) {
                players.remove(toMourn);
            }
            if (!toMourn.isPlayer) {
                bullets.remove(toMourn);
            }
        }

    }

    @Override
    public void shotBullet(Bullet b) {
        synchronized (lock){
            b.addActionListener(this);
            entities.add(b);
            bullets.add(b);
        }
    }
}
