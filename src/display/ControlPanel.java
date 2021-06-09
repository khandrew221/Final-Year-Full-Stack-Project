/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import controls.SimControl;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import simulation.Simulation;

/**
 *
 * @author Kathryn Andrew
 */
public class ControlPanel extends JPanel {
    
    private SimControl simControl;
    private SimMainPanel main;
    private JButton pausePlay;
    private JButton reset;
    
    
    public ControlPanel(SimMainPanel main, SimControl control) {
        this.simControl = control;
        this.main = main;
        
        reset = new JButton("Reset");
        this.add(reset);    
        
        makePausePlayButton();
    }
    
    
    
    private void makePausePlayButton() {
        pausePlay = new JButton("Pause");
        this.add(pausePlay);
        pausePlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (simControl.isRunning()) {
                    simControl.pause();
                    pausePlay.setText("Play");
                } else if(simControl.isPaused()) {
                    simControl.play();
                    pausePlay.setText("Pause");
                }
            }
        }); 
    }
    
}
