package Transferable;
import Global.Bullet;
import Global.Entity;
import Global.Player;
import java.io.Serializable;
import java.util.ArrayList;

public class Update implements Serializable{
    public ArrayList<Entity> entities;
    public ArrayList<Player> players;
    public ArrayList<Bullet> bullets;

}
