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
    
    GRep genetics;
    NNAdapter nn;
    Set<Sense> senses;
    Set<Behaviour> behaviours;
    double[] inputs;
    double[] outputs;
    
    double energy;
    Point position;
    
    
    
    public Bot(GRep g) {
        genetics = g;
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
