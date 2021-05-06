/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controls;

/**
 *
 * @author Anguipes
 */
public class SimControl {
    
    SimConsts settings = new SimConsts();
    
    public void setMAX_LAYERS(int MAX_LAYERS) {
        SimConsts.setMAX_LAYERS(MAX_LAYERS);
    }

    public void setMAX_NODES_PER_LAYER(int MAX_NODES_PER_LAYER) {
        SimConsts.setMAX_NODES_PER_LAYER(MAX_NODES_PER_LAYER);
    }

    public void setNumInputs(int numInputs) {
        SimConsts.setNumInputs(numInputs);
    }

    public void setNumOutputs(int numOutputs) {
        SimConsts.setNumOutputs(numOutputs);
    }    
    
}
