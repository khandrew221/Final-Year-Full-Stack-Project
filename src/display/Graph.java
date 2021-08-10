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
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import simulation.SimStateFacade;

/**
 *
 * @author Kathryn Andrew
 */
public class Graph extends JComponent {
    
    private int HEIGHT;  
    private int WIDTH;     
    private SimStateFacade sim;
    private  Map<String, Map<Long, Double>> data;
    private  Map<String, Double> absMax;
    private int border = 40;
    private int timeRange = 1000;
    private long currentTime = 0;
    private String yAxisLabelSeries;
    private String graphTitle;
    
    
    public Graph(String title, SimStateFacade s, int w, int h) {
        HEIGHT = h;
        WIDTH = w;
        this.setPreferredSize(new Dimension(w,h));
        sim = s;    
        data = new HashMap<>();
        absMax = new HashMap<>();
        graphTitle = title;
    }
    
    public void addSeries(String name) {
        data.put(name, new HashMap<>());
    }
    
    public void setYAxisLabel(String name) {
        if (data.containsKey(name)) {
            yAxisLabelSeries = name;
        }
    }    
    
    private synchronized void drawSeries(Graphics g, String name) {
        
        g.setColor(Color.BLACK); 
        double val = data.get(name).get(new Long(currentTime-timeRange));
        double y = val;
        double w = timeRange;
        
        double minTime = currentTime-timeRange;
        double maxTime = currentTime;
        
        
        for (long i = currentTime-timeRange+1; i < currentTime; i++) {
            double normX1 = ((i-1)-minTime)/(maxTime-minTime);  
            double normX2 = (i-minTime)/(maxTime-minTime); 
            double nextY = data.get(name).get(new Long(i));
            g.drawLine(xToGraphX(normX1), normyToGraphY(y), xToGraphX(normX2), normyToGraphY(nextY));
            y = nextY;
        }        
        
    }
    
    private void drawAxes(Graphics g) {       
        g.setColor(Color.BLACK);         
        g.drawLine(border, HEIGHT-border, border, border);
        g.drawLine(border, HEIGHT-border, WIDTH-border, HEIGHT-border);
        g.setColor(Color.RED); 
        g.drawLine(xToGraphX(0), normyToGraphY(0.5), xToGraphX(1), normyToGraphY(0.5));
    }   
    
    private void drawLabels(Graphics g, String name) {
        if(g instanceof Graphics2D) {
          Graphics2D g2 = (Graphics2D)g;
          g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

          //y axis mid
          g2.drawString("0",xToGraphX(0)-10,normyToGraphY(0.5)+5);
          //y axis lower
          g2.drawString(String.format("%.2f",-absMax.get(name)),xToGraphX(0)-31,normyToGraphY(0));
          //y axis upper
          g2.drawString(String.format("%.2f",absMax.get(name)),xToGraphX(0)-27,normyToGraphY(1)+5);
          //x axis right
          g2.drawString(Long.toString(currentTime),xToGraphX(1)-10,normyToGraphY(0)+14);
          //x axis left
          g2.drawString(Long.toString(currentTime-timeRange),xToGraphX(0)-10,normyToGraphY(0)+14);
          
          //title
          g2.drawString(graphTitle, border, border-10);
        }
    } 
    
    private void drawTestLine(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawLine(xToGraphX(0), normyToGraphY(0), xToGraphX(1), normyToGraphY(1));
    }
    
    private int xToGraphX(double x) { 
        double graphWidth = WIDTH - border*2;
        return (int) Math.round(x*graphWidth)+border;
    }     
    
    private int normyToGraphY(double y) {      
        double graphHeight = HEIGHT - border*2;
        return HEIGHT - border - (int) Math.round(y*graphHeight);
    }     
    
    public synchronized void updateData() {
        currentTime = sim.currentTime();
        for (String name : data.keySet()) {
            Map<Long, Double> newSeries = sim.getDataSeries(name, currentTime-timeRange, currentTime);
            double min = getMin(sim.getDataSeriesMin(name), 0);
            double max = sim.getDataSeriesMax(name); 
            absMax.put(name, getAbsMax(min, max));
            data.put(name, normaliseSeries(newSeries, -absMax.get(name), absMax.get(name)));   
        }
        repaint();
    }
    
    public void reset() {
        absMax = new HashMap<>();
        updateData();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        this.drawAxes(g);
        for (String name : data.keySet()) {
            this.drawSeries(g, name);
        }      
        this.drawLabels(g, yAxisLabelSeries);
    }     
    
    
    public static Map<Long, Double> normaliseSeries(Map<Long, Double> series, double min, double max) {
        
        Map<Long, Double> out = new HashMap<>();

        //normalise values
        for (long i : series.keySet()) {
            double val = series.get(i);
            val = (val-min)/(max-min);  
            out.put(i, val);
        }        
        
        return out;
    }
    
    private double getMin(double a, double b) {
        if (a < b)
            return a;
        else
            return b;
    }
    
    private double getAbsMax(double a, double b) {
        double absA = Math.abs(a);
        double absB = Math.abs(b);
        if (absA > absB)
            return absA;
        else
            return absB;
    }    
}
