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
 * 
 * 
 * @author Kathryn Andrew
 */
public class BehaviourEat extends Behaviour {
    
    int slot;
    String target;
    Environment environment;
    double forageEfficiency; 
    double energyEfficiency; 
    
    /**
     * Creates an eat behaviour.
     * 
     * @param target
     * @param e
     */
    public BehaviourEat(double forageEfficiency, double energyEfficiency, String target, Environment e) {
        this.target = target;
        this.environment = e;
        this.forageEfficiency = forageEfficiency;
        this.energyEfficiency = energyEfficiency;
    }

    
    @Override
    public void execute(Bot bot) {
         if (bot.getOutput(slot) >= 0.5) {
            double eatAmount = environment.trueValueAt(target, bot.getPosition())*forageEfficiency;
            environment.adjustValueAt(target, bot.getPosition(), -eatAmount);
            bot.metabolise(eatAmount*energyEfficiency);
        }   
    }
        
    
    /**
     * Returns the set of behaviour input slots.
     * 
     * Req for: TESTING
     * 
     * @return 
     */
    @Override
    public Set<Integer> inputSlots() {
        Set<Integer> out = new HashSet<>();
        out.add(slot);
        return out;
    }
    
    
    /**
     * Renumbers the input slots.  This must be called after adding the behaviour,
     * and called for all behaviours when a behaviour is removed.
     * 
     * Req for: UC004
     * 
     */    
    @Override
    public void renumberInputs(int startSlot) {
        slot = startSlot;     
    }   

    
    @Override
    public String toString() {
        return "Type: eat, " + 
                "target: " +  target;
    }
}
