/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import controls.SimControl;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import simulation.SimStateFacade;

/**
 *
 * @author Kathryn Andrew
 */
public class FieldMaker extends ComponentMaker {
    
    private FieldsSelect fieldsSelect; 
    private FieldsGraphics fieldsGraphics;    
    
    private JTextField nameField;
    private JButton colorChooser;
    private JPanel colorSwatch;
    private JPanel makerPanel;
    
    
    private JPanel fieldsPreview;
    private LabelledSlider growthRateSlider;
    private LabelledSlider densitySlider;
        

    
    public FieldMaker(SimControl simControl, SimStateFacade simFacade) {
        
        super(simControl, simFacade);             
        this.setLayout(new BorderLayout());        
        
        JLabel info = new JLabel();
        info.setText("<html>Fields can be used to represent any scalar valued factor within the environment, such as the amount of food or other resource available in a location." +
                " The values of the field can be sensed at specific points by a bot using an Envirosense.  Resources can be taken from the field and converted into energy by an eat behaviour.");
        this.add(info, BorderLayout.PAGE_END); 
        info.setPreferredSize(new Dimension(600, 100));
        info.setBorder(BorderFactory.createEtchedBorder());
        
        JPanel mainHolder = new JPanel();
        mainHolder.setBorder(BorderFactory.createEtchedBorder());
        this.add(mainHolder);
        
        makerPanel = new JPanel(new GridLayout(6, 1));
        mainHolder.add(makerPanel);

        
        makeNamePanel();
        makeDensityPanel();      
        makeGrowthRateSlider();
        makeColorPanel();
        makeAddComponentButton();

      
                fieldsGraphics = new FieldsGraphics(200, 200, super.getFacade().fieldsReport());
        fieldsSelect= new FieldsSelect(this);
        mainHolder.add(fieldsSelect);
        
        fieldsSelect.setup("Fields", getIDsAndLabels(), true, true);

        
        makeRemoveComponentButton();
        

        mainHolder.add(fieldsGraphics);
        
    }
    
    
    private void makeNamePanel() {
        JPanel namePanel = new JPanel();       
        JLabel nameLabel = new JLabel("Name: ");        
        nameField = new JTextField();
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        nameField.setPreferredSize(new Dimension(100, 25));
        makerPanel.add(namePanel);                
    }
    
    private void makeDensityPanel() {    
        densitySlider = new LabelledSlider("Density", 2, 20, 18, 11, null);
        makerPanel.add(densitySlider);   
        densitySlider.setToolTipText("<html>Controls how close sample points in the scalar field are.<br>"
                + "Lower density leads to smoother gradients in larger patches,"
                + "<br>but less overall material.</html>");
    }
    
    private void makeGrowthRateSlider() {
        growthRateSlider = new LabelledSlider("Growth %", 0, 0.1, 100, 0, null);       
        makerPanel.add(growthRateSlider); 
        growthRateSlider.setToolTipText("<html>Controls how quickly the resource represented by the field will increase over time,<br>"
                + "up to its maximum value.</html>");        
    }    
    
    private void makeColorPanel() {
        JPanel colorPanel = new JPanel();  
        
        colorSwatch = new JPanel();
        colorSwatch.setPreferredSize(new Dimension(25, 25));
        colorSwatch.setBackground(Color.BLUE);
        
        colorChooser = new JButton("Select Colour");
        colorChooser.addActionListener(this);
        
        colorPanel.add(colorSwatch);
        colorPanel.add(colorChooser);
        
        makerPanel.add(colorPanel);               
    }       

    @Override
    void makeAddComponentButton() {
        addComponentButton = new JButton("Add field");
        addComponentButton.addActionListener(this);        
        makerPanel.add(addComponentButton);               
    }  
    
    @Override
    void makeRemoveComponentButton() {
        removeComponentButton = new JButton("Remove fields");
        removeComponentButton.addActionListener(this);        
        makerPanel.add(removeComponentButton);               
    }    
    
    @Override
    public void actionPerformed(ActionEvent e){        
        if (e.getSource().equals(colorChooser)) {
            Color c=JColorChooser.showDialog(this,"Select colour",Color.GREEN);
            if (c != null)
                colorSwatch.setBackground(c);
        } else if (e.getSource().equals(addComponentButton)) {
            if (nameField.getText().isBlank()) {
                JOptionPane.showMessageDialog(null, "Invalid name.");
            } else {
                Color c = colorSwatch.getBackground();
                boolean existingName = super.getControl().addField(nameField.getText().strip(), (int) densitySlider.getValue(), growthRateSlider.getValue()/100.0, c.getRed(), c.getGreen(), c.getBlue());
                if (existingName)
                    JOptionPane.showMessageDialog(null, "A field with this name already exists.");
                else {                 
                    update();
                }
            }            
        } else if (e.getSource().equals(removeComponentButton)) {
            super.getControl().removeFields(fieldsSelect.getSelected());
            update();
        }        
    }
   
    
    /**
     * Allows the FieldsSelect to update the FieldsGraphics
     * @param newActiveFields 
     */
    public void setSelected(Set<String> newActiveFields) {
        fieldsGraphics.setActiveFields(newActiveFields);
    }
    
    /**
     * Updates the whole panel.  Call after change to simulation environment.
     */
    @Override
    public void update() {
        List<Map<String, Object>> fieldsReport = super.getFacade().fieldsReport();
        fieldsSelect.setup("Fields", getIDsAndLabels(), true, false);
        fieldsGraphics.updateData(fieldsReport);
        Set<String> newSelect = fieldsSelect.getSelected();
        newSelect.add(nameField.getText().strip());
        fieldsSelect.setSelected(newSelect);
        this.repaint();
        this.revalidate();    
    }    
    
    /**
     * In this case only calls the standard update.
     */
    @Override
    public void updateAll() {
        update();
    }
    
    
    public Map<String, String> getIDsAndLabels() {
        Map<String, String> out = new HashMap<>();
        for (String entry : super.getFacade().getFields()) {
            out.put(entry, entry);
        }
        return out;
    }
        
}
