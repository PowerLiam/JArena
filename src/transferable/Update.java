package transferable;
import server.Entity;
import server.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class Update implements Serializable{
    private ArrayList<Entity> entities;
    private Player player;
    private boolean gameOver;
    private String winningPlayer;
    private int winningPlayerKills;

    public Update(ArrayList<Entity> entities){
        this.entities = entities;
    }

    public Update(boolean gameOver, String winningPlayer, int winningPlayerKills){
        this.gameOver = gameOver;
        this.winningPlayer = winningPlayer;
        this.winningPlayerKills = winningPlayerKills;
    }

    public void addPlayer(Player player){
        this.player = player;
    }
    public Player getPlayer(){ return player; }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public String getWinningPlayer() {
        return winningPlayer;
    }

    public void setWinningPlayer(String winningPlayer) {
        this.winningPlayer = winningPlayer;
    }

    public int getWinningPlayerKills() {
        return winningPlayerKills;
    }

    public void setWinningPlayerKills(int winningPlayerKills) {
        this.winningPlayerKills = winningPlayerKills;
    }

    public ArrayList<Entity> getEntities(){
        return entities;
    }
}
