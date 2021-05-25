/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import java.util.HashSet;
import java.util.Set;
import utility.Point;

/**
 *
 * @author Kathryn Andrew
 */
public class BehaviourTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        testBasicExecution(true);
    }
    
    
    public static int testBasicExecution(boolean v) {        
        int fails = 0;        
        Point testVal = new Point(100,100);
        Behaviour b = new BehaviourDEBUG(testVal);
        
        //no need to add the sense, we're testing it directly. 
        //Only required for a bot to pass to the sense.  
        Set<Sense> senses = new HashSet<>();
        Set<Behaviour> behaviours = new HashSet<>();
        GRep g = new GRep(10, 5, 1, 1);
        Bot bot = new Bot(g, senses, behaviours, 1, new Point(10,10));
        
        b.execute(bot);
        
        if (!testVal.equals(bot.getPosition())) {
            fails++;
            if (v)
                System.out.println("Failure: bot input not set correctly. " + testVal + " expected, " + bot.getPosition() + " found.");
        }
        
        return fails;
    }
}
