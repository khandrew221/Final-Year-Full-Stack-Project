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
public class SimConsts {
    
    private static int MAX_LAYERS = 2;
    private static int MAX_NODES_PER_LAYER = 3;
    private static int numInputs = 1;
    private static int numOutputs = 1;

    public static int getMAX_LAYERS() {
        return MAX_LAYERS;
    }

    public static int getMAX_NODES_PER_LAYER() {
        return MAX_NODES_PER_LAYER;
    }

    public static int getNumInputs() {
        return numInputs;
    }

    public static int getNumOutputs() {
        return numOutputs;
    }

    static void setMAX_LAYERS(int MAX_LAYERS) {
        SimConsts.MAX_LAYERS = MAX_LAYERS;
    }

    static void setMAX_NODES_PER_LAYER(int MAX_NODES_PER_LAYER) {
        SimConsts.MAX_NODES_PER_LAYER = MAX_NODES_PER_LAYER;
    }

    static void setNumInputs(int numInputs) {
        SimConsts.numInputs = numInputs;
    }

    static void setNumOutputs(int numOutputs) {
        SimConsts.numOutputs = numOutputs;
    }
    
}
