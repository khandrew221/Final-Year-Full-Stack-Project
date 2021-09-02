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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import simulation.SimStateFacade;

/**
 *
 * @author Kathryn Andrew
 */
public class SenseMaker extends ComponentMaker {
    
    private SensesSelect sensesSelect; 
    
    private JPanel typeSpecificMakerPanel;
    private JPanel senseSelectHolder;
   
    private JComboBox typeSelector;
    private JComboBox fieldSelector;
    private JCheckBox envCentred = new JCheckBox("Include point at bot location?");
    
    private LabelledSlider borderRadiusSlider;
    private LabelledSlider envRadiusSlider;
    private LabelledSlider envRingsSlider;   
    private LabelledSlider envPointsSlider; 
    
    private SamplePointsPreview preview;
    
    private JLabel info;
    private JPanel typeSelectorPanel;
    

    /**
     * 
     * @param simControl
     * @param simFacade 
     */
  
    public SenseMaker(SimControl simControl, SimStateFacade simFacade) {
        
        super(simControl, simFacade);
        this.setLayout(new BorderLayout());        
        
        info = new JLabel();
        info.setText("<html>Senses provide inputs to a bot's neural network, and allow it to percieve its environment.</html>");
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
        
        senseSelectHolder = new JPanel();
        senseSelectHolder.setLayout(new BoxLayout(senseSelectHolder, BoxLayout.PAGE_AXIS));
        if (SimConsts.ENABLE_BORDERS)
            senseSelectHolder.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        sensesSelect = new SensesSelect(this);
        sensesSelect.setup("Senses", getIDsAndLabels(), true, true, 400);    
        senseSelectHolder.add(sensesSelect);
        makeRemoveComponentButton();
        rightPanel.add(senseSelectHolder);
        
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
        typeSelectorPanel.add(typeSelector);  
    }
    
    private void makeTypeSelectorPanel() {
        typeSelectorPanel = new JPanel();
        typeSelectorPanel.setBorder(BorderFactory.createEtchedBorder());
        this.add(typeSelectorPanel, BorderLayout.PAGE_START); 
        typeSelectorPanel.add(new JLabel("Select sense to create:")); 
        makeTypeSelector();        
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
                int error = super.getControl().addSenseEnviro(target, envCentred.isSelected(), (int) envRingsSlider.getValue(), (int) envPointsSlider.getValue(), (int) envRadiusSlider.getValue());
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
                    case 4:
                        JOptionPane.showMessageDialog(null, "Invalid parameters passed to sense. This message indicates a major interface error.", "Warning", JOptionPane.WARNING_MESSAGE);
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
                    case 4:
                        JOptionPane.showMessageDialog(null, "Invalid parameters passed to sense. This message indicates a major interface error.", "Warning", JOptionPane.WARNING_MESSAGE);
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
        typeSpecificMakerPanel.setPreferredSize(new Dimension(400,455));
        
        JPanel selectorPanel = new JPanel(new BorderLayout()); 
        JLabel selectorLabel = new JLabel("Target field: ");
        selectorPanel.add(selectorLabel, BorderLayout.WEST);
        selectorPanel.add(fieldSelector, BorderLayout.EAST);
        typeSpecificMakerPanel.add(selectorPanel); 
        
        JPanel centredPanel = new JPanel(new BorderLayout()); 
        centredPanel.add(envCentred);
        typeSpecificMakerPanel.add(centredPanel);       
              
        
        int ringsMin = (int) Math.ceil(super.getFacade().getSenseEnviroRingsMin());
        int ringsMax = (int) Math.floor(super.getFacade().getSenseEnviroRingsMax());  
        envRingsSlider = new LabelledSlider("","Rings", ringsMin, ringsMax, ringsMax-ringsMin, 0, this);
        envRingsSlider.setToolTipText("<html>Sets the number of rings of sample points.</html>");
        typeSpecificMakerPanel.add(envRingsSlider);     

        int pprMin = (int) Math.ceil(super.getFacade().getSenseEnviroPointsPerRingMin());
        int pprMax = (int) Math.floor(super.getFacade().getSenseEnviroPointsPerRingMax()); 
        envPointsSlider = new LabelledSlider("","Points per ring", pprMin, pprMax, pprMax-pprMin, 0, this);
        envPointsSlider.setToolTipText("<html>Sets the number of sample points per ring.</html>");
        typeSpecificMakerPanel.add(envPointsSlider); 
        
        int radiusMin = (int) Math.ceil(super.getFacade().getSenseEnviroRadiusMin());
        int radiusMax = (int) Math.floor(super.getFacade().getSenseEnviroRadiusMax());       
        envRadiusSlider = new LabelledSlider("","Ring radius", radiusMin, radiusMax, radiusMax-radiusMin, 0, this);
        envRadiusSlider.setToolTipText("<html>Sets the distance between sample point rings.</html>");
        typeSpecificMakerPanel.add(envRadiusSlider);    
        
        List<Double> x = new ArrayList<>();
        List<Double> y = new ArrayList<>();
        super.getControl().getEnviroSamplePoints(x, y,  envCentred.isSelected(), (int) envRingsSlider.getValue(), (int) envPointsSlider.getValue(), (int) envRadiusSlider.getValue());
        preview = new SamplePointsPreview(200, 200, x, y);
        typeSpecificMakerPanel.add(preview);  
          
        makeAddComponentButton();
        
        info.setText("<html>An environment sense allows bots to percieve their surroundings by sampling the values of a target "
                + "environment field at fixed points relative to themselves. Each sample point represents a single input to the "
                + "bot's neural network.</html>");
        
        this.repaint();
    }    
    
    /**
     * 
     */    
    private void setForBorderSense() {
        typeSpecificMakerPanel.removeAll();   
        typeSpecificMakerPanel.setPreferredSize(new Dimension (400,210));
        
        int radiusMin = (int) Math.ceil(super.getFacade().getSenseBorderRadiusMin());
        int radiusMax = (int) Math.floor(super.getFacade().getSenseBorderRadiusMax());
        
        borderRadiusSlider = new LabelledSlider("", "radius", radiusMin, radiusMax, radiusMax-radiusMin, 0, this);   
        borderRadiusSlider.setToolTipText("<html>Sets the distance at which the bot will detect the border.</html>");
        typeSpecificMakerPanel.add(borderRadiusSlider);      
        info.setText("<html>A border sense alerts the bot that it is within a given distance of the environment border. It produces "
                + "a strong input if any of its sample points fall outside the environment bounds, and a zero input otherwise.</html>");
        List<Double> x = new ArrayList<>();
        List<Double> y = new ArrayList<>();
        super.getControl().getBorderSamplePoints(x, y, borderRadiusSlider.getValue());
        preview = new SamplePointsPreview(110, 110, x, y);
        preview.setAlignmentX(Component.CENTER_ALIGNMENT);
        typeSpecificMakerPanel.add(preview);  
        makeAddComponentButton();
        this.repaint();
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
        sensesSelect.setup("Senses", getIDsAndLabels(), true, false, 400);
        updatePreview();
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
        addComponentButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        typeSpecificMakerPanel.add(addComponentButton);               
    }  
    
    @Override
    void makeRemoveComponentButton() {
        removeComponentButton = new JButton("Remove selected senses");
        removeComponentButton.addActionListener(this);  
        removeComponentButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        senseSelectHolder.add(removeComponentButton);               
    }


    public Map<String, String> getIDsAndLabels() {
       Map<String, String> out = (Map<String, String>) super.getFacade().senseReport();  
       return out;    
    }    
    
    private void updatePreview() {
        String type = (String) typeSelector.getSelectedItem();
        List<Double> x = new ArrayList<>();
        List<Double> y = new ArrayList<>();
        switch(type)
        {
            case "environment": {
                super.getControl().getEnviroSamplePoints(x, y,  envCentred.isSelected(), (int) envRingsSlider.getValue(), (int) envPointsSlider.getValue(), (int) envRadiusSlider.getValue());
                preview.setPoints(x, y);                  
                break;
            }
            case "border": {
                super.getControl().getBorderSamplePoints(x, y, borderRadiusSlider.getValue());
                preview.setPoints(x, y);
                break;                          
            }
        }                
    }
    
}
