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
    private SimStateFacade sim;
    private Map<String, BufferedImage> bgImg = new HashMap<String, BufferedImage>();  
    
    private  List<Map<String, Object>> botReport;
    
    
    public SimVis(SimStateFacade s, int w, int h) {
        HEIGHT = h;
        WIDTH = w;
        sim = s;       
        for(String str : s.listFields()) {
            bgImg.put(str, new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB));  
        }
        buildEnviroImage();
        updateData();
    }
    
    
    
    protected void buildEnviroImage() {
        
        
        for(String field : bgImg.keySet()) {
            for(int y = 0; y < HEIGHT; y++) {
                for(int x = 0; x < WIDTH; x++) {

                    int[] c = sim.getFieldRGB(field);
                    int red = c[0];
                    int green = c[1];
                    int blue = c[2];
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
        int r = 7;
        g.setColor(Color.RED);        
        synchronized (botReport) {
            for (Map<String, Object> m : botReport) {
                double x = (double) m.get("PosX");
                double y = (double) m.get("PosY");
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
        //buildEnviroImage();
        repaint();
    }
    
}
