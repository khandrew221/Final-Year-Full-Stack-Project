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
    private JButton restart;
    
    
    public ControlPanel(SimMainPanel main, SimControl control) {
        this.simControl = control;
        this.main = main;     
        makePausePlayButton();
        makeRestartButton();
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
                } else if(simControl.isPaused() || simControl.isStopped()) {
                    simControl.play();
                    pausePlay.setText("Pause");
                }
            }
        }); 
    }
    
    private void makeRestartButton() {
        restart = new JButton("Restart");
        this.add(restart);
        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                simControl.restart();
            }
        }); 
    }    
    
}
