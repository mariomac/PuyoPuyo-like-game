/*
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * Mario Macias wrote this file. As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy me a beer in return
 * ----------------------------------------------------------------------------
 */
 
package com.mario.puyo;

import java.util.Random;

/**
 * Define game elements
 * Created by Mario Macias Lloret
 * Date: 20-jun-2005
 */
public class Game {
    //Colors of the pieces
    public static final int NONE   = 0;
    public static final int BLUE   = 1;
    public static final int GREEN  = 2;
    public static final int YELLOW = 3;
    public static final int RED    = 4;

    //Game table size
    public static final int TABLE_WIDTH = 6;
    public static final int TABLE_HEIGHT = 12;

    private int score=0;
    private int level=0;
    //score accumulation for a in-game step
    private int scoreAccum=0;

    //data struct with the game table
    private int table[]=new int[TABLE_WIDTH*TABLE_HEIGHT];

    private int toNextLevel=5; //number of groups to make for the next level

    //score bonus
    protected int bonus=0;

    //Game pieces
    private int piece1,piece2;

    //next game pieces
    private int next1,next2;

    private Random rnd=null;

    private int posx1,posy1; //position of the piece 1
    private int posx2,posy2; //position of the piece 1

    private static final int ROTATION = 1; //rotates in inverse clockwise angle (if -1, clockwise)

    //position of the piece 2, relatively of piece 1
    public static final int PR_UP   = 1;
    public static final int PR_DOWN = 3;
    public static final int PR_LEFT = 2;
    public static final int PR_RIGHT= 0;
    private int pos2Rel;

    //init game date
    public Game() {
        rnd=new Random(System.currentTimeMillis());
        piece1=(Math.abs(rnd.nextInt()%4))+1;
        piece2=(Math.abs(rnd.nextInt()%4))+1;
        next1=(Math.abs(rnd.nextInt()%4))+1;
        next2=(Math.abs(rnd.nextInt()%4))+1;
        posx1=TABLE_WIDTH/2-1;
        posx2=posx1+1;
        posy1=posy2=0;
        pos2Rel=PR_RIGHT;
        initTable();
    }

    public int getScoreAccum() {
        return scoreAccum;
    }

    public int getPosx1() {
        return posx1;
    }

    public int getPosy1() {
        return posy1;
    }
    public int getPosx2() {
        return posx2;
    }

    public int getPosy2() {
        return posy2;
    }

    public int getPosRel2() {
        return pos2Rel;
    }

    public void fall() {        
        posy1++; posy2++;
    }

    //before move or rotate pieces, check if the action can be made
    public void moveLeft() {
        if(getPairLeft()>0 && getTableElement(posx1-1,posy1)==NONE && getTableElement(posx2-1,posy2)==NONE) {
            posx1--;
            posx2--;
        }
    }

    public void moveRight() {
        if(getPairRight()<TABLE_WIDTH-1 && getTableElement(posx1+1,posy1)==NONE && getTableElement(posx2+1,posy2)==NONE) {
            posx1++;
            posx2++;
        }
    }
    public void rotate() {
        pos2Rel=(pos2Rel+ROTATION)&3;
        calcNewPos2();
        //Tests if the rotation can be possible
        if(getPairLeft()>=0 && getPairRight()<TABLE_WIDTH && getPairBottom()<TABLE_HEIGHT
           && getTableElement(posx2,posy2)==NONE) {
        } else {
            pos2Rel=(pos2Rel-ROTATION)&3;
            calcNewPos2();
        }
    }

    public void nextPair() {
        piece1=next1; piece2=next2;
        next1=(Math.abs(rnd.nextInt()%4))+1;
        next2=(Math.abs(rnd.nextInt()%4))+1;
        posx1=TABLE_WIDTH/2-1;
        posx2=posx1+1;
        posy1=posy2=0;
        pos2Rel=PR_RIGHT;
    }

    public void initTable() {
        for(int i=0;i<table.length;i++) {
            table[i]=NONE;
        }
    }

    public void setScore(int score) {
        this.score=score;
    }
    public int getScore() {
        return score;
    }

    public int getLevel() {
        return level;
    }

    public void addScore(int points) {
        score+=points;
    }

    public int getTableElement(int x,int y) {
        if(y<0 || y>=TABLE_HEIGHT || x<0 || x>=TABLE_WIDTH) {
            return NONE;
        }
        return table[y*TABLE_WIDTH+x];
    }

    public void setTableElement(int element,int x,int y) {
        if(y<0 || y>TABLE_HEIGHT || x<0 || x>TABLE_WIDTH) {
        } else {
            table[y*TABLE_WIDTH+x]=element;
        }
    }

    public int getPiece1() {
        return piece1;
    }

    public void setPiece1(int piece1) {
        this.piece1 = piece1;
    }

    public int getPiece2() {
        return piece2;
    }

    public void setPiece2(int piece2) {
        this.piece2 = piece2;
    }

    public int getNext1() {
        return next1;
    }

    public void setNext1(int next1) {
        this.next1 = next1;
    }

    public int getNext2() {
        return next2;
    }

    public void setNext2(int next2) {
        this.next2 = next2;
    }

    //gets the top,bottom, left and right coordinates of the pair of pieces, depending his relative position
    public int getPairTop() {
        if(posy1<posy2) {
            return posy1;
        } else {
            return posy2;
        }
    }

    public int getPairBottom() {
        if(posy1>posy2) {
            return posy1;
        } else {
            return posy2;
        }
    }

    public int getPairLeft() {
        if(posx1<posx2) {
            return posx1;
        } else {
            return posx2;
        }
    }
    public int getPairRight() {
        if(posx1>posx2) {
            return posx1;
        } else {
            return posx2;
        }
    }

    //Calculate position of piece 2 relative to piece 1
    private void calcNewPos2() {
        switch(pos2Rel) {
            case Game.PR_RIGHT:
                posx2=posx1+1;
                posy2=posy1;
                break;
            case Game.PR_UP:
                posx2=posx1;
                posy2=posy1-1;
                break;
            case Game.PR_LEFT:
                posx2=posx1-1;
                posy2=posy1;
                break;
            case Game.PR_DOWN:
                posx2=posx1;
                posy2=posy1+1;
                break;
        }
    }

    //if the pair collides with another pieces or the ground
    public boolean collides() {
        if(posy1<TABLE_HEIGHT-1 && getTableElement(posx1,posy1+1)==NONE &&
           posy2<TABLE_HEIGHT-1 && getTableElement(posx2,posy2+1)==NONE ) {
            return false;
        } else {
            return true;
        }
    }

    //lets fall all the pieces in the game table
    //and checks for piece groups
    //called when a number of pieces disappear
    //or when a pair of pieces collides with another pieces
    //returns true if some changes are made
    public boolean adjustTable() {
        scoreAccum=0;
        boolean changes=false;
        for(int y=TABLE_HEIGHT-2;y>=0;y--) {
            for(int x=0;x<TABLE_WIDTH;x++) {
                if(getTableElement(x,y+1)==NONE && getTableElement(x,y)!=NONE) {
                    setTableElement(getTableElement(x,y),x,y+1);
                    setTableElement(NONE,x,y);
                    changes=true;
                }
            }
        }
        //if pieces can't fall, tests for piece groups
        if(!changes) {
            for(int x=0;x<TABLE_WIDTH;x++) {
                for(int y=0;y<TABLE_HEIGHT;y++) {
                    int element=getTableElement(x,y);
                    if(element!=NONE) {
                        initMarkTable();
                        int piezas=markGroups(element,x,y);
                        if(piezas>3) {
                            //tests when upgrade the game level
                            toNextLevel--;
                            if(toNextLevel<=0) {
                                toNextLevel+=5;
                                if(level<10) {
                                    level++;
                                }
                            }
                            bonus++;
                            scoreAccum+=piezas;
                            for(int xx=0;xx<TABLE_WIDTH;xx++) {
                                for(int yy=0;yy<TABLE_HEIGHT;yy++) {
                                    if(mark[xx][yy]) {
                                        mark[xx][yy]=false;
                                        setTableElement(NONE,xx,yy);
                                        changes=true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return changes;
    }

    //auxiliary table used in the piece agrouppations test
    private boolean mark[][]=new boolean[TABLE_WIDTH][TABLE_HEIGHT];
    private void initMarkTable() {
        for(int x=0;x<TABLE_WIDTH;x++) {
            for(int y=0;y<TABLE_HEIGHT;y++) {
                mark[x][y]=false;
            }
        }
    }

    //Recursively, search for piece groups of the same color of element
    //returns the number of pieces in the groups
    private int markGroups(int element,int x, int y) {
        int ret=0;
        if(getTableElement(x,y)==element && !mark[x][y]) {
            mark[x][y]=true;
            ret=1+markGroups(element,x-1,y)+markGroups(element,x+1,y)+markGroups(element,x,y-1)+markGroups(element,x,y+1);
        }
        return ret;
    }
}
