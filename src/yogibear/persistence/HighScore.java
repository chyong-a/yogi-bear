/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package yogibear.persistence;

import java.util.Objects;
import yogibear.model.RecordData;

/**
 *
 * @author artur
 */
public class HighScore {

    private final RecordData rd;
    private final int baskets;

    /**
     * Initializes HighScore
     * @param rd
     * @param baskets 
     */
    public HighScore(RecordData rd, int baskets) {
        this.rd = rd;
        this.baskets = baskets;
    }

    /**
     * Initializes HighScore
     * @param name
     * @param level
     * @param baskets 
     */
    public HighScore(String name, int level, int baskets) {
        this.rd = new RecordData(name, level);
        this.baskets = baskets;
    }

    public RecordData getRd() {
        return rd;
    }

    public int getBaskets() {
        return baskets;
    }

    /**
     * Checks if two instances of HighScore are equal
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
        HighScore other = (HighScore) obj;
        return baskets == other.baskets && Objects.equals(rd, other.rd);
    }

    /**
     * Returns the hash code of HighScore instance
     * @return 
     */
    @Override
    public int hashCode() {
        return Objects.hash(rd, baskets);
    }

}
