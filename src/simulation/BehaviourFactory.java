/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import utility.Point;

/**
 *
 * @author Kathryn Andrew
 */
public class BehaviourFactory {
    
    
    /**
     * Creates a move behaviour.  min and max must be minimum and maximum coords
     * of a rectangle.
     * @return 
     */
    public static BehaviourMove makeBehaviourMove(double maxSpd, Point min, Point max) {
        return new BehaviourMove(maxSpd, min, max);
    }
    
}