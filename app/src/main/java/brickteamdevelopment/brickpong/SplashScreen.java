package brickteamdevelopment.brickpong;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class SplashScreen extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Typeface type           =   Typeface.createFromAsset(getAssets() , "fonts/Chava-Regular.otf") ;
        TextView presents       =   ((TextView)findViewById(R.id.txv_presents));
        final int splashTime    =   1000;

        presents.setTypeface(type);

        findViewById(R.id.imgLogo).animate().alpha(1).setDuration(1000).start();
        findViewById(R.id.txv_presents).animate().alpha(1).setDuration(1000).withEndAction(new Runnable() {
            @Override
            public void run() {
                new CountDownTimer(splashTime, 300) {

                    public void onTick(long millisUntilFinished) { }
                    public void onFinish() {
                        startActivity(new Intent(SplashScreen.this, MainActivity.class));
                        finish();

                    }
                }.start();
            }
        }).start();

    }

    // This snippet hides the system bars.
    private void hideSystemUI() {


        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else if(Build.VERSION.SDK_INT >= 19) {
            // Set the IMMERSIVE flag.
            // Set the content to appear under the system bars so that the content
            // doesn't resize when the system bars hide and show.

            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        hideSystemUI();
        super.onWindowFocusChanged(hasFocus);
    }
}
