/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.Set;
import utility.Point;

/**
 *
 * The state facade is designed for interface with components that need to know 
 * information about the simulation's current state, e.g. for graphical display, 
 * but should not be allowed access to anything that may alter the running of 
 * the simulation.  All method should pass copies of simulation data only to 
 * prevent any possible data leakage and accidental modification.
 * 
 * @author Kathryn Andrew
 */
public class SimStateFacade {
    
    private Simulation sim;
    
    
    public SimStateFacade(Simulation s) {
        sim = s;
    }

    
    /**
     * 
     * Req for: UC020
     * @return 
     */
    public  List<Map<String, Object>> botReport() {
        return sim.botReport();
    }    

    /**
     * 
     * Req for: UC017
     * @return 
     */
    public Set<String> listFields() {
        return sim.listFields();
    }    
    
    /**
     * 
     * Req for: UC017
     * @return 
     */
    public int[] getFieldRGB(String s) {
        int[] out = new int[3];
        Color c = sim.getFieldColor(s);
        out[0] = c.getRed();
        out[1] = c.getGreen();
        out[2] = c.getBlue();
        return out;
    }        
    

    /**
     * 
     * Req for: UC017
     * @return 
     */
    public double envNormValueAt(String s, double x, double y) {
        return sim.envNormValueAt(s, x, y);
    }      
    
}
