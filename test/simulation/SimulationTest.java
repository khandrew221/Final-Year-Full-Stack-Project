/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

        Simulation s = new Simulation(1000,1000,50);
        
        testAddSense(s, true);
        testAddBehaviour(s, true);        
        testAddRandomBot(s, true);
        
        testClear(s, true);
       
        testInitialise(s, true);
        
        System.out.println("Run failures: " + testRun(s, true));   
        System.out.println("Bot report failures: " + testBotReport(s, true));
    }
    
    public static int testClear(Simulation s, boolean v) {
        int fails = 0;
        s.clearSimulation();  
        if (s.time() != 0) {
            fails++;
            if (v)
                System.out.println("Simulation time incorrect. 0 expected, " + s.time() + " found.");
        }
        if (s.population() != 0) {
            fails++;
            if (v)
                System.out.println("Bot collection not empty.");
        } 
        if (!s.listFields().isEmpty()) {
            fails++;
            if (v)
                System.out.println("Environment not empty.");
        }
        if (!s.getBehaviours().isEmpty()) {
            fails++;
            if (v)
                System.out.println("Behaviours not empty.");
        }    
        if (!s.getSenses().isEmpty()) {
            fails++;
            if (v)
                System.out.println("Senses not empty.");
        } 
        if (s.getNnInputs() != 0) {
            fails++;
            if (v)
                System.out.println("Number of inputs incorrect. 0 expected, " + s.getNnInputs() + " found.");
        }  
         if (s.getNnOutputs() != 0) {
            fails++;
            if (v)
                System.out.println("Number of outputs incorrect. 0 expected, " + s.getNnOutputs() + " found.");
        }            
        
        if (v)
            System.out.println("Clear test failures: " + fails);        
        
        return fails;
    }    
    
    
    public static int testInitialise(Simulation s, boolean v) {
        int fails = 0;
        int startPop = 100;
        Set<String> fields = new HashSet<>();
        fields.add("Test1");
        s.initialise();          
        if (s.time() != 0) {
            fails++;
            if (v)
                System.out.println("Simulation time incorrect. 0 expected, " + s.time() + " found.");
        }        
        if (s.population() != startPop) {
            fails++;
            if (v)
                System.out.println("Population size incorrect."  + startPop + " expected, " + s.population() + " found.");
        } 
        if (!s.listFields().equals(fields)) {
            fails++;
            if (v)
                System.out.println("Environment fields list not correct." + fields + " expected, " + s.listFields() + " found.");
        }
        if (s.getBehaviours().size() != 1) {
            fails++;
            if (v)
                System.out.println("Number of behaviours incorrect. 1 expected, " + s.getBehaviours().size() + " found.");
        }    
         if (s.getSenses().size() != 1) {
            fails++;
            if (v)
                System.out.println("Number of senses incorrect. 1 expected, " + s.getSenses().size() + " found.");
        }  
         if (s.getNnInputs() != 1) {
            fails++;
            if (v)
                System.out.println("Number of inputs incorrect. 1 expected, " + s.getNnInputs() + " found.");
        }  
         if (s.getNnOutputs() != 2) {
            fails++;
            if (v)
                System.out.println("Number of outputs incorrect. 2 expected, " + s.getNnOutputs() + " found.");
        }
        if (s.getMaxPop() != 100) {
            fails++;
            if (v)
                System.out.println("Max population incorrect. 100 expected, " + s.getMaxPop() + " found.");
        }          
        
        if (v)
            System.out.println("Initialise failures: " + fails);        
        
        return fails;
    }     
    
    public static int testAddRandomBot(Simulation s, boolean v) {
        int fails = 0;
        for (int i = 0; i < 50; i++) {
            int pop = s.population();
            s.addStarterBot(10, 12, 23);            
            if (s.population() != (pop+1)) {
                fails++;
                if (v)
                    System.out.println("Bot collection not one larger.  Size: " + s.population());
            }               
        }
        int pop = s.population();
        s.addStarterBot(10, 12, 23);            
        if (s.population() != pop) {
            fails++;
            if (v)
               System.out.println("Bot added over population limit.");
        }        
        if (v)
            System.out.println("Add random bot failures: " + fails);                
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
            System.out.println("Add sense failures: " + fails);        
        
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
            System.out.println("Add behaviour failures: " + fails);        
        
        return fails;
    }    
    
    public static int testRun(Simulation s, boolean v) { 
        int fails = 0;
        
        s.initialise();
        
        long oldTime = s.time();
        
        s.run();
        
        
        int energyFails = 0;
        for (boolean x : s.listIsDead()) {
            if (x) {
                energyFails++;   
            }                
        }        
        if (s.time() != oldTime+1) {
            fails++;
            if (v)
                System.out.println("Simulation time incorrect. " + (oldTime+1) + " expected, " + s.time() + " found.");
        }
        if (v && (energyFails > 0))
            System.out.println(energyFails + " dead bots still present.");        
        fails += energyFails;
 
        if (s.population() > s.getMaxPop()) {
            fails++;
            System.out.println("Population overflow. " + s.getMaxPop() + " maximum, " + s.population() + " found."); 
        }
            
        
        return fails;        
    }
    
    public static int testBotReport(Simulation s, boolean v) {     
        int fails = 0;
        
        List<Map<String, Object>> r = s.botReport();

        if (s.population() != r.size()) {
            fails++;
            System.out.println("List size not population size. " + s.getMaxPop() + " population, " + r.size() + " list size."); 
        }        
        
        for (Map<String, Object> m : r) {
            if (!m.containsKey("PosX")) {
                fails++;
                if (v)
                    System.out.println("Key PosX not present."); 
            } else {
                try {
                    double test = (double) m.get("PosX");
                } catch (Exception e) {
                    fails++;
                    e.printStackTrace();
                }
            }
            if (!m.containsKey("PosY")) {
                fails++;
                if (v)
                    System.out.println("Key PosY not present."); 
            }  else {
                try {
                    double test = (double) m.get("PosY");
                } catch (Exception e) {
                    fails++;
                    e.printStackTrace();
                }
            }
            if (!m.containsKey("ID")) {
                fails++;
                if (v)
                    System.out.println("Key ID not present."); 
            } else {
                try {
                    long test = (long) m.get("ID");
                } catch (Exception e) {
                    fails++;
                    e.printStackTrace();
                }
            }         
        }
        
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
