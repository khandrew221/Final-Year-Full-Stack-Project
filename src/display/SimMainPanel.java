/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import simulation.SimStateFacade;

/**
 *
 * @author Kathryn Andrew
 */
public class SimMainPanel extends JPanel {
    
    private SimStateFacade sim;
    private Map<String, Object> simReport;
    private List<Map<String, Object>> fieldsReport;    
    
    private DataPanel dataPanel;
    private SimVis simVis;
    
    
    SimMainPanel(SimStateFacade s) {
        this.setLayout(new BorderLayout());
        this.sim = s;
        this.dataPanel = new DataPanel();
        this.simVis = new SimVis(s, 500, 500);
        this.add(simVis, BorderLayout.LINE_START);
        this.add(dataPanel, BorderLayout.LINE_END);
    }
    
    
    public void updateData() {
        simReport = sim.simReport();  
        fieldsReport = sim.fieldsReport();
        
        dataPanel.updateData(simReport, fieldsReport);
        simVis.updateData();
        
        repaint();
    }
    
}
