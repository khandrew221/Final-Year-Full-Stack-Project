/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import java.awt.Font;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Kathryn Andrew
 */
public class DataPanel extends JComponent {
    
    private RunView main;
    private FieldsSelect fieldsSelect;     
    private SensesSelect sensesSelect;  
    private BehavioursSelect behavioursSelect;
    
    private JLabel population = new JLabel("Population: ");
    private JLabel cycles = new JLabel("Simulation Cycles: ");   
    
    public DataPanel(RunView main) {
        this.main = main;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.add(population);
        Font font = population.getFont().deriveFont(Font.PLAIN);
        population.setFont(font);
        this.add(cycles);
        cycles.setFont(font);
        
        sensesSelect = new SensesSelect(null);
        this.add(sensesSelect);
        sensesSelect.setFont(font);
        
        behavioursSelect = new BehavioursSelect(null);
        this.add(behavioursSelect);
        behavioursSelect.setFont(font);
        
        fieldsSelect = new FieldsSelect(this.main);
        this.add(fieldsSelect);
        
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
    }
    
    /**
     * Updates frequently changing data.  Call every frame.
     * 
     * @param currentPopulation
     * @param currentTime 
     */
    public void updateData(int currentPopulation, long currentTime) {        
        population.setText("<html><b>Population:</b> " + currentPopulation + "</html>");        
        cycles.setText("<html><b>Simulation Cycles:</b> " + currentTime + "</html>");             
        repaint();
    }
    
    /**
     * sets all data.  Only call if changes to senses or behaviours
     * have occurred.
     * @param simReport
     * @param fieldsReport 
     */
    public void setAll(Map<String, Object> simReport, List<Map<String, Object>> fieldsReport) {
        
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
