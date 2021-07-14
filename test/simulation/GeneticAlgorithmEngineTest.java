/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import java.util.BitSet;
import java.util.Random;

/**
 *
 * @author Kathryn Andrew
 */
public class GeneticAlgorithmEngineTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        int inputs = 1;
        int outputs = 1;
        int maxLayers = 3;
        int maxNodesPerLayer = 3;
        
        GeneticAlgorithmEngine GAEng = new GeneticAlgorithmEngine(maxLayers, maxNodesPerLayer);
        
        GRep p1 = GAEng.randomGRep(inputs, outputs);
        p1.maximal();
        GRep p2 = GAEng.randomGRep(inputs, outputs);
        p2 = new GRep(3, 3, 1, 1);
        
        System.out.println(p1.toBinaryString(false));
        System.out.println(p2.toBinaryString(false));
        
        

        GAEng.breedGRep(p1, p2);
    }
    

    
}
