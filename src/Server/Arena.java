package Server;
import Global.*;
import Transferable.EntitySender;
import java.io.Serializable;
import java.util.ArrayList;

public class Arena implements EntityActionListener, Serializable{
    public String title = "Java Battle Arena";
    /*
    The map has x and y coordinates for length and width of the ArrayList.
    The third, z dimension is for depth. This is only required for multiple entities occupying a coordinate.
     */
    protected ArrayList<Entity> entities = new ArrayList<>(); //Maintained to inform client renders
    protected ArrayList<Player> players = new ArrayList<>(); //Maintained to inform scoreboard
    protected ArrayList<Bullet> bullets = new ArrayList<>(); //Maintained to inform scoreboard
    EntitySender sendToClients;
    public int totalPlayers = 0;
    protected int livingPlayers = 0;


    public Arena(String title) {
        this.title = title;
    }

    public void add(Entity entity, Position position) { //Adds something new to the map, use to initialize locations of players or to add bullets
        entity.addActionListener(this);
        entities.add(entity);
        entity.setPosition(position);
        if(entity.isPlayer) {
            players.add((Player)entity);
            totalPlayers++;
            livingPlayers++;
        }
    }

    public void cycle() {
        //TODO, give cycles a fixed time interval using system time
        for(Entity e : entities){
            e.fulfillVolition();
        }
        validateEntities();
        refreshSendables();
        //TODO serialize and send the EntitySender called sendToClients.
    }

    public void refreshSendables(){
        sendToClients.entities = entities;
        sendToClients.players = players;
        for(Entity e : entities){
            if(!e.isPlayer){
                sendToClients.bullets.add((Bullet) e);
            }
        }
        //Construct an Arena
    }

    public void validateEntities(){ //To be called after all entities have had their volitions
        //Evaluates all damage
        ArrayList<Player> playersToTakeDamage = new ArrayList<>();
        ArrayList<Bullet> bulletsToTakeDamage = new ArrayList<>();
        for(Bullet b : bullets){
            boolean gotAHit = false;
            for(Player p : players){
                if(b.getPosition().equals(p.getPosition())){
                    gotAHit = true;
                    playersToTakeDamage.add(p);
                }
            }
            if(gotAHit){
                bulletsToTakeDamage.add(b);
            }
        }

        for(Player toHurt : playersToTakeDamage){
            toHurt.takeDamage();
        }
        for(Bullet toHurt: bulletsToTakeDamage){
            toHurt.takeDamage();
        }

        //Damage has been evaluated, lets resolve multiple entities in one square
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
                if(pcount > 1) { //This checks if there were more than one entity in that position
                    for(Player p : inCurSquare){ //Bounce the players
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
        entities.remove(toMourn);
        if(toMourn.isPlayer) players.remove(toMourn);
        if(!toMourn.isPlayer) bullets.remove(toMourn);
        //TODO: Inform clients that this entity can be removed from render
    }

    @Override
    public void move(Entity toMove, Position newPosition) {
        //TODO: Inform clients that this entity has moved
    }
}
