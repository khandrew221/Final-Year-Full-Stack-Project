/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import java.awt.BorderLayout;
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
public class GraphTest {
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
        
        
        Graph comp = new Graph("Average Fitness", facade, 600, 400);
        comp.addSeries("Fitness");
        comp.addSeries("Population");
        comp.setYAxisLabel("Fitness");
        comp.updateData();

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
        }, 0, 15, TimeUnit.MILLISECONDS); 
         
    }
    
    
    private static void runDisplay(Simulation s, Graph comp, SimStateFacade facade) {
        s.run();
        comp.updateData();
    }    
}
