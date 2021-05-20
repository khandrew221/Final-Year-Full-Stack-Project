/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

/**
 *
 * @author Kathryn Andrew
 */
public class SenseFactory {
    
    
    
    /**
     * Creates a simple one point envirosense.
     * @return 
     */
    public static SenseEnviro Enviro(String target, Environment e) {
        return new SenseEnviro(target, e);
    }
    
}
