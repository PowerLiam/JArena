package Global;

import Server.Arena;

import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * Created by liamn on 5/6/2017.
 */
public class Map {
    Arena parent;
    public int xlength;
    public int ylength;
    protected ArrayList<ArrayList<ArrayList<Entity>>> map;

    public Map(int x, int y, Arena arena){
        parent = arena;
        map = new ArrayList<ArrayList<ArrayList<Entity>>>();
        xlength = x;
        ylength = y;
        for(int i = 0; i < xlength; i++) { //Start out all empty
            for (int j = 0; j < ylength; j++) {
               map.get(i).get(j).add(new Empty(new Position(i, j)));
            }
        }
    }



    public void buildBoard(){ // evaluates new positions of all entities and rebuilds the board
        for(int i = 0; i < xlength; i++) {
            for (int j = 0; j < ylength; j++) {
                Position cur_square = new Position(i, j);
                for(int entity = 0; entity < getSquare(cur_square).size(); entity++){
                    if(getSquare(cur_square).get(entity).getPosition().x != i || getSquare(cur_square).get(entity).getPosition().y != j){ //That entity has moved and no longer matches arraylist
                        getSquare(getSquare(cur_square).get(entity).getPosition()).add(getSquare(cur_square).remove(entity));  //Remove from its square then add to proper square
                        entity --; //compensate for removing entity from this square to put it on correct square
                    }
                }
                add(new Empty(cur_square), cur_square); //Might as well try to add an empty, will only do something if all entities have left that square
            }
        }
    }

    public void add(Entity entity, Position position) {
        if(getSquare(position).size() == 1 && getSquare(position).get(0).empty){ //If there is only an empty, replace it
            getSquare(position).set(0, entity); //replace it if its an empty placeholder
        }
        else if(!entity.empty || getSquare(position).size() == 0) { //as long as im not trying to add an empty, add it to list.... but if there really is nothing then add an empty anyway
            getSquare(position).add(0, entity); //add it as long as im not trying to add an empty
        }

    }

    public ArrayList<ArrayList<ArrayList<Entity>>> getMap(){
        return map;
    }
    public ArrayList<Entity> getSquare(Position p){
        return map.get(p.x).get(p.y);
    }

    public void clearMap(){
        map = new ArrayList<>();
    }

    public int getNumSquares(){
        int count = 0;
        for(int i = 0; i < xlength; i++){
            for(int j = 0; j < ylength; j++){
                count++;
            }
        }
        return count;
    }

    public void resolve(){  //First move all entities, then resolve conflicts...
        buildBoard(); //Moves all entities to their new locations
        while(!isResolved()) { //resolves conflicts
            for(int i = 0; i < xlength; i++){
                for(int j = 0; j < ylength; j++){
                    resolveSquare(getSquare(new Position(i , j)));
                }
            }
        }
        buildBoard(); //moves entities again to final locations now that they have been validated
    }

    public void resolveSquare(ArrayList<Entity> square){
        boolean hurtAll = false;
        for(Entity e : square){
            if(!e.isPlayer){ //Kill all
                hurtAll = true;
            }
        }
        if(hurtAll){
            for(Entity e : square){
                e.takeDamage();
            }
        } else {
            if(square.size() > 1){
                for(Entity e : square){
                    Position init = e.getPosition();
                    e.aboutFace();
                    e.move();
                    e.aboutFace();
                    if (init.equals(e.getPosition())){ //if you get knocked into a wall, you die
                        parent.die(e);
                    }
                }
            }
        }
    }

    public boolean isResolved(){
        boolean resolved = true;
        for(int i = 0; i < xlength; i++){
            for(int j = 0; j < ylength; j++){
                if(getSquare(new Position(i , j)).size() > 1){
                    resolved = false;
                }
            }
        }
        return resolved;
    }
}
