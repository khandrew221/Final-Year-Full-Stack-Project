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
import javax.swing.JCheckBox;
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
public class SenseMaker extends ComponentMaker {
    
    private SensesSelect sensesSelect; 
    
    JPanel makerPanel;
    JPanel typeSpecificMakerPanel;
   
    JComboBox typeSelector;
    
    JComboBox fieldSelector;
    JSlider envRingsSlider;
    JLabel envRingsLabel = new JLabel();    
    JSlider envPointsSlider;
    JLabel envPointsLabel = new JLabel();
    JSlider envRadiusSlider;
    JLabel envRadiusLabel = new JLabel();   
    JCheckBox envCentred = new JCheckBox("Include point at bot location?");
    LabelledSlider borderRadiusSlider;
    

    /**
     * 
     * @param simControl
     * @param simFacade 
     */
  
    public SenseMaker(SimControl simControl, SimStateFacade simFacade) {
        
        super(simControl, simFacade);
              
        makerPanel = new JPanel(new GridLayout(5, 1));
        this.add(makerPanel);
        
        makeTypeSelector();
        makeFieldSelector();

        typeSpecificMakerPanel = new JPanel();
        typeSpecificMakerPanel.setPreferredSize(new Dimension(500,500));
        this.add(typeSpecificMakerPanel);
        
        
        makeAddComponentButton();
        makeRemoveComponentButton();
       
        sensesSelect= new SensesSelect(this);
        this.add(sensesSelect);        
        sensesSelect.setup("Senses", getIDsAndLabels(), true, true);  
        sensesSelect.setPreferredSize(new Dimension(500,500));
        
        setTypeSpecificMakerPanel((String) typeSelector.getSelectedItem());
        
    }

    /**
     * !!!Warning from line String[] t = types.toArray(new String[0]); suppressed.
     * toArray is generally broken in Java and throws unnecessary warnings. 
     */    
    @SuppressWarnings("unchecked")      
    private void makeTypeSelector() {
        Set<String> types = super.getFacade().getSenseTypes();        
        String[] t = types.toArray(new String[0]);
        typeSelector = new JComboBox(t);
        typeSelector.addActionListener(this);
        makerPanel.add(typeSelector);  
    }
    
    /**
     * !!!Warning from line String[] t = types.toArray(new String[0]); suppressed.
     * toArray is generally broken in Java and throws unnecessary warnings. 
     */    
    @SuppressWarnings("unchecked")      
    private void makeFieldSelector() {
        Set<String> fields = super.getFacade().getFields();        
        String[] f = fields.toArray(new String[0]);
        fieldSelector = new JComboBox(f);
        fieldSelector.addActionListener(this);
    }    
            
    
    /**
     * 
     */    
    private void setTypeSpecificMakerPanel(String type) {
        switch(type)
        {
            case "environment":
                setForEnviromentSense();
                this.revalidate();
                break;
            case "border":
                setForBorderSense();
                this.revalidate();
                break;                
            default:
                typeSpecificMakerPanel.removeAll();
        }        
    }   
    
    /**
     * 
     */    
    private void addSenseType(String type) {
        switch(type)
        {
            case "environment":
                String target = (String) fieldSelector.getSelectedItem();
                int error = super.getControl().addSenseEnviro(target, envCentred.isSelected(), envRingsSlider.getValue(), envPointsSlider.getValue(), envRadiusSlider.getValue());
                switch(error) {
                    case 0:
                        this.update();
                        this.revalidate();
                        break;
                    case 1:    
                        JOptionPane.showMessageDialog(null, "An identical sense already exists.", "Warning", JOptionPane.WARNING_MESSAGE);
                        break;
                    case 2:
                        JOptionPane.showMessageDialog(null, "The sense has no sample points.", "Warning", JOptionPane.WARNING_MESSAGE);
                        break;    
                    case 3:
                        int response = JOptionPane.showConfirmDialog(null,
                                            "Senses cannot be added or removed while the simulation is running. \n Stop simulation and add sense?",
                                            "Warning",
                                            JOptionPane.YES_NO_OPTION); 
                        if(response == JOptionPane.YES_OPTION){
                           super.getControl().stop();
                           addSenseType(type);                           
                        }
                        break;                          
                }  
                break;
            case "border":
                error = super.getControl().addSenseBorder(borderRadiusSlider.getValue());
                switch(error) {
                    case 0:
                        this.update();
                        this.revalidate();
                        break;
                    case 1:    
                        JOptionPane.showMessageDialog(null, "An identical sense already exists.", "Warning", JOptionPane.WARNING_MESSAGE);
                        break;  
                    case 3:
                        int response = JOptionPane.showConfirmDialog(null,
                                            "Senses cannot be added or removed while the simulation is running. \n Stop simulation and add sense?",
                                            "Warning",
                                            JOptionPane.YES_NO_OPTION); 
                        if(response == JOptionPane.YES_OPTION){
                           super.getControl().stop();
                           addSenseType(type);                           
                        }
                        break;                          
                }
            break;
        }        
    }     
    
    /**
     * 
     */    
    private void setForEnviromentSense() {
        typeSpecificMakerPanel.removeAll();
        
        JPanel selectorPanel = new JPanel(new BorderLayout()); 
        JLabel selectorLabel = new JLabel("Target field: ");
        selectorPanel.setPreferredSize(new Dimension(350, 50));
        selectorPanel.add(selectorLabel, BorderLayout.WEST);
        selectorPanel.add(fieldSelector, BorderLayout.EAST);
        typeSpecificMakerPanel.add(selectorPanel); 
        
        JPanel centredPanel = new JPanel(new BorderLayout()); 
        centredPanel.setPreferredSize(new Dimension(350, 50));
        centredPanel.add(envCentred);
        typeSpecificMakerPanel.add(centredPanel);       
                
        JPanel ringsPanel = new JPanel(new BorderLayout()); 
        ringsPanel.setPreferredSize(new Dimension(350, 50));
        envRingsSlider = new JSlider(JSlider.HORIZONTAL, 0, 3, 0);                 
        envRingsSlider.addChangeListener(new ChangeListener() {
          @Override
          public void stateChanged(ChangeEvent event) {
            envRingsLabel.setText("Rings: " + envRingsSlider.getValue());
          }
        });       
        sliderSetup(ringsPanel, envRingsSlider, envRingsLabel, "Rings: "); 
        
        JPanel pointsPanel = new JPanel(new BorderLayout()); 
        pointsPanel.setPreferredSize(new Dimension(350, 50));
        envPointsSlider = new JSlider(JSlider.HORIZONTAL, 1, 20, 1);                 
        envPointsSlider.addChangeListener(new ChangeListener() {
          @Override
          public void stateChanged(ChangeEvent event) {
            envPointsLabel.setText("Points per ring: " + envPointsSlider.getValue());
          }
        });                        
        sliderSetup(pointsPanel, envPointsSlider, envPointsLabel, "Points per ring: ");    
        
        JPanel radiusPanel = new JPanel(new BorderLayout()); 
        radiusPanel.setPreferredSize(new Dimension(350, 50));
        envRadiusSlider = new JSlider(JSlider.HORIZONTAL, 1, 20, 1);                 
        envRadiusSlider.addChangeListener(new ChangeListener() {
          @Override
          public void stateChanged(ChangeEvent event) {
            envRadiusLabel.setText("Ring radius: " + envRadiusSlider.getValue());
          }
        });                        
        sliderSetup(radiusPanel, envRadiusSlider, envRadiusLabel, "Ring radius: ");         
        
    }    
    
    /**
     * 
     */    
    private void setForBorderSense() {
        typeSpecificMakerPanel.removeAll();                     
        borderRadiusSlider = new LabelledSlider("radius", 1, 5, 4, 0);
        borderRadiusSlider.setPreferredSize(new Dimension(350, 50));    
        typeSpecificMakerPanel.add(borderRadiusSlider);      
    }    

    @Override
    public void actionPerformed(ActionEvent e){    
        if (e.getSource().equals(typeSelector)) {
            setTypeSpecificMakerPanel((String) typeSelector.getSelectedItem());
        } else if (e.getSource().equals(addComponentButton)) {
           addSenseType((String) typeSelector.getSelectedItem());
        } else if (e.getSource().equals(removeComponentButton)) {
            if (!super.getControl().isStopped()) {
                int response = JOptionPane.showConfirmDialog(null,
                                            "Senses cannot be added or removed while the simulation is running. \n Stop simulation?",
                                            "Warning",
                                            JOptionPane.YES_NO_OPTION); 
                if(response == JOptionPane.YES_OPTION){
                   super.getControl().stop();
                   super.getControl().removeSenses(sensesSelect.getSelectedIDs());                           
                }
            } else {
                super.getControl().removeSenses(sensesSelect.getSelectedIDs());
                update();
            }
        }     
    }
    
    /**
     * Updates the senseSelect panel.  Call after change within panel.
     */
    public void update() {
        sensesSelect.setup("Senses", getIDsAndLabels(), true, false);
        sensesSelect.setPreferredSize(new Dimension(500,500));
        this.repaint();
        this.revalidate();    
    }
    
    /**
     * Updates the senseSelect panel.  Call after switching to tab.
     */
    public void updateAll() {
        //update for potentially changed field list. 
        makeFieldSelector();
        
        //apply updates
        setTypeSpecificMakerPanel((String) typeSelector.getSelectedItem());
        update();
    }     
    
    @Override
    void makeAddComponentButton() {
        addComponentButton = new JButton("Add sense");
        addComponentButton.addActionListener(this);        
        makerPanel.add(addComponentButton);               
    }  
    
    @Override
    void makeRemoveComponentButton() {
        removeComponentButton = new JButton("Remove senses");
        removeComponentButton.addActionListener(this);        
        makerPanel.add(removeComponentButton);               
    }


    public Map<String, String> getIDsAndLabels() {
       Map<String, String> out = (Map<String, String>) super.getFacade().senseReport();  
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
