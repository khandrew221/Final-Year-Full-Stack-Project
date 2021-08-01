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
import javax.swing.JComponent;

/**
 *
 * @author Kathryn Andrew
 */
public class SamplePointsPreview extends JComponent {

    private int HEIGHT;  
    private int WIDTH; 
    List<Double> xValues; 
    List<Double> yValues;
    
    /**
     * 
     * @param w
     * @param h
     */
    public SamplePointsPreview(int w, int h, List<Double> x, List<Double> y) {
        HEIGHT = h;
        WIDTH = w; 
        this.xValues = x;
        this.yValues = y;
        this.setPreferredSize(new Dimension(w,h));     
    }
    
    public void setPoints( List<Double> x, List<Double> y) {
        this.xValues = x;
        this.yValues = y;  
        this.repaint();
    }
    
    
    @Override
    protected void paintComponent(Graphics g) {
        int pointSize = 4;
        int botSize = 6;        
        g.setColor(Color.white);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.RED);
        g.fillOval((WIDTH/2)-(botSize/2), (HEIGHT/2)-(botSize/2), botSize, botSize);
        g.setColor(Color.BLACK);
        for (int i = 0; i < xValues.size(); i++) {
            int graphX = (int) Math.round(xValues.get(i)*5+WIDTH*0.5)-(pointSize/2);
            int graphY = (int) Math.round(yValues.get(i)*5+HEIGHT*0.5)-(pointSize/2);
            g.fillOval(graphX, graphY, pointSize, pointSize);
        }
    }    
}
