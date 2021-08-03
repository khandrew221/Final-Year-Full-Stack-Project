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
     * req. for: UC004
     * @return 
     */
    public Set<String> getBehaviourTypes() {
        Set<String> out = new HashSet<>();
        EnumSet.allOf(BehaviourType.class).forEach(behaviour -> out.add(behaviour.label));
        return out;
    }    
    
    /**
     * req. for: UC033
     * @return 
     */
    public Map<String, Integer> getFitnessFunctionParameters() {
        Map<String, Integer> out = new HashMap<>();
        EnumSet.allOf(FitnessParameter.class).forEach(parameter -> out.put(parameter.toString(), sim.getGAEngine().getFitnessWeight(parameter)));
        return out;
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
    
    
    public double getBehaviourEatForageEfficiencyMin() {
        return BehaviourFactory.EAT_FORAGE_EFFICIENCY_LIMITS.getMin();
    }

    public double getBehaviourEatForageEfficiencyMax() {
        return BehaviourFactory.EAT_FORAGE_EFFICIENCY_LIMITS.getMax();
    }    
    
    public double getBehaviourEatEnergyEfficiencyMin() {
        return BehaviourFactory.EAT_ENERGY_EFFICIENCY_LIMITS.getMin();
    }

    public double getBehaviourEatEnergyEfficiencyMax() {
        return BehaviourFactory.EAT_ENERGY_EFFICIENCY_LIMITS.getMax();
    }    
    
    public double getBehaviourMoveSpeedMax() {
        return BehaviourFactory.MOVE_SPEED_LIMITS.getMax();
    }    
    public double getBehaviourMoveSpeedMin() {
        return BehaviourFactory.MOVE_SPEED_LIMITS.getMin();
    }  

    public double getSenseEnviroRingsMax() {
        return SenseFactory.ENVIRO_RING_LIMITS.getMax();
    }    
    public double getSenseEnviroRingsMin() {
        return SenseFactory.ENVIRO_RING_LIMITS.getMin();
    }      
    
    public double getSenseEnviroPointsPerRingMax() {
        return SenseFactory.ENVIRO_POINTS_PER_RING_LIMITS.getMax();
    }    
    public double getSenseEnviroPointsPerRingMin() {
        return SenseFactory.ENVIRO_POINTS_PER_RING_LIMITS.getMin();
    }    
    
    public double getSenseEnviroRadiusMin() {
        return SenseFactory.ENVIRO_RADIUS_LIMITS.getMin();
    }    
    public double getSenseEnviroRadiusMax() {
        return SenseFactory.ENVIRO_RADIUS_LIMITS.getMax();
    }    
    
    public double getSenseBorderRadiusMin() {
        return SenseFactory.BORDER_RADIUS_LIMITS.getMin();
    }    
    public double getSenseBorderRadiusMax() {
        return SenseFactory.BORDER_RADIUS_LIMITS.getMax();
    }     
}
