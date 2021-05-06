/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import controls.SimConsts;
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
public class NNAdaptor {
    
    private final BasicNetwork nn = new BasicNetwork();
    
    /**
     * builds the neural network from a genetic representation held in a GRep object.
     * @param g genetic representation
     */    
    public void createFromGRep(GRep g) {        
        //add input layer
        //no activation function (direct value pass), no bias node, SimConsts.getNumInputs nodes
        nn.addLayer(new BasicLayer(null,false,SimConsts.getNumInputs()));
                
        //read the node grid part of the genome to add inner layers and build 
        //node coordinate map        
        Map<LNPair, LNPair> nodeMap = new HashMap<>();        
        int trueL = 1;  //internal layer numbering starts at 1 (0 is input)               
        //loop maximal layers
        for (int L = 1; L <= SimConsts.getMAX_LAYERS(); L++) {
            int trueN = 0;
            //loop nodes
            for (int n = 0; n < SimConsts.getMAX_NODES_PER_LAYER(); n++) {
                if (g.nodeAt(L, n)) {
                    //map node's actual to code position
                    nodeMap.put(new LNPair(trueL, trueN), new LNPair(L, n));
                    //increment number of nodes in layer
                    trueN++;
                }
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
        //sigmoid activation, no bias node, SimConsts.getNumOutputs() nodes
        nn.addLayer(new BasicLayer(new ActivationSigmoid(),false,SimConsts.getNumOutputs()));        
        
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
                        if (g.connectionAt(p1.getLayer(), p1.getNode(), p2.getNode())) {
                            //add the connection weight
                            nn.addWeight(L, n, n2, parseWeight(g.weightAt(p1.getLayer(), p1.getNode(), p2.getNode())));
                        } else {
                            //nodes exist, but connection does not.  Disable connection.
                            nn.enableConnection(L, n, n2, false);
                        }
                    //connections to an output are a special case.  These will not have a destination
                    //in the node map. All outputs exist in code order, so n2 can be used. 
                    } else if (L == nn.getLayerCount()-2) {
                        if (g.connectionAt(p1.getLayer(), p1.getNode(), n2)) {
                            nn.addWeight(L, n, n2, parseWeight(g.weightAt(p1.getLayer(), p1.getNode(), n2)));
                        } else {
                            nn.enableConnection(L, n, n2, false);
                        }                        
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
                    if (g.connectionAt(0, n, p.getNode())) {
                            //add the connection weight
                            nn.addWeight(0, n, n2, parseWeight(g.weightAt(0, n, p.getNode())));
                    } else {
                            //nodes exist, but connection does not.  Disable connection.
                            nn.enableConnection(0, n, n2, false);
                    }
                }
            } 
        }
    }
    
    /**
     * 
     * @return number of layers as an int
     */    
    public int getLayerCount() {
        return nn.getLayerCount();
    }
    
    /**
     * 
     * @return number of inputs as an int
     */
    public int getInputCount() {
        return nn.getInputCount();
    }  
    
    /**
     * 
     * @return number of outputs as an int
     */
    public int getOutputCount() {
        return nn.getOutputCount();
    }  
    
    /**
     * Returns the number of nodes in the layer represented by the given index.
     * If the index is invalid, returns 0.
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
     * Returns the number of nodes in the layer represented by the given index.
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

    public double[] output(double[] input) {
        double[] output = new double[SimConsts.getNumOutputs()]; 
        nn.compute(input, output);
        return output;
    }
       
    
    /**
     * Produces a weight value between -1 and 1 from the first 8 bits of the given bitset. 
     * 0 weight is encoded twice, once at 00000000 and once at 10000000
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
        //normalise 1-255 range to +/- 1
        total = (total-1)/127 - 1;
        return total;
    }
    
    /**
     * Produces a list of weights for active connections.  
     * This differs from encog's .getWeights(), which will return 0.0 weight for inactive connections.
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
     * 
     */
    public static List<Double> testWeightsList(GRep r) {
        List<Double> out = new ArrayList<>();
        int itr = GRep.gridLength() + GRep.mainConnectionsLength();
        for (int I = 0; I < SimConsts.getNumInputs(); I++) {
            for (int n = 0; n < SimConsts.getMAX_NODES_PER_LAYER(); n++) {
                if (r.connectionAt(0, I, n)) {
                    itr++;
                    out.add(parseWeight(r.weightAt(0, I, n)));
                    itr+=8;
                } else {
                    itr+=9;
                }
            }
        }
        itr = GRep.gridLength();
        for (int L = 1; L <= SimConsts.getMAX_LAYERS(); L++) {
            for (int n = 0; n < SimConsts.getMAX_NODES_PER_LAYER(); n++) {
                for (int n2 = 0; n2 < SimConsts.getMAX_NODES_PER_LAYER(); n2++) {
                    if (r.connectionAt(L, n, n2)) {
                        itr++;
                        out.add(parseWeight(r.weightAt(L, n, n2)));
                        itr+=8;
                    } else {
                        itr+=9;
                    }
                }
            }
        }        
        return out;
    }    
}
