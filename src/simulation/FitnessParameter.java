/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

/**
 *
 * @author Kathryn Andrew
 */
public enum FitnessParameter {
    
    COLLISIONS_PER_CYCLE("Collisions"),
    DISTANCE_TRAVELLED("DistanceTravelled"),
    CURRENT_ENERGY("CurrentEnergy");
    
    public final String label;

    private FitnessParameter(String label) {
        this.label = label;
    }
    
}
