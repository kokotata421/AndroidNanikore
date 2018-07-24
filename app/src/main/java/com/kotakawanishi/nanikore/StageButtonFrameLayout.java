package com.kotakawanishi.nanikore;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.Locale;




/**
 * Created by kota on 2017/05/14.
 */



public class StageButtonFrameLayout extends FrameLayout {
    public interface StageButtonCallbacks {
        void selectStage(View view);

    }

    private StageButton stageBtn;
    private StageButtonCallbacks callback;
    final private Scale successfulCandidatesNumberViewScale = new Scale(0.0, 0.7882, 0.0729, 0.115);
    final private Scale successfulCandidatesNumber1ViewScale = new Scale(0.0, 0.7882, 0.0486, 0.115);
    final private double additionOfSuccessfulCandidatesNumber1ViewXScale = 0.00715;
    final private double[] successfulCandidatesNumberViewXScale6 = {0.6621, 0.581, 0.5027, 0.4243, 0.3432, 0.2648};
    final private double[] successfulCandidatesNumberViewXScale5 = {0.6378, 0.5513, 0.4635, 0.3756, 0.2945};
    final private double[] successfulCandidatesNumberViewXScale4 = {0.6027, 0.5081, 0.4189, 0.3243};
    final private double[] successfulCandidatesNumberViewXScale3 = {0.5621, 0.4621, 0.3621};
    final private double[] successfulCandidatesNumberViewXScale2 = {0.5135, 0.4135};



    public StageButtonFrameLayout(Context context){
        super(context);
    }


    public StageButtonFrameLayout(Context context, GameLevelSelectFragment.Level level, int stage, boolean clear){
        super(context);
        stageBtn = new StageButton(this.getContext(), level, stage, clear);
        this.addImage(level);
        this.setEnabled(true);

    }

    private void addImage(GameLevelSelectFragment.Level level){
        if(level == GameLevelSelectFragment.Level.EASY) {
            stageBtn.setImageResource(R.drawable.stage_btn_easy);
        } else if(level == GameLevelSelectFragment.Level.NORMAL){
            stageBtn.setImageResource(R.drawable.stage_btn_normal);
        }else{
            stageBtn.setImageResource(R.drawable.stage_btn_hard);
        }

        stageBtn.setScaleType(ImageView.ScaleType.FIT_XY);
        stageBtn.setPadding(0, 0, 0, 0);
        stageBtn.setEnabled(true);
        stageBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                callback.selectStage(v);
            }
        });
        stageBtn.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.addView(stageBtn);
    }

    protected void addWellDoneStamp(){
        setClipChildren(false);
        final Scale wellDoneStampScale = new Scale(0.6424, -0.1891, 0.4459, 0.0);
        ImageView wellDoneStampView = new ImageView(this.getContext());
        wellDoneStampView.setImageResource(R.drawable.well_done_stamp);
        wellDoneStampView.setScaleType(ImageView.ScaleType.FIT_XY);
        wellDoneStampView.setPadding(0, 0, 0, 0);
        final double stampSize = getLayoutParams().width * wellDoneStampScale.widthScale;
        LayoutParams lpStamp = new LayoutParams((int)stampSize, (int)stampSize);
        double lpLeftMargin = getLayoutParams().width * wellDoneStampScale.xScale;
        double lpTopMargin = getLayoutParams().height * wellDoneStampScale.yScale;
        lpStamp.leftMargin = (int)lpLeftMargin;
        lpStamp.topMargin = (int)lpTopMargin;
        wellDoneStampView.setLayoutParams(lpStamp);
        this.addView(wellDoneStampView);

    }


    protected  void addNumberView(){
        final Scale stageStringScale = new Scale(0.0, 0.1054, 0.6324, 0.1918);
        final Scale stageNumberViewScale = new Scale(0.0, 0.3351, 0.1486, 0.2351);
        final Scale stageNumber1ViewScale = new Scale(0.0, 0.3351, 0.0965, 0.2351);
        final double[] stageNumberViewXScale = {0.5189, 0.3297};
        final double additionOfStageNumber1ViewXScale = 0.01905;


        double stageStringWidth = getLayoutParams().width * stageStringScale.widthScale;
        double stageStringHeight = getLayoutParams().height * stageStringScale.heightScale;
        LayoutParams lpStageString = new LayoutParams((int)stageStringWidth, (int)stageStringHeight);
        double stageStringLeftMargin = getLayoutParams().width / 2.0 - stageStringWidth / 2.0;
        double stageStringTopMargin = getLayoutParams().height * stageStringScale.yScale;
        lpStageString.leftMargin = (int)stageStringLeftMargin;
        lpStageString.topMargin = (int)stageStringTopMargin;
        ImageView stageString = new ImageView(this.getContext());
        stageString.setLayoutParams(lpStageString);
        stageString.setImageResource(R.drawable.stage_string_label);
        this.addView(stageString);


        int tempNumber = this.stageBtn.getStage().getNumberOfStage() + 1;
        for(int i = 0; i < 2; i++){
            ImageView stageNumberView = new ImageView(this.getContext());
            stageNumberView.setScaleType(ImageView.ScaleType.FIT_XY);
            stageNumberView.setPadding(0, 0, 0, 0);
            LayoutParams lpNumber;

            if(i == 1 && this.stageBtn.getStage().getNumberOfStage() + 1 < 10){
                stageNumberView.setImageResource(R.drawable.stage_number0);
            }else{
                String string = String.format(Locale.ENGLISH, "stage_number%d", tempNumber % 10);
                int strId = getResources().getIdentifier(string, "drawable", this.getContext().getPackageName());
                stageNumberView.setImageResource(strId);
            }
            if(tempNumber % 10 == 1){
                double numberWidth = getLayoutParams().width * stageNumber1ViewScale.widthScale;
                double numberHeight = getLayoutParams().height * stageNumber1ViewScale.heightScale;
                lpNumber = new LayoutParams((int)numberWidth, (int)numberHeight);
                double numberLeftMargin = getLayoutParams().width * (stageNumberViewXScale[i] + additionOfStageNumber1ViewXScale);
                lpNumber.leftMargin = (int)numberLeftMargin;
            }else {
                double numberWidth = getLayoutParams().width * stageNumberViewScale.widthScale;
                double numberHeight = getLayoutParams().height * stageNumberViewScale.heightScale;
                lpNumber = new LayoutParams((int)numberWidth, (int)numberHeight);
                double numberLeftMargin = getLayoutParams().width * stageNumberViewXScale[i];
                lpNumber.leftMargin = (int)numberLeftMargin;
            }
            double numberTopMargin = getLayoutParams().height * stageNumberViewScale.yScale;
            lpNumber.topMargin = (int)numberTopMargin;
            stageNumberView.setLayoutParams(lpNumber);
            this.addView(stageNumberView);
            tempNumber /= 10;
        }
    }

    protected  void addLockView(){
        final Scale stageLockScale = new Scale(0.0, 0.1183, 0.4772, 0.4508);
        ImageView stageLockView = new ImageView(this.getContext());
        stageLockView.setScaleType(ImageView.ScaleType.FIT_XY);
        stageLockView.setPadding(0, 0, 0, 0);
        stageLockView.setImageResource(R.drawable.stage_lock);
        double lockWidth = getLayoutParams().width * stageLockScale.widthScale;
        double lockHeight = getLayoutParams().height * stageLockScale.heightScale;
        double lockLeftMargin = getLayoutParams().width / 2.0 - lockWidth / 2.0;
        double lockTopMargin = getLayoutParams().height * stageLockScale.yScale;
        LayoutParams lpLock = new LayoutParams((int)lockWidth, (int)lockHeight);
        lpLock.leftMargin = (int)lockLeftMargin;
        lpLock.topMargin = (int)lockTopMargin;
        stageLockView.setLayoutParams(lpLock);
        this.addView(stageLockView);
        this.setEnabled(false);
    }


    protected void addSuccessfulCandidates(Integer numberI){
        int number = numberI;
        if(number >= 100000 && number <= 999999) {
            addSuccessCandidatesNumberOf6Digits(number);
        }else if(number >= 10000 && number <= 99999) {
            addSuccessCandidatesNumberOf5Digits(number);
        }else if(number >= 1000 && number <= 9999){
            addSuccessCandidatesNumberOf4Digits(number);
        }else if(number >= 100 && number <= 999){
            addSuccessCandidatesNumberOf3Digits(number);
        }else if(number >= 10 && number <= 99){
            addSuccessCandidatesNumberOf2Digits(number);
        }else{
            addSuccessCandidatesNumberOf1Digit(number);
        }
        
    }

    private void addSuccessCandidatesNumberOf6Digits(Integer number){
        int tempNumber = number;
        for(int i = 0; i < 6; i++){
            ImageView passingNumberView = new ImageView(this.getContext());
            passingNumberView.setScaleType(ImageView.ScaleType.FIT_XY);
            passingNumberView.setPadding(0, 0, 0, 0);
            LayoutParams lpNumber;


            String string = String.format(Locale.ENGLISH, "stage_number%d", tempNumber % 10);
            int strId = getResources().getIdentifier(string, "drawable", this.getContext().getPackageName());
            passingNumberView.setImageResource(strId);

            if(tempNumber % 10 == 1){
                double numberWidth = getLayoutParams().width * successfulCandidatesNumber1ViewScale.widthScale;
                double numberHeight = getLayoutParams().height * successfulCandidatesNumber1ViewScale.heightScale;
                lpNumber = new LayoutParams((int)numberWidth, (int)numberHeight);
                double numberLeftMargin = getLayoutParams().width * (successfulCandidatesNumberViewXScale6[i] + additionOfSuccessfulCandidatesNumber1ViewXScale);
                lpNumber.leftMargin = (int)numberLeftMargin;
            }else {
                double numberWidth = getLayoutParams().width * successfulCandidatesNumberViewScale.widthScale;
                double numberHeight = getLayoutParams().height * successfulCandidatesNumberViewScale.heightScale;
                lpNumber = new LayoutParams((int)numberWidth, (int)numberHeight);
                double numberLeftMargin = getLayoutParams().width * successfulCandidatesNumberViewXScale6[i];
                lpNumber.leftMargin = (int)numberLeftMargin;
            }
            double numberTopMargin = getLayoutParams().height * successfulCandidatesNumberViewScale.yScale;
            lpNumber.topMargin = (int)numberTopMargin;
            passingNumberView.setLayoutParams(lpNumber);
            this.addView(passingNumberView);
            tempNumber /= 10;
        }
    }

    private void addSuccessCandidatesNumberOf5Digits(Integer number){
        int tempNumber = number;
        for(int i = 0; i < 5; i++){
            ImageView passingNumberView = new ImageView(this.getContext());
            passingNumberView.setScaleType(ImageView.ScaleType.FIT_XY);
            passingNumberView.setPadding(0, 0, 0, 0);
            LayoutParams lpNumber;


            String string = String.format(Locale.ENGLISH, "stage_number%d", tempNumber % 10);
            int strId = getResources().getIdentifier(string, "drawable", this.getContext().getPackageName());
            passingNumberView.setImageResource(strId);

            if(tempNumber % 10 == 1){
                double numberWidth = getLayoutParams().width * successfulCandidatesNumber1ViewScale.widthScale;
                double numberHeight = getLayoutParams().height * successfulCandidatesNumber1ViewScale.heightScale;
                lpNumber = new LayoutParams((int)numberWidth, (int)numberHeight);
                double numberLeftMargin = getLayoutParams().width * (successfulCandidatesNumberViewXScale5[i] + additionOfSuccessfulCandidatesNumber1ViewXScale);
                lpNumber.leftMargin = (int)numberLeftMargin;
            }else {
                double numberWidth = getLayoutParams().width * successfulCandidatesNumberViewScale.widthScale;
                double numberHeight = getLayoutParams().height * successfulCandidatesNumberViewScale.heightScale;
                lpNumber = new LayoutParams((int)numberWidth, (int)numberHeight);
                double numberLeftMargin = getLayoutParams().width * successfulCandidatesNumberViewXScale5[i];
                lpNumber.leftMargin = (int)numberLeftMargin;
            }
            double numberTopMargin = getLayoutParams().height * successfulCandidatesNumberViewScale.yScale;
            lpNumber.topMargin = (int)numberTopMargin;
            passingNumberView.setLayoutParams(lpNumber);
            this.addView(passingNumberView);
            tempNumber /= 10;
        }

    }

    private void addSuccessCandidatesNumberOf4Digits(Integer number){
        int tempNumber = number;
        for(int i = 0; i < 4; i++){
            ImageView passingNumberView = new ImageView(this.getContext());
            passingNumberView.setScaleType(ImageView.ScaleType.FIT_XY);
            passingNumberView.setPadding(0, 0, 0, 0);
            LayoutParams lpNumber;


            String string = String.format(Locale.ENGLISH, "stage_number%d", tempNumber % 10);
            int strId = getResources().getIdentifier(string, "drawable", this.getContext().getPackageName());
            passingNumberView.setImageResource(strId);

            if(tempNumber % 10 == 1){
                double numberWidth = getLayoutParams().width * successfulCandidatesNumber1ViewScale.widthScale;
                double numberHeight = getLayoutParams().height * successfulCandidatesNumber1ViewScale.heightScale;
                lpNumber = new LayoutParams((int)numberWidth, (int)numberHeight);
                double numberLeftMargin = getLayoutParams().width * (successfulCandidatesNumberViewXScale4[i] + additionOfSuccessfulCandidatesNumber1ViewXScale);
                lpNumber.leftMargin = (int)numberLeftMargin;
            }else {
                double numberWidth = getLayoutParams().width * successfulCandidatesNumberViewScale.widthScale;
                double numberHeight = getLayoutParams().height * successfulCandidatesNumberViewScale.heightScale;
                lpNumber = new LayoutParams((int)numberWidth, (int)numberHeight);
                double numberLeftMargin = getLayoutParams().width * successfulCandidatesNumberViewXScale4[i];
                lpNumber.leftMargin = (int)numberLeftMargin;
            }
            double numberTopMargin = getLayoutParams().height * successfulCandidatesNumberViewScale.yScale;
            lpNumber.topMargin = (int)numberTopMargin;
            passingNumberView.setLayoutParams(lpNumber);
            this.addView(passingNumberView);
            tempNumber /= 10;
        }
    }

    private void addSuccessCandidatesNumberOf3Digits(Integer number){
        int tempNumber = number;
        for(int i = 0; i < 3; i++){
            ImageView passingNumberView = new ImageView(this.getContext());
            passingNumberView.setScaleType(ImageView.ScaleType.FIT_XY);
            passingNumberView.setPadding(0, 0, 0, 0);
            LayoutParams lpNumber;


            String string = String.format(Locale.ENGLISH, "stage_number%d", tempNumber % 10);
            int strId = getResources().getIdentifier(string, "drawable", this.getContext().getPackageName());
            passingNumberView.setImageResource(strId);

            if(tempNumber % 10 == 1){
                double numberWidth = getLayoutParams().width * successfulCandidatesNumber1ViewScale.widthScale;
                double numberHeight = getLayoutParams().height * successfulCandidatesNumber1ViewScale.heightScale;
                lpNumber = new LayoutParams((int)numberWidth, (int)numberHeight);
                double numberLeftMargin = getLayoutParams().width * (successfulCandidatesNumberViewXScale3[i] + additionOfSuccessfulCandidatesNumber1ViewXScale);
                lpNumber.leftMargin = (int)numberLeftMargin;
            }else {
                double numberWidth = getLayoutParams().width * successfulCandidatesNumberViewScale.widthScale;
                double numberHeight = getLayoutParams().height * successfulCandidatesNumberViewScale.heightScale;
                lpNumber = new LayoutParams((int)numberWidth, (int)numberHeight);
                double numberLeftMargin = getLayoutParams().width * successfulCandidatesNumberViewXScale3[i];
                lpNumber.leftMargin = (int)numberLeftMargin;
            }
            double numberTopMargin = getLayoutParams().height * successfulCandidatesNumberViewScale.yScale;
            lpNumber.topMargin = (int)numberTopMargin;
            passingNumberView.setLayoutParams(lpNumber);
            this.addView(passingNumberView);
            tempNumber /= 10;
        }
    }

    private void addSuccessCandidatesNumberOf2Digits(Integer number){
        int tempNumber = number;
        for(int i = 0; i < 2; i++){
            ImageView passingNumberView = new ImageView(this.getContext());
            passingNumberView.setScaleType(ImageView.ScaleType.FIT_XY);
            passingNumberView.setPadding(0, 0, 0, 0);
            LayoutParams lpNumber;


            String string = String.format(Locale.ENGLISH, "stage_number%d", tempNumber % 10);
            int strId = getResources().getIdentifier(string, "drawable", this.getContext().getPackageName());
            passingNumberView.setImageResource(strId);

            if(tempNumber % 10 == 1){
                double numberWidth = getLayoutParams().width * successfulCandidatesNumber1ViewScale.widthScale;
                double numberHeight = getLayoutParams().height * successfulCandidatesNumber1ViewScale.heightScale;
                lpNumber = new LayoutParams((int)numberWidth, (int)numberHeight);
                double numberLeftMargin = getLayoutParams().width * (successfulCandidatesNumberViewXScale2[i] + additionOfSuccessfulCandidatesNumber1ViewXScale);
                lpNumber.leftMargin = (int)numberLeftMargin;
            }else {
                double numberWidth = getLayoutParams().width * successfulCandidatesNumberViewScale.widthScale;
                double numberHeight = getLayoutParams().height * successfulCandidatesNumberViewScale.heightScale;
                lpNumber = new LayoutParams((int)numberWidth, (int)numberHeight);
                double numberLeftMargin = getLayoutParams().width * successfulCandidatesNumberViewXScale2[i];
                lpNumber.leftMargin = (int)numberLeftMargin;
            }
            double numberTopMargin = getLayoutParams().height * successfulCandidatesNumberViewScale.yScale;
            lpNumber.topMargin = (int)numberTopMargin;
            passingNumberView.setLayoutParams(lpNumber);
            this.addView(passingNumberView);
            tempNumber /= 10;
        }

    }

    private void addSuccessCandidatesNumberOf1Digit(Integer number){
        ImageView passingNumberView = new ImageView(this.getContext());
        passingNumberView.setScaleType(ImageView.ScaleType.FIT_XY);
        passingNumberView.setPadding(0, 0, 0, 0);
        LayoutParams lpNumber;


        String string = String.format(Locale.ENGLISH, "stage_number%d", number);
        int strId = getResources().getIdentifier(string, "drawable", this.getContext().getPackageName());
        passingNumberView.setImageResource(strId);

        if(number == 1){
            double numberWidth = getLayoutParams().width * successfulCandidatesNumber1ViewScale.widthScale;
            double numberHeight = getLayoutParams().height * successfulCandidatesNumber1ViewScale.heightScale;
            lpNumber = new LayoutParams((int)numberWidth, (int)numberHeight);
            double numberLeftMargin = getLayoutParams().width / 2.0 - numberWidth / 2.0;
            lpNumber.leftMargin = (int)numberLeftMargin;
        }else {
            double numberWidth = getLayoutParams().width * successfulCandidatesNumberViewScale.widthScale;
            double numberHeight = getLayoutParams().height * successfulCandidatesNumberViewScale.heightScale;
            lpNumber = new LayoutParams((int)numberWidth, (int)numberHeight);
            double numberLeftMargin = getLayoutParams().width / 2.0 - numberWidth / 2.0;
            lpNumber.leftMargin = (int)numberLeftMargin;
        }
        double numberTopMargin = getLayoutParams().height * successfulCandidatesNumberViewScale.yScale;
        lpNumber.topMargin = (int)numberTopMargin;
        passingNumberView.setLayoutParams(lpNumber);
        this.addView(passingNumberView);

    }

    StageButton getStageBtn(){
        return this.stageBtn;
    }
    protected void setCallBack(StageButtonCallbacks callback){
        this.callback = callback;
    }



}
