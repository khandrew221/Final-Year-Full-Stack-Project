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

    private static int nextID = 0;
    private final int ID;
    
    Sense() {
        ID = nextID;
        nextID++;
    }
    
    public int getID() {
        return ID;
    }
    
    public abstract void sensoryInput(Bot bot);
    public abstract Set<Integer> outputSlots();
    public abstract void renumberOutputs(int startSlot);
    public abstract String toString();
}
