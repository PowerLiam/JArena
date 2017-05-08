package Client;

import java.io.IOException;
import Transferable.*;

public class Client {
    private ClientInformation me;
    private Update latest;

    public static void main(String args[]) throws IOException {
        Client myClient = new Client();
    }

    public Client() throws IOException {
        //TODO: Send ClientInformation on port 9091 (updatePort)
        //TODO: Thread a ServerListener on port 9091 (updatePort)
    }

    private void updateVolition(Volition v){
        //TODO: Update client's volition on the server using port 9092 (volitionPort)
    }

    private void getServerUpdate(Update u){
        this.latest = u;
        //TODO: Trigger a re-render here
    }

    public void renderBoard(){
        //TODO: Add Graphics Render
    }

    //TODO: Thread for both Volition Sending and Update Receipt
}
