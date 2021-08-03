/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import utility.MinMaxPair;
import utility.Point;

/**
 *
 * @author Kathryn Andrew
 */
public class BehaviourFactory {
    
    public static final MinMaxPair EAT_FORAGE_EFFICIENCY_LIMITS = new MinMaxPair(-1, 1);
    public static final MinMaxPair EAT_ENERGY_EFFICIENCY_LIMITS = new MinMaxPair(-1, 1);
    
    public static final MinMaxPair MOVE_SPEED_LIMITS = new MinMaxPair(1, 5);
    
    /**
     * Creates a move behaviour.  min and max must be minimum and maximum coords
     * of a rectangle.
     * @return 
     */
    public static BehaviourMove makeBehaviourMove(double maxSpd, Point min, Point max) {
        return new BehaviourMove(maxSpd, min, max);
    }
    
    /**
     * Creates an eat behaviour
     * @param forageEfficiency
     * @param energyEfficiency
     * @param target
     * @param e
     * @return 
     */
    public static BehaviourEat makeBehaviourEat(double forageEfficiency, double energyEfficiency, String target, Environment e) {
        return new BehaviourEat(forageEfficiency, energyEfficiency, target, e);
    }    
    
}
