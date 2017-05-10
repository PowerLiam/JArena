package client;
import transferable.ClientInformation;
import transferable.Update;
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
        System.out.println("beg const");
        this.runner = runner;
        this.info = info;
        this.socket = socket;
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("bef flush");
        this.outputStream.flush(); //Necessary to avoid 'chicken or egg' situation
        System.out.println("after flush");
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        System.out.println("Before write obj");
        outputStream.writeObject(info);
        System.out.println("AFter WRite obj");
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
