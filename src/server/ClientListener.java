package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import global.Position;
import transferable.*;

public class ClientListener{
    private ObjectOutputStream updateOutput;
    private ObjectInputStream updateInput;

    private String name;
    private ObjectInputStream updateInputStream;
    private ObjectOutputStream updateOutputStream;
    private ClientInformation clientInformation;
    private Player myPlayer;
    private Thread vol;
    private Arena ParentArena;

    public ClientListener(ObjectInputStream updateInput, ObjectOutputStream updateOutput, Socket volitionSocket, Arena Parent, ClientInformation myInfo) throws IOException, ClassNotFoundException {

        this.updateOutputStream = updateOutput;
        this.updateInputStream = updateInput;
        clientInformation = myInfo;
        name = myInfo.getName();

        ParentArena = Parent;
        myPlayer = new Player(clientInformation);
        myPlayer.setId(ParentArena.getId());
        ParentArena.add(myPlayer, new Position(0,0));

        vol = new Thread(new VolitionListener(this, volitionSocket, clientInformation));
        vol.start();
    }

    public void updateVolition(Volition v){ //Updates the SERVER'S copy of the client's sent volition.
        myPlayer.setVolition(v);
    }

    public void sendClientUpdate(Update u){
        try {
            u.addPlayer(this.getMyPlayer());
            updateOutputStream.writeObject(u);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Player getMyPlayer(){
        return myPlayer;
    }
}
