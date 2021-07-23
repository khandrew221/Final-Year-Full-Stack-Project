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
public enum SenseType {
    
    ENVIRONMENT("environment"),
    BORDER("border");
    
    public final String label;

    private SenseType(String label) {
        this.label = label;
    }
    
}
