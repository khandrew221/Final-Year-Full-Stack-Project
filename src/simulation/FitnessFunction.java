/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import controls.SimConsts;

/**
 *
 * @author Kathryn Andrew
 */
public class FitnessFunction {
    
    int weightCollisionsPerCycle = -1;
    int weightCurrentEnergy = 1;
    int weightDistanceTravelled = 1;
    
    
    public void calcFitness(Bot bot) {        
        double total = 0;        
        total += bot.getCollisionsPerCycle() * weightCollisionsPerCycle;
        total += bot.getEnergy() / SimConsts.getMAX_ENERGY() * weightCurrentEnergy;
        total += averageDistanceTravelledPerCycle(bot) * weightDistanceTravelled;        
        bot.setFitness(total);
    }   
    
    /**
     * Returns the bot's average distance travelled per simulation cycle as a
     * proportion of the maximum possible distance per cycle.  If bot age or
     * movement speed are 0, returns 0.
     * @param bot
     * @return 
     */
    public double averageDistanceTravelledPerCycle(Bot bot) {
        if (bot.getAge() > 0 && bot.getMaxMoveSpeed() > 0) {
            return bot.getDistanceTravelled() / (double) bot.getAge() / bot.getMaxMoveSpeed();
        }
        return 0;
    }
    

}