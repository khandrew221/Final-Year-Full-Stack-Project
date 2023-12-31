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

/**
 *
 * @author Kathryn Andrew
 */
public class ControlPanel extends JPanel {
    
    private SimControl simControl;
    private JButton pausePlay;
    private JButton reset;
    private JButton stop;
    
    
    public ControlPanel(SimControl control) {
        this.simControl = control;   
        makePausePlayButton();
        makeStopButton();
        makeResetButton();
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
                } else if(simControl.hasCriticalChange()) {
                    simControl.restart();
                    simControl.play();
                    pausePlay.setText("Pause");
                } else if(simControl.isPaused() || simControl.isStopped()) {
                    simControl.play();
                    pausePlay.setText("Pause");
                } 
            }
        }); 
    }
    
    private void makeResetButton() {
        reset = new JButton("Reset");
        this.add(reset);
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                simControl.restart();
            }
        }); 
    }    
    
    
    public void setPausePlayText() {
        if (simControl.isRunning()) {
            pausePlay.setText("Pause");
        } else if(simControl.isPaused() || simControl.isStopped()) {
            pausePlay.setText("Play");
        }         
    }
    

    private void makeStopButton() {
        stop = new JButton("Stop");
        this.add(stop);
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                simControl.stop();
                setPausePlayText();
            }
        }); 
    }     
}
