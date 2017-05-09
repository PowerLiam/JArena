package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import Transferable.*;

public class ClientListener{
    private Socket updateSocket;
    private String name;
    private ObjectInputStream updateInputStream;
    private ObjectOutputStream updateOutputStream;
    private ClientInformation clientInformation;
    private Volition volition;

    private Thread vol;

    public ClientListener(Socket updateSocket, Socket volitionSocket) throws IOException, ClassNotFoundException {
        this.updateSocket = updateSocket;
        this.updateOutputStream = new ObjectOutputStream(updateSocket.getOutputStream());
        this.updateOutputStream.flush(); //Necessary to avoid 'chicken or egg' situation
        this.updateInputStream = new ObjectInputStream(updateSocket.getInputStream());

        clientInformation = (ClientInformation) updateInputStream.readObject();
        this.name = clientInformation.getName();

        vol = new Thread(new VolitionListener(this, volitionSocket));
        vol.run();
    }

    public void updateVolition(Volition v){ //Updates the SERVER'S copy of the client's sent volition.
        this.volition = v;
        //TODO: Add an event listener to trigger action in the Arena when this happens
    }

    public void sendClientUpdate(Update u){
        try {
            updateOutputStream.writeObject(u);
        } catch (IOException e) {
            System.err.println("Client " + updateSocket.getInetAddress() + " :" + e.getMessage());
            e.printStackTrace();
        }
    }
}
