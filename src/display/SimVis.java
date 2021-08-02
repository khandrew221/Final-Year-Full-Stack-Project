/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Map;
import java.util.Set;
import javax.swing.JComponent;
import simulation.SimStateFacade;

/**
 *
 * @author Kathryn Andrew
 */
public class SimVis extends JComponent {
    
    private int HEIGHT;  
    private int WIDTH; 
    private int simHeight;  
    private int simWidth;     
    private SimStateFacade sim;
    private FieldsGraphics fieldsGraphics;
    
    public SimVis(SimStateFacade s, int simW, int simH, int w, int h) {
        HEIGHT = h;
        WIDTH = w;
        simHeight = simH;
        simWidth = simW;
        this.setPreferredSize(new Dimension(w,h));
        sim = s;    
        fieldsGraphics = new FieldsGraphics(w, h, sim.fieldsReport());
        this.add(fieldsGraphics);        
        updateData();
    }
    
    protected void paintBots(Graphics g) {
        int r = 7;
        g.setColor(Color.RED);        
        for (Map<String, Object> m : sim.botReport()) {
            double x = simToVisX((double) m.get("PosX"));
            double y = simToVisY((double) m.get("PosY"));
            g.fillOval((int)Math.round(x - r*0.5), (int)Math.round(y - r*0.5), (int)Math.round(r), (int)Math.round(r));
        }
    }  
    
    /**
     * Call every frame.
     */
    public void updateData() {
        fieldsGraphics.updateData(sim.fieldsReport());
        repaint();
    }
    
    /**
     * Converts simulation x value to visualisation x value
     * @param x
     * @return 
     */
    private double simToVisX(double x) {
        double w = (double) WIDTH;
        double sw = (double) simWidth;
        return w/sw*x;
    }
    
    /**
     * Converts simulation y value to visualisation y value
     * @param x
     * @return 
     */
    private double simToVisY(double y) {
        double h = (double) HEIGHT;
        double sh = (double) simHeight;
        return h/sh*y;
    } 
    
    /**
     * Converts visualisation x value to simulation x value
     * @param x
     * @return 
     */
    private double visToSimX(double x) {
        double w = (double) WIDTH;
        double sw = (double) simWidth;
        return sw/w*x;
    }
    
    /**
     * Converts visualisation y value to simulation y value
     * @param y
     * @return 
     */
    private double visToSimY(double y) {
        double h = (double) HEIGHT;
        double sh = (double) simHeight;
        return sh/h*y;
    }    
    
    /**
     * Allows fieldsGraphics to set the active fields.
     * @param newActiveFields 
     */
    public void setActiveFields(Set<String> newActiveFields) {
        fieldsGraphics.setActiveFields(newActiveFields);
    } 
    
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        fieldsGraphics.paintComponent(g);
        this.paintBots(g);
    } 
    
}
