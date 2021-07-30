/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import controls.SimConsts;
import java.awt.Color;
import java.util.HashSet;
import java.util.Set;
import utility.Point;

/**
 *
 * @author Kathryn Andrew
 */
public class FitnessFunctionTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        fitnessFunctionTest(true);
    }
    
    
    public static int fitnessFunctionTest(boolean v) {
        int fails = 0;
        int maxMove = 2;
        Set<Sense> senses = new HashSet<>();
        Set<Behaviour> behaviours = new HashSet<>();
        behaviours.add(new BehaviourMove(maxMove, new Point(0,0), new Point(100,100)));
        Point testPos = new Point (maxMove,maxMove);
        Point newPos = new Point (0,0);
        Environment env = new Environment(100,100);
        env.addField("TEST1", 11, 0, 100, 0, Color.yellow);
        int testOutputs = 1;
        int testInputs = 1;
        double testEnergy = 0;
        GRep g = new GRep(0, 10, 5, 1, 1);
        Bot b = new Bot(g, senses, behaviours, testEnergy, testPos);
        FitnessFunction f = new FitnessFunction();
        f.setWeightAmountEaten(2);
        f.setWeightCollisionsPerCycle(-3);
        f.setWeightCurrentEnergy(5);
        f.setWeightDistanceTravelled(-10);
        
        f.calcFitness(b);
        
        if (0 != b.getFitness()) {
            fails++;
            if (v)
                System.out.println("Failure: bot fitness not set correctly. 0 expected, " + b.getFitness() + " found.");
        } 
        
        
        b = new Bot(g, senses, behaviours, SimConsts.getMAX_ENERGY()*0.5, testPos);
        f.calcFitness(b);
        
        if (2.5 != b.getFitness()) {
            fails++;
            if (v)
                System.out.println("Failure: bot current energy fitness not set correctly. 2.5 expected, " + b.getFitness() + " found.");
        } 
        
        b = new Bot(g, senses, behaviours, testEnergy, testPos);
        b.setPosition(newPos);
        b.incrementAge();
        f.calcFitness(b);
        
        if (-10 != b.getFitness()) {
            fails++;
            if (v)
                System.out.println("Failure: bot distance travelled fitness not set correctly. -10 expected, " + b.getFitness() + " found.");
        }  
         
        
        return fails;
    }
}
