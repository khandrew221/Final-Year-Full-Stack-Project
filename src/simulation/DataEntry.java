/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Kathryn Andrew
 */
public class DataEntry {
    
    private Map<Long, Double> entries;
    double highest;
    double lowest;
    
    
    public DataEntry() {
        entries = new HashMap<>();
        highest = Double.MIN_VALUE;
        lowest = Double.MAX_VALUE;
    }
    
    
    public void addEntry(long time, double value) {
        entries.put(time, value);
        if (value > highest)
            highest = value;
        if (value < lowest)
            lowest = value;        
    }

    public double getHighest() {
        return highest;
    }

    public double getLowest() {
        return lowest;
    }

    public Map<Long, Double> getEntries() {
        Map<Long, Double> out = new HashMap<>();
        for (long key : entries.keySet()) {
            out.put(key, entries.get(key));
        }
        return out;
    }
    
    public double getEntry(long time) {
        if (entries.containsKey(time)) {
            return entries.get(time);
        } else {
            return 0;
        }
    }    
    
}
