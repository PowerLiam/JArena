package Server;
import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{
    private JList leaderBoard;
    private JPanel panel1;
    private JProgressBar gameProgress;
    private JButton startStopButton;
    private JTextField commandField;
    private Arena myGame = new Arena("Java Battle Arena");

    private ServerSocket server;
    private int port = 9091;

    public Server() throws IOException {
        server = new ServerSocket(port);
        this.run();
    }

    @Override
    public void run() {
        while(true){
            try{
                Socket client = server.accept();
                ClientListener pending = new ClientListener(client);
                Thread starting = new Thread(pending);
                starting.start();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
