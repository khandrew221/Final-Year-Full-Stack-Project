/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import utility.Point;

/**
 *
 * @author Kathryn Andrew
 */
public class SenseEnviro extends Sense {
    
    private final Environment env;
    private final String target; 
    private double maxRange;    
    private Map<Integer, Point> samplePoints = new HashMap<>();
    
    
    public SenseEnviro(String targ, Environment e, List<Point> points) {
        this.env = e;
        for (int i = 0; i < points.size(); i++) {
            this.samplePoints.put(i, points.get(i));
            double range = samplePoints.get(i).getDistanceFrom(new Point(0,0));
            if (range > maxRange) {
                maxRange = range;
            }
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
            /*if (Double.isNaN(val)) {
                System.out.println("NAN as sense input.");
                System.out.println(bot.getPosition());
                System.out.println(samplePoints.get(slot));
                System.out.println(samplePoint);
                System.out.println(env.normValueAtVerbose(target, samplePoint));
            }*/
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
        String range = String.format("%.2f",maxRange);
        return "Field: " + target + ", sample points: " + samplePoints.size() + " , max range: " + range;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.target);
        hash = 41 * hash + Objects.hashCode(this.samplePoints);
        return hash;
    }

    /**
     * Checks target and set of points
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
        final SenseEnviro other = (SenseEnviro) obj;
        if (!Objects.equals(this.target, other.target)) {
            return false;
        }        
        HashSet<Point> thisValues = new HashSet<>(this.samplePoints.values());
        HashSet<Point> otherValues = new HashSet<>(other.samplePoints.values());        
        if (!thisValues.equals(otherValues)) {
            return false;
        }
        return true;
    }

    
}
