/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import java.util.HashMap;
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
    private Map<Integer, Point> points = new HashMap<>();
    
    
    public SenseEnviro(String targ, Environment e) {
        this.env = e;
        points.put(0, new Point(0,0));
        target = targ;
    }
    
    
    @Override
    public Set<Integer> outputSlots() {
        return points.keySet();
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
        for (int slot : points.keySet()) {   
            Point samplePoint = Point.displace(bot.getPosition(), points.get(slot));
            double val = env.normValueAt(target, samplePoint);
            bot.setInput(slot, val);
        }        
    }    
    
    @Override
    public void renumberOutputs(int startSlot) {
        Map<Integer, Point> newPoints = new HashMap<>();
        int slot = startSlot;
        for (int k : points.keySet()) {
            newPoints.put(slot, points.get(k));
            slot++;
        }
        points = newPoints;
    }
    
}
