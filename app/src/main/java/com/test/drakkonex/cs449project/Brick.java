package com.test.drakkonex.cs449project;

/**
 * Created by Drakkonex on 4/19/2017.
 */

import android.graphics.RectF;

public class Brick {

    private RectF rectF;

    private boolean isVisible;

    public Brick(int row, int column, int width, int height){

        isVisible = true;

        int padding = 1;

        rectF = new RectF(column * width + padding,
                row * height + padding,
                column * width + width - padding,
                row * height + height - padding);
    }

    public RectF getRect(){
        return this.rectF;
    }

    public void setInvisible(){
        isVisible = false;
    }

    public boolean getVisibility(){
        return isVisible;
    }
}