package brickteamdevelopment.brickpong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


public class Game {

    public int             screenWidth,
                            screenHeight;

    private float points = -1;

    private GamesApiHandler gamesApiHandler;

    public Game(int width, int height,GamesApiHandler gamesApiHandler){

        this.points             =   -1;
        this.screenHeight       =   height;
        this.screenWidth        =   width;
        this.gamesApiHandler    =   gamesApiHandler;
    }

    public int getScreenWidth() { return screenWidth; }
    public int getScreenHeight() { return screenHeight; }

    public float getPoints() { return points; }
    public void incrementPoints(float points){
        this.points += points;
    }

    public void draw(Canvas canvas){
        canvas.drawColor(Color.BLACK);
    }
}
