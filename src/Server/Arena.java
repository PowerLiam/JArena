package Server;
import Global.*;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class Arena implements EntityActionListener{
    public String title = "Java Battle Arena";
    public Map map = new Map(1000, 1000, this);
    /*
    The map has x and y coordinates for length and width of the ArrayList.
    The third, z dimension is for depth. This is only required for multiple entities occupying a coordinate.
     */
    protected ArrayList<Entity> entities = new ArrayList<>(); //Maintained to inform client renders
    protected ArrayList<Player> players = new ArrayList<>(); //Maintained to inform scoreboard
    public int totalPlayers = 0;
    protected int livingPlayers = 0;


    public Arena() {
        //Construct an Arena
    }

    public void add(Entity entity, Position position) { //Adds something new to the map, use to initialize locations of players or to add bullets
        entity.addActionListener(this);
        entities.add(entity);
        map.add(entity, position);
        if(entity.isPlayer) {
            players.add((Player)entity);
            totalPlayers++;
            livingPlayers++;
        }
    }

    public void cycle() {
        for(int i = 0; i < map.xlength; i++) {
            for (int j = 0; j < map.ylength; j++) {
                for(Entity e : map.getSquare(new Position(i, j))) {
                    e.fulfillVolition(); //Will trigger move() appropriately
                }
                map.resolve();
            }
        }
    }



    @Override
    public void die(Entity toMourn) {
        entities.remove(toMourn);
        map.getSquare(toMourn.getPosition()).remove(toMourn);
        if(toMourn.isPlayer) players.remove(toMourn);
        //TODO: Inform clients that this entity can be removed from render
    }

    @Override
    public void move(Entity toMove, Position newPosition) {
        //TODO: Inform clients that this entity has moved
    }
}
