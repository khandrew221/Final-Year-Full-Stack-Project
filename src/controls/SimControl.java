/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controls;

import java.awt.Color;
import java.util.Set;
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
     * Removes fields with the given names.
     * 
     * Req for: UC028
     */
    public void removeFields(Set<String> toRemove) {
        simulation.removeFields(toRemove);
    }    
    
    
    public void setMAX_LAYERS(int MAX_LAYERS) {
        SimConsts.setMAX_LAYERS(MAX_LAYERS);
    }

    public void setMAX_NODES_PER_LAYER(int MAX_NODES_PER_LAYER) {
        SimConsts.setMAX_NODES_PER_LAYER(MAX_NODES_PER_LAYER);
    }  
    
}
