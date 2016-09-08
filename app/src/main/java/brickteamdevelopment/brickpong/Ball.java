package brickteamdevelopment.brickpong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;
import java.util.regex.Matcher;

public class Ball {

    private Point   screen;

    private Paint   paintWhite = new Paint(),
                    paintBlack = new Paint();

    private float   border,
                    width,
                    height,
                    speedX,
                    speedY,
                    x,
                    y,
                    paddleCoefficient = 0.4f;

    public Ball(int screenWidth,int screenHeight,boolean isBackgroundBall){

        screen          = new Point(screenWidth, screenHeight);

        if(isBackgroundBall){

            height          =   screenWidth / (new Random().nextInt(20) + 5);
            width           =   height;
            x               =   new Random().nextInt(screenWidth)  + 1;
            y               =   new Random().nextInt(screenHeight) + 1;

            speedX          =   screenWidth / 5.5f;
            speedY          =   speedX;

            if(new Random().nextBoolean())
                paintBlack.setColor(Color.rgb(195,0,0));
            else
                paintBlack.setColor(Color.rgb(215,0,0));

            paintWhite.setColor(paintBlack.getColor());
        }else {

            height          =   screenWidth / 20;
            width           =   height;
            x               =   new Random().nextInt(screenWidth /2) + screenWidth /4;
            y               =   new Random().nextInt(screenHeight / 2) + (screenHeight / 4);

            float b = new Random().nextInt(4)+3+new Random().nextFloat();

            speedX          =   screenWidth / b;
            speedY          =   screenWidth / 7-b;

            paintBlack.setColor(Color.BLACK);
            paintWhite.setColor(Color.WHITE);

            if(y >= screenHeight / 2)
                changeMovementY();

            if(x >= screenWidth / 2)
                changeMovementX();
        }

        border          =   width / 8;

        if(new Random().nextBoolean())
            changeMovementX();

        if(new Random().nextBoolean())
            changeMovementY();
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

    public void incrementCoordinates(long fps){

        x += speedX / fps;
        y += speedY / fps;
    }

    public void incrementSpeed(){

        //speedX *= 1.05f;
        //speedY *= 1.05f;
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

        canvas.drawRect(x,
                        y,
                        getBallEndX(),
                        getBallEndY(),
                        paintBlack);

        canvas.drawRect(x + border,
                        y + border,
                        getBallEndX() - border,
                        getBallEndY() - border,
                        paintWhite);
    }
}
