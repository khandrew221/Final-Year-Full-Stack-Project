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
public class Simulation {
    
    private int nnInputs = 0;
    private int nnOutputs = 0;
    private Environment environment;
    private Set<Bot> bots;
    private Set<Sense> senses;
    private Set<Behaviour> behaviours;
    
    public Simulation(int envXsize, int envYsize) {
        environment = new Environment(envXsize, envYsize);
        bots = new HashSet<>();
        senses = new HashSet<>();
        behaviours = new HashSet<>();
    }
        
    /**
     * Adds a sense and sets its corresponding input slots.
     * 
     * Req for: UC003
     * @param s 
     */
    public void addSense(Sense s) {
        s.renumberInputs(nnInputs);
        senses.add(s);
        setNNInputs();
    }
    

    /**
     * Adds a behaviour and sets its corresponding output slots.
     * 
     * Req for: UC004
     * @param b 
     */
    public void addBehaviour(Behaviour b) {
        b.renumberOutputs(nnOutputs);
        behaviours.add(b);
        setNNOutputs();
    }    
    
    
    /**
     * Sets the nnInputs value to that required by the sense set.
     * 
     * Req for: UC003
     */
    private void setNNInputs() {
        nnInputs = 0;
        for (Sense s : senses) {
            nnInputs += s.inputSlots().size();
        }     
    }
    
    /**
     * Sets the nnOutputs value to that required by the behaviour set.
     * 
     * Req for: UC004
     */
    private void setNNOutputs() {
        nnOutputs = 0;
        for (Behaviour b : behaviours) {
            nnOutputs += b.outputSlots().size();
        }
    }
    
    
    
    /**
     * 
     * 
     * Req for: TESTING
     */
    public Set<Integer> getInputSlots() {
        Set<Integer> out = new HashSet<>();
        for (Sense s : senses) {
            out.addAll(s.inputSlots());
        }
        return out;
    }
    
    /**
     * 
     * 
     * Req for: TESTING
     */
    public Set<Integer> getOutputSlots() {
        Set<Integer> out = new HashSet<>();
        for (Behaviour b : behaviours) {
            out.addAll(b.outputSlots());
        }
        return out;
    }    

    /**
     * 
     * 
     * Req for: TESTING
     */    
    public int getNnInputs() {
        return nnInputs;
    }

    /**
     * 
     * 
     * Req for: TESTING
     */    
    public int getNnOutputs() {
        return nnOutputs;
    }
    
    
    /**
     * 
     * 
     * Req for: TESTING
     */    
    Environment getEnv() {
        return environment;
    }   

    /**
     * 
     * 
     * Req for: TESTING
     */       
    Set<Sense> getSenses() {
        return senses;
    }

    /**
     * 
     * 
     * Req for: TESTING
     */     
    public Set<Behaviour> getBehaviours() {
        return behaviours;
    }
    
    
    
    
}
