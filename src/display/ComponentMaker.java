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
public abstract class ComponentMaker extends JPanel implements ActionListener, Updatable  {
    
    private SimControl simControl;
    private SimStateFacade simFacade;
    JButton addComponentButton;
    JButton removeComponentButton;    
    
    
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
     */
    abstract void makeRemoveComponentButton();     
    
    /**
     *
     * @param e
     */
    @Override
    public abstract void actionPerformed(ActionEvent e);
    
    
    /**
     * Updates the panel based on internal changes (adding/removing a 
     * component controlled by this maker).
     */
    public abstract void update();
    
    /**
     * Updates for re-showing, including all changes to simulation parameters.
     * Call on showing this panel.
     */
    public abstract void updateAll();    
    
}
