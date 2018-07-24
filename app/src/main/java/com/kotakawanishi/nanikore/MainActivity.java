package com.kotakawanishi.nanikore;

import android.content.SharedPreferences;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.nifty.cloud.mb.core.NCMB;

import java.security.SecureRandom;
import java.util.Random;


public class MainActivity extends MediaPlayerActivity implements TitleFragment.OnFragmentInteractionListener, GameLevelSelectFragment.OnFragmentInteractionListener, GameRecordFragment.OnFragmentInteractionListener, GameFragment.OnFragmentInteractionListener,GameOverFragment.OnFragmentInteractionListener, CheckAnswerFragment.OnFragmentInteractionListener{


    //Views
    private RelativeLayout mainLayout;
    private AdView bannerView;
    private InterstitialAd mInterstitialAd;

    //Data
    private Point referenceSize;
    private boolean soundOn = true;
    boolean backFromDead = false;

    //Sound
    private SoundPool mSoundPool;
    int mSoundResId2;
    int mSoundResId;
    private int mSoundResIdCorrect;
    private int mSoundResIdWrong;
    private int mSoundResIdCountDown;
    private int mSoundResIdCountDownFinished;
    private int mSoundResIdStageClear;
    private int mSoundResIdStampSound;

    //Others
    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener;
    static final String STATEKEY = "STATEKEY";
    static final String SOUNDKEY = "SOUNDKEY";
    private String answer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        NCMB.initialize(this.getApplicationContext(), getString(R.string.app_key), getString(R.string.client_key));
        MobileAds.initialize(getApplicationContext(), getString(R.string.app_id));
        bannerView = (AdView) findViewById(R.id.adView);
        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        if(savedInstanceState != null) {
            backFromDead = savedInstanceState.getBoolean(STATEKEY);
            soundOn = savedInstanceState.getBoolean(SOUNDKEY);
        }
        setGlobalLayoutListener();
        this.showBanner();
        this.configureSound();
        setReferenceSize();
        this.configureInterstitial();
        this.startMainFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        super.setVolume(1.0f, 1.0f);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        mpStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mpStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.releaseSound();
    }


    private void setReferenceSize() {
        Display display = this.getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        referenceSize = point;
    }

    @Override
    public Point getReferenceSize() {
        return referenceSize;
    }

    private void setGlobalLayoutListener(){
        globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                resizeReferenceSize();
                mainLayout.getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
            }
        };
        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
    }

    private void resizeReferenceSize(){
        referenceSize = new Point(referenceSize.x, mainLayout.getHeight());
    }

    private void startMainFragment(){
        TitleFragment fragment = new TitleFragment();
        // Fragmentの追加や削除といった変更を行う際は、Transactionを利用します
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 新しく追加を行うのでaddを使用します
        // 他にも、よく使う操作で、replace removeといったメソッドがあります
        // メソッドの1つ目の引数は対象のViewGroupのID、2つ目の引数は追加するfragment
        if(!backFromDead)
            transaction.add(R.id.mainLayout, fragment);
        else
            transaction.replace(R.id.mainLayout, fragment);
        // 最後にcommitを使用することで変更を反映します
        transaction.commit();
        backFromDead = false;
    }


    protected void configureSound() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).build();
        mSoundPool = new SoundPool.Builder().setMaxStreams(2).setAudioAttributes(audioAttributes).build();
        mSoundResId = mSoundPool.load(this, R.raw.decision_sound, 1);
        mSoundResId2 = mSoundPool.load(this, R.raw.touch_sound, 1);
        mSoundResIdCorrect = mSoundPool.load(this, R.raw.correct, 1);
        mSoundResIdWrong = mSoundPool.load(this, R.raw.wrong, 1);
        mSoundResIdCountDown = mSoundPool.load(this, R.raw.count_down, 1);
        mSoundResIdCountDownFinished = mSoundPool.load(this, R.raw.count_down_done, 1);
        mSoundResIdStageClear = mSoundPool.load(this, R.raw.stage_clear, 1);
        mSoundResIdStampSound = mSoundPool.load(this, R.raw.stamp_sound, 1);

        if (soundOn)
            mpStart();
    }


    public boolean switchSound(){
        soundOn = !soundOn;
        if (soundOn) {
            mpStart();
            return true;
        } else {
            mpStop();
            return false;
        }
    }

    public void turnDownVolume(){
        mp.setVolume(0.7f, 0.7f);
    }

    public void stopSound(){
        mp.pause();
    }

    public void goToGameLevelFragment(){
        GameLevelSelectFragment fragment = new GameLevelSelectFragment();
        // Fragmentの追加や削除といった変更を行う際は、Transactionを利用します
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 新しく追加を行うのでaddを使用します
        // 他にも、よく使う操作で、replace removeといったメソッドがあります
        // メソッドの1つ目の引数は対象のViewGroupのID、2つ目の引数は追加するfragment
        transaction.replace(R.id.mainLayout, fragment);
        // 最後にcommitを使用することで変更を反映します
        transaction.commit();
    }

    public void goBackHome(){
        this.mpStart();
        super.setVolume(1.0f, 1.0f);
        TitleFragment fragment = new TitleFragment();
        // Fragmentの追加や削除といった変更を行う際は、Transactionを利用します
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 新しく追加を行うのでaddを使用します
        // 他にも、よく使う操作で、replace removeといったメソッドがあります
        // メソッドの1つ目の引数は対象のViewGroupのID、2つ目の引数は追加するfragment
        transaction.replace(R.id.mainLayout, fragment);
        // 最後にcommitを使用することで変更を反映します
        transaction.commit();
    }

    public void goToGameRecord(int level, int stage){
        // Fragmentの追加や削除といった変更を行う際は、Transactionを利用します
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 新しく追加を行うのでaddを使用します
        // 他にも、よく使う操作で、replace removeといったメソッドがあります
        // メソッドの1つ目の引数は対象のViewGroupのID、2つ目の引数は追加するfragment
        transaction.replace(R.id.mainLayout, GameRecordFragment.newInstance(level, stage));
        // 最後にcommitを使用することで変更を反映します
        transaction.commit();
    }

    public void goToGameFragment(int level, int stage){
        // Fragmentの追加や削除といった変更を行う際は、Transactionを利用します
        super.mpStart();
        super.setVolume(0.7f, 0.7f);
        mainLayout.removeViewInLayout(bannerView);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 新しく追加を行うのでaddを使用します
        // 他にも、よく使う操作で、replace removeといったメソッドがあります
        // メソッドの1つ目の引数は対象のViewGroupのID、2つ目の引数は追加するfragment
        transaction.replace(R.id.mainLayout, GameFragment.newInstance(level, stage));
        // 最後にcommitを使用することで変更を反映します
        transaction.commit();
    }

    public void goToGameOverFragment(boolean result, int level, int stage, String answer, int time){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 新しく追加を行うのでaddを使用します
        // 他にも、よく使う操作で、replace removeといったメソッドがあります
        // メソッドの1つ目の引数は対象のViewGroupのID、2つ目の引数は追加するfragment
        super.resultSoundCreate(result);
        mainLayout.addView(bannerView);
        this.answer = answer;
        transaction.replace(R.id.mainLayout, GameOverFragment.newInstance(level, stage, result, time));
        // 最後にcommitを使用することで変更を反映します
        transaction.commit();
    }

    public void resultSound(){
        super.resultSoundStart();
    }

    public void resultSoundStop(){
        super.resultSoundStop();
    }

    public void goToAnswerCheckFragment(int level, int stage, boolean result){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 新しく追加を行うのでaddを使用します
        // 他にも、よく使う操作で、replace removeといったメソッドがあります
        // メソッドの1つ目の引数は対象のViewGroupのID、2つ目の引数は追加するfragment
        transaction.replace(R.id.mainLayout, CheckAnswerFragment.newInstance( level, stage, answer, result));
        // 最後にcommitを使用することで変更を反映します
        transaction.commit();
    }

    public void touchSound() {
        if (soundOn)
            mSoundPool.play(mSoundResId, 1.0f, 1.0f, 0, 0, 1.0f);
    }

    public void touchSound2() {
        if (soundOn)
            mSoundPool.play(mSoundResId2, 1.0f, 1.0f, 0, 0, 1.0f);
    }

    public void correctSound(){
        if(soundOn)
            mSoundPool.play(mSoundResIdCorrect, 1.0f, 1.0f, 0, 0, 1.0f);
    }

    public void wrongSound(){
        if(soundOn)
            mSoundPool.play(mSoundResIdWrong, 1.0f, 1.0f, 0, 0, 1.0f);
    }

    public void stageClearSound(){
        if(soundOn)
            mSoundPool.play(mSoundResIdStageClear, 1.0f, 1.0f, 0, 0, 1.0f);
    }

    public void countDownSound(){
        if(soundOn)
            mSoundPool.play(mSoundResIdCountDown, 1.0f, 1.0f, 0, 0, 1.0f);
    }

    public void countDownCountFinished(){
        if(soundOn)
            mSoundPool.play(mSoundResIdCountDownFinished, 1.0f, 1.0f, 0, 0, 1.0f);
    }


    public void stampSound(){
        if(soundOn)
            mSoundPool.play(mSoundResIdStampSound, 1.0f, 1.0f, 0, 0, 1.0f);
    }


    protected void releaseSound() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(getString(R.string.sound_on), soundOn);
        editor.apply();
        mSoundPool.release();
        mSoundPool = null;
    }

    private void showBanner() {
        bannerView.setAdListener(new AdListener() {
            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        bannerView.loadAd(adRequest);
    }

    public int getAdHeight(){
        return AdSize.SMART_BANNER.getHeightInPixels(this);
    }


    private void configureInterstitial(){
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                configureInterstitial();
            }
        });
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });
        requestNewInterstitial();
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    public void showInterstitial() {
        Random rnd = new SecureRandom();

        int ran = rnd.nextInt(3);
        if (mInterstitialAd.isLoaded() && ran == 0) {
            mInterstitialAd.show();
        } else {
            requestNewInterstitial();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATEKEY, true);
        outState.putBoolean(SOUNDKEY, soundOn);
    }




}
