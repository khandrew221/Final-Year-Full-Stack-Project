/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import controls.SimControl;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import simulation.SimStateFacade;
import simulation.Simulation;

/**
 *
 * @author Kathryn Andrew
 */
public class SenseMakerTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         JFrame testFrame = new JFrame();
        testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        int envXsize = 500;
        int envYsize = 500;
        int maxPop = 100;
        
        Simulation s = new Simulation(envXsize, envYsize, maxPop);
        s.initialise();
        SimStateFacade facade = new SimStateFacade(s);
        SimControl c = new SimControl(s);
          
        SenseMaker comp = new SenseMaker(c, facade);
        testFrame.getContentPane().add(comp, BorderLayout.CENTER); 
        
                
        testFrame.pack();
        testFrame.setVisible(true);
        
        
    }
    
}
