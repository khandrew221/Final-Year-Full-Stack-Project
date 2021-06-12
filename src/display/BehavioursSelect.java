/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Kathryn Andrew
 */
public class BehavioursSelect extends SelectableList {
    
    /**
     * Creates a display of behaviours with optional tickboxes for each 
     * behaviours.
     * 
     * @param container
     */    
    public BehavioursSelect(JPanel container) {
        super(container);
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        Font font = this.getFont().deriveFont(Font.PLAIN);
        this.setFont(font);         
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
    }
    
    
    /**
     * Updates the container when a checkbox is activated.
     */    
    public void updateContainer() {
        /*
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
        }*/
    }
}
