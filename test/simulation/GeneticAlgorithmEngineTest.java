/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import controls.SimConsts;
import java.util.SortedSet;

/**
 *
 * @author Kathryn Andrew
 */
public class GeneticAlgorithmEngineTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        int maxLayers = SimConsts.getMAX_LAYERS();
        int maxNodesPerLayer = SimConsts.getMAX_NODES_PER_LAYER();
        
        GeneticAlgorithmEngine GAEng = new GeneticAlgorithmEngine(maxLayers, maxNodesPerLayer);
        
        testBreedGRep(true, GAEng);
        
        System.out.println("Generation numbering failures: " + testGeneration(true, GAEng));
    }
    

    public static int testBreedGRep(boolean v, GeneticAlgorithmEngine GAEng) {
        int fails = 0;        
        Simulation s = new Simulation(1000,1000,50);
        s.initialise();
        for (int i = 0; i < 400; i++) {
            s.run();
        }
        SortedSet<Bot> bots = s.getBots();
        double populationFitness = 0;
        double minimumFitness = Math.abs(bots.last().getFitness());
        for (Bot bot : bots) {
            populationFitness += bot.getFitness() + minimumFitness;          
        }      
        GRep parent1 = GAEng.getParent(bots, populationFitness, minimumFitness);
        GRep parent2 = GAEng.getParent(bots, populationFitness, minimumFitness);
        GRep parent3 = GAEng.getParent(bots, populationFitness, minimumFitness);
        GRep parent4 = GAEng.getParent(bots, populationFitness, minimumFitness);        
        
        if (parent1.equals(parent2) && 
            parent1.equals(parent3) &&    
            parent1.equals(parent4)) {
            fails++;
            if (v)
                System.out.println("All parents identical.  Possible selection error.");
        }
        
        GRep child12 = GAEng.breedGRep(parent1, parent2);
        
        fails += testUniformCrossover(v, parent1, parent2, child12);
        fails += testPointMutate(v, parent1, GAEng); 
        
        return fails;
    }
    
    public static int testUniformCrossover(boolean v, GRep parent1, GRep parent2, GRep child) {
        int fails = 0;       
        for (int i = 0; i < parent1.requiredLength(); i++) {
            boolean a = parent1.bitAt(i);
            boolean b = parent2.bitAt(i);
            if (a == b) {
                boolean c = child.bitAt(i);
                if (a != c) {
                    fails++;
                    if (v)
                        System.out.println("Impossible child genome: bit at " + i + " different to both parents.  Parents " + a + b + ", child " + c);
                }
            }
        }
        return fails;
    }
    
    public static int testPointMutate(boolean v, GRep parent1, GeneticAlgorithmEngine GAEng) {
        int fails = 0;       
        GRep mutated = GAEng.pointMutate(parent1);
        if (mutated.equals(parent1)) {
            fails++;
            if (v)
                System.out.println("No mutation, possible muation error.");            
        }
        return fails;
    }  
    
    public static int testGeneration(boolean v, GeneticAlgorithmEngine GAEng) {
        int fails = 0;       
        GRep parent1 = GAEng.randomGRep(3, 3);
        GRep parent2 = GAEng.randomGRep(3, 3);
        GRep child = GAEng.breedGRep(parent1, parent2);
        if (child.getGeneration() != 1) {
            fails++;
            if (v)
                System.out.println("Child of two 0 gen perents not generation 1.  Generation found: " + child.getGeneration());            
        }
        GRep child2 = GAEng.breedGRep(child, parent2);
        if (child2.getGeneration() != 2) {
            fails++;
            if (v)
                System.out.println("Child of gen 1&0 not generation 2.  Generation found: " + child2.getGeneration());            
        }  
        child2 = GAEng.breedGRep(parent2, child);
        if (child2.getGeneration() != 2) {
            fails++;
            if (v)
                System.out.println("Child of gen 0&1 not generation 2.  Generation found: " + child2.getGeneration());            
        }           

        parent1.setGeneration(20);
        parent2.setGeneration(1);
        child = GAEng.breedGRep(parent1, parent2);   
        if (child.getGeneration() != 21) {
            fails++;
            if (v)
                System.out.println("Child of gen 20&1 not generation 21.  Generation found: " + child.getGeneration());            
        }        
        child = GAEng.breedGRep(parent2, parent1);   
        if (child.getGeneration() != 21) {
            fails++;
            if (v)
                System.out.println("Child of gen 1&20 not generation 21.  Generation found: " + child.getGeneration());            
        }             
        
        return fails;
    }     
}
