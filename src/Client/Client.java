package Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Global.Constants;
import Global.Position;
import Transferable.*;

public class Client {
    private String host = "127.0.0.1";
    private int updatePort = Constants.UPDATE_PORT;
    private int volitionPort = Constants.VOLITION_PORT;
    private Socket volitionSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private ClientInformation me;
    private Update latest;
    private Thread upd;

    public static void main(String args[]) throws IOException {
        Client myClient = new Client();
    }

    public Client() throws IOException {
        me = new ClientInformation("Test Client", new Position(0,0), false);
        upd = new Thread(new ServerListener(this, new Socket(host, updatePort), me));
        upd.start();

        this.volitionSocket = new Socket(host, volitionPort);
        this.outputStream = new ObjectOutputStream(volitionSocket.getOutputStream());
        this.outputStream.flush(); //Necessary to avoid 'chicken or egg' situation
        this.inputStream = new ObjectInputStream(volitionSocket.getInputStream());
    }

    private void updateVolition(Volition v) throws IOException {
        //Use to update Server of a new Volition
        outputStream.writeObject(v);
    }

    public void getServerUpdate(Update u){
        //Called by ServerListener, not intended for other use.
        this.latest = u;
        //TODO: Trigger a re-render here, since the Server resent its entities
    }

    public void renderBoard(){
        //TODO: Add Graphics Render
        //TODO: **Clients should not render themselves, rather allow themselves to be Updated,
        //TODO:   however this doesn't allow for my client to have a different icon than others!**
    }
}
