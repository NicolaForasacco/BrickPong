package brickteamdevelopment.brickpong;

import android.graphics.Canvas;

public class PaddleBottom extends Paddle{

    public PaddleBottom(int screenWidth,int screenHeight){

        super(screenWidth,screenHeight);

        y = screenHeight - height;

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
                y,
                getPaddleEndX(),
                getPaddleEndY(),
                paintWhite);

        canvas.drawRect(x + border,
                y + border,
                getPaddleEndX() - border,
                getPaddleEndY() - border,
                paintBlack);

        if(getPaddleEndX() > screenWidth){

            //se la barra "supera" il limite destro si disegna il restante sul limite sinistro
            canvas.drawRect(0,
                    y,
                    getPaddleEndX() - screenWidth,
                    getPaddleEndY(),
                    paintWhite);

            canvas.drawRect(0,
                    y + border,
                    getPaddleEndX() - screenWidth - border,
                    getPaddleEndY() - border,
                    paintBlack);
        }
    }
}
