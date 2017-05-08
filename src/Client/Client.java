package Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Server.ClientListener;
import Transferable.*;
import Global.*;

public class Client {
    private String host = "127.0.0.1";
    private int port = 9091;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Thread listener;
    private String name;
    private Socket socket;
    private ClientInformation me;
    private Update latest;
    private Volition volition;
    private boolean hasVolition;

    public static void main(String args[]) throws IOException {
        Client myClient = new Client();
    }

    public Client() throws IOException {
        socket = new Socket(host, port);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.flush();
        inputStream = new ObjectInputStream(socket.getInputStream());

        me = new ClientInformation(name, new Position(0,0), false);
        try{
            outputStream.writeObject(me);
        } catch (IOException e) {
            e.printStackTrace();
        }

        volition = new Volition(Constants.FACING_NORTH);
    }

    public void renderBoard(){
        //TODO: Add Graphics Render
    }

    //TODO: Thread for both Volition Sending and Update Receipt
}
