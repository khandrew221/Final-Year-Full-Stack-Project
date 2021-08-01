/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import utility.Point;

/**
 *
 * @author Kathryn Andrew
 */
public class SenseBorder extends Sense {
    
    Environment environment;    
    private Map<Integer, Point> points = new HashMap<>();
    
    
    public SenseBorder(Environment e, List<Point> points) {
        this.environment = e;
        for (int i = 0; i < points.size(); i++) {
            this.points.put(i, points.get(i));
        }
    }  
    
    /**
     *
     * @param bot
     */
    @Override
    public void sensoryInput(Bot bot) {
        for (int slot : points.keySet()) {   
            Point samplePoint = Point.displace(bot.getPosition(), points.get(slot));
            if (environment.inField(samplePoint)) {
                bot.setInput(slot, 0);
            } else {
                bot.setInput(slot, 1);
            }          
        }         
    }
    
    /**
     *
     * @return
     */
    @Override
    public Set<Integer> outputSlots() {
        return points.keySet();
    }
    
    /**
     *
     * @param startSlot
     */
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
    
    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "ID: " + super.getID() +  
                ", Type: Border";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.points);
        return hash;
    }

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
        final SenseBorder other = (SenseBorder) obj;
        if (!Objects.equals(this.points, other.points)) {
            return false;
        }
        return true;
    }
   
    
}
