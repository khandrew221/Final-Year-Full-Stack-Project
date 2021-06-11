/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import controls.SimControl;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import simulation.SimStateFacade;

/**
 *
 * @author Kathryn Andrew
 */
public abstract class ComponentMaker extends JPanel implements ActionListener {
    
    private SimControl simControl;
    private SimStateFacade simFacade;
    JButton addComponentButton;
    
    
    public ComponentMaker(SimControl simControl, SimStateFacade simFacade) {
        this.simControl = simControl;
        this.simFacade = simFacade;
    }
    
    
    SimControl getControl() {
        return simControl;
    }
    
    SimStateFacade getFacade() {
        return simFacade;
    }
        
    /**
     * 
     */
    abstract void makeAddComponentButton(); 
    
    /**
     *
     * @param e
     */
    @Override
    public abstract void actionPerformed(ActionEvent e);
    
}
