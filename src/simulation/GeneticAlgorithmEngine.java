/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

/**
 * Creates genetic representations.
 * @author Kathryn Andrew
 */
public class GeneticAlgorithmEngine {
    
    int MAX_LAYERS;
    int MAX_NODES_PER_LAYER;
    
    /**
     * 
     * @param maxLayers
     * @param maxNodesPerLayer 
     */   
    public GeneticAlgorithmEngine(int maxLayers, int maxNodesPerLayer) {
        MAX_LAYERS = maxLayers;
        MAX_NODES_PER_LAYER = maxNodesPerLayer;
    }
    
    
    /**
     * Creates and returns a random genetic representation that defines a network
     * with the given number of inputs and outputs. Maximum layers and nodes
     * per layer are defined by GeneticAlgorithmEngine attributes MAX_LAYERS and
     * MAX_NODES_PER_LAYER.
     * @param numInputs
     * @param numOutputs
     * @return 
     */
    public GRep randomGRep(int numInputs, int numOutputs) {
        GRep g = new GRep(MAX_LAYERS, MAX_NODES_PER_LAYER, numInputs, numOutputs);
        g.randomise();
        return g;
    };
    
}
