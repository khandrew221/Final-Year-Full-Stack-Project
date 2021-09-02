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
public class AboutPanel extends JComponent {
    
    JLabel contents;
    
    public AboutPanel() {
        
        this.setLayout(new BorderLayout());
        
        JPanel mainHolder = new JPanel();
        if (SimConsts.ENABLE_BORDERS)
            mainHolder.setBorder(BorderFactory.createEtchedBorder());        

        
        mainHolder.setLayout(new BoxLayout(mainHolder, BoxLayout.PAGE_AXIS));
        mainHolder.setPreferredSize(new Dimension(200,200));
        this.add(mainHolder, BorderLayout.CENTER); 
        
        contents = new JLabel("help", SwingConstants.CENTER);
        if (SimConsts.ENABLE_BORDERS)
            contents.setBorder(BorderFactory.createEtchedBorder());
        mainHolder.add(contents);
        
        contents.setText("<html><h1>About</h1></html>");
       
        JLabel about = new JLabel("", SwingConstants.CENTER);
        if (SimConsts.ENABLE_BORDERS)
            about.setBorder(BorderFactory.createEtchedBorder());
        mainHolder.add(about);        
        about.setText("<html>"
                + "This program runs simulations to demonstrate the evolution of behaviour in simple creatures (\"bots\"), "
                + "driven by genetically determined neural networks.  The simulation consists of three parts: bots, "
                + "their environment, and the reproductive process."
                + "<h3>Bots</h3>"
                + "<p>Each bot is equipped with three things: a set of senses, through which it can perceive its "
                + "environment, a set of behaviours, which govern how the performs actions like moving or eating, and "
                + "a neural network \"brain\". The structure of this brain is determined by the bot's genetics, and "
                + "its role is to take inputs from the senses and transform them into outputs that govern the behaviours. "
                + "At the beginning of a simulation, the starting bots' brains are wired randomly and therefore won't "
                + "produce much by way of intelligent responses to their environment. However, over many "
                + "generations of breeding, brains that produce intelligent responses to the environment evolve.</p><br>"
                + "<p>Bots also have an internal store of energy, which is slowly depleted by behaviours and once every "
                + "cycle to keep them alive. Some energy can be restored by eating, depending on the amount of food "
                + "available at the bot's location. When a bot reaches 0 energy, it dies and is removed from the population."
                + "<h3>Environment</h3>"
                + "The environment the bots exist in consists of \"fields\", each of which represent varied amounts of "
                + "a resource (e.g. food) or feature present at all points in the environment. The environment is also "
                + "bounded, with hard borders at its edges; getting bots to avoid collision with these boundaries is one "
                + "possible selection criteria."
                + "<h3>Reproduction</h3>"
                + "<p>The reproductive process is how generations of bots gradually refine the structure of their brains "
                + "to produce appropriate responses to their environment. If the population of the simulation is under a "
                + "maximum population threshold, a new bot will be bred from two parents and added to the simulation. "
                + "Parents will be selected randomly, but using a process that makes bots with a higher fitness score more "
                + "likely to breed (specifically, using <i>fitness proportionate selection</i>). The genetic code of the child "
                + "will be a mix of the parent's genetic codes (via <i>uniform crossover</i>). Any two bots in the population "
                + "may produce a child, no matter their respective location, and the child will be placed randomly within the "
                + "environment.</p><br>"     
                + "<p>The driver for evolution in this simulation is a bot's fitness score, which is constantly updated based on "
                + "how well the bot has performed over its lifetime against various criteria. How the fitness score is "
                + "calculated can be changed to select for particular traits. A graph of the average fitness of the entire population "
                + "over time is located underneath the main simulation view.</p>"
                + "</html>");        
    }        
}
