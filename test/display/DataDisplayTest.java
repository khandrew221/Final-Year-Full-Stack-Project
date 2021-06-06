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
import javax.swing.JLabel;
import simulation.SimStateFacade;
import simulation.Simulation;

/**
 *
 * @author Kathryn Andrew
 */
public class DataDisplayTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFrame testFrame = new JFrame();
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        
        int envXsize = 500;
        int envYsize = 500;
        int maxPop = 100;
        
        Simulation s = new Simulation(envXsize, envYsize, maxPop);
        s.initialise();
        SimStateFacade facade = new SimStateFacade(s);
        
        
        DataPanel comp = new DataPanel();
        comp.setPreferredSize(new Dimension(envXsize,envYsize)); 
        
        testFrame.getContentPane().add(comp, BorderLayout.CENTER);        

        
        comp.updateData(facade.simReport(), facade.fieldsReport());

        
        
        testFrame.pack();
        testFrame.setVisible(true);
        
        
        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //long startTime = System.nanoTime();
                s.run();
                comp.updateData(facade.simReport(), facade.fieldsReport());
                //long endTime = System.nanoTime();
                //System.out.println((endTime - startTime)/1000000);
            }
        }, 0, 15, TimeUnit.MILLISECONDS); 
        
        
    }
    
}
