/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import controls.SimConsts;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
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
    
    private int minGen;
    private int maxGen;
    
    public SimVis(SimStateFacade s, int simW, int simH, int w, int h) {
        HEIGHT = h;
        WIDTH = w;
        simHeight = simH;
        simWidth = simW;
        this.setPreferredSize(new Dimension(w,h));
        sim = s;    
        fieldsGraphics = new FieldsGraphics(w, h, sim.fieldsReport());
        this.add(fieldsGraphics);  
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        updateData();
    }
    
    protected void paintBots(Graphics g) {
        int r = 7;    
        for (Map<String, Object> m : sim.botReport()) {
            g.setColor(energyToColour((double) m.get("Energy"))); 
            double x = simToVisX((double) m.get("PosX"));
            double y = simToVisY((double) m.get("PosY"));
            //fudge edge positions so the bots aren't drawn off environment
            if (x > WIDTH-r*0.5)
                x = WIDTH-r*0.5;
            if (x < r*0.5)
                x = r*0.5;            
            if (y > HEIGHT-r*0.5)
                y = HEIGHT-r*0.5;  
            if (y < r*0.5)
                y = r*0.5;                
            g.fillOval((int)Math.round(x - r*0.5), (int)Math.round(y - r*0.5), (int)Math.round(r), (int)Math.round(r));
        }
    }  
    
    /**
     * Call every frame.
     */
    public void updateData() {
        fieldsGraphics.updateData(sim.fieldsReport());
        setMinMaxGen();
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
    
    public Color genToColour(int gen) {
        if (minGen == maxGen) {
            return new Color(255, 0, 0);
        } else {
           double range = maxGen-minGen;
           double val = (double) gen-minGen;
           val = val/range*255;
           if (val > 255)
               val = 255;
           if (val < 0)
               val = 0;           
           return new Color((int) Math.round(val), 0, 0);
        }
    }
    
    public Color energyToColour(double val) {
           if (val < 120) {
               return new Color(126, 126, 126);
           }
           val = val/SimConsts.getMAX_ENERGY()*255.0;
           if (val > 255)
               val = 255;
           if (val < 0)
               val = 0;           
           return new Color((int) Math.round(val), 0, 0);
    }    
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        fieldsGraphics.paintComponent(g);
        this.paintBots(g);
    } 
    
    private void setMinMaxGen() {
        minGen = Integer.MAX_VALUE;
        maxGen = 0;
        for (Map<String, Object> m : sim.botReport()) {
            int gen = (int) m.get("Generation"); 
            if (gen > maxGen) {
                maxGen = gen;
            }
            if (gen < minGen) {
                minGen = gen;
            }
        }
    }
}
