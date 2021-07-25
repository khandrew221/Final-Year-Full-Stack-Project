/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import controls.SimConsts;
import controls.SimState;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import utility.Point;

/**
 *
 * @author Kathryn Andrew
 */
public class Simulation {
    
    private long simTime;
    private SimState runState;    
    private int maxPop;
    private int nnInputs = 0;
    private int nnOutputs = 0;
    
    private Environment environment;
    private GeneticAlgorithmEngine GAEngine;
    
    private Set<Sense> senses;
    private Set<Behaviour> behaviours;
    private SortedSet<Bot> bots;
   
    
    public Simulation(int envXsize, int envYsize, int maxPop) {
        this.maxPop = maxPop;
        this.environment = new Environment(envXsize, envYsize);
        this.bots = new TreeSet<>();
        this.senses = new HashSet<>();
        this.behaviours = new HashSet<>();
        this.runState = SimState.RUNNING;
        this.GAEngine = new GeneticAlgorithmEngine(SimConsts.getMAX_LAYERS(), SimConsts.getMAX_NODES_PER_LAYER());
    }
      
    
    /**
     * Initialises the simulation. TEMPORARY HARDCODED
     * 
     * Req for: UC002
     */    
    public synchronized void initialise() {
              
        clearSimulation();
        maxPop = 100;
        
        environment.addField("Test1", 11, 0, 100, Color.GREEN);
        environment.randomiseField("Test1", 0, 100);    
        
        addSense(SenseFactory.MakeEnvironmentSense("Test1", environment, true, 0, 0, 0));
        //addSense(SenseFactory.MakeBorderSense(environment,1));
        addBehaviour(new BehaviourMove(1, new Point(0,0), new Point(environment.getXSize(), environment.getYSize())));
        
        addBehaviour(new BehaviourEat(0.1, 1, "Test1", environment));
        
        for (int i = 0; i < maxPop*0.5; i++) {
            addStarterBot(SimConsts.getSTART_ENERGY());
        }  
        
        this.runState = SimState.RUNNING;
    }

    /**
     * Clears all for reinitialising.
     * 
     * Req for: UC002
     * 
     */
    public synchronized void clearSimulation() {     
        simTime = 0;
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
     */    
    public synchronized void run() {        
        
        if (runState == SimState.RUNNING) {
    
            for (Bot bot : bots) {
                bot.run();
                GAEngine.calcFitness(bot);
            }
            
            bots.removeIf(bot -> bot.isDead());

            if (population() < maxPop) {
                GRep g;
                if (bots.size() > SimConsts.getGENETIC_BOTTLENECK_POPULATION()) {  //avoids genetic bottleneck
                    addBredBot(SimConsts.getSTART_ENERGY());
                }
                else {                    
                   addStarterBot(SimConsts.getSTART_ENERGY()); 
                }  
            }  
         
            simTime++;         
           
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
        setState(SimState.STOPPED_WITH_CRITICAL_CHANGE);
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
        setState(SimState.STOPPED_WITH_CRITICAL_CHANGE);
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
        setState(SimState.STOPPED_WITH_CRITICAL_CHANGE);
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
        setState(SimState.STOPPED_WITH_CRITICAL_CHANGE);
    }
    
    /**
     * Adds a randomised bot with no ancestors to the simulation.
     * 
     * Req for: UC006
     */
    public synchronized void addStarterBot(int startEnergy) {
        if (population() < maxPop) {
            GRep g = GAEngine.randomGRep(nnInputs, nnOutputs);
            Bot bot = new Bot(g, senses, behaviours, startEnergy, environment.randomPosition());
            bots.add(bot);                
        }
    }     
    
    /**
     * Adds a bot bred from two parents to the simulation.  Note that the two
     * parents may be identical!
     * 
     * pre: bots is not empty
     * 
     * Req for: UC032
     */
    public synchronized void addBredBot(int startEnergy) {
        if (population() < maxPop) {
            updateBotOrder(); //only need to update for GA methods
            GRep g = GAEngine.breedGRep(bots, true); //breed with mutation
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
     * Returns a list of maps describing bot status.  All map values should be
     * primitive types (inc. String) or standard 
     * collections of primitive types.
     * 
     * PosX     double      bot's X position in the environment 
     * PosY     double      bot's Y position in the environment 
     * ID       long        bot's ID number
     * 
     * Req for: UC020
     */     
    public synchronized List<Map<String, Object>> botReport() { 
        List<Map<String, Object>> out = Collections.synchronizedList(new ArrayList<>());   
        synchronized(bots) {
            for (Bot bot : bots) {
                Map<String, Object> b = new HashMap<>();  
                b.put("PosX", bot.getPosition().getX());
                b.put("PosY", bot.getPosition().getY());
                b.put("ID", bot.getID());
                out.add(b);
            }   
        }        
        return out;
    }    
    
    /**
     * 
     * Returns a list of maps describing the environment fields.  All map values 
     * should be primitive types (inc. String) or arrays of primitive types.
     * 
     * name     String      field name
     * RGB      int[]       R, G and B colour components
     * Xsamples int         number of samples on the x axis
     * Ysamples int         number of samples on the y axis
     * values   double[][]  multidimensional array of normalised values
     * 
     * Req for: UC021
     * @return 
     */     
    public synchronized List<Map<String, Object>> fieldsReport() { 
        List<Map<String, Object>> out = new ArrayList<>();   
        for (String field : listFields()) {
            Map<String, Object> n = new HashMap<>();  
            n.put("name", field);
            n.put("RGB", getFieldRGB(field));
            n.put("Xsamples", environment.getXSamples(field));
            n.put("Ysamples", environment.getYSamples(field));
            n.put("values", environment.getValues(field));
            out.add(n);
        }           
        return out;
    }

    /**
     * 
     * Returns a map of data describing the overall simulation state.  
     * All map values should be primitive types (inc. String) or arrays of 
     * primitive types.
     * 
     * time         long            current simulation cycle
     * population   int             current population
     * senses       String[]        all senses text described
     * behaviours   String[]        all behaviours text described 
     * 
     * Req for: UC021
     * @return 
     */     
    public synchronized Map<String, Object> simulationReport() { 
        Map<String, Object> out = new HashMap<>();
        out.put("time", simTime);
        out.put("population", population());
        
        out.put("senses", senseReport());       
        out.put("behaviours", behaviourReport());        
        
        return out;
    }   
    
    /**
     * 
     * Req for: UC003, UC029
     * @return 
     */
    public synchronized Map<String, String> senseReport() {
        Map<String, String> out = new HashMap<>();
        for (Sense s : senses) {
            out.put(Integer.toString(s.getID()), s.toString());
        }        
        return out;
    }     
    
    /**
     * 
     * Req for: UC004, UC030
     * @return 
     */
    public synchronized Map<String, String> behaviourReport() {
        Map<String, String> out = new HashMap<>();
        for (Behaviour b : behaviours) {
            out.put(Integer.toString(b.getID()), b.toString());
        }        
        return out;
    }        
    
    /**
     * returns the color of the named field, or white if no field with that name
     * is found.
     * 
     * Req for: UC017, UC021
     * 
     * @param name 
     */
    private int[] getFieldRGB(String field) {
        int[] out = new int[3];
        Color c = environment.getColor(field);
        out[0] = c.getRed();
        out[1] = c.getGreen();
        out[2] = c.getBlue();
        return out;
    }     
    
    
    /**
     * 
     * 
     * Req for: UC002
     */    
    public long time() {
        return simTime;
    }    
    
    /**
     * Returns the current bot population
     * 
     * Req for: UC022
     */
    public int population() {
        return bots.size();
    }   
    
    /**
     * Returns the environment x size
     * 
     * Req for: UC017
     */
    public int envXSize() {
        return environment.getXSize();
    }  
    
    /**
     * Returns the environment y size
     * 
     * Req for: UC017
     */
    public int envYSize() {
        return environment.getYSize();
    }      
    
    /**
     * Adds an environment field.
     * 
     * Req for: UC007
     */
    public void addField(String name, int density, Color color) {
        environment.addField(name, density, 0, 1, color);
        setState(SimState.STOPPED_WITH_CRITICAL_CHANGE);
    }
    
    /**
     * Restarts the simulation with the same settings but new bots.  
     * TEMPORARILY HARDCODED
     * 
     * Req for: UC026
     */    
    public synchronized void restart() {
        
        simTime = 0;
        
        bots.clear();
        for (int i = 0; i < maxPop*0.5; i++) {
            addStarterBot(SimConsts.getSTART_ENERGY());
        }          
        
        
        
        if (getState() == SimState.STOPPED_WITH_CRITICAL_CHANGE) {
            setState(SimState.STOPPED);
        }
        
    }    
    
    /**
     * Sets the simulation's running state. Should be set from a SimControl 
     * object's specific methods.
     * 
     * Req for: UC024, UC025, UC026
     * 
     * @param newState 
     */
    public void setState(SimState newState) {
        runState = newState;
    }
    
    /**
     * Gets the simulation's running state. 
     * 
     * Req for: UC024, UC025, UC026
     * 
     */
    public SimState getState() {
        return runState;
    } 
    
    /**
     * Removes fields with the given names.
     * 
     * Req for: UC028
     */
    public void removeFields(Set<String> toRemove) {
        environment.removeFields(toRemove);
        setState(SimState.STOPPED_WITH_CRITICAL_CHANGE);
    }      
    
    /**
     * Returns the Genetic Algorithm Engine
     * 
     * Req for: UC033
     * 
     * @return 
     */
    public GeneticAlgorithmEngine getGAEngine() {
        return GAEngine;
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
     *  Req for: UC003, UC007
     */     
    public Set<String> listFields() {
        return environment.listFields();
    }   
    

    /**
     * 
     * 
     *  Req for: UC003
     */     
    public Environment getEnvironment() {
        return environment;
    }
    
    /**
     * 
     * Req for: UC003
     * @param s
     * @return 
     */
    public synchronized boolean containsSense(Sense s) {
        for (Sense sense : senses) {
            if(sense.equals(s))
                return true;
        }
        return false;
    }
    
    /**
     * 
     * Req for: UC004
     * @return 
     */
    public synchronized boolean containsMoveBehaviour() {
        for (Behaviour behaviour : behaviours) {
            if(behaviour instanceof BehaviourMove) {
                return true;
            }                
        }
        return false;
    }    
    
    
    /**
     * 
     * Req for: UC029
     * @param ids
     */
    public synchronized void removeSenses(Set<Integer> ids) {
        if (!ids.isEmpty()) {    
            int oldSize = senses.size();
            senses.removeIf(sense -> ids.contains(sense.getID()));
            int newSize = senses.size();
            if (oldSize != newSize) {
                int startSlot = 0;
                for (Sense sense : senses) {
                    sense.renumberOutputs(startSlot);
                    startSlot += sense.outputSlots().size();
                }
                nnInputs = startSlot;
                setState(SimState.STOPPED_WITH_CRITICAL_CHANGE);
            }
        }
    }    
    
    
    /**
     * 
     * Req for: UC030
     * @param ids
     */
    public synchronized void removeBehaviours(Set<Integer> ids) {
        if (!ids.isEmpty()) {
            int oldSize = behaviours.size();
            behaviours.removeIf(behaviour -> ids.contains(behaviour.getID()));
            int newSize = behaviours.size();
            if (oldSize != newSize) {
                int startSlot = 0;
                for (Behaviour behaviour : behaviours) {
                    behaviour.renumberInputs(startSlot);
                    startSlot += behaviour.inputSlots().size();
                }
                nnOutputs = startSlot;
                setState(SimState.STOPPED_WITH_CRITICAL_CHANGE);
            }
        }
    }   
    
    /**
     * Updates the order of the bot list. 
     * This is achieved by building a new SortedSet from the old one, then 
     * assigning the new set to the bots variable.  Not an ideal solution but
     * workable.
     */
    public synchronized void updateBotOrder() {
        SortedSet newBots = new TreeSet<>();
        for(Bot bot : bots) {
            newBots.add(bot);
        }
        bots = newBots;
    }

    /**
     * Req for: testing.
     * @return 
     */
    public SortedSet<Bot> getBots() {
        return bots;
    }
    
}
