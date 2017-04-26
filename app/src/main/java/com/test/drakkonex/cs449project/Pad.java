package com.test.drakkonex.cs449project;

import android.graphics.RectF;

/**
 * Created by Drakkonex on 4/19/2017.
 */

public class Pad {

    //Get the coordinates of the pad object
    private RectF rectF;

    // dimensions of pad
    private float length;
    private float height;

    // X and Y coordinate
    private float x;
    private float y;

    //speed of pad
    private float padSpeed;

    // Pad movement
    public final int STOP = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;

    //current pad movement
    private int moving = STOP;

    public Pad(int sX, int sY){
        //dimensions initialized
        length = 130;
        height = 20;

        //set pad to center
        x = sX / 2;
        y = sY - 20;

        //defines the pad's coordinates
        rectF = new RectF(x,y,x+length,y+height);

        //pixels covered/s
        padSpeed = 350;
    }

    public RectF getRect(){
        return rectF;
    }

    public void setMovementState(int movement){
        moving = movement; // changes pad direction
    }

    public void update(long fps){

        // updates pad to the left
        if (moving == LEFT){
            x = x - padSpeed/fps;
        }

        //updates pad to the right
        if (moving == RIGHT){
            x = x + padSpeed/fps;
        }

        rectF.left = x;
        rectF.right = x + length; // finally marking changed coordinates
    }


}
