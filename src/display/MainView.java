/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import controls.SimControl;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import simulation.SimStateFacade;
import simulation.Simulation;

/**
 *
 * @author Kathryn Andrew
 */
public class MainView extends JFrame {
    
    Simulation sim;
    SimStateFacade facade;
    SimControl control;
    
    SimMainPanel viewer;
    FieldMaker fieldMaker;
    SenseMaker senseMaker;
    BehaviourMaker behaviourMaker;

    
    public MainView(Simulation s) {
        sim = s;
        facade = new SimStateFacade(sim);
        control = new SimControl(sim);
        JTabbedPane tabbedPane = new JTabbedPane();
        
        viewer = new SimMainPanel(sim, facade, 500, 500);
        fieldMaker = new FieldMaker(control, facade);
        senseMaker = new SenseMaker(control, facade);
        behaviourMaker = new BehaviourMaker(control, facade);
                
        tabbedPane.addTab("Tab 1", null, viewer,
                  "Does nothing");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        
        tabbedPane.addTab("Tab 2", null, fieldMaker,
                  "Does nothing");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);      
        
        tabbedPane.addTab("Tab 3", null, senseMaker,
                  "Does nothing");
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);   

        tabbedPane.addTab("Tab 4", null, behaviourMaker,
                  "Does nothing");
        tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);            
        
        this.add(tabbedPane);
     

        tabbedPane.addChangeListener( new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
                int index = sourceTabbedPane.getSelectedIndex();
                if (index == 0) {
                    viewer.setAll();
                } else if (index == 1) {
                    fieldMaker.update();
                }
                else if (index == 2) {
                    System.out.println("2 selected");
                    senseMaker = new SenseMaker(control, facade);
                }
            }
        });
        
    }
    
    /**
     * This invokes a frame by frame update for the visualisation, and does NOT
     * update for simulation component changes.
     */
    public void update() {
        viewer.updateData();
        senseMaker.updateAll();
    }
    
}
