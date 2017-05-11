package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import global.Constants;
import global.Position;
import transferable.*;

import javax.swing.*;

public class Client extends JFrame implements ActionListener, KeyListener {
    private String host = "127.0.0.1"; //TODO: Prompt for Host
    private int updatePort = Constants.UPDATE_PORT;
    private int volitionPort = Constants.VOLITION_PORT;
    private Socket volitionSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private ClientInformation me;
    private Update latest;
    private Thread upd;
    private Volition currentVolition;

    public static void main(String args[]) throws IOException {
        Client myClient = new Client();
        myClient.queue();
        myClient.renderBoard();
    }

    public Client() throws IOException {
        super();
        guiInit();
        currentVolition = new Volition(false,false);
        me = new ClientInformation("Test Client", new Position(0,0), false); //TODO: Prompt for Name
        upd = new Thread(new ServerListener(this, new Socket(host, updatePort), me));
        upd.start();
        this.volitionSocket = new Socket(host, volitionPort);
        this.outputStream = new ObjectOutputStream(volitionSocket.getOutputStream());
        this.outputStream.flush(); //Necessary to avoid 'chicken or egg' situation
        this.inputStream = new ObjectInputStream(volitionSocket.getInputStream());
    }

    private void queue(){
        while(latest == null){
            renderQueue();
        }
    }

    private void updateVolition() throws IOException {
        //Use to update server of a new Volition
        outputStream.writeObject(currentVolition);
    }

    public void getServerUpdate(Update u){
        //Called by ServerListener, not intended for other use.
        this.latest = u;
        //TODO: Trigger a re-render here, since the server resent its entities
    }

    public void renderQueue(){
        //TODO: Add a 'Waiting for Players...' message. Called in a while loop, don't spam dialog
    }

    public void renderBoard(){
        //TODO: Add Graphics Render
    }

    public void guiInit(){

    }
    public void actionPerformed(ActionEvent event){

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent event) { //https://docs.oracle.com/javase/tutorial/uiswing/events/keylistener.html

        if (event.getKeyCode() == KeyEvent.VK_UP) {
            modVolitionDirectional(Constants.FACING_NORTH);
        } else if (event.getKeyCode() == KeyEvent.VK_DOWN) {
            modVolitionDirectional(Constants.FACING_SOUTH);
        } else if (event.getKeyCode() == KeyEvent.VK_LEFT) {
            modVolitionDirectional(Constants.FACING_WEST);
        } else if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
            modVolitionDirectional(Constants.FACING_EAST);
        } else if(event.getKeyCode() == KeyEvent.VK_SPACE){
            currentVolition.setFacingVolition(false);
            currentVolition.setMovementVolition(false);
            currentVolition.setShootingVolition(true);
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void modVolitionDirectional(int facing){
        currentVolition.setFacingVolition(facing);
        if (latest.getPlayer().facing() == facing){
            currentVolition.setMovementVolition(true);
            currentVolition.setFacingVolition(false);
            currentVolition.setShootingVolition(false);
        } else {
            currentVolition.setMovementVolition(false);
            currentVolition.setFacingVolition(true);
            currentVolition.setShootingVolition(false);
        }
    }
}
