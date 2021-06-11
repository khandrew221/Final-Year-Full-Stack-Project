/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JComponent;

/**
 *
 * FieldsGraphics displays a visualisation of the simulation fields, based on a
 * simulation facade fieldsReport(). The component can be of any size and will 
 * scale the field data to fit.
 * 
 * 
 * 
 * @author Kathryn Andrew
 */
public class FieldsGraphics extends JComponent {
    
    private int HEIGHT;  
    private int WIDTH; 
    private FieldsImages fieldsImages;
    private Set<String> activeFields;

    /**
     * 
     * @param w
     * @param h
     * @param fieldsReport 
     */
    public FieldsGraphics(int w, int h, List<Map<String, Object>> fieldsReport) {
        HEIGHT = h;
        WIDTH = w;
        this.setPreferredSize(new Dimension(w,h));     
        fieldsImages = new FieldsImages();
        fieldsImages.initialise(fieldsReport);        
        activeFields = fieldsImages.getFields();
    }

    /**
     * Updates the FieldImages data via a fieldsReport(). This should be called
     * after any changes have occurred to the environment, e.g. adding or 
     * removing a field.
     * 
     * @param fieldsReport 
     */
    public void updateData(List<Map<String, Object>> fieldsReport) {
        fieldsImages.initialise(fieldsReport);
        repaint();
    }        
        
    /**
     * 
     * Updates the set of active fields. This should be called
     * after any changes have occurred to field selection.
     * 
     * @param newActiveFields 
     */
    public void setActiveFields(Set<String> newActiveFields) {
        activeFields = newActiveFields;
    } 
    
    
        
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.white);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        fieldsImages.paintSelected(g, WIDTH, HEIGHT, activeFields);
    } 
    
}
