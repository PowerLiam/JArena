package server;
import global.Constants;
import global.Position;
import transferable.ClientInformation;
import transferable.Update;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server extends JFrame implements Runnable{
    private Arena myGame;
    private ServerSocket update, volition;
    private int updatePort = Constants.UPDATE_PORT;
    private int volitionPort = Constants.VOLITION_PORT;
    private boolean isQueueing;
    private boolean run = true;

    public ArrayList<ClientListener> allClients = new ArrayList<>();
    private JTable leaderBoard;
    private JPanel monitor;
    private JButton stateChanger;
    private JScrollPane scrollPane;

    public static void main(String[] args) throws IOException {
        Server run = new Server();
        run.run(); //Not a thread
    }

    public Server() throws IOException {
        super("Java Battle Arena");
        myGame = new Arena("Java Battle Arena", this);
        volition = new ServerSocket(volitionPort);
        update = new ServerSocket(updatePort);
        isQueueing = true;

        setContentPane(monitor);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension(700,500));
        setVisible(true);

        stateChanger.addActionListener(e -> {
           isQueueing = false;
           stateChanger.setText("End Game");
           stateChanger.removeActionListener(stateChanger.getActionListeners()[0]);
           stateChanger.addActionListener(f -> {
               run = false;
           });
        });
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
                updateScoreBoard();

            } catch (IOException | ClassNotFoundException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println("Stopped Queueing.");
        while(myGame.players.size() > 1 && run){
            boolean gameEnded = myGame.cycle();
            updateScoreBoard();
            if(gameEnded) run = false;
        }
        stateChanger.setText("WINNER: " + myGame.players.get(0).myInfo.getName());
    }

    public void updateAllClientListeners(Update u){
        for(ClientListener listener : allClients){
            listener.sendClientUpdate(u);
        }
    }

    private void assignStartingPosition(Player next){ //For now, starting position is random.
        next.currentPosition = new Position((int) (Math.random() * 201), (int) (Math.random() * 201));
    }

    private void updateScoreBoard(){
        DefaultTableModel model = new DefaultTableModel(allClients.size(), 3){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }

            @Override
            public String getColumnName(int column){
                switch(column){
                    case 0: return "ID";
                    case 1: return "Player";
                    case 2: return "Kills";
                }
                return null;
            }
        };
        leaderBoard.setModel(model);
        Object[][] data = new Object[allClients.size()][2];
        Collections.sort(allClients);
        for(int i = 0; i < allClients.size(); i++){
            leaderBoard.setValueAt(allClients.get(i).getMyPlayer().id, i, 0);
            leaderBoard.setValueAt(allClients.get(i).clientInformation.getName(), i, 1);
            leaderBoard.setValueAt(allClients.get(i).getMyPlayer().numberOfKills, i, 2);
        }
    }
}
