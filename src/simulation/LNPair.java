/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

/**
 * A simple pair class to hold layer + node index coordinates, for mapping to each other as
 * implemented position to genetically coded position.
 * @author Kathryn Andrew
 */
public class LNPair {
    final int layer;
    final int node;
    
    public LNPair(int l, int n) {
        layer = l;
        node = n;
    }

    public int getLayer() {
        return layer;
    }

    public int getNode() {
        return node;
    }
    
    @Override
    public String toString() {
        return "<" + Integer.toString(layer) + ", " + Integer.toString(node) + ">";
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LNPair)) {
            return false;
        }
        LNPair c = (LNPair) o;
        return Integer.compare(layer, c.getLayer()) == 0
                && Integer.compare(node, c.getNode()) == 0;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.layer;
        hash = 29 * hash + this.node;
        return hash;
    }

}
