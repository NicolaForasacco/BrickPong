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
    private Paddle          paddle;
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
     * Vengono inizializzato il paddle del menu e viene fatto partire il thread per mostrarlo
     */
    public void showMenu(Point dim, int bannerHeight) {

        pause();

        Paint paintWhite = new Paint();
        paintWhite.setColor(Color.WHITE);

        paddle      =   new Paddle(dim.x,
                                dim.y - bannerHeight,
                                true);

        game        =   new Game(dim.x,
                                dim.y - bannerHeight,
                                "#B71C1C");

        fps         =   (long) paddle.speed;
        gameType    =   "menu";
        playing     =   true;
        gameThread  =   new Thread(this);

        gameThread.start();
    }

    /*
     * Vengono inizializzati i vari oggetti del gioco e viene fatto partire il thread che li disegnerà
     */
    public void showGame(Point dim,int bannerHeight,SurfaceView view){

        pause();

        Paint paintWhite = new Paint();
        paintWhite.setColor(Color.WHITE);

        paddle      =   new Paddle(dim.x,
                                dim.y - bannerHeight,
                                false);

        game        =   new Game(dim.x,
                                dim.y - bannerHeight,
                                "#B71C1C");

        ball        =   new Ball(dim.x,
                                dim.y - bannerHeight);

        fps         =   (long) paddle.speed;
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
     * Vengono aggiornati coordinate e velocità del paddle
     */
    public void updateMenu(){

        paddle.incrementX();
        if (paddle.getX() >= game.getScreenWidth())
            paddle.setX(0);

        paddle.incrementMovement(fps);
    }

    public void drawMenu() {

        if (ourHolder.getSurface().isValid()) {

            canvas = ourHolder.lockCanvas();
            if(canvas != null) {

                game.draw(canvas);

                paddle.draw(canvas, game.getScreenWidth());

                ourHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    /*
     * Vengono aggiornati coordinate e velocità di paddle e palla
     */
    public void updateGame(){

        paddle.incrementX();
        if (paddle.getX() >= game.getScreenWidth())
            paddle.setX(0);

        if (isScreenTouched)
            paddle.incrementMovement(fps);
        else
            paddle.decrementMovement(fps);

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

        if (ball.getY() <= 0) {

            //se la pallina tocca il limite superiore
            ball.changeMovementY();
            game.incrementPoints();

            MainActivity.txvPoints.post(new Runnable() {
                @Override
                public void run() {
                    MainActivity.txvPoints.setText(String.valueOf(game.getPoints()));
                }
            });
        }

        if (ball.getPaddleEndY() >= (game.getScreenHeight() - paddle.getHeight())) {

            //se la pallina tocca il limite inferiore
            if ((ball.getPaddleEndX() >= paddle.getX()
                    && ball.getX() <= paddle.getPaddleEndX())
                    || (ball.getX() >= 0
                    && ball.getX() <= paddle.getPaddleEndX() - game.getScreenWidth())) {

                //la pallina ha toccato la barra quindi torna in alto

                ball.incrementSpeed();
                paddle.incrementMaxSpeed();

                ball.bounceOnPaddle(paddle);

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

                paddle.draw(canvas, game.getScreenWidth());

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

