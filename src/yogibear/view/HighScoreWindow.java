/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package yogibear.view;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.WindowConstants;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import yogibear.persistence.HighScore;

/**
 *
 * @author artur
 */
public class HighScoreWindow extends JDialog {

    private final JTable table;

    /**
     * Initializes HighScoreWindow
     * @param highScores
     * @param parent 
     */
    public HighScoreWindow(ArrayList<HighScore> highScores, JFrame parent) {
        super(parent, true);
        table = new JTable(new HighScoreTableModel(highScores));
        table.setFillsViewportHeight(true);

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
        table.setRowSorter(sorter);

        add(new JScrollPane(table));
        setSize(400, 400);
        setTitle("Score Board");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
