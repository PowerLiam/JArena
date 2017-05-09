package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Global.Position;
import Transferable.*;

public class ClientListener{
    private Socket updateSocket;

    private String name;
    private ObjectInputStream updateInputStream;
    private ObjectOutputStream updateOutputStream;
    private ClientInformation clientInformation;
    private Player myPlayer;
    private Thread vol;
    private Arena ParentArena;

    public ClientListener(Socket updateSocket, Socket volitionSocket, Arena Parent) throws IOException, ClassNotFoundException {
        this.updateSocket = updateSocket;
        this.updateOutputStream = new ObjectOutputStream(updateSocket.getOutputStream());
        this.updateOutputStream.flush(); //Necessary to avoid 'chicken or egg' situation
        this.updateInputStream = new ObjectInputStream(updateSocket.getInputStream());
        clientInformation = (ClientInformation) updateInputStream.readObject();
        this.name = clientInformation.getName();

        ParentArena = Parent;
        myPlayer = new Player();
        myPlayer.setId(ParentArena.getId());
        ParentArena.add(myPlayer, new Position(0,0));

        vol = new Thread(new VolitionListener(this, volitionSocket));
        vol.run();
    }

    public void updateVolition(Volition v){ //Updates the SERVER'S copy of the client's sent volition.
        myPlayer.setVolition(v);
    }

    public void sendClientUpdate(Update u){
        try {
            u.addPlayer(this.getMyPlayer());
            updateOutputStream.writeObject(u);
        } catch (IOException e) {
            System.err.println("Client " + updateSocket.getInetAddress() + " :" + e.getMessage());
            e.printStackTrace();
        }
    }

    public Player getMyPlayer(){
        return myPlayer;
    }
}
