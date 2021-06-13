/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import controls.SimState;
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
        
        testAddRemoveSense(s, true);
        testAddRemoveBehaviour(s, true);        
        testAddRandomBot(s, true);
        
        testClear(s, true);
       
        testInitialise(s, true);
       
        
        System.out.println("Run failures: " + testRun(s, true));   
        
        System.out.println("Bot report failures: " + testBotReport(s, true));
        
        System.out.println("Fields report failures: " + testFieldsReport(s, true));
        System.out.println("Simulation report failures: " + testSimulationReport(s, true));

        testReset(s, true);
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
        if (s.getState() != SimState.RUNNING) {
            fails++;
            if (v)
                System.out.println("Run state incorrect, " + SimState.RUNNING + "expected, " + s.getState() + " found.");
        }        
        
        if (v)
            System.out.println("Initialise failures: " + fails);        
        
        return fails;
    }     
    
    public static int testReset(Simulation s, boolean v) {
        int fails = 0;
        int startPop = 100;
        Set<String> fields = new HashSet<>();
        fields.add("Test1");
        s.initialise();
        s.run();
        s.restart();
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
            System.out.println("Restart failures: " + fails);        
        
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
    
    public static int testAddRemoveSense(Simulation s, boolean v) {
        int fails = 0;
        for (int i = 0; i < 4; i++) {
            s.setState(SimState.STOPPED);
            int inp = s.getNnInputs();
            Sense sen = SenseFactory.MakeEnvironmentSense("Test1", s.getEnv(), true, 0, 0, 0);
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
            if (s.getState() != SimState.STOPPED_WITH_CRITICAL_CHANGE) {
                fails++;
                if (v)
                    System.out.println("State not set to STOPPED_WITH_CRITICAL_CHANGE after adding sense.");                
            }            
        }
        
        s.setState(SimState.PAUSED);
        Set<Integer> ids = new HashSet<>();
        s.removeSenses(ids);
        if (!(s.getSenses().size() == 4)) {
            fails++;
            if (v)
                System.out.println("Error on removing empty ID set: wrong final sense set size.");                
       }   
       if (!setIsRange(s.getInputSlots(), 0, s.getInputSlots().size())) {
            fails++;
            if (v)
                System.out.println("Error on removing empty ID set: Input set is not correct range.");
        }  
        if (!(s.getInputSlots().size() == s.getNnInputs())) {
            fails++;
            if (v)
                System.out.println("Error on removing empty ID set: Number of inputs is not the same as required number of inputs.");                
        }   
        if (s.getState() != SimState.PAUSED) {
            fails++;
            if (v)
                System.out.println("State change but no senses removed (empty set).");                
        } 
        
        s.setState(SimState.PAUSED);
        ids.add(0);
        ids.add(3);
        s.removeSenses(ids);
        if (!(s.getSenses().size() == 2)) {
            fails++;
            if (v)
                System.out.println("Error on removing ID set: wrong final sense set size.");                
       }  
       if (!setIsRange(s.getInputSlots(), 0, s.getInputSlots().size())) {
            fails++;
            if (v)
                System.out.println("Error on removing ID set: Input set is not correct range.");
        }        
        if (!(s.getInputSlots().size() == s.getNnInputs())) {
            fails++;
            if (v)
                System.out.println("Error on removing ID set: Number of inputs is not the same as required number of inputs.");                
        }    
        if (s.getState() != SimState.STOPPED_WITH_CRITICAL_CHANGE) {
            fails++;
            if (v)
                System.out.println("State not set to STOPPED_WITH_CRITICAL_CHANGE after removing sense.");                
        }         
        
        s.setState(SimState.PAUSED);
        s.removeSenses(ids);
        if (!(s.getSenses().size() == 2)) {
            fails++;
            if (v)
                System.out.println("Error on removing nonexistent ID set: wrong final sense set size.");                
       }   
       if (!setIsRange(s.getInputSlots(), 0, s.getInputSlots().size())) {
            fails++;
            if (v)
                System.out.println("Error on removing nonexistent ID set: Input set is not correct range.");
        }         
        if (!(s.getInputSlots().size() == s.getNnInputs())) {
            fails++;
            if (v)
                System.out.println("Error on removing nonexistent ID set: Number of inputs is not the same as required number of inputs.");                
        }   
        if (s.getState() != SimState.PAUSED) {
            fails++;
            if (v)
                System.out.println("State change but no senses removed (nonexistent senses).");                
        }        
        
        s.setState(SimState.PAUSED);
       ids.add(2);
        s.removeSenses(ids);
        if (!(s.getSenses().size() == 1)) {
            fails++;
            if (v)
                System.out.println("Error on removing mixed existing/nonexisting ID set: wrong final sense set size.");                
       }    
       if (!setIsRange(s.getInputSlots(), 0, s.getInputSlots().size())) {
            fails++;
            if (v)
                System.out.println("Error on removing mixed existing/nonexisting ID set: Input set is not correct range.");
        }           
        if (!(s.getInputSlots().size() == s.getNnInputs())) {
            fails++;
            if (v)
                System.out.println("Error on removing mixed existing/nonexisting ID set: Number of inputs is not the same as required number of inputs.");                
        }        
        if (s.getState() != SimState.STOPPED_WITH_CRITICAL_CHANGE) {
            fails++;
            if (v)
                System.out.println("State not set to STOPPED_WITH_CRITICAL_CHANGE after removing sense.");                
        }               

        if (v)
            System.out.println("Add/remove sense failures: " + fails);        
        
        return fails;
    }
    
    public static int testAddRemoveBehaviour(Simulation s, boolean v) {
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

        s.setState(SimState.PAUSED);
        Set<Integer> ids = new HashSet<>();
        s.removeBehaviours(ids);
        if (!(s.getBehaviours().size() == 4)) {
            fails++;
            if (v)
                System.out.println("Error on removing empty ID set: wrong final behaviour set size.");                
       }   
       if (!setIsRange(s.getOutputSlots(), 0, s.getOutputSlots().size())) {
            fails++;
            if (v)
                System.out.println("Error on removing empty ID set: Output set is not correct range.");
        }  
        if (!(s.getOutputSlots().size() == s.getNnOutputs())) {
            fails++;
            if (v)
                System.out.println("Error on removing empty ID set: Number of inputs is not the same as required number of inputs.");                
        }     
        if (s.getState() != SimState.PAUSED) {
            fails++;
            if (v)
                System.out.println("State change but no behaviours removed (empty set).");                
        } 
        
        s.setState(SimState.PAUSED);
        ids.add(0);
        ids.add(3);
        s.removeBehaviours(ids);
        if (!(s.getBehaviours().size() == 2)) {
            fails++;
            if (v)
                System.out.println("Error on removing ID set: wrong final behaviour set size.");                
       }  
       if (!setIsRange(s.getOutputSlots(), 0, s.getOutputSlots().size())) {
            fails++;
            if (v)
                System.out.println("Error on removing ID set: Output set is not correct range.");
        }        
        if (!(s.getOutputSlots().size() == s.getNnOutputs())) {
            fails++;
            if (v)
                System.out.println("Error on removing ID set: Number of inputs is not the same as required number of inputs.");                
        }       
        if (s.getState() != SimState.STOPPED_WITH_CRITICAL_CHANGE) {
            fails++;
            if (v)
                System.out.println("State not set to STOPPED_WITH_CRITICAL_CHANGE after removing behaviour.");                
        }               

        s.setState(SimState.PAUSED);
        s.removeBehaviours(ids);
        if (!(s.getBehaviours().size() == 2)) {
            fails++;
            if (v)
                System.out.println("Error on removing nonexistent ID set: wrong final behaviour set size.");                
       }   
       if (!setIsRange(s.getOutputSlots(), 0, s.getOutputSlots().size())) {
            fails++;
            if (v)
                System.out.println("Error on removing nonexistent ID set: Output set is not correct range.");
        }         
        if (!(s.getOutputSlots().size() == s.getNnOutputs())) {
            fails++;
            if (v)
                System.out.println("Error on removing nonexistent ID set: Number of inputs is not the same as required number of inputs.");                
        }  
        if (s.getState() != SimState.PAUSED) {
            fails++;
            if (v)
                System.out.println("State change but no behaviours removed (no valid ids).");                
        } 
        
        s.setState(SimState.PAUSED);
        ids.add(2);
        s.removeBehaviours(ids);
        if (!(s.getBehaviours().size() == 1)) {
            fails++;
            if (v)
                System.out.println("Error on removing mixed existing/nonexisting ID set: wrong final behaviour set size.");                
       }    
       if (!setIsRange(s.getOutputSlots(), 0, s.getOutputSlots().size())) {
            fails++;
            if (v)
                System.out.println("Error on removing mixed existing/nonexisting ID set: Output set is not correct range.");
        }           
        if (!(s.getOutputSlots().size() == s.getNnOutputs())) {
            fails++;
            if (v)
                System.out.println("Error on removing mixed existing/nonexisting ID set: Number of inputs is not the same as required number of inputs.");                
        }   
        if (s.getState() != SimState.STOPPED_WITH_CRITICAL_CHANGE) {
            fails++;
            if (v)
                System.out.println("State not set to STOPPED_WITH_CRITICAL_CHANGE after removing behaviours.");                
        }               

        
        if (v)
            System.out.println("Add/Remove behaviour failures: " + fails);        
        
        return fails;
    }    
    
    public static int testRun(Simulation s, boolean v) { 
        int fails = 0;
        
        //s.initialise();
        
        long oldTime = s.time();
        
        
        s.run();
        
        
        int energyFails = 0;
        for (boolean x : s.listIsDead()) {
            if (x) {
                energyFails++;   
            }                
        }     
        if (v && (energyFails > 0))
            System.out.println(energyFails + " dead bots still present.");        
        fails += energyFails;        
        if (s.time() != oldTime+1) {
            fails++;
            if (v)
                System.out.println("Simulation time incorrect. " + (oldTime+1) + " expected, " + s.time() + " found.");
        } 
        if (s.population() > s.getMaxPop()) {
            fails++;
            System.out.println("Population overflow. " + s.getMaxPop() + " maximum, " + s.population() + " found."); 
        }
        
        
        s.setState(SimState.STOPPED);
        oldTime = s.time();
        s.run();
        if (s.time() != oldTime) {
            fails++;
            if (v)
                System.out.println("Error: Simulation time changing when simulation is stopped.");
        }     
        
        s.setState(SimState.PAUSED);
        oldTime = s.time();
        s.run();
        if (s.time() != oldTime) {
            fails++;
            if (v)
                System.out.println("Error: Simulation time changing when simulation is paused.");
        }         
        
        s.setState(SimState.RUNNING);
        return fails;        
    }
    
    public static int testBotReport(Simulation s, boolean v) {     
        int fails = 0;
        
        List<Map<String, Object>> r = s.botReport();

        if (s.population() != r.size()) {
            fails++;
            System.out.println("List size not population size. " + s.population() + " population, " + r.size() + " list size."); 
        }        
        
        for (Map<String, Object> m : r) {
            fails += testDouble(m, "PosX", v);
            fails += testDouble(m, "PosY", v);
            fails += testLong(m, "ID", v);    
        }
        
        return fails;
    }
    
    public static int testFieldsReport(Simulation s, boolean v) {     
        int fails = 0;
        
        List<Map<String, Object>> r = s.fieldsReport();

        if (s.listFields().size() != r.size()) {
            fails++;
            System.out.println("List size not equal to number of fields. " + s.listFields().size() + " population, " + r.size() + " list size."); 
        }        
        
        for (Map<String, Object> m : r) {
            fails += testString(m, "name", v);
            if (!m.containsKey("RGB")) {
                fails++;
                if (v)
                    System.out.println("Key RGB not present."); 
            } else {
                try {
                    int[] test = (int[]) m.get("RGB");
                    int a = test[0];
                    int b = test[1];
                    int c = test[2];
                } catch (Exception e) {
                    fails++;
                    e.printStackTrace();
                }
            } 
            fails += testInt(m, "Xsamples", v);
            fails += testInt(m, "Ysamples", v);
            if (!m.containsKey("values")) {
                fails++;
                if (v)
                    System.out.println("Key values not present."); 
            } else {
                try {
                    double[][] test = (double[][]) m.get("values");
                    int xSize = (int) m.get("Xsamples");
                    int ySize = (int) m.get("Ysamples");
                    for (int x = 0; x < xSize; x++) {
                        for (int y = 0; y < ySize; y++) {
                            double a = test[x][y];
                        }
                    }
                } catch (Exception e) {
                    fails++;
                    e.printStackTrace();
                }
            }            
        }        
        return fails;
    }    
    
    public static int testSimulationReport(Simulation s, boolean v) {     
        int fails = 0;
        
        Map<String, Object> m = s.simulationReport();
        int exSize = 4;

        if (m.size() != exSize) {
            fails++;
            System.out.println("List size not equal to expected size. " + exSize + " expected, " + m.size() + " list size."); 
        }        
        
        fails += testLong(m, "time", v);   
        fails += testInt(m, "population", v);
        
        if (!m.containsKey("senses")) {
            fails++;
            if (v)
                System.out.println("Key senses not present."); 
        } else {
            try {
                Map<String, String> test = (Map<String, String>) m.get("senses");
                for (String str : test.keySet()) {
                    String a = test.get(str);
                    Integer in = Integer.parseInt(str); //unsafe warning; ignore
                }
            } catch (Exception e) {
                fails++;
                e.printStackTrace();
            }
        }  
        if (!m.containsKey("behaviours")) {
            fails++;
            if (v)
                System.out.println("Key behaviours not present."); 
        } else {
            try {
                Map<String, String> test = (Map<String, String>) m.get("behaviours");
                for (String str : test.keySet()) {
                    String a = test.get(str);
                    Integer in = Integer.parseInt(str); //unsafe warning; ignore
                }
            } catch (Exception e) {
                fails++;
                e.printStackTrace();
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
    
    
    public static int testInt(Map m, String key, boolean v) {
        int fails = 0;
        if (!m.containsKey(key)) {
            fails++;
            if (v)
                System.out.println("Key " + key + " not present."); 
        } else {
            try {
                int test = (int) m.get(key);
            } catch (Exception e) {
                fails++;
                e.printStackTrace();
            }
        }     
        return fails;
    }
              
    public static int testLong(Map m, String key, boolean v) {
        int fails = 0;
        if (!m.containsKey(key)) {
            fails++;
            if (v)
                System.out.println("Key " + key + " not present."); 
        } else {
            try {
                long test = (long) m.get(key);
            } catch (Exception e) {
                fails++;
                e.printStackTrace();
            }
        }     
        return fails;
    }
    
    public static int testDouble(Map m, String key, boolean v) {
        int fails = 0;
        if (!m.containsKey(key)) {
            fails++;
            if (v)
                System.out.println("Key " + key + " not present."); 
        } else {
            try {
                double test = (double) m.get(key);
            } catch (Exception e) {
                fails++;
                e.printStackTrace();
            }
        }     
        return fails;
    }       
    
    public static int testString(Map m, String key, boolean v) {
        int fails = 0;
        if (!m.containsKey(key)) {
            fails++;
            if (v)
                System.out.println("Key " + key + " not present."); 
        } else {
            try {
                String test = (String) m.get(key);
            } catch (Exception e) {
                fails++;
                e.printStackTrace();
            }
        }     
        return fails;
    }     
    
}
