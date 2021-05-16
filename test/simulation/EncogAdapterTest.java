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
public class EncogAdapterTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        NNAdaptorTest(true);        
    }
    
    public static boolean NNAdaptorTest(boolean v) {
        
        boolean reqPass = GRepTest.GRepTest(false);
        
        if (!reqPass) {
            System.out.println("Genetic Representation test failed.  Aborting.");
            return false;
        }
        
        if (v)
            System.out.println("Prerequisite class tests passed.");    
        
        int totalFails = 0;
        SimControl control = new SimControl();
        EncogAdapter nn = new EncogAdapter();
        GRep r = new GRep();
        //r.randomise();
        r.maximal();
        nn.createFromGRep(r);
        
        int fails = basicStrucTest(nn, v);
        totalFails += fails;
        if (v)
            System.out.println("Basic structure test failures: " + fails);       

        fails = connectionsTest(nn, r, v);
        totalFails += fails;
        if (v)
            System.out.println("Connection test failures: " + fails);   
        
        fails = connectionsTest(nn, r, v);
        totalFails += fails;
        if (v)
            System.out.println("Connection test failures: " + fails);        
        
        
        fails = 0;
        Random random = new Random();
        for (int x = 0; x < 1000; x++) {
            control.setMAX_LAYERS(random.nextInt(100)+1);
            control.setMAX_NODES_PER_LAYER(random.nextInt(30)+1);
            control.setNumOutputs(random.nextInt(SimConsts.getMAX_NODES_PER_LAYER()+1));
            control.setNumInputs(random.nextInt(100)+1);
            r.randomise();
            nn.createFromGRep(r);
            fails += basicStrucTest(nn, v); 
            fails += connectionsTest(nn, r, v);
            fails += outputTest(nn, v);
        }
        if (v)
            System.out.println("Failures in 1000 randomised tests: " + fails);
        
        totalFails += fails;
        
        if (totalFails == 0)
            return true;
        
        return false;
    }
    
    public static int basicStrucTest(EncogAdapter nn, boolean v) {
        int fails = 0;
        if (nn.getLayerCount() > SimConsts.getMAX_LAYERS() + 2) {
            fails++;
            if (v)
                System.out.println("Layer count mismatch: max " + (SimConsts.getMAX_LAYERS()+2) + " expected, " + nn.getLayerCount() + " found.");      
        }
        if (!(nn.getInputCount() == SimConsts.getNumInputs())) {
            fails++;
            if (v)
                System.out.println("Inputs mismatch: " + SimConsts.getNumInputs() + " expected, " + nn.getInputCount() + " found.");
        }   
        if (!(nn.getOutputCount() == SimConsts.getNumOutputs())) {
            fails++;
            if (v)
                System.out.println("Outputs mismatch: " + SimConsts.getNumOutputs() + " expected, " + nn.getOutputCount() + " found.");
        }          
        for (int i = 1; i < nn.getLayerCount(); i++) {
            if (nn.getNodesInLayer(i) > SimConsts.getMAX_NODES_PER_LAYER()) {
                fails++;
                if (v)
                    System.out.println("Number of nodes mismatch in layer " + i + ": " + SimConsts.getMAX_NODES_PER_LAYER() + " expected, " + nn.getNodesInLayer(i) + " found.");               
            }     
        }
        return fails;
    }
    
    
    public static int connectionsTest(EncogAdapter nn, GRep r, boolean v) {
        int fails = 0;
        if (!EncogAdapter.testWeightsList(r).containsAll(nn.weights())) {
            fails++;
            if (v)
                System.out.println("Connections test failed: neural network connection list is not contained in genetic representation full active weight list.");        
        }        
        return fails;
    }    
    
    public static int outputTest(EncogAdapter nn, boolean v) {
        int fails = 0;
        double[] in = new double[SimConsts.getNumInputs()];
        double[] out = nn.output(in);
        if (out.length != SimConsts.getNumOutputs()) {
            fails++;
            if (v)
                System.out.println("Number of outputs mismatch: " + SimConsts.getNumOutputs() + " expected, " + out.length + " found.");             
        }
        for (double x : out) {
            if (x > 1 || x < 0) {
                fails++;
                if (v)
                    System.out.println("Outputs range mismatch: value between 0 and 1 expected, " + x + " found.");             
            }
        }
        
        return fails;
    }
}
