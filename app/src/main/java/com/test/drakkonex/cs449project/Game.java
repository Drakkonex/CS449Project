package com.test.drakkonex.cs449project;

import android.app.Activity;
import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class Game extends Activity{

    // for instantiating the game
    GameView GameView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //initializes the game
        GameView = new GameView(this);
        setContentView(GameView);
    }

    //Now the base game class, GameView is created
    class GameView extends SurfaceView implements Runnable{

        //create the game thread
        Thread gameThread = null;

        //for drawing the assets onto the canvas
        SurfaceHolder surfaceHolder;

        //a bool that changes whether the play is on or off
        volatile boolean play;

        //Game starts paused
        boolean paused = true;

        //For drawing onto the canvas
        Canvas canvas;
        Paint paint;

        //to track game framerate
        long fps;

        //to calculate framerate
        long thisTimeFrame;

        //screen size
        int sizeX;
        int sizeY;

        //Creates the pad for ball to bounce off
        Pad pad;

        //Creates the ball to target bricks
        Ball ball;

        //Creates the bricks to be targeted
        Brick[] bricks = new Brick[200];
        int numBr = 0;

        //storing the score and lives
        int score = 0;
        int lives = 3;

        //constructor to set up game
        public GameView(Context context){

            super(context);

            surfaceHolder = getHolder();
            paint = new Paint();

            Display display = getWindowManager().getDefaultDisplay();
            // store the resolution in size
            Point size = new Point();
            display.getSize(size);

            // get the x and y val
            sizeX = size.x;
            sizeY = size.y;

            pad = new Pad(sizeX,sizeY);
            ball = new Ball();

            restartGame();
        }

        public  void restartGame(){

            //generate ball
            ball.reset(sizeX,sizeY);
            int brickW = sizeX/8;
            int brickH = sizeY/10;

            //generates bricks
            numBr = 0;
            for(int c = 0; c < 8; c++){
                for(int r = 0; r < 3; r++){
                    bricks[numBr++]=new Brick(r,c,brickW,brickH);
                }
            }

            //reset score/lives for gameover
            if (lives == 0){
                lives = 3;
                score = 0;
            }
        }

        @Override
        public void run(){
            while(play){
                //get the current game time
                long startTimeFrame = System.currentTimeMillis();
                // update the frames
                if(!paused){
                    update();
                }

                //draw the frame
                draw();

                //Calculate current frame
                thisTimeFrame = System.currentTimeMillis() - startTimeFrame;
                if(thisTimeFrame>=1){
                    fps = 1000/thisTimeFrame;
                }
            }
        }

        //Update for movement, collision
        public void update(){

            //move ball/pad
            pad.update(fps);
            ball.update(fps);

            //checks if ball hits brick
            for(int i = 0; i< numBr; i++) {
                if(bricks[i].getVisibility()){
                    if(RectF.intersects(bricks[i].getRect(),ball.getRect())){
                        bricks[i].setInvisible();
                        ball.reverseYVelocity();
                        score+=10;
                    }

                }
            }
            // pad and ball collision
            if(RectF.intersects(pad.getRect(),ball.getRect())){
                ball.setRandomXVelocity();
                ball.reverseYVelocity();
                ball.clearObstacleY(pad.getRect().top-2);
            }
            // ball hits bottom of screen
            if(ball.getRect().bottom > sizeY){
                ball.reverseYVelocity();
                ball.clearObstacleY(sizeY - 2);

                lives--;
                if(lives == 0){
                    paused = true;
                    restartGame();
                }
            }
            //ball hits top
            if(ball.getRect().top < 0){
                ball.reverseYVelocity();
                ball.clearObstacleY(12);
            }
            // ball hits leftside
            if(ball.getRect().left < 0){
                ball.reverseXVelocity();
                ball.clearObstacleX(2);
            }
            //ball hits rightside
            if (ball.getRect().right > (sizeX-10)){
                ball.reverseXVelocity();
                ball.clearObstacleX(sizeX - 22);
            }

            if(score == numBr*10)
            {
                paused = true;
                restartGame();
            }

        }

        public void draw(){
            if(surfaceHolder.getSurface().isValid()){

                canvas = surfaceHolder.lockCanvas();

                //background color
                canvas.drawColor(Color.argb(255, 95, 244, 66));


               // paint.setColor(Color.argb(255,255,255,255));
                paint.setColor(Color.argb(255, 255, 0, 0));

                //pad
                canvas.drawRect(pad.getRect(),paint);

                //ball
                canvas.drawRect(ball.getRect(),paint);

                paint.setColor(Color.argb(255, 11, 91, 188));

                for (int i = 0; i < numBr; i++){
                    if(bricks[i].getVisibility()){
                        canvas.drawRect(bricks[i].getRect(),paint);
                    }
                }

                paint.setColor(Color.argb(255, 255, 255, 255));

                paint.setTextSize(40);
                canvas.drawText("Score: "+ score+ "  Lives: "+lives, 10,50,paint);

                if(score == numBr*10){
                    paint.setTextSize(90);
                    canvas.drawText("You Won",10,sizeY/2,paint);
                }

                if(lives <= 0){
                    paint.setTextSize(90);
                    canvas.drawText("you Lost",10,sizeY/2,paint);
                }

                surfaceHolder.unlockCanvasAndPost(canvas);

            }
        }

        public void pause(){
            play = false;
            try{
                gameThread.join();
            }
            catch (InterruptedException e){
                Log.e("Error: ", "joining thread");
            }
        }

        public void resume(){
            play = true;
            gameThread = new Thread(this);
            gameThread.start();
        }

        @Override
        public boolean onTouchEvent(MotionEvent motionEvent){
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
                case MotionEvent.ACTION_DOWN:
                    paused = false;
                    if(motionEvent.getX()> (sizeX/2)){
                        pad.setMovementState(pad.RIGHT);
                    }
                    else{
                        pad.setMovementState(pad.LEFT);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    pad.setMovementState(pad.STOP);
                    break;
            }
            return true;
        }

    }

    @Override
    protected void onResume(){
        super.onResume();

        GameView.resume();
    }

    @Override
    protected void onPause(){
        super.onPause();

        GameView.pause();
    }

}
