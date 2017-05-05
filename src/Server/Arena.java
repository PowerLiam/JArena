package Server;
import Global.Entity;
import Global.EntityActionListener;
import Transferable.Position;

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
        map.get(position.x).get(position.y).add(entity);
        if (entity.getClass().getSimpleName().equals("Player")) {
            totalPlayers++;
            livingPlayers++;
        }
    }

    public void cycle() {
        boolean needsToTakeDamage;
        for (ArrayList<ArrayList<Entity>> x : map) {
            for (ArrayList<Entity> y : x) {
                needsToTakeDamage = containsBothObjectsOfType(y, "Person", "Bullet");
                for (Entity z : y) {
                    if(needsToTakeDamage) z.takeDamage(); //Will trigger die() appropriately
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
    public void die() {
        //TODO: Inform clients that this entity can be removed from render
    }

    @Override
    public void move() {
        //TODO: Inform clients that this entity has moved
    }
}
