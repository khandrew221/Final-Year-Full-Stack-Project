/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

/**
 *
 * @author Kathryn Andrew
 */
public class Point {

    private final double x;
    private final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    
    public double getDistanceFrom(Point b){
        double x1 = this.x - b.getX();
        double y1 = this.y - b.getY();
        return Math.sqrt(x1*x1 + y1*y1);
    }    

    
    
    
    /**
     * 
     * Returns if the point falls into the rectangle defined by the two provided points (min,min) and (max,max) 
     * 
     * Req.for UC014 (move)
     * @param min
     * @param max
     * @return 
     */
    public boolean inBounds(Point min, Point max) {
        if (x >= min.x && x <= max.x && y >= min.y && y <= max.y)
            return true;
        return false;
    }    
    
    /**
     * Req. for UC012 (enviro)
     * @param origin
     * @param displacement
     * @return 
     */
    public static Point displace(Point origin, Point displacement) {
        return new Point(origin.x + displacement.x, origin.y + displacement.y);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Point other = (Point) obj;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
            return false;
        }
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "(" + Double.toString(x) + ", " + Double.toString(y) + ")";
    }

};
