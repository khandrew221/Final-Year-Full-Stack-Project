/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import controls.SimConsts;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Kathryn Andrew
 */
public class LabelledSlider extends JPanel {

    private JSlider slider;
    private JLabel label;
    private String name;
    private String text;
    private double min;
    private double max;
    private int steps;
    private Updatable container;
    
    public LabelledSlider(String name, String text, double min, double max, int steps, int start, Updatable container) {
        this.setLayout(new BorderLayout());
        
        if (SimConsts.ENABLE_BORDERS)
            this.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.BLACK, 1), new EmptyBorder(5, 5, 5, 5)));
        else 
            this.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        this.min = min;
        this.max = max;
        this.steps = steps;
        this.container = container;

        this.name = name;
        this.text = text;
        slider = new JSlider(JSlider.HORIZONTAL, 0, steps, start);
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        
        label = new JLabel(text + ": " + String.format("%.2f",getValue())); 
        
        slider.addChangeListener(new ChangeListener() {
          @Override
          public void stateChanged(ChangeEvent event) {
            label.setText(text + ": " + String.format("%.2f",getValue()));
            if (container != null) {
                container.update();
            }
          }
        });
                
        
        this.add(label, BorderLayout.WEST);
        this.add(slider, BorderLayout.EAST);      
    }
    
    public final double getValue() {
        return min + (slider.getValue()/(double)steps * (max - min));
    } 
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public void setToolTipText(String s) {
        label.setToolTipText(s);
        slider.setToolTipText(s);
    }

}
