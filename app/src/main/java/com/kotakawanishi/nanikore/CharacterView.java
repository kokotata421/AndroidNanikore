package com.kotakawanishi.nanikore;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatImageView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;


/**
 * Created by kota on 2017/05/19.
 */

public class CharacterView extends AppCompatImageView {

    private class DragViewListener implements OnTouchListener {
        private CharacterView dragView;
        private int preDx, preDy, newDx, newDy;

        DragViewListener(CharacterView dragView) {
            this.dragView = dragView;
            preDx = preDy = newDx = newDy = 0;
        }


        @Override
        public boolean onTouch(View view, MotionEvent event) {
            newDx = (int)event.getRawX();
            newDy = (int)event.getRawY();

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    gameFragment.disableScroll();
                    break;
                case MotionEvent.ACTION_MOVE:
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) dragView.getLayoutParams();

                    int dx = layoutParams.leftMargin + (newDx - preDx);
                    int dy = layoutParams.topMargin + (newDy - preDy);

                    layoutParams.leftMargin = dx;
                    layoutParams.topMargin = dy;

                    dragView.setLayoutParams(layoutParams);

                    break;
                case MotionEvent.ACTION_UP:
                    view.performClick();
                    CharacterViewCallBack callBack = dragView.gameFragment;
                    Integer result= callBack.touchPositionOnSquare(event.getRawX(), event.getRawY(), dragView.question);
                    if(result == null){
                        dragView.backPosition();
                    }else{
                        callBack.placeOnSquare(result, dragView.question, dragView);
                    }
                    gameFragment.enableScroll();
                    break;
            }

            preDx = newDx;
            preDy = newDy;

            return true;
        }
    }

    interface CharacterViewCallBack{
        Integer touchPositionOnSquare(float x, float y, Question question);
        void placeOnSquare(Integer position, Question question, CharacterView characterView);
    }

    public GameFragment gameFragment;
    protected char character;
    protected Integer currentPositionInSquare;
    protected FrameLayout.LayoutParams defaultPosition;
    protected Question question;

    public CharacterView(Context context) {
        super(context);
    }

    public CharacterView(Context context, char character, GameFragment gameFragment, Question question, FrameLayout.LayoutParams defaultLp) {
        super(context);
        this.character = character;
        this.gameFragment = gameFragment;
        this.setBackgroundColor(Color.BLUE);
        this.currentPositionInSquare = null;
        this.question = question;
        this.addImage(character);
        this.setLayoutParams(defaultLp);
        this.setDefaultPosition(defaultLp);
        this.setOnTouchListener(new DragViewListener(this));
    }


    private void addImage(char character){
        String string;

        switch (character){
            case 'あ':
                string = "character_a";
                break;
            case 'い':
                string = "character_i";
                break;
            case 'う':
                string = "character_u";
                break;
            case 'え':
                string = "character_e";
                break;
            case 'お':
                string = "character_o";
                break;
            case 'か':
                string = "character_ka";
                break;
            case 'き':
                string = "character_ki";
                break;
            case 'く':
                string = "character_ku";
                break;
            case 'け':
                string = "character_ke";
                break;
            case 'こ':
                string = "character_ko";
                break;
            case 'さ':
                string = "character_sa";
                break;
            case 'し':
                string = "character_shi";
                break;
            case 'す':
                string = "character_su";
                break;
            case 'せ':
                string = "character_se";
                break;
            case 'そ':
                string = "character_so";
                break;
            case 'た':
                string = "character_ta";
                break;
            case 'ち':
                string = "character_chi";
                break;
            case 'つ':
                string = "character_tsu";
                break;
            case 'て':
                string = "character_te";
                break;
            case 'と':
                string = "character_to";
                break;
            case 'な':
                string = "character_na";
                break;
            case 'に':
                string = "character_ni";
                break;
            case 'ぬ':
                string = "character_nu";
                break;
            case 'ね':
                string = "character_ne";
                break;
            case 'の':
                string = "character_no";
                break;
            case 'は':
                string = "character_ha";
                break;
            case 'ひ':
                string = "character_hi";
                break;
            case 'ふ':
                string = "character_hu";
                break;
            case 'へ':
                string = "character_he";
                break;
            case 'ほ':
                string = "character_ho";
                break;
            case 'ま':
                string = "character_ma";
                break;
            case 'み':
                string = "character_mi";
                break;
            case 'む':
                string = "character_mu";
                break;
            case 'め':
                string = "character_me";
                break;
            case 'も':
                string = "character_mo";
                break;
            case 'や':
                string = "character_ya";
                break;
            case 'ゆ':
                string = "character_yu";
                break;
            case 'よ':
                string = "character_yo";
                break;
            case 'ら':
                string = "character_ra";
                break;
            case 'り':
                string = "character_ri";
                break;
            case 'る':
                string = "character_ru";
                break;
            case 'れ':
                string = "character_re";
                break;
            case 'ろ':
                string = "character_ro";
                break;
            case 'わ':
                string = "character_wa";
                break;
            case 'を':
                string = "character_wo";
                break;
            case 'ん':
                string = "character_n";
                break;
            case 'が':
                string = "character_ga";
                break;
            case 'ぎ':
                string = "character_gi";
                break;
            case 'ぐ':
                string = "character_gu";
                break;
            case 'げ':
                string = "character_ge";
                break;
            case 'ご':
                string = "character_go";
                break;
            case 'ざ':
                string = "character_za";
                break;
            case 'じ':
                string = "character_zi";
                break;
            case 'ず':
                string = "character_zu";
                break;
            case 'ぜ':
                string = "character_ze";
                break;
            case 'ぞ':
                string = "character_zo";
                break;
            case 'だ':
                string = "character_da";
                break;
            case 'ぢ':
                string = "character_dji";
                break;
            case 'づ':
                string = "character_dzu";
                break;
            case 'で':
                string = "character_de";
                break;
            case 'ど':
                string = "character_do";
                break;
            case 'ば':
                string = "character_ba";
                break;
            case 'び':
                string = "character_bi";
                break;
            case 'ぶ':
                string = "character_bu";
                break;
            case 'べ':
                string = "character_be";
                break;
            case 'ぼ':
                string = "character_bo";
                break;
            case 'ぱ':
                string = "character_pa";
                break;
            case 'ぴ':
                string = "character_pi";
                break;
            case 'ぷ':
                string = "character_pu";
                break;
            case 'ぺ':
                string = "character_pe";
                break;
            case 'ぽ':
                string = "character_po";
                break;
            case 'っ':
                string = "character_tsu";
                break;
            case 'ゃ':
                string = "character_ya";
                break;
            case 'ゅ':
                string = "character_yu";
                break;
            case 'ょ':
                string = "character_yo";
                break;
            default:
                string = "character_a";
                break;

        }
        int strId = getResources().getIdentifier(string, "drawable", this.getContext().getPackageName());
        this.setImageResource(strId);
    }

    private void backPosition(){
        if(currentPositionInSquare == null){
            this.backDefaultPosition();
        }else{
            CharacterViewCallBack callBack = gameFragment;
            callBack.placeOnSquare(currentPositionInSquare,  this.question, this);
        }
    }

    void setDefaultPosition(FrameLayout.LayoutParams layoutParams){
        this.defaultPosition = new FrameLayout.LayoutParams(layoutParams);
    }

    FrameLayout.LayoutParams getDefaultPosition(){
        return this.defaultPosition;
    }


    protected void backDefaultPosition(){
        if(this.currentPositionInSquare != null)
            this.gameFragment.releaseCharacterView(this.currentPositionInSquare, this.question);
        currentPositionInSquare = null;
        this.setLayoutParams(new FrameLayout.LayoutParams(defaultPosition));
    }



    @Override
    public boolean performClick() {
        // Calls the super implementation, which generates an AccessibilityEvent
        // and calls the onClick() listener on the view, if any
        super.performClick();

        // Handle the action for the custom click here

        return true;
    }




}
