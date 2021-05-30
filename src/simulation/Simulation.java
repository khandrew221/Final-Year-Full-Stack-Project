/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import controls.SimConsts;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import utility.Point;

/**
 *
 * @author Kathryn Andrew
 */
public class Simulation {
    
    private int maxPop;
    private int nnInputs = 0;
    private int nnOutputs = 0;
    private Environment environment;
    private TreeSet<Bot> bots;
    private Set<Sense> senses;
    private Set<Behaviour> behaviours;
    
    public Simulation(int envXsize, int envYsize, int maxPop) {
        this.maxPop = maxPop;
        environment = new Environment(envXsize, envYsize);
        bots = new TreeSet<>();
        senses = new HashSet<>();
        behaviours = new HashSet<>();
    }
      
    
    /**
     *Initialises the simulation. TEMPORARY HARDCODED
     * 
     * Req for: UC002
     */    
    public void initialise() {
              
        clearSimulation();
        maxPop = 100;
        
        environment.addField("Test1", 11, 0, 100);
        
        addSense(new SenseEnviro("Test1", environment));
        addBehaviour(new BehaviourMove(1, new Point(0,0), new Point(environment.getXSize(), environment.getYSize())));
        
        
        Random random = new Random();
        
        for (int i = 0; i < 100; i++) {
            addStarterBot(10, 5, random.nextInt(6));
        }
    }

    /**
     * Clears all for re initialising
     * 
     * Req for: UC002
     * 
     */
    public void clearSimulation() {        
        bots.clear();
        environment.clearAllFields();
        senses.clear();
        behaviours.clear();  
        nnInputs = 0;
        nnOutputs = 0;
    }
    
    /**
     * Runs a single simulation cycle.
     * 
     * Req for: UC010
     * @param s 
     */    
    public void run() {        
        for (Bot bot : bots) {        
            bot.run();
        }
        bots.removeIf(i -> i.isDead());
 
        if (population() < maxPop) {
            addStarterBot(SimConsts.getMAX_LAYERS(), SimConsts.getMAX_NODES_PER_LAYER(), 10);
        }
                    
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
        if (population() < maxPop) {
            GRep g = new GRep(MAX_LAYERS, MAX_NODES_PER_LAYER, nnInputs, nnOutputs);
            g.randomise();
            Bot bot = new Bot(g, senses, behaviours, startEnergy, environment.randomPosition());
            bots.add(bot);            
        }
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
    int getMaxPop() {
        return maxPop;
    }    

    /**
     * 
     * 
     * Req for: TESTING
     */       
    List<Boolean> listIsDead() {
        List<Boolean> out = new ArrayList<>();
        for (Bot bot : bots) {
            out.add(bot.isDead());
        }
        return out;
    }     
    
    /**
     * 
     * 
     * Req for: TESTING
     */     
    public Set<Behaviour> getBehaviours() {
        return behaviours;
    }
    
    /**
     * 
     * 
     * Req for: TESTING
     */     
    public Set<String> listFields() {
        return environment.listFields();
    }    
    
    
}
