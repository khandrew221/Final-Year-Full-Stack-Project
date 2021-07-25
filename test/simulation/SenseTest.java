/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import java.awt.Color;
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
        testEnviroMultipoint(true);
        testBorder(true);
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
        GRep g = new GRep(0, 10, 5, 1, 1);
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
        e.addField("Test1", 11, 0, 100, 0, Color.GREEN);
        e.randomiseField("Test1", 0, 100);
        Sense s = SenseFactory.MakeEnvironmentSense("Test1", e, true, 0, 0, 0);
        s.renumberOutputs(0);
        
        //no need to add the sense, we're testing it directly. 
        //Only required for a bot to pass to the sense.  
        Set<Sense> senses = new HashSet<>();
        Set<Behaviour> behaviours = new HashSet<>();
        GRep g = new GRep(0, 10, 5, 1, 1);
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

    public static int testEnviroMultipoint(boolean v) {        
        int fails = 0;   
        
        Environment e = new Environment(100,100);
        e.addField("Test1", 11, 0, 100, 0, Color.GREEN);
        e.randomiseField("Test1", 0, 100);
        Sense s = SenseFactory.MakeEnvironmentSense("Test1", e, true, 3, 4, 7);
        s.renumberOutputs(0);
        
        //no need to add the sense, we're testing it directly. 
        //Only required for a bot to pass to the sense.  
        Set<Sense> senses = new HashSet<>();
        Set<Behaviour> behaviours = new HashSet<>();
        GRep g = new GRep(0, 10, 5, s.outputSlots().size(), 1);
        Bot b = new Bot(g, senses, behaviours, 1, new Point (10,10));
        
        s.sensoryInput(b);
        
        
        if (b.getInputs().length != s.outputSlots().size()) {
            fails++;
            if (v)
                System.out.println("Failure: wrong number of bot input slots. " + s.outputSlots().size() + " expected, " + b.getInputs().length + " found.");            
        }
        
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
    

    public static int testBorder(boolean v) {        
        int fails = 0;   
        
        Environment e = new Environment(100,100);
        Sense s = SenseFactory.MakeBorderSense(e, 5);
        s.renumberOutputs(0);
        
        //no need to add the sense, we're testing it directly. 
        //Only required for a bot to pass to the sense.  
        Set<Sense> senses = new HashSet<>();
        Set<Behaviour> behaviours = new HashSet<>();
        GRep g = new GRep(0, 10, 5, s.outputSlots().size(), 1);
        Bot b = new Bot(g, senses, behaviours, 1, new Point (10,10));
        
        s.sensoryInput(b);
        
        if (b.getInputs().length != s.outputSlots().size()) {
            fails++;
            if (v)
                System.out.println("Failure: wrong number of bot input slots. " + s.outputSlots().size() + " expected, " + b.getInputs().length + " found.");            
        }
        
        for (int i = 0; i < 4; i++) {
            if (b.getInputs()[i] != 0) {
                fails++;
                if (v)
                    System.out.println("Failure: bot input not set correctly in slot " + i + ". 0 expected, " + b.getInputs()[i] + " found.");
            }
            if (b.getInputs()[i] > 1) {
            fails++;
            if (v)
                 System.out.println("Failure: bot input over in slot " + i + ". " + b.getInputs()[i] + " found.");            
            }
            if (b.getInputs()[i] < 0) {
               fails++;
                if (v)
                    System.out.println("Failure: bot input less than zero in slot " + i + ". " + b.getInputs()[i] + " found.");            
            }     
        }
        
        b.setPosition(new Point(0,0));
        s.sensoryInput(b);
   
        for (int i = 0; i < 4; i++) {
            if (i == 0 || i == 2) {
                if (b.getInputs()[i] != 0) {
                    fails++;
                    if (v)
                        System.out.println("Failure: bot input not set correctly in slot " + i + ". 0 expected, " + b.getInputs()[i] + " found.");
                } 
            } else {
                if (b.getInputs()[i] != 1) {
                    fails++;
                    if (v)
                        System.out.println("Failure: bot input not set correctly in slot " + i + ". 1 expected, " + b.getInputs()[i] + " found.");
                }                 
            }
            if (b.getInputs()[i] > 1) {
            fails++;
            if (v)
                 System.out.println("Failure: bot input over in slot " + i + ". " + b.getInputs()[i] + " found.");            
            }
            if (b.getInputs()[i] < 0) {
               fails++;
                if (v)
                    System.out.println("Failure: bot input less than zero in slot " + i + ". " + b.getInputs()[i] + " found.");            
            }     
        }     

        b.setPosition(new Point(100,100));
        s.sensoryInput(b);
        
        for (int i = 0; i < 4; i++) {
            if (i == 1 || i == 3) {
                if (b.getInputs()[i] != 0) {
                    fails++;
                    if (v)
                        System.out.println("Failure: bot input not set correctly in slot " + i + ". 0 expected, " + b.getInputs()[i] + " found.");
                } 
            } else {
                if (b.getInputs()[i] != 1) {
                    fails++;
                    if (v)
                        System.out.println("Failure: bot input not set correctly in slot " + i + ". 1 expected, " + b.getInputs()[i] + " found.");
                }                 
            }
            if (b.getInputs()[i] > 1) {
            fails++;
            if (v)
                 System.out.println("Failure: bot input over in slot " + i + ". " + b.getInputs()[i] + " found.");            
            }
            if (b.getInputs()[i] < 0) {
               fails++;
                if (v)
                    System.out.println("Failure: bot input less than zero in slot " + i + ". " + b.getInputs()[i] + " found.");            
            }     
        }         
        
                   
        return fails;
    }    
    
}
