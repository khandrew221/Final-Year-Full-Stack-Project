/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Kathryn Andrew
 */
public class LabelledSlider extends JPanel {

    JSlider slider;
    JLabel label;
    String name;
    
    public LabelledSlider(String name, int min, int max, int start) {
        this.setPreferredSize(new Dimension(300, 50));
        
        this.name = name;
        slider = new JSlider(JSlider.HORIZONTAL, min, max, start);
        slider.setMajorTickSpacing(20);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        
        label = new JLabel(name + ": " + slider.getValue()); 
        
        slider.addChangeListener(new ChangeListener() {
          @Override
          public void stateChanged(ChangeEvent event) {
            label.setText(name + ": " + slider.getValue());
          }
        });
                
        this.add(label);
        this.add(slider);      
    }
    
    public int getValue() {
        return slider.getValue();
    } 
    
    @Override
    public String getName() {
        return name;
    }

}
