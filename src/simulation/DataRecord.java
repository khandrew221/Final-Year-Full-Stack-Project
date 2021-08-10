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
public class DataRecord {
    
    private Map<String, DataEntry> records;
        
    public DataRecord() {
        records = new HashMap<>();
    }
        
    public void addRecord(String name) {
        if (!records.keySet().contains(name))
            records.put(name, new DataEntry());
    }
    
    public void addEntry(String name, long time, double value) {
        if (records.keySet().contains(name))
            records.get(name).addEntry(time, value);        
    }
    
    
    public double getEntry(String name, long time) {
        if (records.keySet().contains(name))
            if (records.get(name).getEntries().containsKey(time)) {
                return records.get(name).getEntries().get(time);
            } else {
                return 0;
            }
        else {
            return 0;
        }
    }
    
    public Map<Long, Double> getEntries(String name) {
        Map<Long, Double> out = new HashMap<>();

        if (records.keySet().contains(name)) {
            out = records.get(name).getEntries();
        }
        
        return out;
    }    
    
    public Map<Long, Double> getSeries(String name, long start, long end) {
        Map<Long, Double> out = new HashMap<>();
        if (records.keySet().contains(name)) {
            for (long i = start; i < end; i++) {
                out.put(i,records.get(name).getEntry(i));
            }
        } 
        return out;
    }     
    
    public double getSeriesMin(String name) {
        if (records.keySet().contains(name)) {
            return records.get(name).lowest;
        } 
        return 0;
    }  
    
    public double getSeriesMax(String name) {
        if (records.keySet().contains(name)) {
            return records.get(name).highest;
        } 
        return 0;
    }     
    
    public void printAll() {
        for (String name : records.keySet()) {
            for (long t : records.get(name).getEntries().keySet()) {
                System.out.println(name + ", " + t + ", " + records.get(name).getEntries().get(t));
            }
        }
        
    }
    
}
