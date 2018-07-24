package com.kotakawanishi.nanikore;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatImageView;
import android.widget.FrameLayout;

/**
 * Created by kota on 2017/05/19.
 */

public class BlankSquareView extends AppCompatImageView {
    protected CharacterView characterView = null;
    private FrameLayout.LayoutParams frameLayoutParams;
    public BlankSquareView(Context context){
        super(context);
        this.setEnabled(false);
        this.setBackgroundColor(Color.WHITE);
    }

    protected void setFrameLayoutParams(FrameLayout.LayoutParams layoutParams){
        this.frameLayoutParams = layoutParams;
        this.setLayoutParams(this.frameLayoutParams);
    }

    protected FrameLayout.LayoutParams getFrameLayoutParams(){
        return this.frameLayoutParams;
    }
}

