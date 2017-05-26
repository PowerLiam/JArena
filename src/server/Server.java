package server;
import global.Constants;
import transferable.ClientInformation;
import transferable.ServerInformation;
import transferable.Update;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class Server implements Runnable{
    Arena myGame;
    int spacePerPlayer = 25;
    private ServerSocket update, volition;
    private int updatePort = Constants.UPDATE_PORT;
    private int volitionPort = Constants.VOLITION_PORT;
    public boolean isQueueing;
    public boolean run = true;
    Socket updater;
    private ArrayList<ServerListener> listeners = new ArrayList<>();

    public ArrayList<ClientListener> allClients = new ArrayList<>();

    public Server() throws IOException {
        myGame = new Arena("Java Battle Arena", this);
        volition = new ServerSocket(volitionPort);
        update = new ServerSocket(updatePort);
        update.setSoTimeout(2000);
        isQueueing = true;
    }

    @Override
    public void run() {
        while(isQueueing){
            try{
                updater = update.accept();
                if(updater != null) {
                    ObjectInputStream pendingInputStream = new ObjectInputStream(updater.getInputStream());
                    ObjectOutputStream pendingOutputStream = new ObjectOutputStream(updater.getOutputStream());
                    pendingOutputStream.flush();
                    ClientInformation pendingInfo = (ClientInformation) pendingInputStream.readObject();
                    Socket volitioner = volition.accept();
                    ClientListener pending = new ClientListener(pendingInputStream, pendingOutputStream, volitioner, myGame, pendingInfo);
                    allClients.add(pending);
                    //Dynamically change arena size based on number of connected clients
                    int numConnected = allClients.size();
                    Constants.BOUNDARY_X = numConnected * spacePerPlayer;
                    Constants.BOUNDARY_Y = numConnected * spacePerPlayer;

                    System.out.println("Added Player: " + pendingInfo.getName() + " ID " + pending.getMyPlayer().id);
                    updateScoreBoard();
                }

            } catch(SocketTimeoutException e){
                //Expected exception, discard.
            }
            catch (IOException | ClassNotFoundException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }

        for(ClientListener cur : allClients){
            assignStartingPosition(cur.getMyPlayer());
            try {
                cur.sendServerInfo(new ServerInformation(Constants.BOUNDARY_X, Constants.BOUNDARY_Y));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        while(myGame.players.size() > 1 && run){
            boolean gameEnded = myGame.cycle();
            updateScoreBoard();
            if(gameEnded) run = false;
        }
        for(ServerListener s : listeners) s.endGame();
        if(myGame.players.size() == 0){
            updateAllClientListeners(new Update(true, "Nobody", 0));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            updateScoreBoard();
            JOptionPane.showMessageDialog(null, "The game ended in a tie.");
        }
        updateAllClientListeners(new Update(true, myGame.players.get(0).myInfo.getName(), myGame.players.get(0).numberOfKills));
        if(myGame.players.size() > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            updateScoreBoard();
            JOptionPane.showMessageDialog(null, myGame.players.get(0).myInfo.getName() + " won the game with " + myGame.players.get(0).numberOfKills + " kills.");
        }
    }

    public void updateAllClientListeners(Update u){
        for(ClientListener listener : allClients){
            listener.sendClientUpdate(u);
        }
    }

    private void assignStartingPosition(Player next){ //For now, starting position is random.
        next.currentPosition.setX((int) (Math.random() * (Constants.BOUNDARY_X + 1)));
        next.currentPosition.setY((int) (Math.random() * (Constants.BOUNDARY_Y + 1)));
    }

    public void addListener(ServerListener s){
        listeners.add(s);
    }

    private void updateScoreBoard(){
        for(ServerListener s : listeners){
            s.updateScoreBoard();
        }
    }
}
