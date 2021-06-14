/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controls;

import display.MainView;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import simulation.Simulation;

/**
 *
 * @author Kathryn Andrew
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        int envXsize = 500;
        int envYsize = 500;
        int maxPop = 100;
        
        Simulation s = new Simulation(envXsize, envYsize, maxPop);
        s.initialise();
        
        MainView m = new MainView(s);
        m.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                        
        m.pack();
        m.setVisible(true);
        
        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //long startTime = System.nanoTime();
                try {
                s.run();
                } catch (Exception e) {
                  e.printStackTrace();
                }
                m.update();
                //long endTime = System.nanoTime();
                //System.out.println((endTime - startTime)/1000000);
            }
        }, 0, 15, TimeUnit.MILLISECONDS); 
    }
    
}
