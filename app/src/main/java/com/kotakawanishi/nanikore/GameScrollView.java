package com.kotakawanishi.nanikore;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by kota on 2017/12/08.
 */

public class GameScrollView extends ScrollView {

    private boolean scrollEnable;
    public GameScrollView(Context context){
        super(context);
        scrollEnable = true;
    }

    public GameScrollView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (this.canScroll()) {
                    if (!scrollEnable) {
                        return false;
                    } else {
                        //スクロール可能な場合は既定の処理を実施
                        break;
                    }
                } else {
                    return false;
                }

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    protected boolean canScroll() {
        View child = this.getChildAt(0);
        if (child != null) {
            int childHeight = child.getLayoutParams().height;
            return this.getLayoutParams().height < childHeight + this.getPaddingTop() + this.getPaddingBottom();
        }
        return false;
    }

    protected void setScrollEnable(boolean enable){
        scrollEnable = enable;
    }
}
