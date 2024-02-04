package yogibear.view;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import yogibear.model.Direction;
import yogibear.model.Game;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author artur
 */
public class MainWindow extends JFrame {

    private final Game game;
    private Board board;
    private final JLabel gameStatLabel;
    private Timer t;
    private final JLabel gameTimer;
    private long startTime;
    private int elapsedTime;

    /**
     * Initializes MainWindow
     */
    public MainWindow() {
        game = new Game(this);

        setTitle("Yogi Bear");
        setSize(600, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        URL url = MainWindow.class.getClassLoader().getResource("resources/icon.png");
        setIconImage(Toolkit.getDefaultToolkit().getImage(url));

        JMenuBar menuBar = new JMenuBar();
        JMenu menuGame = new JMenu("Menu");
        JMenu menuGameLevel = new JMenu("Levels");
        createGameLevelMenuItems(menuGameLevel);

        JMenuItem menuHighScores = new JMenuItem(new AbstractAction("Score Board") {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HighScoreWindow(game.getHighScores(), MainWindow.this);
            }
        });

        JMenuItem menuGameExit = new JMenuItem(new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        menuGame.add(menuGameLevel);
        menuGame.add(menuHighScores);
        menuGame.addSeparator();
        menuGame.add(menuGameExit);
        menuBar.add(menuGame);
        setJMenuBar(menuBar);

        setLayout(new BorderLayout(0, 10));

        gameStatLabel = new JLabel("Current Game Label");
        add(gameStatLabel, BorderLayout.NORTH);

        gameTimer = new JLabel("Game Timer");
        add(gameTimer, BorderLayout.SOUTH);

        try {
            add(board = new Board(game), BorderLayout.CENTER);
        } catch (IOException ex) {
        }

        Timer rangerTimer = new Timer(600, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!game.isGameEnded()) {
                    game.moveRanger();
                }
            }
        });
        rangerTimer.start();

        Timer refreshTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                refreshGameStatLabel();
                board.repaint();
            }
        });
        refreshTimer.start();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                super.keyPressed(ke);
                if (!game.isLevelLoaded()) {
                    return;
                }
                int keyPressed = ke.getKeyCode();
                Direction d = null;
                switch (keyPressed) {
                    case 65:
                        d = Direction.LEFT;
                        break;
                    case 37:
                        d = Direction.LEFT;
                        break;
                    case 68:
                        d = Direction.RIGHT;
                        break;
                    case 39:
                        d = Direction.RIGHT;
                        break;
                    case 87:
                        d = Direction.UP;
                        break;
                    case 38:
                        d = Direction.UP;
                        break;
                    case 83:
                        d = Direction.DOWN;
                        break;
                    case 40:
                        d = Direction.DOWN;
                        break;
                    case KeyEvent.VK_ESCAPE:
                        game.loadGame(game.getGameLvl());
                }

                if (!game.isGameEnded() && d != null) {
                    game.step(d);
                }
            }
        });

        setResizable(false);
        setLocationRelativeTo(null);
        game.loadGame(1);
        board.setScale(2.0);
        pack();
        refreshGameStatLabel();
        startTimer();
        setVisible(true);
    }

    /**
     * Resets the timer
     */
    public void resetTimer() {
        if (t != null) {
            startTime = System.currentTimeMillis();
            t.restart();
        }
    }

    /**
     * Stops the timer
     */
    public void stopTimer() {
        if (t != null) {
            t.stop();
        }
    }

    /**
     * Creates and adds the menu items with levels
     * @param menu 
     */
    private void createGameLevelMenuItems(JMenu menu) {
        for (Integer i : game.getLevels()) {
            JMenuItem item = new JMenuItem(new AbstractAction("Level-" + i) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    game.loadGame(i);
                    board.refresh();
                    pack();
                }
            });
            menu.add(item);
        }
    }

    /**
     * Refreshes the label with the stat of the game
     */
    private void refreshGameStatLabel() {
        String s = "Remaining lives: " + game.getLevelNumberLives() + "/3";
        s += ", baskets collected: " + game.getLevelNumBoxesInPlace() + "/" + game.getLevelNumBoxes();
        gameStatLabel.setText(s);
    }

    /**
     * Starts the timer; updates the elapsed time
     */
    private void startTimer() {
        startTime = System.currentTimeMillis();
        t = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedTime = (int) (System.currentTimeMillis() - startTime) / 1000;
                String timerText = "Elapsed time: " + elapsedTime + "s";
                gameTimer.setText(timerText);
            }
        });
        t.start();
    }

}
