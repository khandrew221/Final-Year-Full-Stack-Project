/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import controls.SimControl;
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

    SimStateFacade facade;
    SimControl control;
    
    SimMainPanel viewer;
    FieldMaker fieldMaker;
    SenseMaker senseMaker;
    BehaviourMaker behaviourMaker;

    
    public MainView(Simulation s) {
        facade = new SimStateFacade(s);
        control = new SimControl(s);
        JTabbedPane tabbedPane = new JTabbedPane();
        
        viewer = new SimMainPanel(control, facade, 500, 500);
        fieldMaker = new FieldMaker(control, facade);
        senseMaker = new SenseMaker(control, facade);
        behaviourMaker = new BehaviourMaker(control, facade);
                
        tabbedPane.addTab("Main", null, viewer, "");
        tabbedPane.addTab("Environment", null, fieldMaker, "Add/Remove environment fields");           
        tabbedPane.addTab("Senses", null, senseMaker, "Add/Remove senses");
        tabbedPane.addTab("Behaviours", null, behaviourMaker, "Add/Remove behaviours");        
    
        tabbedPane.addChangeListener( new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                JTabbedPane source = (JTabbedPane) changeEvent.getSource();
                int index = source.getSelectedIndex();
                switch (index) {
                    case 0:
                        viewer.setAll();
                    case 1:
                        fieldMaker.updateAll();
                    case 2:
                        senseMaker.updateAll();
                    case 3:
                        behaviourMaker.updateAll();
                }               
            }
        });
        
        this.add(tabbedPane);
        
    }
    
    /**
     * This invokes a frame by frame update for the visualisation, and does NOT
     * update for simulation component changes.
     */
    public void update() {
        viewer.updateData();
    }
    
}
