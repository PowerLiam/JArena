package server;
import global.Constants;
import global.Position;
import transferable.ClientInformation;
import transferable.Update;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
                ObjectInputStream pendingInputStream = new ObjectInputStream(updater.getInputStream());
                ObjectOutputStream pendingOutputStream = new ObjectOutputStream(updater.getOutputStream());
                pendingOutputStream.flush();
                ClientInformation pendingInfo = (ClientInformation) pendingInputStream.readObject();
                Socket volitioner = volition.accept();
                ClientListener pending = new ClientListener(pendingInputStream, pendingOutputStream, volitioner, myGame, pendingInfo);
                assignStartingPosition(pending.getMyPlayer());
                allClients.add(pending);
                System.out.println("Added Player: " + pendingInfo.getName() + " ID " + pending.getMyPlayer().id);
            } catch (IOException | ClassNotFoundException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }
        while(myGame.players.size() > 1){
            boolean gameEnded = myGame.cycle();
            //TODO: Update the ScoreBoard with Players' kill counts
            if(gameEnded) break;
        }
        System.out.print("WINNER: " + myGame.players.get(0).myInfo.getName());
        //TODO: Mark Winner on Scoreboard
    }

    public void updateAllClientListeners(Update u){
        for(ClientListener listener : allClients){
            listener.sendClientUpdate(u);
        }
    }

    private void assignStartingPosition(Player next){ //For now, starting position is random.
        next.currentPosition = new Position((int) (Math.random() * 201), (int) (Math.random() * 201));
    }
}
