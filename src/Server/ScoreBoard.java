package Server;

import javax.swing.*;

/**
 * A component of JArena
 *
 * @author Jordan Blackadar <"jordan.blackadar@outlook.com">
 * @version 0.1.0
 * @since 5/6/2017 3:48 PM
 */
public class ScoreBoard {
    private JList leaderBoard;
    private JPanel panel1;
    private JProgressBar gameProgress;
    private JButton startStopButton;
    private JTextField commandField;
    private Arena myGame = new Arena();
}
