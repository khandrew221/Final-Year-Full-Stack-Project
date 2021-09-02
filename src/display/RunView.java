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
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import simulation.SimStateFacade;

/**
 *
 * @author Kathryn Andrew
 */
public class RunView extends JPanel {
    
    public static final int GRAPH_SIZE = 150;
    
    private SimStateFacade facade;       
    private SimVis simVis;
    private ControlPanel controlPanel;
    private Graph fitnessGraph;
    
    
    RunView(SimControl control, SimStateFacade simFacade, int visX, int visY) {
        this.setLayout(new BorderLayout());
        this.facade = simFacade;
        this.simVis = new SimVis(simFacade, simFacade.envXSize(), simFacade.envYSize(), visX, visY);
        this.add(simVis, BorderLayout.PAGE_START);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setAll();
        controlPanel = new ControlPanel(control);
        
        fitnessGraph = new Graph("Average Fitness", facade, 500, GRAPH_SIZE, 10000);
        fitnessGraph.addSeries("Fitness");
        fitnessGraph.setYAxisLabel("Fitness");
        fitnessGraph.updateData();
        
        this.add(fitnessGraph, BorderLayout.CENTER);
        
        this.add(controlPanel, BorderLayout.PAGE_END);
    }
    

    /**
     * sets all subcomponent data.  Only call if changes to senses or behaviours
     * have occurred.
     */
    public void setAll() {
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
            System.out.println("Error casting time to long.");
        }
        simVis.updateData(); 
        fitnessGraph.updateData();
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
