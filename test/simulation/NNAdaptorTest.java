package simulation;


import java.util.Arrays;
import controls.SimConsts;
import controls.SimControl;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Anguipes
 */
public class NNAdaptorTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        boolean reqPass = GRepTest.GRepTest(false);
        
        if (!reqPass)
            System.out.println("Genetic Representation test failed.  Aborting.");
        
        System.out.println("Prerequisite class tests passed.");


                
        
        SimControl control = new SimControl();
        NNAdaptor nn = new NNAdaptor();
        GRep r = new GRep();
        r.randomise();
        //r.maximal();
        System.out.println("String LE: " + r.toBinaryString(false));
        
        nn.createFromGRep(r);
        
        System.out.println(nn.getLayerCount());
        System.out.println(nn.getInputCount());
        System.out.println(nn.getOutputCount());
        
        System.out.println(nn.getNodesInLayer(0));
        System.out.println(nn.getNodesInLayer(1));
        System.out.println(nn.getNodesInLayer(2));
        System.out.println(nn.getNodesInLayer(3));
        
        System.out.println(nn.getTotalNodeCount());
  
        double[] in = new double[SimConsts.getNumInputs()];
        System.out.println(Arrays.toString(nn.output(in)));
        System.out.println("Basic structure test: Total failures: " + basicStrucTest(nn));
        
        connectionsTest(nn, r);
        
        int fails = 0;
        Random random = new Random();
        for (int x = 0; x < 1000; x++) {
            control.setMAX_LAYERS(random.nextInt(100)+1);
            control.setMAX_NODES_PER_LAYER(random.nextInt(30)+1);
            control.setNumOutputs(random.nextInt(SimConsts.getMAX_NODES_PER_LAYER()+1));
            control.setNumInputs(random.nextInt(100)+1);
            r.randomise();
            nn.createFromGRep(r);
            fails += basicStrucTest(nn); 
            fails += connectionsTest(nn, r);
        }
        System.out.println("Failures in 1000 randomised tests: " + fails);

    }
    
    public static int basicStrucTest(NNAdaptor nn) {
        int fails = 0;
        if (nn.getLayerCount() > SimConsts.getMAX_LAYERS() + 2) {
            System.out.println("Layer count mismatch: max " + (SimConsts.getMAX_LAYERS()+2) + " expected, " + nn.getLayerCount() + " found.");
            fails++;
        }
        if (!(nn.getInputCount() == SimConsts.getNumInputs())) {
            System.out.println("Inputs mismatch: " + SimConsts.getNumInputs() + " expected, " + nn.getInputCount() + " found.");
            fails++;
        }   
        if (!(nn.getOutputCount() == SimConsts.getNumOutputs())) {
            System.out.println("Outputs mismatch: " + SimConsts.getNumOutputs() + " expected, " + nn.getOutputCount() + " found.");
            fails++;
        }          
        for (int i = 1; i < nn.getLayerCount(); i++) {
            if (nn.getNodesInLayer(i) > SimConsts.getMAX_NODES_PER_LAYER()) {
                System.out.println("Number of nodes mismatch in layer " + i + ": " + SimConsts.getMAX_NODES_PER_LAYER() + " expected, " + nn.getNodesInLayer(i) + " found.");
                fails++;                
            }     
        }
        return fails;
    }
    
    
    public static int connectionsTest(NNAdaptor nn, GRep r) {
        int fails = 0;
        if (!NNAdaptor.testWeightsList(r).containsAll(nn.weights())) {
            fails++;
            System.out.println("Connections test failed: neural network connection list is not contained in genetic representation full active weight list.");        
        }        
        return fails;
    }    
    
}
