package edu.depaul.csc472.gamescreen.utility;

/**
 * Created by keertika on 11/20/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.View;

import edu.depaul.csc472.gamescreen.R;


public class Sound {

    private final static String TAG = "Sound";

    private MediaPlayer musicPlayer;
    private SoundPool soundPool;
    private int mSoundID;
    private boolean mLoaded;
    private float mVolume;

    public Sound(Activity activity) {

        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        float actVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mVolume = actVolume / maxVolume;

        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);

//      Build the SoundPool object
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder().setAudioAttributes(audioAttrib).setMaxStreams(6).build();
        } else soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                mLoaded = true;
            }
        });
        mSoundID = soundPool.load(activity, R.raw.balloon_pop, 1);
    }

    public void playSound(View v) {
        if (mLoaded) {
            soundPool.play(mSoundID, mVolume, mVolume, 1, 0, 1f);
        }
    }

    public void prepareMusicPlayer(Context context) {
        musicPlayer = MediaPlayer.create(context.getApplicationContext(), R.raw.pleasant_music);
        musicPlayer.setVolume(.5f, .5f);
        musicPlayer.setLooping(true);
    }

    public void playMusic() {
        if (musicPlayer != null) {
            musicPlayer.start();
        }
    }


}
