package brickteamdevelopment.brickpong;

import android.graphics.Color;
import android.graphics.Paint;


public class Paddle {

    protected boolean       isSlow = false;


    public float            border,
                            width,
                            height,
                            speed,
                            movement = 0 ,
                            maxSpeed  =   15,
                            x,
                            y;

    protected Paint         paintBlack,
                            paintWhite;

    public Paddle(){}

    public Paddle(int screenWidth,int screenHeight,boolean isSlow){

        height          =   screenHeight / 50 ;
        width           =   screenWidth / 5 ;

        x               =   0;

        speed           =   screenWidth / 14 ;

        this.isSlow     =   isSlow;
        paintBlack      =   new Paint();
        paintWhite      =   new Paint();
        border          =   width / 25;

        paintBlack.setColor(Color.BLACK);
        paintWhite.setColor(Color.WHITE);
    }

    public void incrementMaxSpeed(){
        maxSpeed *= 1.1f;
    }

    public void incrementX(){

        x = x + movement;
    }

    public float getHeight(){ return height; }

    public void setX(int x){ this.x = x; }

    public float getX(){ return x; }

    public float getY(){ return y; }

    public float getPaddleEndX(){ return x + width; }

    public float getPaddleEndY(){ return y + height; }

}
