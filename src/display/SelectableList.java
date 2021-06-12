/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.JCheckBox;
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
     * Allows the container panel to be updated with changes to the 
     * SelectableList.  
     */
    public abstract void updateContainer(); 
}
