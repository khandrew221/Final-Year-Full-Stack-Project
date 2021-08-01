/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import controls.SimControl;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import simulation.SimStateFacade;

/**
 *
 * @author Kathryn Andrew
 */
public class FitnessPanel extends JPanel {

    private SimControl simControl;
    private SimStateFacade simFacade;    
    
    JPanel fitnessSliderPanel;
    List<LabelledSlider> fitnessSliders;
    
    public FitnessPanel(SimControl simControl, SimStateFacade simFacade) {        
        this.simControl = simControl;
        this.simFacade = simFacade;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    }
    
   /**
     * 
     * 
     * 
     * @param title 
     * @param ParamsAndWeights 
     */
    public void setup(String title, Map<String, Integer> ParamsAndWeights) {
        this.removeAll();
        fitnessSliderPanel = new JPanel();
        fitnessSliders = new ArrayList<>();
        this.setBorder(BorderFactory.createTitledBorder(title));
        for(String name : ParamsAndWeights.keySet()) {
            LabelledSlider entry = new LabelledSlider(name, -10, 10, 20, ParamsAndWeights.get(name)+10, null);
            this.add(entry);
            fitnessSliders.add(entry);
        }             
        fitnessSliderPanel.setPreferredSize(new Dimension(200,35*ParamsAndWeights.size() + 30));
        this.add(fitnessSliderPanel);        
    }   
    
    /**
     * 
     */
    public void updateFitnessWeights() {
        Map<String, Integer> newWeights = new HashMap<>();
        for (LabelledSlider slider : fitnessSliders) {
            newWeights.put(slider.getName(), (int) slider.getValue());
        }
        simControl.setFitnessWeights(newWeights);
    }
   
}
