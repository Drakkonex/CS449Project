package com.test.drakkonex.cs449project;

/**
 * Created by Drakkonex on 4/19/2017.
 */

import android.graphics.RectF;

import java.util.Random;

public class Ball {
    RectF rectF;
    float xVel;
    float yVel;
    float width = 10;
    float height = 10;
    // creates the variables for the ball

    public Ball(){

        xVel = 200;
        yVel = -400;
        //ball goes straight up to start

        rectF = new RectF();
    }

    public RectF getRect(){
        return rectF;
    }

    public void update(long fps){
        rectF.left = rectF.left + (xVel/fps);
        rectF.top = rectF.top + (yVel/fps);
        rectF.right = rectF.left + width;
        rectF.bottom = rectF.top - height;
    }

    public void setRandomXVelocity(){
        Random generator = new Random();
        int answer = generator.nextInt(2);

        if(answer == 0){
            reverseXVelocity();
        }
    }

    public void reverseXVelocity(){
        xVel = -xVel;
    }

    public void reverseYVelocity(){
        yVel = -yVel;
    }

    public void clearObstacleY(float y){
        rectF.bottom = y;
        rectF.top = y - height;
    }

    public void clearObstacleX(float x){
        rectF.left = x;
        rectF.right = x + width;
    }

    public void reset(int x, int y){
        rectF.left = x / 2;
        rectF.top = y - 20;
        rectF.right = x / 2 + width;
        rectF.bottom = y - 20 - height;
    }

}
