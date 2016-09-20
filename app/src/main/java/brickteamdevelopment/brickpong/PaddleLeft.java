package brickteamdevelopment.brickpong;

import android.graphics.Canvas;

public class PaddleLeft extends Paddle{

    public PaddleLeft(int screenWidth,int screenHeight,int a){

        super(screenWidth,screenHeight,a);

        width           =   screenHeight / 2.5f ;
        speed           =   screenHeight / 14 ;
        y = 0;

        speed       *= -1;
        maxSpeed    *= -1;
    }


    public void incrementMovementSlow(long fps){

        if(movement > (speed / fps) * 6)
            movement += speed / fps;
        else
            movement = (speed / fps) * 6;
    }

    public void incrementMovement(long fps){

        if (movement > (speed / fps) * maxSpeed * -1)
            movement += speed / fps;
        else
            movement = (speed / fps) * maxSpeed * -1;
    }

    public void decrementMovement(long fps){

        if (movement < 0)
            movement -= speed / fps;
        else
            movement = 0;
    }

    public float getPaddleEndX(){ return x + height; }

    public float getPaddleEndY(){ return y + width; }

    public void draw(Canvas canvas, int screenHeight) {

        canvas.drawRect(0 + a,
                y,
                getPaddleEndX()+a,
                getPaddleEndY(),
                paintWhite);

        if(getPaddleEndY() > screenHeight){

            //se la barra "supera" il limite superiore si disegna il restante sul limite inferiore
            canvas.drawRect(0 + a,
                    0,
                    getPaddleEndX()+a,
                    getPaddleEndY()- screenHeight,
                    paintWhite);

        }
    }
}