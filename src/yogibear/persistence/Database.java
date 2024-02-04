/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package yogibear.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import yogibear.model.RecordData;

/**
 *
 * @author artur
 */
public class Database {

    private final String tableName = "highscore";
    private final Connection conn;
    private final HashMap<RecordData, Integer> highScores;

    /**
     * Initializes Database
     */
    public Database() {
        Connection c = null;
        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql://localhost/yogibear?"
                    + "serverTimezone=UTC&user=root&password=admin123");
        } catch (Exception ex) {
            System.err.println("No connection");
        }
        this.conn = c;
        highScores = new HashMap<>();
        loadHighScores();
    }

    /**
     * Returns the list of 10 elements of HighScores sorted by baskets
     * @return 
     */
    public ArrayList<HighScore> getHighScores() {
        ArrayList<HighScore> scores = new ArrayList<>();
        for (RecordData rd : highScores.keySet()) {
            HighScore h = new HighScore(rd, highScores.get(rd));
            scores.add(h);
        }

        Comparator<HighScore> basketsComparator = Comparator.comparingInt(HighScore::getBaskets);
        Collections.sort(scores, basketsComparator.reversed());
        return new ArrayList<>(scores.subList(0, Math.min(scores.size(), 10)));
    }

    /**
     * Calls mergeHighScores
     * @param name
     * @param lvl
     * @param newScore
     * @return 
     */
    public boolean storeHighScore(String name, int lvl, int newScore) {
        return mergeHighScores(new RecordData(name, lvl), newScore, newScore > 0);
    }

    /**
     * Returns the old score based on the given parameters
     * @param name
     * @param lvl
     * @return 
     */
    public int getOldScore(String name, int lvl) {
        RecordData rd = new RecordData(name, lvl);
        if (highScores.containsKey(rd)) {
            return highScores.get(rd);
        }
        return 0;
    }

    /**
     * Loads all the records from the database table
     */
    private void loadHighScores() {
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
            while (rs.next()) {
                String name = rs.getString("Name");
                int lvl = rs.getInt("Level");
                int baskets = rs.getInt("Baskets");
                RecordData rd = new RecordData(name, lvl);
                mergeHighScores(rd, baskets, false);
            }
        } catch (Exception e) {
            System.err.println("The error occured while loading high scores: " + e.getMessage());
        }
    }

    /**
     * Gets the new record and in case it is better it saves it into the database
     * @param rd
     * @param score
     * @param store
     * @return 
     */
    private boolean mergeHighScores(RecordData rd, int score, boolean store) {
        boolean doUpdate = true;
        if (highScores.containsKey(rd)) {
            int oldScore = highScores.get(rd);
            doUpdate = (score > oldScore || oldScore == 0);
        }
        if (doUpdate) {
            highScores.remove(rd);
            highScores.put(rd, score);
            if (store) {
                return storeToDatabase(rd, score) > 0;
            }
            return true;
        }
        return false;
    }

    /**
     * Saves to the database the given record
     * @param rd
     * @param score
     * @return 
     */
    private int storeToDatabase(RecordData rd, int score) {
        try (Statement stmt = conn.createStatement()) {
            String s = "INSERT INTO " + tableName
                    + " (Name, Level, Baskets) "
                    + "VALUES('" + rd.getName() + "'," + rd.getLevel()
                    + "," + score
                    + ") ON DUPLICATE KEY UPDATE Baskets=" + score;
            return stmt.executeUpdate(s);
        } catch (Exception e) {
            System.err.println("The error occured while saving the record!");
        }
        return 0;
    }
}
