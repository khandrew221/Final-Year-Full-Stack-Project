/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import utility.Point;

/**
 *
 * @author Kathryn Andrew
 */
public class SenseEnviro extends Sense {
    
    private final Environment env;
    private final String target; 
    private Map<Integer, Point> samplePoints = new HashMap<>();
    
    
    public SenseEnviro(String targ, Environment e, List<Point> points) {
        this.env = e;
        for (int i = 0; i < points.size(); i++) {
            samplePoints.put(i, points.get(i));
        }
        target = targ;
    }    
    
    
    @Override
    public Set<Integer> outputSlots() {
        return samplePoints.keySet();
    }
    
    /**
     * Runs the environment sense.  
     * 
     * Req for: UC012
     *
     * @param bot 
     */    
    @Override
    public void sensoryInput(Bot bot) {        
        for (int slot : samplePoints.keySet()) {   
            Point samplePoint = Point.displace(bot.getPosition(), samplePoints.get(slot));
            double val = env.normValueAt(target, samplePoint);
            bot.setInput(slot, val);
        }        
    }    
    
    @Override
    public void renumberOutputs(int startSlot) {
        Map<Integer, Point> newPoints = new HashMap<>();
        int slot = startSlot;
        for (int k : samplePoints.keySet()) {
            newPoints.put(slot, samplePoints.get(k));
            slot++;
        }
        samplePoints = newPoints;
    }
    
    @Override
    public String toString() {
        return "Type: Enviro, " + 
                "Field: " +  target +
                ", sample points: " + samplePoints.size();
    }
    
}
