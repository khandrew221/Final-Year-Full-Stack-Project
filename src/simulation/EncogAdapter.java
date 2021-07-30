/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;

/**
 *
 * @author Kathryn Andrew
 */
public class EncogAdapter implements NNAdapter {
    
    private final BasicNetwork nn = new BasicNetwork();
    
    /**
     * builds the neural network from a genetic representation held in a GRep object.
     * 
     * Req for: UC009
     * 
     * @param g genetic representation
     */    
    @Override
    public void createFromGRep(GRep g) {    
        
        int MAX_LAYERS = g.getMAX_LAYERS();
        int MAX_NODES_PER_LAYER = g.getMAX_NODES_PER_LAYER();
        int NUM_INPUTS = g.getNUM_INPUTS();
        int NUM_OUTPUTS = g.getNUM_OUTPUTS();
        
        //add input layer
        //no activation function (direct value pass), no bias node, NUM_INPUTS nodes
        nn.addLayer(new BasicLayer(null,false,NUM_INPUTS));
                
        //read the node grid part of the genome to add inner layers and build 
        //node coordinate map        
        Map<LNPair, LNPair> nodeMap = new HashMap<>();        
        int trueL = 1;  //internal layer numbering starts at 1 (0 is input)               
        //loop maximal layers
        for (int L = 1; L <= MAX_LAYERS; L++) {
            int trueN = 0;
            //loop nodes in layer
            for (int n = 0; n < MAX_NODES_PER_LAYER; n++) {
                //if (g.nodeAt(L, n)) {
                    //map node's actual to code position
                    nodeMap.put(new LNPair(trueL, trueN), new LNPair(L, n));
                    //increment number of nodes in layer
                    trueN++;
                //}
            }
            //if the layer has nodes, increment true layer for next pass.
            //no nodes means the layer is empty, and will not exist in the true network
            if (trueN > 0) {
                //add layer with sigmoid activation, no bias node, trueN nodes
                nn.addLayer(new BasicLayer(new ActivationSigmoid(),false,trueN));
                //increment number of layers in implementation 
                trueL++;
            }
        }

        //add output layer
        //sigmoid activation, no bias node, NumOutputs() nodes
        nn.addLayer(new BasicLayer(new ActivationSigmoid(), false, NUM_OUTPUTS));        
        
        //required for further work on network
        nn.getStructure().finalizeStructure(); 
        
        //create inner connections
        for (int L = 1; L < nn.getLayerCount()-1; L++) {
            //loop nodes in layer
            for (int n = 0; n < nn.getLayerNeuronCount(L); n++) {                
                LNPair p1 = nodeMap.get(new LNPair(L, n)); //code position of start node
                //loop nodes in next layer
                for (int n2 = 0; n2 < nn.getLayerNeuronCount(L+1); n2++) {             
                    LNPair p2 = nodeMap.get(new LNPair(L+1, n2)); //code position of end node
                    //if the end node exists 
                    if (p2 != null) {                       
                        //and there is supposed to be a connection
                        //if (g.connectionAt(p1.getLayer(), p1.getNode(), p2.getNode())) {
                            //add the connection weight
                            nn.addWeight(L, n, n2, parseWeight(g.weightAt(p1.getLayer(), p1.getNode(), p2.getNode())));
                        //} else {
                            //nodes exist, but connection does not.  Disable connection.
                            //nn.enableConnection(L, n, n2, false);
                       // }
                    //connections to an output are a special case.  These will not have a destination
                    //in the node map. All outputs exist in code order, so n2 can be used. 
                    } else if (L == nn.getLayerCount()-2) {
                        //if (g.connectionAt(p1.getLayer(), p1.getNode(), n2)) {
                            nn.addWeight(L, n, n2, parseWeight(g.weightAt(p1.getLayer(), p1.getNode(), n2)));
                        //} else {
                        //    nn.enableConnection(L, n, n2, false);
                        //}                        
                    }
                }
            }
        }
        
        //create connections from input nodes
        for (int n = 0; n < nn.getLayerNeuronCount(0); n++) {                
            //loop nodes in next layer
            for (int n2 = 0; n2 < nn.getLayerNeuronCount(1); n2++) {             
                LNPair p = nodeMap.get(new LNPair(1, n2)); //code position of end node
                //if the end node exists 
                if (p != null) {                       
                    //and there is supposed to be a connection
                    //if (g.connectionAt(0, n, p.getNode())) {
                            //add the connection weight
                            nn.addWeight(0, n, n2, parseWeight(g.weightAt(0, n, p.getNode())));
                    //} else {
                            //nodes exist, but connection does not.  Disable connection.
                            //nn.enableConnection(0, n, n2, false);
                    //}
                }
            } 
        }
    }   
    
    /**
     * Produces a weight value between -1 and 1 from the first 8 bits of the given bitset. 
     * 0 weight is encoded twice, once at 00000000 and once at 10000000
     * 
     * Req for: UC009, this.createFromGRep()
     * 
     * @param w
     * @return a double between -1 and 1 inclusive
     */
    private static double parseWeight(BitSet w) {
        //return zero for empty bitset
        if (w.isEmpty()) return 0;            
        //otherwise, parse bitset as value between 1 and 255;
        double total = 0;
        for (int i = 7; i >= 0; i--) {
            if (w.get(i)) total += Math.pow(2, i);
        }            
        //normalise 1-255 range to +/- 6
        total = ((total-1)/127-1)*6;
        return total;
    }  
    
    
    /**
     * Returns an array (double[numOutputs]) of outputs calculated from the given array of inputs.
     * 
     * Req for: TESTING
     * 
     * @param input double[] of length numInputs
     * @return double[] of length numOutputs
     */
    @Override
    public double[] output(double[] input) {
        double[] output = new double[nn.getOutputCount()]; 
        nn.compute(input, output);
        return output;
    }     
    
    
    
    /**
     * Produces a list of weights for active connections.  
     * This differs from encog's .dumpWeights(), which will return 0.0 weight for inactive connections.  
     * 
     * Req for: TESTING
     * 
     * @return list of weights as doubles
     */
    public List<Double> weights() {
        List<Double> out = new ArrayList<>();
        for (int I = 0; I < getInputCount(); I++) {
            for (int n = 0; n < nn.getLayerNeuronCount(1); n++) {
                if (nn.isConnected(0, I, n)) {
                    out.add(nn.getWeight(0, I, n));
                }
            }
        }
        for (int L = 1; L < nn.getLayerCount()-1; L++) {
            for (int n = 0; n < nn.getLayerNeuronCount(L); n++) {
                for (int n2 = 0; n2 < nn.getLayerNeuronCount(L+1); n2++) {
                    if (nn.isConnected(L, n, n2)) {
                        out.add(nn.getWeight(L, n, n2));
                    }
                }
            }
        }   
        return out;
    }  
    
    /**
     * Produces a "test weight list" from a genetic representation.This list will contain 
     * all possible active (index bit 1) weight values from the genetic representation.
     * 
     * Req for: TESTING
     * 
     * @param r Genetic Representation
     * @param MAX_LAYERS
     * @param MAX_NODES_PER_LAYER
     * @param NUM_INPUTS
     * @param NUM_OUTPUTS
     * @return List of all active weights from the GRep as doubles 
     */
    public static List<Double> testWeightsList(GRep r, int MAX_LAYERS, int MAX_NODES_PER_LAYER, int NUM_INPUTS, int NUM_OUTPUTS) {
        List<Double> out = new ArrayList<>();
        int itr = r.gridLength() + r.mainConnectionsLength();
        for (int I = 0; I < NUM_INPUTS; I++) {
            for (int n = 0; n < MAX_NODES_PER_LAYER; n++) {
                //if (r.connectionAt(0, I, n)) {
                    itr++;
                    out.add(parseWeight(r.weightAt(0, I, n)));
                    itr+=8;
                //} else {
                //    itr+=9;
                //}
            }
        }
        itr = r.gridLength();
        for (int L = 1; L <= MAX_LAYERS; L++) {
            for (int n = 0; n < MAX_NODES_PER_LAYER; n++) {
                for (int n2 = 0; n2 < MAX_NODES_PER_LAYER; n2++) {
                    //if (r.connectionAt(L, n, n2)) {
                        itr++;
                        out.add(parseWeight(r.weightAt(L, n, n2)));
                        itr+=8;
                    //} else {
                    //    itr+=9;
                    //}
                }
            }
        }        
        return out;
    }      
    
    
    /**
     * Req for: TESTING
     * @return number of layers as an int
     */    
    public int getLayerCount() {
        return nn.getLayerCount();
    }
    
    /**
     * Req for: TESTING
     * @return number of inputs as an int
     */
    public int getInputCount() {
        return nn.getInputCount();
    }  
    
    /**
     * Req for: TESTING
     * @return number of outputs as an int
     */
    public int getOutputCount() {
        return nn.getOutputCount();
    }  
    
    /**
     * Returns the number of nodes in the layer represented by the given index.
     * If the index is invalid, returns 0.
     * 
     * Req for: TESTING
     * 
     * @param i layer index
     * @return 
     */
    public int getNodesInLayer(int i){
        if (i >= 0 && i < nn.getLayerCount()) {
            return nn.getLayerNeuronCount(i);
        }
        else {
            return 0;
        }
    }
     
    /**
     * Returns the total number of nodes in the network.
     * If the index is invalid, returns 0.
     * @return number of nodes in the neural network
     */
    
    public int getTotalNodeCount(){
        int total = 0;
        for (int i = 0; i < nn.getLayerCount(); i++) {
            total += nn.getLayerNeuronCount(i);
        }        
        return total;
    }

}
