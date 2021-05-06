package simulation;


import java.util.Random;
import controls.SimConsts;
import controls.SimControl;

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

    //CHANGE!!!!!
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GRepTest(true);
    }
    
    
    public static boolean GRepTest(boolean v) {
        int totalFails = 0;
        SimControl control = new SimControl();
        GRep r = new GRep();
        r.randomise();
        
        if (v) {
            System.out.println("Required length: " + GRep.requiredLength());
            //System.out.println("String BE: " + r.toBinaryString(true));
            System.out.println("String LE: " + r.toBinaryString(false));
        }
        
        
        int itr = 0;
        int fails = gridTest(r, itr);
        totalFails += fails;
        if (v)
            System.out.println("Grid test: " + fails + " failures.");        
        itr = GRep.gridLength();
        
        fails = mainConnectionsTest(r, itr, v);
        totalFails += fails;
        if (v)
            System.out.println("Main connections test: " + fails + " failures."); 
        
        itr += GRep.mainConnectionsLength();
        
        fails = inputConnectionsTest(r, itr, v);
        totalFails += fails;
        if (v)
            System.out.println("Input connections test: " + fails + " failures."); 
        
        
        r.maximal();
        itr = 0;       
        fails = gridTest(r, itr);
        totalFails += fails;
        if (v)
            System.out.println("Maximal grid test: " + fails + " failures.");        
        itr = GRep.gridLength();
        
        fails = mainConnectionsTest(r, itr, v);
        totalFails += fails;
        if (v)
            System.out.println("Maximal Main connections test: " + fails + " failures."); 
        
        itr += GRep.mainConnectionsLength();
        
        fails = inputConnectionsTest(r, itr, v);
        totalFails += fails;
        if (v)
            System.out.println("Maximal Input connections test: " + fails + " failures."); 
        
        
        r = new GRep();
        itr = 0;       
        fails = gridTest(r, itr);
        totalFails += fails;
        if (v)
            System.out.println("0 Grid test: " + fails + " failures.");        
        itr = GRep.gridLength();
        
        fails = mainConnectionsTest(r, itr, v);
        totalFails += fails;
        if (v)
            System.out.println("0 Main connections test: " + fails + " failures."); 
        
        itr += GRep.mainConnectionsLength();
        
        fails = inputConnectionsTest(r, itr, v);
        totalFails += fails;
        if (v)
            System.out.println("0 Input connections test: " + fails + " failures."); 
        
        
        fails = 0;
        Random random = new Random();
        for (int x = 0; x < 1000; x++) {
            control.setMAX_LAYERS(random.nextInt(100));
            control.setMAX_NODES_PER_LAYER(random.nextInt(30));
            control.setNumOutputs(random.nextInt(SimConsts.getMAX_NODES_PER_LAYER()+1));
            control.setNumInputs(random.nextInt(100));
            r.randomise();
            itr = 0;
            fails += gridTest(r, itr); 
            itr = GRep.gridLength();
            fails += mainConnectionsTest(r, itr, v); 
            itr += GRep.mainConnectionsLength();
            fails += inputConnectionsTest(r, itr, v);          
        }
        
        if (v)
            System.out.println("Failures in 1000 randomised tests: " + fails);
        totalFails += fails;
        
        if (totalFails == 0)
            return true;
        
        return false;
    }
    
    
    public static int mainConnectionsTest(GRep r, int itr, boolean v) {
        int fails = 0;
        for (int L = 1; L <= SimConsts.getMAX_LAYERS(); L++) {
            for (int n = 0; n < SimConsts.getMAX_NODES_PER_LAYER(); n++) {
                for (int n2 = 0; n2 < SimConsts.getMAX_NODES_PER_LAYER(); n2++) {
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
    
    public static int gridTest(GRep r, int itr) {
        int fails = 0;
        for (int L = 1; L <= SimConsts.getMAX_LAYERS(); L++) {
            for (int n = 0; n < SimConsts.getMAX_NODES_PER_LAYER(); n++) {
                if (r.nodeAt(L, n) != r.bitAt(itr)) fails++;
                itr++;
            }
        }
        return fails;
    }    
    
    public static int inputConnectionsTest(GRep r, int itr, boolean v) {
        int fails = 0;
        for (int I = 0; I < SimConsts.getNumInputs(); I++) {
            for (int n = 0; n < SimConsts.getMAX_NODES_PER_LAYER(); n++) {
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
    
    
}
