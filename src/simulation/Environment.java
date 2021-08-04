/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import utility.Point;

/**
 *
 * @author Kathryn Andrew
 */
public class Environment {
    
    private final Map<String, ScalarField> fields = new HashMap<>();;
    private int xSize;
    private int ySize;
    
    
    /**
     * 
     * @param x
     * @param y
     */
    public Environment(int x, int y) {
        xSize = x;
        ySize = y;    
    }

    /**
     * Removes all fields
     * 
     * UC002
     */
    public void clearAllFields() {
        fields.clear();
    }
    
    /**
     * adds a randomised field with the given String as name/key to the field map.
     * If the name is already present as a map key, nothing is added.
     * 
     * UC002
     * 
     * @param name name of the new field
     */
    void addField(String name, int d, double min, double max, double growthRate, Color color) {
        if (!fields.containsKey(name)) {
            fields.put(name, new ScalarField(xSize, ySize, d, min, max, growthRate, color));
            fields.get(name).randomise(min, max);
        }
    }     
    
    
    /**
     * Removes fields with the given names.
     * 
     * Req for: UC028
     */
    public void removeFields(Set<String> toRemove) {
        for (String n : toRemove) {
            fields.remove(n);
        }
    }    
    
    /**
     * Resizes the environment and all scalar fields.
     * 
     * req for: UNUSED BUT TESTED
     * 
     * @param x
     * @param y
     */
    void resize(int x, int y) {
        xSize = x;
        ySize = y;       
        for (ScalarField s : fields.values()) {
            s.resample(x, y, s.getDensity());
        }
    }
    
    /**
     * returns the color of the named field, or white if no field with that name
     * is found.
     * 
     * Req for: UC017
     * 
     * @param name 
     */
    Color getColor(String name) {
        if (fields.containsKey(name))
            return fields.get(name).getColor();
        else
            return Color.WHITE;
    }    
   
    
    /**
     * returns the color of the named field, or white if no field with that name
     * is found.
     * 
     * Req for: fitness
     * 
     * @param name 
     */
    Double getMaxValue(String name) {
        if (fields.containsKey(name))
            return fields.get(name).getValMax();
        else
            return 0.0;
    }      
    


    /**
     * randomises the field with the given String as name/key 
     * If the name is already present as a map key, nothing is added.
     * 
     * @param name name of the field to be randomised
     */
    void randomiseField(String name, double min, double max) {
        if (fields.containsKey(name))
            fields.get(name).randomise(min, max);
    }    
    
    /**
     * randomises the field with the given String as name/key 
     * If the name is already present as a map key, nothing is added.
     * 
     * @param name name of the field to be randomised
     */
    void randomiseField(String name) {
        if (fields.containsKey(name))
            fields.get(name).randomise();
    }      
    
    /**
     * 
     * Req for: testing
     * 
     * @return set of all field names
     */
    public Set<String> listFields() {
        return fields.keySet();
    }     
    
    /**
     * Returns the normalised value of the field with name n at position x,y, or 0 if name not found or position out of bounds 
     * 
     * Req for: envirosense
     * 
     * @param n  
     * @param p
     * @return value of field with name n at position x,y, or 0 if name not found or position out of bounds 
     */
    public double normValueAt(String n, Point p) {
        if (fields.containsKey(n)) {
            if (inField(p))
                return fields.get(n).normValueAt(p);
            return 0;
        } else
            return 0;
    }    
     
    
    /**
     * Adjusts the field value at the given point.
     * 
     * Req for: eat, field growth
     * 
     * Post: no field point is over max amount
     * Post: no field point is under min amount
     * 
     * @param n  
     * @param p
     * @param amount
     */
    public void adjustValueAt(String n, Point p, double amount) {  
        if (fields.containsKey(n)) {
            if (inField(p))
                fields.get(n).adjustValueAt(p, amount);
        }
    } 
    
    /**
     * Grows all fields
     */
    public void growAll() {  
        for (String n : fields.keySet()) {
            fields.get(n).grow();
        }
    }      
    
    /**
     * 
     * Req for: utility
     * 
     * @param p
     * @return 
     */
    public boolean inField(Point p) {
        if (p.getX() >= 0 && p.getX() <= xSize && p.getY() >= 0 && p.getY() <= ySize) {
            return true;
        }
        return false;
    }

    /**
     * Returns a random position within the environment
     * Currently excludes max values due to .nextDouble()'s 0 inclusive to 1 
     * exclusive range 
     * 
     * Req for: UC006
     */
    public Point randomPosition() {
        Random random = new Random();
        return new Point(random.nextDouble()*xSize, random.nextDouble()*ySize);
    }      
    
    
    public int getXSize() {
        return xSize;
    }

    public int getYSize() {
        return ySize;
    }  

    public int getDensity(String n) {
        if (fields.containsKey(n)) {
            return fields.get(n).getDensity();
        }        
        return 0;
    }
    
    /**
     * 
     * Req for: UC021
     * 
     * @param n
     * @return 
     */
    public int getXSamples(String n) {
        if (fields.containsKey(n)) {
            return fields.get(n).getXSamples();
        }        
        return 0;
    }    
    
    /**
     * 
     * Req for: UC021
     * 
     * @param n
     * @return 
     */    
   public int getYSamples(String n) {
        if (fields.containsKey(n)) {
            return fields.get(n).getYSamples();
        }        
        return 0;
    }    

    /**
     * 
     * Req for: UC021
     * 
     * @param n
     * @return 
     */   
   public double[][] getValues(String n) {
        if (fields.containsKey(n)) {
            return fields.get(n).getValues();
        }        
        return new double[0][0];
    }   
   
   
       /**
     * Returns the value at position x,y for the field with name n, or 0 if name not found or position out of bounds 
     * @param n  
     * @param p
     * @return value of field with name n at position x,y, or 0 if name not found or position out of bounds 
     */
    public double trueValueAt(String n, Point p) {
        if (fields.containsKey(n)) {
            if (inField(p))
                return fields.get(n).trueValueAt(p);
            return 0;
        } else
            return 0;
    }    
   
    /**
     * fills the field with the given String as name/key with the given value
     * If the name is already present as a map key, nothing is added.
     * @param name name of the field
     * @param value value for sample points to be set to
     */
    void setField(String name, double value) {
        if (fields.containsKey(name))
            fields.get(name).setTo(value);
    } 
    
      /*
     * !!!! necessary?
     * Checks that the field size and density is the same as the Environment settings.
     * @param n
     * @return true if field size and density is the same as the Environment settings, otherwise false
     
    private boolean verifyField(String n) {
        if (fields.containsKey(n)) {
            ScalarField s = fields.get(n);
            if (s.getXSize() == xSize && s.getYSize() == ySize && s.getDensity() == density)
                return true;
            return false;
        } else
            return false;             
    }
   
    */
   
   


}
