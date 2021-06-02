/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controls;

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
        
        SimConsts consts = new SimConsts();
        SimControl control = new SimControl();
        
        
        Simulation s = new Simulation(100,100,100);
        s.initialise();
        s.run();
    }
    
}
