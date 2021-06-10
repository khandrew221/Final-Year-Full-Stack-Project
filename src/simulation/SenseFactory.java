/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import java.util.ArrayList;
import java.util.List;
import utility.Point;

/**
 *
 * @author Kathryn Andrew
 */
public class SenseFactory {
    
    
    
    /**
     * Creates an envirosense.
     * @return 
     */
    public static SenseEnviro MakeEnvironmentSense(String target, Environment e, boolean centred, int rings, int pointsPerRing, int radius) {
        
        List<Point> samplePoints = new ArrayList<>();
        
        if(centred) {
            samplePoints.add(new Point(0,0));
        } 
        
        if (rings > 0) {
            double radianStep = Math.PI*2/pointsPerRing;
            for (int ring = 0; ring < rings; ring++) {
                for (double radians = 0; radians < Math.PI*2; radians+=radianStep) {
                    System.out.println(radians);
                    double x = radius*(ring+1)*Math.cos(radians);
                    double y = radius*(ring+1)*Math.sin(radians);
                    samplePoints.add(new Point(x, y));
                }
            }
        }
        
        return new SenseEnviro(target, e, samplePoints);
    }
    
}
