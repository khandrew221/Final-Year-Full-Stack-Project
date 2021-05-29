/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import java.util.HashSet;
import java.util.Set;
import utility.Point;

/**
 *
 * @author Kathryn Andrew
 */
public class SenseTest {
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    
        testBasicExecution(true);
        testEnviro1point(true);
    }
    
    public static int testBasicExecution(boolean v) {        
        int fails = 0;        
        double testVal = 100;
        Sense s = new SenseDEBUG(testVal);
        s.renumberOutputs(0);
        
        //no need to add the sense, we're testing it directly. 
        //Only required for a bot to pass to the sense.  
        Set<Sense> senses = new HashSet<>();
        Set<Behaviour> behaviours = new HashSet<>();
        GRep g = new GRep(10, 5, 1, 1);
        Bot b = new Bot(g, senses, behaviours, 1, new Point (10,10));
        
        s.sensoryInput(b);
        
        if (b.getInputs()[0] != testVal) {
            fails++;
            if (v)
                System.out.println("Failure: bot input not set correctly. " + testVal + " expected, " + b.getInputs()[0] + " found.");
        }
        
        return fails;
    }
    
    public static int testEnviro1point(boolean v) {        
        int fails = 0;   
        
        Environment e = new Environment(100,100);
        e.addField("Test1", 11, 0, 100);
        e.randomiseField("Test1", 0, 100);
        Sense s = new SenseEnviro("Test1", e);
        s.renumberOutputs(0);
        
        //no need to add the sense, we're testing it directly. 
        //Only required for a bot to pass to the sense.  
        Set<Sense> senses = new HashSet<>();
        Set<Behaviour> behaviours = new HashSet<>();
        GRep g = new GRep(10, 5, 1, 1);
        Bot b = new Bot(g, senses, behaviours, 1, new Point (10,10));
        
        s.sensoryInput(b);
        
        if (b.getInputs()[0] != e.normValueAt("Test1", b.getPosition())) {
            fails++;
            if (v)
                System.out.println("Failure: bot input not set correctly. " + e.normValueAt("Test1", b.getPosition()) + " expected, " + b.getInputs()[0] + " found.");
        }
        if (b.getInputs()[0] > 1) {
           fails++;
            if (v)
                System.out.println("Failure: bot input over 1. " + b.getInputs()[0] + " found.");            
        }
        if (b.getInputs()[0] < 0) {
           fails++;
            if (v)
                System.out.println("Failure: bot input less than zero. " + b.getInputs()[0] + " found.");            
        }        
            
        
        return fails;
    }    

/*

        Environment e = new Environment(500, 500);
        e.addField("Test", 11, 5, 5);
        e.randomiseField("Test", 5, 5);
        Sense s = new SenseEnviro("Test", e);
        Set<Sense> senses = new HashSet<>();
        senses.add(s);
        Set<Behaviour> behaviours = new HashSet<>();
        Behaviour beh = new BehaviourMove(1, new Point(0,0), new Point(e.getXSize(), e.getYSize()));
        behaviours.add(beh);
        GRep g = new GRep(10, 5, 1, 2);
        g.randomise();
        
        Bot b = new Bot(g, senses, behaviours, 1);
        
        s.sensoryInput(b);
        
        for (double i : b.getInputs()) {
            System.out.println(i);
        }
        
        System.out.println(e.normValueAt("Test", b.getPosition()));
        
        b.runNN();
        
        for (double i : b.getOutputs()) {
            System.out.println(i);
        }        
        
        System.out.println("Old pos: " + b.getPosition().toString());
        
        beh.execute(b);
        
        System.out.println("New pos: " + b.getPosition().toString());
        
        
        b.run();
        for (double i : b.getInputs()) {
            System.out.println(i);
        }
        for (double i : b.getOutputs()) {
            System.out.println(i);
        }
        System.out.println("New pos: " + b.getPosition().toString());    */    
    
}
