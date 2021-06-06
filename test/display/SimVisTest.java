/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import simulation.SimStateFacade;
import simulation.Simulation;

/**
 *
 * @author Kathryn Andrew
 */
public class SimVisTest {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
                // TODO code application logic here
        
        JFrame testFrame = new JFrame();
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        
        int envXsize = 500;
        int envYsize = 500;
        int maxPop = 100;
        
        Simulation s = new Simulation(envXsize, envYsize, maxPop);
        s.initialise();
        SimStateFacade facade = new SimStateFacade(s);
        
        
        SimVis comp = new SimVis(facade, envXsize, envYsize);
        comp.buildEnviroImage();
        
        testFrame.getContentPane().add(comp, BorderLayout.CENTER);        
        testFrame.pack();
        testFrame.setVisible(true);
           
        //note: simulation run and display data update MUST be in the same thread to avoid concurrent modification exceptions!!! 
        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                runDisplay(s, comp, facade);
            }
        }, 0, 1, TimeUnit.MILLISECONDS); 
         
    }
    
    
    private static void runDisplay(Simulation s, SimVis comp, SimStateFacade facade) {
        s.run();
        comp.updateData();
    }
    
}
