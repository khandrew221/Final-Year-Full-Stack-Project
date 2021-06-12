/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * Abstract class for a component that displays a list of items. The items 
 * may have associated checkboxes and the SelectableList may be linked to a 
 * JPanel that it can update on changes to the checkboxes.
 * 
 * @author Kathryn Andrew
 */
public abstract class SelectableList extends JPanel {
    
    private JPanel container;
    private Map<String, JCheckBox> checkBoxes = new HashMap<>();   
    
    public SelectableList(JPanel container) {
        this.container = container;            
    }
    
    public Map<String, JCheckBox> getCheckBoxes() {
        return checkBoxes;
    }
    
    public JPanel getContainer() {
        return container;
    }
    
    /**
     * Returns the set of selected object names.
     * @return 
     */
    public Set<String> getSelected() {
        Set<String> out = new HashSet<>();
        for (String s : checkBoxes.keySet()) {
            if (checkBoxes.get(s).isSelected()) {
                out.add(s);
            }
        }
        return out;
    }
    
    /**
     * sets the checkboxes matching the names in the given set to be selected.
     * All others will be deselected.
     * @return 
     */
    public void setSelected(Set<String> select) {
        for (String s : checkBoxes.keySet()) {
            if (select.contains(s)) {
                checkBoxes.get(s).setSelected(true);
            } else {
                checkBoxes.get(s).setSelected(false);
            }
        }
        updateContainer();
    } 
    
   /**
     * 
     * sets up the selection panel.Should be called after changes to relevant
     * simulation parameters. 
     * 
     * @param title
     * @param interactive
     * @param autoSelectFirst 
     */
    public void setup(String title, Set<String> labels, boolean interactive, boolean autoSelectFirst) {
        Set<String> oldSelected = getSelected();
        this.removeAll();
        checkBoxes.clear();
        this.setBorder(BorderFactory.createTitledBorder(title));
        boolean first = autoSelectFirst;
        for(String label : labels) {
            JPanel entry = new JPanel();
            if (interactive) {               
                JCheckBox checkBox = new JCheckBox(label);
                if (first) {
                    checkBox.setSelected(true);
                    first = false;
                } else {
                    if (oldSelected.contains(label))
                        checkBox.setSelected(true);
                }
                checkBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        updateContainer();
                    }
                }); 
                entry.add(checkBox);
                checkBoxes.put(label, checkBox);
            } else {
                JLabel fieldName = new JLabel(label);
                entry.add(fieldName);            
            }
            this.add(entry);
        }        
        updateContainer();        
        this.setPreferredSize(new Dimension(200,35*labels.size() + 30));
    }    
    
    /**
     * Allows the container panel to be updated with changes to the 
     * SelectableList.  
     */
    public abstract void updateContainer(); 
      
}
