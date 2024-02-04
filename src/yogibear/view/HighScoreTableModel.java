/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package yogibear.view;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import yogibear.persistence.HighScore;

/**
 *
 * @author artur
 */
public class HighScoreTableModel extends AbstractTableModel {

    private final ArrayList<HighScore> highScores;
    private final String[] colName = new String[]{"Name", "Level", "Baskets"};

    /**
     * Initializes HighScoreTableModel
     * @param highScores 
     */
    public HighScoreTableModel(ArrayList<HighScore> highScores) {
        this.highScores = highScores;
    }

    @Override
    public int getRowCount() {
        return highScores.size();
    }

    @Override
    public int getColumnCount() {
        return this.colName.length;
    }

    @Override
    public Object getValueAt(int row, int column) {
        HighScore h = highScores.get(row);
        if (column == 0) {
            return h.getRd().getName();
        } else if (column == 1) {
            return h.getRd().getLevel();
        }
        return h.getBaskets();
    }

    @Override
    public String getColumnName(int i) {
        return colName[i];
    }

}
