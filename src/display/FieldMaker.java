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
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import simulation.SimStateFacade;

/**
 *
 * @author Kathryn Andrew
 */
public class FieldMaker extends ComponentMaker {
    
    private FieldsSelect fieldsSelect; 
    private FieldsGraphics fieldsGraphics;    
    
    JTextField nameField;
    JSlider densitySlider;
    JLabel densityLabel; 
    JButton colorChooser;
    JPanel colorSwatch;
    JPanel makerPanel;
    
    JPanel fieldsPreview;
    

    
    public FieldMaker(SimControl simControl, SimStateFacade simFacade) {
        
        super(simControl, simFacade);
              
        makerPanel = new JPanel(new GridLayout(5, 1));
        this.add(makerPanel);
        makeNamePanel();
        makeDensityPanel();        
        makeColorPanel();
        makeAddComponentButton();
        
       
        fieldsGraphics = new FieldsGraphics(200, 200, super.getFacade().fieldsReport());
        fieldsSelect= new FieldsSelect(this);
        this.add(fieldsSelect);
        fieldsSelect.setup(super.getFacade().fieldsReport(), true, true);
        makeRemoveComponentButton();
        
        this.add(fieldsGraphics);
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
        JPanel densityPanel = new JPanel(new BorderLayout());            
        densityPanel.setPreferredSize(new Dimension(300, 50));
        
        densitySlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 11);
        densitySlider.setMajorTickSpacing(20);
        densitySlider.setPaintTicks(true);
        densitySlider.setPaintLabels(true);
        
        densitySlider.addChangeListener(new ChangeListener() {
          @Override
          public void stateChanged(ChangeEvent event) {
            if (densitySlider.getValue() < 2) {
                densitySlider.setValue(2);
            }
            densityLabel.setText("Density: " + densitySlider.getValue());
          }
        });
                
        densityLabel = new JLabel("Density: " + densitySlider.getValue());   
        
        densityPanel.add(densityLabel, BorderLayout.WEST);
        densityPanel.add(densitySlider, BorderLayout.EAST);
        makerPanel.add(densityPanel);        
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
                boolean existingName = super.getControl().addField(nameField.getText().strip(), densitySlider.getValue(), c.getRed(), c.getGreen(), c.getBlue());
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
    public void update() {
        List<Map<String, Object>> fieldsReport = super.getFacade().fieldsReport();
        fieldsSelect.setup(fieldsReport, true, false);
        fieldsGraphics.updateData(fieldsReport);
        Set<String> newSelect = fieldsSelect.getSelected();
        newSelect.add(nameField.getText().strip());
        fieldsSelect.setSelected(newSelect);
        this.repaint();
        this.revalidate();    
    }    
        
}