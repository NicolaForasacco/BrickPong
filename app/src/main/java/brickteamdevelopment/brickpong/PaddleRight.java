package brickteamdevelopment.brickpong;

import android.graphics.Canvas;

public class PaddleRight extends Paddle{

    public PaddleRight(){}

    public PaddleRight(int screenWidth,int screenHeight){

        super(screenWidth,screenHeight);

        width           =   screenHeight / 2.5f ;

        x = screenWidth - height;
        y = screenHeight - width;

        speed           =   screenHeight / 13 ;




    }
    public void incrementMovementSlow(long fps){

        if(movement < (speed / fps) * 6)
            movement += speed / fps;
        else
            movement = (speed / fps) * 6;
    }

    public void incrementMovement(long fps){

        if (movement < (speed / fps) * maxSpeed)
            movement += speed / fps;
        else
            movement = (speed / fps) * maxSpeed;
    }

    public void decrementMovement(long fps){

        if (movement > 0)
            movement -= speed / fps;
        else
            movement = 0;
    }

    public float getPaddleEndX(){ return x + height; }

    public float getPaddleEndY(){ return y + width; }

    public void draw(Canvas canvas, int screenHeight) {



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

        if(getPaddleEndY() > screenHeight){

            //se la barra "supera" il limite inferiore si disegna il restante sul limite superiore
            canvas.drawRect(x,
                    0,
                    getPaddleEndX(),
                    getPaddleEndY()- screenHeight,
                    paintBlack);

            canvas.drawRect(x+border,
                    0 + border,
                    getPaddleEndX() - border,
                    getPaddleEndY() - screenHeight - border,
                    paintWhite);

        }
    }
}
