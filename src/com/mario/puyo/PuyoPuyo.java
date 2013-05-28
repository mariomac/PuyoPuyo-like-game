/*
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * Mario Macias wrote this file. As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy me a beer in return
 * ----------------------------------------------------------------------------
 */
 
package com.mario.puyo;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Mario Macias Lloret
 * Date: 20-jun-2005
 */
public class PuyoPuyo {
    public static JFrame frame;
    private static GameCanvas gameCanvas;
    private static GameControl gameControl;

    public static void main(String []args) {
        gameControl=new GameControl();
        frame=new JFrame("Puyo-Puyo");
        frame.addKeyListener(gameControl);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100,100,GameCanvas.FRAME_WIDTH,GameCanvas.FRAME_HEIGHT);
        gameCanvas=new GameCanvas(gameControl);
        gameControl.setCanvas(gameCanvas);
        frame.getContentPane().add(gameCanvas);
        frame.show();
        gameControl.bucle();

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                gameControl.setExitGame(true);
            }
        });
    }

}
