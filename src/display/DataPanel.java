/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import simulation.SimStateFacade;

/**
 *
 * @author Kathryn Andrew
 */
public class DataPanel extends JComponent {
    
    private RunView main;
    private SimStateFacade facade;
    private FieldsSelect fieldsSelect;     
    private SensesSelect sensesSelect;  
    private BehavioursSelect behavioursSelect;
    
    private JLabel population = new JLabel("Population: ");
    private JLabel cycles = new JLabel("Simulation Cycles: ");   
    
    public DataPanel(RunView main, SimStateFacade facade) {
        this.main = main;
        this.facade = facade;
        this.setLayout(new BorderLayout());
               
        JPanel mainHolder = new JPanel();
        mainHolder.setBorder(BorderFactory.createEtchedBorder());
        mainHolder.setLayout(new BoxLayout(mainHolder, BoxLayout.PAGE_AXIS));
        this.add(mainHolder, BorderLayout.CENTER);
        
        mainHolder.add(population, Component.LEFT_ALIGNMENT);
        mainHolder.add(cycles, Component.LEFT_ALIGNMENT);
        
        mainHolder.add(Box.createRigidArea(new Dimension(5,15)));
        
        sensesSelect = new SensesSelect(null);
        mainHolder.add(sensesSelect, Component.LEFT_ALIGNMENT);
        
        behavioursSelect = new BehavioursSelect(null);
        mainHolder.add(behavioursSelect, Component.LEFT_ALIGNMENT);
        
        fieldsSelect = new FieldsSelect(this.main);
        mainHolder.add(fieldsSelect, Component.LEFT_ALIGNMENT);
        
        Dimension minSize = new Dimension(0, 0);
        Dimension prefSize = new Dimension(0, 300);
        Dimension maxSize = new Dimension(Short.MAX_VALUE, 400);
        mainHolder.add(new Box.Filler(minSize, prefSize, maxSize));
        
        
    }
    
    /**
     * Updates frequently changing data.  Call every frame.
     * 
     * @param currentPopulation
     * @param currentTime 
     */
    public void updateData() {   
        Map<String, Object> simReport = facade.simReport();
        population.setText("<html><b>Population:</b> " + (int) simReport.get("population") + "</html>");        
        cycles.setText("<html><b>Simulation Cycles:</b> " + (long) simReport.get("time") + "</html>");             
        repaint();
    }
    
    /**
     * sets all data.  Only call if changes to senses or behaviours
     * have occurred.
     * @param simReport
     * @param fieldsReport 
     */
    public void setAll() {
        Map<String, Object> simReport = facade.simReport();
        List<Map<String, Object>> fieldsReport = facade.fieldsReport();
        
        population.setText("<html><b>Population:</b> " + (int) simReport.get("population") + "</html>");        
        cycles.setText("<html><b>Simulation Cycles:</b> " + (long) simReport.get("time") + "</html>");
        
        Map<String, String> s = (Map<String, String>) simReport.get("senses");
        sensesSelect.setup("Current Senses", s, false, false, 400);       
        
        Map<String, String> b = (Map<String, String>) simReport.get("behaviours");
        behavioursSelect.setup("Current Behaviours", b, false, false, 400);          
        
        Map<String, String> fieldNames = new HashMap<>();
        for (Map m : fieldsReport) {
            String field = (String) m.get("name");
            fieldNames.put(field, field);
        }        
        fieldsSelect.setup("Current Fields", fieldNames, true, true, 400);
        
        repaint();
    }                    
    
}
