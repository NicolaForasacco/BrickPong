package brickteamdevelopment.brickpong;

import android.graphics.Canvas;

public class PaddleRight extends Paddle{

    public PaddleRight(int screenWidth,int screenHeight,int a){

        super(screenWidth,screenHeight,a);

        width           =   screenHeight / ab ;

        x = screenWidth - height;
        y = 0;

        speed           =   screenHeight / abc ;
    }

    @Override
    public void incrementY() {
        super.incrementY();

        if(direction == -1)
            if (getY() <= 0)
                setY((int)(width * ab));

        if(direction == 1)
            if(getY() >= (int)(width * ab))
                setY(0);
    }


    public void incrementMovementSlow(long fps){

        if (direction == 1)
            movement = (speed / fps) * 6 * -1;

        if (direction == -1)
            movement = (speed / fps) * 6;
    }

    public void incrementMovement(long fps){

        if (direction == 1)
            movement = (speed / fps) * maxSpeed;

        if (direction == -1)
            movement = (speed / fps) * maxSpeed * -1;
    }

    public void decrementMovement(){

        movement = 0;
    }

    public float getPaddleEndX(){ return x + height; }

    public float getPaddleEndY(){ return y + width; }

    public void draw(Canvas canvas, int screenHeight) {

        canvas.drawRect(x + border,
                y,
                getPaddleEndX() + border,
                getPaddleEndY(),
                paintWhite);

        if(getPaddleEndY() > screenHeight){

            // if the paddle exceeds the bottom boundary, draw the remainder on the top boundary
            canvas.drawRect(x + border,
                    0,
                    getPaddleEndX() + border,
                    getPaddleEndY() - screenHeight,
                    paintWhite);

        }
    }
}
