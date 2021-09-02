/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import controls.SimControl;
import java.awt.BorderLayout;
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
    
    public static final int DATA_TAB = 0;
    public static final int FITNESS_TAB = 1;
    public static final int ENVIRONMENT_TAB = 2;
    public static final int SENSES_TAB = 3;
    public static final int BEHAVIOURS_TAB = 4;    
    

    private SimStateFacade facade;
    private SimControl control;
    
    private RunView viewer;
    private DataPanel data;
    private FieldMaker fieldMaker;
    private SenseMaker senseMaker;
    private BehaviourMaker behaviourMaker;
    private FitnessPanel fitnessPanel;
    
    private JTabbedPane tabbedPane;

    
    public MainView(Simulation s) {
        this.setLayout(new BorderLayout());
        facade = new SimStateFacade(s);
        control = new SimControl(s);
        tabbedPane = new JTabbedPane();
        
        
        viewer = new RunView(control, facade, 500, 500);
        data = new DataPanel(viewer, facade);
        data.setAll();
        fieldMaker = new FieldMaker(control, facade);
        senseMaker = new SenseMaker(control, facade);
        behaviourMaker = new BehaviourMaker(control, facade);
        fitnessPanel = new FitnessPanel (control, facade);
        facade.getFitnessFunctionParameters();
        fitnessPanel.setup("Fitness", facade.getFitnessFunctionParameters());
                
        this.add(viewer, BorderLayout.WEST);
        tabbedPane.addTab("Data", null, data, "View simulation data"); 
        tabbedPane.addTab("Fitness", null, fitnessPanel, "Adjust fitness function");           
        tabbedPane.addTab("Environment", null, fieldMaker, "<html>Add/Remove environment fields<br>Simulation must be stopped to use.</html>");           
        tabbedPane.addTab("Senses", null, senseMaker, "<html>Add/Remove senses<br>Simulation must be stopped to use.</html>");
        tabbedPane.addTab("Behaviours", null, behaviourMaker, "<html>Add/Remove behaviours<br>Simulation must be stopped to use./html>");  
        tabbedPane.addTab("Help", null, new HelpPanel());  
        tabbedPane.addTab("About", null, new AboutPanel());  
    
        tabbedPane.addChangeListener( new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                JTabbedPane source = (JTabbedPane) changeEvent.getSource();
                int index = source.getSelectedIndex();
                switch (index) {
                    case DATA_TAB:
                        data.setAll();
                        break;
                    case FITNESS_TAB:
                        break;
                    case ENVIRONMENT_TAB:
                        fieldMaker.updateAll();
                        break;
                    case SENSES_TAB:
                        senseMaker.updateAll();
                        break;
                    case BEHAVIOURS_TAB:
                        behaviourMaker.updateAll();
                        break;                        
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
        data.updateData();
        if (tabbedPane.getSelectedIndex() == FITNESS_TAB)
            fitnessPanel.updateFitnessWeights();
        if (!control.isStopped()) {
            tabbedPane.setEnabledAt(ENVIRONMENT_TAB, false);
            tabbedPane.setEnabledAt(SENSES_TAB, false);
            tabbedPane.setEnabledAt(BEHAVIOURS_TAB, false);
            if (tabbedPane.getSelectedIndex() == ENVIRONMENT_TAB ||
                    tabbedPane.getSelectedIndex() == SENSES_TAB ||
                    tabbedPane.getSelectedIndex() == BEHAVIOURS_TAB)
                tabbedPane.setSelectedIndex(DATA_TAB);
        } else {
            tabbedPane.setEnabledAt(ENVIRONMENT_TAB, true);
            tabbedPane.setEnabledAt(SENSES_TAB, true);
            tabbedPane.setEnabledAt(BEHAVIOURS_TAB, true);            
        }
    }
}
