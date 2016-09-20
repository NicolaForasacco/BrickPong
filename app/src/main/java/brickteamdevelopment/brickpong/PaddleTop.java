package brickteamdevelopment.brickpong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class PaddleTop extends Paddle {

    public PaddleTop(int screenWidth,int screenHeight,int a){

        super(screenWidth,screenHeight,a);

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

    public void draw(Canvas canvas, int screenWidth) {

        canvas.drawRect(x,
                0,
                getPaddleEndX(),
                height,
                paintWhite);


        if (getPaddleEndX() > screenWidth) {

            //se la barra "supera" il limite destro si disegna il restante sul limite sinistro
            canvas.drawRect(0,
                    0,
                    getPaddleEndX() - screenWidth,
                    getPaddleEndY(),
                    paintWhite);


        }
    }
}
