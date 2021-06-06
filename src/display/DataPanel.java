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
import simulation.SimStateFacade;

/**
 *
 * @author Kathryn Andrew
 */
public class DataPanel extends JComponent {
    
    private SimStateFacade sim;
    private Map<String, Object> simReport;
    private  List<Map<String, Object>> fieldsReport;
    
    private JLabel population = new JLabel("Population: ");
    private JLabel cycles = new JLabel("Simulation Cycles: ");
    private JLabel senses = new JLabel("Senses: ");
    private JLabel behaviours = new JLabel("Behaviours: ");
    private JLabel fields = new JLabel("Environment fields: ");
    
    DataPanel(SimStateFacade s) {
        this.sim = s;
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
        this.add(fields);
        fields.setFont(font);        
    }

    
    
    
    
    public void updateData() {
        simReport = sim.simReport();  
        fieldsReport = sim.fieldsReport();
        
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
        
        String fieldsString = "<html><br/><b>Fields: </b>" + fieldsReport.size();
        for(Map m : fieldsReport) {
            String field = (String) m.get("name");
            fieldsString += "<br/>" + field;
        } 
        fields.setText(fieldsString);
        
        repaint();
    }
    
}
