/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import java.util.ArrayList;
import java.util.List;
import utility.MinMaxPair;
import utility.Point;

/**
 *
 * @author Kathryn Andrew
 */
public class SenseFactory {
    
    public static final MinMaxPair ENVIRO_RING_LIMITS = new MinMaxPair(0, 3);
    public static final MinMaxPair ENVIRO_POINTS_PER_RING_LIMITS = new MinMaxPair(1, 20);
    public static final MinMaxPair ENVIRO_RADIUS_LIMITS = new MinMaxPair(1, 10);
    public static final MinMaxPair BORDER_RADIUS_LIMITS = new MinMaxPair(1, 10);
    
    /**
     * Creates an envirosense.
     * @return 
     */
    public static SenseEnviro MakeEnvironmentSense(String target, Environment e, boolean centred, int rings, int pointsPerRing, double radius) {
        
        List<Point> samplePoints = new ArrayList<>();
        
        if(centred) {
            samplePoints.add(new Point(0,0));
        } 
        
        if (rings > 0 && pointsPerRing > 0) {
            double radianStep = Math.PI*2/pointsPerRing;
            for (int ring = 0; ring < rings; ring++) {
                for (int points = 0; points < pointsPerRing; points++) {
                    double radians = (radianStep * points);
                    double x = radius*(ring+1)*Math.cos(radians);
                    double y = radius*(ring+1)*Math.sin(radians);
                    samplePoints.add(new Point(x, y));
                }
            }
        }
        
        return new SenseEnviro(target, e, samplePoints);
    }

    /**
     * Creates an border sense.
     * @param e
     * @param radius
     * @return 
     */
    public static SenseBorder MakeBorderSense(Environment e, double radius) {
        
        List<Point> samplePoints = new ArrayList<>();
        
        samplePoints.add(new Point(0,radius));
        samplePoints.add(new Point(0,-radius));
        samplePoints.add(new Point(radius,0));
        samplePoints.add(new Point(-radius,0));

        return new SenseBorder(e, samplePoints);
    }    
    
    
    public static void getBorderSamplePoints(List<Double> x, List<Double> y, double radius) {
        x.add(0.0);
        x.add(0.0);
        x.add(radius);
        x.add(-radius);
        y.add(radius);
        y.add(-radius);
        y.add(0.0);
        y.add(0.0);        
    } 
    
    public static void getEnviroSamplePoints(List<Double> xVals, List<Double> yVals, boolean centred, int rings, int pointsPerRing, double radius) {
        List<Point> samplePoints = new ArrayList<>();
        
        if(centred) {
            samplePoints.add(new Point(0,0));
        } 
        
        if (rings > 0 && pointsPerRing > 0) {
            double radianStep = Math.PI*2/pointsPerRing;
            for (int ring = 0; ring < rings; ring++) {
                for (int points = 0; points < pointsPerRing; points++) {
                    double radians = (radianStep * points);
                    double x = radius*(ring+1)*Math.cos(radians);
                    double y = radius*(ring+1)*Math.sin(radians);
                    samplePoints.add(new Point(x, y));
                }
            }
        }    
        
        for (Point p : samplePoints) {
            xVals.add(p.getX());
            yVals.add(p.getY());            
        }
    }     
}
