/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Kathryn Andrew
 */
public class Simulation {
    
    Environment environment;
    Set<Bot> bots;
    Set<Sense> senses;
    Set<Behaviour> behaviours;
    
    public Simulation(int envXsize, int envYsize) {
        environment = new Environment(envXsize, envYsize);
        bots = new HashSet<>();
        senses = new HashSet<>();
        behaviours = new HashSet<>();
    }
}
