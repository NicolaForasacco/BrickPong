package com.brick.brickpong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class PaddleTop extends Paddle {

    public PaddleTop(int screenWidth,int screenHeight,int a){

        super(screenWidth,screenHeight,a);

        y = 0;
        x = 0;

        speed       *= -1;
        maxSpeed    *= -1;
    }

    public void incrementMovementSlow(long fps){

        if (direction == -1)
            movement = (speed / fps) * 6 * -1;


        if (direction == 1)
            movement = (speed / fps) * 6;

    }

    public void incrementMovement(long fps){

        if (direction == -1)
            movement = (speed / fps) * maxSpeed * -1;

        if (direction == 1)
            movement = (speed / fps) * maxSpeed;

    }

    public void decrementMovement(){

        movement = 0;
    }

    @Override
    public void incrementX(){
        super.incrementX();

        if(direction == -1)
            if (getX() <= 0)
                setX((int)(width * ab));


        if(direction == 1)
            if(getX() >= (int)(width * ab))
                setX(0);
    }

    public float getPaddleEndX(){ return x + width; }

    public float getPaddleEndY(){ return y + height; }

    public void draw(Canvas canvas, int screenWidth) {


            if (getPaddleEndX() > screenWidth) {

                canvas.drawRect(x + border,
                        0,
                        screenWidth + border,
                        height,
                        paintWhite);

                // if the paddle exceeds the right boundary, draw the remainder on the left boundary
                canvas.drawRect(0 + border,
                        0,
                        getPaddleEndX() - screenWidth + border,
                        getPaddleEndY(),
                        paintWhite);


            } else {

                canvas.drawRect(x + border,
                        0,
                        getPaddleEndX() + border,
                        height,
                        paintWhite);
            }

    }
}
