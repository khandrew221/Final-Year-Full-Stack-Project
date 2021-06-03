/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import controls.SimConsts;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
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
    private Set<Sense> senses;
    private Set<Behaviour> behaviours;
    private SortedSet<Bot> bots = Collections.synchronizedSortedSet(new TreeSet<Bot>());
    
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
        
        environment.addField("Test1", 11, 0, 100, Color.GREEN);
        environment.randomiseField("Test1", 0, 100);
        environment.addField("Test2", 11, 0, 100, Color.BLUE);
        environment.randomiseField("Test2", 0, 100);        
        
        addSense(new SenseEnviro("Test1", environment));
        addBehaviour(new BehaviourMove(1, new Point(0,0), new Point(environment.getXSize(), environment.getYSize())));
  
        Random random = new Random();
        
        for (int i = 0; i < maxPop; i++) {
            addStarterBot(10, 5, SimConsts.getSTART_ENERGY());
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
        
        synchronized(bots) {
            for (Bot bot : bots) {
                bot.run();
            }
            bots.removeIf(bot -> bot.isDead());
        } 

        if (population() < maxPop) {
            addStarterBot(SimConsts.getMAX_LAYERS(), SimConsts.getMAX_NODES_PER_LAYER(), SimConsts.getSTART_ENERGY());
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
     * 
     * Returns the normalised value of the given environment field at the given positions
     * Returns 0 if position is out of bounds.
     * 
     * Req for: UC017
     */     
    public double envNormValueAt(String field, double x, double y) {
        Point p = new Point(x, y);
        if (p.inBounds(new Point(0,0), new Point(environment.getXSize(), environment.getYSize()))) {
            return environment.normValueAt(field, p);
        } else {
            return 0;
        }   
    } 
    
    /**
     * 
     * Returns a list of bot positions
     * 
     * 
     * Req for: UC017
     */     
    public List<Point> botReport() { 
        List<Point> out = new ArrayList<>();        
        synchronized(bots) {
            Iterator i = bots.iterator();
            Bot b;
            while (i.hasNext()) {
                b = (Bot) i.next();
                out.add(b.getPosition());
            }    
        }        
        return out;
    }      
    
    /**
     * returns the color of the named field, or white if no field with that name
     * is found.
     * 
     * Req for: UC017
     * 
     * @param name 
     */
    public Color getFieldColor(String name) {
        return environment.getColor(name);
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
