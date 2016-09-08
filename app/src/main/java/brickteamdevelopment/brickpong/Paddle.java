package brickteamdevelopment.brickpong;

import android.graphics.Color;
import android.graphics.Paint;

public class Paddle {

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

    public Paddle(int screenWidth,int screenHeight){

        height          =   screenHeight / 50 ;
        width           =   screenWidth / 2.5f ;

        x               =   0;

        speed           =   screenWidth / 13 ;

        border          =   width / 35;

        paintBlack.setColor(Color.BLACK);
        paintWhite.setColor(Color.WHITE);
    }

    public void incrementMaxSpeed(){ maxSpeed *= 1.05f; }

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
