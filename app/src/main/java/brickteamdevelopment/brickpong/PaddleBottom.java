package brickteamdevelopment.brickpong;

import android.graphics.Canvas;

public class PaddleBottom extends Paddle{

    public PaddleBottom(int screenWidth,int screenHeight, int a){

        super(screenWidth,screenHeight,a );

        x           =   screenWidth - width;
        y           =   screenHeight - height;
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

    public void draw(Canvas canvas, int screenWidth) {

        canvas.drawRect(x,
                y,
                getPaddleEndX(),
                getPaddleEndY(),
                paintWhite);

        if(getPaddleEndX() > screenWidth){

            //se la barra "supera" il limite destro si disegna il restante sul limite sinistro
            canvas.drawRect(0,
                    y,
                    getPaddleEndX() - screenWidth,
                    getPaddleEndY(),
                    paintWhite);
        }
    }
}
