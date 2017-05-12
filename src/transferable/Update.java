package transferable;
import server.Entity;
import server.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class Update implements Serializable{
    private ArrayList<Entity> entities;
    private Player player;

    public Update(ArrayList<Entity> entities){
        this.entities = entities;
    }

    public synchronized void addPlayer(Player player){
        this.player = player;
    }
    public synchronized Player getPlayer(){ return player; }
    public synchronized ArrayList<Entity> getEntities(){
        return entities;
    }
}
