package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import Transferable.*;

public class ClientListener implements Runnable{
    private Socket socket;
    private String name;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private ClientInformation clientInformation;
    private Volition volition;

    public ClientListener(Socket socket) throws IOException, ClassNotFoundException {
        this.socket = socket;
        try {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.outputStream.flush(); //Necessary to avoid 'chicken or egg' situation
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.println("Client " + socket.getInetAddress() + " :" + e.getMessage());
            e.printStackTrace();
        }
        clientInformation = (ClientInformation) inputStream.readObject();
        this.name = clientInformation.getName();
        this.run();
    }

    @Override
    public void run() {
        while(true){
            //TODO: Thread for both sending updates and receiving Volition
        }
    }
}
