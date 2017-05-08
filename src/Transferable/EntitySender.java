package Transferable;

import Global.Bullet;
import Global.Entity;
import Global.Player;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by liamn on 5/7/2017.
 */
public class EntitySender implements Serializable{
    public ArrayList<Entity> entities;
    public ArrayList<Player> players;
    public ArrayList<Bullet> bullets;

}
