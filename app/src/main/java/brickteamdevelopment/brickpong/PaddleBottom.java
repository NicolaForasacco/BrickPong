package brickteamdevelopment.brickpong;

import android.graphics.Canvas;

public class PaddleBottom extends Paddle{

    public PaddleBottom(){}

    public PaddleBottom(int screenWidth,int screenHeight,boolean isSlow){

        super(screenWidth,screenHeight,isSlow);

        y = screenHeight - height;
    }

    public void incrementMovement(long fps){

        if(isSlow){

            if(movement < (speed / fps) * 6)
                movement += speed / fps;
            else
                movement = (speed / fps) * 6;
        }else{

            if (movement < (speed / fps) * maxSpeed)
                movement += speed / fps;
            else
                movement = (speed / fps) * maxSpeed;
        }
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
                paintBlack);

        canvas.drawRect(x + border,
                y + border,
                getPaddleEndX() - border,
                getPaddleEndY() - border,
                paintWhite);

        if(getPaddleEndX() >= screenWidth){

            //se la barra "supera" il limite destro si disegna il restante sul limite sinistro
            canvas.drawRect(0,
                    y,
                    getPaddleEndX() - screenWidth,
                    getPaddleEndY(),
                    paintBlack);

            canvas.drawRect(0,
                    y + border,
                    getPaddleEndX() - screenWidth - border,
                    getPaddleEndY() - border,
                    paintWhite);

        }
    }
}
