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
public class Bot {
    
    private GRep genetics;
    private NNAdapter nn;
    private Set<Sense> senses;
    private Set<Behaviour> behaviours;
    private double[] inputs;
    private double[] outputs;
    
    private double energy;
    private Point position;
    
    
    
    public Bot(GRep g, Set<Sense> s, Set<Behaviour> b, double e) {
        genetics = g;
        senses = s;
        behaviours = b; 
        inputs = new double[g.getNUM_INPUTS()];
        outputs = new double[g.getNUM_OUTPUTS()];
        position = new Point(0,0);
        energy = e;
        nn = new EncogAdapter();
        nn.createFromGRep(g);
    }
    
    
    
    /**
     * Returns the output from the specified slot
     * @param slot
     * @return 
     */
    public double getOutput(int slot) {
        return outputs[slot];
    }
    
}
