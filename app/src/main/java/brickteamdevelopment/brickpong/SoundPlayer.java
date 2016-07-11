package brickteamdevelopment.brickpong;

import android.media.MediaPlayer;
import android.provider.MediaStore;

public class SoundPlayer extends Thread {

    Thread mpThread;

    MediaPlayer mp;

    public SoundPlayer(){}

    public void setMediaPlayer(MediaPlayer mp){
        this.mp = mp;
    }

    public void start(){

        mpThread = new Thread(this);
        mpThread.start();
    }

    @Override
    public void run() {
        mp.start();
        super.run();
    }
}
