package com.brick.brickpong;

import android.graphics.Canvas;

public class PaddleBottom extends Paddle{

    public PaddleBottom(int screenWidth,int screenHeight, int a){

        super(screenWidth,screenHeight,a );

        x           =   screenWidth - width;
        y           =   screenHeight - height;
    }

    public void incrementMovementSlow(long fps){

        if (direction == 1)
            movement = (speed / fps) * 6 ;

        if (direction == -1)
            movement = (speed / fps) * 6 * -1;
    }

    public void incrementMovement(long fps){

        if (direction == 1)
            movement = (speed / fps) * maxSpeed * -1;

        if (direction == -1)
            movement = (speed / fps) * maxSpeed;
    }

    public void decrementMovement(){

        movement = 0;
    }

    @Override
    public void incrementX(){
        super.incrementX();

        if(direction == 1)
            if (getX() <= 0)
                setX((int)(width * ab));


        if(direction == -1)
            if(getX() >= (int)(width * ab))
                setX(0);
    }

    public float getPaddleEndX(){ return x + width; }

    public float getPaddleEndY(){ return y + height; }

    public void draw(Canvas canvas, int screenWidth) {

        if(getPaddleEndX() > screenWidth){

            canvas.drawRect(x + border,
                    y,
                    screenWidth + border,
                    getPaddleEndY(),
                    paintWhite);

            // if the paddle exceeds the right boundary, draw the remainder on the left boundary
            canvas.drawRect(0 + border,
                    y,
                    getPaddleEndX() - screenWidth + border,
                    getPaddleEndY(),
                    paintWhite);
        }else{

            canvas.drawRect(x + border,
                    y,
                    getPaddleEndX() + border,
                    getPaddleEndY(),
                    paintWhite);
        }
    }
}
