package server;
import global.Constants;
import transferable.Update;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable{
    private JList leaderBoard;
    private JPanel panel1;
    private JProgressBar gameProgress;
    private JButton startStopButton;
    private JTextField commandField;
    private Arena myGame;
    private ServerSocket update, volition;
    private int updatePort = Constants.UPDATE_PORT;
    private int volitionPort = Constants.VOLITION_PORT;
    private boolean isQueueing; //TODO: Modify from a Button

    public ArrayList<ClientListener> allClients = new ArrayList<>();

    public Server() throws IOException {
        volition = new ServerSocket(volitionPort);
        update = new ServerSocket(updatePort);
        myGame = new Arena("Java Battle Arena", this);
        isQueueing = true;
        this.run();
    }

    @Override
    public void run() {
        while(isQueueing){
            try{
                Socket updater = update.accept();
                Socket volitioner = volition.accept();
                ClientListener pending = new ClientListener(updater, volitioner, myGame);
                allClients.add(pending);
            } catch (IOException | ClassNotFoundException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }
        //TODO: Cycle Arena
        //TODO: Update the ScoreBoard with Players' kill counts
        //TODO: Check for players.size() == 1 to end the game
    }

    public void updateAllClientListeners(Update u){
        for(ClientListener listener : allClients){
            listener.sendClientUpdate(u);
        }
    }

}
