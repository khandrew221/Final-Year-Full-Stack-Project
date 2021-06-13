/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    
    private final Simulation sim;
    
    public SimStateFacade(Simulation s) {
        sim = s;
    }

    /**
     * req. for: UC003
     * @return 
     */
    public Set<String> getSenseTypes() {
        Set<String> out = new HashSet<>();
        EnumSet.allOf(SenseType.class).forEach(sense -> out.add(sense.label));
        return out;
    }
    
    /**
     * req. for: UC003
     * @return 
     */
    public Set<String> getFields() {
        return sim.listFields();
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
     * Req for: UC021
     * @return 
     */
    public  List<Map<String, Object>> fieldsReport() {
        return sim.fieldsReport();
    }     
    
    /**
     * 
     * Req for: UC022
     * @return 
     */
    public  Map<String, Object> simReport() {
        return sim.simulationReport();
    }     
    
    /**
     * 
     * Req for: UC003, UC029
     * @return 
     */
    public Map<String, String> senseReport() {
        return sim.senseReport();
    }     

    /**
     * 
     * Req for: UC004, UC030
     * @return 
     */
    public Map<String, String> behaviourReport() {       
        return sim.behaviourReport();
    }     
    /**
     * 
     * Req for: UC017
     * @return 
     */
    public int envXSize() {
        return sim.envXSize();
    }
    
    /**
     * 
     * Req for: UC017
     * @return 
     */
    public int envYSize() {
        return sim.envYSize();
    }    
    
}
