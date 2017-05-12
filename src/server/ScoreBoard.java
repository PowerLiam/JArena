package server;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.Collections;

public class ScoreBoard extends JFrame implements ServerListener{
    JPanel monitor;
    JButton stateChanger;
    JScrollPane scrollPane;
    JTable leaderBoard;
    Server myServer;
    Thread runningServer;

    public static void main(String[] args) throws IOException {
        new ScoreBoard();
    }

    public ScoreBoard() throws IOException {
        super("Game Leader Board");
        setContentPane(monitor);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension(700,500));
        setVisible(true);

        myServer = new Server();
        myServer.addListener(this);
        runningServer = new Thread(myServer);
        runningServer.start();


        stateChanger.addActionListener(e -> {
            myServer.isQueueing = false;
            stateChanger.setText("End Game");
            stateChanger.removeActionListener(stateChanger.getActionListeners()[0]);
            stateChanger.addActionListener(f -> {
                myServer.run = false;
            });
        });
    }

    public void updateScoreBoard(){
        DefaultTableModel model = new DefaultTableModel(myServer.allClients.size(), 3){
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
        Object[][] data = new Object[myServer.allClients.size()][2];
        Collections.sort(myServer.allClients);
        for(int i = 0; i < myServer.allClients.size(); i++){
            leaderBoard.setValueAt(myServer.allClients.get(i).getMyPlayer().id, i, 0);
            leaderBoard.setValueAt(myServer.allClients.get(i).clientInformation.getName(), i, 1);
            leaderBoard.setValueAt(myServer.allClients.get(i).getMyPlayer().numberOfKills, i, 2);
        }
    }
}
