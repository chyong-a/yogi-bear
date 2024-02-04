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
public class RecordData {

    private final String name;
    private final int level;

    /**
     * Initializes RecordData
     * @param name
     * @param level 
     */
    public RecordData(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    /**
     * Returns hash code of the RecordData instance
     * @return 
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.name);
        hash = 29 * hash + this.level;
        return hash;
    }

    /**
     * Checks if two instances of RecordData are equal
     * @param obj
     * @return 
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RecordData other = (RecordData) obj;
        return !(this.level != other.level || !Objects.equals(this.name, other.name));
    }
}
