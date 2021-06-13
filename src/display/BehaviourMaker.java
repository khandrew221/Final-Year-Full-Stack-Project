/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import controls.SimControl;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import simulation.SimStateFacade;

/**
 *
 * @author Kathryn Andrew
 */
public class BehaviourMaker extends ComponentMaker {
    private BehavioursSelect behavioursSelect; 
    
    JPanel makerPanel;
    JPanel typeSpecificMakerPanel;
   
    JComboBox typeSelector;
    
    JSlider maxSpeedSlider;
    JLabel maxSpeedLabel = new JLabel();    

    /**
     * 
     * @param simControl
     * @param simFacade 
     */
  
    public BehaviourMaker(SimControl simControl, SimStateFacade simFacade) {
        
        super(simControl, simFacade);
              
        makerPanel = new JPanel(new GridLayout(5, 1));
        this.add(makerPanel);
        
        makeTypeSelector();
        
        typeSpecificMakerPanel = new JPanel();
        typeSpecificMakerPanel.setPreferredSize(new Dimension(500,500));
        this.add(typeSpecificMakerPanel);
        
        
        makeAddComponentButton();
        makeRemoveComponentButton();
       
        behavioursSelect = new BehavioursSelect(this);
        this.add(behavioursSelect);        
        behavioursSelect.setup("Behaviours", getIDsAndLabels(), true, true);  
        behavioursSelect.setPreferredSize(new Dimension(500,500));
        
        setTypeSpecificMakerPanel((String) typeSelector.getSelectedItem());
        
    }

    /**
     * !!!Warning from line String[] t = types.toArray(new String[0]); suppressed.
     * toArray is generally broken in Java and throws unnecessary warnings. 
     */    
    @SuppressWarnings("unchecked")      
    private void makeTypeSelector() {
        Set<String> types = super.getFacade().getBehaviourTypes();        
        String[] t = types.toArray(new String[0]);
        typeSelector = new JComboBox(t);
        typeSelector.addActionListener(this);
        makerPanel.add(typeSelector);  
    }
        
    /**
     * 
     */    
    private void setTypeSpecificMakerPanel(String type) {
        switch(type)
        {
            case "move":
                setForBehaviourMove();
                this.revalidate();
                break;
            default:
                typeSpecificMakerPanel.removeAll();
        }        
    }   
    
    /**
     * 
     */    
    private void addBehaviourType(String type) {
        switch(type)
        {
            case "move":
                int error = super.getControl().addBehaviourMove(maxSpeedSlider.getValue());
                switch(error) {
                    case 0:
                        this.update();
                        this.revalidate();
                        break;
                    case 1:    
                        JOptionPane.showMessageDialog(null, "A move behaviour already exists.", "Warning", JOptionPane.WARNING_MESSAGE);
                        break;
                    case 3:
                        int response = JOptionPane.showConfirmDialog(null,
                                            "Behaviours cannot be added or removed while the simulation is running. \n Stop simulation and add behaviour?",
                                            "Warning",
                                            JOptionPane.YES_NO_OPTION); 
                        if(response == JOptionPane.YES_OPTION){
                           super.getControl().stop();
                           addBehaviourType(type);                           
                        }
                        break;                          
                }  
                break;
        }        
    }     
    
    /**
     * 
     */    
    private void setForBehaviourMove() {
        typeSpecificMakerPanel.removeAll();
        
        JPanel maxSpeedPanel = new JPanel(new BorderLayout()); 
        maxSpeedPanel.setPreferredSize(new Dimension(350, 50));
        maxSpeedSlider = new JSlider(JSlider.HORIZONTAL, 1, 5, 1);                 
        maxSpeedSlider.addChangeListener(new ChangeListener() {
          @Override
          public void stateChanged(ChangeEvent event) {
            maxSpeedLabel.setText("Maximum speed: " + maxSpeedSlider.getValue());
          }
        });       
        sliderSetup(maxSpeedPanel, maxSpeedSlider, maxSpeedLabel, "Maximum speed: "); 
    }     

    @Override
    public void actionPerformed(ActionEvent e){    
        if (e.getSource().equals(typeSelector)) {
            setTypeSpecificMakerPanel((String) typeSelector.getSelectedItem());
        } else if (e.getSource().equals(addComponentButton)) {
           addBehaviourType((String) typeSelector.getSelectedItem());
        } else if (e.getSource().equals(removeComponentButton)) {
            if (!super.getControl().isStopped()) {
                int response = JOptionPane.showConfirmDialog(null,
                                            "Behaviours cannot be added or removed while the simulation is running. \n Stop simulation?",
                                            "Warning",
                                            JOptionPane.YES_NO_OPTION); 
                if(response == JOptionPane.YES_OPTION){
                   super.getControl().stop();
                   super.getControl().removeBehaviours(behavioursSelect.getSelectedIDs());                           
                }
            } else {
                super.getControl().removeBehaviours(behavioursSelect.getSelectedIDs());
                update();
            }
        }     
    }
    
    /**
     * Updates the behaviourSelect panel.  Call after change to simulation behaviours.
     */
    @Override
    public void update() {
        behavioursSelect.setup("Behaviours", getIDsAndLabels(), true, false);
        behavioursSelect.setPreferredSize(new Dimension(500,500));
        this.repaint();
        this.revalidate();    
    }     
    
    @Override
    void makeAddComponentButton() {
        addComponentButton = new JButton("Add behaviour");
        addComponentButton.addActionListener(this);        
        makerPanel.add(addComponentButton);               
    }  
    
    @Override
    void makeRemoveComponentButton() {
        removeComponentButton = new JButton("Remove behaviours");
        removeComponentButton.addActionListener(this);        
        makerPanel.add(removeComponentButton);               
    }


    public Map<String, String> getIDsAndLabels() {
       Map<String, String> out = (Map<String, String>) super.getFacade().behaviourReport();  
       return out;    
    }    
    

    private void sliderSetup(JPanel container, JSlider slider, JLabel label, String labelText) {
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);       
        label.setText(labelText + slider.getValue());
        container.add(label, BorderLayout.WEST);
        container.add(slider, BorderLayout.EAST);
        typeSpecificMakerPanel.add(container);  
    }
}
