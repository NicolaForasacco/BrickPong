package brickteamdevelopment.brickpong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class GameView implements Runnable {

    private Thread          gameThread = null;
    private SurfaceHolder   ourHolder;
    private Canvas          canvas;
    public Game             game;
    private Ball            ball;

    public boolean          playing,
                            isScreenTouched;

    private long            fps = 1;
    private long            timeThisFrame;

    private MainActivity    mainActivity;
    public String          gameType;

    private PaddleTop       paddleTop;
    private PaddleBottom    paddleBottom;
    private PaddleLeft      paddleLeft;
    private PaddleRight     paddleRight;
    private Ball[]          backgroundBalls;
    private float totPoint=0;


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

    public void showGame(Point dim, int bannerHeight, GamesApiHandler gamesApiHandler) {

        if(gamesApiHandler != null)
            gameType = "game";
        else
            gameType = "menu";

        pause();

        totPoint = 0;

        Paint paintWhite = new Paint();
        paintWhite.setColor(Color.WHITE);

        paddleLeft      =   new PaddleLeft(dim.x, dim.y-bannerHeight);

        paddleRight     =   new PaddleRight(dim.x, dim.y - bannerHeight);

        paddleTop       =   new PaddleTop(dim.x, dim.y - bannerHeight);

        paddleBottom    =   new PaddleBottom(dim.x, dim.y - bannerHeight);

        game            =   new Game(dim.x,
                                    dim.y - bannerHeight,
                                    "#B71C1C",
                                    gamesApiHandler);

        ball            =   new Ball(dim.x,
                                    dim.y - bannerHeight,
                                    false);

        backgroundBalls = new Ball[14];

        for(int i = 0; i < backgroundBalls.length; i++){

            backgroundBalls[i] = new Ball(dim.x,
                    dim.y - bannerHeight,
                    true);
        }

        fps             =   (long)paddleBottom.speed;
        playing         =   true;
        gameThread      =   new Thread(this);

        gameThread.start();
    }

    /*private void soundPlayerInit(){

        limitBounceSound    =   new SoundPlayer();
        paddleBounceSound   =   new SoundPlayer();

        limitBounceSound.setMediaPlayer(MediaPlayer.create(mainActivity.getApplicationContext(),R.raw.blip_select26));
        paddleBounceSound.setMediaPlayer(MediaPlayer.create(mainActivity.getApplicationContext(),R.raw.jump83));
    }*/

    public void updateMenu(){

        paddleLeft.incrementY();
        if(paddleLeft.getY() <= 0)
            paddleLeft.setY(game.getScreenHeight());

        paddleRight.incrementY();
        if(paddleRight.getY() >= game.getScreenHeight())
            paddleRight.setY(0);

        paddleTop.incrementX();
        if (paddleTop.getX() >= game.getScreenWidth())
            paddleTop.setX(0);

        paddleBottom.incrementX();
        if (paddleBottom.getX() <= 0)
            paddleBottom.setX(game.getScreenWidth());

        paddleLeft.incrementMovementSlow(fps);

        paddleRight.incrementMovementSlow(fps);

        paddleTop.incrementMovementSlow(fps);

        paddleBottom.incrementMovementSlow(fps);
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
        if(paddleLeft.getY() <= 0)
            paddleLeft.setY(game.getScreenHeight());

        paddleRight.incrementY();
        if(paddleRight.getY() >= game.getScreenHeight())
            paddleRight.setY(0);

        paddleTop.incrementX();
        if (paddleTop.getX() >= game.getScreenWidth())
            paddleTop.setX(0);

        paddleBottom.incrementX();
        if (paddleBottom.getX() <= 0)
            paddleBottom.setX(game.getScreenWidth());


        if (isScreenTouched){
            paddleRight.incrementMovement(fps);
            paddleLeft.incrementMovement(fps);
            paddleBottom.incrementMovement(fps);
            paddleTop.incrementMovement(fps);
        }
        else{
            paddleRight.decrementMovement(fps);
            paddleBottom.decrementMovement(fps);
            paddleTop.decrementMovement(fps);
            paddleLeft.decrementMovement(fps);
        }

        ball.incrementCoordinates(fps);



        totPoint += ball.getASpeed(fps);

        MainActivity.txvPoints.post(new Runnable() {
            @Override
            public void run() {

                MainActivity.txvPoints.setText(String.valueOf((int)totPoint/10));
            }
        });


        if(ball.getX() <= paddleLeft.getPaddleEndX()) {

            if ((ball.getBallEndY() >= paddleLeft.getY()
                    && ball.getY() <= paddleLeft.getPaddleEndY())
                    || (ball.getY() >= 0
                    && ball.getY() <= paddleLeft.getPaddleEndY() - game.getScreenHeight())) {

                //la pallina ha toccato la barra sinistra

                ball.incrementSpeed();
                incrementPaddlesSpeed();
                ball.bounceOnPaddle(paddleLeft);
                game.incrementPoints();

                quakeLeft();
                printPoints();

            } else
                mainActivity.showGameOverMenu();
        }

        if(ball.getBallEndX() >= paddleRight.getX()) {

            if ((ball.getBallEndY() >= paddleRight.getY()
                    && ball.getY() <= paddleRight.getPaddleEndY())
                    || (ball.getY() >= 0
                    && ball.getY() <= paddleRight.getPaddleEndY() - game.getScreenHeight())) {

                //la pallina ha toccato la barra destra

                ball.incrementSpeed();
                incrementPaddlesSpeed();
                ball.bounceOnPaddle(paddleRight);
                game.incrementPoints();

                quakeRight();
                printPoints();

            } else
                mainActivity.showGameOverMenu();
        }

        if(ball.getY() <= paddleTop.getHeight()) {

            if ((ball.getBallEndX() >= paddleTop.getX()
                    && ball.getX() <= paddleTop.getPaddleEndX())
                    || (ball.getX() >= 0
                    && ball.getX() <= paddleTop.getPaddleEndX() - game.getScreenWidth())) {

                //la pallina ha toccato la barra superiore

                ball.incrementSpeed();
                incrementPaddlesSpeed();
                ball.bounceOnPaddle(paddleTop);
                game.incrementPoints();

                quakeTop();
                printPoints();

            } else
                mainActivity.showGameOverMenu();
        }

        if(ball.getBallEndY() >= paddleBottom.getY()) {

            if ((ball.getBallEndX() >= paddleBottom.getX()
                    && ball.getX() <= paddleBottom.getPaddleEndX())
                    || (ball.getX() >= 0
                    && ball.getX() <= paddleBottom.getPaddleEndX() - game.getScreenWidth())) {

                //la pallina ha toccato la barra inferiore

                ball.incrementSpeed();
                incrementPaddlesSpeed();
                ball.bounceOnPaddle(paddleBottom);
                game.incrementPoints();

                quakeBottom();
                printPoints();

            } else
                mainActivity.showGameOverMenu();
        }
    }

    private void printPoints(){

        /*MainActivity.txvPoints.post(new Runnable() {
            @Override
            public void run() {

                MainActivity.txvPoints.setText(String.valueOf(game.getPoints()));
            }
        });*/
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


    public void quakeRight(){

        MainActivity.gameView.post(new Runnable() {
            @Override
            public void run() {

                MainActivity.gameView
                        .animate()
                        .translationX(8)
                        .setDuration(80)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {

                                MainActivity.gameView
                                        .animate()
                                        .translationX(-4)
                                        .setDuration(80)
                                        .withEndAction(new Runnable() {
                                            @Override
                                            public void run() {

                                                MainActivity.gameView
                                                        .animate()
                                                        .translationX(0)
                                                        .setDuration(40)
                                                        .start();
                                            }
                                        }).start();
                            }
                        }).start();
            }
        });
    }

    public void quakeLeft(){

        MainActivity.gameView.post(new Runnable() {
            @Override
            public void run() {

                MainActivity.gameView
                        .animate()
                        .translationX(-8)
                        .setDuration(80)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {

                                MainActivity.gameView
                                        .animate()
                                        .translationX(+4)
                                        .setDuration(80)
                                        .withEndAction(new Runnable() {
                                            @Override
                                            public void run() {

                                                MainActivity.gameView
                                                        .animate()
                                                        .translationX(0)
                                                        .setDuration(40)
                                                        .start();
                                            }
                                        }).start();
                            }
                        }).start();
            }
        });
    }

    public void quakeTop(){

        MainActivity.gameView.post(new Runnable() {
            @Override
            public void run() {

                MainActivity.gameView
                        .animate()
                        .translationY(-8)
                        .setDuration(80)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {

                                MainActivity.gameView
                                        .animate()
                                        .translationY(+4)
                                        .setDuration(80)
                                        .withEndAction(new Runnable() {
                                            @Override
                                            public void run() {

                                                MainActivity.gameView
                                                        .animate()
                                                        .translationY(0)
                                                        .setDuration(40)
                                                        .start();
                                            }
                                        }).start();
                            }
                        }).start();
            }
        });
    }

    public void quakeBottom(){

        MainActivity.gameView.post(new Runnable() {
            @Override
            public void run() {

                MainActivity.gameView
                        .animate()
                        .translationY(8)
                        .setDuration(80)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {

                                MainActivity.gameView
                                        .animate()
                                        .translationY(-4)
                                        .setDuration(80)
                                        .withEndAction(new Runnable() {
                                            @Override
                                            public void run() {

                                                MainActivity.gameView
                                                        .animate()
                                                        .translationY(0)
                                                        .setDuration(40)
                                                        .start();
                                            }
                                        }).start();
                            }
                        }).start();
            }
        });
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

