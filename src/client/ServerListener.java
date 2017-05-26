package client;
import transferable.ClientInformation;
import transferable.Update;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ServerListener implements Runnable {
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private ClientInformation info;
    private Client runner;
    private boolean receivedFirstUpdate = false;

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
                if(!receivedFirstUpdate) {
                    runner.getContentPane().removeAll();
                    receivedFirstUpdate = true;
                    runner.add(runner.arenaDisplay);
                }
                if(toNotify.isGameOver()){
                    try{
                        socket.close();
                    } catch(IOException e){
                        e.printStackTrace();
                    }
                }
                runner.getServerUpdate(toNotify);
            } catch(SocketException e){
                System.err.println("Lost connection to server.");
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    runner.loseConnection();
                } catch (InterruptedException e1) {
                    //This will never happen
                }
                break;
            }
            catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
