/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import simulation.Simulation;
import utility.Point;

/**
 *
 * @author Kathryn Andrew
 */
public class SimVis extends JComponent {
    
    private int HEIGHT;  
    private int WIDTH; 
    private Simulation sim;
    private Map<String, BufferedImage> bgImg = new HashMap<String, BufferedImage>();  
    
    private List<Point> botReport;
    
    
    public SimVis(Simulation s, int w, int h) {
        HEIGHT = h;
        WIDTH = w;
        sim = s;       
        for(String str : s.listFields()) {
            bgImg.put(str, new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB));  
        }
        
        updateData();
    }
    
    
    
    protected void buildEnviroImage() {
        
        
        for(String field : bgImg.keySet()) {
            for(int y = 0; y < HEIGHT; y++) {
                for(int x = 0; x < WIDTH; x++) {

                    Color c = sim.getFieldColor(field);
                    int red = c.getRed();
                    int green = c.getGreen();
                    int blue = c.getBlue();
                    int alpha = 0 + (int) Math.round(sim.envNormValueAt(field, x, y)*200);

                    //bitwise colour 
                    int p = (alpha<<24) | (red<<16) | (green<<8) | blue;

                    bgImg.get(field).setRGB(x, y, p);

                    //(int)(Math.random()*256); 
                }
            }            
        }
    }
    
    
    protected void paintBackground(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        for(String field : bgImg.keySet()) {
            g.drawImage(bgImg.get(field), 0, 0, this);
        } 
    }
    
    protected void paintBots(Graphics g) {
        int r = 5;
        g.setColor(Color.DARK_GRAY);
        
        synchronized(botReport) {
            Iterator i = botReport.iterator(); // Must be in synchronized block
            Point p = new Point(0,0);
            while (i.hasNext()) {
                p = (Point) i.next();
                g.fillOval((int)Math.round(p.getX() - r/2), (int)Math.round(p.getY() - r/2), (int)Math.round(r), (int)Math.round(r));;
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
        if (botReport.isEmpty()) {
            System.out.println("Empty!");
        }
        buildEnviroImage();
        repaint();
    }
    
}
