package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import global.Position;
import transferable.*;

public class ClientListener implements Comparable<ClientListener>{
    private ObjectOutputStream updateOutput;
    private ObjectInputStream updateInput;

    private String name;
    private ObjectInputStream updateInputStream;
    private ObjectOutputStream updateOutputStream;
    public ClientInformation clientInformation;
    private Player myPlayer;
    private Thread vol;
    private Arena ParentArena;
    private int missedUpdateCount;

    public ClientListener(ObjectInputStream updateInput, ObjectOutputStream updateOutput, Socket volitionSocket, Arena Parent, ClientInformation myInfo) throws IOException, ClassNotFoundException {

        this.updateOutputStream = updateOutput;
        this.updateInputStream = updateInput;
        clientInformation = myInfo;
        name = myInfo.getName();

        ParentArena = Parent;
        myPlayer = new Player(clientInformation);
        myPlayer.setId(ParentArena.getId());
        ParentArena.add(myPlayer);

        vol = new Thread(new VolitionListener(this, volitionSocket, clientInformation));
        vol.start();
    }

    public void updateVolition(Volition v){ //Updates the SERVER'S copy of the client's sent volition.
        synchronized (ParentArena.lock) {
            myPlayer.setVolition(v);
        }
    }

    public void sendServerInfo(ServerInformation s) throws IOException {
        updateOutputStream.writeObject(s);
    }

    public void sendClientUpdate(Update u){
        synchronized (myPlayer) {
            try {
                u.addPlayer(this.getMyPlayer());
                //System.out.println(clientInformation.getName() + " Sending back : (" + getMyPlayer().currentPosition.getX() + "'" + getMyPlayer().currentPosition.getY() + ")");
                updateOutputStream.reset();
                updateOutputStream.writeObject(u);
                missedUpdateCount = 0;
            } catch (IOException e) {
                missedUpdateCount++;
                if (missedUpdateCount < 10) {
                    System.err.println(clientInformation.getName() + " has missed " + missedUpdateCount + " consecutive update(s).");
                } else if (missedUpdateCount == 10) {
                    System.err.println(clientInformation.getName() + " lost connection or left. Removing entity...");
                    getMyPlayer().die();
                }
            }
        }
    }

    public synchronized Player getMyPlayer(){
        return myPlayer;
    }

    @Override
    public int compareTo(ClientListener other) {
        if(this.getMyPlayer().numberOfKills < other.getMyPlayer().numberOfKills) return 1;
        else if(this.getMyPlayer().numberOfKills > other.getMyPlayer().numberOfKills) return -1;
        else return 0;
    }
}
