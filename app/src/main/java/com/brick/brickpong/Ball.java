package com.brick.brickpong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;
public class Ball {
    private Point   screen;

    private Paint   paintWhite = new Paint(),
                    paintBlack = new Paint();

    private float   ballBorder,
                    width,
                    height,
                    speedX,
                    speedY,
                    x,
                    y,
                    paddleCoefficient = 0.2f,
                    speedPercentage = 1.08f;

    private final int border;

    public Ball(int screenWidth,int screenHeight,int border,boolean isBackgroundBall){

        this.border = border;

        if(screenWidth > 0){

            screen  = new Point(screenWidth, screenHeight);

            if(isBackgroundBall){

                height          =   screenWidth / (new Random().nextInt(20) + 5);
                width           =   height;

                x               =   new Random().nextInt(screenWidth)  + 1;
                y               =   new Random().nextInt(screenHeight) + 1;

                speedX          =   screenWidth / 2.5f;
                speedY          =   speedX;

                int color = new Random().nextInt(35);

                paintBlack.setColor(Color.rgb(color,color,color));
                paintWhite.setColor(paintBlack.getColor());

            }else {

                height          =   screenWidth / 20;
                width           =   height;

                x = screenWidth / 2 + width ;
                y = screenHeight / 2 + width ;

                float b = new Random().nextInt(2) + 3 + new Random().nextFloat();

                speedX          =   screenWidth / b;
                speedY          =   screenWidth / 5 - b;

                paintBlack.setColor(Color.BLACK);
                paintWhite.setColor(Color.WHITE);

                if(y >= screenHeight / 2)
                    changeMovementY();

                if(x >= screenWidth / 2)
                    changeMovementX();
            }

        ballBorder =  width / 8;

        if(new Random().nextBoolean())
            changeMovementX();

        if(new Random().nextBoolean())
            changeMovementY();

        }
    }

    public float  getX(){ return x; }

    public float  getY(){ return y; }

    public float  getBallEndX(){ return x + width; }

    public float  getBallEndY(){ return y + height; }

    public void changeMovementX(){

        speedX *= -1;

        if(getX() <= 0)
            x = 1;

        if(getBallEndX() >= screen.x)
            x = screen.x - width - 1 ;
    }

    public void changeMovementY(){

        speedY *= -1;

        if(getY() <= 0)
            y = 1;
    }

    public float getASpeed(long fps){
        return (Math.abs(speedX / fps) + Math.abs(speedY / fps))/70;
    }

    public void incrementCoordinates(long fps){

        x += speedX / fps;
        y += speedY / fps;
    }

    public void incrementSpeed(){

        speedX *= speedPercentage;
        speedY *= speedPercentage;
    }

    public void bounceOnPaddle(PaddleLeft paddle){

        float v2y = speedY + paddle.speed * paddleCoefficient;

        if (speedX <= 0)
            speedX *= -1;

        if(v2y == 0)
            v2y = (new Random().nextInt(2) == 0)? 1: -1;

        speedY = v2y;
    }

    public void bounceOnPaddle(PaddleRight paddle){

        float v2y = speedY + paddle.speed * paddleCoefficient;

        if (speedX >= 0)
            speedX *= -1;

       if(v2y == 0)
            v2y = (new Random().nextInt(2) == 0)? 1: -1;

        speedY = v2y;
    }

    public void bounceOnPaddle(PaddleTop paddle){

        float v2x = speedX + paddle.speed * paddleCoefficient;

        if (speedY <= 0)
            speedY *= -1;

        if(v2x == 0)
            v2x = (new Random().nextInt(2) == 0)? 1: -1;

        speedX = v2x;
    }

    public void bounceOnPaddle(PaddleBottom paddle){

        float v2x = speedX + paddle.speed * paddleCoefficient;

        if (speedY >= 0)
            speedY *= -1;


        if(v2x == 0)
            v2x = (new Random().nextInt(2) == 0)? 1: -1;

        speedX = v2x;
    }

    public void draw(Canvas canvas){

        canvas.drawRect(x+ border,
                        y,
                        getBallEndX()+ border,
                        getBallEndY(),
                        paintWhite);

        canvas.drawRect(x + ballBorder + border,
                        y + ballBorder,
                        getBallEndX() - ballBorder + border,
                        getBallEndY() - ballBorder,
                        paintBlack);
    }
}
