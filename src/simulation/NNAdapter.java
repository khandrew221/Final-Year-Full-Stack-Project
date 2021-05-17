/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

/**
 *
 * @author Kathryn Andrew
 */
public interface NNAdapter {

    public void createFromGRep(GRep g, int MAX_LAYERS, int MAX_NODES_PER_LAYER, int NUM_INPUTS, int NUM_OUTPUTS);
    public double[] output(double[] input);
    
    
}
