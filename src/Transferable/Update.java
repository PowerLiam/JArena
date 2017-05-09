package Transferable;
import Server.Entity;
import Server.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class Update implements Serializable{
    private ArrayList<Entity> entities;
    private Player player;

    public Update(ArrayList<Entity> entities){
        this.entities = entities;
    }

    public void addPlayer(Player player){
        this.player = player;
    }

    public ArrayList<Entity> getEntities(){
        return entities;
    }
}
