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
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

/**
 * Controls the game state and the player controls
 * Creado por Mario Macias Lloret
 * Date: 20-jun-2005
 */
public class GameControl implements KeyListener {
    //if cursor down button is pressed
    private boolean dir_down=false;

    private boolean exitGame=false;

    private final static int MAX_FALL_TIME          = 1150;   //the time that we stand for the next falling of piece
    private final static int LEVEL_FALL_TIME_DEC    = 100;    //the decrement of the fall time between levels
    private final static int FAST_FALLING_TIME      = 70;

    private JComponent gameCanvas=null;

    //game state
    public static final int STATE_DRIVING          = 0; //The player drives the pair of pieces
    public static final int STATE_ADJUST_TABLE     = 1; //All the pieces in the game table are falling
    public static final int STATE_GAMEOVER         = 2;
    private int gameState=STATE_GAMEOVER;

    //next time when pieces will fall
    long nextFall = 0;

    private Game game=new Game();

    // Info messages
    private static final int TIME_MESSAGE= 1600;
    private String message=null;
    private String alt= null;
    private long timeMessage=0;

    public int getGameState() {
        return gameState;
    }

    public void setCanvas(JComponent c) {
        gameCanvas=c;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_SPACE:
                if(gameState==STATE_GAMEOVER) {
                    gameState=STATE_DRIVING;
                    message=alt=null;
                    nextFall=System.currentTimeMillis()+MAX_FALL_TIME;
                    game=new Game();
                }
                break;
            case KeyEvent.VK_DOWN:
                if(!dir_down) {
                    if(gameState==STATE_DRIVING) {
                        nextFall=System.currentTimeMillis()+LEVEL_FALL_TIME_DEC;
                    }
                    dir_down=true;
                }
                break;
            case KeyEvent.VK_LEFT:
                game.moveLeft();
                break;
            case KeyEvent.VK_RIGHT:
                game.moveRight();
                break;
            case KeyEvent.VK_UP:
                game.rotate();
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                //nextFall=System.currentTimeMillis();
                dir_down=false;
                break;
            case KeyEvent.VK_LEFT:
                break;
            case KeyEvent.VK_RIGHT:
                break;
            case KeyEvent.VK_UP:
                break;
        }
    }

    public void keyTyped(KeyEvent e) {

    }

    public boolean isDirDown() {
        return dir_down;
    }

    public void bucle() {
        int sca=0;
        while(!exitGame) {
            long time=System.currentTimeMillis();
            Thread.yield();
            if(gameState==STATE_GAMEOVER) {
                sca=0;
                message="Push";
                alt="space bar";
                timeMessage=System.currentTimeMillis()+1000;
            } else if(time>nextFall) { //Tests if the pieces must fall one position
                if(time>timeMessage) {
                    message=null;
                    sca=0;
                }
                if(gameState==STATE_DRIVING) {
                    //if the player is driving the pieces, checks if collides with another pieces
                    //and acts in consequence
                    if(!game.collides()) {
                        game.fall();
                        if(dir_down) {
                            nextFall+=FAST_FALLING_TIME;
                        } else {
                            nextFall+=MAX_FALL_TIME-game.getLevel()*LEVEL_FALL_TIME_DEC;
                        }
                    } else {
                        game.setTableElement(game.getPiece1(),game.getPosx1(),game.getPosy1());
                        game.setTableElement(game.getPiece2(),game.getPosx2(),game.getPosy2());
                        game.nextPair();

                        nextFall+=FAST_FALLING_TIME;
                        gameState=STATE_ADJUST_TABLE;
                    }
                } else {
                    //Let's fall the pieces in the table and checks if groups of pieces are formed
                    if(game.adjustTable()) {
                        int ft1=MAX_FALL_TIME-LEVEL_FALL_TIME_DEC*game.getLevel();
                        int ft2=FAST_FALLING_TIME*5;
                        if(ft1>ft2) {
                            nextFall+=ft2;
                        } else {
                            nextFall+=ft1;
                        }
                        sca+=game.getScoreAccum()*game.bonus;
                        if(sca>0) {
                            int sc=game.getScore()+game.getScoreAccum()*game.bonus;
                            game.setScore(sc);
                            String msg="+"+sca+"!!";
                            String alt=null;
                            if(game.bonus>1) {
                                alt=""+game.bonus+"x combo";
                            }
                            setMessage(msg,alt);
                            //game.bonus++;
                        }
                    } else {
                        if(game.getTableElement(game.getPosx1(),game.getPosy1()) != Game.NONE ||
                                game.getTableElement(game.getPosx2(),game.getPosy2()) != Game.NONE ) {
                            gameState=STATE_GAMEOVER;
                        } else {

                            if(dir_down) {
                                nextFall+=FAST_FALLING_TIME;
                            } else {
                                nextFall+=MAX_FALL_TIME-game.getLevel()*LEVEL_FALL_TIME_DEC;
                            }
                            gameState=STATE_DRIVING;
                        }
                        game.bonus=0;
                    }
                }
            }
            gameCanvas.repaint();
        }
    }

    public boolean isExitGame() {
        return exitGame;
    }

    public void setExitGame(boolean exitGame) {
        this.exitGame = exitGame;
    }

    private void setMessage(String msg,String alt) {
        message=msg;
        this.alt=alt;
        timeMessage=System.currentTimeMillis()+TIME_MESSAGE;
    }

    public String getMessage() {
        return message;
    }
    public String getAltMessage() {
        return alt;
    }
}
