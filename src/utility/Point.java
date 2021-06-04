/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.util.Objects;

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
    //same xy values but different CoordSystemIndex with return false
    //equivalent positions in different coordinate systems will return true: checks of this type should be implemented elsewhere, where conversion can be achieved
    public boolean equals(Object o) {
        // self check
        if (this == o) {
            return true;
        }
        // null check
        if (o == null) {
            return false;
        }
        // type check and cast
        if (getClass() != o.getClass()) {
            return false;
        }
        Point check = (Point) o;
        return Objects.equals(check.getX(), this.getX())
                && Objects.equals(check.getY(), this.getY());
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
    
    @Override
    public String toString() {
        return "(" + Double.toString(x) + ", " + Double.toString(y) + ")";
    }

};