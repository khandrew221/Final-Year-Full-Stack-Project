/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private Map<String, BufferedImage> bgImg = new HashMap<String, BufferedImage>();  
    
    private List<Map<String, Object>> botReport;
    private List<Map<String, Object>> fieldsReport;
    
    
    public SimVis(SimStateFacade s, int simW, int simH, int w, int h) {
        HEIGHT = h;
        WIDTH = w;
        simHeight = simW;
        simWidth = simH;
        this.setPreferredSize(new Dimension(w,h));
        sim = s;    
        
        fieldsReport = sim.fieldsReport();
        for(Map m : fieldsReport) {
            String field = (String) m.get("name");
            int xSamples = (int) m.get("Xsamples");
            int ySamples = (int) m.get("Ysamples");
            bgImg.put(field, new BufferedImage(xSamples, ySamples, BufferedImage.TYPE_INT_ARGB));            
        }

        buildEnviroImages();
        updateData();
    }
    
    
    
    protected void buildEnviroImages() {        
        for(Map m : fieldsReport) {
            String field = (String) m.get("name");
            if (bgImg.containsKey(field)) {
                int xSamples = (int) m.get("Xsamples");
                int ySamples = (int) m.get("Ysamples");
                int[] rgb = (int[]) m.get("RGB");
                int red = rgb[0];
                int green = rgb[1];
                int blue = rgb[2];
                double[][] values = (double[][]) m.get("values");
                int[] pixels = new int[xSamples*ySamples];
                for(int y = 0; y < xSamples; y++) {
                    for(int x = 0; x < ySamples; x++) {
                        int alpha = (int) (values[x][y]*200);
                        int p = alpha<<24 | red<<16 | green<<8 | blue;
                        pixels[y*xSamples+x] = p;
                    }
                }
                bgImg.get(field).setRGB(0, 0, xSamples, ySamples, pixels, 0, xSamples);
            }            
        }
    }
    
    
    protected void paintBackground(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        for(String field : bgImg.keySet()) {
            
            g.drawImage(scale(bgImg.get(field), WIDTH, HEIGHT), 0, 0, this);
        } 
    }
    
    protected void paintBots(Graphics g) {
        int r = 7;
        g.setColor(Color.RED);        
        synchronized (botReport) {
            for (Map<String, Object> m : botReport) {
                double x = simToVisX((double) m.get("PosX"));
                double y = simToVisY((double) m.get("PosY"));
                g.fillOval((int)Math.round(x - r*0.5), (int)Math.round(y - r*0.5), (int)Math.round(r), (int)Math.round(r));
            }
        }
    }    
    
    
    // paint all components
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.paintBackground(g);
        this.paintBots(g);
    } 
    
    
    public void updateData() {
        botReport = sim.botReport();
        buildEnviroImages();
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
     * Returns a scaled version of the given BufferedImage using bilinear interpolation 
     * @param img
     * @param w
     * @param h
     * @return 
     */
    BufferedImage scale(BufferedImage img, int w, int h) {
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = out.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, w, h, null);
        g.dispose();
        return out;
    }
    
}
