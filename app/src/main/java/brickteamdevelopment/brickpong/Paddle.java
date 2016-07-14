package brickteamdevelopment.brickpong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;


public class Paddle {

    private final boolean   isSlow;


    public float            border,
                            width,
                            height,
                            speed,
                            movement = 0 ,
                            maxSpeed  =   15,
                            x,
                            y;

    private Paint           paintBlack,
                            paintWhite;

    public Paddle(int screenWidth,int screenHeight,boolean isSlow){

        height          =   screenHeight / 50 ;
        width           =   screenWidth / 5 ;
        x               =   height / 2;
        y               =   screenHeight - height;


        speed           =   screenWidth / 14 ;
        this.isSlow     =   isSlow;
        paintBlack      =   new Paint();
        paintWhite      =   new Paint();
        border          =   width / 25;

        paintBlack.setColor(Color.BLACK);
        paintWhite.setColor(Color.WHITE);
    }

    public void incrementMovement(long fps){

        if(isSlow){

            if(movement < (speed / fps) * 6)
                movement += speed / fps;
            else
                movement = (speed / fps) * 6;
        }else{
            if(movement < (speed / fps) * maxSpeed)
                movement += speed / fps;
            else
                movement = (speed / fps)* maxSpeed;
        }
    }

    public void incrementMaxSpeed(){
        maxSpeed *= 1.1f;
    }

    public void decrementMovement(long fps){

        if(movement > 0)
            movement -= speed / fps;
        else
            movement = 0;
    }

    public void incrementX(){ x = x + movement;}

    public float getHeight(){ return height; }

    public void setX(int x){ this.x = x; }

    public float getX(){ return x; }

    public float getY(){ return y; }

    public float getPaddleEndX(){ return x + width; }

    public float getPaddleEndY(){ return y + height; }

    public void draw(Canvas canvas,int screenWidth){

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

            canvas.drawRect(0 + border,
                            y + border,
                            getPaddleEndX() - screenWidth - border,
                            getPaddleEndY() - border,
                            paintWhite);

        }
    }
}
