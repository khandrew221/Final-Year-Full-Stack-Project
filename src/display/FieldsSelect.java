/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Kathryn Andrew
 */
public class FieldsSelect extends SelectableList {

    //private JPanel container;
    //private Map<String, JCheckBox> checkBoxes = new HashMap<>();    
    
    /**
     * Creates a display of fields with tickboxes for each field.
     * 
     * @param container
     */    
    public FieldsSelect(JPanel container) {
        super(container);
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        Font font = this.getFont().deriveFont(Font.PLAIN);
        this.setFont(font);         
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
    }
    
    /**
     * 
     * sets up the selection panel using a fieldsReport().  Should be called 
     * after changes to number of fields, field names, or field colours.
     * 
     * @param fieldsReport
     * @param interactive
     * @param autoSelectFirst 
     */
    public void setup(List<Map<String, Object>> fieldsReport, boolean interactive, boolean autoSelectFirst) {
        Set<String> oldSelected = getSelected();
        this.removeAll();
        super.getCheckBoxes().clear();
        this.setBorder(BorderFactory.createTitledBorder("Fields"));
        boolean first = autoSelectFirst;
        for(Map m : fieldsReport) {
            JPanel entry = new JPanel();
            JPanel swatch = new JPanel();
            int[] rgb = (int[]) m.get("RGB");
            swatch.setBackground(new Color(rgb[0], rgb[1], rgb[2])); 
            swatch.setPreferredSize(new Dimension(10,10));
            entry.add(swatch);   
            String field = (String) m.get("name");
            if (interactive) {               
                JCheckBox checkBox = new JCheckBox(field);
                if (first) {
                    checkBox.setSelected(true);
                    first = false;
                } else {
                    if (oldSelected.contains(field))
                        checkBox.setSelected(true);
                }
                checkBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        updateContainer();
                    }
                }); 
                entry.add(checkBox);
                super.getCheckBoxes().put(field, checkBox);
            } else {
                JLabel fieldName = new JLabel(field);
                entry.add(fieldName);            
            }
            this.add(entry);
        }        
        updateContainer();        
        this.setPreferredSize(new Dimension(200,35*fieldsReport.size() + 30));
    }
    
    /**
     * Updates the container when a checkbox is activated.
     */    
    public void updateContainer() {
        JPanel container = super.getContainer();
        if (container != null) {
            if (container instanceof SimMainPanel) {
                SimMainPanel n = (SimMainPanel) container;
                n.setSelected(getSelected());
            } else if (container instanceof FieldMaker) {
                FieldMaker n = (FieldMaker) container;
                n.setSelected(getSelected());   
                n.repaint();
            }
        }
    }
        
   
    
}
