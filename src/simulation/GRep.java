/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import java.util.BitSet;
import java.util.Random;

/**
 * Note: genome.length() will produce an incorrect length, without leading zeros.  
 * Since bitset's get() will produce a 0 value at any point beyond its given length,
 * it is safe to use requiredLength() when dealing with the genome length
 * @author Kathryn Andrew
 */
public class GRep {
    
    private final int MAX_NODES_PER_LAYER;
    private final int MAX_LAYERS;
    private final int NUM_INPUTS;
    private final int NUM_OUTPUTS;
    private BitSet genome;
    
    public GRep(int MAX_LAYERS, int MAX_NODES_PER_LAYER, int NUM_INPUTS, int NUM_OUTPUTS) {
        this.MAX_LAYERS = MAX_LAYERS;
        this.MAX_NODES_PER_LAYER = MAX_NODES_PER_LAYER;
        this.NUM_INPUTS = NUM_INPUTS;
        this.NUM_OUTPUTS = NUM_OUTPUTS;
        genome = new BitSet(requiredLength());
    }
    
    /**
     * Randomises the entire genome, with a 50/50 probability of 0 or 1 for each bit.
     * 
     * Req for: UC002
     * 
     */
    public void randomise() {       
        Random random = new Random();        
        //number of longs required for the required genome length
        int length = (int) Math.ceil(requiredLength()/64.0);        
        //convert stream of random longs to bitset via .toArray() to long[], trim, and assign  
        genome = BitSet.valueOf(random.longs(length).toArray()).get(0, requiredLength());  
    }   
    

    /**
     * Returns whether the genome codes for a node present at the given position on the inner node grid.
     * 
     * Req for: UC009
     * 
     * @param layer an int between 1 and MAX_LAYERS (inclusive)
     * @param node an int between 0 and MAX_NODES_PER_LAYER-1 (inclusive)
     * @return true if there should be a node at the given x,y coordinate.
     */
    public boolean nodeAt(int layer, int node) {
        int ind = (layer-1)*MAX_NODES_PER_LAYER + node;
        return genome.get(ind);
    }
    
    /**
     * Returns whether the genome codes for an active connection between the given nodes (at layer, node and layer+1, node2).
     * Note that this does NOT test whether the connection is between nodes that exist given the node grid, or any other condition
     * that causes the connection to be invalidated: it only checks the index bit of the connection gene.
     * Layer 0 represents the input layer. Layer MAX_LAYERS+1 represents the output layer.
     * 
     * Req for: UC009
     * 
     * @param layer an int between 0 and MAX_LAYERS (inclusive)
     * @param node an int between 0 and MAX_NODES_PER_LAYER-1 (inclusive)
     * @param node2 an int between 0 and MAX_NODES_PER_LAYER-1 (inclusive)
     * @return true if the connection gene for the connection between the two nodes has its index set to 1
     */
    public boolean connectionAt(int layer, int node, int node2) {
        if (layer == 0) {
           //input connections
           int ind = gridLength() + mainConnectionsLength() + 9*MAX_NODES_PER_LAYER*node + 9*node2;
           return genome.get(ind);
        } else {
            int ind = (layer-1)*9*MAX_NODES_PER_LAYER*MAX_NODES_PER_LAYER + 9*MAX_NODES_PER_LAYER*node + 9*node2 + gridLength();
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
     * 
     * Req for: UC009
     * 
     * @param layer an int between 1 and MAX_LAYERS+1 (inclusive)
     * @param node an int between 0 and MAX_NODES_PER_LAYER-1 (inclusive)
     * @param node2 an int between 0 and MAX_NODES_PER_LAYER-1 (inclusive)
     * @return a bitset representing the weight of the connection between the two nodes
     */
    public BitSet weightAt(int layer, int node, int node2) {
        if (layer == 0) {
            //input connections
            int ind = gridLength() + mainConnectionsLength() + 9*MAX_NODES_PER_LAYER*node + 9*node2 + 1;
            return genome.get(ind, ind+8);
        } else {
            //main connections
            int ind = (layer-1)*9*MAX_NODES_PER_LAYER*MAX_NODES_PER_LAYER + 9*MAX_NODES_PER_LAYER*node + 9*node2 + gridLength() + 1;
            return genome.get(ind, ind+8);
        }
    }  

   /**
     * Req for: constructor, multiple methods, testing 
     * @return the required genome length based on simulation factors
     */
    public int requiredLength() {        
        return gridLength() + mainConnectionsLength() + inputConnectionsLength();
    }
    
    /**
     *  Req for: constructor, multiple methods, testing 
     * @return the number of bits dedicated to defining the node grid
     */
    public int gridLength() {        
        return  MAX_NODES_PER_LAYER*MAX_LAYERS;
    } 
    
    /**
     *  Req for: constructor, multiple methods, testing 
     * @return the number of bits dedicated to defining the main and output connections
     */
    public int mainConnectionsLength() {        
        return  9*MAX_LAYERS*MAX_NODES_PER_LAYER*MAX_NODES_PER_LAYER;
    }   
    
    /**
     *  Req for: constructor, multiple methods, testing 
     * @return the number of bits dedicated to defining the input connections
     */
    public int inputConnectionsLength() {        
        return  9*NUM_INPUTS*MAX_NODES_PER_LAYER;
    }         

    /**
     *  Req for: UC008 
     * @return 
     */    
    public int getMAX_NODES_PER_LAYER() {
        return MAX_NODES_PER_LAYER;
    }
    /**
     *  Req for: UC008 
     * @return 
     */       
    public int getMAX_LAYERS() {
        return MAX_LAYERS;
    }
    /**
     *  Req for: UC008 
     * @return 
     */       
    public int getNUM_INPUTS() {
        return NUM_INPUTS;
    }
    /**
     *  Req for: UC008 
     * @return 
     */       
    public int getNUM_OUTPUTS() {
        return NUM_OUTPUTS;
    }
    
    
    
    
    /**
     * Makes all bits 1 to the required length
     * 
     * Req for: TESTING
     * 
     */
    public void maximal() {       
        genome = new BitSet(requiredLength());
        genome.flip(0, requiredLength());
    } 
    
    
    /**
     * Req for: UC034
     * @return the bit at the given index as a boolean value
     */
    public boolean bitAt(int i) {        
        return genome.get(i);
    }   
    
    /**
     * Req for: UC034
     * @return the sub bitset from the given index i (inclusive) to the given index i2 (exclusive) 
     */
    public BitSet bitSetAt(int i, int i2) {        
        return genome.get(i, i2);
    }     
    
    /**
     * Sets the given section of the genome to match the given bitset.  Matching
     * will start from the beginning of the provided bitset.
     * 
     * Req for: UC034
     * @param i     initial index position (inclusive)
     * @param i2    final index position (exclusive)
     * @param b     bitset to match
     */
    public void setBitsAt(int i, int i2, BitSet b) { 
        for (int n = 0; n < i2-i; n++) {
            genome.set(n+i, b.get(n));
        }
    }     
 
    
    /**
     * Converts the genome BitSet to a binary string, with leading zeros.
     * 
     * Req for: TESTING
     * 
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
