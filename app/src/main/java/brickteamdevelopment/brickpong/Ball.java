package brickteamdevelopment.brickpong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;

public class Ball {

    private Point   screen;

    private Paint   paintWhite,
                    paintBlack;

    private float   border,
                    width,
                    height,
                    speedX,
                    speedY,
                    x,
                    y;

    public Ball(int screenWidth,int screenHeight){

        screen          = new Point(screenWidth, screenHeight);

        height          =   screenWidth / 20;
        width           =   screenWidth / 20;

        x               =   new Random().nextInt((int)(screenWidth * 0.6)) + screenWidth * 0.2f;
        y               =   new Random().nextInt((int)(screenHeight / 2)) + (int)(screenHeight / 4);

        speedX          =   screenWidth / 2.5f;
        speedY          =   speedX;

        paintBlack      =   new Paint();
        paintWhite      =   new Paint();
        border          =   width / 8;

        paintBlack.setColor(Color.BLACK);
        paintWhite.setColor(Color.WHITE);

        if(y >= screenHeight / 2){
            changeMovementY();
        }

        if(new Random().nextBoolean())
            changeMovementX();
    }

    public float  getX(){ return x; }

    public float  getY(){ return y; }

    public float  getPaddleEndX(){ return x + width; }

    public float  getPaddleEndY(){ return y + height; }

    public void changeMovementX(){

        speedX *= -1;

        if(getX() <= 0)
            x = 1;

        if(getPaddleEndX() >= screen.x)
            x = screen.x - width - 1 ;
    }

    public void changeMovementY(){

        speedY *= -1;

        if(getY() <= 0)
            y = 0;
    }

    public void incrementCoordinates(long fps){

        x = x + speedX / fps;
        y = y + speedY / fps;
    }

    public void incrementSpeed(){
        speedX = speedX * 1.1f;
        speedY = speedY * 1.1f;
    }

    public void bounceOnPaddle(PaddleTop paddle){

        float v2x = speedX + paddle.speed * 0.8f;

        if (speedY <= 0)
            speedY *= -1;

        if(v2x == 0)
            v2x = (new Random().nextInt(2) == 0)? 1: -1;

        speedX = v2x;
    }

    public void bounceOnPaddle(PaddleBottom paddle){

        float v2x = speedX + paddle.speed * 0.8f;

        if (speedY >= 0)
            speedY *= -1;


        if(v2x == 0)
            v2x = (new Random().nextInt(2) == 0)? 1: -1;

        speedX = v2x;
    }

    public void draw(Canvas canvas){

        canvas.drawRect(x,
                        y,
                        getPaddleEndX(),
                        getPaddleEndY(),
                        paintBlack);

        canvas.drawRect(x + border,
                        y + border,
                        getPaddleEndX() - border,
                        getPaddleEndY() - border,
                        paintWhite);
    }
}
