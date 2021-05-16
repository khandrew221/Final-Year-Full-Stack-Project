/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import controls.SimConsts;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Kathryn Andrew
 */
public class Environment {
    
    private final Map<String, ScalarField> fields = new HashMap<>();;
    private int xSize;
    private int ySize;
    
    
    /**
     * checks for minimum input value ranges, as these are the ones that cause fatal errors if 
     * too low.  !!!NO BAD DESIGN BY CONTRACT
     * @param x
     * @param y
     */
    public Environment(int x, int y) {
        if (x >= SimConsts.getENV_MIN_SIZE())
            xSize = x;
        else 
            xSize = SimConsts.getENV_MIN_SIZE();
        if (y >= SimConsts.getENV_MIN_SIZE())
            ySize = y;
        else 
            ySize = SimConsts.getENV_MIN_SIZE();        
    }

    /**
     * Resizes the environment and all scalar fields  !!!NO BAD DESIGN BY CONTRACT
     * @param x
     * @param y
     * @param d 
     */
    void resize(int x, int y) {
        if (x >= SimConsts.getENV_MIN_SIZE())
            xSize = x;
        else 
            xSize = SimConsts.getENV_MIN_SIZE();
        if (y >= SimConsts.getENV_MIN_SIZE())
            ySize = y;
        else 
            ySize = SimConsts.getENV_MIN_SIZE();        
        for (ScalarField s : fields.values()) {
            s.resample(x, y, s.getDensity());
        }
    }
    
    
    /**
     * adds an empty (all 0 value) field with the given String as name/key to the field map.
     * If the name is already present as a map key, nothing is added.
     * @param name name of the new field
     */
    void addField(String name, int d, double min, double max) {
        if (!fields.containsKey(name))
            fields.put(name, new ScalarField(xSize, ySize, d, min, max));
    }    
    
    /**
     * removes field with the given String as name/key from the field map if it exists
     * If the name is already present as a map key, nothing is added.
     * @param name name of the field to be removed
     */
    void removeField(String name) {
        if (fields.containsKey(name))
            fields.remove(name);
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

    /**
     * randomises the field with the given String as name/key 
     * If the name is already present as a map key, nothing is added.
     * @param name name of the field to be randomised
     */
    void randomiseField(String name, double min, double max) {
        if (fields.containsKey(name))
            fields.get(name).randomise(min, max);
    }    
    
    /**
     * 
     * @return set of all field names
     */
    public Set<String> listFields() {
        return fields.keySet();
    }     
    
    /**
     * Returns the normalised value of the field with name n at position x,y, or 0 if name not found or position out of bounds 
     * @param n  
     * @param x 
     * @param y 
     * @return value of field with name n at position x,y, or 0 if name not found or position out of bounds 
     */
    public double normValueAt(String n, double x, double y) {
        if (fields.containsKey(n)) {
            if (x >= 0 && x <= xSize && y >= 0 && y <= ySize)
                return fields.get(n).normValueAt(x, y);
            return 0;
        } else
            return 0;
    }    
    
    /**
     * Returns the value at position x,y for the field with name n, or 0 if name not found or position out of bounds 
     * @param n  
     * @param x 
     * @param y 
     * @return value of field with name n at position x,y, or 0 if name not found or position out of bounds 
     */
    public double trueValueAt(String n, double x, double y) {
        if (fields.containsKey(n)) {
            if (x >= 0 && x <= xSize && y >= 0 && y <= ySize)
                return fields.get(n).trueValueAt(x, y);
            return 0;
        } else
            return 0;
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
    }*/
    
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
    
}
