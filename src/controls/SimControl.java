/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controls;

import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.Set;
import simulation.Behaviour;
import simulation.BehaviourFactory;
import simulation.FitnessParameter;
import simulation.Sense;
import simulation.SenseFactory;
import simulation.Simulation;
import utility.Point;

/**
 *
 * @author Kathryn Andrew
 */
public class SimControl {
     
    private Simulation simulation;
    
    SimConsts settings = new SimConsts();
    
    public SimControl(Simulation simulation) {
        this.simulation = simulation;
    }
    
    /**
     * Pauses the simulation.  
     * 
     * Req. for: UC024
     */
    public void pause() {
        simulation.setState(SimState.PAUSED);
    }    
    
    /**
     * Plays the simulation.  
     * 
     * Req. for: UC025
     */
    public void play() {
        simulation.setState(SimState.RUNNING);
    }        
    
    /**
     * Stops the simulation.  
     * 
     * Req. for: UC003
     */
    public void stop() {
        simulation.setState(SimState.STOPPED);
    }      
    
    /**
     * Returns if the simulation is paused 
     * 
     * Req. for: UC024, UC025
     */
    public boolean isPaused() {
        if (simulation.getState() == SimState.PAUSED)
            return true;
        else
            return false;
    }        
    
    /**
     * Returns if the simulation is paused 
     * 
     * Req. for: UC024, UC025
     */
    public boolean isRunning() {
        if (simulation.getState() == SimState.RUNNING)
            return true;
        else
            return false;
    }   
    
    /**
     * Returns if the simulation is stopped
     * 
     * Req. for: 
     */
    public boolean isStopped() {
        if (simulation.getState() == SimState.STOPPED || 
                simulation.getState() == SimState.STOPPED_WITH_CRITICAL_CHANGE)
            return true;
        else
            return false;
    }     
    
    /**
     * Restarts the simulation with the same parameters but fresh bots.
     * 
     * Req. for: UC026
     */
    public void restart() {
        simulation.restart();
    }    
    
    /**
     * Returns if the simulation has had a critical change since it was last 
     * in a play state.
     * 
     * Req. for: UC024, UC025
     */
    public boolean hasCriticalChange() {
        if (simulation.getState() == SimState.STOPPED_WITH_CRITICAL_CHANGE)
            return true;
        else
            return false;
    }      
    
    /**
     * 
     * Returns true if the name already exists, false otherwise.
     * 
     * @param name
     * @param density
     * @param r
     * @param g
     * @param b
     * @return 
     */
    public boolean addField(String name, int density, double growthRate, int r, int g, int b) {
        if (simulation.listFields().contains(name)) {
            return true;
        } else {
            simulation.addField(name, density, growthRate, new Color(r, g, b));
            return false;
        }
    }
    
    /**
     * 
     * 0 for no errors
     * 1 for existing sense
     * 2 for no points
     * 3 for modification while running warning
     * 
     * @param target
     * @param centred
     * @param rings
     * @param pointsPerRing
     * @param radius
     * @return error code
     */
    public int addSenseEnviro(String target, boolean centred, int rings, int pointsPerRing, int radius) {
        if (isStopped()) {
            Sense sense = SenseFactory.MakeEnvironmentSense(target, simulation.getEnvironment(), centred, rings, pointsPerRing, radius);
            if (!centred) {
                if (rings == 0 || pointsPerRing == 0) {
                    return 2;
                }
            }
            if (simulation.containsSense(sense)) {
                return 1;
            }

            simulation.addSense(sense);
            return 0;
        } 
        return 3;
    }
    
    /**
     * 
     * 0 for no errors
     * 1 for existing sense
     * 3 for modification while running warning
     * 
     * @param radius
     * @return error code
     */
    public int addSenseBorder(double radius) {
        if (isStopped()) {
            Sense sense = SenseFactory.MakeBorderSense(simulation.getEnvironment(), radius);
            if (simulation.containsSense(sense)) {
                return 1;
            }
            simulation.addSense(sense);
            return 0;
        } 
        return 3;
    }    
    
    /**
     * 0 for no errors
     * 1 for existing behaviour
     * 3 for modification while running warning
     * @param maxSpeed
     * @return 
     */
    public int addBehaviourMove(double maxSpeed) {
        if (isStopped()) {
            Behaviour behaviour = BehaviourFactory.makeBehaviourMove(maxSpeed, new Point(0,0), new Point(simulation.envXSize(), simulation.envYSize()));
            if (simulation.containsMoveBehaviour()) {
                return 1;
            }
            simulation.addBehaviour(behaviour);
            return 0;
        } 
        return 3;
    }    
    
    /**
     * 0 for no errors
     * 3 for modification while running warning
     * @return 
     */
    public int addBehaviourEat(double forageEfficiency, double energyEfficiency, String target) {
        if (isStopped()) {
            Behaviour behaviour = BehaviourFactory.makeBehaviourEat(forageEfficiency, energyEfficiency, target, simulation.getEnvironment());
            simulation.addBehaviour(behaviour);
            return 0;
        } 
        return 3;
    }    
        
    /**
     * 
     * @return 
     */
    public void getBorderSamplePoints(List<Double> x, List<Double> y, double radius) {
        x.clear();
        y.clear();
        SenseFactory.getBorderSamplePoints(x, y, radius);
    }       
    
    /**
     * 
     * @return 
     */
    public void getEnviroSamplePoints(List<Double> x, List<Double> y, boolean centred, int rings, int pointsPerRing, double radius) {
        x.clear();
        y.clear();
        SenseFactory.getEnviroSamplePoints(x, y, centred, rings, pointsPerRing, radius);
    }       
    
    /**
     * Removes fields with the given names.
     * 
     * Req for: UC028
     */
    public void removeFields(Set<String> toRemove) {
        simulation.removeFields(toRemove);
    }    
    
    /**
     * Removes senses with the given ID numbers.
     * 
     * Req for: UC029
     */
    public void removeSenses(Set<Integer> toRemove) {
        simulation.removeSenses(toRemove);
    }      
    
    /**
     * Removes behaviours with the given ID numbers.
     * 
     * Req for: UC030
     */
    public void removeBehaviours(Set<Integer> toRemove) {
        simulation.removeBehaviours(toRemove);
    }    
    
    /**
     * Sets the weight of a fitness parameter. No action will be taken for a 
     * parameter that cannot be identified by the given string. Weights over the 
     * maximum will set the parameter to the maximum.  Weights over the 
     * minimum will set the parameter to the minimum.  
     * 
     * Req. for: UC033
     * @param weight
     * @param parameter 
     */
    public void setFitnessWeight(int weight, String parameter) {    
        try {
            FitnessParameter param = FitnessParameter.valueOf(parameter);
            if (weight > SimConsts.getFITNESS_WEIGHT_MAX()) {
                simulation.getGAEngine().setFitnessWeight(SimConsts.getFITNESS_WEIGHT_MAX(), param);
            } else if (weight <= SimConsts.getFITNESS_WEIGHT_MAX() &&
                       weight >= SimConsts.getFITNESS_WEIGHT_MIN()) {
                simulation.getGAEngine().setFitnessWeight(weight, param);
            } else {
               simulation.getGAEngine().setFitnessWeight(SimConsts.getFITNESS_WEIGHT_MIN(), param); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }       
    }
    
    /**
     * Sets the weight of a set of fitness parameters. No action will be taken for a 
     * parameter that cannot be identified by the given string. Weights over the 
     * maximum will set the parameter to the maximum.  Weights over the 
     * minimum will set the parameter to the minimum.  
     * 
     * Req. for: UC033
     * @param weight
     * @param parameter 
     */
    public void setFitnessWeights(Map<String, Integer> newWeights) {  
        for (String parameter : newWeights.keySet()) {
            setFitnessWeight(newWeights.get(parameter), parameter);
        }   
    }    
     
    
    public void setMAX_LAYERS(int MAX_LAYERS) {
        SimConsts.setMAX_LAYERS(MAX_LAYERS);
    }

    public void setMAX_NODES_PER_LAYER(int MAX_NODES_PER_LAYER) {
        SimConsts.setMAX_NODES_PER_LAYER(MAX_NODES_PER_LAYER);
    }  
    

}
