package Transferable;
import Server.Entity;
import java.io.Serializable;
import java.util.ArrayList;

public class Update implements Serializable{
    private ArrayList<Entity> entities;

    public Update(ArrayList<Entity> entities){
        this.entities = entities;
    }

    public ArrayList<Entity> getEntities(){
        return entities;
    }
}
