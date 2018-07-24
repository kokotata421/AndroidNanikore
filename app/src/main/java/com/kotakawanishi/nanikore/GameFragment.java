package com.kotakawanishi.nanikore;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GameFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameFragment extends Fragment implements Question.QuestionCallbacks, CharacterView.CharacterViewCallBack{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String LEVEL = "LEVEL";
    private static final String STAGE = "STAGE";

    private FrameLayout timeFrame;
    private GameScrollView scrollView;
    private FrameLayout levelStageFrame;

    private ImageView backgroundView;
    private FrameLayout contentLayout;
    private FrameLayout layout;

    private Stage stage;
    private int time;
    private int countDownTime;
    private int numberOfQuestions;
    private Timer tm;
    private boolean gameStarted = false;
    private ImageView countDownNumberView;
    private List<Question> questions = new ArrayList<>();
    private List<ImageView> minViews = new ArrayList<>();
    private List<ImageView> secViews = new ArrayList<>();
    private List<ImageView> questionLabelViews = new ArrayList<>();
    final Scale timeNumberScaleToFrame = new Scale(0.0, 0.435, 0.0458, 0.439);
    final Scale timeNumber1ScaleToFrame = new Scale(0.0, 0.435, 0.0305, 0.439);
    final double additionOfTimeNumber1XScale = 0.0053;
    final double[] minuteNumberXScale = {0.4169, 0.3599};
    final double[] secondNumberXScale = {0.5892, 0.5322};
    final Scale questionScale = new Scale(0.035, 0.2, 0.0562,  0.0326);
    final double intervalScaleOfQuestionAndSquare = 0.0241;
    final double intervalScaleOfQuestions = 0.0402;
    final double intervalLengthOfCharacterScale = 0.01;
    final Scale squareScale = new Scale(0.035, 0.0, 0.19, 0.0);
    final Scale squareScaleIn5 = new Scale(0.035, 0.0, 0.17, 0.0);
    final Scale squareScaleIn6 = new Scale(0.035, 0.0, 0.14, 0.0);
    final Scale answerBtnScale = new Scale(0.171, 0.0637);
    private static final int CHARACTER_3 = 3;
    private static final int CHARACTER_4 = 4;
    private static final int CHARACTER_5 = 5;
    private static final int CHARACTER_6 = 6;
    boolean scrollEnabled = false;
    ArrayList<Integer> questionHeights = new ArrayList<>();



    private OnFragmentInteractionListener mListener;

    public GameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameFragment newInstance(int level, int stage) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putInt(LEVEL, level);
        args.putInt(STAGE, stage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            stage = new Stage(GameLevelSelectFragment.Level.getLevel(getArguments().getInt(LEVEL)), getArguments().getInt(STAGE));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        layout = (FrameLayout) view.findViewById(R.id.layout_game);
        timeFrame = (FrameLayout) view.findViewById(R.id.time_frame);
        backgroundView = new ImageView(getContext());
        backgroundView.setPadding(0,0,0,0);
        backgroundView.setScaleType(ImageView.ScaleType.FIT_XY);
        backgroundView.setImageResource(R.drawable.main_background);
        scrollView = new GameScrollView(getContext());
        scrollView.setPadding(0,0,0,0);
        contentLayout = new FrameLayout(getContext());
        contentLayout.setPadding(0,0,0,0);
        levelStageFrame = (FrameLayout) view.findViewById(R.id.level_stage_label_frame_game);

        this.configureData();

        mListener.turnDownVolume();
        return view;
    }

    private void configureView(){
        final Scale timeFrameSizeScale = new Scale(1.0, 0.0952);

        double timeFrameHeight = mListener.getReferenceSize().y * timeFrameSizeScale.heightScale;
        FrameLayout.LayoutParams lpTimeFrame = new FrameLayout.LayoutParams(mListener.getReferenceSize().x, (int)timeFrameHeight);
        double timeFrameTopMargin = mListener.getReferenceSize().y - timeFrameHeight;
        lpTimeFrame.topMargin = (int)timeFrameTopMargin;
        timeFrame.setLayoutParams(lpTimeFrame);

        scrollView.setPadding(0,0,0,0);
        scrollView.setFillViewport(true);
        scrollView.setLayoutParams(new FrameLayout.LayoutParams(mListener.getReferenceSize().x, mListener.getReferenceSize ().y - (int)timeFrameHeight));
        this.configureTimeFrame();
    }


    private void configureData(){
        time = stage.getTime();
        numberOfQuestions = stage.getNumberOfQuestion();
        this.fetchQuestionData();
    }

    /**
     * Called when the Fragment is visible to the user.  This is generally
     * tied to {@link Activity#onStart() Activity.onStart} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStart() {
        super.onStart();
        mListener.turnDownVolume();
    }



    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link Activity#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();
        if(!gameStarted) {
            this.configureView();
            this.gameStartCount();
        }else{
            if(tm != null) {
                tm.cancel();
                tm = null;
            }
            gameOver(false);
        }
    }



    private void gameStartCount() {
        final Scale countDownScale = new Scale(0.0, 0.2402, 0.5233, 0.0488);
        final Scale gameStartCountSecondLabelScale = new Scale(0.4906, 0.3157, 0.08132, 0.04833);


        this.showLevelStageLabel();
        this.configureGameViews();
        countDownTime = 5;

        showCountDownView(countDownTime);

        final double gameStartLabelWidth = mListener.getReferenceSize().x * countDownScale.widthScale;
        final double gameStartLabelHeight = mListener.getReferenceSize().y * countDownScale.heightScale;
        FrameLayout.LayoutParams lpGameStartLabel = new FrameLayout.LayoutParams((int) gameStartLabelWidth, (int) gameStartLabelHeight);
        final ImageView gameStartLabel = new ImageView(getContext());
        gameStartLabel.setPadding(0, 0, 0, 0);
        gameStartLabel.setScaleType(ImageView.ScaleType.FIT_XY);
        final double gameStartLeftMargin = mListener.getReferenceSize().x / 2.0 - gameStartLabelWidth / 2.0;
        final double gameStartTopMargin = mListener.getReferenceSize().y * countDownScale.yScale;
        lpGameStartLabel.leftMargin = (int) gameStartLeftMargin;
        lpGameStartLabel.topMargin = (int) gameStartTopMargin;
        gameStartLabel.setLayoutParams(lpGameStartLabel);
        gameStartLabel.setImageResource(R.drawable.until_the_start_of_the_exam_label);
        layout.addView(gameStartLabel);

        final ImageView countDownSecondLabel = new ImageView(getContext());
        countDownSecondLabel.setScaleType(ImageView.ScaleType.FIT_XY);
        countDownSecondLabel.setPadding(0, 0, 0, 0);
        countDownSecondLabel.setImageResource(R.drawable.start_count_down_second_label);
        final double secondLabelWidth = mListener.getReferenceSize().x * gameStartCountSecondLabelScale.widthScale;
        final double secondLabelHeight = mListener.getReferenceSize().y * gameStartCountSecondLabelScale.heightScale;
        FrameLayout.LayoutParams lpSecondLabel = new FrameLayout.LayoutParams((int) secondLabelWidth, (int) secondLabelHeight);
        final double secondLabelLeftMargin = mListener.getReferenceSize().x * gameStartCountSecondLabelScale.xScale;
        final double secondLabelTopMargin = mListener.getReferenceSize().y * gameStartCountSecondLabelScale.yScale;
        lpSecondLabel.leftMargin = (int) secondLabelLeftMargin;
        lpSecondLabel.topMargin = (int) secondLabelTopMargin;
        countDownSecondLabel.setLayoutParams(lpSecondLabel);
        layout.addView(countDownSecondLabel);

        AlphaAnimation alphaAnimation = new AlphaAnimation((float)1.0, (float)0.0);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setRepeatCount(4);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mListener.countDownSound();

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mListener.countDownCountFinished();
                layout.post(new Runnable() {
                    @Override
                    public void run() {
                        layout.removeViewInLayout(gameStartLabel);
                        layout.removeViewInLayout(countDownNumberView);
                        layout.removeViewInLayout(countDownSecondLabel);
                        layout.removeViewInLayout(levelStageFrame);
                        layout.addView(scrollView);
                    }});

                contentLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        contentLayout.addView(levelStageFrame);
                    }});
               startTimer();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                mListener.countDownSound();
                countDownTime -= 1;
                showCountDownView(countDownTime);
            }
        });
        countDownNumberView.startAnimation(alphaAnimation);
    }

    private void showCountDownView(int countDownTime){
        final Scale gameStartCountNumberScale = new Scale(0.41079, 0.318, 0.0523, 0.04296);
        final Scale gameStartCountNumber1Scale = new Scale(0.41069, 0.318, 0.0289, 0.04296);

        if(countDownTime == 5){
            countDownNumberView = new ImageView(getContext());
            countDownNumberView.setScaleType(ImageView.ScaleType.FIT_XY);
            countDownNumberView.setPadding(0, 0, 0, 0);
            countDownNumberView.setImageResource(R.drawable.start_count_down_number5);
            double countDownWidth = mListener.getReferenceSize().x * gameStartCountNumberScale.widthScale;
            double countDownHeight = mListener.getReferenceSize().y * gameStartCountNumberScale.heightScale;
            FrameLayout.LayoutParams lpCountDown = new FrameLayout.LayoutParams((int) countDownWidth, (int) countDownHeight);
            double countDownLeftMargin = mListener.getReferenceSize().x * gameStartCountNumberScale.xScale;
            double countDownTopMargin = mListener.getReferenceSize().y * gameStartCountNumberScale.yScale;
            lpCountDown.leftMargin = (int) countDownLeftMargin;
            lpCountDown.topMargin = (int) countDownTopMargin;
            countDownNumberView.setLayoutParams(lpCountDown);
            layout.addView(countDownNumberView);
        }else if (countDownTime == 1){
            final double countDownWidth = mListener.getReferenceSize().x * gameStartCountNumber1Scale.widthScale;
            final double countDownHeight = mListener.getReferenceSize().y * gameStartCountNumber1Scale.heightScale;
            FrameLayout.LayoutParams lpCountDown = new FrameLayout.LayoutParams((int) countDownWidth, (int) countDownHeight);
            final double countDownLeftMargin = mListener.getReferenceSize().x * gameStartCountNumber1Scale.xScale;
            final double countDownTopMargin = mListener.getReferenceSize().y * gameStartCountNumber1Scale.yScale;
            lpCountDown.leftMargin = (int) countDownLeftMargin;
            lpCountDown.topMargin = (int) countDownTopMargin;
            countDownNumberView.setLayoutParams(lpCountDown);
            countDownNumberView.setImageResource(R.drawable.start_count_down_number1);
        }else{
            String string = String.format(Locale.ENGLISH, "start_count_down_number%d", countDownTime);
            int strId = getResources().getIdentifier(string, "drawable", getContext().getPackageName());
            countDownNumberView.setImageResource(strId);
        }
    }

    private void configureTimeFrame(){
        final Scale colonScaleToFrame = new Scale(0.0, 0.493, 0.0161, 0.332);
        double colonWidth = timeFrame.getLayoutParams().width * colonScaleToFrame.widthScale;
        double colonHeight = timeFrame.getLayoutParams().height * colonScaleToFrame.heightScale;

        FrameLayout.LayoutParams lpColon = new FrameLayout.LayoutParams((int)colonWidth, (int)colonHeight);
        double colonLeftMargin = mListener.getReferenceSize().x / 2.0 - colonWidth / 2.0;
        double colonTopMargin = timeFrame.getLayoutParams().height * colonScaleToFrame.yScale;
        lpColon.leftMargin = (int)colonLeftMargin;
        lpColon.topMargin = (int)colonTopMargin;
        ImageView colonView = new ImageView(getContext());
        colonView.setImageResource(R.drawable.time_left_colon);
        colonView.setPadding(0, 0, 0, 0);
        colonView.setScaleType(ImageView.ScaleType.FIT_XY);
        colonView.setLayoutParams(lpColon);
        timeFrame.addView(colonView);
        this.showTimeCount(true);
    }



    private void showTimeCount(boolean beginning){
        int m = time / 60;
        int s = time % 60;
        if(!secViews.isEmpty()){
            for(int i = 0; i < 2; i++){
                timeFrame.removeView(secViews.get(i));
            }
            secViews.clear();
        }

        if(s == 59 || beginning){
            if(!minViews.isEmpty()){
                for(int i = 0; i < 2; i++){
                    timeFrame.removeView(minViews.get(i));
                }
                minViews.clear();
            }

            for(int i = 0; i < 2; i++){
                int tm = m % 10;
                ImageView mNumberView = new ImageView(getContext());
                mNumberView.setScaleType(ImageView.ScaleType.FIT_XY);
                mNumberView.setPadding(0, 0, 0, 0);
                String string = String.format(Locale.ENGLISH, "time_left_number%d", tm);
                int strId = getResources().getIdentifier(string, "drawable", getContext().getPackageName());
                mNumberView.setImageResource(strId);
                FrameLayout.LayoutParams lpNumber;
                if(tm == 1){
                    double numberWidth = this.timeFrame.getLayoutParams().width * timeNumber1ScaleToFrame.widthScale;
                    double numberHeight = this.timeFrame.getLayoutParams().height * timeNumber1ScaleToFrame.heightScale;
                    lpNumber = new FrameLayout.LayoutParams((int)numberWidth, (int)numberHeight);
                    double numberLeftMargin = this.timeFrame.getLayoutParams().width * (minuteNumberXScale[i] + additionOfTimeNumber1XScale);
                    lpNumber.leftMargin = (int)numberLeftMargin;
                }else {
                    double numberWidth = this.timeFrame.getLayoutParams().width * timeNumberScaleToFrame.widthScale;
                    double numberHeight = this.timeFrame.getLayoutParams().height * timeNumberScaleToFrame.heightScale;
                    lpNumber = new FrameLayout.LayoutParams((int)numberWidth, (int)numberHeight);
                    double numberLeftMargin = this.timeFrame.getLayoutParams().width * minuteNumberXScale[i];
                    lpNumber.leftMargin = (int)numberLeftMargin;
                }
                double numberTopMargin = this.timeFrame.getLayoutParams().height * timeNumber1ScaleToFrame.yScale;
                lpNumber.topMargin = (int)numberTopMargin;
                mNumberView.setLayoutParams(lpNumber);
                minViews.add(mNumberView);
                timeFrame.addView(mNumberView);
                m /= 10;
            }
        }


        for(int i = 0; i < 2; i++){
            int ts = s % 10;
            ImageView sNumberView = new ImageView(getContext());
            sNumberView.setScaleType(ImageView.ScaleType.FIT_XY);
            sNumberView.setPadding(0, 0, 0, 0);
            String string = String.format(Locale.ENGLISH, "time_left_number%d", ts);
            int strId = getResources().getIdentifier(string, "drawable", getContext().getPackageName());
            sNumberView.setImageResource(strId);
            FrameLayout.LayoutParams lpNumber;
            if(ts == 1){
                double numberWidth = this.timeFrame.getLayoutParams().width * timeNumber1ScaleToFrame.widthScale;
                double numberHeight = this.timeFrame.getLayoutParams().height * timeNumber1ScaleToFrame.heightScale;
                lpNumber = new FrameLayout.LayoutParams((int)numberWidth, (int)numberHeight);
                double numberLeftMargin = this.timeFrame.getLayoutParams().width * (secondNumberXScale[i] + additionOfTimeNumber1XScale);
                lpNumber.leftMargin = (int)numberLeftMargin;
            }else{
                double numberWidth = this.timeFrame.getLayoutParams().width * timeNumberScaleToFrame.widthScale;
                double numberHeight = this.timeFrame.getLayoutParams().height * timeNumberScaleToFrame.heightScale;
                lpNumber = new FrameLayout.LayoutParams((int)numberWidth, (int)numberHeight);
                double numberLeftMargin = this.timeFrame.getLayoutParams().width * secondNumberXScale[i];
                lpNumber.leftMargin = (int)numberLeftMargin;
            }
            double numberTopMargin = this.timeFrame.getLayoutParams().height * timeNumber1ScaleToFrame.yScale;
            lpNumber.topMargin = (int)numberTopMargin;
            sNumberView.setLayoutParams(lpNumber);
            secViews.add(sNumberView);
            timeFrame.addView(sNumberView);

            s /= 10;
        }
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

        switch(stage.getLevel()){
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
        int tempNumber = stage.getNumberOfStage() + 1;
        for(int i = 0; i < 2; i++){
            ImageView stageNumberView = new ImageView(getContext());
            stageNumberView.setScaleType(ImageView.ScaleType.FIT_XY);
            stageNumberView.setPadding(0, 0, 0, 0);
            FrameLayout.LayoutParams lpNumber;

            if(i == 1 && stage.getNumberOfStage() + 1 < 10){
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



    private void configureGameViews(){
        final Scale noteScale = new Scale(0.035, 0.1329, 0.4855, 0.01953);
        final Scale noteScaleOfSixCharacters = new Scale(0.035, 0.165, 0.7536, 0.01953);

        double squareHeight;


        switch (this.questions.get(0).information.difficultyOfQuestion){
            case CHARACTER_3: squareHeight = mListener.getReferenceSize().x * squareScale.widthScale;
                break;
            case CHARACTER_4: squareHeight = mListener.getReferenceSize().x * squareScale.widthScale;
                break;
            case CHARACTER_5: squareHeight = mListener.getReferenceSize().x * squareScaleIn5.widthScale;
                break;
            case CHARACTER_6: squareHeight = mListener.getReferenceSize().x * squareScaleIn6.widthScale;
                break;
            default: squareHeight = mListener.getReferenceSize().x * squareScale.widthScale;
                break;
        }


        scrollView.addView(contentLayout);
        double eachQuestionHeight = mListener.getReferenceSize().y * intervalScaleOfQuestions +  mListener.getReferenceSize().y * intervalScaleOfQuestionAndSquare * 3 + squareHeight * 2 + mListener.getReferenceSize().y * questionScale.heightScale + mListener.getReferenceSize().y * answerBtnScale.heightScale;
        double contentSize = mListener.getReferenceSize().y * questionScale.yScale + eachQuestionHeight * numberOfQuestions;
        if(this.questions.get(0).information.difficultyOfQuestion < 6){
            contentLayout.setLayoutParams(new FrameLayout.LayoutParams(mListener.getReferenceSize().x, (int)contentSize));
            backgroundView.setLayoutParams(new FrameLayout.LayoutParams(mListener.getReferenceSize().x, (int)contentSize));
        }else{
            double contentWidth = mListener.getReferenceSize().x * squareScale.xScale + mListener.getReferenceSize().x * (squareScaleIn6.widthScale) * this.questions.get(0).information.difficultyOfQuestion + mListener.getReferenceSize().y * intervalLengthOfCharacterScale * 6;
            contentLayout.setLayoutParams(new FrameLayout.LayoutParams(mListener.getReferenceSize ().x > (int)contentWidth ? mListener.getReferenceSize().x : (int)contentWidth, (int)contentSize));
            backgroundView.setLayoutParams(new FrameLayout.LayoutParams((int)contentWidth, (int)contentSize));
            if(contentWidth > mListener.getReferenceSize().x) {
                ImageView noteViewOfCharacter6 = new ImageView(getContext());
                noteViewOfCharacter6.setImageResource(R.drawable.game_note_six_characters);
                noteViewOfCharacter6.setScaleType(ImageView.ScaleType.FIT_XY);
                noteViewOfCharacter6.setPadding(0, 0, 0, 0);
                double noteWidth = mListener.getReferenceSize().x * noteScaleOfSixCharacters.widthScale;
                double noteHeight = mListener.getReferenceSize().y * noteScaleOfSixCharacters.heightScale;
                FrameLayout.LayoutParams lpNote = new FrameLayout.LayoutParams((int)noteWidth, (int)noteHeight);
                double noteTopMargin = mListener.getReferenceSize().y * noteScaleOfSixCharacters.yScale;
                double noteLeftMargin = mListener.getReferenceSize().x * noteScaleOfSixCharacters.xScale;
                lpNote.leftMargin = (int)noteLeftMargin;
                lpNote.topMargin = (int)noteTopMargin;
                noteViewOfCharacter6.setLayoutParams(lpNote);
                contentLayout.addView(noteViewOfCharacter6);
            }
        }
        scrollEnabled = true;
        ImageView noteView = new ImageView(getContext());
        noteView.setImageResource(R.drawable.game_note);
        noteView.setScaleType(ImageView.ScaleType.FIT_XY);
        noteView.setPadding(0, 0, 0, 0);
        double noteWidth = mListener.getReferenceSize().x * noteScale.widthScale;
        double noteHeight = mListener.getReferenceSize().y * noteScale.heightScale;
        FrameLayout.LayoutParams lpNote = new FrameLayout.LayoutParams((int)noteWidth, (int)noteHeight);
        double noteTopMargin = mListener.getReferenceSize().y * noteScale.yScale;
        double noteLeftMargin = mListener.getReferenceSize().x * noteScale.xScale;
        lpNote.leftMargin = (int)noteLeftMargin;
        lpNote.topMargin = (int)noteTopMargin;
        noteView.setLayoutParams(lpNote);
        contentLayout.addView(noteView);
        contentLayout.addView(backgroundView);
        showQuestionViews();
    }


    private void showQuestionViews(){
        double intervalLengthOfQuestionAndSquare = mListener.getReferenceSize().y * intervalScaleOfQuestionAndSquare;
        double intervalLengthOfQuestions = mListener.getReferenceSize().y * intervalScaleOfQuestions;
        double intervalLengthOfCharacters = mListener.getReferenceSize().x * intervalLengthOfCharacterScale;
        double currentYPoint = mListener.getReferenceSize().y * questionScale.yScale;



        for(int i = 0;  i < numberOfQuestions; i++){
            ImageView questionLabel = new ImageView(getContext());
            questionLabel.setScaleType(ImageView.ScaleType.FIT_XY);
            questionLabel.setPadding(0, 0, 0, 0);
            questionLabel.setImageResource(R.drawable.question_string_label);
            double questionWidth = mListener.getReferenceSize().x * questionScale.widthScale;
            double questionHeight = mListener.getReferenceSize().y * questionScale.heightScale;
            FrameLayout.LayoutParams lpQuestion = new FrameLayout.LayoutParams((int)questionWidth, (int)questionHeight);
            double questionLeftMargin = mListener.getReferenceSize().x * questionScale.xScale;
            lpQuestion.leftMargin = (int)questionLeftMargin;
            lpQuestion.topMargin = (int)currentYPoint;
            questionHeights.add((int)currentYPoint);
            questionLabel.setLayoutParams(lpQuestion);
            questionLabelViews.add(questionLabel);
            contentLayout.addView(questionLabel);
            showQuestionNumberLabel(i + 1, lpQuestion);
            double btnWidth = mListener.getReferenceSize().x * answerBtnScale.widthScale;
            double btnHeight = mListener.getReferenceSize().y * answerBtnScale.heightScale;
            FrameLayout.LayoutParams btnSize = new FrameLayout.LayoutParams((int)btnWidth, (int)btnHeight);
            double squareSize;

            switch (this.questions.get(0).information.difficultyOfQuestion){
                case CHARACTER_3: squareSize = mListener.getReferenceSize().x * squareScale.widthScale;
                    break;
                case CHARACTER_4: squareSize = mListener.getReferenceSize().x * squareScale.widthScale;
                    break;
                case CHARACTER_5: squareSize = mListener.getReferenceSize().x * squareScaleIn5.widthScale;
                    break;
                case CHARACTER_6: squareSize = mListener.getReferenceSize().x * squareScaleIn6.widthScale;
                    break;
                default: squareSize = mListener.getReferenceSize().x * squareScale.widthScale;
                    break;
            }

            currentYPoint += questionHeight + intervalLengthOfQuestionAndSquare;
            char[] characterArray = questions.get(i).information.shuffleWord().toCharArray();
            for (int j = 0; j < 2; j++){
                double currentXPoint = mListener.getReferenceSize().x * squareScale.xScale;
                for (int k = 0; k < questions.get(i).information.difficultyOfQuestion; k++){

                    if(j == 0){
                        BlankSquareView square = new BlankSquareView(getContext());
                        FrameLayout.LayoutParams lpSquare = new FrameLayout.LayoutParams((int)squareSize, (int)squareSize);
                        lpSquare.leftMargin = (int)currentXPoint;
                        lpSquare.topMargin = (int)currentYPoint;
                        square.setFrameLayoutParams(lpSquare);
                        square.setPadding(0,0,0,0);
                        contentLayout.addView(square);
                        questions.get(i).squareViews.add(square);
                    }else{
                        FrameLayout.LayoutParams lpCharacter = new FrameLayout.LayoutParams((int)squareSize, (int)squareSize);
                        lpCharacter.leftMargin = (int)currentXPoint;
                        lpCharacter.topMargin = (int)currentYPoint;
                        CharacterView characterView = new CharacterView(getContext(),characterArray[k], this,questions.get(i), lpCharacter);
                        characterView.setPadding(0,0,0,0);
                        characterView.setScaleType(ImageView.ScaleType.FIT_XY);
                        characterView.layout((int)currentXPoint, (int)currentYPoint,(int)squareSize, (int)squareSize);
                        contentLayout.addView(characterView);
                        questions.get(i).characterViews.add(characterView);
                    }


                    currentXPoint += squareSize + intervalLengthOfCharacters;
                }

                currentYPoint += squareSize + intervalLengthOfQuestionAndSquare;

            }
            showBtns(i, currentYPoint,btnSize);
            currentYPoint += intervalLengthOfQuestions + btnSize.height;
        }
    }

    private void showBtns(int questionNumber, double topMargin, FrameLayout.LayoutParams btnSize){

        final double intervalScaleOfBtns = 0.0141;
        double intervalLengthOfBtn = mListener.getReferenceSize().x * intervalScaleOfBtns;

        FrameLayout.LayoutParams lpAnswer = new FrameLayout.LayoutParams(btnSize);
        double btnLeftMargin = mListener.getReferenceSize().x * questionScale.xScale;
        lpAnswer.leftMargin = (int)btnLeftMargin;
        lpAnswer.topMargin = (int)topMargin;
        questions.get(questionNumber).getAnswerBtn().setLayoutParams(lpAnswer);
        contentLayout.addView(questions.get(questionNumber).answerBtn);

        FrameLayout.LayoutParams lpReset = new FrameLayout.LayoutParams(btnSize);

        lpReset.leftMargin = (int)btnLeftMargin + btnSize.width + (int)intervalLengthOfBtn;
        lpReset.topMargin = (int)topMargin;
        questions.get(questionNumber).getResetBtn().setLayoutParams(lpReset);
        contentLayout.addView(questions.get(questionNumber).resetBtn);


    }

    private void showQuestionNumberLabel(int number, FrameLayout.LayoutParams layoutParams){
        final Scale questionNumberSizeScale = new Scale(0.0325, 0.0326);
        final Scale questionNumber1SizeScale = new Scale(0.02288, 0.0326);
        final Scale questionNumber10SizeScale = new Scale(0.05186, 0.0326);
        final double intervalLengthScaleOfQuestionToNumber = 0.012;
        final double additionOfQuestionNumber1XScale = 0.00481;
        final double subtractionOfQuestionNumber10XScale = -0.00568;

        ImageView questionNumberView = new ImageView(getContext());
        questionNumberView.setScaleType(ImageView.ScaleType.FIT_XY);
        questionNumberView.setPadding(0,0,0,0);
        FrameLayout.LayoutParams lpNumber;
        String string = String.format(Locale.ENGLISH, "question_number%d", number);
        int strId = getResources().getIdentifier(string, "drawable", getContext().getPackageName());
        questionNumberView.setImageResource(strId);
        if(number == 1){
            double numberWidth = mListener.getReferenceSize().x * questionNumber1SizeScale.widthScale;
            double numberHeight = mListener.getReferenceSize().y * questionNumber1SizeScale.heightScale;
            lpNumber = new FrameLayout.LayoutParams((int)numberWidth, (int)numberHeight);
            double numberLeftMargin = layoutParams.leftMargin + layoutParams.width + mListener.getReferenceSize().x * (intervalLengthScaleOfQuestionToNumber + additionOfQuestionNumber1XScale);
            lpNumber.leftMargin = (int)numberLeftMargin;
            lpNumber.topMargin = layoutParams.topMargin;
        }else if(number == 10){
            double numberWidth = mListener.getReferenceSize().x * questionNumber10SizeScale.widthScale;
            double numberHeight = mListener.getReferenceSize().y * questionNumber10SizeScale.heightScale;
            lpNumber = new FrameLayout.LayoutParams((int)numberWidth, (int)numberHeight);
            double numberLeftMargin = layoutParams.leftMargin + layoutParams.width + mListener.getReferenceSize().x * (intervalLengthScaleOfQuestionToNumber + subtractionOfQuestionNumber10XScale);
            lpNumber.leftMargin = (int)numberLeftMargin;
            lpNumber.topMargin = layoutParams.topMargin;
        }else{
            double numberWidth = mListener.getReferenceSize().x * questionNumberSizeScale.widthScale;
            double numberHeight = mListener.getReferenceSize().y * questionNumberSizeScale.heightScale;
            lpNumber = new FrameLayout.LayoutParams((int)numberWidth, (int)numberHeight);
            double numberLeftMargin = layoutParams.leftMargin + layoutParams.width + mListener.getReferenceSize().x * intervalLengthScaleOfQuestionToNumber;
            lpNumber.leftMargin = (int)numberLeftMargin;
            lpNumber.topMargin = layoutParams.topMargin;
        }
        questionNumberView.setLayoutParams(lpNumber);
        contentLayout.addView(questionNumberView);
    }

    public void disableScroll(){
        scrollView.setScrollEnable(false);
    }

    public void enableScroll(){
        scrollView.setScrollEnable(true);
    }

    private void startTimer(){
        tm = new Timer();
        tm.schedule(new TimerTask() {
            Handler handler = new Handler();

            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        time -= 1;
                        showTimeCount(false);
                        if(time == 0){
                            disableBtns();
                            mListener.countDownCountFinished();
                        }else if(time < 0){
                            tm.cancel();
                            tm = null;
                            gameOver(false);
                        }

                        if((time > 0 && time < 11) || time == 30 || time == 20 || time == 60){
                            mListener.countDownSound();
                        }

                    }
                });
            }
        }, 1000, 1000);
    }

    /**
     * Called when the Fragment is no longer started.  This is generally
     * tied to {@link Activity#onStop() Activity.onStop} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStop() {
        super.onStop();
        if(tm != null) {
            tm.cancel();
            tm = null;
        }
        gameStarted = true;

    }

    private void fetchQuestionData(){
        AssetManager assetManager = getContext().getApplicationContext().getResources().getAssets();
        try {
            final String fileName = String.format(Locale.ENGLISH, "%s%d.csv", this.stage.getLevel().getLabel(), this.stage.getNumberOfStage() + 1);
            InputStream inputStream = assetManager.open(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferReader = new BufferedReader(inputStreamReader);
            String line;
            if((line = bufferReader.readLine()) != null) {
                String[] data = line.split(",", 0);
                List<Integer> selectedQuestionNumber = new ArrayList<>();
                for(int i = 0; i < numberOfQuestions; i++){

                    SecureRandom r = new SecureRandom();
                    int number;
                    do{
                        number = r.nextInt(data.length);
                    } while(selectedQuestionNumber.contains(number));
                    selectedQuestionNumber.add(number);
                    String question = data[number];
                    String[] words = question.split(" ", 0);
                    Map<String,String> wordsAndAnswers = new HashMap<>();
                    for(String word : words){

                        final String[] wordAndAnswer = word.split(":", 0);
                        wordsAndAnswers.put(wordAndAnswer[0],wordAndAnswer[1]);
                        Log.d("answer",wordAndAnswer[0]);

                    }
                    ImageButton answerBtn = new ImageButton(getContext());
                    answerBtn.setPadding(0,0,0,0);
                    answerBtn.setScaleType(ImageView.ScaleType.FIT_XY);
                    answerBtn.setImageResource(R.drawable.answer_btn);
                    ImageButton resetBtn = new ImageButton(getContext());
                    resetBtn.setPadding(0,0,0,0);
                    resetBtn.setScaleType(ImageView.ScaleType.FIT_XY);
                    resetBtn.setImageResource(R.drawable.reset_btn);

                    questions.add(new Question(i, wordsAndAnswers, this, answerBtn, resetBtn));
                }
            }
            bufferReader.close();
        } catch(IOException e){

        }
    }


    public void isStageClear(){
        for(Question question : questions){
            if(!question.clear){
                ringCorrectSound();
                return;
            }
        }
        this.ringStageClearSound();
        tm.cancel();
        gameOver(true);
    }

    private void disableBtns(){
        for(Question question : questions){
            question.getAnswerBtn().setEnabled(false);
            question.getResetBtn().setEnabled(false);
        }
    }


    public Integer touchPositionOnSquare(float x, float y, Question question){
        for(int i = 0; i < question.squareViews.size(); i++){
            int[] anchorPos = new int[2];
            question.squareViews.get(i).getLocationOnScreen(anchorPos);
            FrameLayout.LayoutParams lpSquare = (FrameLayout.LayoutParams)question.squareViews.get(i).getLayoutParams();
            float leftPoint = lpSquare.leftMargin;
            float rightPoint = lpSquare.leftMargin + lpSquare.width;
            float topPoint = anchorPos[1];
            float bottomPoint = topPoint + lpSquare.height;
            if(x > leftPoint && x < rightPoint && y > topPoint && y < bottomPoint){
                return i;
            }
        }
        return null;

    }

    public void placeOnSquare(Integer position, Question question, CharacterView characterView){
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(question.squareViews.get(position).getFrameLayoutParams());
        if(question.squareViews.get(position).characterView != null){
            if(question.squareViews.get(position).characterView == characterView){
                characterView.setLayoutParams(layoutParams);
            }else{
                question.squareViews.get(position).characterView.backDefaultPosition();
                if(characterView.currentPositionInSquare != null){
                    releaseCharacterView(characterView.currentPositionInSquare, characterView.question);
                }
                question.squareViews.get(position).characterView = characterView;
                characterView.setLayoutParams(layoutParams);
            }
        }else{
            if(characterView.currentPositionInSquare != null){
                releaseCharacterView(characterView.currentPositionInSquare, characterView.question);
            }
            question.squareViews.get(position).characterView = characterView;
            characterView.setLayoutParams(layoutParams);
        }
        characterView.currentPositionInSquare = position;
    }

    public void showCorrectCircle(int questionNumber){
        final Scale correctCircleScale = new Scale(-0.0229, -0.0201, 0.1288, 0.0717);
        double circleWidth = mListener.getReferenceSize().x * correctCircleScale.widthScale;
        double circleHeight = mListener.getReferenceSize().y * correctCircleScale.heightScale;
        FrameLayout.LayoutParams lpCircle = new FrameLayout.LayoutParams((int)circleWidth, (int)circleHeight);
        double circleLeftMargin = questionLabelViews.get(questionNumber).getLeft() + mListener.getReferenceSize().x * correctCircleScale.xScale;
        double circleTopMargin = questionHeights.get(questionNumber) + mListener.getReferenceSize().y * correctCircleScale.yScale;
        lpCircle.leftMargin = (int)circleLeftMargin;
        lpCircle.topMargin = (int)circleTopMargin;
        ImageView correctCircle = new ImageView(getContext());
        correctCircle.setPadding(0, 0, 0, 0);
        correctCircle.setScaleType(ImageView.ScaleType.FIT_XY);
        correctCircle.setImageResource(R.drawable.correct_mark);
        correctCircle.setLayoutParams(lpCircle);
        contentLayout.addView(correctCircle);
        correctCircle.post(new Runnable() {
            @Override
            public void run() {
                isStageClear();
            }
        });
    }

    public void showIncorrectCircle(int questionNumber){
        final Scale incorrectCircleScale = new Scale(0.0129, -0.0201, 0.1288, 0.0667);
        double circleWidth = mListener.getReferenceSize().x * incorrectCircleScale.widthScale;
        double circleHeight = mListener.getReferenceSize().y * incorrectCircleScale.heightScale;
        FrameLayout.LayoutParams lpCircle = new FrameLayout.LayoutParams((int)circleWidth, (int)circleHeight);
        double circleLeftMargin = questionLabelViews.get(questionNumber).getLeft() + mListener.getReferenceSize().x * incorrectCircleScale.xScale;
        double circleTopMargin = questionHeights.get(questionNumber) + (int)(mListener.getReferenceSize().y * incorrectCircleScale.yScale);
        lpCircle.leftMargin = (int)circleLeftMargin;
        lpCircle.topMargin = (int)circleTopMargin;
        final ImageView incorrectCircle = new ImageView(getContext());
        incorrectCircle.setPadding(0, 0, 0, 0);
        incorrectCircle.setScaleType(ImageView.ScaleType.FIT_XY);
        incorrectCircle.setImageResource(R.drawable.incorrect_circle);
        incorrectCircle.setLayoutParams(lpCircle);
        contentLayout.addView(incorrectCircle);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat( incorrectCircle, "alpha", 1f, 0f );

        objectAnimator.setDuration(1000);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                contentLayout.removeView(incorrectCircle);
            }
        });
        objectAnimator.start();
    }

    public void releaseCharacterView(int position, Question question){
        question.squareViews.get(position).characterView = null;
    }

    public void ringWrongSound(){
        mListener.wrongSound();
    }

    public void ringCorrectSound(){
        mListener.correctSound();
    }

    public void ringStageClearSound(){
        mListener.stageClearSound();
    }


    private void gameOver(boolean result){
        mListener.stopSound();

        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
        }
        List<Map<String, String>> answers = new ArrayList<>();
        for(int i = 0; i < stage.getNumberOfQuestion(); i++){
            answers.add(questions.get(i).information.wordAndAnswer);
        }
        Gson gson = new Gson();
        String answerString = gson.toJson(answers);

        layout.removeAllViewsInLayout();
        questions.clear();
        minViews.clear();
        secViews.clear();
        questionLabelViews.clear();
        mListener.goToGameOverFragment(result, stage.getLevel().getLevelNumber(), stage.getNumberOfStage(), answerString,result ? stage.getTime() - time : 0);
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
        void correctSound();
        void wrongSound();
        void stageClearSound();
        void turnDownVolume();
        void countDownSound();
        void countDownCountFinished();
        void stopSound();
        void goToGameOverFragment(boolean result, int level, int stage, String answer, int time);
    }
}
