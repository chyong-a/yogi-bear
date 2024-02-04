/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package yogibear.model;

import java.util.Objects;

/**
 *
 * @author artur
 */
public class Position {

    private int x, y;

    /**
     * Initializes Position
     * @param x
     * @param y 
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Returns the new position based on the given direction
     * @param d
     * @return 
     */
    public Position translate(Direction d) {
        return new Position(x + d.x, y + d.y);
    }

    /**
     * Checks if two instances of Position are equal
     * @param obj
     * @return 
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Position position = (Position) obj;
        return x == position.x && y == position.y;
    }

    /**
     * Returns the hash code of the Position instance
     * @return 
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

}
