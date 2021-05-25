/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import utility.Point;

/**
 *
 * @author Kathryn Andrew
 */
public class Simulation {
    
    private int nnInputs = 0;
    private int nnOutputs = 0;
    private Environment environment;
    private TreeSet<Bot> bots;
    private Set<Sense> senses;
    private Set<Behaviour> behaviours;
    
    public Simulation(int envXsize, int envYsize) {
        environment = new Environment(envXsize, envYsize);
        bots = new TreeSet<>();
        senses = new HashSet<>();
        behaviours = new HashSet<>();
    }
      
    /**
     * Runs a single simulation cycle.
     * 
     * Req for: UC010
     * @param s 
     */    
    public void run() {
        

    }
    
    /**
     * Adds a sense and sets its corresponding input slots.
     * 
     * Req for: UC003
     * @param s 
     */
    public void addSense(Sense s) {
        s.renumberOutputs(nnInputs);
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
        b.renumberInputs(nnOutputs);
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
            nnInputs += s.outputSlots().size();
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
            nnOutputs += b.inputSlots().size();
        }
    }
    
    /**
     * Adds a randomised bot with no ancestors to the simulation.
     * 
     * Req for: UC006
     */
    public void addStarterBot(int MAX_LAYERS, int MAX_NODES_PER_LAYER, int startEnergy) {
        GRep g = new GRep(MAX_LAYERS, MAX_NODES_PER_LAYER, nnInputs, nnOutputs);
        g.randomise();
        Bot bot = new Bot(g, senses, behaviours, startEnergy, new Point(0,0));
        bots.add(bot);
    }    
    
    /**
     * Returns the current bot population
     * 
     * Req for: testing
     */
    public int population() {
        return bots.size();
    }    
    
    /**
     * 
     * 
     * Req for: TESTING
     */
    public Set<Integer> getInputSlots() {
        Set<Integer> out = new HashSet<>();
        for (Sense s : senses) {
            out.addAll(s.outputSlots());
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
            out.addAll(b.inputSlots());
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
