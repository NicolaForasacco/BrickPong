package brickteamdevelopment.brickpong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


public class Game {

    private final Paint     paint;

    private int             points,
                            screenWidth,
                            screenHeight;

    public Game(int width, int height, String color){
        this.points         =   0;
        this.screenHeight   =   height;
        this.screenWidth    =   width;
        this.paint          =   new Paint();
        this.paint.setColor(Color.parseColor(color));
    }

    public int getScreenWidth() { return screenWidth; }
    public int getScreenHeight() { return screenHeight; }

    public int getPoints() { return points; }
    public void incrementPoints(){ points++; }

    public void draw(Canvas canvas){

        canvas.drawColor(paint.getColor());
    }
}
