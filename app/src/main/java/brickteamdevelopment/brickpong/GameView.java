package brickteamdevelopment.brickpong;

import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class GameView implements Runnable {

    private Thread          gameThread = null;
    private SurfaceHolder   ourHolder;
    private Canvas          canvas;
    private MainActivity    mainActivity;

    public boolean          playing;

    private long            fps = 1,
                            timeThisFrame;

    public String          gameType;

    private PaddleTop       paddleTop;
    private PaddleBottom    paddleBottom;
    private PaddleLeft      paddleLeft;
    private PaddleRight     paddleRight;

    public Game             game;
    private Ball            ball;

    private Ball[]          backgroundBalls;

    public GameView(MainActivity mainActivity, SurfaceView view) {

        ourHolder           =   view.getHolder();
        this.mainActivity   =   mainActivity;
    }

    @Override
    public void run() {

        while (playing) {

            long startFrameTime = System.currentTimeMillis();

            updateBackground();

            if (gameType.compareTo("menu") == 0) {

                updateMenu();
                drawMenu();
            } else {

                updateGame();
                drawGame();
            }

            timeThisFrame = System.currentTimeMillis() - startFrameTime;

            if (timeThisFrame > 0)
                fps = 1000 / timeThisFrame;
        }
    }

    public void showGame(Point screen, int bannerHeight, boolean isGame) {

        if(isGame)
            gameType = "game";
        else
            gameType = "menu";

        pause();

        int gameSide    =   (screen.y - bannerHeight) / 4 * 2;
        int border      =   (screen.x - gameSide) / 2;

        paddleLeft      =   new PaddleLeft(gameSide, gameSide, border);
        paddleRight     =   new PaddleRight(gameSide, gameSide, border);
        paddleTop       =   new PaddleTop(gameSide, gameSide, border);
        paddleBottom    =   new PaddleBottom(gameSide, gameSide, border);

        game            =   new Game(gameSide, gameSide);

        ball            =   new Ball(gameSide, gameSide, border, false);

        backgroundBalls = new Ball[9];

        for(int i = 0; i < backgroundBalls.length; i++)
            backgroundBalls[i] = new Ball(gameSide, gameSide, border, true);


        fps             =   (long)paddleBottom.speed;
        playing         =   true;
        gameThread      =   new Thread(this);

        gameThread.start();
    }

    public void goLeft(){

        paddleTop.goLeft();
        paddleBottom.goLeft();
        paddleRight.goLeft();
        paddleLeft.goLeft();
    }

    public void goRight(){

        paddleTop.goRight();
        paddleBottom.goRight();
        paddleRight.goRight();
        paddleLeft.goRight();
    }

    public void stopPaddle(){

        if(gameType.compareTo("menu")!=0) {

            paddleTop.stop();
            paddleBottom.stop();
            paddleRight.stop();
            paddleLeft.stop();
        }
    }

    /*private void soundPlayerInit(){

        limitBounceSound    =   new SoundPlayer();
        paddleBounceSound   =   new SoundPlayer();

        limitBounceSound.setMediaPlayer(MediaPlayer.create(mainActivity.getApplicationContext(),R.raw.blip_select26));
        paddleBounceSound.setMediaPlayer(MediaPlayer.create(mainActivity.getApplicationContext(),R.raw.jump83));
    }*/

    public void updateMenu(){

        paddleLeft.incrementY();
        paddleRight.incrementY();

        paddleBottom.incrementX();
        paddleTop.incrementX();

        if (paddleLeft.direction != 0) {
            paddleRight.incrementMovementSlow(fps);
            paddleLeft.incrementMovementSlow(fps);
            paddleBottom.incrementMovementSlow(fps);
            paddleTop.incrementMovementSlow(fps);
        }
    }

    public void drawMenu() {

        if (ourHolder.getSurface().isValid()) {

            canvas = ourHolder.lockCanvas();
            if(canvas != null) {

                game.draw(canvas);

                drawBackground();
                paddleTop.draw(canvas, game.getScreenWidth());
                paddleBottom.draw(canvas, game.getScreenWidth());
                paddleLeft.draw(canvas, game.getScreenHeight());
                paddleRight.draw(canvas, game.getScreenHeight());

                ourHolder.unlockCanvasAndPost(canvas);
            }
        }
    }


    public void updateGame(){

        paddleLeft.incrementY();
        paddleRight.incrementY();

        paddleBottom.incrementX();
        paddleTop.incrementX();

        if (paddleLeft.direction != 0){

            paddleRight.incrementMovement(fps);
            paddleLeft.incrementMovement(fps);
            paddleBottom.incrementMovement(fps);
            paddleTop.incrementMovement(fps);
        }
        else{
            paddleRight.decrementMovement();
            paddleBottom.decrementMovement();
            paddleTop.decrementMovement();
            paddleLeft.decrementMovement();
        }

        ball.incrementCoordinates(fps);


        game.incrementPoints(ball.getASpeed(fps));

        MainActivity.txvPoints.post(new Runnable() {
            @Override
            public void run() {

                MainActivity.txvPoints.setText(String.valueOf((int)game.getPoints()));
            }
        });

        if(ball.getX() <= paddleLeft.getPaddleEndX()) {

            if ((ball.getBallEndY() >= paddleLeft.getY()
                    && ball.getY() <= paddleLeft.getPaddleEndY())
                    || (ball.getY() >= 0
                    && ball.getY() <= paddleLeft.getPaddleEndY() - game.getScreenHeight())) {

                // the ball touched the left paddle

                incrementPaddlesSpeed();
                ball.bounceOnPaddle(paddleLeft);
                ball.incrementSpeed();
                
            } else
                mainActivity.showGameOverMenu();
        }

        if(ball.getBallEndX() >= paddleRight.getX()) {

            if ((ball.getBallEndY() >= paddleRight.getY()
                    && ball.getY() <= paddleRight.getPaddleEndY())
                    || (ball.getY() >= 0
                    && ball.getY() <= paddleRight.getPaddleEndY() - game.getScreenHeight())) {

                // the ball touched the right paddle

                incrementPaddlesSpeed();
                ball.bounceOnPaddle(paddleRight);
                ball.incrementSpeed();

            } else
                mainActivity.showGameOverMenu();
        }

        if(ball.getY() <= paddleTop.getHeight()) {

            if ((ball.getBallEndX() >= paddleTop.getX()
                    && ball.getX() <= paddleTop.getPaddleEndX())
                    || (ball.getX() >= 0
                    && ball.getX() <= paddleTop.getPaddleEndX() - game.getScreenWidth())) {

                // the ball touched the top paddle

                incrementPaddlesSpeed();
                ball.bounceOnPaddle(paddleTop);
                ball.incrementSpeed();

            } else
                mainActivity.showGameOverMenu();
        }

        if(ball.getBallEndY() >= paddleBottom.getY()) {

            if ((ball.getBallEndX() >= paddleBottom.getX()
                    && ball.getX() <= paddleBottom.getPaddleEndX())
                    || (ball.getX() >= 0
                    && ball.getX() <= paddleBottom.getPaddleEndX() - game.getScreenWidth())) {

                // the ball touched the bottom paddle

                incrementPaddlesSpeed();
                ball.bounceOnPaddle(paddleBottom);
                ball.incrementSpeed();

            } else
                mainActivity.showGameOverMenu();
        }
    }

    private void incrementPaddlesSpeed(){

        paddleLeft.incrementMaxSpeed();
        paddleRight.incrementMaxSpeed();
        paddleTop.incrementMaxSpeed();
        paddleBottom.incrementMaxSpeed();
    }

    public void drawGame(){

        if (ourHolder.getSurface().isValid()) {

            canvas = ourHolder.lockCanvas();

            if(canvas != null) {

                game.draw(canvas);

                drawBackground();
                paddleTop.draw(canvas, game.getScreenWidth());
                paddleBottom.draw(canvas, game.getScreenWidth());
                paddleLeft.draw(canvas, game.getScreenHeight());
                paddleRight.draw(canvas, game.getScreenHeight());

                ball.draw(canvas);

                ourHolder.unlockCanvasAndPost(canvas);
            }
        }
    }


    public void updateBackground(){

        for(int i = 0; i < backgroundBalls.length; i++){

            backgroundBalls[i].incrementCoordinates(fps);

            if (backgroundBalls[i].getBallEndX() >= game.getScreenWidth() || backgroundBalls[i].getX() <= 0)
                backgroundBalls[i].changeMovementX();

            if(backgroundBalls[i].getY() <=0 || backgroundBalls[i].getBallEndY()>= game.getScreenHeight())
                backgroundBalls[i].changeMovementY();
        }
    }

    public void drawBackground(){

        for(int i = 0; i < backgroundBalls.length; i++)
            backgroundBalls[i].draw(canvas);
    }

    public void pause() {

        playing = false;

        if(gameThread != null){

            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Log.e("Error:", "joining thread");
            }
        }

        gameThread = null;
    }

    public void resume() {

        if(gameThread == null )
            gameThread = new Thread(this);

        if(playing == false)
            playing = true;

        if(!gameThread.isAlive())
            gameThread.start();
    }
}

