/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Kathryn Andrew
 */
public class BehaviourMove extends Behaviour {
    
    int moveDirSlot;
    int moveSpeedSlot;
    
    
    public void execute(Bot bot) {
        
        double dir = bot.getOutput(moveDirSlot);
        double speed = bot.getOutput(moveSpeedSlot);
        
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
        out.add(moveDirSlot);
        out.add(moveSpeedSlot);
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
        moveDirSlot = startSlot;
        moveSpeedSlot = startSlot+1;        
    }   
    
}
