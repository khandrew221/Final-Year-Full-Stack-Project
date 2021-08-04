/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
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
        mainHolder.setPreferredSize(new Dimension(200,200));
        this.add(mainHolder, BorderLayout.CENTER);
        
        mainHolder.add(population);
        Font font = population.getFont().deriveFont(Font.PLAIN);
        population.setFont(font);
        mainHolder.add(cycles);
        cycles.setFont(font);
        
        sensesSelect = new SensesSelect(null);
        mainHolder.add(sensesSelect);
        sensesSelect.setFont(font);
        
        behavioursSelect = new BehavioursSelect(null);
        mainHolder.add(behavioursSelect);
        behavioursSelect.setFont(font);
        
        fieldsSelect = new FieldsSelect(this.main);
        mainHolder.add(fieldsSelect);
        
        
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
        sensesSelect.setup("Senses", s, false, false);       
        
        Map<String, String> b = (Map<String, String>) simReport.get("behaviours");
        behavioursSelect.setup("Behaviours", b, false, false);          
        
        Map<String, String> fieldNames = new HashMap<>();
        for (Map m : fieldsReport) {
            String field = (String) m.get("name");
            fieldNames.put(field, field);
        }        
        fieldsSelect.setup("Fields", fieldNames, true, true);
        
        repaint();
    }                    
    
}
