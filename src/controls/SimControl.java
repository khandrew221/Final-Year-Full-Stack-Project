/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controls;

import java.awt.Color;
import java.util.Set;
import simulation.Sense;
import simulation.SenseFactory;
import simulation.Simulation;

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
     * Returns if the simulation is paused 
     * 
     * Req. for: UC024, UC025
     */
    public boolean isStopped() {
        if (simulation.getState() == SimState.STOPPED)
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
    public boolean addField(String name, int density, int r, int g, int b) {
        if (simulation.listFields().contains(name)) {
            return true;
        } else {
            simulation.addField(name, density, new Color(r, g, b));
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
        if (simulation.getState() == SimState.STOPPED) {
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
    
    
    public void setMAX_LAYERS(int MAX_LAYERS) {
        SimConsts.setMAX_LAYERS(MAX_LAYERS);
    }

    public void setMAX_NODES_PER_LAYER(int MAX_NODES_PER_LAYER) {
        SimConsts.setMAX_NODES_PER_LAYER(MAX_NODES_PER_LAYER);
    }  
    
}
