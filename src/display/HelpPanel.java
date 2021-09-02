/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import controls.SimConsts;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author Kathryn Andrew
 */
public class HelpPanel extends JComponent {
    
    JLabel contents;
    
    public HelpPanel() {
        
        this.setPreferredSize(new Dimension(200,200));
        this.setLayout(new BorderLayout());
        
        JPanel mainHolder = new JPanel();
        mainHolder.setBorder(BorderFactory.createEtchedBorder());
        mainHolder.setLayout(new BoxLayout(mainHolder, BoxLayout.PAGE_AXIS));
        mainHolder.setPreferredSize(new Dimension(200,200));
        this.add(mainHolder, BorderLayout.CENTER); 
        
        contents = new JLabel("help", SwingConstants.CENTER);
        if (SimConsts.ENABLE_BORDERS)
            contents.setBorder(BorderFactory.createEtchedBorder());
        mainHolder.add(contents);
        
        contents.setText("<html><h1>HELP</h1></html>");
        
        JLabel senseHowTo = new JLabel("help", SwingConstants.CENTER);
        if (SimConsts.ENABLE_BORDERS)
            senseHowTo.setBorder(BorderFactory.createEtchedBorder());
        mainHolder.add(senseHowTo);        
        senseHowTo.setText("<html><h2>Adding or Removing Senses</h2>"
                + "<p>To add or remove a sense, the simulation must be stopped.<p><br> "
                + "<p>To add a new sense, first select the \"senses\" tab. "
                + "Then select the type of sense you wish to add in the top drop down box. Configure the "
                + "sense using the options to the left of the senses tab (these should change with the "
                + "sense type selected.  Finally, add the sense using the \"add sense\" button. The sense "
                + "you have create should then be added to the list of active senses on the right of the "
                + "senses tab.</p><br>"
                + "<p>Senses can be removed by selecting them in the checkbox list on the right of the "
                + "senses tab, then pressing the \"removed selected senses\" button.</p>"
                + "</html>");
        
        JLabel behaviourHowTo = new JLabel("help", SwingConstants.CENTER);
        if (SimConsts.ENABLE_BORDERS)
            behaviourHowTo.setBorder(BorderFactory.createEtchedBorder());
        mainHolder.add(behaviourHowTo);        
        behaviourHowTo.setText("<html><h2>Adding or Removing Behaviors</h2>"
                + "<p>To add or remove a behaviour, the simulation must be stopped.<p><br> "
                + "<p>To add a new behaviour, first select the \"behaviours\" tab. "
                + "Then select the type of behaviour you wish to add in the top drop down box. Configure the "
                + "behaviour using the options to the left of the behaviours tab (these should change with the "
                + "behaviour type selected.  Finally, add the behaviour using the \"add behaviour\" button. The behaviour "
                + "you have create should then be added to the list of active behaviours on the right of the "
                + "behaviours tab.</p><br>"
                + "<p>Behaviours can be removed by selecting them in the checkbox list on the right of the "
                + "behaviours tab, then pressing the \"removed selected behaviours\" button.</p>"
                + "</html>");        
        
        JLabel fieldsHowTo = new JLabel("help", SwingConstants.CENTER);
        if (SimConsts.ENABLE_BORDERS)
            fieldsHowTo.setBorder(BorderFactory.createEtchedBorder());
        mainHolder.add(fieldsHowTo);        
        fieldsHowTo.setText("<html><h2>Adding or Removing Environment Fields</h2>"
                + "<p>To add or remove a field, the simulation must be stopped.<p><br> "
                + "<p>To add a new field, first select the \"Environment\" tab. "
                + "A field must have a unique name, which can be entered in the name text box. "
                + "The field's density and growth rate can be configured using the sliders, and its display "
                + "colour using the colour select button. "
                + "Finally, add the field using the \"add field\" button. The field "
                + "you have create should then be added to the list of current fields on the right of the "
                + "environment tab.</p><br>"
                + "<p>Fields can be removed by selecting them in the checkbox list on the right of the "
                + "environment tab, then pressing the \"removed selected fields\" button.</p>"
                + "</html>");         
    }

    
}
