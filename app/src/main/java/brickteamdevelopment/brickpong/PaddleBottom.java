package brickteamdevelopment.brickpong;

import android.graphics.Canvas;

public class PaddleBottom extends Paddle{

    public PaddleBottom(int screenWidth,int screenHeight, int a){

        super(screenWidth,screenHeight,a );

        x           =   screenWidth - width;
        y           =   screenHeight - height;


    }

    public void incrementMovementSlow(long fps){

        if (verso == 1)
            movement = (speed / fps) * 6 ;

        if (verso == -1)
            movement = (speed / fps) * 6 * -1;
    }

    public void incrementMovement(long fps){

        if (verso == 1)
            movement = (speed / fps) * maxSpeed * -1;

        if (verso == -1)
            movement = (speed / fps) * maxSpeed;
    }

    public void decrementMovement(long fps){

        movement = 0;
    }

    @Override
    public void incrementX(){
        super.incrementX();

        if(verso == 1)
            if (getX() <= 0)
                setX((int)(width * ab));


        if(verso == -1)
            if(getX() >= (int)(width * ab))
                setX(0);
    }

    public void draw(Canvas canvas, int screenWidth) {



        if(getPaddleEndX() > screenWidth){

            canvas.drawRect(x+a,
                    y,
                    screenWidth+a,
                    getPaddleEndY(),
                    paintWhite);

            //se la barra "supera" il limite destro si disegna il restante sul limite sinistro
            canvas.drawRect(0+a,
                    y,
                    getPaddleEndX() - screenWidth+a,
                    getPaddleEndY(),
                    paintWhite);
        }else{

            canvas.drawRect(x+a,
                    y,
                    getPaddleEndX()+a,
                    getPaddleEndY(),
                    paintWhite);
        }
    }
}
