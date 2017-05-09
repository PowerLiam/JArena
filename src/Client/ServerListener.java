package Client;
import Transferable.ClientInformation;
import Transferable.Update;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerListener implements Runnable {
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private ClientInformation info;
    private Client runner;

    public ServerListener(Client runner, Socket socket, ClientInformation info) throws IOException {
        this.runner = runner;
        this.info = info;
        this.socket = socket;
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.outputStream.flush(); //Necessary to avoid 'chicken or egg' situation
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        outputStream.writeObject(info);
    }

    @Override
    public void run() {
        while(true){
            try {
                Update toNotify = (Update) inputStream.readObject();
                runner.getServerUpdate(toNotify);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
