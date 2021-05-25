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
    
    private GRep genetics;
    private NNAdapter nn;
    private Set<Sense> senses;
    private Set<Behaviour> behaviours;
    private double[] inputs;
    private double[] outputs;
    
    private double energy;
    private Point position;
    
    
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
        genetics = g;
        senses = s;
        behaviours = b; 
        inputs = new double[g.getNUM_INPUTS()];
        outputs = new double[g.getNUM_OUTPUTS()];
        position = new Point(p.getX(),p.getY());
        energy = e;
        nn = new EncogAdapter();
        nn.createFromGRep(g);
    }
    
    
    
    /**
     * Sets the bot's position
     * 
     * Req for: UC014
     * 
     * @return 
     */    
    public void setPosition(Point p) {
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
     * Req for: UC012
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
    public void runNN() {
        outputs = nn.output(inputs);
    }     
    
    /**
     * 
     * 
     * Req for: UC011
     *
     */
    public void run() {
        
        if (energy > 0) {            
            for (Sense s : senses) {
                s.sensoryInput(this);
            }            
            runNN();
            for (Behaviour b : behaviours) {
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
     * Returns a copy of the neural network outputs
     * 
     * Req for: UC013
     *
     */
    public double[] getOutputs() {
        return outputs.clone();
    }      
    
    
    @Override
    /**
     * Allows autosorting via treeset in the simulation by bot energy 
     */
    public int compareTo(Bot bot) {
        if (this.energy > bot.energy)
            return 1;
        else
            return -1; 
    }
    
    
}
