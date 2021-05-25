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
public class SenseDEBUG extends Sense {
    
    int outSlot;
    double testVal;
    
    
    public SenseDEBUG(double val) {
        testVal = val;
    }
    
    @Override
    public Set<Integer> outputSlots() {
        return new HashSet<Integer>();
    }
    
    @Override
    public void sensoryInput(Bot bot) {        
        bot.setInput(outSlot, testVal);
    }    
    
    @Override
    public void renumberOutputs(int startSlot) {
        outSlot = startSlot;
    }
    
}
