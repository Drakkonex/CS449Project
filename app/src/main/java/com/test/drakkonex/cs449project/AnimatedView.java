package com.test.drakkonex.cs449project;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class AnimatedView extends ImageView{
    private Context mContext;
    int x = -1; // change
    int y = -1;
    static int z=0;
    private static int xVelocity = 100;
    private static int yVelocity = 50;
    private Handler h;
    private final int FRAME_RATE = 30;

    public AnimatedView(Context context, AttributeSet attrs)  {
        super(context, attrs);
        mContext = context;
        h = new Handler();
    }
    private Runnable r = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    public boolean onTouchEvent(MotionEvent event)
    {

        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN: {

            }
            break;

            case MotionEvent.ACTION_MOVE:
            {
                x=(int)event.getX();
                y=(int)event.getY();

                invalidate();
            }

            break;
            case MotionEvent.ACTION_UP:

                x=(int)event.getX();
                y=(int)event.getY();
                System.out.println(".................."+x+"......"+y); //x= 345 y=530
                invalidate();
                break;
        }
        return true;
    }
    protected void onDraw(Canvas c) {

        BitmapDrawable ball = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.pilka);
        if (x<0 && y <0) {
            x = this.getWidth()/2;
            y = this.getHeight()/2;
        } else {
            x += xVelocity;
            y += yVelocity;
            if ((x > this.getWidth() - ball.getBitmap().getWidth()) || (x < 0)) {
                xVelocity = xVelocity*-1;
            }
            if ((y > this.getHeight() - ball.getBitmap().getHeight()) || (y < 0)) {
                yVelocity = yVelocity*-1;
            }

        }
       // if(z>10 && xVelocity!=0 && yVelocity!=0)
       // {
       //     xVelocity=-2;
       //     yVelocity=-1;
       // }
        z++;
        c.drawBitmap(ball.getBitmap(), x, y, null);
        h.postDelayed(r, FRAME_RATE);
    }
}