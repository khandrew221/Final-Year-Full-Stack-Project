/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import controls.SimControl;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import simulation.SimStateFacade;

/**
 *
 * @author Kathryn Andrew
 */
public class FitnessPanel extends JPanel {

    private SimControl simControl;
    private SimStateFacade simFacade;    
    
    private JPanel fitnessSliderPanel;
    private JLabel info;
    private List<LabelledSlider> fitnessSliders;
    
    public FitnessPanel(SimControl simControl, SimStateFacade simFacade) {        
        this.simControl = simControl;
        this.simFacade = simFacade;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        info = new JLabel();
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
        
        JPanel mainHolder = new JPanel();
        mainHolder.setBorder(BorderFactory.createEtchedBorder());
        this.add(mainHolder);
        mainHolder.setLayout(new BorderLayout());
        
        fitnessSliderPanel = new JPanel();
        fitnessSliderPanel.setLayout(new BoxLayout(fitnessSliderPanel, BoxLayout.PAGE_AXIS));
        
        fitnessSliders = new ArrayList<>();
        fitnessSliderPanel.setBorder(BorderFactory.createTitledBorder(title));
        for(String name : ParamsAndWeights.keySet()) {
            LabelledSlider entry = new LabelledSlider(name, paramNameToText(name), -10, 10, 20, ParamsAndWeights.get(name)+10, null);
            fitnessSliderPanel.add(entry);
            fitnessSliders.add(entry);
            entry.setToolTipText(paramNameToTooltip(name));
        }             
        //fitnessSliderPanel.setPreferredSize(new Dimension(200,35*ParamsAndWeights.size() + 30));
        mainHolder.add(fitnessSliderPanel);   
        mainHolder.add(info, BorderLayout.PAGE_END);
        info.setPreferredSize(new Dimension(100, 100));
        info.setBorder(BorderFactory.createEtchedBorder());  
        info.setText("<html>Bots are each given a fitness score. The score updates every simulation cycle, "
                + "and bots with a higher score are more likely to breed. Positive weights encourage the evolution of the "
                + "associated trait, and negative weights discourage the trait. A weight of zero causes the trait to be "
                + "ignored in terms of fitness score.</html>");
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
    
    private String paramNameToText(String n) {
        switch (n) {
            case "COLLISIONS_PER_CYCLE":
                return "Collisions with the environment border";
            case "DISTANCE_TRAVELLED":
                return "Average speed";   
            case "CURRENT_ENERGY":
                return "Current energy";   
            case "AMOUNT_EATEN":
                return "Amount eaten";                             
        }
        return "ERROR";
    }
    
    private String paramNameToTooltip(String n) {
        switch (n) {
            case "COLLISIONS_PER_CYCLE":
                return "<html>Rewards or penalises collisions with the environment boundary.</html>";
            case "DISTANCE_TRAVELLED":
                return "<html>Rewards or penalises moving at a high speed.</html>";  
            case "CURRENT_ENERGY":
                return "<html>Rewards or penalises having a large amount of energy<br>"
                        + "at the moment parents are selected.</html>";   
            case "AMOUNT_EATEN":
                return "<html>Rewards or penalises bots that have eaten a lot<br> "
                        + "(across all eat behaviours over the bot's lifetime).</html>";                               
        }
        return "ERROR";
    }    
   
}
