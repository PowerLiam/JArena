package Server;
import Global.*;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class Arena implements EntityActionListener{
    public String title = "Java Battle Arena";
    protected ArrayList<ArrayList<ArrayList<Entity>>> map = new ArrayList<>();
    /*
    The map has x and y coordinates for length and width of the ArrayList.
    The third, z dimension is for depth. This is only required for multiple entities occupying a coordinate.
     */
    protected ArrayList<Entity> entities = new ArrayList<>(); //Maintained to inform client renders
    protected ArrayList<Player> players = new ArrayList<>(); //Maintained to inform scoreboard
    public int totalPlayers = 0;
    protected int livingPlayers = 0;
    public int length = 25;
    public int width = 25;

    public Arena() {
        //Construct an Arena
    }

    public void add(Entity entity, Position position) {
        if (position.x > length || position.y > width) throw new InvalidParameterException("Outside of Arena.");
        entity.addActionListener(this);
        entities.add(entity);
        map.get(position.x).get(position.y).add(entity);
        if(entity.isPlayer) {
            players.add((Player)entity);
            totalPlayers++;
            livingPlayers++;
        }
    }

    public void cycle() {
        boolean needsToTakeDamage;
        int bulletIndex = -1;
        Bullet damageInflictor;
        for (ArrayList<ArrayList<Entity>> x : map) {
            for (ArrayList<Entity> y : x) {
                needsToTakeDamage = containsBothObjectsOfType(y, "Person", "Bullet");
                if(needsToTakeDamage) {// Find the bullet so we can report it as the damage source
                    for(int i = 0; i < y.size(); i++){ //We must find the bullet first, since a player's death removes it from the map entirely
                        if(!y.get(i).isPlayer){ //If it's not a player, it's a bullet
                            bulletIndex = i;
                            break; //We want the FIRST bullet in the list, in the case of two bullets in the same spot (that seems fair...?)
                        }
                    }
                    damageInflictor = (Bullet) y.get(bulletIndex);
                    for (Entity z : y) {
                        if (z.getHealth() - 1 == 0 && z.isPlayer) damageInflictor.kill(); //If our next hit is fatal to any player, tell the bullet it killed someone
                        z.takeDamage(); //Will trigger die() appropriately
                    }
                }
                for(Entity z : y) {
                    z.fulfillVolition(); //Will trigger move() appropriately
                }
            }
        }
    }

    private static <E> boolean containsObjectOfType(List<E> list, String simpleName){
        for (E x: list){
            if (x.getClass().getSimpleName().equals(simpleName)){
                return true;
            }
        }
        return false;
    }

    private static <E> boolean containsBothObjectsOfType(List<E> list, String simpleNameOne, String simpleNameTwo){
        boolean containsOne = false;
        boolean containsTwo = false;
        for (E x: list){
            if (x.getClass().getSimpleName().equals(simpleNameOne)){
                containsOne = true;
            }
            if(x.getClass().getSimpleName().equals(simpleNameTwo)){
                containsTwo = true;
            }
        }
        return (containsOne && containsTwo);
    }

    @Override
    public void die(Entity toMourn) {
        entities.remove(toMourn);
        map.get(toMourn.getPosition().x).get(toMourn.getPosition().y).remove(toMourn);
        if(toMourn.isPlayer) players.remove(toMourn);
        //TODO: Inform clients that this entity can be removed from render
    }

    @Override
    public void move(Entity toMove, Position newPosition) {
        //TODO: Inform clients that this entity has moved
    }
}
