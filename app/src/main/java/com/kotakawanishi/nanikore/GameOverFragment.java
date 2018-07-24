package com.kotakawanishi.nanikore;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.FindCallback;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;
import com.nifty.cloud.mb.core.NCMBQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GameOverFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GameOverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameOverFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String LEVEL_PARAM = "level";
    private static final String STAGE_PARAM = "stage";
    private static final String RESULT_PARAM = "result";
    private static final String TIME_PARAM = "time";
    // TODO: Rename and change types of parameters



    private ImageButton playAgainBtn;
    private ImageButton nextStageBtn;
    private ImageButton checkAnswerBtn;
    private ImageButton titleBtn;
    private ImageView sealView;
    private ImageView resultStringView;
    private ImageView resultStampView;
    private FrameLayout levelStageFrame;



    private int level;
    private int stage;
    private boolean result;
    private Integer time;


    private OnFragmentInteractionListener mListener;

    public GameOverFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameOverFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameOverFragment newInstance(int level, int stage, boolean result, int time) {
        GameOverFragment fragment = new GameOverFragment();
        Bundle args = new Bundle();
        args.putInt(LEVEL_PARAM, level);
        args.putInt(STAGE_PARAM, stage);
        args.putBoolean(RESULT_PARAM, result);
        args.putInt(TIME_PARAM, time);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            level = getArguments().getInt(LEVEL_PARAM);
            stage = getArguments().getInt(STAGE_PARAM);
            result = getArguments().getBoolean(RESULT_PARAM);
            time = getArguments().getInt(TIME_PARAM);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_over, container, false);
        playAgainBtn = (ImageButton)  view.findViewById(R.id.play_again_btn);
        nextStageBtn = (ImageButton) view.findViewById(R.id.next_stage_btn);
        checkAnswerBtn = (ImageButton) view.findViewById(R.id.check_answer_btn);
        titleBtn = (ImageButton) view.findViewById(R.id.title_btn);
        sealView = (ImageView) view.findViewById(R.id.seal_view);
        resultStringView = (ImageView) view.findViewById(R.id.result_string_image_view);
        resultStampView = (ImageView) view.findViewById(R.id.result_stamp_view);
        levelStageFrame = (FrameLayout) view.findViewById(R.id.level_stage_label_frame_game_over);
        this.configureViews();
        this.showViews();
        playAgainBtn.setEnabled(false);
        nextStageBtn.setEnabled(false);
        checkAnswerBtn.setEnabled(false);
        titleBtn.setEnabled(false);
        return view;
    }

    /**
     * Called when all saved state has been restored into the view hierarchy
     * of the fragment.  This can be used to do initialization based on saved
     * state that you are letting the view hierarchy track itself, such as
     * whether check box widgets are currently checked.  This is called
     * after {@link #onActivityCreated(Bundle)} and before
     * {@link #onStart()}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */


    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link Activity#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */


    @Override
    public void onResume() {
        super.onResume();
    }




    private void configureViews(){
        final Scale resultStampScale = new Scale(0.0, 0.23, 0.6119, 0.2929);
        final Scale sealScale = new Scale(0.6119, 0.5468);

        double resultStampWidth = mListener.getReferenceSize().x * resultStampScale.widthScale;
        double resultStampHeight = mListener.getReferenceSize().y * resultStampScale.heightScale;
        FrameLayout.LayoutParams lpResultStamp = new FrameLayout.LayoutParams((int)resultStampWidth, (int)resultStampHeight);
        double resultStampLeftMargin = mListener.getReferenceSize().x / 2.0 - resultStampWidth / 2.0;
        double resultStampTopMargin = mListener.getReferenceSize().y * resultStampScale.yScale;
        lpResultStamp.leftMargin = (int)resultStampLeftMargin;
        lpResultStamp.topMargin = (int)resultStampTopMargin;

        resultStampView.setLayoutParams(lpResultStamp);

        double sealWidth = mListener.getReferenceSize().x * sealScale.widthScale;
        double sealHeight = mListener.getReferenceSize().y * sealScale.heightScale;
        FrameLayout.LayoutParams lpSeal = new FrameLayout.LayoutParams((int)sealWidth, (int)sealHeight);
        sealView.setLayoutParams(lpSeal);
        sealView.setPivotX(0.5f);
        sealView.setPivotY(0.0f);
        this.showLevelStageLabel();
    }

    private void showLevelStageLabel(){
        final Scale levelStageLabelScale = new Scale(0.0, 0.0439, 0.9742, 0.0705);
        final Scale easyStringScale = new Scale(0.4342, 0.0, 0.2198, 0.461);
        final Scale normalStringScale = new Scale(0.44895, 0.0, 0.1771, 0.461);
        final Scale hardStringScale = new Scale(0.4296, 0.0, 0.2334, 0.461);

        double labelWidth = mListener.getReferenceSize().x * levelStageLabelScale.widthScale;
        double labelHeight = mListener.getReferenceSize().y * levelStageLabelScale.heightScale;
        FrameLayout.LayoutParams lpLabel = new FrameLayout.LayoutParams((int)labelWidth, (int)labelHeight);
        double labelLeftMargin = mListener.getReferenceSize().x / 2.0 - labelWidth / 2.0;
        double labelTopMargin = mListener.getReferenceSize().y * levelStageLabelScale.yScale;
        lpLabel.leftMargin = (int)labelLeftMargin;
        lpLabel.topMargin = (int)labelTopMargin;
        levelStageFrame.setLayoutParams(lpLabel);

        switch(GameLevelSelectFragment.Level.getLevel(level)){
            case EASY:
                ImageView easyStringLabel = new ImageView(getContext());
                easyStringLabel.setImageResource(R.drawable.easy_string);
                easyStringLabel.setScaleType(ImageView.ScaleType.FIT_XY);
                easyStringLabel.setPadding(0, 0, 0, 0);
                double easyStringWidth = levelStageFrame.getLayoutParams().width * easyStringScale.widthScale;
                double easyStringHeight = levelStageFrame.getLayoutParams().height * easyStringScale.heightScale;
                double easyStringLeftMargin = levelStageFrame.getLayoutParams().width * easyStringScale.xScale;
                double easyStringTopMargin = levelStageFrame.getLayoutParams().height / 2.0 - easyStringHeight / 2.0;
                FrameLayout.LayoutParams lpEasy = new FrameLayout.LayoutParams((int)easyStringWidth, (int)easyStringHeight);
                lpEasy.leftMargin = (int)easyStringLeftMargin;
                lpEasy.topMargin = (int)easyStringTopMargin;
                easyStringLabel.setLayoutParams(lpEasy);
                levelStageFrame.addView(easyStringLabel);
                break;
            case NORMAL:
                ImageView normalStringLabel = new ImageView(getContext());
                normalStringLabel.setImageResource(R.drawable.normal_string);
                normalStringLabel.setScaleType(ImageView.ScaleType.FIT_XY);
                normalStringLabel.setPadding(0, 0, 0, 0);
                double normalStringWidth = levelStageFrame.getLayoutParams().width * normalStringScale.widthScale;
                double normalStringHeight = levelStageFrame.getLayoutParams().height * normalStringScale.heightScale;
                double normalStringLeftMargin = levelStageFrame.getLayoutParams().width * normalStringScale.xScale;
                double normalStringTopMargin = levelStageFrame.getLayoutParams().height / 2.0 - normalStringHeight / 2.0;
                FrameLayout.LayoutParams lpNormal = new FrameLayout.LayoutParams((int)normalStringWidth, (int)normalStringHeight);
                lpNormal.leftMargin = (int)normalStringLeftMargin;
                lpNormal.topMargin = (int)normalStringTopMargin;
                normalStringLabel.setLayoutParams(lpNormal);
                levelStageFrame.addView(normalStringLabel);
                break;

            case HARD:
                ImageView hardStringLabel = new ImageView(getContext());
                hardStringLabel.setImageResource(R.drawable.hard_string);
                hardStringLabel.setScaleType(ImageView.ScaleType.FIT_XY);
                hardStringLabel.setPadding(0, 0, 0, 0);
                double hardStringWidth = levelStageFrame.getLayoutParams().width * hardStringScale.widthScale;
                double hardStringHeight = levelStageFrame.getLayoutParams().height * hardStringScale.heightScale;
                double hardStringLeftMargin = levelStageFrame.getLayoutParams().width * hardStringScale.xScale;
                double hardStringTopMargin = levelStageFrame.getLayoutParams().height / 2.0 - hardStringHeight / 2.0;
                FrameLayout.LayoutParams lpHard = new FrameLayout.LayoutParams((int)hardStringWidth, (int)hardStringHeight);
                lpHard.leftMargin = (int)hardStringLeftMargin;
                lpHard.topMargin = (int)hardStringTopMargin;
                hardStringLabel.setLayoutParams(lpHard);
                levelStageFrame.addView(hardStringLabel);
                break;
        }
        showNumberOnLevelStageLabel();
    }

    private void showNumberOnLevelStageLabel(){
        final Scale numberSizeScale = new Scale(0.0346, 0.451);
        final Scale number1SizeScale = new Scale(0.0185, 0.451);

        final double[] numberXScale = {0.9343, 0.89015};
        final double additionOfNumber1XScale = 0.00305;
        int tempNumber = stage + 1;
        for(int i = 0; i < 2; i++){
            ImageView stageNumberView = new ImageView(getContext());
            stageNumberView.setScaleType(ImageView.ScaleType.FIT_XY);
            stageNumberView.setPadding(0, 0, 0, 0);
            FrameLayout.LayoutParams lpNumber;

            if(i == 1 && stage + 1 < 10){
                stageNumberView.setImageResource(R.drawable.stage_number0_on_level_stage);
            }else{
                String string = String.format(Locale.ENGLISH, "stage_number%d_on_level_stage", tempNumber % 10);
                int strId = getResources().getIdentifier(string, "drawable", getContext().getPackageName());
                stageNumberView.setImageResource(strId);
            }
            if(tempNumber % 10 == 1){
                double numberWidth = levelStageFrame.getLayoutParams().width * number1SizeScale.widthScale;
                double numberHeight = levelStageFrame.getLayoutParams().height * number1SizeScale.heightScale;
                lpNumber = new FrameLayout.LayoutParams((int)numberWidth, (int)numberHeight);
                double numberLeftMargin = levelStageFrame.getLayoutParams().width * (numberXScale[i] + additionOfNumber1XScale);
                lpNumber.leftMargin = (int)numberLeftMargin;
                double numberTopMargin = levelStageFrame.getLayoutParams().height / 2.0 - numberHeight / 2.0;
                lpNumber.topMargin = (int)numberTopMargin;
            }else{
                double numberWidth = levelStageFrame.getLayoutParams().width * numberSizeScale.widthScale;
                double numberHeight = levelStageFrame.getLayoutParams().height * numberSizeScale.heightScale;
                lpNumber = new FrameLayout.LayoutParams((int)numberWidth, (int)numberHeight);
                double numberLeftMargin = levelStageFrame.getLayoutParams().width * numberXScale[i];
                lpNumber.leftMargin = (int)numberLeftMargin;
                double numberTopMargin = levelStageFrame.getLayoutParams().height / 2.0 - numberHeight / 2.0;
                lpNumber.topMargin = (int)numberTopMargin;
            }
            stageNumberView.setLayoutParams(lpNumber);
            levelStageFrame.addView(stageNumberView);
            tempNumber /= 10;
        }
    }

    private void showViews(){
        final Scale failureStringScale = new Scale(0.0, 0.1508, 0.3019, 0.05015);
        final Scale passStringScale = new Scale(0.0, 0.1508, 0.2012, 0.05015);

        FrameLayout.LayoutParams lpResultString;

        if(result){
            resultStringView.setImageResource(R.drawable.pass_label);
            resultStampView.setImageResource(R.drawable.well_done_stamp_game_over);
            double resultStringWidth = mListener.getReferenceSize().x * passStringScale.widthScale;
            double resultStringHeight = mListener.getReferenceSize().y * passStringScale.heightScale;
            lpResultString = new FrameLayout.LayoutParams((int)resultStringWidth, (int)resultStringHeight);
            double resultStringLeftMargin = mListener.getReferenceSize().x / 2.0 - resultStringWidth / 2.0;
            double resultStringTopMargin= mListener.getReferenceSize().y * passStringScale.yScale;
            lpResultString.leftMargin = (int)resultStringLeftMargin;
            lpResultString.topMargin = (int)resultStringTopMargin;
            resultStringView.setLayoutParams(lpResultString);
        }else{
            resultStringView.setImageResource(R.drawable.failure_label);
            resultStampView.setImageResource(R.drawable.more_practice_stamp);
            double resultStringWidth = mListener.getReferenceSize().x * failureStringScale.widthScale;
            double resultStringHeight = mListener.getReferenceSize().y * failureStringScale.heightScale;
            lpResultString = new FrameLayout.LayoutParams((int)resultStringWidth, (int)resultStringHeight);
            double resultStringLeftMargin = mListener.getReferenceSize().x / 2.0 - resultStringWidth / 2.0;
            double resultStringTopMargin= mListener.getReferenceSize().y * failureStringScale.yScale;
            lpResultString.leftMargin = (int)resultStringLeftMargin;
            lpResultString.topMargin = (int)resultStringTopMargin;
            resultStringView.setLayoutParams(lpResultString);
        }

        double sealLeftMargin = mListener.getReferenceSize().x / 2.0 - sealView.getLayoutParams().width / 2.0;
        double sealTopMargin = 0 - sealView.getLayoutParams().height;
        FrameLayout.LayoutParams lpSeal = (FrameLayout.LayoutParams)sealView.getLayoutParams();
        lpSeal.leftMargin = (int)sealLeftMargin;
        lpSeal.topMargin = (int)sealTopMargin;
        sealView.setLayoutParams(lpSeal);
        sealView.setVisibility(View.VISIBLE);

        mListener.resultSound();
        this.startResultAnimation();
    }


    private void startResultAnimation(){
        FrameLayout.LayoutParams lpResult = (FrameLayout.LayoutParams) resultStampView.getLayoutParams();

        PropertyValuesHolder holderY = PropertyValuesHolder.ofFloat( "translationY", 0, sealView.getLayoutParams().height + lpResult.topMargin + lpResult.height - sealView.getLayoutParams().height);
        // rotationプロパティを0fから360fに変化させます
        PropertyValuesHolder holderXScale = PropertyValuesHolder.ofFloat( "scaleX", 1.1f, 1.0f);
        PropertyValuesHolder holderYScale = PropertyValuesHolder.ofFloat( "scaleY", 1.05f, 1.0f);


        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
                sealView, holderXScale, holderY, holderYScale);

        objectAnimator.setDuration(1400);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param animation
             */
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                try{
                    Thread.sleep(300);
                }catch(InterruptedException e){}

                mListener.stampSound();
                resultStampView.setVisibility(View.VISIBLE);
                resultStringView.setVisibility(View.VISIBLE);
                configureBtns();
                sealView.setVisibility(View.GONE);
                mListener.showInterstitial();
                if(result)
                    updateRecord();
                else
                    enableBtns();

            }
        });
        objectAnimator.start();
    }

    private void enableBtns(){
        playAgainBtn.setEnabled(true);
        nextStageBtn.setEnabled(true);
        checkAnswerBtn.setEnabled(true);
        titleBtn.setEnabled(true);
    }

    private void configureBtns(){

        final Scale nextBtnScale = new Scale(0.0, 0.5913, 0.3621, 0.0908);
        final double intervalScaleOfBtns = 0.0302;

        double btnWidth = mListener.getReferenceSize().x * nextBtnScale.widthScale;
        double btnHeight = mListener.getReferenceSize().y * nextBtnScale.heightScale;
        if(result && (stage < 23 || level != 2)){
            FrameLayout.LayoutParams lpNextBtn = new FrameLayout.LayoutParams((int)btnWidth, (int)btnHeight);
            double nextLeftMargin = mListener.getReferenceSize().x / 2.0 - mListener.getReferenceSize().x * intervalScaleOfBtns - btnWidth;
            double nextTopMargin = mListener.getReferenceSize().y * nextBtnScale.yScale;
            lpNextBtn.leftMargin = (int)nextLeftMargin;
            lpNextBtn.topMargin = (int)nextTopMargin;
            nextStageBtn.setLayoutParams(lpNextBtn);
            nextStageBtn.setVisibility(View.VISIBLE);
            nextStageBtn.setEnabled(true);
            FrameLayout.LayoutParams lpAgainBtn = new FrameLayout.LayoutParams((int)btnWidth, (int)btnHeight);
            double againLeftMargin = mListener.getReferenceSize().x / 2.0 + mListener.getReferenceSize().x * intervalScaleOfBtns;
            double againTopMargin = mListener.getReferenceSize().y * nextBtnScale.yScale;
            lpAgainBtn.leftMargin = (int)againLeftMargin;
            lpAgainBtn.topMargin = (int)againTopMargin;
            playAgainBtn.setLayoutParams(lpAgainBtn);
            playAgainBtn.setVisibility(View.VISIBLE);
            playAgainBtn.setEnabled(true);
        }else{
            FrameLayout.LayoutParams lpAgainBtn = new FrameLayout.LayoutParams((int)btnWidth, (int)btnHeight);
            double againLeftMargin = mListener.getReferenceSize().x / 2.0 - btnWidth / 2.0;
            double againTopMargin = mListener.getReferenceSize().y * nextBtnScale.yScale;
            lpAgainBtn.leftMargin = (int)againLeftMargin;
            lpAgainBtn.topMargin = (int)againTopMargin;
            playAgainBtn.setLayoutParams(lpAgainBtn);
            playAgainBtn.setVisibility(View.VISIBLE);
            playAgainBtn.setEnabled(true);
        }

        checkAnswerBtn.setVisibility(View.VISIBLE);
        checkAnswerBtn.setEnabled(true);
        FrameLayout.LayoutParams lpCheck = new FrameLayout.LayoutParams((int)btnWidth, (int)btnHeight);
        double checkLeftMargin = mListener.getReferenceSize().x / 2.0 - mListener.getReferenceSize().x * intervalScaleOfBtns - btnWidth;
        double checkTopMargin = mListener.getReferenceSize().y * nextBtnScale.yScale + btnHeight + mListener.getReferenceSize().x * intervalScaleOfBtns;
        lpCheck.leftMargin = (int) checkLeftMargin;
        lpCheck.topMargin = (int)checkTopMargin;
        checkAnswerBtn.setLayoutParams(lpCheck);

        titleBtn.setVisibility(View.VISIBLE);
        titleBtn.setEnabled(true);
        FrameLayout.LayoutParams lpTitle = new FrameLayout.LayoutParams((int)btnWidth, (int)btnHeight);
        double titleLeftMargin = mListener.getReferenceSize().x / 2.0 + mListener.getReferenceSize().x * intervalScaleOfBtns;
        double titleTopMargin = (int)checkTopMargin;
        lpTitle.leftMargin = (int)titleLeftMargin;
        lpTitle.topMargin = (int)titleTopMargin;
        titleBtn.setLayoutParams(lpTitle);
        if(result)
            nextStageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nextStage();
                }
            });
        playAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAgain();
            }
        });
        titleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackTitle();
            }
        });
        checkAnswerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });
    }


    public void nextStage(){
        mListener.touchSound();
        mListener.resultSoundStop();
        try{
            Thread.sleep(500);
        }catch (InterruptedException e){
        }
        Stage stage = new Stage(GameLevelSelectFragment.Level.getLevel(level), this.stage);
        Stage nextStage;
        if(stage.getNumberOfStage() > 22){
            nextStage = Stage.nextLevel(stage);
            if(nextStage != null)
                mListener.goToGameFragment(nextStage.getLevel().getLevelNumber(), nextStage.getNumberOfStage());
        }else{
            nextStage = Stage.nextStage(stage);
            mListener.goToGameFragment(nextStage.getLevel().getLevelNumber(), nextStage.getNumberOfStage());
        }
    }

    public void playAgain(){
        mListener.touchSound();
        mListener.resultSoundStop();
        try{
            Thread.sleep(500);
        }catch (InterruptedException e){
        }
        mListener.goToGameFragment(level, stage);
    }

    public void checkAnswer(){
        mListener.touchSound();
        mListener.goToAnswerCheckFragment( level, stage, result);

    }

    public void goBackTitle(){
        mListener.touchSound();
        mListener.resultSoundStop();
        mListener.goBackHome();
    }


    private void updateRecord(){
        updateSelfRecord();
        updateGeneralRecord();
        updateClearNumberRecord();

    }

    private void updateSelfRecord(){
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());

        String value = String.format( Locale.ENGLISH,"%s%d",GameLevelSelectFragment.Level.getLevel(level).getLabel(), stage + 1);

        String recordString = pref.getString(value, null);
        List<Integer> rankRecordList = new ArrayList<>();
        Gson gson = new Gson();
        int[] rankRecord;
        if(recordString != null) {
            rankRecord = gson.fromJson(recordString, int[].class);
            if (rankRecord != null && rankRecord.length > 0)
                rankRecordList = toList(rankRecord);
        }

        rankRecordList.add(time);
        Collections.sort(rankRecordList);
        if(rankRecordList.size() > 5){
            rankRecordList.remove(5);
        }
        rankRecord = toArr(rankRecordList);
        String newRecordString = gson.toJson(rankRecord);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(String.format(Locale.ENGLISH, "%s%d", GameLevelSelectFragment.Level.getLevel(level).getLabel(), stage + 1), newRecordString);
        editor.apply();
    }

    private void updateGeneralRecord(){
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String value = String.format( Locale.ENGLISH,"%s%d",GameLevelSelectFragment.Level.getLevel(level).getLabel(), stage + 1);

        NCMBQuery<NCMBObject> query = new NCMBQuery<>(getString(R.string.stage_record_class_key));
        query.whereEqualTo(getString(R.string.stage_key), value);

        query.findInBackground(new FindCallback<NCMBObject>() {
            @Override
            public void done(List<NCMBObject> results, NCMBException e) {
                if (e != null) {
                    int[] record;
                    Gson gson = new Gson();
                    String unprocessedRecord = pref.getString("unprocessedRecord", null);
                    if(unprocessedRecord != null) {
                        int[] rankRecord;

                        rankRecord = gson.fromJson(unprocessedRecord, int[].class);
                        int count = pref.getInt("unprocessed",0);
                        record = new int[count + 1];
                        for(int i = 0; i < count + 1; i++){
                            if(i == count)
                                record[i] = time;
                            else
                                record[i] = rankRecord[i];
                        }
                        String recordString = gson.toJson(record);
                        pref.edit().putString("unprocessedRecord", recordString).apply();
                        pref.edit().putInt("unprocessed", count + 1).apply();
                    }else{
                        record = new int[time];
                        String recordString = gson.toJson(record);
                        pref.edit().putString("unprocessedRecord", recordString).apply();
                        pref.edit().putInt("unprocessed", 1).apply();
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("データを保存できませんでした。").setPositiveButton("閉じる", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();

                } else {
                    if (!results.isEmpty()) {
                        NCMBObject object = results.get(0);
                        List list = object.getList(getString(R.string.rank_record_key));
                        List<Integer>recordList = new ArrayList<>();
                        if(list.get(0) instanceof  Double){
                            for (int i = 0; i < list.size();i++){
                                Double number = (Double)list.get(i);
                                recordList.add(number.intValue());
                            }

                        }else if(list.get(0) instanceof  Integer) {
                            for (int i = 0; i < list.size(); i++) {
                                Integer number = (Integer) list.get(i);
                                recordList.add(number);
                            }
                        }
                        if(recordList.size() > 0 && recordList.get(0) == 0)
                            recordList.clear();

                        recordList.add(time);

                        if(pref.getInt("unprocessed", 0) != 0){
                            Gson gson = new Gson();
                            String unprocessedRecord = pref.getString("unprocessedRecord", null);
                            int[] rankRecord = gson.fromJson(unprocessedRecord, int[].class);
                            for(int record : rankRecord){
                                recordList.add(record);
                            }
                            pref.edit().putString("unprocessedRecord", null).apply();
                            pref.edit().putInt("unprocessed", 0).apply();
                        }
                        Collections.sort(recordList);
                        if (recordList.size() > 5)
                            recordList.remove(5);
                        object.put(getString(R.string.rank_record_key), recordList);
                        object.saveInBackground(new DoneCallback() {
                            @Override
                            public void done(NCMBException e) {
                                if (e != null) {
                                    int[] record;
                                    Gson gson = new Gson();
                                    String unprocessedRecord = pref.getString("unprocessedRecord", null);
                                    if(unprocessedRecord != null) {
                                        int[] rankRecord;

                                        rankRecord = gson.fromJson(unprocessedRecord, int[].class);
                                        int count = pref.getInt("unprocessed",0);
                                        record = new int[count + 1];
                                        for(int i = 0; i < count + 1; i++){
                                            if(i == count)
                                                record[i] = time;
                                            else
                                                record[i] = rankRecord[i];
                                        }
                                        String recordString = gson.toJson(record);
                                        pref.edit().putString("unprocessedRecord", recordString).apply();
                                        pref.edit().putInt("unprocessed", count + 1).apply();
                                    }else{
                                        record = new int[time];
                                        String recordString = gson.toJson(record);
                                        pref.edit().putString("unprocessedRecord", recordString).apply();
                                        pref.edit().putInt("unprocessed", 1).apply();
                                    }
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setMessage("データを保存できませんでした。").setPositiveButton("閉じる", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    builder.show();
                                    enableBtns();

                                } else {
                                    enableBtns();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void updateClearNumberRecord(){
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = pref.edit();
        Gson gson = new Gson();
        String resultRecordString = pref.getString(GameLevelSelectFragment.Level.getLevel(level).getLabel(), null);
        boolean[] selectedLevelRecord = gson.fromJson(resultRecordString, boolean[].class);

        if(!selectedLevelRecord[stage]){
            NCMBQuery<NCMBObject> passQuery = new NCMBQuery<>(getString(R.string.passing_people_class_key));
            String levelValue = String.format(Locale.ENGLISH, "%s", GameLevelSelectFragment.Level.getLevel(level).getLabel());
            passQuery.whereEqualTo(getString(R.string.level_key), levelValue);
            passQuery.findInBackground(new FindCallback<NCMBObject>() {
                @Override
                public void done(List<NCMBObject> results, NCMBException e) {
                    if (e != null) {
                        return;
                        //検索失敗時の処理
                    } else{
                        NCMBObject object = results.get(0);
                        List numbers = object.getList(getString(R.string.number_key));
                        if(numbers.get(0) instanceof Double){
                            Double record = (Double) numbers.get(stage) + 1.0;
                            numbers.set(stage, record);
                        }else if(numbers.get(0) instanceof  Integer){
                            Integer record = (Integer) numbers.get(stage) + 1;
                            numbers.set(stage, record);
                        }
                        object.put(getString(R.string.number_key), numbers);
                        object.saveInBackground(new DoneCallback() {
                            @Override
                            public void done(NCMBException e) {
                                if (e != null) {

                                    //エラー発生時の処理
                                } else {

                                    //成功時の処理
                                }
                            }});
                    }
                }
            });

            selectedLevelRecord[stage] = true;
            String record = gson.toJson(selectedLevelRecord);
            editor.putString(GameLevelSelectFragment.Level.getLevel(level).getLabel(), record);
            editor.apply();

        }

    }

    public static ArrayList<Integer> toList(int[] arr){
        // int[] -> ArrayList<Integer>
        ArrayList<Integer> list = new ArrayList<>();
        for (int t : arr) list.add(t);
        return list;
    }


    public static int[] toArr(List<Integer> list){
        int l = list.size();
        int[] arr = new int[l];
        Iterator<Integer> iterator = list.iterator();
        for (int i = 0;i < l; i++)
            arr[i] = iterator.next();
        return arr;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Called when the Fragment is no longer started.  This is generally
     * tied to {@link Activity#onStop() Activity.onStop} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * Called to ask the fragment to save its current dynamic state, so it
     * can later be reconstructed in a new instance of its process is
     * restarted.  If a new instance of the fragment later needs to be
     * created, the data you place in the Bundle here will be available
     * in the Bundle given to {@link #onCreate(Bundle)},
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}, and
     * {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>This corresponds to {@link Activity#onSaveInstanceState(Bundle)
     * Activity.onSaveInstanceState(Bundle)} and most of the discussion there
     * applies here as well.  Note however: <em>this method may be called
     * at any time before {@link #onDestroy()}</em>.  There are many situations
     * where a fragment may be mostly torn down (such as when placed on the
     * back stack with no UI showing), but its state will not be saved until
     * its owning activity actually needs to save its state.
     *
     * @param outState Bundle in which to place your saved state.
     */

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        Point getReferenceSize();
        void touchSound();
        void stampSound();
        void goToAnswerCheckFragment(int level, int stage, boolean result);
        void showInterstitial();
        void goBackHome();
        void goToGameFragment(int level, int stage);
        void resultSound();
        void resultSoundStop();
    }
}
