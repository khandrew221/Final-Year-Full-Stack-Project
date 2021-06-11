/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Kathryn Andrew
 */
public class FieldsImages {
    
    private Map<String, BufferedImage> fieldImages = new HashMap<String, BufferedImage>(); 
    
    
    /**
     * Call to (re)initialise the data. Should be called after any action that 
     * could change the environment.
     * 
     * @param fieldsReport 
     */
    public void initialise(List<Map<String, Object>> fieldsReport) {
        for(Map m : fieldsReport) {
            String field = (String) m.get("name");
            int xSamples = (int) m.get("Xsamples");
            int ySamples = (int) m.get("Ysamples");
            fieldImages.put(field, new BufferedImage(xSamples, ySamples, BufferedImage.TYPE_INT_ARGB));        
        }
        buildFieldImages(fieldsReport);
    }

    /**
     * Builds the raw images of the environment fields. Should only be called as
     * part of initialise()
     * 
     * @param fieldsReport 
     */ 
    private void buildFieldImages(List<Map<String, Object>> fieldsReport) {        
        for(Map m : fieldsReport) {
            String field = (String) m.get("name");
            if (fieldImages.containsKey(field)) {
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
                fieldImages.get(field).setRGB(0, 0, xSamples, ySamples, pixels, 0, xSamples);
            }            
        }
    }
    
    
    /**
     * Paints all available fields to the given graphics object at the given
     * dimensions.
     * 
     * @param g
     * @param WIDTH
     * @param HEIGHT 
     */
    protected void paintAll(Graphics g, int WIDTH, int HEIGHT) {
        for(String field : fieldImages.keySet()) {      
            g.drawImage(scale(fieldImages.get(field), WIDTH, HEIGHT), 0, 0, null);
        } 
    }
    
    /**
     * Paints selected fields to the given graphics object at the given
     * dimensions. 
     * @param g
     * @param WIDTH
     * @param HEIGHT
     * @param selected 
     */
    protected void paintSelected(Graphics g, int WIDTH, int HEIGHT, Set<String> selected) {
        if (selected != null) {
            for(String field : selected) {
                if (fieldImages.containsKey(field)) {
                    g.drawImage(scale(fieldImages.get(field), WIDTH, HEIGHT), 0, 0, null);
                }            
            } 
        }
    }    
        
    /**
     * Returns a scaled version of the given BufferedImage using bilinear interpolation 
     * @param img
     * @param w
     * @param h
     * @return 
     */
    private BufferedImage scale(BufferedImage img, int w, int h) {
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = out.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, w, h, null);
        g.dispose();
        return out;
    }
    
    /**
     * Returns a set of strings naming the current fields.
     * @return 
     */
    public Set<String> getFields() {
        return fieldImages.keySet();
    }
    
}
