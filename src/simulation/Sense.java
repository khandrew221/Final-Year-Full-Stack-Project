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
public abstract class Sense {
    
    abstract void sensoryInput(Bot bot);
    abstract Set<Integer> inputSlots();
}