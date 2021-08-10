/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

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
        contents.setBorder(BorderFactory.createEtchedBorder());
        contents.setPreferredSize(new Dimension(200,200));
        mainHolder.add(contents);
        
        contents.setText("<html><h1>HELP</h1></html>");
        
    }

    
}
