/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import controls.SimConsts;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

/**
 * Note: genome.length() will produce an incorrect length, without leading zeros.  
 * Since bitset's get() will produce a 0 value at any point beyond its given length,
 * it is safe to use requiredLength() when dealing with the genome length
 * @author Kathryn Andrew
 */
public class GRep {
    
    private BitSet genome;
    
    public GRep() {
        genome = new BitSet(requiredLength());
    }
    
    /**
     * Randomises the entire genome, with a 50/50 probability of 0 or 1 for each bit.
     */
    public void randomise() {       
        Random random = new Random();        
        //number of longs required for the required genome length
        int length = (int) Math.ceil(requiredLength()/64.0);        
        //convert stream of random longs to bitset via .toArray() to long[], trim, and assign  
        genome = BitSet.valueOf(random.longs(length).toArray()).get(0, requiredLength());  
    }
    
    /**
     * Makes all bits 1 to the required length
     */
    public void maximal() {       
        genome = new BitSet(requiredLength());
        genome.flip(0, requiredLength());
    }    
    

    /**
     * Returns whether the genome codes for a node present at the given position on the inner node grid.
     * @param layer an int between 1 and MAX_LAYERS (inclusive)
     * @param node an int between 0 and MAX_NODES_PER_LAYER-1 (inclusive)
     * @return true if there should be a node at the given x,y coordinate.
     */
    public boolean nodeAt(int layer, int node) {
        int ind = (layer-1)*SimConsts.getMAX_NODES_PER_LAYER() + node;
        return genome.get(ind);
    }
    
    /**
     * Returns whether the genome codes for an active connection between the given nodes (at layer, node and layer+1, node2).
     * Note that this does NOT test whether the connection is between nodes that exist given the node grid, or any other condition
     * that causes the connection to be invalidated: it only checks the index bit of the connection gene.
     * Layer 0 represents the input layer. Layer MAX_LAYERS+1 represents the output layer.
     * @param layer an int between 0 and MAX_LAYERS (inclusive)
     * @param node an int between 0 and MAX_NODES_PER_LAYER-1 (inclusive)
     * @param node2 an int between 0 and MAX_NODES_PER_LAYER-1 (inclusive)
     * @return true if the connection gene for the connection between the two nodes has its index set to 1
     */
    public boolean connectionAt(int layer, int node, int node2) {
        if (layer == 0) {
           //input connections
           int ind = gridLength() + mainConnectionsLength() + 9*SimConsts.getMAX_NODES_PER_LAYER()*node + 9*node2;
           return genome.get(ind);
        } else {
            int ind = (layer-1)*9*SimConsts.getMAX_NODES_PER_LAYER()*SimConsts.getMAX_NODES_PER_LAYER() + 9*SimConsts.getMAX_NODES_PER_LAYER()*node + 9*node2 + gridLength();
            return genome.get(ind);            
        }
    }    
    
    /**
     * Returns the bitset representing the weight coded for the connection between the given nodes (at layer, node and layer+1, node2).
     * Note that this does NOT test whether the connection is between nodes that exist given the node grid, or any other condition
     * that causes the connection to be invalidated: it only returns the bitset representing the value that the connection would take
     * IF valid.
     * A BitSet is returned so that interpretation into a value can be coded elsewhere.
     * Layer 0 represents the input layer. Layer MAX_LAYERS+1 represents the output layer.
     * @param layer an int between 1 and MAX_LAYERS+1 (inclusive)
     * @param node an int between 0 and MAX_NODES_PER_LAYER-1 (inclusive)
     * @param node2 an int between 0 and MAX_NODES_PER_LAYER-1 (inclusive)
     * @return a bitset representing the weight of the connection between the two nodes
     */
    public BitSet weightAt(int layer, int node, int node2) {
        if (layer == 0) {
            //input connections
            int ind = gridLength() + mainConnectionsLength() + 9*SimConsts.getMAX_NODES_PER_LAYER()*node + 9*node2 + 1;
            return genome.get(ind, ind+8);
        } else {
            //main connections
            int ind = (layer-1)*9*SimConsts.getMAX_NODES_PER_LAYER()*SimConsts.getMAX_NODES_PER_LAYER() + 9*SimConsts.getMAX_NODES_PER_LAYER()*node + 9*node2 + gridLength() + 1;
            return genome.get(ind, ind+8);
        }
    }  
    
    /**
     * FOR TESTING
     * @return the bit at the given index as a boolean value
     */
    public boolean bitAt(int i) {        
        return genome.get(i);
    }   
    
    /**
     * FOR TESTING
     * @return the sub bitset from the given index i (inclusive) to the given index i2 (exclusive) 
     */
    public BitSet bitSetAt(int i, int i2) {        
        return genome.get(i, i2);
    }     
    
    /**
     * @return the required genome length based on simulation factors
     */
    public static int requiredLength() {        
        return gridLength() + mainConnectionsLength() + inputConnectionsLength();
    }
    
    /**
     * @return the number of bits dedicated to defining the node grid
     */
    public static int gridLength() {        
        return  SimConsts.getMAX_NODES_PER_LAYER()*SimConsts.getMAX_LAYERS();
    } 
    
    /**
     * @return the number of bits dedicated to defining the main and output connections
     */
    public static int mainConnectionsLength() {        
        return  9*SimConsts.getMAX_LAYERS()*SimConsts.getMAX_NODES_PER_LAYER()*SimConsts.getMAX_NODES_PER_LAYER();
    }   
    
    /**
     * @return the number of bits dedicated to defining the input connections
     */
    public static int inputConnectionsLength() {        
        return  9*SimConsts.getNumInputs()*SimConsts.getMAX_NODES_PER_LAYER();
    }     
    
    /**
     * Converts the genome BitSet to a binary string, with leading zeros.
     * @param BE if true, gives a big-endian output.  Otherwise little-endian.
     * @return a binary string representing the genome BitSet
     */
    public String toBinaryString(boolean BE) {
        StringBuilder buffer = new StringBuilder(requiredLength());
        if (BE) {
            for (int i = requiredLength()-1; i >= 0; i--) {
                buffer.append(genome.get(i) ? "1" : "0");   
            } 
        } else  {
            for (int i = 0; i < requiredLength(); i++) {
                buffer.append(genome.get(i) ? "1" : "0");   
            } 
        }
        return buffer.toString();
    }
}
