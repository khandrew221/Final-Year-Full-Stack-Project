/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import controls.SimConsts;
import java.util.Random;
import utility.Point;

/**
 *
 * @author Anguipes
 */
public class ScalarFieldTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ScalarFieldTest(true);
    }
    
    public static boolean ScalarFieldTest(boolean v) {
        
        int fails = 0;
        
        fails += testMinimal(v); 
        fails += testRandom(v);
        
        if (fails == 0)
            return true;
        
        return false;
    }
    
    public static int testMinimal(boolean v) {
        
        int fails = 0;
        
        ScalarField s = new ScalarField(100, 100, 2, 0, 100);
        
        if (v)
            System.out.println("New field, 0 values expected at all points:");        
        fails += cornerTest(s, 100, 100, 0, 0, 0, 0, v);   
        fails += edgeTest(s, 100, 100, 0, 0, 0, 0, v); 
        fails += centreTest(s, 100, 100, 0, v);

        if (v) {        
            System.out.println();                
            System.out.println("All sample points set to 3:");
        }
        s.setTo(3);        
        fails += cornerTest(s, 100, 100, 3, 3, 3, 3, v);
        fails += edgeTest(s, 100, 100, 3, 3, 3, 3, v);
        fails += centreTest(s, 100, 100, 3, v);
        
        if (v) {        
            System.out.println();
            System.out.println("Sample square set randomly:");
        }

        s.randomise(-100, 100);
        Random random = new Random();
        double T00 = random.nextInt(100);
        double T01 = random.nextInt(100);
        double T10 = random.nextInt(100);
        double T11 = random.nextInt(100);
        s.setTest(T00, T01, T10, T11);
        fails += cornerTest(s, 100, 100, T00, T01, T10, T11, v);   
        fails += edgeTest(s, 100, 100, (T00+T10)*0.5, (T00+T01)*0.5, (T10+T11)*0.5, (T01+T11)*0.5, v);     
        fails += centreTest(s, 100, 100, (T00+T10+T01+T11)*0.25, v);
        
        if (v)
            System.out.println("Resampled:");
        int x = 100 + random.nextInt(5000);
        int y = 100 + random.nextInt(5000);
        int d = random.nextInt(20)+2;
        s.resample(x, y, d);
        s.setTo(3);
        fails += cornerTest(s, 100, 100, 3, 3, 3, 3, v);
        fails += edgeTest(s, 100, 100, 3, 3, 3, 3, v);
        fails += centreTest(s, 100, 100, 3, v);        
        
        return fails;
    }
    
    public static int testRandom(boolean v) {        
        int fails = 0;
        Random random = new Random();
        ScalarField s = new ScalarField(100, 100, 2, 0, 100);
        
        for (int i = 0; i < 1000; i++) {           
            int x = SimConsts.getENV_MIN_SIZE() + random.nextInt(SimConsts.getENV_MAX_SIZE()-SimConsts.getENV_MIN_SIZE());
            int y = SimConsts.getENV_MIN_SIZE() + random.nextInt(SimConsts.getENV_MAX_SIZE()-SimConsts.getENV_MIN_SIZE());
            int d = random.nextInt(SimConsts.getENV_MAX_DENSITY()-SimConsts.getENV_MIN_DENSITY())+SimConsts.getENV_MIN_DENSITY();
            s.resample(x, y, d);

            int xSamples = (int) Math.ceil(x/100*d);
            int ySamples = (int) Math.ceil(y/100*d);     
            double xUnit = (double) x / (xSamples-1);
            double yUnit = (double) y / (ySamples-1);

            double T00 = random.nextInt(100);
            double T01 = random.nextInt(100);
            double T10 = random.nextInt(100);
            double T11 = random.nextInt(100);
            s.setTest(T00, T01, T10, T11);
            fails += cornerTest(s, xUnit, yUnit, T00, T01, T10, T11, false);   
            fails += edgeTest(s, xUnit, yUnit, (T00+T10)*0.5, (T00+T01)*0.5, (T10+T11)*0.5, (T01+T11)*0.5, false);     
            fails += centreTest(s, xUnit, yUnit, (T00+T10+T01+T11)*0.25, false); 
        }        
        
        if (v)
            System.out.println("Failures in 1000 randomised tests: " + fails);

        return fails;
    }    
    
    public static int cornerTest(ScalarField s, double xmax, double ymax, double expected0, double expected1, double expected2, double expected3, boolean v) {
        int fails = 0;
        
        Point p = new Point(0,0);
        
        double test = s.trueValueAt(p);
        if(!approxEquals(test, expected0, 0.0000001)) {
            fails++;
            if (v)
                System.out.println("(0,0): " + expected0 + " expected, actual value " + test);
        }
        
        p = new Point(0, ymax);
        test = s.trueValueAt(p);
        if(!approxEquals(test, expected1, 0.0000001)) {
            fails++;
            if (v)
                System.out.println("(0,ymax): " + expected1 + " expected, actual value " + test);
        }
        
        p = new Point(xmax, 0);      
        test = s.trueValueAt(p);
        if(!approxEquals(test, expected2, 0.0000001)) {
            fails++;
            if (v)
                System.out.println("(xmax,0): " + expected2 + " expected, actual value " + test);
        }    
        p = new Point(xmax, ymax);
        test = s.trueValueAt(p);
        if(!approxEquals(test, expected3, 0.0000001)) {
            fails++;
            if (v)
                System.out.println("(xmax,ymax): " + expected3 + " expected, actual value " + test);
        }   
        if  (v && fails == 0)
            System.out.println("Corners return expected values.");
        return fails;
    }

    public static int edgeTest(ScalarField s, double xmax, double ymax, double expected0, double expected1, double expected2, double expected3, boolean v) {
        int fails = 0;
        Point p = new Point(xmax*0.5, 0);
        double test = s.trueValueAt(p);
        if(!approxEquals(test, expected0, 0.0000001)) {
            fails++;
            if (v)
                System.out.println("(xmax*0.5, 0): " + expected0 + " expected, actual value " + test);
        }
        p = new Point(0, ymax*0.5);
        test = s.trueValueAt(p);
        if(!approxEquals(test, expected1, 0.0000001)) {
            fails++;
            if (v)
                System.out.println("(0, ymax*0.5): " + expected1 + " expected, actual value " + test);
        }
        p = new Point(xmax, ymax*0.5);
        test = s.trueValueAt(p);
        if(!approxEquals(test, expected2, 0.0000001)) {
            fails++;
            if (v)
                System.out.println("(xmax, ymax*0.5): " + expected2 + " expected, actual value " + test);
        }    
        p = new Point(xmax*0.5, ymax);
        test = s.trueValueAt(p);
        if(!approxEquals(test, expected3, 0.0000001)) {
            fails++;
            if (v)
                System.out.println("(xmax*0.5,ymax): " + expected3 + " expected, actual value " + test);
        }   
        if  (v && fails == 0)
            System.out.println("Edges return expected values.");
        return fails;
    }   
    
    public static int centreTest(ScalarField s, double xmax, double ymax, double expected0, boolean v) {
        int fails = 0;
        Point p = new Point(xmax*0.5, ymax*0.5);
        double test = s.trueValueAt(p);
        if(!approxEquals(test, expected0, 0.0000001)) {
            fails++;
            if (v)
                System.out.println("Centre: " + expected0 + " expected, actual value " + test);
        } 
        if  (v && fails == 0)
            System.out.println("Centre returns expected value.");
        return fails;
    }    

    public static boolean approxEquals(double v1, double v2, double tol) {
        if (Math.abs(v1 - v2) > tol) {
            return false;
        }
        return true;
    }
}
