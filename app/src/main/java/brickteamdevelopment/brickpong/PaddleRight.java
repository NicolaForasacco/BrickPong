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

        if(verso == -1)
            if (getY() <= 0)
                setY((int)(width*ab));

        if(verso == 1)
            if(getY()>=(int)(width*ab))
                setY(0);
    }


    public void incrementMovementSlow(long fps){

        if (verso == 1)
            movement = (speed / fps) *6*-1;

        if (verso == -1)
            movement = (speed / fps) * 6;
    }

    public void incrementMovement(long fps){

        if (verso == 1)
            movement = (speed / fps) * maxSpeed;

        if (verso == -1)
            movement = (speed / fps) * maxSpeed * -1;
    }

    public void decrementMovement(long fps){

        movement = 0;
    }

    public float getPaddleEndX(){ return x + height; }

    public float getPaddleEndY(){ return y + width; }

    public void draw(Canvas canvas, int screenHeight) {

        canvas.drawRect(x + a,
                y,
                getPaddleEndX()+a,
                getPaddleEndY(),
                paintWhite);

        if(getPaddleEndY() > screenHeight){

            //se la barra "supera" il limite inferiore si disegna il restante sul limite superiore
            canvas.drawRect(x + a,
                    0,
                    getPaddleEndX()+a,
                    getPaddleEndY()-screenHeight,
                    paintWhite);

        }
    }
}
