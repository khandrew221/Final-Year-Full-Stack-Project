/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import java.util.Set;

/**
 *
 * @author Kathryn Andrew
 */
public abstract class Behaviour {
    
    public abstract void execute(Bot bot);
    public abstract Set<Integer> outputSlots();
    public abstract void renumberOutputs(int startSlot);    
}
