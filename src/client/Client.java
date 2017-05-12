package client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import global.Constants;
import global.Position;
import server.Entity;
import server.Player;
import transferable.*;

import javax.swing.*;

public class Client extends JFrame implements ActionListener, KeyListener {
    private String providedAddress;
    private String userName;
    private int updatePort = Constants.UPDATE_PORT;
    private int volitionPort = Constants.VOLITION_PORT;
    private Socket volitionSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private ClientInformation me;
    private Update latest;
    private Thread upd;
    private Volition currentVolition;
    private Entity[][] board = new Entity[15][15];

    private JPanel canvas;

    public static void main(String args[]) throws IOException, InterruptedException {
        Client myClient = new Client();
        myClient.queue();
        myClient.renderBoard();
    }

    public Client() throws IOException {
        super("Java Battle Arena");
        while(userName == null || userName.isEmpty() || userName == "")
            userName = JOptionPane.showInputDialog(null, "Enter a Username:", "Input", JOptionPane.INFORMATION_MESSAGE);
        while(providedAddress == null || providedAddress.isEmpty() || providedAddress == "")
            providedAddress = (String) JOptionPane.showInputDialog(null, "Enter the Server's IP:", "Input", JOptionPane.QUESTION_MESSAGE, null, null, "127.0.0.1");
        guiInit();
        currentVolition = new Volition(false,false);
        me = new ClientInformation(userName, new Position(0,0), false);
        upd = new Thread(new ServerListener(this, new Socket(providedAddress, updatePort), me));
        upd.start();
        this.volitionSocket = new Socket(providedAddress, volitionPort);
        this.outputStream = new ObjectOutputStream(volitionSocket.getOutputStream());
        this.outputStream.flush(); //Necessary to avoid 'chicken or egg' situation
        this.inputStream = new ObjectInputStream(volitionSocket.getInputStream());
    }

    public void guiInit(){
        canvas = new JPanel();
        this.add(canvas);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(new Dimension(700,700));
        this.setBackground(Color.WHITE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
    }

    private void updateVolition() throws IOException {
        //Use to update server of a new Volition
        outputStream.writeObject(currentVolition);
    }

    private void queue() throws InterruptedException {
        while(latest == null){
            renderQueue();
            TimeUnit.SECONDS.sleep(1);
        }
    }

    public void getServerUpdate(Update u){
        //Called by ServerListener, not intended for other use.
        this.latest = u;
        //TODO: Trigger a re-render here, since the server resent its entities
    }

    public void loseConnection() throws InterruptedException {
        //Called by ServerListener, not intended for other use.
        TimeUnit.SECONDS.sleep(5);
        System.exit(-1);
    }

    public void renderQueue(){
        this.add(new QueueDisplay());
        this.pack();
    }

    //Builds Board
    //TODO: This should be in ArenaDisplay, overriding paintComponent(Graphics g) so that we can use repaint();
    //TODO: http://stackoverflow.com/questions/13075417/java-jframe-graphics
    protected void paintComponent(Graphics g){
        int xOffset = 0;
        int yOffset = 0;
        for(Entity[] row : board){//Each Row
            for(Entity space : row){ //Each Square
                if(space.equals(null)){
                    g.drawRect(xOffset, yOffset, Constants.SQUARE_DIM, Constants.SQUARE_DIM);
                }
                xOffset += Constants.SQUARE_DIM;
            }
            yOffset += Constants.SQUARE_DIM;
        }
    }

    public void renderBoard(){
        ArrayList<Entity> allEntities = latest.getEntities();
        Player me = latest.getPlayer();
        Position myPos = me.getPosition();
        Position topLeft = new Position(myPos.x - 7, myPos.y - 7); //TODO: This could be negative!
        Position botRight = new Position(myPos.x + 7, myPos.y + 7);



        for(Entity cur: allEntities){
            if(cur.getPosition().x > topLeft.x && cur.getPosition().x < botRight.x){
                if(cur.getPosition().y > topLeft.y && cur.getPosition().y < botRight.y){
                    board[topLeft.x + cur.getPosition().x][topLeft.y + cur.getPosition().y] = cur; //TODO: Throwing ArrayIndexOutOfBounds!
                }
            }
        }



        //Loops through every space that I will render
        for(int y = topLeft.y; y > botRight.y; y--){
            for(int x = topLeft.x; x > botRight.x; x++){
                Entity current;



            }
        }

        //TODO: Add Graphics Render
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
        //TODO: Call updateVolition() when you're ready to send currentVolition() to the Server.
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


    /**
     * In-Line Classes for JPanel elements intended for rendering
     */
    class QueueDisplay extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
            g.drawString("Waiting for Server...",215,350);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(700, 700);
        }
    }

    class ArenaDisplay extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setFont(new Font("Helvetica", Font.PLAIN, 30));
            g.drawString("Got an Update!",215,350);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(700, 700);
        }
    }
}
