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
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
    JComboBox fieldSelector;
    
    LabelledSlider maxSpeedSlider;  
    LabelledSlider selectForageEfficiency;
    LabelledSlider selectEnergyEfficiency;
    
    JLabel info;

    /**
     * 
     * @param simControl
     * @param simFacade 
     */
  
    public BehaviourMaker(SimControl simControl, SimStateFacade simFacade) {
        
        super(simControl, simFacade);
        this.setLayout(new BorderLayout());        
        
        info = new JLabel();
        info.setText("<html>Behaviours are controlled by the output of a bot's neural network, and allow it to respond to sensory inputs e.g. by moving or eating.</html>");
        this.add(info, BorderLayout.PAGE_END); 
        info.setPreferredSize(new Dimension(600, 100));
        info.setBorder(BorderFactory.createEtchedBorder());
        
        JPanel mainHolder = new JPanel();
        mainHolder.setBorder(BorderFactory.createEtchedBorder());
        this.add(mainHolder);
        
        makerPanel = new JPanel(new GridLayout(5, 1));
        mainHolder.add(makerPanel);        
              
        
        makeTypeSelector();
              makeFieldSelector();
        
        typeSpecificMakerPanel = new JPanel();
        typeSpecificMakerPanel.setPreferredSize(new Dimension(500,500));
        mainHolder.add(typeSpecificMakerPanel);
        
        
        makeAddComponentButton();
        makeRemoveComponentButton();
       
        behavioursSelect = new BehavioursSelect(this);
        mainHolder.add(behavioursSelect);        
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
            case "eat":
                setForBehaviourEat();
                this.revalidate();
                break;                
            default:
                typeSpecificMakerPanel.removeAll();
                this.revalidate();
                break;
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
            case "eat":
                String target = (String) fieldSelector.getSelectedItem();
                error = super.getControl().addBehaviourEat(selectForageEfficiency.getValue()/100.0, selectForageEfficiency.getValue()/100.0, target);
                switch(error) {
                    case 0:
                        this.update();
                        this.revalidate();
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
        maxSpeedSlider = new LabelledSlider("Maximum speed", 1, 5, 4, 0, this);
        typeSpecificMakerPanel.add(maxSpeedSlider);
        info.setText("<html>A move behaviour allows the bot to move in response to its sensory inputs. Bots cannot move outside of the bounds of the environment.</html>");
        this.repaint();    
    }     
    
    /**
     * 
     */    
    private void setForBehaviourEat() {
        typeSpecificMakerPanel.removeAll();
        
        JPanel selectorPanel = new JPanel(new BorderLayout()); 
        JLabel selectorLabel = new JLabel("Target field: ");
        selectorPanel.setPreferredSize(new Dimension(350, 50));
        selectorPanel.add(selectorLabel, BorderLayout.WEST);
        selectorPanel.add(fieldSelector, BorderLayout.EAST);
        typeSpecificMakerPanel.add(selectorPanel); 
        
        selectForageEfficiency = new LabelledSlider("Forage efficiency (%)", -10, 10, 20, 11, null);
        selectForageEfficiency .setToolTipText("<html>Controls how much of the resource the bot will consume in a single round.<br>"
                + "Negative values will instead cause the bot to desposit the resource in the environment.</html>");      
        typeSpecificMakerPanel.add(selectForageEfficiency); 
        selectEnergyEfficiency = new LabelledSlider("Energy efficiency (%)", -10, 10, 20, 11, null);
        selectEnergyEfficiency .setToolTipText("<html>Controls what proportion of the resource consumed will be converted to energy.</html>");            
        typeSpecificMakerPanel.add(selectEnergyEfficiency); 
        info.setText("<html>An eat behaviour allows the bot to interact with an environment field by consuming or excreting resources.<br> "
                + "Two positive values for forage and energy efficiency will cause the bot to gain energy from consuming the resource.<br>"
                + "A negative forage and a positive energy efficiency will cause the bot to lose energy from depositing the resource.<br>"
                + "A positive forage and a negative energy efficiency will cause the bot to lose energy from consuming the resource.<br>"
                + "Two negative values for forage and energy efficiency will cause the bot to gain energy from depositing the resource.</html>");
        this.repaint();         
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
    
    /**
     * Updates the behaviourMaker panel.  Call after switching to tab.
     */
    @Override
    public void updateAll() {
        //update for potentially changed field list. 
        makeFieldSelector();
        
        //apply updates
        setTypeSpecificMakerPanel((String) typeSelector.getSelectedItem());
        update();
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
}
