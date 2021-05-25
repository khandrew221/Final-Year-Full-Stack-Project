/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import java.util.Set;
import utility.Point;

/**
 *
 * @author Kathryn Andrew
 */
public class SimulationTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Simulation s = new Simulation(1000,1000);
        
        testAddSense(s, true);
        testAddBehaviour(s, true);        
        testAddRandomBot(s, true);
        
    }
    
    public static int testAddRandomBot(Simulation s, boolean v) {
        int fails = 0;
        for (int i = 0; i < 4; i++) {
            int pop = s.population();
            s.addStarterBot(10, 12, 23);            
            if (s.population() != (pop+1)) {
                fails++;
                if (v)
                    System.out.println("Bot collection not one larger.  Size: " + s.population());
            }               
        }
        if (v)
            System.out.println("Failures: " + fails);        
        
        return fails;
    }
    
    public static int testAddSense(Simulation s, boolean v) {
        int fails = 0;
        for (int i = 0; i < 4; i++) {
            int inp = s.getNnInputs();
            Sense sen = SenseFactory.Enviro("Test1", s.getEnv());
            s.addSense(sen);            
            if (!setIsRange(sen.outputSlots(), inp, inp+sen.outputSlots().size())) {
                fails++;
                if (v)
                    System.out.println("Input set is not correct range.");
            }  
            if (!s.getSenses().contains(sen)) {
                fails++;
                if (v)
                    System.out.println("Sense set does not contain new sense.");                
            }  
            if (!(s.getInputSlots().size() == s.getNnInputs())) {
                fails++;
                if (v)
                    System.out.println("Number of inputs is not the same as required number of inputs.");                
            }              
        }

        if (v)
            System.out.println("Failures: " + fails);        
        
        return fails;
    }
    
    public static int testAddBehaviour(Simulation s, boolean v) {
        int fails = 0;
        for (int i = 0; i < 4; i++) {
            int outp = s.getNnOutputs();
            Behaviour beh = new BehaviourMove(10, new Point(0,0), new Point(s.getEnv().getXSize(), s.getEnv().getYSize()));
            s.addBehaviour(beh);            
            if (!setIsRange(beh.inputSlots(), outp, outp+beh.inputSlots().size())) {
                fails++;
                if (v)
                    System.out.println("Output set is not correct range.");
            }  
            if (!s.getBehaviours().contains(beh)) {
                fails++;
                if (v)
                    System.out.println("Behaviour set does not contain new behaviour.");                
            }  
            if (!(s.getOutputSlots().size() == s.getNnOutputs())) {
                fails++;
                if (v)
                    System.out.println("Number of outputs is not the same as required number of outputs.");                
            }              
        }

        if (v)
            System.out.println("Failures: " + fails);        
        
        return fails;
    }    
    
    
    public static boolean setIsRange(Set<Integer> s, int start, int end) {
        if (s.size() != end-start)
            return false;
        for (int i = start; i < end; i++) {
            if (!s.contains(i))
                return false;
        }
        return true;
    }
    
}
