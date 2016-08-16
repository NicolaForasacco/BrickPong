package brickteamdevelopment.brickpong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class GameView implements Runnable {

    private Thread          gameThread = null;
    private SurfaceHolder   ourHolder;
    private Canvas          canvas;
    private Paint           paint;
    public Game             game;
    private Ball            ball;

    public boolean          playing,
                            isScreenTouched;

    private long            fps = 1;
    private long            timeThisFrame;

    private MainActivity    mainActivity;
    private String          gameType;
    private SoundPlayer     paddleBounceSound,
                            limitBounceSound;

    private PaddleTop       paddleTop;
    private PaddleBottom    paddleBottom;

    public GameView(MainActivity mainActivity, SurfaceView view) {

        ourHolder           =   view.getHolder();
        paint               =   new Paint();
        this.mainActivity   =   mainActivity;
    }

    @Override
    public void run() {

        while (playing) {

            long startFrameTime = System.currentTimeMillis();

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

    /*
     * Vengono inizializzato il paddle2 del menu e viene fatto partire il thread per mostrarlo
     */
    public void showMenu(Point dim, int bannerHeight) {

        pause();

        Paint paintWhite = new Paint();
        paintWhite.setColor(Color.WHITE);

        paddleBottom =   new PaddleBottom(dim.x,
                                dim.y - bannerHeight,
                                true);

        game        =   new Game(dim.x,
                                dim.y - bannerHeight,
                                "#B71C1C",
                                null);

        fps         =   (long) paddleBottom.speed;
        gameType    =   "menu";
        playing     =   true;
        gameThread  =   new Thread(this);

        gameThread.start();
    }

    /*
     * Vengono inizializzati i vari oggetti del gioco e viene fatto partire il thread che li disegnerà
     */
    public void showGame(Point dim,int bannerHeight,SurfaceView view,GamesApiHandler gamesApiHandler){

        pause();

        Paint paintWhite = new Paint();
        paintWhite.setColor(Color.WHITE);

        paddleTop =   new PaddleTop(dim.x,
                dim.y - bannerHeight,
                false);

        paddleBottom =   new PaddleBottom(dim.x,
                                dim.y - bannerHeight,
                                false);

        game        =   new Game(dim.x,
                                dim.y - bannerHeight,
                                "#B71C1C"
                                ,gamesApiHandler);

        ball        =   new Ball(dim.x,
                                dim.y - bannerHeight);

        fps         =   (long) paddleBottom.speed;
        gameType    =   "game";
        playing     =   true;
        gameThread  =   new Thread(this);

        gameThread.start();
    }

    private void soundPlayerInit(){

        limitBounceSound    =   new SoundPlayer();
        paddleBounceSound   =   new SoundPlayer();

        limitBounceSound.setMediaPlayer(MediaPlayer.create(mainActivity.getApplicationContext(),R.raw.blip_select26));
        paddleBounceSound.setMediaPlayer(MediaPlayer.create(mainActivity.getApplicationContext(),R.raw.jump83));
    }

    /*
     * Vengono aggiornati coordinate e velocità del paddle2
     */
    public void updateMenu(){

        paddleBottom.incrementX();
        if (paddleBottom.getX() >= game.getScreenWidth())
            paddleBottom.setX(0);

        paddleBottom.incrementMovement(fps);
    }

    public void drawMenu() {

        if (ourHolder.getSurface().isValid()) {

            canvas = ourHolder.lockCanvas();
            if(canvas != null) {

                game.draw(canvas);

                paddleBottom.draw(canvas, game.getScreenWidth());

                ourHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    /*
     * Vengono aggiornati coordinate e velocità di paddle2 e palla
     */
    public void updateGame(){

        paddleTop.incrementX();
        if (paddleTop.getX() <= 0)
            paddleTop.setX(game.getScreenWidth());

        if (isScreenTouched)
            paddleTop.incrementMovement(fps);
        else
            paddleTop.decrementMovement(fps);

        paddleBottom.incrementX();
        if (paddleBottom.getX() >= game.getScreenWidth())
            paddleBottom.setX(0);

        if (isScreenTouched)
            paddleBottom.incrementMovement(fps);
        else
            paddleBottom.decrementMovement(fps);

        ball.incrementCoordinates(fps);

        if ((ball.getPaddleEndX() >= game.getScreenWidth()) || (ball.getX() <= 0)) {

            //se la pallina tocca il limite destro o sinistro
            ball.changeMovementX();

            game.incrementPoints();
            MainActivity.txvPoints.post(new Runnable() {
                @Override
                public void run() {
                    MainActivity.txvPoints.setText(String.valueOf(game.getPoints()));
                }
            });

        }

        if (ball.getY() <= (paddleTop.getHeight())) {

            //se la pallina tocca il limite superiore
            if ((ball.getPaddleEndX() >= paddleTop.getX()
                    && ball.getX() <= paddleTop.getPaddleEndX())
                    || (ball.getX() >= 0
                    && ball.getX() <= paddleTop.getPaddleEndX() - game.getScreenWidth())) {

                //la pallina ha toccato la barra quindi torna in basso

                ball.incrementSpeed();
                paddleTop.incrementMaxSpeed();

                ball.bounceOnPaddle(paddleTop);

                game.incrementPoints();
                MainActivity.txvPoints.post(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.txvPoints.setText(String.valueOf(game.getPoints()));

                    }
                });
            } else {

                //la pallina non ha toccato la barra, il giocatore ha perso
                mainActivity.showGameOverMenu();
            }
        }

        if (ball.getPaddleEndY() >= (game.getScreenHeight() - paddleBottom.getHeight())) {

            //se la pallina tocca il limite inferiore
            if ((ball.getPaddleEndX() >= paddleBottom.getX()
                    && ball.getX() <= paddleBottom.getPaddleEndX())
                    || (ball.getX() >= 0
                    && ball.getX() <= paddleBottom.getPaddleEndX() - game.getScreenWidth())) {

                //la pallina ha toccato la barra quindi torna in alto

                ball.incrementSpeed();
                paddleBottom.incrementMaxSpeed();

                ball.bounceOnPaddle(paddleBottom);

                game.incrementPoints();
                MainActivity.txvPoints.post(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.txvPoints.setText(String.valueOf(game.getPoints()));

                    }
                });
            } else {

                //la pallina non ha toccato la barra, il giocatore ha perso
                mainActivity.showGameOverMenu();
            }
        }
    }

    public void drawGame(){

        if (ourHolder.getSurface().isValid()) {

            canvas = ourHolder.lockCanvas();

            if(canvas != null) {

                game.draw(canvas);

                paddleTop.draw(canvas, game.getScreenWidth());

                paddleBottom.draw(canvas, game.getScreenWidth());

                ball.draw(canvas);

                ourHolder.unlockCanvasAndPost(canvas);
            }
        }
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

