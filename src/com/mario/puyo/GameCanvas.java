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
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Draws the game scene
 * Created by Mario Macias Lloret
 * Date: 20-jun-2005
 */
public class GameCanvas extends JComponent {
    public static final int FRAME_WIDTH=340;
    public static final int FRAME_HEIGHT=480;

    public static final int TILE_SIZE = 32;

    private Font fontsmall=new Font(null,0,15);
    private Font fontmedium=new Font(null,0,25);
    private Font fontlarge=new Font(null,0,35);

    private GameControl gameControl=null;
    //Background image for buffering
    private BufferedImage imagen=null;
    Graphics2D gi;

    //images of the game pieces
    private static Image IMG_RED=null;
    private static Image IMG_BLUE=null;
    private static Image IMG_GREEN=null;
    private static Image IMG_YELLOW=null;

    protected GameCanvas() {}
    public GameCanvas(GameControl gcont) {
        super();
        this.gameControl=gcont;
        imagen=new BufferedImage(FRAME_WIDTH,FRAME_HEIGHT,BufferedImage.TYPE_INT_RGB);
        gi=imagen.createGraphics();
        //Initialize the background image
        for(int y=0;y<FRAME_HEIGHT;y++) {
            float t=1.0f-((float)y/(float)FRAME_HEIGHT);
            gi.setColor(new Color(0.5f,0.5f,t));
            gi.drawLine(0,y,FRAME_WIDTH,y);
        }

        gi.setColor(Color.black);
        gi.setFont(fontsmall);
        gi.drawString("Score",(Game.TABLE_WIDTH+1)*TILE_SIZE+5,25);
        gi.drawString("Level",(Game.TABLE_WIDTH+1)*TILE_SIZE+5,95);
        gi.drawString("Next",(Game.TABLE_WIDTH+1)*TILE_SIZE+5,205);

        //initialize piece images
        try {
            IMG_RED=ImageIO.read(new File("res/puyo_red.png"));
            IMG_GREEN=ImageIO.read(new File("res/puyo_green.png"));
            IMG_BLUE=ImageIO.read(new File("res/puyo_blue.png"));
            IMG_YELLOW=ImageIO.read(new File("res/puyo_yellow.png"));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    //refresh screen
    public void paintComponent(Graphics g) {
        Game game=gameControl.getGame();

        //draws game table background
        for(int y=0;y<Game.TABLE_HEIGHT*TILE_SIZE;y++) {
            float t=1.0f-((float)y/(float)FRAME_HEIGHT);
            gi.setColor(new Color(t,t,t));
            gi.drawLine(0,y,Game.TABLE_WIDTH*TILE_SIZE,y);
        }

        //draws score, level and next pair
        gi.setColor(Color.WHITE);
        gi.fillRect((Game.TABLE_WIDTH+1)*TILE_SIZE,30,100,30);
        gi.fillRect((Game.TABLE_WIDTH+1)*TILE_SIZE,100,50,30);
        gi.fillRect((Game.TABLE_WIDTH+1)*TILE_SIZE,210,70,35);
        gi.setColor(Color.BLACK);

        gi.setFont(fontmedium);
        gi.drawString(Integer.toString(game.getScore()),(Game.TABLE_WIDTH+1)*TILE_SIZE+5,55);
        gi.drawString(Integer.toString(game.getLevel()),(Game.TABLE_WIDTH+1)*TILE_SIZE+5,125);
        gi.drawImage(getPieceImage(game.getNext1()),(Game.TABLE_WIDTH+1)*TILE_SIZE+3,213,null);
        gi.drawImage(getPieceImage(game.getNext2()),(Game.TABLE_WIDTH+2)*TILE_SIZE+3,213,null);

        //draws game table (all pieces)
        for(int x=0;x<Game.TABLE_WIDTH;x++) {
            for(int y=0;y<Game.TABLE_HEIGHT;y++) {
                int element=game.getTableElement(x,y);
                if(element!=Game.NONE) {
                    gi.drawImage(getPieceImage(element),x*TILE_SIZE,y*TILE_SIZE,null);
                }
            }
        }

        //draws in-game messages
        String msg=gameControl.getMessage();
        String alt=gameControl.getAltMessage();
        if(msg!=null) {
            long r=System.currentTimeMillis();
            gi.setColor(new Color((int)(r&255),(int)((r*r)&255),(int)((r*r*r)&255)));

            gi.setFont(fontlarge);
            int x=TILE_SIZE*3+(int)(r%10);
            int y=TILE_SIZE*5+(int)((r*r)%10);
            gi.drawString(gameControl.getMessage(),x,y);

            if(alt!=null) {
                x=TILE_SIZE/2+(int)((r+x)%10);
                y=TILE_SIZE*6+(int)((r+y)%10);
                gi.setFont(fontmedium);
                gi.drawString(gameControl.getAltMessage(),x,y);
            }


        }

        if(gameControl.getGameState()!=GameControl.STATE_GAMEOVER) {
            //draws current controlled pieces
            gi.drawImage(getPieceImage(game.getPiece1()),game.getPosx1()*TILE_SIZE,game.getPosy1()*TILE_SIZE,null);
            gi.drawImage(getPieceImage(game.getPiece2()),game.getPosx2()*TILE_SIZE,game.getPosy2()*TILE_SIZE,null);
        }

        g.drawImage(imagen,0,0,null);
    }

    private Image getPieceImage(int piece) {
        switch(piece) {
            case Game.RED:
                return IMG_RED;
            case Game.BLUE:
                return IMG_BLUE;
            case Game.GREEN:
                return IMG_GREEN;
            case Game.YELLOW:
                return IMG_YELLOW;
            default:
                return null;
        }
    }

}
