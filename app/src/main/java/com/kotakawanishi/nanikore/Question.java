package com.kotakawanishi.nanikore;

import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by kota on 2017/05/19.
 */

class Question {

    interface QuestionCallbacks{
        void showCorrectCircle(int questionNumber);
        void showIncorrectCircle(int questionNumber);
        void ringWrongSound();
    }



    InfoOfQuestion information;
    List<BlankSquareView> squareViews = new ArrayList<>();
    List<CharacterView> characterViews = new ArrayList<>();
    ImageButton answerBtn;
    ImageButton resetBtn;
    boolean clear = false;
    private QuestionCallbacks callback;

    Question(int numberOfQuestion, Map<String, String> map, final QuestionCallbacks callback, ImageButton answerBtn, ImageButton resetBtn){
        super();
        information = new InfoOfQuestion(numberOfQuestion,map);
        this.answerBtn = answerBtn;
        this.resetBtn = resetBtn;
        answerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAnswerCorrect();
            }
        });
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });

        this.callback = callback;
    }

    ImageButton getAnswerBtn(){
        return this.answerBtn;
    }

    ImageButton getResetBtn(){
        return this.resetBtn;
    }

    private void isAnswerCorrect(){

        for(BlankSquareView view : squareViews){
            if(view.characterView == null){
                this.callback.ringWrongSound();
                this.callback.showIncorrectCircle(this.information.numberOfQuestion);
                return;
            }
        }


        String answerString = "";

        for(BlankSquareView view : squareViews){
           answerString = answerString.concat(String.valueOf(view.characterView.character));
        }

        List<String> allAnswers = information.findAllPossibleAnswers(answerString);
        Set<String> correctAnswers = this.information.wordAndAnswer.keySet();
        int i = 0;
        int j = 0;
        for(String answer : allAnswers){
            i++;
            for(final String correctAnswer : correctAnswers){
                if(correctAnswer.equals(answer)) {
                    clear = true;
                    changeCharacterViews(correctAnswer);
                    callback.showCorrectCircle(information.numberOfQuestion);
                    disableTouchCharacterViews();
                    answerBtn.setEnabled(false);
                    resetBtn.setEnabled(false);
                    return;
                }else{
                    j++;
                    if(i == allAnswers.size() && j == correctAnswers.size()){
                        this.callback.ringWrongSound();
                        this.callback.showIncorrectCircle(this.information.numberOfQuestion);
                    }
                }
            }
            j = 0;
        }
    }





    private void changeCharacterViews(String correctAnswer){
        char[] changingCharacterArray = {'っ','ゃ','ゅ','ょ'};
        char[] characters = correctAnswer.toCharArray();
        for(int i = 0; i < characters.length; i++){
            char character = characters[i];
            for(char changingCharacter : changingCharacterArray){
                if(character == changingCharacter){
                    changeCharacterView(this.squareViews.get(i).characterView);
                }
            }
        }
    }

    private void changeCharacterView(final CharacterView characterView){

        if(characterView.character == 'っ' || characterView.character == 'つ'){
            String string = "character_small_tsu";
            int strId = characterView.getResources().getIdentifier(string, "drawable", characterView.getContext().getPackageName());
            characterView.setImageResource(strId);
        }else if(characterView.character == 'ゃ' || characterView.character == 'や'){
            String string = "character_small_ya";
            int strId = characterView.getResources().getIdentifier(string, "drawable", characterView.getContext().getPackageName());
            characterView.setImageResource(strId);
        }else if(characterView.character == 'ゅ' || characterView.character == 'ゆ'){
            String string = "character_small_yu";
            int strId = characterView.getResources().getIdentifier(string, "drawable", characterView.getContext().getPackageName());
            characterView.setImageResource(strId);
        }else if(characterView.character == 'ょ' || characterView.character == 'よ'){
            String string = "character_small_yo";
            int strId = characterView.getResources().getIdentifier(string, "drawable", characterView.getContext().getPackageName());
            characterView.setImageResource(strId);
        }
    }

    private void disableTouchCharacterViews(){
        for(CharacterView view : this.characterViews){
            view.setEnabled(false);
        }
    }



    private void reset(){
        for(BlankSquareView view : squareViews){
            if(view.characterView != null){
                view.characterView.backDefaultPosition();
                view.characterView = null;
            }
        }

    }
}
