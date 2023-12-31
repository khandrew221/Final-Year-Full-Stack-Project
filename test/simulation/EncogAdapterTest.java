package simulation;


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
        
        
                  
        //varianceTest(true);
        

    }
    
    public static boolean NNAdaptorTest(boolean v) {
        
        boolean reqPass = GRepTest.GRepTest(false);
        
        if (!reqPass) {
            System.out.println("Genetic Representation test failed.  Aborting.");
            return false;
        }
        
        if (v)
            System.out.println("Prerequisite class tests passed.");    
        
        int MAX_LAYERS = 2;
        int MAX_NODES_PER_LAYER = 3;
        int NUM_INPUTS = 1;
        int NUM_OUTPUTS = 1;
        
        int totalFails = 0;
        EncogAdapter nn = new EncogAdapter();
        GRep r = new GRep(0, MAX_LAYERS, MAX_NODES_PER_LAYER, NUM_INPUTS, NUM_OUTPUTS);
        //r.randomise();
        r.maximal();
        nn.createFromGRep(r);
        
        int fails = basicStrucTest(nn, MAX_LAYERS, MAX_NODES_PER_LAYER, NUM_INPUTS, NUM_OUTPUTS, v);
        totalFails += fails;
        if (v)
            System.out.println("Basic structure test failures: " + fails);       

        fails = connectionsTest(nn, r, MAX_LAYERS, MAX_NODES_PER_LAYER, NUM_INPUTS, NUM_OUTPUTS, v);
        totalFails += fails;
        if (v)
            System.out.println("Connection test failures: " + fails);   
        
        fails = connectionsTest(nn, r, MAX_LAYERS, MAX_NODES_PER_LAYER, NUM_INPUTS, NUM_OUTPUTS, v);
        totalFails += fails;
        if (v)
            System.out.println("Connection test failures: " + fails);        
        
        
        fails = 0;
        Random random = new Random();
        for (int x = 0; x < 1000; x++) {
            MAX_LAYERS = random.nextInt(100)+1;
            MAX_NODES_PER_LAYER = random.nextInt(30)+1;
            NUM_INPUTS = random.nextInt(100)+1;
            NUM_OUTPUTS = random.nextInt(MAX_NODES_PER_LAYER+1);
            r = new GRep(0, MAX_LAYERS, MAX_NODES_PER_LAYER, NUM_INPUTS, NUM_OUTPUTS);
            r.randomise();
            nn.createFromGRep(r);
            fails += basicStrucTest(nn, MAX_LAYERS, MAX_NODES_PER_LAYER, NUM_INPUTS, NUM_OUTPUTS, v); 
            fails += connectionsTest(nn, r, MAX_LAYERS, MAX_NODES_PER_LAYER, NUM_INPUTS, NUM_OUTPUTS, v);
            fails += outputTest(nn, NUM_INPUTS, NUM_OUTPUTS, v);
        }
        if (v)
            System.out.println("Failures in 1000 randomised tests: " + fails);
        
        totalFails += fails;
        
        if (totalFails == 0)
            return true;
        

        
        return false;
    }
    
    public static int basicStrucTest(EncogAdapter nn, int MAX_LAYERS, int MAX_NODES_PER_LAYER, int NUM_INPUTS, int NUM_OUTPUTS, boolean v) {
        int fails = 0;
        if (nn.getLayerCount() > MAX_LAYERS + 2) {
            fails++;
            if (v)
                System.out.println("Layer count mismatch: max " + (MAX_LAYERS+2) + " expected, " + nn.getLayerCount() + " found.");      
        }
        if (!(nn.getInputCount() == NUM_INPUTS)) {
            fails++;
            if (v)
                System.out.println("Inputs mismatch: " + NUM_INPUTS + " expected, " + nn.getInputCount() + " found.");
        }   
        if (!(nn.getOutputCount() == NUM_OUTPUTS)) {
            fails++;
            if (v)
                System.out.println("Outputs mismatch: " + NUM_OUTPUTS + " expected, " + nn.getOutputCount() + " found.");
        }          
        for (int i = 1; i < nn.getLayerCount(); i++) {
            if (nn.getNodesInLayer(i) > MAX_NODES_PER_LAYER) {
                fails++;
                if (v)
                    System.out.println("Number of nodes mismatch in layer " + i + ": " + MAX_NODES_PER_LAYER + " expected, " + nn.getNodesInLayer(i) + " found.");               
            }     
        }
        return fails;
    }
    
    
    public static int connectionsTest(EncogAdapter nn, GRep r, int MAX_LAYERS, int MAX_NODES_PER_LAYER, int NUM_INPUTS, int NUM_OUTPUTS, boolean v) {
        int fails = 0;
        if (!EncogAdapter.testWeightsList(r, MAX_LAYERS, MAX_NODES_PER_LAYER, NUM_INPUTS, NUM_OUTPUTS).containsAll(nn.weights())) {
            fails++;
            if (v)
                System.out.println("Connections test failed: neural network connection list is not contained in genetic representation full active weight list.");        
        }        
        return fails;
    }    
    
    public static int outputTest(EncogAdapter nn, int NUM_INPUTS, int NUM_OUTPUTS, boolean v) {
        int fails = 0;
        double[] in = new double[NUM_INPUTS];
        for (int i = 0; i < in.length; i++) {
            in[i] = Math.random();
        } 
        double[] out = nn.output(in);
        if (out.length != NUM_OUTPUTS) {
            fails++;
            if (v)
                System.out.println("Number of outputs mismatch: " + NUM_OUTPUTS + " expected, " + out.length + " found.");             
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
    
    public static int varianceTest( boolean v) {
        
        EncogAdapter nn = new EncogAdapter();
        int MAX_LAYERS = 1;
        int MAX_NODES_PER_LAYER = 2;
        int NUM_INPUTS = 2;
        int NUM_OUTPUTS = 1;
        GRep r = new GRep(0, MAX_LAYERS, MAX_NODES_PER_LAYER, NUM_INPUTS, NUM_OUTPUTS);
        r.randomise();
        //r.maximal();
        nn.createFromGRep(r);

        System.out.println("Layers: " + nn.getLayerCount()); 
        System.out.println("Nodes: " + nn.getTotalNodeCount()); 
                System.out.println(nn.weights());             
         
        
        int fails = 0;
        double[] in = new double[NUM_INPUTS];
        double[] totalOut = new double[NUM_OUTPUTS];
        double[] minOut = new double[NUM_OUTPUTS];
        double[] maxOut = new double[NUM_OUTPUTS];
        
        double[] out = nn.output(in);
        
        for (int i = 0; i < minOut.length; i++) {
                minOut [i] = out[i];
                maxOut [i] = out[i];                
        }  
        
        for (int n = 0; n < 1000; n++) {
        
            for (int i = 0; i < in.length; i++) {
                in[i] = n/1000.0;
            } 
            out = nn.output(in);

            for (int i = 0; i < out.length; i++) {
                totalOut[i] += out[i];
            } 

            for (int i = 0; i < out.length; i++) {
                if (out[i] > maxOut[i])
                    maxOut[i] = out[i];
                if (out[i] < minOut[i])
                    minOut[i] = out[i];                
            }           
            
            /*for (int i = 0; i < in.length; i++) {
                System.out.println("In: " + in[i]);             
            }        
            /*for (int i = 0; i < out.length; i++) {
                System.out.println("Out: " + out[i]);             
            }  */      
            
        }
        
        for (int i = 0; i < totalOut.length; i++) {
                System.out.println("mean Out: " + totalOut[i] / 1000);   
                System.out.println("max Out: " + maxOut[i]); 
                System.out.println("min Out: " + minOut[i]); 
        }  
        
        return fails;
    }    
}
