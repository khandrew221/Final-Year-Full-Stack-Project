/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import java.util.Set;
import utility.Point;

/**
 *
 * @author Kathryn Andrew
 */
public class Bot implements Comparable<Bot> {
    
    private static long nextID = 0;
    
    private long id;
    
    private GRep genetics;
    private NNAdapter nn;
    private Set<Sense> senses;
    private Set<Behaviour> behaviours;
    private double[] inputs;
    private double[] outputs;
    
    private double energy;
    private Point position;
    
    private int age;
    private double distanceTravelled;
    private int collisions;    
    
    private double fitness;
    
    
    /**
     * 
     * Req. for: UC015
     * 
     * @param g
     * @param s
     * @param b
     * @param e 
     */
    
    public Bot(GRep g, Set<Sense> s, Set<Behaviour> b, double e, Point p) {
        id = nextID;
        if (nextID++ == Long.MIN_VALUE)
            nextID = 0; 
        genetics = g;
        senses = s;
        behaviours = b; 
        inputs = new double[g.getNUM_INPUTS()];
        outputs = new double[g.getNUM_OUTPUTS()];
        position = new Point(p.getX(),p.getY());
        energy = e;
        nn = new EncogAdapter();
        nn.createFromGRep(g);
        age = 0;
        distanceTravelled = 0;
        collisions = 0;
        fitness = 0;
    }
    
    
    
    /**
     * Sets the bot's position
     * 
     * Req for: UC014
     */    
    public void setPosition(Point p) {
        double dist = p.getDistanceFrom(position);
        this.distanceTravelled += dist;
        position = new Point(p.getX(), p.getY());
    }         
    
    
    /**
     * Returns the output from the specified slot
     * 
     * Req for: UC014
     * 
     * @param slot
     * @return 
     */
    public double getOutput(int slot) {
        return outputs[slot];
    }
    
    /**
     * Sets the input for a neural network sense slot.
     * 
     * Req for: UC012
     *
     */
    public void setInput(int slot, double value) {
        inputs[slot] = value;
    }

    /**
     * 
     * 
     * Req for: UC012, UC020
     *
     */
    public Point getPosition() {
        return new Point(position.getX(), position.getY());
    }    
    
    /**
     * 
     * 
     * Req for: UC013
     *
     */
    private void runNN() {
        outputs = nn.output(inputs);
    }     
    
    /**
     * 
     * 
     * Req for: UC011
     *
     */
    public void run() {   
        metabolise();
        incrementAge();
        if (energy > 0) {            
            for (Sense s : senses) {
                s.sensoryInput(this);
            }            
            runNN();
            for (Behaviour b : behaviours) {
                if (energy > 0)
                    b.execute(this);
            }           
        }        
    }     
    
    
     /**
     * Returns a copy of the neural network inputs
     * 
     * Req for: UC012
     *
     */
    public double[] getInputs() {
        return inputs.clone();
    }    
    
    /**
     * 
     * Temporary hard coded
     * 
     * Req for: UC011
     * 
     */
    private void metabolise() {
        energy -= 1;
    }
    
    
     /**
     * Returns a copy of the neural network outputs
     * 
     * Req for: UC013
     *
     */
    public double[] getOutputs() {
        return outputs.clone();
    }      
    
     /**
     * Returns whether or not the bot is dead
     * 
     * Req for: UC010
     *
     */
    public boolean isDead() {
        if (energy > 0)
            return false;
        else
            return true;
    }       
    
    
    /**
     * Return's the bot's current energy.
     * 
     * Req for: testing
     * 
     * @return 
     */
    public double getEnergy() {
        return energy;
    }
    
     /**
     * Returns the bot's id number
     * 
     * Req for: UC020
     *
     */
    public long getID() {
        return id;
    }    
    
    
    @Override
    /**
     * Allows autosorting by bot fitness via treeset in the simulation.  
     */
    public int compareTo(Bot bot) {
        if (this.equals(bot)) 
            return 0;
        if (this.getFitness() < bot.getFitness())
            return 1;
        else
            return -1; 
    }
    
    
    /**
     * Returns the genetic representation 
     * 
     * Req for: TESTING
     *
     */
    GRep getGRep() {
        return this.genetics;
    } 
    
    /**
     * Returns the sense set
     * 
     * Req for: TESTING
     *
     */
    Set<Sense> getSenses() {
        return this.senses;
    }   
    
    /**
     * Returns the behaviour set
     * 
     * Req for: TESTING
     *
     */
    Set<Behaviour> getBehaviours() {
        return this.behaviours;
    }     

    
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    
    /**
     * Adds 1 to the bot age
     */
    public void incrementAge() {
        this.age += 1;
    }    

    public double getDistanceTravelled() {
        return distanceTravelled;
    }

    public void setDistanceTravelled(double distanceTravelled) {
        this.distanceTravelled = distanceTravelled;
    }
   
    
    /**
     * Adds 1 to number of bot collisions
     */
    public void incrementCollisions() {
        this.collisions += 1;
    }     

    public int getCollisions() {
        return collisions;
    }

    public double getFitness() {
        return fitness;
    }
    
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }    
    
    /**
     * Returns this bot's number of collisions per cycle, or 0 for a bot at
     * age 0
     * Req for: fitness function
     * @return 
     */    
    public double getCollisionsPerCycle() {
        if (age > 0) {
            return (double) collisions / age;
        }
        return 0;
    }
    
    /**
     * Returns the maximum move speed of the bot
     * Req for: fitness function
     * @return 
     */
    public double getMaxMoveSpeed() {
        for (Behaviour behaviour : behaviours) {
            if(behaviour instanceof BehaviourMove) {
                return (((BehaviourMove) behaviour).getMaxSpeed()*Math.sqrt(2));
            }                
        }
        return 0;
    }
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Bot other = (Bot) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }    
}
