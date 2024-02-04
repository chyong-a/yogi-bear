/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package yogibear.model;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author artur
 */
public class GameLevel {

    private final int MAX_LIVES = 3;
    private final int lvl;
    private final int rows, cols;
    private final LevelItem[][] level;
    private Position player;
    private Position ranger;
    private Position spawnPoint;
    private int numBaskets, numBasketsCollected, lives;

    /**
     * Initializes GameLevel
     * @param gameLevelRows
     * @param lvl 
     */
    public GameLevel(ArrayList<String> gameLevelRows, int lvl) {
        this.lvl = lvl;

        int width = 0;
        for (String s : gameLevelRows) {
            if (s.length() > width) {
                width = s.length();
            }
        }
        rows = gameLevelRows.size();
        cols = width;
        level = new LevelItem[rows][cols];

        numBaskets = 0;
        numBasketsCollected = 0;
        lives = MAX_LIVES;

        for (int i = 0; i < rows; i++) {
            String row = gameLevelRows.get(i);
            for (int j = 0; j < row.length(); j++) {
                switch (row.charAt(j)) {
                    case 'P':
                        player = new Position(j, i);
                        spawnPoint = new Position(j, i);
                        level[i][j] = LevelItem.EMPTY;
                        break;
                    case 'R':
                        ranger = new Position(j, i);
                        level[i][j] = LevelItem.EMPTY;
                        break;
                    case 'B':
                        level[i][j] = LevelItem.BASKET;
                        numBaskets++;
                        break;
                    case 'T':
                        level[i][j] = LevelItem.TREE;
                        break;
                    case 'M':
                        level[i][j] = LevelItem.MOUNTAIN;
                        break;
                    default:
                        level[i][j] = LevelItem.EMPTY;
                        break;
                }
            }

            for (int j = row.length(); j < cols; j++) {
                level[i][j] = LevelItem.EMPTY;
            }
        }
    }

    /**
     * Initializes GameLevel
     * @param gl 
     */
    public GameLevel(GameLevel gl) {
        this.lvl = gl.lvl;
        this.rows = gl.rows;
        this.cols = gl.cols;
        this.numBaskets = gl.numBaskets;
        this.numBasketsCollected = gl.numBasketsCollected;
        this.lives = gl.lives;
        this.level = new LevelItem[rows][cols];
        this.player = new Position(gl.player.getX(), gl.player.getY());
        this.ranger = new Position(gl.ranger.getX(), gl.ranger.getY());
        this.spawnPoint = gl.spawnPoint;
        for (int i = 0; i < rows; i++) {
            System.arraycopy(gl.level[i], 0, level[i], 0, cols);
        }
    }

    public int getLvl() {
        return lvl;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public LevelItem[][] getLevel() {
        return level;
    }

    public Position getPlayer() {
        return player;
    }

    public Position getRanger() {
        return ranger;
    }

    public boolean isGameEnded() {
        return isWon() || isLost();
    }

    public boolean isLost() {
        return lives <= 0;
    }

    public boolean isWon() {
        return numBasketsCollected >= numBaskets;
    }

    public int getNumBaskets() {
        return numBaskets;
    }

    public int getNumBasketsCollected() {
        return numBasketsCollected;
    }

    public int getNumberLives() {
        return lives;
    }

    /**
     * Moves player based on the given direction if the movement towards this direction is possible
     * @param d
     * @return 
     */
    public boolean movePlayer(Direction d) {
        if (isGameEnded()) {
            return false;
        }

        Position curr = player;
        Position next = curr.translate(d);

        if (isEmpty(next)) {
            player = next;
            if (isCaughtByRanger()) {
                handleCaughtByRanger();
                return false;
            }
            return true;
        } else if (isBasket(next)) {
            player = next;
            level[next.getY()][next.getX()] = LevelItem.EMPTY;
            numBasketsCollected++;
            return true;
        }
        return false;
    }

    /**
     * Moves ranger in the random direction
     */
    public void moveRanger() {
        Random random = new Random();
        int randInt = random.nextInt(4);
        Position next = null;
        switch (randInt) {
            case 0:
                next = ranger.translate(Direction.UP);
                break;
            case 1:
                next = ranger.translate(Direction.DOWN);
                break;
            case 2:
                next = ranger.translate(Direction.LEFT);
                break;
            case 3:
                next = ranger.translate(Direction.RIGHT);
                break;
        }

        if (next != null && isEmpty(next)) {
            ranger = next;
        }

        if (isCaughtByRanger()) {
            handleCaughtByRanger();
        }
    }

    /**
     * Handles the case of being caught by ranger that is decrementing the lives and respawning the player
     */
    private void handleCaughtByRanger() {
        this.lives--;
        this.player = this.spawnPoint;
    }

    /**
     * Checks if the new position goes beyond the map
     * @param p
     * @return 
     */
    private boolean isNotBeyondPark(Position p) {
        return (p.getX() >= 0 && p.getY() >= 0 && p.getX() < cols && p.getY() < rows);
    }

    /**
     * Checks if the new position is empty to go on
     * @param p
     * @return 
     */
    private boolean isEmpty(Position p) {
        if (!isNotBeyondPark(p)) {
            return false;
        }
        return (level[p.getY()][p.getX()] == LevelItem.EMPTY);
    }

    /**
     * Checks if the new position is a basket
     * @param p
     * @return 
     */
    private boolean isBasket(Position p) {
        if (!isNotBeyondPark(p)) {
            return false;
        }
        return (level[p.getY()][p.getX()] == LevelItem.BASKET);
    }

    /**
     * Checks if the player is caught by ranger or not 
     * @return 
     */
    private boolean isCaughtByRanger() {
        return ranger.translate(Direction.UP).equals(player) || ranger.translate(Direction.DOWN).equals(player) || ranger.translate(Direction.LEFT).equals(player) || ranger.translate(Direction.RIGHT).equals(player);
    }

}
