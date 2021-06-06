/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.HashSet;
import java.util.Set;
import utility.Point;

/**
 *
 * @author Kathryn Andrew
 */
public class BehaviourDEBUG extends Behaviour {
    
    private Point testVal;
    
    
    public BehaviourDEBUG(Point testVal) {
        this.testVal = testVal;
    }
    
    
    @Override
    public void execute(Bot bot) {        
        bot.setPosition(testVal);
    }
    
    
    /**
     * Returns the set of output slots.
     * 
     * Req for: TESTING
     * 
     * @return 
     */
    public Set<Integer> inputSlots() {
        Set<Integer> out = new HashSet<>();
        return out;
    }
    
    
    /**
     * Renumbers the output slots.  This must be called after adding the sense,
     * and called for all senses when one is removed.
     * 
     * Req for: UC004
     * 
     * @return 
     */    
    public void renumberInputs(int startSlot) {
       
    }
    
    @Override 
    public String toString() {
        return "Type: debug, value: " + testVal;
    }  
}
