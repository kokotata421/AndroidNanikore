package com.kotakawanishi.nanikore;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.ads.AdSize;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CheckAnswerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CheckAnswerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckAnswerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String LEVEL_PARAM = "level";
    private static final String STAGE_PARAM = "stage";
    private static final String ANSWER_PARAM = "answer";
    private static final String RESULT_PARAM = "result";
    private static final int CHARACTER_3 = 3;
    private static final int CHARACTER_4 = 4;
    private static final int CHARACTER_5 = 5;
    private static final float ANSWER_SIZE_4 = 16.5f;
    private static final float ANSWER_SIZE_5 = 15.5f;
    private static final float ANSWER_SIZE_6 = 13f;

    // TODO: Rename and change types of parameters



    //Views

    private ImageView mainBackground;
    private ImageButton playBtn;
    private ImageButton titleBtn;
    private FrameLayout levelStageFrame;
    private FrameLayout contentLayout;
    private ScrollView scrollView;


    //Data
    private Stage stage;
    private boolean result;
    private List<Map<String, String>> answers;

    //Scale
    final Scale answerLabelSizeScaleOf3Characters = new Scale(0.32, 0.03);
    final Scale answerLabelSizeScaleOf4Characters = new Scale(0.38, 0.03);
    final Scale answerLabelSizeScaleOf5Characters = new Scale(0.47, 0.03);
    final Scale answerLabelSizeScaleOf6Characters = new Scale(0.4, 0.03);
    final Scale btnSizeScale = new Scale(0.0, 0.79,0.3621, 0.0908);
    final Scale questionScale = new Scale(0.02, 0.1923, 0.0532, 0.03);
    final Double answer1XScale = 0.02;
    final Double answer2XScale = 0.51;
    final double intervalScaleOfAnswers = 0.04;
    final double intervalScaleOfExtraAnswer = 0.03;
    final double intervalScaleOfQuestionLabelToAnswer = 0.02;
    final double minimumContentViewSizeScale = 0.1929;
    final double intervalScaleOfBtns = 0.012;


    private OnFragmentInteractionListener mListener;

    public CheckAnswerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CheckAnswerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckAnswerFragment newInstance(int level, int stage,String answer, boolean result) {
        CheckAnswerFragment fragment = new CheckAnswerFragment();
        Bundle args = new Bundle();
        args.putInt(LEVEL_PARAM, level);
        args.putInt(STAGE_PARAM, stage);
        args.putString( ANSWER_PARAM, answer);
        args.putBoolean( RESULT_PARAM,result);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String answerG;

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            answerG = getArguments().getString(ANSWER_PARAM);
            result = getArguments().getBoolean(RESULT_PARAM);
            stage = new Stage(GameLevelSelectFragment.Level.getLevel(getArguments().getInt(LEVEL_PARAM)), getArguments().getInt(STAGE_PARAM));
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Map<String, String>>>(){}.getType();
            answers = gson.fromJson(answerG, listType);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check_answer, container, false);

        mainBackground = (ImageView) view.findViewById(R.id.background_check_answer);
        playBtn = (ImageButton)  view.findViewById(R.id.play_again_btn_check_answer);
        titleBtn = (ImageButton) view.findViewById(R.id.title_btn_check_answer);
        levelStageFrame = (FrameLayout) view.findViewById(R.id.level_stage_frame_check_answer);
        contentLayout = (FrameLayout) view.findViewById(R.id.content_layout_check_answer);
        scrollView = (ScrollView) view.findViewById(R.id.scroll_view_check_answer);

        configureViews();

        return view;
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

    }

    private void configureViews(){
        FrameLayout.LayoutParams lpScroll = new FrameLayout.LayoutParams(mListener.getReferenceSize().x, mListener.getReferenceSize().y - AdSize.SMART_BANNER.getHeightInPixels(getContext()));
        scrollView.setLayoutParams(lpScroll);
        this.showLevelStageLabel();
        showViews();
    }

    private void showViews(){
        this.configureContentViews();
        this.showLevelStageLabel();
    }
    
    private void showAnswerLabels(){
        double intervalLengthOfQuestionLabelToAnswer = mListener.getReferenceSize().y * intervalScaleOfQuestionLabelToAnswer;
        double intervalLengthOfAnswers = mListener.getReferenceSize().y * intervalScaleOfAnswers;
        double currentYPoint = mListener.getReferenceSize().y * questionScale.yScale;
        double answerLabelHeight;

        for(int i = 0; i < answers.size(); i++){
            ImageView questionLabel = new ImageView(getContext());
            questionLabel.setImageResource(R.drawable.question_string_label);
            questionLabel.setPadding(0, 0, 0, 0);
            questionLabel.setScaleType(ImageView.ScaleType.FIT_XY);
            double questionWidth = mListener.getReferenceSize().x * questionScale.widthScale;
            double questionHeight = mListener.getReferenceSize().y * questionScale.heightScale;
            double questionLeftMargin = mListener.getReferenceSize().x * questionScale.xScale;
            FrameLayout.LayoutParams lpQuestion = new FrameLayout.LayoutParams((int)questionWidth, (int)questionHeight);
            lpQuestion.leftMargin = (int)questionLeftMargin;
            lpQuestion.topMargin = (int)currentYPoint;
            questionLabel.setLayoutParams(lpQuestion);
            contentLayout.addView(questionLabel);
            showQuestionNumberLabel(i + 1, lpQuestion);
            currentYPoint += intervalLengthOfQuestionLabelToAnswer + questionHeight;
            int count = 1;
            Map<String, String> answer = answers.get(i);
            answerLabelHeight = mListener.getReferenceSize().y * answerLabelSizeScaleOf3Characters.heightScale;
            for(String key: answer.keySet()){

                if (count % 2 == 1 && count > 2){
                    currentYPoint += mListener.getReferenceSize().y * answerLabelSizeScaleOf3Characters.heightScale + mListener.getReferenceSize().y * intervalScaleOfExtraAnswer;
                }
                TextView textView = new TextView(getContext());
                textView.setPadding(0, 0, 0, 0);
                textView.setText(String.format(Locale.ENGLISH, "%s:%s", key, answer.get(key)));
                textView.setGravity(Gravity.START);
                textView.setTextColor(Color.BLACK);
                Typeface typefaceOriginal = Typeface.createFromAsset(getContext().getAssets(), "font/arial.ttf");
                textView.setTypeface(typefaceOriginal);
                double textLeftMargin = count % 2 == 1 ? mListener.getReferenceSize().x * answer1XScale : mListener.getReferenceSize().x * answer2XScale;
                double textWidth;
                double textHeight;
                switch (answers.get(0).keySet().iterator().next().length()){
                    case CHARACTER_3:

                    case CHARACTER_4:
                        textWidth = mListener.getReferenceSize().x * answerLabelSizeScaleOf4Characters.widthScale;
                        textHeight = mListener.getReferenceSize().y * answerLabelSizeScaleOf4Characters.heightScale;
                        textView.setTextSize(ANSWER_SIZE_4);
                        break;
                    case CHARACTER_5:
                        textWidth = mListener.getReferenceSize().x * answerLabelSizeScaleOf5Characters.widthScale;
                        textHeight = mListener.getReferenceSize().y * answerLabelSizeScaleOf5Characters.heightScale;
                        textView.setTextSize(ANSWER_SIZE_5);
                        break;
                    default:
                        textWidth = mListener.getReferenceSize().x * answerLabelSizeScaleOf6Characters.widthScale;
                        textHeight = mListener.getReferenceSize().y * answerLabelSizeScaleOf6Characters.heightScale;
                        textView.setTextSize(ANSWER_SIZE_6);
                        break;
                }
                FrameLayout.LayoutParams lpText = new FrameLayout.LayoutParams((int)textWidth, (int)textHeight);
                lpText.leftMargin = (int)textLeftMargin;
                lpText.topMargin = (int)currentYPoint;
                textView.setLayoutParams(lpText);
                contentLayout.addView(textView);
                count += 1;

            }
            currentYPoint += intervalLengthOfAnswers + answerLabelHeight;
        }
        showBtns(currentYPoint);
    }
    
    private void showBtns(double topMargin){
        double btnWidth = mListener.getReferenceSize().x * btnSizeScale.widthScale;
        double btnHeight = mListener.getReferenceSize().y * btnSizeScale.heightScale;

        FrameLayout.LayoutParams lpTitleBtn = new FrameLayout.LayoutParams((int)btnWidth, (int)btnHeight);
        double titleBtnLeftMargin = mListener.getReferenceSize().x / 2.0 + mListener.getReferenceSize().x * intervalScaleOfBtns;
        lpTitleBtn.leftMargin = (int)titleBtnLeftMargin;
        lpTitleBtn.topMargin = (int)topMargin;
        titleBtn.setLayoutParams(lpTitleBtn);
        titleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackTitle();
            }
        });

        FrameLayout.LayoutParams lpPlayBtn = new FrameLayout.LayoutParams((int)btnWidth, (int)btnHeight);
        double playBtnLeftMargin = titleBtnLeftMargin - (mListener.getReferenceSize().x * intervalScaleOfBtns) * 2 - btnWidth;
        lpPlayBtn.leftMargin = (int)playBtnLeftMargin;
        lpPlayBtn.topMargin = (int)topMargin;
        playBtn.setLayoutParams(lpPlayBtn);
        if(result && !(stage.getNumberOfStage() > 22 && stage.getLevel().ordinal() > 1)){
            playBtn.setImageResource(R.drawable.next_stage_btn);
            playBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nextStage();
                }
            });
        }else{
            playBtn.setImageResource(R.drawable.play_again_btn);
            playBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playAgain();
                }
            });
        }
    }

    private void showQuestionNumberLabel(int number, FrameLayout.LayoutParams layoutParams) {
        final Scale questionNumberSizeScale = new Scale(0.0325, 0.03);
        final Scale questionNumber1SizeScale = new Scale(0.02288, 0.03);
        final Scale questionNumber10SizeScale = new Scale(0.05186, 0.03);
        final double intervalLengthScaleOfQuestionToNumber = 0.012;
        final double additionOfQuestionNumber1XScale = 0.00481;
        final double subtractionOfQuestionNumber10XScale = -0.00568;

        ImageView questionNumberView = new ImageView(getContext());
        questionNumberView.setScaleType(ImageView.ScaleType.FIT_XY);
        questionNumberView.setPadding(0, 0, 0, 0);
        FrameLayout.LayoutParams lpNumber;
        String string = String.format(Locale.ENGLISH, "question_number%d", number);
        int strId = getResources().getIdentifier(string, "drawable", getContext().getPackageName());
        questionNumberView.setImageResource(strId);
        if (number == 1) {
            double numberWidth = mListener.getReferenceSize().x * questionNumber1SizeScale.widthScale;
            double numberHeight = mListener.getReferenceSize().y * questionNumber1SizeScale.heightScale;
            lpNumber = new FrameLayout.LayoutParams((int) numberWidth, (int) numberHeight);
            double numberLeftMargin = layoutParams.leftMargin + layoutParams.width + mListener.getReferenceSize().x * (intervalLengthScaleOfQuestionToNumber + additionOfQuestionNumber1XScale);
            lpNumber.leftMargin = (int) numberLeftMargin;
            lpNumber.topMargin = layoutParams.topMargin;
        } else if (number == 10) {
            double numberWidth = mListener.getReferenceSize().x * questionNumber10SizeScale.widthScale;
            double numberHeight = mListener.getReferenceSize().y * questionNumber10SizeScale.heightScale;
            lpNumber = new FrameLayout.LayoutParams((int) numberWidth, (int) numberHeight);
            double numberLeftMargin = layoutParams.leftMargin + layoutParams.width + mListener.getReferenceSize().x * (intervalLengthScaleOfQuestionToNumber + subtractionOfQuestionNumber10XScale);
            lpNumber.leftMargin = (int) numberLeftMargin;
            lpNumber.topMargin = layoutParams.topMargin;
        } else {
            double numberWidth = mListener.getReferenceSize().x * questionNumberSizeScale.widthScale;
            double numberHeight = mListener.getReferenceSize().y * questionNumberSizeScale.heightScale;
            lpNumber = new FrameLayout.LayoutParams((int) numberWidth, (int) numberHeight);
            double numberLeftMargin = layoutParams.leftMargin + layoutParams.width + mListener.getReferenceSize().x * intervalLengthScaleOfQuestionToNumber;
            lpNumber.leftMargin = (int) numberLeftMargin;
            lpNumber.topMargin = layoutParams.topMargin;
        }
        questionNumberView.setLayoutParams(lpNumber);
        contentLayout.addView(questionNumberView);
    }

    private void configureContentViews(){
        final Scale noteScale = new Scale(0.035, 0.1329, 0.6995, 0.01953);

        int numberOfAnswers = answers.size();
        int numberOfExtraHeight = 0;
        for(int i = 0; i < answers.size(); i++){
            if(answers.get(i).keySet().size() > 2){
                numberOfExtraHeight += (answers.get(i).keySet().size() - 1) / 2;
            }
        }

        double eachAnswerHeight = mListener.getReferenceSize().y * intervalScaleOfAnswers + mListener.getReferenceSize().y * intervalScaleOfQuestionLabelToAnswer + mListener.getReferenceSize().y * answerLabelSizeScaleOf3Characters.heightScale + mListener.getReferenceSize().y * questionScale.heightScale;
        double eachExtraHeight = mListener.getReferenceSize().y * intervalScaleOfExtraAnswer + mListener.getReferenceSize().y * answerLabelSizeScaleOf3Characters.heightScale;

        double contentSize = mListener.getReferenceSize().y * minimumContentViewSizeScale + eachAnswerHeight * numberOfAnswers + eachExtraHeight * numberOfExtraHeight + mListener.getReferenceSize().y * btnSizeScale.heightScale + mListener.getReferenceSize().y * intervalScaleOfAnswers;
        contentLayout.setLayoutParams(new FrameLayout.LayoutParams(mListener.getReferenceSize().x, contentSize > scrollView.getLayoutParams().height ? (int)contentSize : scrollView.getLayoutParams().height));
        mainBackground.setLayoutParams(new FrameLayout.LayoutParams(mListener.getReferenceSize().x, contentSize > scrollView.getLayoutParams().height ? (int)contentSize : scrollView.getLayoutParams().height));

        ImageView noteView = new ImageView(getContext());
        noteView.setImageResource(R.drawable.answer_note);
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

        this.showAnswerLabels();
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

    public void nextStage(){
        mListener.touchSound();
        try{
            Thread.sleep(500);
        }catch (InterruptedException e){
        }
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
        try{
            Thread.sleep(500);
        }catch (InterruptedException e){
        }
        mListener.goToGameFragment(stage.getLevel().getLevelNumber(), stage.getNumberOfStage());

    }

    public void goBackTitle(){
        mListener.touchSound();
        mListener.goBackHome();
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
        void touchSound();
        void goBackHome();
        void goToGameFragment(int level, int stage);
    }
}
