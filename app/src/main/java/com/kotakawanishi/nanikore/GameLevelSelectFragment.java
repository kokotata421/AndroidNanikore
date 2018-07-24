package com.kotakawanishi.nanikore;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.nifty.cloud.mb.core.FindCallback;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;
import com.nifty.cloud.mb.core.NCMBQuery;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GameLevelSelectFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GameLevelSelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameLevelSelectFragment extends Fragment implements StageButtonFrameLayout.StageButtonCallbacks {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FrameLayout layout;
    private ImageView levelSelectLabel;
    private ImageButton easyLevelBtn;
    private ImageButton normalLevelBtn;
    private ImageButton hardLevelBtn;
    private ImageButton nextArrow;
    private ImageButton backArrow;
    private ImageButton homeBtn;
    private List<StageButtonFrameLayout> stageBtns = new ArrayList<>();
    private int pageNumber;
    private Level selectedLevel;
    boolean[] selectedLevelRecord;
    ArrayList<Integer> clearNumberRecord;
    int accessibleStage;


    enum Level{
        EASY,
        NORMAL,
        HARD;

        public String getLabel(){
            switch (this){
                case EASY: return "Easy";
                case NORMAL: return "Normal";
                case HARD: return "Hard";
            }
            return null;
        }

        public int getLevelNumber(){
            switch (this){
                case EASY: return 0;
                case NORMAL: return 1;
                case HARD: return 2;
            }
            return 0;
        }

        public static GameLevelSelectFragment.Level getLevel(int level){
            switch (level){
                case 0: return EASY;
                case 1: return NORMAL;
                case 2: return HARD;
                default:break;
            }
            return EASY;
        }
    }

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public GameLevelSelectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameLevelSelectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameLevelSelectFragment newInstance(String param1, String param2) {
        GameLevelSelectFragment fragment = new GameLevelSelectFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_level_select, container, false);
        layout = (FrameLayout) view.findViewById(R.id.game_level_layout);
        layout.setClipChildren(false);
        configureViews();
        return view;
    }

    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void configureViews(){
        pageNumber = 1;
        final Scale backHomeBtnScale = new Scale(0.0402, 0.0288, 0.1839, 0.0);
        final Scale levelSelectLabelScale = new Scale(0.0, 0.1638, 0.82, 0.1025);
        final Scale eachLevelBtnScale = new Scale(0.0, 0.3173, 0.7246, 0.1025);
        final double eachLevelIntervalHeightScale = 0.0439;
        final Scale arrowScale = new Scale(0.0289, 0.7749, 0.2979, 0.1025);
        levelSelectLabel = new ImageView(getContext());
        levelSelectLabel.setImageResource(R.drawable.level_select);
        levelSelectLabel.setPadding(0,0,0,0);
        levelSelectLabel.setScaleType(ImageView.ScaleType.FIT_XY);
        easyLevelBtn = new ImageButton(getContext());
        easyLevelBtn.setPadding(0,0,0,0);
        easyLevelBtn.setScaleType(ImageView.ScaleType.FIT_XY);
        easyLevelBtn.setTag(0);
        easyLevelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout.removeView(easyLevelBtn);
                layout.removeView(normalLevelBtn);
                layout.removeView(hardLevelBtn);
                levelSelect(view);
            }
        });
        easyLevelBtn.setImageResource(R.drawable.easy_btn);
        normalLevelBtn = new ImageButton(getContext());
        normalLevelBtn.setPadding(0,0,0,0);
        normalLevelBtn.setScaleType(ImageView.ScaleType.FIT_XY);
        normalLevelBtn.setTag(1);
        normalLevelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout.removeView(easyLevelBtn);
                layout.removeView(normalLevelBtn);
                layout.removeView(hardLevelBtn);
                levelSelect(view);
            }
        });
        normalLevelBtn.setImageResource(R.drawable.normal_btn);
        hardLevelBtn = new ImageButton(getContext());
        hardLevelBtn.setPadding(0,0,0,0);
        hardLevelBtn.setScaleType(ImageView.ScaleType.FIT_XY);
        hardLevelBtn.setTag(2);
        hardLevelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout.removeView(easyLevelBtn);
                layout.removeView(normalLevelBtn);
                layout.removeView(hardLevelBtn);
                levelSelect(view);
            }
        });
        hardLevelBtn.setImageResource(R.drawable.hard_btn);

        homeBtn = new ImageButton(getContext());
        homeBtn.setImageResource(R.drawable.home_btn);
        homeBtn.setPadding(0, 0,0,0);
        homeBtn.setScaleType(ImageView.ScaleType.FIT_XY);
        double homeBtnSize = mListener.getReferenceSize().x * backHomeBtnScale.widthScale;
        FrameLayout.LayoutParams lpHomeBtn = new FrameLayout.LayoutParams((int)homeBtnSize, (int)homeBtnSize);
        double homeBtnLeftMargin = mListener.getReferenceSize().x * backHomeBtnScale.xScale;
        double homeBtnTopMargin = mListener.getReferenceSize().y * backHomeBtnScale.yScale;
        lpHomeBtn.leftMargin = (int)homeBtnLeftMargin;
        lpHomeBtn.topMargin = (int)homeBtnTopMargin;
        homeBtn.setLayoutParams(lpHomeBtn);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onHomeBtn();
            }
        });

        double levelSelectLabelWidth = mListener.getReferenceSize().x * levelSelectLabelScale.widthScale;
        double levelSelectLabelHeight = mListener.getReferenceSize().y * levelSelectLabelScale.heightScale;
        FrameLayout.LayoutParams lpLevelSelect = new FrameLayout.LayoutParams((int)levelSelectLabelWidth, (int)levelSelectLabelHeight);
        double levelSelectLeftMargin = mListener.getReferenceSize().x / 2.0 - levelSelectLabelWidth / 2.0;
        double levelSelectTopMargin = mListener.getReferenceSize().y * levelSelectLabelScale.yScale;
        lpLevelSelect.leftMargin = (int)levelSelectLeftMargin;
        lpLevelSelect.topMargin = (int)levelSelectTopMargin;
        levelSelectLabel.setLayoutParams(lpLevelSelect);


        double levelBtnWidth = mListener.getReferenceSize().x * eachLevelBtnScale.widthScale;
        double levelBtnHeight = mListener.getReferenceSize().y * eachLevelBtnScale.heightScale;
        double levelBtnLeftMargin = mListener.getReferenceSize().x / 2.0 - levelBtnWidth / 2.0;

        FrameLayout.LayoutParams lpEasyBtn = new FrameLayout.LayoutParams((int)levelBtnWidth, (int)levelBtnHeight);
        double easyBtnTopMargin = mListener.getReferenceSize().y * eachLevelBtnScale.yScale;
        lpEasyBtn.leftMargin = (int)levelBtnLeftMargin;
        lpEasyBtn.topMargin = (int)easyBtnTopMargin;
        easyLevelBtn.setLayoutParams(lpEasyBtn);

        FrameLayout.LayoutParams lpNormalBtn = new FrameLayout.LayoutParams((int)levelBtnWidth, (int)levelBtnHeight);
        double normalBtnTopMargin = easyBtnTopMargin + levelBtnHeight + mListener.getReferenceSize().y * eachLevelIntervalHeightScale;
        lpNormalBtn.leftMargin = (int)levelBtnLeftMargin;
        lpNormalBtn.topMargin = (int)normalBtnTopMargin;
        normalLevelBtn.setLayoutParams(lpNormalBtn);

        FrameLayout.LayoutParams lpHardBtn = new FrameLayout.LayoutParams((int)levelBtnWidth, (int)levelBtnHeight);
        double hardBtnTopMargin = normalBtnTopMargin + levelBtnHeight + mListener.getReferenceSize().y * eachLevelIntervalHeightScale;
        lpHardBtn.leftMargin = (int)levelBtnLeftMargin;
        lpHardBtn.topMargin = (int)hardBtnTopMargin;
        hardLevelBtn.setLayoutParams(lpHardBtn);

        double arrowWidth = mListener.getReferenceSize().x * arrowScale.widthScale;
        double arrowHeight = mListener.getReferenceSize().y * arrowScale.heightScale;
        double arrowTopMargin = mListener.getReferenceSize().y * arrowScale.yScale;

        backArrow = new ImageButton(getContext());
        backArrow.setImageResource(R.drawable.back_arrow);
        backArrow.setScaleType(ImageView.ScaleType.FIT_XY);
        backArrow.setPadding(0,0,0,0);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPage();
            }
        });

        FrameLayout.LayoutParams lpBackArrow = new FrameLayout.LayoutParams((int)arrowWidth, (int)arrowHeight);
        double backArrowLeftMargin = mListener.getReferenceSize().x * arrowScale.xScale;
        lpBackArrow.leftMargin = (int)backArrowLeftMargin;
        lpBackArrow.topMargin = (int)arrowTopMargin;
        backArrow.setLayoutParams(lpBackArrow);
        backArrow.setVisibility(View.INVISIBLE);

        nextArrow = new ImageButton(getContext());
        nextArrow.setImageResource(R.drawable.next_arrow);
        nextArrow.setScaleType(ImageView.ScaleType.FIT_XY);
        nextArrow.setPadding(0,0,0,0);
        FrameLayout.LayoutParams lpNextArrow = new FrameLayout.LayoutParams((int)arrowWidth, (int)arrowHeight);
        double nextArrowLeftMargin = mListener.getReferenceSize().x - backArrowLeftMargin - arrowWidth;
        lpNextArrow.leftMargin = (int)nextArrowLeftMargin;
        lpNextArrow.topMargin = (int)arrowTopMargin;
        nextArrow.setLayoutParams(lpNextArrow);
        nextArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextPage();
            }
        });
        nextArrow.setVisibility(View.INVISIBLE);

        layout.addView(levelSelectLabel);
        layout.addView(homeBtn);
        layout.addView(easyLevelBtn);
        layout.addView(normalLevelBtn);
        layout.addView(hardLevelBtn);
        layout.addView(nextArrow);
        layout.addView(backArrow);
    }

    private  void levelSelect(View view){
        mListener.touchSound();
        homeBtn.setEnabled(false);
        int tag = (int)view.getTag();
        switch(tag){
            case 0:
                selectedLevel = Level.EASY;
                levelSelectLabel.setImageResource(R.drawable.easy_btn);
                break;
            case 1:
                selectedLevel = Level.NORMAL;
                levelSelectLabel.setImageResource(R.drawable.normal_btn);
                break;
            case 2:
                selectedLevel = Level.HARD;
                levelSelectLabel.setImageResource(R.drawable.hard_btn);
                break;
            default:
                break;
        }

        if(!this.isRecordExists()){
            this.createRecord();
        }else{
            this.setClearStage();
        }

        this.fetchClearNumberRecord();
    }

    protected boolean isRecordExists(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String recordString = pref.getString(selectedLevel.getLabel(), null);
        Gson gson = new Gson();
        selectedLevelRecord = gson.fromJson(recordString, boolean[].class);
        return selectedLevelRecord != null;
    }

    protected void createRecord() {
        boolean[] record = new boolean[24];
        for (int i = 0; i < 24; i++) {
            record[i] = false;
        }
        selectedLevelRecord = record;
        Gson gson = new Gson();
        String recordString = gson.toJson(record);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(selectedLevel.getLabel(), recordString);
        editor.apply();
        this.setClearStage();
    }

    protected void setClearStage(){
        int stage = 2;
        do{
            if(selectedLevelRecord[stage])
                stage++;
            else
                break;
        }while(stage < 23);

        accessibleStage = stage;
    }

    private void fetchClearNumberRecord(){
        clearNumberRecord = new ArrayList<>();
        NCMBQuery<NCMBObject> query = new NCMBQuery<>(getString(R.string.passing_people_class_key));
        query.whereEqualTo(getString(R.string.level_key), selectedLevel.getLabel());

        query.findInBackground(new FindCallback<NCMBObject>() {
            @Override
            public void done(List<NCMBObject> results, NCMBException e) {
                if (e != null) {
                    for(int i = 0; i < 24; i++){
                        clearNumberRecord.add(0);
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("データを取得できませんでした。").setPositiveButton("閉じる", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                    showArrows();
                    showStageBtns();
                    homeBtn.setEnabled(true);
                    mListener.showInterstitial();

                } else{
                    NCMBObject data = results.get(0);
                    List numbers = data.getList(getString(R.string.number_key));
                    if(numbers.get(0) instanceof  Double){
                        for (int i = 0; i < numbers.size();i++){
                            Double number = (Double)numbers.get(i);
                            clearNumberRecord.add(number.intValue());
                        }

                    }else if(numbers.get(0) instanceof  Integer){
                        for (int i = 0; i < numbers.size();i++){
                            Integer number = (Integer)numbers.get(i);
                            clearNumberRecord.add(number);
                        }

                    }
                    showArrows();
                    showStageBtns();
                    homeBtn.setEnabled(true);
                    mListener.showInterstitial();
                }
            }
        });

    }

    private void showArrows(){
        nextArrow.setVisibility(View.GONE);
        backArrow.setVisibility(View.GONE);
        switch (pageNumber) {
            case 1:
                nextArrow.setVisibility(View.VISIBLE);
                break;
            case 2:
                nextArrow.setVisibility(View.VISIBLE);
                backArrow.setVisibility(View.VISIBLE);
                break;
            case 3:
                nextArrow.setVisibility(View.VISIBLE);
                backArrow.setVisibility(View.VISIBLE);
                break;
            case 4:
                backArrow.setVisibility(View.VISIBLE);
            default:
                break;
        }
    }

    private void showStageBtns(){
        final Scale stageBtnScale = new Scale(0.0, 0.329, 0.2979, 0.0);
        final double stageBtnHorizontalIntervalScale = 0.02415;
        final double stageBtnVerticalIntervalScale = 0.05017;

        final double horizontalIntervalLength = mListener.getReferenceSize().x * stageBtnHorizontalIntervalScale;
        final double verticalIntervalLength = mListener.getReferenceSize().y * stageBtnVerticalIntervalScale;

        final double stageSize = mListener.getReferenceSize().x * stageBtnScale.widthScale;

        double horizontalWidth = mListener.getReferenceSize().x * stageBtnScale.widthScale * 3 + horizontalIntervalLength * 2;

        double stageBtnX = mListener.getReferenceSize().x / 2.0 - horizontalWidth / 2.0;



        stageBtns = new ArrayList<>();

        for(int i = 0; i < 6; i++){
            StageButtonFrameLayout stageBtn = new StageButtonFrameLayout(getContext(), selectedLevel, 6 * (pageNumber - 1) + i, selectedLevelRecord[6 * (pageNumber - 1) + i]);
            stageBtn.setPadding(0,0,0,0);
            stageBtn.setEnabled(true);
            stageBtn.setCallBack(this);
            stageBtns.add(stageBtn);
        }


        StageButtonFrameLayout stageBtn = stageBtns.get(0);
        FrameLayout.LayoutParams lpStageBtn = new FrameLayout.LayoutParams((int)stageSize, (int)stageSize);
        double stageLeftMargin = (int)stageBtnX;
        double stageTopMargin = mListener.getReferenceSize().y * stageBtnScale.yScale;
        lpStageBtn.setMargins((int)stageLeftMargin,(int)stageTopMargin,0,0);
        stageBtn.setLayoutParams(lpStageBtn);
        layout.addView(stageBtn);
        double previousLeftMargin = stageLeftMargin;

        for(int i = 1; i < 3; i++){
            StageButtonFrameLayout newStageBtn = stageBtns.get(i);
            FrameLayout.LayoutParams newLpStageBtn = new FrameLayout.LayoutParams((int)stageSize, (int)stageSize);
            double newStageLeftMargin = (int)previousLeftMargin + stageSize + horizontalIntervalLength;
            newLpStageBtn.leftMargin = (int)newStageLeftMargin;
            newLpStageBtn.topMargin = (int)stageTopMargin;
            newStageBtn.setLayoutParams(newLpStageBtn);
            layout.addView(newStageBtn);
            previousLeftMargin = newStageLeftMargin;
        }

        StageButtonFrameLayout stageBtn4 = stageBtns.get(3);
        FrameLayout.LayoutParams lpStageBtn4 = new FrameLayout.LayoutParams((int)stageSize, (int)stageSize);
        double stage4LeftMargin = (int)stageBtnX;
        double stage4TopMargin = stageTopMargin + stageBtn.getLayoutParams().height + verticalIntervalLength;
        lpStageBtn4.leftMargin = (int)stage4LeftMargin;
        lpStageBtn4.topMargin = (int)stage4TopMargin;
        stageBtn4.setLayoutParams(lpStageBtn4);
        layout.addView(stageBtn4);
        previousLeftMargin = stageBtnX;
        for(int i = 4; i < 6; i++){
            StageButtonFrameLayout newStageBtn = stageBtns.get(i);
            FrameLayout.LayoutParams newLpStageBtn = new FrameLayout.LayoutParams((int)stageSize, (int)stageSize);
            double newStageLeftMargin = previousLeftMargin + stageSize + horizontalIntervalLength;
            newLpStageBtn.leftMargin = (int)newStageLeftMargin;
            newLpStageBtn.topMargin = (int)stage4TopMargin;
            newStageBtn.setLayoutParams(newLpStageBtn);
            layout.addView(newStageBtn);
            previousLeftMargin = newStageLeftMargin;
        }
        this.configureStageViews();
    }

    private void configureStageViews(){
        for(int i = 0; i < 6; i++){
            StageButtonFrameLayout btn = stageBtns.get(i);
            if(accessibleStage >= btn.getStageBtn().getStage().getNumberOfStage()){
                if(btn.getStageBtn().getStage().getClear())
                    btn.addWellDoneStamp();
                btn.addNumberView();
                btn.addSuccessfulCandidates(clearNumberRecord.get(btn.getStageBtn().getStage().getNumberOfStage()));
            }else{
                btn.addSuccessfulCandidates((clearNumberRecord.get(btn.getStageBtn().getStage().getNumberOfStage())));
                btn.addLockView();
            }
        }
    }

    @Override
    public void selectStage(View view) {
        mListener.touchSound();
        removeStageBtnViews();
        layout.removeAllViewsInLayout();
        StageButton btn = (StageButton)view;
        mListener.goToGameRecord(btn.getStage().getLevel().getLevelNumber(), btn.getStage().getNumberOfStage());
    }

    protected void removeStageBtnViews(){
        for(int i = 0; i < 6; i++){
            StageButtonFrameLayout btn = stageBtns.get(i);
            btn.setCallBack(null);
            layout.removeView(btn);
        }
        stageBtns.clear();
        stageBtns = null;
    }

    public void nextPage(){
        mListener.touchSound();
        pageNumber++;
        removeStageBtnViews();
        this.showStageBtns();
        this.showArrows();
    }

    public void backPage(){
        mListener.touchSound();
        pageNumber--;
        removeStageBtnViews();
        this.showStageBtns();
        this.showArrows();
    }

    public void onHomeBtn(){
        mListener.touchSound2();
        if(stageBtns.size() != 0)
            removeStageBtnViews();
        else {
            layout.removeAllViewsInLayout();
        }
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
        void touchSound2();
        void goBackHome();
        void goToGameRecord(int Level, int stage);
        void showInterstitial();

    }
}
