package brickteamdevelopment.brickpong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


public class Game {

    private final Paint     paint;

    private int             points=-1,
                            screenWidth,
                            screenHeight;

    private GamesApiHandler gamesApiHandler;

    public Game(int width, int height, String color, GamesApiHandler gamesApiHandler){
        this.points             =   -1;
        this.screenHeight       =   height;
        this.screenWidth        =   width;
        this.paint              =   new Paint();
        this.paint.setColor(Color.parseColor(color));
        this.gamesApiHandler    =   gamesApiHandler;
    }

    public int getScreenWidth() { return screenWidth; }
    public int getScreenHeight() { return screenHeight; }

    public int getPoints() { return points; }
    public void incrementPoints(){

        if(points<0)
            points = 0;

        points++;
        gamesApiHandler.checkForAchievements(points);
    }

    public void draw(Canvas canvas){

        canvas.drawColor(paint.getColor());
    }
}
