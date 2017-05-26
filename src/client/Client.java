package client;

import global.Constants;
import global.Position;
import server.Entity;
import server.Player;
import server.Wall;
import transferable.ClientInformation;
import transferable.ServerInformation;
import transferable.Update;
import transferable.Volition;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Client extends JFrame implements ActionListener, KeyListener {
    ArenaDisplay arenaDisplay;
    QueueDisplay queueDisplay;
    GameEndDisplay gameEndDisplay;
    String endGameString;
    int endGameInt;
    int BOUNDARY_X;
    int BOUNDARY_Y;
    BufferedImage bullet;
    BufferedImage player;
    BufferedImage enemy;
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
    private Entity[][] board = new Entity[Constants.BOARD_VIEW_WINDOW_SIZE][Constants.BOARD_VIEW_WINDOW_SIZE];

    public Client() throws IOException, InterruptedException, ClassNotFoundException {
        super("Java Battle Arena");
        bullet = resizeBufferedImage(ImageIO.read(Client.class.getResourceAsStream("resources/bullet.png")), 40, 40);
        player = resizeBufferedImage(ImageIO.read(Client.class.getResourceAsStream("resources/client.png")), 40, 40);
        enemy = resizeBufferedImage(ImageIO.read(Client.class.getResourceAsStream("resources/enemy.png")), 40, 40);

        while (userName == null || userName.isEmpty() || userName == "")
            userName = JOptionPane.showInputDialog(null, "Enter a Username:", "Input", JOptionPane.INFORMATION_MESSAGE);
        while (providedAddress == null || providedAddress.isEmpty() || providedAddress == "")
            providedAddress = (String) JOptionPane.showInputDialog(null, "Enter the Server's IP:", "Input", JOptionPane.QUESTION_MESSAGE, null, null, "127.0.0.1");
        guiInit();
        latest = new Update(null);
        currentVolition = new Volition(false, false);
        me = new ClientInformation(userName, new Position(0, 0), false);
        upd = new Thread(new ServerListener(this, new Socket(providedAddress, updatePort), me));
        upd.start();
        this.volitionSocket = new Socket(providedAddress, volitionPort);
        this.outputStream = new ObjectOutputStream(volitionSocket.getOutputStream());
        this.outputStream.flush(); //Necessary to avoid 'chicken or egg' situation
        this.inputStream = new ObjectInputStream(volitionSocket.getInputStream());

        addKeyListener(this);
        renderQueue();
    }

    public static void main(String args[]) throws IOException, InterruptedException, ClassNotFoundException {
        Client myClient = new Client();
        myClient.renderQueue();

    }

    public static BufferedImage resizeBufferedImage(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public static BufferedImage rotateCw(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage newImage = new BufferedImage(height, width, img.getType());
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                newImage.setRGB(height - 1 - j, i, img.getRGB(i, j));
            }
        return newImage;
    }

    public void guiInit() {

        arenaDisplay = new ArenaDisplay();

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(new Dimension(600, 600));
        this.setBackground(Color.WHITE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);

    }

    private void updateVolition() throws IOException {
        synchronized (currentVolition) {
            outputStream.reset();
            outputStream.writeObject(currentVolition);
        }
    }

    public void getServerInformation(ServerInformation s) {
        this.BOUNDARY_X = s.getBOUNDARY_X();
        this.BOUNDARY_Y = s.getBOUNDARY_Y();
    }

    public void getServerUpdate(Update u) {
        //Called by ServerListener, not intended for other use.
        if (!(u.isGameOver())) {
            synchronized (latest) {
                this.latest = u;
            }
            this.repaint();
            this.revalidate();
        } else {
            this.remove(arenaDisplay);
            gameEndDisplay = new GameEndDisplay();
            endGameString = (u.getWinningPlayer() + " won!");
            endGameInt = u.getWinningPlayerKills();
            this.add(gameEndDisplay);
            this.pack();
        }
    }

    public void loseConnection() throws InterruptedException {
        //Called by ServerListener, not intended for other use.
        TimeUnit.SECONDS.sleep(7);
        System.exit(-1);
    }

    public void renderQueue() {
        queueDisplay = new QueueDisplay();
        this.add(queueDisplay);
        this.pack();
    }

    public void renderBoard() {
        board = new Entity[Constants.BOARD_VIEW_WINDOW_SIZE][Constants.BOARD_VIEW_WINDOW_SIZE]; //Last render is now invalid.
        ArrayList<Entity> allEntities = latest.getEntities();
        Player me = latest.getPlayer();
        Position myPos = me.getPosition();
        System.out.println("Position: " + myPos.getX() + "," + myPos.getY());
        Position topLeft = new Position(myPos.getX() - 7, myPos.getY() - 7);
        Position botRight = new Position(myPos.getX() + 7, myPos.getY() + 7);

        for (Entity entity : allEntities) {
            if (entity.getPosition().getX() >= topLeft.getX() && entity.getPosition().getX() <= botRight.getX()) {
                if (entity.getPosition().getY() >= topLeft.getY() && entity.getPosition().getY() <= botRight.getY()) {
                    board[entity.getPosition().getX() - topLeft.getX()][entity.getPosition().getY() - topLeft.getY()] = entity;
                }
            }
        }

        int renderedX;
        int renderedY = 0;
        for (int y = topLeft.getY(); y <= botRight.getY(); y++) {
            renderedX = 0;
            for (int x = topLeft.getX(); x <= botRight.getX(); x++) {
                if (x < 0 || y < 0 || x > BOUNDARY_X || y > BOUNDARY_Y) {
                    board[renderedX][renderedY] = new Wall();
                }
                renderedX++;
            }
            renderedY++;
        }
    }

    public void actionPerformed(ActionEvent event) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent event) { //https://docs.oracle.com/javase/tutorial/uiswing/events/keylistener.html
        if (latest.getEntities() == null) {
            renderQueue();
        } else {

            if (event.getKeyCode() == KeyEvent.VK_UP) {
                modVolitionDirectional(Constants.FACING_NORTH);
            } else if (event.getKeyCode() == KeyEvent.VK_DOWN) {
                modVolitionDirectional(Constants.FACING_SOUTH);
            } else if (event.getKeyCode() == KeyEvent.VK_LEFT) {
                modVolitionDirectional(Constants.FACING_WEST);
            } else if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
                modVolitionDirectional(Constants.FACING_EAST);
            } else if (event.getKeyCode() == KeyEvent.VK_SPACE) {
                currentVolition.setFacingVolition(false);
                currentVolition.setMovementVolition(false);
                currentVolition.setShootingVolition(true);
            }
            try {
                updateVolition();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void modVolitionDirectional(int facing) {
        currentVolition.setFacingVolition(facing);
        if (latest.getPlayer().facing() == facing) {
            currentVolition.setMovementVolition(true);
            currentVolition.setFacingVolition(false);
            currentVolition.setShootingVolition(false);
        } else {
            currentVolition.setMovementVolition(false);
            currentVolition.setFacingVolition(true);
            currentVolition.setShootingVolition(false);
        }
    }

    private BufferedImage getRotatedImage(Entity entity, BufferedImage image) {
        switch (entity.getFacing()) {
            case Constants.FACING_NORTH: {
                return image;
            }
            case Constants.FACING_EAST: {
                return rotateCw(image);
            }
            case Constants.FACING_SOUTH: {
                return rotateCw(rotateCw(image));
            }
            case Constants.FACING_WEST: {
                return rotateCw(rotateCw(rotateCw(image)));
            }
        }
        return null;
    }

    /**
     * In-Line Classes for JPanel elements intended for rendering
     */
    class QueueDisplay extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            String waiting = "Waiting for Server...";
            Graphics2D g2d = (Graphics2D) g.create();
            super.paintComponent(g);
            g2d.setFont(new Font("TimesRoman", Font.PLAIN, 30));
            FontMetrics fm = g2d.getFontMetrics();
            int x = ((getWidth() - fm.stringWidth(waiting)) / 2);
            int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
            g2d.drawString(waiting, x, y);
            g2d.dispose();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(600, 600);
        }
    }

    class GameEndDisplay extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            super.paintComponent(g);
            g2d.setFont(new Font("TimesRoman", Font.PLAIN, 30));
            FontMetrics fm = g2d.getFontMetrics();
            int x = ((getWidth() - fm.stringWidth(endGameString)) / 2);
            int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
            g2d.drawString(endGameString, x, y);

            x = ((getWidth() - fm.stringWidth(endGameInt + " kills.")) / 2);
            g2d.drawString(endGameInt + " kills.", x, y + fm.getHeight() + fm.getDescent());
            g2d.dispose();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(600, 600);
        }
    }

    class ArenaDisplay extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            boolean gridColor = true;
            Color rgbGridColor = new Color(184, 184, 184);
            boolean onOddSquare = false;

            //Essentially determines what color to start the whole rendering process on, xor statement defines a grid.
            if (board[7][7] == null) {
                onOddSquare = false;
            } else {
                Position myPos = board[7][7].getPosition();
                if (myPos.getX() % 2 == 0 ^ myPos.getY() % 2 == 0) onOddSquare = true;
            }

            renderBoard();
            int xOffset = 0;
            for (Entity[] row : board) {//Each Row
                int yOffset = 0;
                for (int i = row.length - 1; i >= 0; i--) {//Each Square

                    if (onOddSquare) {
                        g.setColor(Color.lightGray);
                        onOddSquare = false;
                    } else if (!onOddSquare) {
                        g.setColor(rgbGridColor);
                        onOddSquare = true;
                    }

                    g.fillRect(xOffset, yOffset, Constants.SQUARE_DIM, Constants.SQUARE_DIM);
                    if (row[i] == null) {
                        yOffset += Constants.SQUARE_DIM;
                        continue;
                    } else if (row[i].isWall()) {
                        g.setColor(Color.DARK_GRAY);
                        g.fillRect(xOffset, yOffset, Constants.SQUARE_DIM, Constants.SQUARE_DIM);
                    } else if (row[i].equals(latest.getPlayer())) {
                        g.drawImage(getRotatedImage(row[i], player), xOffset, yOffset, this);
                    } else if (row[i].isPlayer()) {
                        g.drawImage(getRotatedImage(row[i], enemy), xOffset, yOffset, this);
                    } else {
                        g.drawImage(getRotatedImage(row[i], bullet), xOffset, yOffset, this);
                    }
                    yOffset += Constants.SQUARE_DIM;
                }
                xOffset += Constants.SQUARE_DIM;
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(600, 600);
        }
    }

}
