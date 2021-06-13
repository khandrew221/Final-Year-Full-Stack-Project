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

    private static int nextID = 0;
    private final int ID;
    
    public Behaviour() {
        ID = nextID;
        nextID++;
    }
    
    public int getID() {
        return ID;
    }
    
    public abstract void execute(Bot bot);
    public abstract Set<Integer> inputSlots();
    public abstract void renumberInputs(int startSlot);   
    public abstract String toString();    
}
