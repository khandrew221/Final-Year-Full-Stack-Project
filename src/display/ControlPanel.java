/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Kathryn Andrew
 */
public class ControlPanel extends JPanel {
    
    private SimMainPanel main;
    private JButton pause;
    private JButton reset;
    
    
    public ControlPanel(SimMainPanel main) {
        this.main = main;
        
    }
    
}
