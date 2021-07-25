/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import java.awt.Color;
import java.util.Random;
import utility.Point;

/**
 *
 * @author Kathryn Andrew
 */
public class ScalarField {
    
    private int xSize;
    private int ySize; 
    private int density;
    private int xSamples;
    private int ySamples;    
    private double xUnit;
    private double yUnit; 
    private double valMin;
    private double valMax;    
    private double[][] values;
    private Color color;
    
    
    public ScalarField(int x, int y, int d, double min, double max, Color color) {
        this.xSize = x;
        this.ySize = y;
        this.density = d;
        this.xSamples = (int) Math.ceil(xSize/100*density);
        this.ySamples = (int) Math.ceil(ySize/100*density);
        this.valMin = min;
        this.valMax = max;
        this.values = new double[xSamples][ySamples]; 
        //(double) required for correct double result
        this.xUnit = (double) xSize / (xSamples-1);
        this.yUnit = (double) ySize / (ySamples-1);
        this.color = color;
    }
    
    /**
     * Randomise the grid with values between min (inclusive) and max (exclusive)
     * @param min
     * @param max
     */
    void randomise(double min, double max) {
        Random random = new Random();
        for (int i = 0; i < xSamples; i++) {
            for (int i2 = 0; i2 < ySamples; i2++) {
                double val = random.nextDouble();
                values[i][i2] = val;
            }
        }
        this.valMin = min;
        this.valMax = max;
    }
    
    /**
     * Sets the entire field to a given value
     * @param v value
     */
    void setTo(double v) {
        for (int i = 0; i < xSamples; i++) {
            for (int i2 = 0; i2 < ySamples; i2++) {
                values[i][i2] = normalise(v);
            }
        }
    }       
    
    /**
     * Returns the normal interpolated value at the given x,y location
     * @param p point within the field
     * @return interpolated value between 0 and 1 at location 
     */
    public double normValueAt(Point p) {
        
        int x1 = getLeftX(p.getX());
        int x2 = getRightX(p.getX());
        int y1 = getLowerY(p.getY());
        int y2 = getUpperY(p.getY()); 
        
        //closest sample values
        double Q11 = values[x1][y1];
        double Q12 = values[x1][y2];
        double Q21 = values[x2][y1];
        double Q22 = values[x2][y2];  

        double f = (p.getX() % xUnit) / xUnit;
        
        //System.out.println("f: " + f);
        
        double R1 = lerp(Q11, Q21, f);
        double R2 = lerp(Q12, Q22, f);
        
        //System.out.println("R1: " + R1);
        //System.out.println("R2: " + R2); 
        
        f = (p.getY() % yUnit) / yUnit;
        
        //System.out.println("f: " + f);
        
        return lerp(R1, R2, f);
    }

    /**
     * Returns the true interpolated value at the given x,y location
     * @param p point within the field
     * @return interpolated value at location between 0 and 1
     */    
    public double trueValueAt(Point p) {  
        double value = valMin + (normValueAt(p) * (valMax - valMin));
        //catch rounding errors
        if (value < valMin)
            return valMin;
        else if (value > valMax)
            return valMax;
        else        
            return value;
    } 

    /**
     * Changes the value of the given sample point by the given amount. Amount
     * should be in normalised form.
     * 
     * pre: x and y values must be valid.
     * 
     * Req for: eat behaviour
     * 
     * @param x
     * @param y
     * @param amount 
     */
    private void adjustSamplePointValue(int x, int y, double amount) {  
        if (Double.isNaN(amount)) {
            System.out.println("ERROR!!!!: " + amount);
        }
        values[x][y] = values[x][y]+amount;
        if (values[x][y] > 1)
            values[x][y] = 1;
        else if (values[x][y] < 0)
            values[x][y] = 0;
    }     
    
    /**
     * Changes the value of the field at the given position by the given amount.
     * Amount will be spread proportionally between closest sample points. 
     * Req for: eat behaviour
     * 
     * @param p
     * @param amount 
     */
    public void adjustValueAt(Point p, double amount) {  
        // amount as proportion of normalised range
        double normAmount = amount / (valMax - valMin);
        
        int x1 = getLeftX(p.getX());
        int x2 = getRightX(p.getX());
        int y1 = getLowerY(p.getY());
        int y2 = getUpperY(p.getY());  
        
        double old11 = values[x1][y1];
        double old12 = values[x1][y2];
        double old21 = values[x2][y1];
        double old22 = values[x2][y2];        
       
        //if point to adjust is exactly on a sample point
        if (x1 == x2 && y1 == y2) {
            adjustSamplePointValue(x1, y1, normAmount);
        } else if (x1 == x2) {
            //same x, distribute between two

            //closest sample values
            double Q11 = values[x1][y1];
            double Q12 = values[x1][y2];

            //propotion along y axis 
            double f = (p.getY() % yUnit) / yUnit;
            
            adjustSamplePointValue(x1, y1, normAmount*f);
            adjustSamplePointValue(x1, y2, normAmount*(1-f));         
            
        } else if (y1 == y2) {
            //same y, distribute between two
            //propotion along x axis 
            double f = (p.getX() % xUnit) / xUnit;
            
            //adjust closest sample values
            adjustSamplePointValue(x1, y1, normAmount*f);
            adjustSamplePointValue(x2, y1, normAmount*(1-f)); 
            
        } else {            
            //get areas between sample values and given point            
            double x1true = x1*xUnit;
            double x2true = x2*xUnit;
            double y1true = y1*yUnit;
            double y2true = y2*yUnit;
            
            double Q11Area = Math.abs(p.getX()-x1true)*Math.abs(p.getY()-y1true);
            double Q12Area = Math.abs(p.getX()-x1true)*Math.abs(p.getY()-y2true);
            double Q21Area = Math.abs(p.getX()-x2true)*Math.abs(p.getY()-y1true);
            double Q22Area = Math.abs(p.getX()-x2true)*Math.abs(p.getY()-y2true);  
            
            double areaTotal = Q11Area + Q12Area + Q21Area + Q22Area;

            //change to proportion of total
            Q11Area = Q11Area/areaTotal;
            Q12Area = Q12Area/areaTotal;
            Q21Area = Q21Area/areaTotal;
            Q22Area = Q22Area/areaTotal;     

            //adjust points.  Note diagonal swapping of areas: closer to point ->
            // larger area atteched to diagonal opposite point -> larger proportion
            // goes to closer point
            adjustSamplePointValue(x1, y1, normAmount*Q22Area);
            adjustSamplePointValue(x2, y2, normAmount*Q11Area);
            adjustSamplePointValue(x1, y2, normAmount*Q21Area);
            adjustSamplePointValue(x2, y1, normAmount*Q12Area);
            
        }
    }         
    
    /**
     * Returns the x sample number immediately to the right of the given x coordinate
     * @param x between 0 and xSize inclusive
     * @return 
     */
    private int getRightX(double x) {  
        int out = (int) Math.ceil(x/xUnit);
        if (out < 0)
            return 0;
        else if (out >= xSamples)    
            return xSamples-1;
        else
            return out;
    }
    
    /**
     * Returns the x sample number immediately to the left of the given x coordinate
     * @param x between 0 and xSize inclusive
     * @return 
     */    
    private int getLeftX(double x) {    
        int out = (int) Math.floor(x/xUnit);
        if (out < 0)
            return 0;
        else if (out >= xSamples)    
            return xSamples-1;
        else
            return out;
    }    

    /**
     * Returns the y sample number immediately above the given y coordinate
     * @param y between 0 and ySize inclusive
     * @return 
     */        
    private int getUpperY(double y) {    
        int out = (int) Math.ceil(y/yUnit);
        if (out < 0)
            return 0;
        else if (out >= ySamples)    
            return ySamples-1;
        else
            return out;        
    }    

    /**
     * Returns the y sample number immediately below the given y coordinate
     * @param y between 0 and ySize inclusive
     * @return 
     */    
    private int getLowerY(double y) {    
        int out = (int) Math.floor(y/yUnit);
        if (out < 0)
            return 0;
        else if (out >= ySamples)    
            return ySamples-1;
        else
            return out;          
    }      
    
    /**
     * Linear interpolation between values a and b at proportional distance f from value a
     * @param a
     * @param b
     * @param f between 0 and 1 inclusive
     * @return 
     */
    private double lerp(double a, double b, double f) {
        if (a == b)
            return a;
        if (f == 0.0)
            return a;        
        if (f == 1.0)
            return b;
        return (a * (1.0 - f)) + (b * f);
    }
    
    /**
     * Sets test values
     * @param v value
     */
    void setTest(double v0, double v1, double v2, double v3) {
        values[0][0] = normalise(v0);
        values[0][1] = normalise(v1);      
        values[1][0] = normalise(v2);
        values[1][1] = normalise(v3);          
    }    
    
    /**
     * Resets the size and density of the field.  Automatically sets derived parameters.  
     * Resamples the field to resize.
     * @param x x size of the field
     * @param y y size of the field
     * @param d density of the field in samples per 100 length
     */
    void resample(int x, int y, int d) {
        int newXSamples = (int) Math.ceil(x/100*d);
        int newYSamples = (int) Math.ceil(y/100*d);
        double resampleXUnit = (double) xSize/(newXSamples-1);
        double resampleYUnit = (double) ySize/(newYSamples-1);
        double[][] newValues = new double[newXSamples][newYSamples];       
        for (int i = 0; i < newXSamples; i++) {
            for (int i2 = 0; i2 < newYSamples; i2++) {
                newValues[i][i2] = this.normValueAt(new Point(resampleXUnit*i, resampleYUnit*i2));
            }
        } 
        this.xSize = x;
        this.ySize = y;
        this.density = d;
        this.xSamples = newXSamples;
        this.ySamples = newYSamples;   
        this.values = newValues;
        //(double) required for correct double result
        this.xUnit = (double) xSize / (xSamples-1);
        this.yUnit = (double) ySize / (ySamples-1);   
    }

    public int getXSize() {
        return xSize;
    }

    public int getYSize() {
        return ySize;
    }

    public int getDensity() {
        return density;
    }
    
    private double normalise(double x) {
        return (x-valMin)/(valMax-valMin);        
    }
    
    public Color getColor() {
        return color;
    }

    public int getXSamples() {
        return xSamples;
    }

    public int getYSamples() {
        return ySamples;
    }
    
    public double[][] getValues() {
        return values.clone();
    }   
    
    
        /**
     * Returns the normal interpolated value at the given x,y location
     * @param p point within the field
     * @return interpolated value between 0 and 1 at location 
     */
    public double normValueAtVerbose(Point p) {
        
        int x1 = getLeftX(p.getX());
        int x2 = getRightX(p.getX());
        int y1 = getLowerY(p.getY());
        int y2 = getUpperY(p.getY()); 
        
        System.out.println("x1: " + x1);
        System.out.println("x2: " + x2);
        System.out.println("y1: " + y1);
        System.out.println("y2: " + y2);
        
        //closest sample values
        double Q11 = values[x1][y1];
        double Q12 = values[x1][y2];
        double Q21 = values[x2][y1];
        double Q22 = values[x2][y2];
        
        System.out.println("Q11: " + Q11);
        System.out.println("Q12: " + Q12);
        System.out.println("Q21: " + Q21);
        System.out.println("Q22: " + Q22);    

        double f = (p.getX() % xUnit) / xUnit;
        
        //System.out.println("f: " + f);
        
        double R1 = lerp(Q11, Q21, f);
        double R2 = lerp(Q12, Q22, f);
        
        //System.out.println("R1: " + R1);
        //System.out.println("R2: " + R2); 
        
        f = (p.getY() % yUnit) / yUnit;
        
        //System.out.println("f: " + f);
        
        return lerp(R1, R2, f);
    }
}
