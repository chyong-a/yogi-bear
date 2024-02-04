/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package yogibear.model;

/**
 *
 * @author artur
 */
public enum LevelItem {
    BASKET('B'), EMPTY(' '), MOUNTAIN('M'), TREE('T');

    public final char representation;

    /**
     * Initializes LevelItem
     * @param representation 
     */
    LevelItem(char representation) {
        this.representation = representation;
    }

}
