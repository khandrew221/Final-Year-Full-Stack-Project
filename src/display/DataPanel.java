/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import java.awt.Font;
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
    
    private SimMainPanel main;
    private FieldsSelect fieldsSelect;     
    
    private JLabel population = new JLabel("Population: ");
    private JLabel cycles = new JLabel("Simulation Cycles: ");
    private JLabel senses = new JLabel("Senses: ");
    private JLabel behaviours = new JLabel("Behaviours: ");    
    
    public DataPanel(SimMainPanel main) {
        this.main = main;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.add(population);
        Font font = population.getFont().deriveFont(Font.PLAIN);
        population.setFont(font);
        this.add(cycles);
        cycles.setFont(font);
        this.add(senses);
        senses.setFont(font);
        this.add(behaviours);
        behaviours.setFont(font);
        
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
        
        String[] s = (String[]) simReport.get("senses");
        String senseString = "<html><br/><b>Senses: </b>" + s.length;
        for (int i = 0; i < s.length; i++) {
            senseString += "<br/>Sense " + (i+1) + ": "; 
            senseString += s[i];
        }   
        senseString += "</html>";
        senses.setText(senseString);
        
        String[] b = (String[]) simReport.get("behaviours");
        String behavString = "<html><br/><b>Behaviours: </b>" + b.length;
        for (int i = 0; i < b.length; i++) {
            behavString += "<br/>Behaviour " + (i+1) + ": ";
            behavString += b[i];
        }        
        behaviours.setText(behavString);       
        
        fieldsSelect.setup(fieldsReport, true, true);
        
        repaint();
    }                    
    
}
