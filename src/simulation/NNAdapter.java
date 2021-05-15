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

    public void createFromGRep(GRep g);
    public double[] output(double[] input);
    
    //necessary?
    public int getLayerCount();
    public int getInputCount();
    public int getOutputCount();   
    public int getNodesInLayer(int i);
    public int getTotalNodeCount();
    
}
