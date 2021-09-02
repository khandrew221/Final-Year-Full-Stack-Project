/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import controls.SimConsts;
import controls.SimControl;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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
    
    private JPanel typeSpecificMakerPanel;
    private JPanel behaviourSelectHolder;
   
    private JComboBox typeSelector; 
    private JComboBox fieldSelector;
    
    private LabelledSlider maxSpeedSlider;  
    private LabelledSlider selectForageEfficiency;
    private LabelledSlider selectEnergyEfficiency;
    
    private JLabel info;
    private JPanel typeSelectorPanel;

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
        info.setPreferredSize(new Dimension(100, 100));
        info.setBorder(BorderFactory.createEtchedBorder());
        
        makeTypeSelectorPanel();
        
        JPanel mainHolder = new JPanel();
        mainHolder.setBorder(BorderFactory.createEtchedBorder());
        mainHolder.setLayout(new GridLayout(1,2));        
        this.add(mainHolder); 
        
        JPanel leftPanel = new JPanel();        
        if (SimConsts.ENABLE_BORDERS)
            leftPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        mainHolder.add(leftPanel);        
        JPanel rightPanel = new JPanel();
        if (SimConsts.ENABLE_BORDERS)
            rightPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        mainHolder.add(rightPanel);        
              
        makeFieldSelector();
        
        typeSpecificMakerPanel = new JPanel();
        typeSpecificMakerPanel.setLayout(new BoxLayout(typeSpecificMakerPanel, BoxLayout.PAGE_AXIS));
        if (SimConsts.ENABLE_BORDERS)
            typeSpecificMakerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        leftPanel.add(typeSpecificMakerPanel);
       
        
        behaviourSelectHolder = new JPanel();
        behaviourSelectHolder.setLayout(new BoxLayout(behaviourSelectHolder, BoxLayout.PAGE_AXIS));
        if (SimConsts.ENABLE_BORDERS)
            behaviourSelectHolder.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        behavioursSelect = new BehavioursSelect(this);
        behavioursSelect.setup("Behaviours", getIDsAndLabels(), true, true, 400);       
        behaviourSelectHolder.add(behavioursSelect);
        makeRemoveComponentButton();
        rightPanel.add(behaviourSelectHolder);

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
        typeSelectorPanel.add(typeSelector);  
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
                    case 4:
                        JOptionPane.showMessageDialog(null, "Invalid parameters passed to behaviour. This message indicates a major interface error.", "Warning", JOptionPane.WARNING_MESSAGE);
                        break;                           
                }  
                break;
            case "eat":
                String target = (String) fieldSelector.getSelectedItem();
                error = super.getControl().addBehaviourEat(selectForageEfficiency.getValue()/100.0, selectEnergyEfficiency.getValue()/100.0, target);
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
                    case 4:
                        JOptionPane.showMessageDialog(null, "Invalid parameters passed to behaviour. This message indicates a major interface error.", "Warning", JOptionPane.WARNING_MESSAGE);
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
        typeSpecificMakerPanel.setPreferredSize(new Dimension(400,100));
        int speedMin = (int) Math.ceil(super.getFacade().getBehaviourMoveSpeedMin());
        int speedMax = (int) Math.floor(super.getFacade().getBehaviourMoveSpeedMax());
        maxSpeedSlider = new LabelledSlider("", "Maximum speed", speedMin, speedMax, speedMax-speedMin, 0, this);
        typeSpecificMakerPanel.add(maxSpeedSlider);
        makeAddComponentButton();
        info.setText("<html>A move behaviour allows the bot to move in response to its sensory inputs. Bots cannot move outside of the bounds of the environment.</html>");
        this.repaint();    
    }     
    
    /**
     * 
     */    
    private void setForBehaviourEat() {
        typeSpecificMakerPanel.removeAll();
        typeSpecificMakerPanel.setPreferredSize(new Dimension(400,200));
        
        JPanel selectorPanel = new JPanel(new BorderLayout()); 
        JLabel selectorLabel = new JLabel("Target field: ");
        selectorPanel.setPreferredSize(new Dimension(350, 50));
        selectorPanel.add(selectorLabel, BorderLayout.WEST);
        selectorPanel.add(fieldSelector, BorderLayout.EAST);
        typeSpecificMakerPanel.add(selectorPanel); 
        
        //multiply by 100 to convert to %
        int forageEfficiencyMin = (int) Math.ceil(super.getFacade().getBehaviourEatForageEfficiencyMin()) * 100;
        int forageEfficiencyMax = (int) Math.floor(super.getFacade().getBehaviourEatForageEfficiencyMax()) * 100;
        int energyEfficiencyMin = (int) Math.ceil(super.getFacade().getBehaviourEatEnergyEfficiencyMin()) * 100;
        int energyEfficiencyMax = (int) Math.floor(super.getFacade().getBehaviourEatEnergyEfficiencyMax()) * 100;        
        
        selectForageEfficiency = new LabelledSlider("", "Forage efficiency (%)", forageEfficiencyMin, forageEfficiencyMax, (forageEfficiencyMax-forageEfficiencyMin), (forageEfficiencyMax-forageEfficiencyMin)/2, null);
        selectForageEfficiency .setToolTipText("<html>Controls how much of the resource the bot will consume in a single round.<br>"
                + "Negative values will instead cause the bot to desposit the resource in the environment.</html>");      
        typeSpecificMakerPanel.add(selectForageEfficiency); 
        selectEnergyEfficiency = new LabelledSlider("", "Energy efficiency (%)", energyEfficiencyMin, energyEfficiencyMax, (energyEfficiencyMax-energyEfficiencyMin), (energyEfficiencyMax-energyEfficiencyMin)/2, null);
        selectEnergyEfficiency.setToolTipText("<html>Controls what proportion of the resource consumed will be converted to energy.</html>");            
        typeSpecificMakerPanel.add(selectEnergyEfficiency); 
        makeAddComponentButton();
        info.setText("<html>An eat behaviour allows the bot to interact with an environment field by consuming or excreting resources. Bots will only eat if the behaviour recieves a neural network output over a certain threshold.<br> "
                + "Two positive values for forage and energy efficiency will cause the bot to gain energy from consuming the resource.<br>"
                + "A negative forage and a positive energy efficiency will cause the bot to lose energy from depositing the resource.<br>"
                + "A positive forage and a negative energy efficiency will cause the bot to lose energy from consuming the resource.<br>"
                + "Two negative values for forage and energy efficiency will cause the bot to gain energy from depositing the resource.</html>");
        this.repaint();         
    }    
    
    
    private void makeTypeSelectorPanel() {
        typeSelectorPanel = new JPanel();
        typeSelectorPanel.setBorder(BorderFactory.createEtchedBorder());
        this.add(typeSelectorPanel, BorderLayout.PAGE_START); 
        typeSelectorPanel.add(new JLabel("Select behaviour to create:")); 
        makeTypeSelector();        
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
        behavioursSelect.setup("Behaviours", getIDsAndLabels(), true, false, 400);
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
        addComponentButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        typeSpecificMakerPanel.add(addComponentButton);               
    }  
    
    @Override
    void makeRemoveComponentButton() {
        removeComponentButton = new JButton("Remove selected behaviours");
        removeComponentButton.addActionListener(this);  
        removeComponentButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        behaviourSelectHolder.add(removeComponentButton);               
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
