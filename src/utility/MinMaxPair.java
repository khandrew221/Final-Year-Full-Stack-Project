/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

/**
 *
 * @author Kathryn Andrew
 */
public class MinMaxPair {
    
    private double min;
    private double max;
    
    public MinMaxPair(double val1, double val2) {
        if (val1 > val2) {
            min = val2;
            max = val1;
        } else {
            min = val1;
            max = val2;            
        }
    }
    
    public double getMin() {
        return min;
    }
    
    public double getMax() {
        return max;
    }    
}
