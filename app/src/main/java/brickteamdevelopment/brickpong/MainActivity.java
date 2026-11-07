package brickteamdevelopment.brickpong;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.lang.reflect.Method;
import java.util.Random;

public class MainActivity extends Activity  {

    public static SurfaceView       gameView;
    private GameView                game;
    public static TextView          txvPoints;

    private Typeface                type;
    public SharedPreferencesManager sharedPreferences;

    public int score;
    private ImageView btn_play;
    private ImageView share;
    private ImageView trophies;

    private TextView rateUs;
    private LinearLayout leftArrow;
    private LinearLayout rightArrow;
    private TextView title;

    private enum State {
        MENU,
        GAME,
        PAUSE,
        GAMEOVER
    }

    private State                   currentState;

    //region Activity lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        type    =   Typeface.createFromAsset(getAssets(), "fonts/Chava-Regular.otf") ;

        sharedPreferences   =   new SharedPreferencesManager(getApplicationContext());
        gameView            =   (SurfaceView) findViewById(R.id.gameView);

        game                =   new GameView(this,
                                                gameView);

        btn_play    =   (ImageView)findViewById(R.id.play);
        share       =   (ImageView)findViewById(R.id.share);
        trophies    =   (ImageView)findViewById(R.id.trophies);
        rateUs      =   (TextView)findViewById(R.id.rate_us);
        leftArrow   =   (LinearLayout) findViewById(R.id.left_arrow);
        rightArrow  =   (LinearLayout)findViewById(R.id.right_arrow);
        title       =   (TextView)findViewById(R.id.title);

        rateUs.setOnTouchListener(new View.OnTouchListener() {
            private Rect rect;
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                rect = new Rect(v.getLeft(),
                        v.getTop(),
                        v.getRight(),
                        v.getBottom());

                if(actionChecker(rect, event, v, rateUs))
                    showGooglePlayGamePage();

                return true;
            }
        });
        showMenu();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        switch (currentState){
            case PAUSE:
                game.resume();
                break;
            case GAMEOVER:
                showGameOverMenu();
                break;
            case GAME:
                game.resume();
                break;
            case MENU:
                showMenu();
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {

        game.pause();

        super.onPause();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        hideSystemUI();
        super.onWindowFocusChanged(hasFocus);

        switch (currentState){
            case PAUSE:{
                game.drawGame();
                break;
            }
        }

    }

    //endregion


    private void showMenu(){

        currentState = State.MENU;

        game.showGame(getScreenDimension(), getBannerHeight(), false);

        if(new Random().nextBoolean())
            game.goRight();
        else
            game.goLeft();

        findViewById(R.id.lyt_menu).setVisibility(View.VISIBLE);

        TextView best_label     =   (TextView)findViewById(R.id.best_label_menu);
        TextView best_txv       =   (TextView)findViewById(R.id.best_menu);


        best_label.setTypeface(type);
        best_txv.setTypeface(type);
        rateUs.setTypeface(type);
        ((TextView)findViewById(R.id.title)).setTypeface(type);

        best_txv.setText(String.valueOf(sharedPreferences.getRecord()));


        share.setOnTouchListener(new View.OnTouchListener() {

            private Rect rect;
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                rect = new Rect(v.getLeft(),
                                v.getTop(),
                                v.getRight(),
                                v.getBottom());

                if(actionChecker(rect, event, v, share))
                    share("My record in BrickPong is " + String.valueOf(sharedPreferences.getRecord()) + "\nCan you beat me?");

                return true;
            }
        });

        btn_play.setOnTouchListener(new View.OnTouchListener() {

            private Rect rect;
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                rect = new Rect(v.getLeft(),
                                v.getTop(),
                                v.getRight(),
                                v.getBottom());

                if(actionChecker(rect, event, v, btn_play)) {

                    findViewById(R.id.best_lyt).animate().scaleX(0.4f).scaleY(0.4f).alpha(0.8f).withEndAction(new Runnable() {
                        @Override
                        public void run() {

                            showGame();
                        }
                    }).setDuration(300).start();
                }

                return true;
            }
        });

        trophies.setOnTouchListener(new View.OnTouchListener() {

            private Rect rect;
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                rect = new Rect(v.getLeft(),
                                v.getTop(),
                                v.getRight(),
                                v.getBottom());

                if(actionChecker(rect, event, v, trophies))
                    showLeaderboards();

                return true;
            }
        });
    }

    public void showGame(){

        currentState = State.GAME;

        rateUs.setVisibility(View.INVISIBLE);
        findViewById(R.id.score_layout).setVisibility(View.INVISIBLE);
        findViewById(R.id.best_lyt).setVisibility(View.INVISIBLE);
        findViewById(R.id.lyt_menu).setVisibility(View.GONE);

        findViewById(R.id.points).setVisibility(View.VISIBLE);
        findViewById(R.id.lyt_game).setVisibility(View.VISIBLE);

        title.setText("");

        txvPoints   =   (TextView) findViewById(R.id.points);
        txvPoints.setTypeface(type);
        txvPoints.setText("0");

        game.showGame(getScreenDimension(), getBannerHeight(), true);

        leftArrow.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN){

                    leftArrow.animate()
                            .scaleX(0.95f)
                            .scaleY(0.95f)
                            .setDuration(50).start();

                   game.goLeft();

                } else if(event.getAction() == MotionEvent.ACTION_UP){

                    leftArrow.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(50).start();

                    game.stopPaddle();
                }

                return true;
            }
        });

        rightArrow.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {


                if (event.getAction() == MotionEvent.ACTION_DOWN){

                    rightArrow.animate()
                            .scaleX(0.95f)
                            .scaleY(0.95f)
                            .setDuration(50).start();

                    game.goRight();

                } else if(event.getAction() == MotionEvent.ACTION_UP){

                    rightArrow.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(50).start();

                    game.stopPaddle();
                }
                return true;
            }
        });
    }

    public void showGameOverMenu(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                game.pause();

                currentState = State.GAMEOVER;

                findViewById(R.id.points).setVisibility(View.INVISIBLE);
                findViewById(R.id.lyt_game).setVisibility(View.GONE);

                findViewById(R.id.lyt_menu).setVisibility(View.VISIBLE);

                rateUs.setVisibility(View.VISIBLE);
                title.setVisibility(View.VISIBLE);

                title.setTextColor(Color.RED);
                title.setText(R.string.game_over);

                score = (int)((game.game.getPoints() >= 0) ? game.game.getPoints() : score);

                game.showGame(getScreenDimension(), getBannerHeight(), false);

                if(new Random().nextBoolean())
                    game.goRight();
                else
                    game.goLeft();


                findViewById(R.id.score_layout).animate().scaleX(0.5f).scaleY(0.5f).alpha(0.5f).withEndAction(new Runnable() {
                    @Override
                    public void run() {

                        findViewById(R.id.score_layout).setVisibility(View.VISIBLE);
                        findViewById(R.id.score_layout).animate().scaleX(1).scaleY(1).alpha(1).setDuration(250).start();
                    }
                }).start();


                TextView best_label                 =   (TextView)findViewById(R.id.best_label);
                TextView score_txv                  =   ((TextView)findViewById(R.id.score));
                TextView best_txv                   =   ((TextView)findViewById(R.id.best));

                best_label.setTypeface(type);
                score_txv.setTypeface(type);
                best_txv.setTypeface(type);
                ((TextView)findViewById(R.id.score_label)).setTypeface(type);



                if(!sharedPreferences.checkForRecord(score)){   //Si aggiorna il record nelle sharedPreferences

                    best_label.setBackgroundColor(Color.TRANSPARENT);
                    best_label.setTextColor(Color.WHITE);
                }else {

                    best_label.setBackgroundColor(Color.GREEN);
                    best_label.setTextColor(Color.BLACK);
                }

                score_txv.setText(String.valueOf(score));
                best_txv.setText(String.valueOf(sharedPreferences.getRecord()));

                title.setTypeface(type);

                share.setOnTouchListener(new View.OnTouchListener() {

                    private Rect rect;
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        rect = new Rect(v.getLeft(),
                                        v.getTop(),
                                        v.getRight(),
                                        v.getBottom());

                        if(actionChecker(rect, event, v, share))
                            share("My record in BrickPong is " + String.valueOf(sharedPreferences.getRecord()) + "\nCan you beat me?");

                        return true;
                    }
                });

                btn_play.setOnTouchListener(new View.OnTouchListener() {

                    private Rect rect;
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        rect = new Rect(v.getLeft(),
                                        v.getTop(),
                                        v.getRight(),
                                        v.getBottom());

                        if(actionChecker(rect, event, v, btn_play)){
                            findViewById(R.id.score_layout).animate().scaleX(0.4f).scaleY(0.4f).alpha(0.8f).withEndAction(new Runnable() {
                                @Override
                                public void run() {

                                    showGame();
                                }
                            }).setDuration(300).start();
                        }

                        return true;
                    }
                });

                trophies.setOnTouchListener(new View.OnTouchListener() {

                    private Rect rect;
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        rect = new Rect(v.getLeft(),
                                        v.getTop(),
                                        v.getRight(),
                                        v.getBottom());

                        if(actionChecker(rect, event, v, trophies))
                            showLeaderboards();

                        return true;
                    }
                });
            }
        });
    }


    private void showGooglePlayGamePage(){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("market://details?id=" + getPackageName()));
        startActivity(i);
    }

    public void showLeaderboards(){
        Toast.makeText(this, getString(R.string.best) + ": " + sharedPreferences.getRecord(), Toast.LENGTH_SHORT).show();
    }

    /*
     * Metodo che si occupa di gestire le animazioni dei bottoni
     */
    public boolean actionChecker(Rect rect, MotionEvent event, View v, View layout){

        if ( event.getAction() == MotionEvent.ACTION_DOWN ){

            layout.animate()
                    .scaleX(0.9f)
                    .scaleY(0.9f)
                    .setDuration(50).start();

            return false;

        } else if(event.getAction() == MotionEvent.ACTION_UP){

            layout.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(50).start();

            if (rect != null && !rect.contains(v.getLeft() + (int) event.getX(),
                                                v.getTop() + (int) event.getY()))
                return false;
            else
                return true;

        }
        return false;
    }


        public int getBannerHeight(){
        return 0;
    }

    public Point getScreenDimension(){

        final DisplayMetrics metrics    = new DisplayMetrics();
        Display display                 = getWindowManager().getDefaultDisplay();
        Method  mGetRawH                = null,
                mGetRawW                = null;

        int     realWidth   = 0,
                realHeight  = 0;

        try {
            // For JellyBeans and onward
            if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN){
                display.getRealMetrics(metrics);

                realWidth   =   metrics.widthPixels;
                realHeight  =   metrics.heightPixels;
            }else{
                mGetRawH    =   Display.class.getMethod("getRawHeight");
                mGetRawW    =   Display.class.getMethod("getRawWidth");

                realWidth   =   (Integer) mGetRawW.invoke(display);
                realHeight  =   (Integer) mGetRawH.invoke(display);
            }
        }catch(Exception e){}

        return new Point(realWidth,realHeight);
    }

    private void share(String mex){

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, mex + "\n" + getString(R.string.try_brick_pong));

        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share using "));
    }

    private void hideSystemUI() {

        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else if(Build.VERSION.SDK_INT >= 19) {

            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
