package com.brick.brickpong;

import android.graphics.Canvas;

public class PaddleLeft extends Paddle{

    public PaddleLeft(int screenWidth,int screenHeight,int a){

        super(screenWidth,screenHeight,a);

        width           =   screenHeight / ab ;
        speed           =   screenHeight / abc ;

        y = screenHeight - width;

        speed       *= -1;
        maxSpeed    *= -1;
    }

    @Override
    public void incrementY() {
        super.incrementY();

        if(direction == 1)
            if (getY() <= 0)
                setY((int)(width * ab));


        if(direction == -1)
            if(getY() >= (int)(width * ab))
                setY(0);
    }


    public void incrementMovementSlow(long fps){

        if (direction == -1)
            movement = (speed / fps) * 6;

        if (direction == 1)
            movement = (speed / fps) * 6 * -1;

    }

    public void incrementMovement(long fps){

        fps = 60;
        if (direction == -1)
            movement = (speed / fps) * maxSpeed;

        if (direction == 1)
            movement = (speed / fps) * maxSpeed * -1;
    }

    public void decrementMovement(){

        movement = 0;
    }

    public float getPaddleEndX(){ return x + height; }

    public float getPaddleEndY(){ return y + width; }

    public void draw(Canvas canvas, int screenHeight) {

        canvas.drawRect(0 + border,
                y,
                getPaddleEndX() + border,
                getPaddleEndY(),
                paintWhite);

        if(getPaddleEndY() > screenHeight){

            // if the paddle exceeds the top boundary, draw the remainder on the bottom boundary
            canvas.drawRect(0 + border,
                    0,
                    getPaddleEndX() + border,
                    getPaddleEndY() - screenHeight,
                    paintWhite);

        }
    }
}