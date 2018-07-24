package com.kotakawanishi.nanikore;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by kota on 2017/05/13.
 */

public class MediaPlayerActivity extends AppCompatActivity {


    static protected MediaPlayer mp;
    static protected MediaPlayer resultSound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mp == null) {
            mp = MediaPlayer.create(this, R.raw.main_bgm);
            mp.setLooping(true);
        }

    }


    @Override
    protected void onDestroy(){
        mp.stop();
        if(mp != null)
            mp.reset();
        mp.release();
        resultSound.reset();
        resultSound.release();
        resultSound = null;
        mp = null;
    }

    protected void mpChange(int intSong) {
        mp = MediaPlayer.create(this, intSong);
        mp.setLooping(true);
    }

    protected void mpStart() {
        if (!mp.isPlaying()) {
            mp.seekTo(0);
            mp.start();
        }
    }

    protected void resultSoundCreate(boolean result){
        if(result){
            resultSound = MediaPlayer.create(this, R.raw.clear_sound);
        }else{
            resultSound = MediaPlayer.create(this,R.raw.failure_sound);
        }
        mp.setLooping(false);
    }

    protected void resultSoundStart(){
        resultSound.start();
    }

    protected void resultSoundStop(){
        if(resultSound.isPlaying())
            resultSound.stop();
    }


    protected void mpStop() {
        if (mp.isPlaying()) {
            mp.pause();
        }
    }

    protected void setVolume(float leftVolume, float rightVolume){
        if(mp.isPlaying()){
            mp.setVolume(leftVolume, rightVolume);
        }
    }

}
