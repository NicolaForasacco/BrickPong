package brickteamdevelopment.brickpong;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameUtils;

import java.lang.reflect.Method;
import java.util.Calendar;

public class MainActivity extends FragmentActivity implements  GoogleApiClient.ConnectionCallbacks,
                                                                GoogleApiClient.OnConnectionFailedListener {

    private SurfaceView             gameView;   //View dove viene disegnato il gioco
    private GameView                game;       //Oggetto che gestisce il thread del gioco e tutti i relativi oggetti
    public static TextView          txvPoints;  //Testo che mostra il punteggio durante il gioco

    private Typeface                type;       //Oggetto utilizzato per modificare il font del testo

    private AdView                  mAdView;
    public SharedPreferencesManager sharedPreferences;
    private GamesApiHandler         gamesApiHandler;

    private enum State {
        MENU,
        GAME,
        PAUSE,
        GAMEOVER
    }

    private State                   currentState;

    //region Variabili  di Play Games
    private static int              RC_SIGN_IN = 9001;
    private boolean                 mResolvingConnectionFailure = false,
                                    mAutoStartSignInflow        = true,
                                    mSignInClicked              = false;
    //endregion

    //region Activity lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        type    =   Typeface.createFromAsset(getAssets(), "fonts/Chava-Regular.otf") ;

        sharedPreferences   =   new SharedPreferencesManager(getApplicationContext());

        gameView    =   (SurfaceView) findViewById(R.id.gameView);
        game        =   new GameView(this, gameView);
        gameView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    game.isScreenTouched = true;

                else if (event.getAction() == MotionEvent.ACTION_UP)
                    game.isScreenTouched = false;

                return true;
            }
        });

        gamesApiHandler =   new GamesApiHandler(this, this);

        showAds();
        showMenu();
    }

    @Override
    public void onDestroy() {

        if (mAdView != null)
            mAdView.destroy();

        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        gamesApiHandler.connect();

        switch (currentState){
            case PAUSE:
                showPauseMenu();
                break;
            case GAMEOVER:
                showGameOverMenu();
                break;
            case GAME:
                showPauseMenu();
                break;
            case MENU:
                showMenu();
                break;
        }

       if (mAdView != null)
           mAdView.resume();
    }

    @Override
    protected void onStop() {

        gamesApiHandler.disconnect();
        super.onStop();
    }

    @Override
    protected void onPause() {

        if (mAdView != null)
            mAdView.pause();

        game.pause();

        super.onPause();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        hideSystemUI();
        super.onWindowFocusChanged(hasFocus);
    }

    //endregion


    private void showMenu(){

        currentState = State.MENU;

        //Si fa partire un thread che continua a disegnare la barra alla base del menu
        game.showMenu(getScreenDimension(),getBannerHeight());

        findViewById(R.id.lyt_menu).setVisibility(View.VISIBLE);
        findViewById(R.id.lyt_game).setVisibility(View.GONE);
        findViewById(R.id.final_menu).setVisibility(View.GONE);
        findViewById(R.id.lyt_pause).setVisibility(View.GONE);

        final LinearLayout btn_play     =   (LinearLayout)findViewById(R.id.play);
        final LinearLayout rate         =   (LinearLayout)findViewById(R.id.rate);
        final LinearLayout share        =   (LinearLayout)findViewById(R.id.share);
        final LinearLayout trophies     =   (LinearLayout)findViewById(R.id.trophies);

        ((TextView)findViewById(R.id.app_name)).setTypeface(type);
        ((TextView)findViewById(R.id.rate_us_txv)).setTypeface(type);

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

        rate.setOnTouchListener(new View.OnTouchListener() {

            private Rect rect;
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                rect = new Rect(v.getLeft(),
                        v.getTop(),
                        v.getRight(),
                        v.getBottom());

                if(actionChecker(rect, event, v, rate))
                    showGooglePlayGamePage();

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

                if(actionChecker(rect, event, v, btn_play))
                    showGame();

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

        findViewById(R.id.lyt_game).setVisibility(View.VISIBLE);
        findViewById(R.id.lyt_menu).setVisibility(View.GONE);
        findViewById(R.id.final_menu).setVisibility(View.GONE);
        findViewById(R.id.lyt_pause).setVisibility(View.GONE);
        findViewById(R.id.tutorial).setVisibility(View.GONE);

        txvPoints   =   (TextView) findViewById(R.id.points);
        txvPoints.setTypeface(type);
        txvPoints.setText("0");

        //Si fa partire un thread che si occupa di disegnare tutti gli elementi del gioco
        game.showGame(getScreenDimension(),
                    getBannerHeight(),
                    (SurfaceView)findViewById(R.id.gameView),gamesApiHandler);


        if(sharedPreferences.isFirstTime()){

            //Nel caso in cui sia la prima volta che l'utente gioca si mostra il tutorial dopo 300 millisecondi che il gioco è iniziato
            //Finche il tutorial non verrà chiuso non sarà possibile premere il tasto per mettere in pausa il gioco
            new CountDownTimer(300, 300) {

                public void onTick(long millisUntilFinished) {}
                public void onFinish() { showTutorial(); }
            }.start();

        }else
            enablePauseMenu();
    }

    public void showTutorial(){

        game.pause();

        final LinearLayout tutorialMenu             =   (LinearLayout)findViewById(R.id.tutorial);
        final ViewPager mPager                      =   (ViewPager) findViewById(R.id.pager);
        final TutorialPagerAdapter mPagerAdapter    =   new TutorialPagerAdapter(getSupportFragmentManager());

        mPager.setAdapter(mPagerAdapter);

        tutorialMenu.animate()
                .scaleY(0.8f)
                .scaleX(0.8f)
                .alpha(0)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {

                        tutorialMenu.setVisibility(View.VISIBLE);
                        tutorialMenu.animate()
                                .scaleY(1)
                                .scaleX(1)
                                .alpha(1)
                                .setDuration(200).start();
                    }
                }).start();


        findViewById(R.id.close).setOnTouchListener(new View.OnTouchListener() {

            private Rect rect;
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                rect = new Rect(v.getLeft(),
                        v.getTop(),
                        v.getRight(),
                        v.getBottom());

                if (actionChecker(rect, event, v, findViewById(R.id.close))) {

                    findViewById(R.id.tutorial).setVisibility(View.GONE);

                    enablePauseMenu();

                    game.resume();
                }

                return true;
            }
        });

        findViewById(R.id.next).setOnTouchListener(new View.OnTouchListener() {

            private Rect rect;
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                rect = new Rect(v.getLeft(),
                        v.getTop(),
                        v.getRight(),
                        v.getBottom());

                if (actionChecker(rect, event, v, findViewById(R.id.next))) {

                    if (mPager.getCurrentItem() < mPagerAdapter.getCount() - 1)
                        mPager.setCurrentItem(mPager.getCurrentItem() + 1, true);
                    else {

                        findViewById(R.id.tutorial).setVisibility(View.GONE);

                        enablePauseMenu();

                        game.resume();
                    }
                }

                return true;
            }
        });

        findViewById(R.id.prev).setOnTouchListener(new View.OnTouchListener() {

            private Rect rect;
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                rect = new Rect(v.getLeft(),
                        v.getTop(),
                        v.getRight(),
                        v.getBottom());

                if (actionChecker(rect, event, v, findViewById(R.id.prev)))
                    if (mPager.getCurrentItem() != 0)
                        mPager.setCurrentItem(mPager.getCurrentItem() - 1,true);

                return true;
            }
        });
    }

    /*
    *  Metodo che una volta chiamato permetterà all'utente di mettere in pausa il gioco
    *  ovvero abiliterà il relativo pulsante
    */
    public void enablePauseMenu(){

        final LinearLayout btnPause = (LinearLayout)findViewById(R.id.btn_pause);

        btnPause.setOnTouchListener(new View.OnTouchListener() {

            private Rect rect;
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                rect = new Rect(v.getLeft(),
                                v.getTop(),
                                v.getRight(),
                                v.getBottom());

                if(actionChecker(rect, event, v, btnPause))
                    showPauseMenu();

                return true;
            }
        });
    }

    public void showPauseMenu() {

        currentState = State.PAUSE;
        game.pause();

        findViewById(R.id.lyt_pause).setVisibility(View.VISIBLE);
        findViewById(R.id.tutorial).setVisibility(View.GONE);

        final LinearLayout resume   = (LinearLayout) findViewById(R.id.resume);
        final LinearLayout retry    = (LinearLayout) findViewById(R.id.retry);
        final LinearLayout backhome = (LinearLayout) findViewById(R.id.back_home);

        TextView best_label         =   (TextView)findViewById(R.id.pause_best_label);
        TextView score_txv          =   ((TextView)findViewById(R.id.pause_score));
        TextView best_txv           =   ((TextView)findViewById(R.id.pause_best));

        ((TextView)findViewById(R.id.pause)).setTypeface(type);
        ((TextView)findViewById(R.id.pause_score_label)).setTypeface(type);
        best_label.setTypeface(type);
        score_txv.setTypeface(type);
        best_txv.setTypeface(type);

        resume.setOnTouchListener(new View.OnTouchListener() {
            private Rect rect;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                rect = new Rect(v.getLeft(),
                        v.getTop(),
                        v.getRight(),
                        v.getBottom());

                if (actionChecker(rect, event, v, resume)) {
                    (findViewById(R.id.lyt_pause)).setVisibility(View.INVISIBLE);
                    game.resume();
                }

                return true;
            }
        });

        retry.setOnTouchListener(new View.OnTouchListener() {

            private Rect rect;
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                rect = new Rect(v.getLeft(),
                        v.getTop(),
                        v.getRight(),
                        v.getBottom());

                if (actionChecker(rect, event, v, retry))
                    showGame();

                return true;
            }
        });

        backhome.setOnTouchListener(new View.OnTouchListener() {

            private Rect rect;
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                rect = new Rect(v.getLeft(),
                        v.getTop(),
                        v.getRight(),
                        v.getBottom());

                if (actionChecker(rect, event, v, backhome))
                    showMenu();

                return true;
            }
        });

        if(sharedPreferences.checkForRecord(game.game.getPoints())){

            best_label.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
            best_label.setTextColor(Color.WHITE);
        }else {

            best_label.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.light_orange));
            best_label.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
        }

        score_txv.setText(String.valueOf(game.game.getPoints()));
        best_txv.setText(String.valueOf(sharedPreferences.getRecord()));
    }

    public void showGameOverMenu(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                game.pause();

                currentState = State.GAMEOVER;
                final int score = game.game.getPoints();

                findViewById(R.id.lyt_game).setVisibility(View.INVISIBLE);
                findViewById(R.id.final_menu).setVisibility(View.VISIBLE);

                final LinearLayout play_final       =   (LinearLayout) findViewById(R.id.final_play);
                final LinearLayout share_final      =   (LinearLayout) findViewById(R.id.final_share);
                final LinearLayout trophies_final   =   (LinearLayout) findViewById(R.id.final_trophies);

                TextView best_label                 =   (TextView)findViewById(R.id.best_label);
                TextView score_txv                  =   ((TextView)findViewById(R.id.score));
                TextView best_txv                   =   ((TextView)findViewById(R.id.best));

                ((TextView)findViewById(R.id.score_label)).setTypeface(type);
                ((TextView)findViewById(R.id.game_over)).setTypeface(type);
                best_label.setTypeface(type);
                score_txv.setTypeface(type);
                best_txv.setTypeface(type);

                trophies_final.setOnTouchListener(new View.OnTouchListener() {

                    private Rect rect;
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        rect = new Rect(v.getLeft(),
                                v.getTop(),
                                v.getRight(),
                                v.getBottom());

                        if(actionChecker(rect, event, v, trophies_final))
                            showLeaderboards();

                        return true;
                    }
                });

                share_final.setOnTouchListener(new View.OnTouchListener() {

                    private Rect rect;
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        rect = new Rect(v.getLeft(),
                                        v.getTop(),
                                        v.getRight(),
                                        v.getBottom());

                        if(actionChecker(rect, event, v, share_final))
                            share("I scored " + String.valueOf(score) + " in BrickPong!\nCan you beat me?");

                        return true;
                    }
                });

                play_final.setOnTouchListener(new View.OnTouchListener() {

                    private Rect rect;
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        rect = new Rect(v.getLeft(),
                                v.getTop(),
                                v.getRight(),
                                v.getBottom());

                        if(actionChecker(rect, event, v, play_final))
                            showGame();

                        return true;
                    }
                });


                gamesApiHandler.updateRecord(sharedPreferences.getRecord());
                gamesApiHandler.updateRecord(score);          //Si aggiorna il record dell'utente in google play games
                gamesApiHandler.checkForAchievements(score);  //Si controlla il raggiungimento di eventuali risultati

                if(sharedPreferences.checkForRecord(score)){   //Si aggiorna il record nelle sharedPreferences

                    best_label.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                    best_label.setTextColor(Color.WHITE);
                }else {

                    best_label.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.light_orange));
                    best_label.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                }

                score_txv.setText(String.valueOf(score));
                best_txv.setText(String.valueOf(sharedPreferences.getRecord()));
            }
        });
    }


    private void showGooglePlayGamePage(){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("market://details?id=" + getPackageName()));
        startActivity(i);
    }

    private void showAds(){

        mAdView             =   (AdView) findViewById(R.id.adView);
        AdRequest adRequest =   new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
               .build();
        mAdView.loadAd(adRequest);
    }



    /*
    *  Metodo che mostra le classifiche su play games
    */
    public void showLeaderboards(){

        if(gamesApiHandler.checkConnection()){
            startActivityForResult( Games.Leaderboards.getLeaderboardIntent(gamesApiHandler.getApiClient(),
                    getResources().getString(R.string.leaderboard_score)), 0) ;
        }else{
            gamesApiHandler.connect();
            Toast.makeText(getApplicationContext(),"Google Play Games not connected", Toast.LENGTH_SHORT).show();
        }
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

        findViewById(R.id.adView).measure(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        return findViewById(R.id.adView).getMeasuredHeight();
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



    //region Metodi del servizio Play Games

    @Override
    public void onConnected(Bundle bundle) {}

    @Override
    public void onConnectionSuspended(int i) {

        gamesApiHandler.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        if (mResolvingConnectionFailure) {
            // already resolving
            return;
        }

        // if the sign-in button was clicked or if auto sign-in is enabled,
        // launch the sign-in flow
        if (mSignInClicked || mAutoStartSignInflow) {

            mAutoStartSignInflow        =   false;
            mSignInClicked              =   false;
            mResolvingConnectionFailure =   true;

            // Attempt to resolve the connection failure using BaseGameUtils.
            // The R.string.signin_other_error value should reference a generic
            // error string in your strings.xml file, such as "There was
            // an issue with sign-in, please try again later."
            if (!BaseGameUtils.resolveConnectionFailure(this,
                                                        gamesApiHandler.getApiClient(),
                                                        connectionResult,
                                                        RC_SIGN_IN, "")) {
                mResolvingConnectionFailure = false;
            }
        }
        // Put code here to display the sign-in button
    }

    //endregion

    //Adapter utilizzato per il tutorial
    public class TutorialPagerAdapter extends FragmentStatePagerAdapter {

        public TutorialPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return TutorialFragment.create(position);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
