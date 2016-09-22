package brickteamdevelopment.brickpong;

import android.graphics.Color;
import android.graphics.Paint;

public class Paddle {

    public final int a;
    public float            border,
                            width,
                            height,
                            speed,
                            movement = 0,
                            maxSpeed = 15,
                            x,
                            y;

    protected Paint         paintBlack = new Paint(),
                            paintWhite = new Paint();

    protected float ab = 5f;

    protected int abc = 14;

    public int verso = 0;

    private float speedPercentage = 1.05f;

    public Paddle(int screenWidth,int screenHeight,int a){

        this.a = a;

        height          =   screenHeight / 70 ;
        width           =   screenWidth / ab ;

        x               =   0;

        speed           =   screenWidth / abc ;

        border          =   0;

        paintBlack.setColor(Color.BLACK);
        paintWhite.setColor(Color.WHITE);
    }

    public void goLeft(){

        verso = -1;

        speed       *= -1;
        maxSpeed    *= -1;
    }

    public void goRight(){

        verso = 1;

        speed       *= -1;
        maxSpeed    *= -1;
    }

    public void stop(){

        verso = 0;

        speed       *= -1;
        maxSpeed    *= -1;
    }


    public void incrementMaxSpeed(){
        maxSpeed *= speedPercentage;
    }

    public void incrementX(){ x += movement; }

    public void incrementY(){ y += movement; }

    public float getHeight(){ return height; }

    public void setX(int x){ this.x = x; }

    public void setY(int y){ this.y = y;}

    public float getX(){ return x; }

    public float getY(){ return y; }

    public float getPaddleEndX(){ return x + width; }

    public float getPaddleEndY(){ return y + height; }

}
