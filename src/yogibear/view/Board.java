/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package yogibear.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import javax.swing.JPanel;
import yogibear.model.Game;
import yogibear.model.LevelItem;
import yogibear.model.Position;
import resources.ResourceLoader;

/**
 *
 * @author artur
 */
public class Board extends JPanel {

    private final Game game;
    private final Image player, basket, ranger, mountain, tree, empty;
    private double scale;
    private int scaledSize;
    private final int tileSize = 32;

    /**
     * Initializes Board
     * @param game
     * @throws IOException 
     */
    public Board(Game game) throws IOException {
        this.game = game;
        this.scale = 1.0;
        this.scaledSize = (int) (scale * this.tileSize);
        player = ResourceLoader.loadImage("resources/player.png");
        basket = ResourceLoader.loadImage("resources/basket.png");
        ranger = ResourceLoader.loadImage("resources/ranger.png");
        mountain = ResourceLoader.loadImage("resources/mountain.png");
        tree = ResourceLoader.loadImage("resources/tree.png");
        empty = ResourceLoader.loadImage("resources/empty.png");
    }

    public boolean setScale(double scale) {
        this.scale = scale;
        scaledSize = (int) (scale * tileSize);
        return refresh();
    }

    /**
     * Refreshes the board
     * @return 
     */
    public boolean refresh() {
        if (!game.isLevelLoaded()) {
            return false;
        }
        Dimension dim = new Dimension(game.getLevelCols() * this.scaledSize, game.getLevelRows() * this.scaledSize);
        setPreferredSize(dim);
        setMaximumSize(dim);
        repaint();
        return true;
    }

    /**
     * Paints the components of the board based on the game field
     * @param g 
     */
    @Override
    protected void paintComponent(Graphics g) {
        if (!game.isLevelLoaded()) {
            return;
        }
        Graphics2D gr = (Graphics2D) g;
        int w = game.getLevelCols();
        int h = game.getLevelRows();
        Position p = game.getPlayerPos();
        Position r = game.getRangerPos();
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Image img = null;
                LevelItem li = game.getItem(y, x);
                switch (li) {
                    case BASKET:
                        img = basket;
                        break;
                    case MOUNTAIN:
                        img = mountain;
                        break;
                    case TREE:
                        img = tree;
                        break;
                    case EMPTY:
                        img = empty;
                        break;
                }
                if (p.getX() == x && p.getY() == y) {
                    img = player;
                }
                if (r.getX() == x && r.getY() == y) {
                    img = ranger;
                }
                if (img == null) {
                    continue;
                }
                gr.drawImage(img, x * scaledSize, y * scaledSize, scaledSize, scaledSize, null);
            }
        }
    }

}
