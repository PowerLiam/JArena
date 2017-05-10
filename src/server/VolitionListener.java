package server;

import transferable.ClientInformation;
import transferable.Volition;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class VolitionListener implements Runnable{
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private ClientInformation clientInformation;
    private ClientListener runner;

    public VolitionListener(ClientListener runner,Socket socket){
        this.runner = runner;
        try {
            this.socket = socket;
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.outputStream.flush(); //Necessary to avoid 'chicken or egg' situation
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.println("client " + socket.getInetAddress() + " :" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(true){
            try {
                Volition toNotify = (Volition) inputStream.readObject();
                runner.updateVolition(toNotify);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
