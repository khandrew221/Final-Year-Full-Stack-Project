/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import java.util.BitSet;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;

/**
 * Creates genetic representations.
 * @author Kathryn Andrew
 */
public class GeneticAlgorithmEngine {
    
    private FitnessFunction fitnessFunction = new FitnessFunction();
    
    int MAX_LAYERS;
    int MAX_NODES_PER_LAYER;
    double mutationRate;
    
    /**
     * 
     * @param maxLayers
     * @param maxNodesPerLayer 
     */   
    public GeneticAlgorithmEngine(int maxLayers, int maxNodesPerLayer) {
        MAX_LAYERS = maxLayers;
        MAX_NODES_PER_LAYER = maxNodesPerLayer;
        mutationRate = 2;
    }
    
    
    /**
     * Creates and returns a random genetic representation that defines a network
     * with the given number of inputs and outputs. Maximum layers and nodes
     * per layer are defined by GeneticAlgorithmEngine attributes MAX_LAYERS and
     * MAX_NODES_PER_LAYER.
     * @param numInputs
     * @param numOutputs
     * @return 
     */
    public GRep randomGRep(int numInputs, int numOutputs) {
        GRep g = new GRep(MAX_LAYERS, MAX_NODES_PER_LAYER, numInputs, numOutputs);
        g.randomise();
        return g;
    };
    
    /**
     * Breeds a new GRep from two parents, using uniform crossover.
     * @param numInputs
     * @param numOutputs
     * @return 
     */
    public GRep breedGRep(GRep parent1, GRep parent2) {        
        //both parents should have same number of inputs and outputs
        GRep g = new GRep(MAX_LAYERS, MAX_NODES_PER_LAYER, parent1.getNUM_INPUTS(), parent1.getNUM_OUTPUTS());
        
       /* BitSet gridChromosome1 = parent1.bitSetAt(0, parent1.gridLength());
        BitSet gridChromosome2 = parent2.bitSetAt(0, parent2.gridLength());
        
        System.out.println(parent1.gridLength());
        System.out.println(parent2.gridLength());
        
        System.out.println(gridChromosome1);
        System.out.println(gridChromosome2);
        
        System.out.println(uniformCrossover(parent1.gridLength(), gridChromosome1, gridChromosome2));*/
        
        BitSet gridChromosome1 = parent1.bitSetAt(0, parent1.requiredLength());
        BitSet gridChromosome2 = parent2.bitSetAt(0, parent2.requiredLength());      
        
        g.setBitsAt(0, g.requiredLength(), uniformCrossover(g.requiredLength(), gridChromosome1, gridChromosome2));
        return g;
    };    
    
    
    /**
     * Breeds a new GRep from two parents selected by roulette wheel selection, 
     * using uniform crossover.
     * 
     * Bots must not be empty!
     * 
     * @param bots
     * @return 
     */
    public GRep breedGRep(SortedSet<Bot> bots, boolean mutateOffspring) {

        //get the total population fitness
        //fitness must be modified into positive range
        double populationFitness = 0;
        double minimumFitness = Math.abs(bots.last().getFitness());
        for (Bot bot : bots) {
            populationFitness += bot.getFitness() + minimumFitness;          
        }

        //get two parent GReps
        GRep parent1 = getParent(bots, populationFitness, minimumFitness);
        GRep parent2 = getParent(bots, populationFitness, minimumFitness);
        
        if (mutateOffspring) {
            return pointMutate(breedGRep(parent1, parent2));
        } else {        
            return breedGRep(parent1, parent2);
        }
    };    
    
    /**
     * Performs uniform crossover on two bitsets to create a new bitset
     * of the given length.  
     * @param length
     * @param parent1
     * @param parent2
     * @return new BitSet from uniform crossover of parent BitSets
     */
    public BitSet uniformCrossover(int length, BitSet parent1, BitSet parent2) {
        Random random = new Random();
        BitSet out = new BitSet();
        for (int i = 0; i < length; i++) {
            if (random.nextBoolean()) {
                out.set(i, parent1.get(i));
            } else {
                out.set(i, parent2.get(i));
            } 
        } 
        return out;
    }    
    
    /**
     * Roulette wheel selection of one parent genome from the population
     * 
     * @param bots
     * @param populationFitness
     * @param minimumFitness
     * @return 
     */
    public GRep getParent(SortedSet<Bot> bots, double populationFitness, double minimumFitness) {
        double position = Math.random() * populationFitness;
        double spinWheel = 0;
        for (Bot bot : bots) {
            spinWheel += bot.getFitness() + minimumFitness;
            if (spinWheel >= position) {
                return bot.getGRep();
            }        
        }
        return bots.first().getGRep();
    }  

    /**
     * Returns a mutation version of the GRep based on the mutation rate.
     * @param g
     * @return 
     */
    public GRep pointMutate(GRep g) {
        //mutation rate = probability of single gene in genome changing
        double rate = mutationRate*(1.0/g.requiredLength());
        GRep out = g.clone();
        for (int i = 0; i < g.requiredLength(); i++) {
            if (Math.random() < rate) {
                g.flipBitAt(i);
            }
        }
        return out;
    }
    
    /**
     * Calculates and sets the fitness of the given bot.
     * 
     * Req. for UC031
     * @param bot 
     */
    public void calcFitness(Bot bot) {
         fitnessFunction.calcFitness(bot);
    }
    
    
    /**
     * Sets the weight of a parameter.
     * 
     * 
     * Req. for: UC033
     */
    public void setFitnessWeight(int weight, FitnessParameter parameter) {
        switch(parameter) {
        case COLLISIONS_PER_CYCLE:
          fitnessFunction.setWeightCollisionsPerCycle(weight);
          break;
        case CURRENT_ENERGY:
          fitnessFunction.setWeightCurrentEnergy(weight);
          break;
        case DISTANCE_TRAVELLED:
          fitnessFunction.setWeightDistanceTravelled(weight);
          break;
      }      
    }    
    
    
    /**
     * Req. for: UC033
     * 
     */
    public int getFitnessWeight(FitnessParameter parameter) {
        switch(parameter) {
        case COLLISIONS_PER_CYCLE:
          return fitnessFunction.getWeightCollisionsPerCycle();
        case CURRENT_ENERGY:
          return fitnessFunction.getWeightCurrentEnergy();
        case DISTANCE_TRAVELLED:
          return fitnessFunction.getWeightDistanceTravelled();
      }
      return 0;
    }
    
}
