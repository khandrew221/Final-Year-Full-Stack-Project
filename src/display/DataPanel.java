/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Kathryn Andrew
 */
public class DataPanel extends JComponent {
    
    private SimMainPanel main;
    
    private JLabel population = new JLabel("Population: ");
    private JLabel cycles = new JLabel("Simulation Cycles: ");
    private JLabel senses = new JLabel("Senses: ");
    private JLabel behaviours = new JLabel("Behaviours: ");    
    private JPanel fieldsSelect = new JPanel();
    private JLabel fieldsLabel = new JLabel("Environment fields: ");
    private Map<String, JCheckBox> fieldsCheckBoxes = new HashMap<>();
    
    DataPanel(SimMainPanel main) {
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
        
        this.add(fieldsSelect);
        fieldsSelect.setLayout(new BoxLayout(fieldsSelect, BoxLayout.PAGE_AXIS));
        fieldsLabel.setFont(font); 
        
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
    }
    
    public void updateData(int currentPopulation, long currentTime) {        
        population.setText("<html><b>Population:</b> " + currentPopulation + "</html>");        
        cycles.setText("<html><b>Simulation Cycles:</b> " + currentTime + "</html>");             
        repaint();
    }
    
    
    public void resetAll(Map<String, Object> simReport, List<Map<String, Object>> fieldsReport) {
        
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
        

        fieldsPanelSetup(fieldsReport);
        
        repaint();
    }    
    
    
    private void fieldsPanelSetup(List<Map<String, Object>> fieldsReport) {
        fieldsSelect.removeAll();
        fieldsCheckBoxes.clear();
        
        fieldsSelect.add(fieldsLabel);
        fieldsLabel.setText("<html><br/><b>Fields:</b></html>");
        boolean first = true;
        for(Map m : fieldsReport) {
            String field = (String) m.get("name");
            JCheckBox checkBox = new JCheckBox(field);
            if (first) {
                checkBox.setSelected(true);
                first = false;
            }
            checkBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    updateMain();
                }
            }); 
            fieldsSelect.add(checkBox);
            fieldsCheckBoxes.put(field, checkBox);
        }
        
        updateMain();
    }
    
    
    void updateMain() {        
        Map<String, Boolean> out = new HashMap<>();
        for (String f : fieldsCheckBoxes.keySet()) {
            out.put(f, fieldsCheckBoxes.get(f).isSelected());  
        }
        main.updateFieldSelect(out);
    }
                        
    
}
