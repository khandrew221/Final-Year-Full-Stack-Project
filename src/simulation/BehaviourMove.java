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
public class BehaviourMove extends Behaviour {
  
    
    private double maxSpeed;
    private Point min;
    private Point max;
    
    int xMoveSlot;
    int yMoveSlot;
    
    
    /**
     * Creates a move behaviour.
     * 
     * @param maxSpd
     * @param min
     * @param max 
     */
    public BehaviourMove(double maxSpd, Point min, Point max) {
        maxSpeed = maxSpd;
        this.max = max;
        this.min = min;
    }
    
    /**
     * Returns the max x or y speed. Note that actual max speed is maxSpeed*root(2)
     * @return 
     */
    public double getMaxSpeed() {
        return maxSpeed;
    }
    
    
    
    @Override
    public void execute(Bot bot) {
        
       // System.out.println(bot.getOutputs().length);
      //  System.out.println(xMoveSlot);
      //  System.out.println(yMoveSlot);
        
     //   System.out.println("old: " + bot.getPosition());
        
        double xMove = (bot.getOutput(xMoveSlot)-0.5)*2*maxSpeed; 
        double yMove = (bot.getOutput(yMoveSlot)-0.5)*2*maxSpeed; 
        
        
       
       
       /*if (Double.isNaN(bot.getOutput(xMoveSlot))) {
           System.out.println("xmoveSlot " + xMoveSlot + ": "  + bot.getOutput(xMoveSlot));
           System.out.println("ymoveSlot " + yMoveSlot + ": "  + bot.getOutput(yMoveSlot));
           for (int i = 0; i < bot.getOutputs().length; i++) {
               System.out.println("output " + i + ": "  + bot.getOutputs()[i]);
           }
       }*/
        
        Point displace = new Point(xMove, yMove);
      //  System.out.println("displace: " + displace);
        Point newLoc = Point.displace(bot.getPosition(), displace);
        if (newLoc.inBounds(min, max)) {
            bot.setPosition(newLoc);
        //    System.out.println("new no coll: " + bot.getPosition());
        } else {
            double newX = newLoc.getX();
            double newY = newLoc.getY();            
            if (newLoc.getX() > max.getX())
                newX = max.getX();
            if (newLoc.getX() < min.getX())
                newX = min.getX();
            if (newLoc.getY() > max.getY())
                newY = max.getY();
            if (newLoc.getY() < min.getY())
                newY = min.getY();  
            bot.setPosition(new Point(newX, newY));
            bot.incrementCollisions();
           // System.out.println("col new x: " + newX);
           // System.out.println("col new y: " + newY);
          //  System.out.println("new with coll: " + bot.getPosition());
        }
        
        //System.out.println(newLoc);
        
        //System.out.println(bot.getPosition());
    }
    

    
    /**
     * Returns the set of behaviour input slots.
     * 
     * Req for: TESTING
     * 
     * @return 
     */
    @Override
    public Set<Integer> inputSlots() {
        Set<Integer> out = new HashSet<>();
        out.add(xMoveSlot);
        out.add(yMoveSlot);
        return out;
    }
    
    
    /**
     * Renumbers the input slots.  This must be called after adding the behaviour,
     * and called for all behaviours when a behaviour is removed.
     * 
     * Req for: UC004
     * 
     */    
    @Override
    public void renumberInputs(int startSlot) {
        xMoveSlot = startSlot;
        yMoveSlot = startSlot+1;        
    }   

    
    @Override
    public String toString() {
        return "Type: move, " + 
                "Max speed: " +  maxSpeed +
                ", Rectangle: " + min + max;
    }
    
    
    /* OLD RADIAL MOVE CODE
        @Override
    public void execute(Bot bot) {
        
        double dir = bot.getOutput(moveDirSlot) * 2*Math.PI; 
        double speed = bot.getOutput(moveSpeedSlot) * maxSpeed;
        
        Point displace = new Point(speed * Math.cos(dir), speed * Math.sin(dir));
        Point newLoc = Point.displace(bot.getPosition(), displace);
        if (newLoc.inBounds(min, max))
            bot.setPosition(newLoc);
        else {
            double newX = newLoc.getX();
            double newY = newLoc.getY();            
            if (newLoc.getX() > max.getX())
                newX = max.getX();
            if (newLoc.getX() < min.getX())
                newX = min.getX();
            if (newLoc.getY() > max.getY())
                newY = max.getY();
            if (newLoc.getY() < min.getY())
                newY = min.getY();  
            bot.setPosition(new Point(newX, newY));
        }
    }
    */
}
