/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import controls.SimControl;
import java.awt.BorderLayout;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JPanel;
import simulation.SimStateFacade;
import simulation.Simulation;

/**
 *
 * @author Kathryn Andrew
 */
public class SimMainPanel extends JPanel {
    
    private SimStateFacade sim;       
    private DataPanel dataPanel;
    private SimVis simVis;
    private ControlPanel controlPanel;
    
    
    SimMainPanel(Simulation simulation, SimStateFacade simFacade, int visX, int visY) {
        this.setLayout(new BorderLayout());
        this.sim = simFacade;
        this.dataPanel = new DataPanel(this);
        this.controlPanel = new ControlPanel(this, new SimControl(simulation));
        this.simVis = new SimVis(simFacade, simFacade.envXSize(), simFacade.envYSize(), visX, visY);
        this.add(simVis, BorderLayout.LINE_START);
        this.add(dataPanel, BorderLayout.LINE_END);
        this.add(controlPanel, BorderLayout.NORTH);
        setAll();
    }
    

    /**
     * sets all subcomponent data.  Only call if changes to senses or behaviours
     * have occurred.
     */
    public void setAll() {
        dataPanel.setAll(sim.simReport(),  sim.fieldsReport());
        simVis.updateData();       
        repaint();
    }

    /**
     * updates frequently updating information:
     *  - population
     *  - simulation time
     *  - visualisation
     * 
     * Call every frame.
     * 
     */
    public void updateData() {        
        Map<String, Object> simReport = sim.simReport(); 
        List<Map<String, Object>> fieldsReport = sim.fieldsReport();         
        int population = 0;
        long time = 0;        
        try {
            population  = (int) simReport.get("population");
        } catch (Exception e) {
            System.out.println("Error casting population to int.");
        }
        try {
            time  = (long) simReport.get("time");
        } catch (Exception e) {
            System.out.println("Error casting timen to long.");
        }
        dataPanel.updateData(population, time);
        simVis.updateData();        
        repaint();
    }
    
    /**
     * Allows the datapanel FieldsSelect to update the SimVis
     * @param newActiveFields 
     */
    public void setSelected(Set<String> newActiveFields) {
        simVis.setActiveFields(newActiveFields);
    }
  
}
