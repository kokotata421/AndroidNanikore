package com.kotakawanishi.nanikore;

import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;

/**
 * Created by kota on 2017/12/03.
 */

public class StageButton extends AppCompatImageButton {
    private Stage stage;
    public StageButton(Context context){
        super(context);
    }

    public StageButton(Context context, GameLevelSelectFragment.Level level, int stage, boolean clear){
        super(context);
        this.stage = new Stage(level, stage, clear);
    }

    Stage  getStage(){
        return this.stage;
    }
}
