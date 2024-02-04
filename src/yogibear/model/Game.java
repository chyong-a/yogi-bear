/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package yogibear.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.JOptionPane;
import yogibear.persistence.Database;
import yogibear.persistence.HighScore;
import yogibear.view.MainWindow;

/**
 *
 * @author artur
 */
public class Game {

    private final MainWindow mainWindow;
    private final HashMap<Integer, GameLevel> gameLevels;
    private GameLevel gameLevel;
    private final Database database;
    private boolean isBetterHighScore;
    private final int NUMBER_OF_LEVELS = 10;
    private String name = "";

    /**
     * Initializes Game
     * @param mainWindow 
     */
    public Game(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        gameLevels = new HashMap<>();
        database = new Database();
        readLevels();
    }

    public Collection<Integer> getLevels() {
        return gameLevels.keySet();
    }

    public ArrayList<HighScore> getHighScores() {
        return database.getHighScores();
    }

    public boolean isLevelLoaded() {
        return gameLevel != null;
    }

    public int getLevelRows() {
        return gameLevel.getRows();
    }

    public int getLevelCols() {
        return gameLevel.getCols();
    }

    public LevelItem getItem(int row, int col) {
        return gameLevel.getLevel()[row][col];
    }

    public int getGameLvl() {
        return (gameLevel != null) ? gameLevel.getLvl() : null;
    }

    public int getLevelNumBoxes() {
        return (gameLevel != null) ? gameLevel.getNumBaskets() : 0;
    }

    public int getLevelNumBoxesInPlace() {
        return (gameLevel != null) ? gameLevel.getNumBasketsCollected() : 0;
    }

    public int getLevelNumberLives() {
        return (gameLevel != null) ? gameLevel.getNumberLives() : 0;
    }

    public boolean isGameEnded() {
        return (gameLevel != null && gameLevel.isGameEnded());
    }

    public boolean isLost() {
        return gameLevel.isLost();
    }

    public boolean isWon() {
        return gameLevel.isWon();
    }

    public Position getPlayerPos() {
        return new Position(gameLevel.getPlayer().getX(), gameLevel.getPlayer().getY());
    }

    public Position getRangerPos() {
        return new Position(gameLevel.getRanger().getX(), gameLevel.getRanger().getY());
    }

    /**
     * Loads game based on the given parameter, resets all the fields needed
     * @param level 
     */
    public void loadGame(int level) {
        gameLevel = new GameLevel(gameLevels.get(level));
        isBetterHighScore = false;
        name = "";
        mainWindow.resetTimer();
    }

    /**
     * Moves player and handles end of the game
     * @param d 
     */
    public void step(Direction d) {
        gameLevel.movePlayer(d);
        handleGameEnd();
    }

    /**
     * Moves ranger and handles end of the game
     */
    public void moveRanger() {
        gameLevel.moveRanger();
        handleGameEnd();
    }

    /**
     * Checks if the game is finished; in case it is finished, it handles the cases of the lost/won game and saves the record when it's better than the old record
     */
    public void handleGameEnd() {
        if (isGameEnded()) {
            mainWindow.stopTimer();
            int lvl = gameLevel.getLvl();
            int basketsCollected = gameLevel.getNumBasketsCollected();
            this.name = JOptionPane.showInputDialog("Please, enter your name:");
            int oldScore = database.getOldScore(name, lvl);
            isBetterHighScore = basketsCollected > oldScore;

            String msg;
            if (isLost()) {
                msg = "Game Over!";
                msg = handleBetterScore(isBetterHighScore, lvl, basketsCollected, msg);
                JOptionPane.showMessageDialog(null, msg, "FINISH", JOptionPane.INFORMATION_MESSAGE);
            } else if (isWon()) {
                msg = "You've passed this level!";
                msg = handleBetterScore(isBetterHighScore, lvl, basketsCollected, msg);
                JOptionPane.showMessageDialog(null, msg, "FINISH", JOptionPane.INFORMATION_MESSAGE);

                loadNextLevel();
            }
        }
    }

    /**
     * Loads next level from the current one
     */
    private void loadNextLevel() {
        int nextLvl = gameLevel.getLvl() + 1;
        if (nextLvl <= NUMBER_OF_LEVELS) {
            loadGame(nextLvl);
        }
    }

    /**
     * Based on whether the score is better or not, it changes the message for the message dialog
     * @param isBetterHighScore
     * @param lvl
     * @param basketsCollected
     * @param msg
     * @return 
     */
    private String handleBetterScore(boolean isBetterHighScore, int lvl, int basketsCollected, String msg) {
        if (isBetterHighScore) {
            database.storeHighScore(name, lvl, basketsCollected);
            msg += " You've set a new record! We'll save it!";
        } else {
            msg += " You didn't break your previous record or your previous record was maximum. It won't be saved.";
        }
        return msg;
    }

    /**
     * Reads the levels from the txt files
     */
    private void readLevels() {
        for (int i = 0; i < NUMBER_OF_LEVELS; i++) {
            try (BufferedReader reader = new BufferedReader(new FileReader("lvls/level" + (i + 1) + ".txt"))) {
                String line = readNextLine(reader);
                ArrayList<String> gameLevelRows = new ArrayList<>();
                while (!line.isEmpty()) {
                    int lvl = readGameLvl(line);
                    if (lvl == -1) {
                        return;
                    }
                    gameLevelRows.clear();
                    line = readNextLine(reader);
                    while (!line.isEmpty() && line.trim().charAt(0) != '-') {
                        gameLevelRows.add(line);
                        line = readNextLine(reader);
                    }
                    addNewGameLevel(new GameLevel(gameLevelRows, lvl));
                }
            } catch (Exception e) {
                System.out.println("levels" + (i + 1) + ".txt doesn't exist");
            }
        }
    }

    /**
     * Adds new game level to gameLevels field
     * @param gameLevel 
     */
    private void addNewGameLevel(GameLevel gameLevel) {
        gameLevels.put(gameLevel.getLvl(), gameLevel);
    }

    /**
     * Reads next line of the given BufferedReader
     * @param reader
     * @return
     * @throws IOException 
     */
    private String readNextLine(BufferedReader reader) throws IOException {
        String line = "";
        while (reader.ready() && line.trim().isEmpty()) {
            line = reader.readLine();
        }
        return line;
    }

    /**
     * Reads game level
     * @param line
     * @return 
     */
    private int readGameLvl(String line) {
        line = line.trim();
        if (line.isEmpty() || line.charAt(0) != '-') {
            return -1;
        }
        Scanner s = new Scanner(line);
        s.next();
        if (!s.hasNext()) {
            return -1;
        }
        int lvl = s.nextInt();
        return lvl;
    }

}
