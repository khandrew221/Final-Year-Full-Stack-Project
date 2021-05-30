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
        testMove(true);
    }
    
    
    public static int testBasicExecution(boolean v) {        
        int fails = 0;        
        Point testVal = new Point(100,100);
        Behaviour b = new BehaviourDEBUG(testVal);
        b.renumberInputs(0);
        
        //no need to add a sense or the behaviour. 
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
    
    public static int testMove(boolean v) {        
        int fails = 0;        
        Point testVal = new Point(100,100);
        double maxSpeed = 10;
        Behaviour b = new BehaviourMove(maxSpeed,new Point(0,0),testVal);
        b.renumberInputs(0);
        
        //no need to add a sense or the behaviour. 
        //Only required for a bot to pass to the sense.  
        Set<Sense> senses = new HashSet<>();
        Set<Behaviour> behaviours = new HashSet<>();
        GRep g = new GRep(10, 5, 1, 2);
        g.randomise();
        Point botPos = new Point(10,10);
        Bot bot = new Bot(g, senses, behaviours, 20, botPos);
        
        //run to get outputs
        bot.run();
                    
        
        //location to compare against.  run before b.execute() for correct values!
        double dir = bot.getOutput(0) * 2*Math.PI; 
        double speed = bot.getOutput(1) * maxSpeed;
        Point displace = new Point(speed * Math.cos(dir), speed * Math.sin(dir));
        Point newLoc = Point.displace(bot.getPosition(), displace);
        
        b.execute(bot);
        
        if (newLoc.inBounds(new Point(0,0), testVal)) {
            //new location good, bot should move to new location
            if (bot.getPosition().equals(botPos) && bot.getOutput(0) != 0) {
                fails++;
                if (v)
                    System.out.println("Bot not moved.");
            }
            if (!bot.getPosition().inBounds(new Point(0,0), testVal)) {
                fails++;
                if (v)
                    System.out.println("Bot moved out of bounds.");
            } 
            if (!bot.getPosition().equals(newLoc)) {
                fails++;
                if (v)
                    System.out.println("Bot not moved to correct location. " + newLoc + " expected, " + bot.getPosition() + " found.");
            }            
        } else {            
            //new location bad, modified one required
            double newX = newLoc.getX();
            double newY = newLoc.getX();            
            if (newLoc.getX() > testVal.getX())
                newX = testVal.getX();
            if (newLoc.getX() < 0)
                newX = 0;
            if (newLoc.getY() > testVal.getY())
                newY = testVal.getY();
            if (newLoc.getY() < 0)
                newY = 0;  
            newLoc = new Point(newX, newY);
            if (!bot.getPosition().inBounds(new Point(0,0), testVal)) {
                fails++;
                if (v)
                    System.out.println("Bot moved out of bounds: " + bot.getPosition());
            } 
           if (!bot.getPosition().equals(newLoc)) {
                fails++;
                if (v)
                    System.out.println("Bot not moved to correct location. " + newLoc + " expected, " + bot.getPosition() + " found.");
            }            
        }       
        return fails;
    }    
}
