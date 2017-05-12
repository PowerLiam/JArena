package server;

import transferable.ClientInformation;
import transferable.Volition;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class VolitionListener implements Runnable{
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private ClientInformation clientInformation;
    private ClientListener runner;
    private boolean run;

    public VolitionListener(ClientListener runner,Socket socket, ClientInformation clientInformation){
        this.runner = runner;
        this.clientInformation = clientInformation;
        try {
            this.socket = socket;
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.outputStream.flush(); //Necessary to avoid 'chicken or egg' situation
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.println("client " + socket.getInetAddress() + " :" + e.getMessage());
            e.printStackTrace();
        }
        run = true;
    }

    @Override
    public void run() {
        while(run){
            try {
                Volition toNotify = (Volition) inputStream.readObject();
                System.out.println("V[ Move: " + toNotify.isMovementVolition() + " Face: " + toNotify.isFacingVolition() + " Shoot: " + toNotify.isShootingVolition());
                runner.updateVolition(toNotify);
            } catch (SocketException e){
                System.err.println("Lost connection to " + clientInformation.getName());
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                run = false;
            }
            catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}
