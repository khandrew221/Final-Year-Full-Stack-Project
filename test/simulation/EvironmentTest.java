/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import controls.SimConsts;
import java.awt.Color;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import static simulation.ScalarFieldTest.ScalarFieldTest;
import utility.Point;

/**
 *
 * @author Kathryn Andrew
 */
public class EvironmentTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        System.out.println("Running prerequisite class tests..."); 
        if (ScalarFieldTest(false)) {
            System.out.println("Prerequisite class tests passed.");
            testAddField(true);
            testAddDeleteField(true);
            testGetSetFieldValues(true);
            testResize(true);
        } else {
            System.out.println("Scalar Field prerequisite test failed.  Aborting."); 
        }
    }
      

    public static int testAddField(boolean v) {
        
        int fails = 0;
        
        Environment e = new Environment(100,100);
        Random random = new Random();

        int min = -1000 + random.nextInt(2000);
        int max = -1000 + random.nextInt(2000);
        int d = random.nextInt(SimConsts.getENV_MAX_DENSITY()-SimConsts.getENV_MIN_DENSITY())+SimConsts.getENV_MIN_DENSITY();
        if (min > max) {
                int hold = min;
                min = max;
                max = hold;
        }
        e.addField("Test1", d, min, max, 0, Color.GREEN);
        
        if (!e.listFields().contains("Test1")) {
                fails++;
                if (v)
                    System.out.println("Failure on adding field Test1: Field with name \"Test1\" not found."); 
        }

        if (v)
            System.out.println("Failures on adding field: " + fails);        
        
        return fails;
    }       
    
    
    public static int testAddDeleteField(boolean v) {
        
        int fails = 0;
        
        Environment e = new Environment(100,100);
        
        if (!e.listFields().isEmpty()) {
            fails++;
            if (v) 
               System.out.println("Failure: Map not empty on creation."); 
        } else {
            
            Set<String> testSet = new HashSet<>();
            testSet.add("Test1");
            e.addField("Test1", 11, 0, 100, 0, Color.GREEN);
            
            if (!e.listFields().equals(testSet)) {
                fails++;
                if (v)
                    System.out.println("Failure on adding field Test1: " + testSet + " expected, actual result " + e.listFields()); 
            }

            Set <String> toRemove = new HashSet<>();
            toRemove.add("Test1");
            e.removeFields(toRemove);
            testSet.remove("Test1");
            
            if (!e.listFields().equals(testSet)) {
                fails++;
                if (v)
                    System.out.println("Failure on removing field Test1: " + testSet + " expected, actual result " + e.listFields()); 
            }
            
            testSet.add("Test2");
            testSet.add("Test3");
            testSet.add("Test4");
            e.addField("Test2", 11, 0, 100, 0, Color.GREEN);
            e.addField("Test3", 11, 0, 100, 0, Color.GREEN);
            e.addField("Test4", 11, 0, 100, 0, Color.GREEN);
            
            if (!e.listFields().equals(testSet)) {
                fails++;
                if (v)
                    System.out.println("Failure on adding multiple fields: " + testSet + " expected, actual result " + e.listFields()); 
            }        
            
            
            testSet.add("Test2");
            
            if (!e.listFields().equals(testSet)) {
                fails++;
                if (v)
                    System.out.println("Failure on adding existing fields: " + testSet + " expected, actual result " + e.listFields()); 
            }  
            
            toRemove.clear();
            toRemove.add("Test1");
            e.removeFields(toRemove);
            testSet.remove("Test1");
            
            if (!e.listFields().equals(testSet)) {
                fails++;
                if (v)
                    System.out.println("Failure on removing nonexistent field: " + testSet + " expected, actual result " + e.listFields()); 
            } 
            
                        
            toRemove.add("Test2");
            toRemove.add("Test4");
            e.removeFields(toRemove);
            testSet.remove("Test2");
            testSet.remove("Test4");
            
            if (!e.listFields().equals(testSet)) {
                fails++;
                if (v)
                    System.out.println("Failure on removing multiple mixed existing and nonexisting fields: " + testSet + " expected, actual result " + e.listFields()); 
            }    
            
        }
        
        if (v)
            System.out.println("Failures on adding and deleting fields: " + fails);
        
        return fails;
    } 
    
 
    
    public static int testGetSetFieldValues(boolean v) {
        int fails = 0;
        Random random = new Random();
        Environment e;
        
        for (int i = 0; i < 1000; i++) {
            int x = SimConsts.getENV_MIN_SIZE() + random.nextInt(SimConsts.getENV_MAX_SIZE()-SimConsts.getENV_MIN_SIZE());
            int y = SimConsts.getENV_MIN_SIZE() + random.nextInt(SimConsts.getENV_MAX_SIZE()-SimConsts.getENV_MIN_SIZE());
            int d = random.nextInt(SimConsts.getENV_MAX_DENSITY()-SimConsts.getENV_MIN_DENSITY())+SimConsts.getENV_MIN_DENSITY();
            e = new Environment(x,y);
            e.addField("Test1", d, 0, 100, 0, Color.GREEN);
            //System.out.println(x + ", " + y + ", " + d);
            e.setField("Test1", 0);
            fails += testGetFixedValues(e, "Test1", 0, true);
            int val = random.nextInt(100);
            e.setField("Test1", val);
            fails += testGetFixedValues(e, "Test1", val, true);
            int min = -1000 + random.nextInt(2000);
            int max = -1000 + random.nextInt(2000);   
            if (min > max) {
                int hold = min;
                min = max;
                max = hold;
            }
            e.randomiseField("Test1", min, max);
            fails += testGetRandomisedValues(e, "Test1", min, max, true);
        }
        
        if (v)
            System.out.println("Failures on setting and getting field values: " + fails);
        
        return fails;
    }   
    
    public static int testResize(boolean v) {
        int fails = 0;
        Random random = new Random();
        Environment e;
        
        for (int i = 0; i < 1000; i++) {
            e = new Environment(100,100);
            e.addField("Test1", 11, 0, 100, 0, Color.GREEN);
            int min = -1000 + random.nextInt(2000);
            int max = -1000 + random.nextInt(2000);
            if (min > max) {
                int hold = min;
                min = max;
                max = hold;
            }
            e.randomiseField("Test1", min, max);
            int x = SimConsts.getENV_MIN_SIZE() + random.nextInt(SimConsts.getENV_MAX_SIZE()-SimConsts.getENV_MIN_SIZE());
            int y = SimConsts.getENV_MIN_SIZE() + random.nextInt(SimConsts.getENV_MAX_SIZE()-SimConsts.getENV_MIN_SIZE());
            //int d = random.nextInt(SimConsts.getENV_MAX_DENSITY()-SimConsts.getENV_MIN_DENSITY())+SimConsts.getENV_MIN_DENSITY();
            e.resize(x, y);
            //density currently not changed by resize
            /*if (e.getDensity("Test1") != d) {
                fails++;
                if (v)
                    System.out.println("Wrong evironment density: " + d + " expected, actual result " + e.getDensity("Test1"));              
            }*/
            if (e.getXSize() != x) {
                fails++;
                if (v)
                    System.out.println("Wrong environment x size: " + x + " expected, actual result " + e.getXSize());              
            }            
            if (e.getYSize() != y) {
                fails++;
                if (v)
                    System.out.println("Wrong environment y size: " + y + " expected, actual result " + e.getYSize());              
            } 
            //System.out.println(x + ", " + y + ", " + d);
            fails += testGetRandomisedValues(e, "Test1", min, max, false);
        }
        
        if (v)
            System.out.println("Failures on resizing: " + fails);
        
        return fails;
    }    
    
    
    public static int testGetFixedValues(Environment e, String name, double expected, boolean v) {
        
        int fails = 0;
        Random random = new Random();

        Point p = new Point(0, 0);
        
        if (!approxEquals(e.trueValueAt(name, p), expected, 0.0000001)) {
            fails++;
            if (v)
                    System.out.println("Failure on accessing field " + name + " at 0,0: " + expected + " expected, actual result " + e.trueValueAt(name, p));
        }
        
        p = new Point(e.getXSize(), e.getYSize());
        if (!approxEquals(e.trueValueAt(name, p), expected, 0.0000001)) {
            fails++;
            if (v)
                    System.out.println("Failure on accessing field " + name + " at max,max: " + expected + " expected, actual result " + e.trueValueAt(name, p));
        }        
        
        for (int i = 0; i < 100; i++) {
            p = new Point(random.nextInt(e.getXSize()+1), random.nextInt(e.getYSize()+1));
            if (!approxEquals(e.trueValueAt(name, p), expected, 0.0000001)) {
                fails++;
                if (v)
                        System.out.println("Failure on accessing field " + name + " at " + p.getX() + "," + p.getY() + " : " + expected + " expected, actual result " + e.trueValueAt(name, p));                
            }
        };
        
        //if (v)
        //    System.out.println("Failures accessing set values: " + fails);

        return fails;
    }  
    
    public static int testGetRandomisedValues(Environment e, String name, double min, double max, boolean v) {
        
        int fails = 0;
        Random random = new Random();
        
        for (int i = 0; i < 100; i++) {
            Point p = new Point(random.nextInt(e.getXSize()+1), random.nextInt(e.getYSize()+1));
            double val = e.trueValueAt(name, p);
            if (val > max || val < min) {
                fails++;
                if (v) 
                        System.out.println("Failure on accessing field " + name + " at " + p.getX() + "," + p.getY() + " : " + max + " max " + min + " min expected, actual result " + val);                
            }
        };
        
        //if (v)
        //    System.out.println("Failures accessing randomised values: " + fails);

        return fails;
    }    
    
    public static boolean approxEquals(double v1, double v2, double tol) {
        if (Math.abs(v1 - v2) > tol) {
            return false;
        }
        return true;
    }    
}
