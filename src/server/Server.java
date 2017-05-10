package server;
import global.Constants;
import transferable.ClientInformation;
import transferable.Update;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
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

    public static void main(String[] args) throws IOException {
        Server run = new Server();
        run.run(); //Not a thread
    }

    public Server() throws IOException {
        volition = new ServerSocket(volitionPort);
        update = new ServerSocket(updatePort);
        myGame = new Arena("Java Battle Arena", this);
        isQueueing = true;
    }

    @Override
    public void run() {

        while(isQueueing){
            try{
                Socket updater = update.accept();
                ClientInformation pendingInfo = (ClientInformation) new ObjectInputStream(updater.getInputStream()).readObject();
                System.out.println("Pending: updat");
                Socket volitioner = volition.accept();
                System.out.println("Pending:  vol");
                ClientListener pending = new ClientListener(updater, volitioner, myGame, pendingInfo);
                allClients.add(pending);
                System.out.println("Pending: added");
            } catch (IOException | ClassNotFoundException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }
        while(myGame.players.size() > 1){
            myGame.cycle();
            //TODO: Update the ScoreBoard with Players' kill counts
        }

        //TODO: Mark Winner on Scoreboard
    }

    public void updateAllClientListeners(Update u){
        for(ClientListener listener : allClients){
            listener.sendClientUpdate(u);
        }
    }

}
