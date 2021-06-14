/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import controls.SimControl;
import java.awt.BorderLayout;
import java.awt.Dimension;
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
    
    private SimStateFacade facade;       
    private DataPanel dataPanel;
    private SimVis simVis;
    private ControlPanel controlPanel;
    
    
    SimMainPanel(SimControl control, SimStateFacade simFacade, int visX, int visY) {
        this.setLayout(new BorderLayout());
        this.facade = simFacade;
        this.dataPanel = new DataPanel(this);
        dataPanel.setPreferredSize(new Dimension(500, 500));
        this.controlPanel = new ControlPanel(this, control);
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
        dataPanel.setAll(facade.simReport(), facade.fieldsReport());
        simVis.updateData();     
        controlPanel.setPausePlayText();
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
        Map<String, Object> simReport = facade.simReport(); 
        List<Map<String, Object>> fieldsReport = facade.fieldsReport();         
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
