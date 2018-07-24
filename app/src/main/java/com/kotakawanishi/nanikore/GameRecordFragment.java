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

import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GameRecordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GameRecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameRecordFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String LEVEL_PARAM = "level";
    private static final String STAGE_PARAM = "stage";


    //Views
    private FrameLayout layout;
    private FrameLayout stageLabelFrame;
    private FrameLayout recordChartFrame;
    private ImageButton takeTheExamBtn;
    private ImageButton backLevelSelectBtn;
    private ImageView stageLabel;

    //Data
    private int level;
    private int stage;
    private boolean setUpDone = false;

    private OnFragmentInteractionListener mListener;

    public GameRecordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameRecordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameRecordFragment newInstance(int param1, int param2) {
        GameRecordFragment fragment = new GameRecordFragment();
        Bundle args = new Bundle();
        args.putInt(LEVEL_PARAM, param1);
        args.putInt(STAGE_PARAM, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            level = getArguments().getInt(LEVEL_PARAM);
            stage = getArguments().getInt(STAGE_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_record, container, false);
        layout = (FrameLayout) view.findViewById(R.id.game_record_layout);
        stageLabelFrame = (FrameLayout) view.findViewById(R.id.stage_label_frame);
        recordChartFrame = (FrameLayout) view.findViewById(R.id.record_chart_frame);
        takeTheExamBtn = (ImageButton) view.findViewById(R.id.take_exam_btn);
        takeTheExamBtn.setEnabled(false);
        backLevelSelectBtn = (ImageButton) view.findViewById(R.id.back_stage_select_btn);
        backLevelSelectBtn.setEnabled(false);
        stageLabel = (ImageView) view.findViewById(R.id.stage_label);
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
        mListener.showInterstitial();
    }

    private void configureViews(){
        final Scale stageLabelScale = new Scale(0.0, 0.0288, 0.6097, 0.1054);
        final Scale recordChartViewScale = new Scale(0.0, 0.1462, 0.8734, 0.6308);
        final Scale takeTheExamBtnScale = new Scale(0.0, 0.7938, 0.5614, 0.1003);
        final Scale backStageSelectBtnScale = new Scale(0.0102, 0.0288, 0.1739, 0.0);


        double stageLabelWidth = mListener.getReferenceSize().x * stageLabelScale.widthScale;
        double stageLabelHeight = mListener.getReferenceSize().y * stageLabelScale.heightScale;
        FrameLayout.LayoutParams lpStageLabel = new FrameLayout.LayoutParams((int)stageLabelWidth, (int)stageLabelHeight);
        double stageLabelLeftMargin = mListener.getReferenceSize().x / 2.0 - stageLabelWidth / 2.0;
        double stageLabelTopMargin = mListener.getReferenceSize().y * stageLabelScale.yScale;
        lpStageLabel.leftMargin = (int)stageLabelLeftMargin;
        lpStageLabel.topMargin = (int)stageLabelTopMargin;

        stageLabelFrame.setLayoutParams(lpStageLabel);
        showViews();

        double recordChartWidth = mListener.getReferenceSize().x * recordChartViewScale.widthScale;
        double recordChartHeight = mListener.getReferenceSize().y * recordChartViewScale.heightScale;
        FrameLayout.LayoutParams lpRecordChart = new FrameLayout.LayoutParams((int)recordChartWidth, (int)recordChartHeight);
        double recordChartLeftMargin = mListener.getReferenceSize().x / 2.0 - recordChartWidth / 2.0;
        double recordChartTopMargin = mListener.getReferenceSize().y * recordChartViewScale.yScale;
        lpRecordChart.leftMargin = (int)recordChartLeftMargin;
        lpRecordChart.topMargin = (int)recordChartTopMargin;
        recordChartFrame.setLayoutParams(lpRecordChart);

        double examBtnWidth = mListener.getReferenceSize().x * takeTheExamBtnScale.widthScale;
        double examBtnHeight = mListener.getReferenceSize().y * takeTheExamBtnScale.heightScale;
        FrameLayout.LayoutParams lpExamBtn = new FrameLayout.LayoutParams((int)examBtnWidth, (int)examBtnHeight);
        double examBtnLeftMargin = mListener.getReferenceSize().x / 2.0 - examBtnWidth / 2.0;
        double examBtnTopMargin = mListener.getReferenceSize().y * takeTheExamBtnScale.yScale;
        lpExamBtn.leftMargin = (int)examBtnLeftMargin;
        lpExamBtn.topMargin = (int)examBtnTopMargin;
        takeTheExamBtn.setLayoutParams(lpExamBtn);
        takeTheExamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeTheExam(view);
            }
        });

        double backBtnSize = mListener.getReferenceSize().x * backStageSelectBtnScale.widthScale;
        FrameLayout.LayoutParams lpBackBtn = new FrameLayout.LayoutParams((int)backBtnSize, (int)backBtnSize);
        double backBtnLeftMargin = mListener.getReferenceSize().x * backStageSelectBtnScale.xScale;
        double backBtnTopMargin = mListener.getReferenceSize().y * backStageSelectBtnScale.yScale;
        lpBackBtn.leftMargin = (int)backBtnLeftMargin;
        lpBackBtn.topMargin = (int)backBtnTopMargin;
        backLevelSelectBtn.setLayoutParams(lpBackBtn);
        backLevelSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backGameSelect(view);
            }
        });

        fetchRecordNumber();

    }

    private void showViews(){

        switch (GameLevelSelectFragment.Level.getLevel(level)){
            case EASY: stageLabel.setImageResource(R.drawable.easy_level_label); break;
            case NORMAL: stageLabel.setImageResource(R.drawable.normal_level_label); break;
            case HARD: stageLabel.setImageResource(R.drawable.hard_level_label); break;
            default: break;
        }

        this.showNumberOnLabel();
    }

    private void showNumberOnLabel(){
        Scale stageLabelNumber1ScaleToLabel = new Scale(0.0, 0.0, 0.05061, 0.4971);
        Scale stageLabelNumberScaleToLabel = new Scale(0.0, 0.0, 0.0736, 0.4971);

        double[] stageNumberXScaleToLabel = {0.7916, 0.6925};
        double additionOfXScaleInStageNumber1 = 0.00249;
        int tempNumber = stage + 1;
        for(int i = 0; i < 2; i++){
            ImageView stageNumberView = new ImageView(getContext());
            stageNumberView.setScaleType(ImageView.ScaleType.FIT_XY);
            stageNumberView.setPadding(0, 0, 0, 0);
            FrameLayout.LayoutParams lpNumber;
            if(i == 1 && this.stage + 1 < 10){
                stageNumberView.setImageResource(R.drawable.stage_number0);
            }else{
                String string = String.format(Locale.ENGLISH, "stage_number%d", tempNumber % 10);
                int strId = getResources().getIdentifier(string, "drawable", getContext().getPackageName());
                stageNumberView.setImageResource(strId);
            }
            if(tempNumber % 10 == 1){
                double numberWidth = stageLabelFrame.getLayoutParams().width * stageLabelNumber1ScaleToLabel.widthScale;
                double numberHeight = stageLabelFrame.getLayoutParams().height * stageLabelNumber1ScaleToLabel.heightScale;
                lpNumber = new FrameLayout.LayoutParams((int)numberWidth, (int)numberHeight);
                double numberLeftMargin = stageLabelFrame.getLayoutParams().width * (stageNumberXScaleToLabel[i] + additionOfXScaleInStageNumber1);
                lpNumber.leftMargin = (int)numberLeftMargin;
                double numberTopMargin = stageLabelFrame.getLayoutParams().height / 2.0 - numberHeight / 2.0;
                lpNumber.topMargin = (int)numberTopMargin;
            }else{
                double numberWidth = stageLabelFrame.getLayoutParams().width * stageLabelNumberScaleToLabel.widthScale;
                double numberHeight = stageLabelFrame.getLayoutParams().height * stageLabelNumberScaleToLabel.heightScale;
                lpNumber = new FrameLayout.LayoutParams((int)numberWidth, (int)numberHeight);
                double numberLeftMargin = stageLabelFrame.getLayoutParams().width * stageNumberXScaleToLabel[i];
                lpNumber.leftMargin = (int)numberLeftMargin;
                double numberTopMargin = stageLabelFrame.getLayoutParams().height / 2.0 - numberHeight / 2.0;
                lpNumber.topMargin = (int)numberTopMargin;
            }
            stageNumberView.setLayoutParams(lpNumber);
            stageLabelFrame.addView(stageNumberView);
            tempNumber /= 10;
        }

    }

    public void takeTheExam(View view){
        mListener.touchSound();
        mListener.goToGameFragment(level, stage);
        layout.removeAllViewsInLayout();
    }

    public void backGameSelect(View view){
        mListener.touchSound2();
        mListener.goToGameLevelFragment();
        layout.removeAllViewsInLayout();
    }

    private void fetchRecordNumber(){
        this.fetchRankRecord();
        this.fetchSelfRecord();
    }

    private void fetchRankRecord(){

        NCMBQuery<NCMBObject> query = new NCMBQuery<>(getString(R.string.stage_record_class_key));
        String value = String.format( Locale.ENGLISH,"%s%d",GameLevelSelectFragment.Level.getLevel(level).getLabel(), stage + 1);
        query.whereEqualTo(getString(R.string.stage_key),  value);
        query.findInBackground(new FindCallback<NCMBObject>() {
            @Override
            public void done(List<NCMBObject> results, NCMBException e) {
                if (e != null) {
                    showRecordNumber(null, false);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("データを取得できませんでした。").setPositiveButton("閉じる", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                    takeTheExamBtn.setEnabled(true);
                } else {
                    if(results.isEmpty()){
                        showRecordNumber(null, false);
                    }else{
                        NCMBObject object = results.get(0);
                        List list = object.getList(getString(R.string.rank_record_key));
                        int[] records = new int[list.size()];
                        if(list.get(0) instanceof  Double){
                            for (int i = 0; i < list.size();i++){
                                Double number = (Double)list.get(i);
                                records[i] = number.intValue();
                            }

                        }else if(list.get(0) instanceof  Integer) {
                            for (int i = 0; i < list.size(); i++) {
                                Integer number = (Integer) list.get(i);
                                records[i] = number;
                            }
                        }
                        if(records[0] != 0)
                            showRecordNumber(records, false);
                        else
                            showRecordNumber(null, false);
                    }
                    takeTheExamBtn.setEnabled(true);
                    backLevelSelectBtn.setEnabled(true);
                }
            }
        });
    }

    private void fetchSelfRecord(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String value = String.format( Locale.ENGLISH,"%s%d",GameLevelSelectFragment.Level.getLevel(level).getLabel(), stage + 1);

        String recordString = pref.getString(value, null);
        int[] record;
        Gson gson = new Gson();
        record = gson.fromJson(recordString,int[].class);

        this.showRecordNumber(record, true);
    }

    private void showRecordNumber(int[] record, boolean selfRecord){

        final Scale noRecordLabelScaleToChart = new Scale(0.5844, 0.0, 0.2009, 0.0471);
        final Scale secondLabelScaleToChart = new Scale(0.7329, 0.0, 0.0524, 0.0471);
        final Scale minuteLabelScaleToChart = new Scale(0.5859, 0.0, 0.0524, 0.0471);
        final Scale recordNumberSizeScaleToChart = new Scale(0.0, 0.0, 0.0321, 0.0416);
        final Scale recordNumber1SizeScaleToChart = new Scale(0.0, 0.0, 0.0208, 0.0416);
        final double minuteNumberXScaleToChart = 0.5498;
        final double[] secondNumberXScaleToChart = {0.6865, 0.6497};
        final double additionOfXScaleInRecordNumber1 = 0.00565;
        final double[] stringLabelYScaleToSelfChart = {0.1338, 0.1999, 0.266, 0.3322, 0.3983};
        final double[] stringLabelYScaleToRankChart = {0.6094, 0.6756, 0.7417, 0.8079, 0.874};
        final double[] numberYScaleToSelfChart = {0.138, 0.2056, 0.2721, 0.3386, 0.4042};
        final double[] numberYScaleToRankChart = {0.6133, 0.6794, 0.7456, 0.8117, 0.8779};
        final double[] stringLabelYScaleToChart = selfRecord ? stringLabelYScaleToSelfChart : stringLabelYScaleToRankChart;
        final double[] numberYScaleToChart = selfRecord ? numberYScaleToSelfChart : numberYScaleToRankChart;
        for(int i = 0; i < 5; i++){
            if(record == null){
                double noRecordWidth = recordChartFrame.getLayoutParams().width * noRecordLabelScaleToChart.widthScale;
                double noRecordHeight = recordChartFrame.getLayoutParams().height * noRecordLabelScaleToChart.heightScale;
                FrameLayout.LayoutParams lpNoRecord = new FrameLayout.LayoutParams((int)noRecordWidth, (int)noRecordHeight);
                double noRecordLeftMargin = recordChartFrame.getLayoutParams().width * noRecordLabelScaleToChart.xScale;
                double noRecordTopMargin = recordChartFrame.getLayoutParams().height * stringLabelYScaleToChart[i];
                lpNoRecord.leftMargin = (int)noRecordLeftMargin;
                lpNoRecord.topMargin = (int)noRecordTopMargin;
                ImageView noRecordView = new ImageView(getContext());
                noRecordView.setImageResource(R.drawable.no_record_label);
                noRecordView.setScaleType(ImageView.ScaleType.FIT_XY);
                noRecordView.setPadding(0, 0, 0, 0);
                noRecordView.setLayoutParams(lpNoRecord);
                this.recordChartFrame.addView(noRecordView);
            }else{
                if(i < record.length) {
                    int minute = record[i] / 60;
                    int second = record[i]  % 60;
                    if(minute != 0){
                        FrameLayout.LayoutParams lpMinuteView;
                        ImageView minuteView = new ImageView(getContext());
                        minuteView.setPadding(0,0,0,0);
                        minuteView.setScaleType(ImageView.ScaleType.FIT_XY);
                        if(minute == 1){
                            minuteView.setImageResource(R.drawable.record_number1);
                            double minuteWidth = recordChartFrame.getLayoutParams().width * recordNumber1SizeScaleToChart.widthScale;
                            double minuteHeight = recordChartFrame.getLayoutParams().height * recordNumber1SizeScaleToChart.heightScale;
                            lpMinuteView = new FrameLayout.LayoutParams((int)minuteWidth, (int)minuteHeight);
                            double minuteLeftMargin = recordChartFrame.getLayoutParams().width * (minuteNumberXScaleToChart + additionOfXScaleInRecordNumber1);
                            lpMinuteView.leftMargin = (int)minuteLeftMargin;
                        }else{
                            double minuteWidth = recordChartFrame.getLayoutParams().width * recordNumberSizeScaleToChart.widthScale;
                            double minuteHeight = recordChartFrame.getLayoutParams().height * recordNumberSizeScaleToChart.heightScale;
                            lpMinuteView = new FrameLayout.LayoutParams((int)minuteWidth, (int)minuteHeight);
                            double minuteLeftMargin = recordChartFrame.getLayoutParams().width * minuteNumberXScaleToChart;
                            lpMinuteView.leftMargin = (int)minuteLeftMargin;
                        }
                        String string = String.format(Locale.ENGLISH, "record_number%d", minute);
                        int strId = getResources().getIdentifier(string, "drawable", getContext().getPackageName());
                        minuteView.setImageResource(strId);
                        double minuteTopMargin = recordChartFrame.getLayoutParams().height * numberYScaleToChart[i];
                        lpMinuteView.topMargin = (int)minuteTopMargin;
                        minuteView.setLayoutParams(lpMinuteView);
                        this.recordChartFrame.addView(minuteView);

                        ImageView minuteLabel = new ImageView(getContext());
                        minuteLabel.setImageResource(R.drawable.minute_label);
                        minuteLabel.setPadding(0,0,0,0);
                        minuteLabel.setScaleType(ImageView.ScaleType.FIT_XY);
                        double minuteLabelWidth = recordChartFrame.getLayoutParams().width * minuteLabelScaleToChart.widthScale;
                        double minuteLabelHeight = recordChartFrame.getLayoutParams().height * minuteLabelScaleToChart.heightScale;
                        FrameLayout.LayoutParams lpMinuteLabel = new FrameLayout.LayoutParams((int)minuteLabelWidth, (int)minuteLabelHeight);
                        double minuteLabelLeftMargin = recordChartFrame.getLayoutParams().width * minuteLabelScaleToChart.xScale;
                        double minuteLabelTopMargin = recordChartFrame.getLayoutParams().height * stringLabelYScaleToChart[i];
                        lpMinuteLabel.leftMargin = (int)minuteLabelLeftMargin;
                        lpMinuteLabel.topMargin = (int)minuteLabelTopMargin;
                        minuteLabel.setLayoutParams(lpMinuteLabel);
                        this.recordChartFrame.addView(minuteLabel);
                    }
                    int tempSecond = second;
                    for(int k = 0; k < 2; k++){
                        ImageView secondNumberView = new ImageView(getContext());
                        secondNumberView.setScaleType(ImageView.ScaleType.FIT_XY);
                        secondNumberView.setPadding(0,0,0,0);
                        FrameLayout.LayoutParams lpNumber;
                        if(i == 1 && second < 10){
                            secondNumberView.setImageResource(R.drawable.record_number0);
                        }else{
                            String string = String.format(Locale.ENGLISH, "record_number%d", tempSecond % 10);
                            int strId = getResources().getIdentifier(string, "drawable", getContext().getPackageName());
                            secondNumberView.setImageResource(strId);
                        }
                        if(tempSecond % 10 == 1){
                            double numberWidth = recordChartFrame.getLayoutParams().width * recordNumber1SizeScaleToChart.widthScale;
                            double numberHeight = recordChartFrame.getLayoutParams().height * recordNumber1SizeScaleToChart.heightScale;
                            lpNumber = new FrameLayout.LayoutParams((int)numberWidth, (int)numberHeight);
                            double numberLeftMargin = recordChartFrame.getLayoutParams().width * (secondNumberXScaleToChart[k] + additionOfXScaleInRecordNumber1);
                            lpNumber.leftMargin = (int)numberLeftMargin;
                            double numberTopMargin = recordChartFrame.getLayoutParams().height * numberYScaleToChart[i];
                            lpNumber.topMargin = (int)numberTopMargin;
                        }else{
                            double numberWidth = recordChartFrame.getLayoutParams().width * recordNumberSizeScaleToChart.widthScale;
                            double numberHeight = recordChartFrame.getLayoutParams().height * recordNumberSizeScaleToChart.heightScale;
                            lpNumber = new FrameLayout.LayoutParams((int)numberWidth, (int)numberHeight);
                            double numberLeftMargin = recordChartFrame.getLayoutParams().width * secondNumberXScaleToChart[k];
                            lpNumber.leftMargin = (int)numberLeftMargin;
                            double numberTopMargin = recordChartFrame.getLayoutParams().height * numberYScaleToChart[i];
                            lpNumber.topMargin = (int)numberTopMargin;
                        }
                        secondNumberView.setLayoutParams(lpNumber);
                        recordChartFrame.addView(secondNumberView);
                        tempSecond /= 10;
                    }
                    ImageView secondLabel = new ImageView(getContext());
                    secondLabel.setScaleType(ImageView.ScaleType.FIT_XY);
                    secondLabel.setPadding(0,0,0,0);
                    secondLabel.setImageResource(R.drawable.second_label);
                    double secondLabelWidth = recordChartFrame.getLayoutParams().width * secondLabelScaleToChart.widthScale;
                    double secondLabelHeight = recordChartFrame.getLayoutParams().height * secondLabelScaleToChart.heightScale;
                    FrameLayout.LayoutParams lpSecondLabel = new FrameLayout.LayoutParams((int)secondLabelWidth, (int)secondLabelHeight);
                    double secondLabelLeftMargin = recordChartFrame.getLayoutParams().width * secondLabelScaleToChart.xScale;
                    double secondLabelTopMargin = recordChartFrame.getLayoutParams().height * stringLabelYScaleToChart[i];
                    lpSecondLabel.leftMargin = (int)secondLabelLeftMargin;
                    lpSecondLabel.topMargin = (int)secondLabelTopMargin;
                    secondLabel.setLayoutParams(lpSecondLabel);
                    this.recordChartFrame.addView(secondLabel);

                }else{
                    double noRecordWidth = recordChartFrame.getLayoutParams().width * noRecordLabelScaleToChart.widthScale;
                    double noRecordHeight = recordChartFrame.getLayoutParams().height * noRecordLabelScaleToChart.heightScale;
                    FrameLayout.LayoutParams lpNoRecord = new FrameLayout.LayoutParams((int)noRecordWidth, (int)noRecordHeight);
                    double noRecordLeftMargin = recordChartFrame.getLayoutParams().width * noRecordLabelScaleToChart.xScale;
                    double noRecordTopMargin = recordChartFrame.getLayoutParams().height * stringLabelYScaleToChart[i];
                    lpNoRecord.leftMargin = (int)noRecordLeftMargin;
                    lpNoRecord.topMargin = (int)noRecordTopMargin;
                    ImageView noRecordView = new ImageView(getContext());
                    noRecordView.setImageResource(R.drawable.no_record_label);
                    noRecordView.setLayoutParams(lpNoRecord);
                    this.recordChartFrame.addView(noRecordView);
                }
            }
        }

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
        void goToGameLevelFragment();
        void goToGameFragment(int level, int stage);
        void showInterstitial();
    }
}
