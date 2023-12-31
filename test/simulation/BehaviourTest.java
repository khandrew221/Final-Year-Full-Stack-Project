/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import java.awt.Color;
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
        testEat(true);
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
        GRep g = new GRep(0, 10, 5, 1, 1);
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
        GRep g = new GRep(0, 10, 5, 1, 2);
        g.randomise();
        Point botPos = new Point(10,10);
        Bot bot = new Bot(g, senses, behaviours, 20, botPos);
        
        //run to get outputs
        bot.run();
                    
        
        //location to compare against.  run before b.execute() for correct values!
        
        double xMove = (bot.getOutput(0)-0.5)*2*maxSpeed; 
        double yMove = (bot.getOutput(1)-0.5)*2*maxSpeed; 
        
        Point displace = new Point(xMove, yMove);
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


    public static int testEat(boolean v) {        
        int fails = 0;        
        
        Environment e = new Environment(100,100);
        e.addField("Test1", 11, 0, 100, 0, Color.GREEN);
        e.randomiseField("Test1", 0, 100);
        Sense s = SenseFactory.MakeEnvironmentSense("Test1", e, true, 3, 4, 7);
        s.renumberOutputs(0);
        
        double forageEffc = 0.1;
        double energyEffc = 1;
        
        Behaviour b = new BehaviourEat(forageEffc, energyEffc, "Test1", e);
        b.renumberInputs(0);
        
        //no need to add a sense or the behaviour. 
        //Only required for a bot to pass to the sense.  
        Set<Sense> senses = new HashSet<>();
        Set<Behaviour> behaviours = new HashSet<>();
        GRep g = new GRep(0, 10, 5, 1, 2);
        g.maximal();
        Point botPos = new Point(10,10);
        Bot bot = new Bot(g, senses, behaviours, 20, botPos);
        
        //run to get outputs. maximal bot always eats
        bot.run();
                    
        double envOldValue = e.trueValueAt("Test1", botPos);    
        double prevEnergy = bot.getEnergy();
        double envExpectValue = envOldValue - (envOldValue*forageEffc);
        b.execute(bot);
        double envNewValue = e.trueValueAt("Test1", botPos);
        double newEnergy = bot.getEnergy();
        double expectEnergy = prevEnergy + envOldValue*forageEffc*energyEffc;

        if (envOldValue == envNewValue) {
            fails++;
            if (v)
               System.out.println("Error in eat behaviour: no environment change found.");
        }        
        if (!approxEquals(envExpectValue, envNewValue, 0.0000001)) {
            fails++;
            if (v)
               System.out.println("Error in eat behaviour amount taken from environment: " + envExpectValue + " expected, " + envNewValue + " found.");
        }
        if (envNewValue < 0) {
            fails++;
            if (v)
               System.out.println("Error in eat behaviour: value at location after eating less than minimum.");
        }  
        if (envNewValue > 100) {
            fails++;
            if (v)
               System.out.println("Error in eat behaviour: value at location after eating more than maximum.");
        }        
        if (prevEnergy == newEnergy) {
            fails++;
            if (v)
               System.out.println("Error in eat behaviour: no energy change found.");
        }
        if (!approxEquals(newEnergy, expectEnergy, 0.0000001)) {
            fails++;
            if (v)
               System.out.println("Error in eat behaviour bot energy value: " + expectEnergy + " expected, " + newEnergy + " found.");
        }        
        return fails;
    }        
    
    
    public static boolean approxEquals(double v1, double v2, double tol) {
        if (Math.abs(v1 - v2) > tol) {
            return false;
        }
        return true;
    }    
}
