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
public enum BehaviourType {
    MOVE("move");
    
    public final String label;

    private BehaviourType(String label) {
        this.label = label;
    }
}
