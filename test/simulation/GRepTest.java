package simulation;


import java.util.BitSet;
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
public class GRepTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GRepTest(true);
    }
    
    
    public static boolean GRepTest(boolean v) {
        int totalFails = 0;

        int MAX_LAYERS = 2;
        int MAX_NODES_PER_LAYER = 3;
        int NUM_INPUTS = 1;
        int NUM_OUTPUTS = 1;
        GRep r = new GRep(0, MAX_LAYERS, MAX_NODES_PER_LAYER, NUM_INPUTS, NUM_OUTPUTS);
        r.randomise();
        
        if (v) {
            System.out.println("Required length: " + r.requiredLength());
            //System.out.println("String BE: " + r.toBinaryString(true));
            System.out.println("String LE: " + r.toBinaryString(false));
        }
        
        
        int itr = 0;
        int fails = gridTest(r, MAX_LAYERS, MAX_NODES_PER_LAYER, itr);
        totalFails += fails;
        if (v)
            System.out.println("Grid test: " + fails + " failures.");        
        itr = r.gridLength();
        
        fails = mainConnectionsTest(r, MAX_LAYERS, MAX_NODES_PER_LAYER, itr, v);
        totalFails += fails;
        if (v)
            System.out.println("Main connections test: " + fails + " failures."); 
        
        itr += r.mainConnectionsLength();
        
        fails = inputConnectionsTest(r, MAX_NODES_PER_LAYER, NUM_INPUTS, itr, v);
        totalFails += fails;
        if (v)
            System.out.println("Input connections test: " + fails + " failures."); 
        
        
        r.maximal();
        itr = 0;       
        fails = gridTest(r, MAX_LAYERS, MAX_NODES_PER_LAYER, itr);
        totalFails += fails;
        if (v)
            System.out.println("Maximal grid test: " + fails + " failures.");        
        itr = r.gridLength();
        
        fails = mainConnectionsTest(r, MAX_LAYERS, MAX_NODES_PER_LAYER, itr, v);
        totalFails += fails;
        if (v)
            System.out.println("Maximal Main connections test: " + fails + " failures."); 
        
        itr += r.mainConnectionsLength();
        
        fails = inputConnectionsTest(r, MAX_NODES_PER_LAYER, NUM_INPUTS, itr, v);
        totalFails += fails;
        if (v)
            System.out.println("Maximal Input connections test: " + fails + " failures."); 
        
        
        r = new GRep(0, MAX_LAYERS, MAX_NODES_PER_LAYER, NUM_INPUTS, NUM_OUTPUTS);
        itr = 0;       
        fails = gridTest(r, MAX_LAYERS, MAX_NODES_PER_LAYER, itr);
        totalFails += fails;
        if (v)
            System.out.println("0 Grid test: " + fails + " failures.");        
        itr = r.gridLength();
        
        fails = mainConnectionsTest(r, MAX_LAYERS, MAX_NODES_PER_LAYER, itr, v);
        totalFails += fails;
        if (v)
            System.out.println("0 Main connections test: " + fails + " failures."); 
        
        itr += r.mainConnectionsLength();
        
        fails = inputConnectionsTest(r, MAX_LAYERS, MAX_NODES_PER_LAYER, itr, v);
        totalFails += fails;
        if (v)
            System.out.println("0 Input connections test: " + fails + " failures."); 
        
        
        fails = 0;
        Random random = new Random();
        for (int x = 0; x < 1000; x++) {
            MAX_LAYERS = random.nextInt(100)+1;
            MAX_NODES_PER_LAYER = random.nextInt(30)+1;
            NUM_INPUTS = random.nextInt(100)+1;
            NUM_OUTPUTS = MAX_NODES_PER_LAYER-random.nextInt(MAX_NODES_PER_LAYER);
            r = new GRep(0, MAX_LAYERS, MAX_NODES_PER_LAYER, NUM_INPUTS, NUM_OUTPUTS);
            r.randomise();
            itr = 0;
            fails += gridTest(r, MAX_LAYERS, MAX_NODES_PER_LAYER, itr); 
            itr = r.gridLength();
            fails += mainConnectionsTest(r, MAX_LAYERS, MAX_NODES_PER_LAYER, itr, v); 
            itr += r.mainConnectionsLength();
            fails += inputConnectionsTest(r, MAX_NODES_PER_LAYER, NUM_INPUTS, itr, v);          
        }
        
        if (v)
            System.out.println("Failures in 1000 randomised tests: " + fails);
        totalFails += fails;
        
        
        totalFails += setBitsTest(r);
        
        totalFails += cloneTest(r, v);
        
        if (totalFails == 0)
            return true;
        
        return false;
    }
    
    
    public static int mainConnectionsTest(GRep r, int MAX_LAYERS, int MAX_NODES_PER_LAYER, int itr, boolean v) {
        int fails = 0;
        for (int L = 1; L <= MAX_LAYERS; L++) {
            for (int n = 0; n < MAX_NODES_PER_LAYER; n++) {
                for (int n2 = 0; n2 < MAX_NODES_PER_LAYER; n2++) {
                    if (r.connectionAt(L, n, n2) != r.bitAt(itr)) {
                        fails++;
                        if (v)
                            System.out.println("Index failure at layer " + L + ", node " + n + " to " + n2);   
                    }
                    itr++;
                    if (!(r.weightAt(L, n, n2).equals(r.bitSetAt(itr, itr+8)))) {
                        if (v)
                            System.out.println("Weight failure at layer " + L + ", node " + n + " to " + n2);  
                        fails++;
                    }
                    itr+=8; 
                }
            }
        }
        return fails;
    }
    
    public static int gridTest(GRep r, int MAX_LAYERS, int MAX_NODES_PER_LAYER, int itr) {
        int fails = 0;
        for (int L = 1; L <= MAX_LAYERS; L++) {
            for (int n = 0; n < MAX_NODES_PER_LAYER; n++) {
                if (r.nodeAt(L, n) != r.bitAt(itr)) fails++;
                itr++;
            }
        }
        return fails;
    }    
    
    public static int inputConnectionsTest(GRep r, int MAX_NODES_PER_LAYER, int NUM_INPUTS, int itr, boolean v) {
        int fails = 0;
        for (int I = 0; I < NUM_INPUTS; I++) {
            for (int n = 0; n < MAX_NODES_PER_LAYER; n++) {
                if (r.connectionAt(0, I, n) != r.bitAt(itr)) {
                    fails++;
                    if (v) {
                        System.out.println("Index failure at input layer, node " + I + " to " + n);   
                        System.out.println("Iterator: " + r.bitAt(itr));
                        System.out.println("Method: " + r.connectionAt(0, I, n));
                    }
                }
                itr++;
                if (!(r.weightAt(0, I, n).equals(r.bitSetAt(itr, itr+8)))) {
                    if (v) {
                        System.out.println("Weight failure at input layer, node " + I + " to " + n);   
                        System.out.println("Iterator: " + r.bitSetAt(itr, itr+8));
                        System.out.println("Method: " + r.weightAt(0, I, n));
                    }
                    fails++;
                }
                itr+=8; 
            }
        }
        return fails;
    }    
    
    public static int setBitsTest(GRep r) {
        int fails = 0;
        r.maximal();
        BitSet b = new BitSet();
        Random random  = new Random();
        int start = random.nextInt(20);
        int end = start + 1 + random.nextInt(20);
        r.setBitsAt(start, end, b);
        for (int i = start; i < end; i++) {
            if (r.bitAt(i)) {
                System.out.println("Set bits failure at index " + i + ", false expected, true found."); 
                fails++;
            }        
        }
        return fails;
    } 

    public static int cloneTest(GRep r, boolean v) {
        int fails = 0;
        r.randomise();
        GRep b = r.clone();
        if (!r.equals(b)) {
            fails++;
            if (v)
                System.out.println("Clone failed, clone not equal to original."); 
        }
        return fails;
    }         
    
}
