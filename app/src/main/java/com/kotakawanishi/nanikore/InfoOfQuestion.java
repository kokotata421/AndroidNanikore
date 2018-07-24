package com.kotakawanishi.nanikore;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by kota on 2017/05/19.
 */

class InfoOfQuestion {
    int numberOfQuestion;
    int difficultyOfQuestion;
    Map<String, String> wordAndAnswer;

    InfoOfQuestion(int numberOfQuestion,Map<String, String> wordAndAnswer){
        this.numberOfQuestion = numberOfQuestion;
        this.wordAndAnswer = wordAndAnswer;
        if(wordAndAnswer.keySet().iterator().hasNext()) {
            this.difficultyOfQuestion = wordAndAnswer.keySet().iterator().next().length();
        }
    }

    String  shuffleWord(){
        String word = wordAndAnswer.keySet().iterator().next();
        char[] characters = word.toCharArray();

        while(true){
            String shuffledWord = "";
            SecureRandom r = new SecureRandom();
            int number;
            List<Integer> selectedArray = new ArrayList<>();
            for(int i = 0; i < difficultyOfQuestion; i++){
                while(true){
                    number = r.nextInt(difficultyOfQuestion);
                    if(selectedArray.contains(number)){
                        continue;
                    }
                    selectedArray.add(number);
                    shuffledWord = shuffledWord.concat(String.valueOf(characters[number]));
                    break;
                }
            }
            if(isWordChanged(shuffledWord)){
                return shuffledWord;
            }
        }
    }

    private boolean isWordChanged(String shuffledWord){
        ArrayList<String> allPossibleWords = findAllPossibleAnswers(shuffledWord);
        for(String originalKey : wordAndAnswer.keySet()){
            for(String word : allPossibleWords){
                if(originalKey.equals(word))
                    return false;
            }
        }
        return true;
    }

    ArrayList<String> findAllPossibleAnswers(String answer){
        ArrayList<String> stringList = new ArrayList<>();
        ArrayList<Integer> numberList = new ArrayList<>();


        this.includeChangeableCharacters(answer, stringList, numberList);
        if(numberList.isEmpty()){
            ArrayList<String> allAnswer = new ArrayList<>();
            allAnswer.add(answer);
            return allAnswer;
        }

        return new ArrayList<>(new HashSet<>(this.fetchAnswerArray(answer, stringList, numberList, numberList.size())));
    }

    private void includeChangeableCharacters(String answer,List<String> stringArray,List<Integer> numberArray){
        String changingCharacterArray[] = {"つ","っ","や","ゃ","ゆ","ゅ","よ","ょ"};
        for(String changingCharacter : changingCharacterArray){
            if(answer.contains(changingCharacter)){
                int i = 0;
                while(true){
                    if(answer.indexOf(changingCharacter, i) != -1){
                        numberArray.add(answer.indexOf(changingCharacter, i));
                        stringArray.add(changingCharacter);
                        i = answer.indexOf(changingCharacter, i) + 1;
                    }else{
                        break;
                    }
                }
            }
        }

    }

    private ArrayList<String> fetchAnswerArray(String answer, ArrayList<String> stringList, ArrayList<Integer> numberList, int count){
        ArrayList<String> answerList = new ArrayList<>();
        answerList.add(answer);
        for(int i = 1; i <= count; i++){
            if(i == 1 || count - i == 1){
                for(int j = 0; j < count; j++){
                    ArrayList<Integer> selectedNumberList = new ArrayList<>();
                    selectedNumberList.add(j);
                    if(i == 1){
                        answerList.add(this.fetchNewAnswer(answer, stringList, numberList, selectedNumberList));
                    }else{
                        answerList.add(this.fetchNewAnswer(answer, stringList, numberList, this.fetchNumberListOtherThan(selectedNumberList, count)));
                    }
                }
            }else if(i == 2 || count - i == 2){
                int currentBase = 0;
                for(int j = 1; j <= count - 1; j++){
                    while(true){
                        ArrayList<Integer> selectedNumberList = new ArrayList<>();
                        selectedNumberList.add(currentBase);
                        selectedNumberList.add(currentBase + j);
                        if(i == 2){
                            answerList.add(this.fetchNewAnswer(answer, stringList, numberList, selectedNumberList));
                        }else{
                            answerList.add(this.fetchNewAnswer(answer, stringList, numberList, this.fetchNumberListOtherThan(selectedNumberList, count)));
                        }
                        currentBase += 1;
                        if(currentBase + j > count - 1){
                            break;
                        }
                    }
                    currentBase = 0;
                }
            }else if(i == 3){
                for(int currentBase = 0; currentBase <= count - 3; currentBase++){
                    for(int j = 1; j <= count - 2; j++){
                        int currentBase2 = currentBase + j;
                        if(currentBase2 >= count - 1){
                            break;
                        }
                        for(int k = 1; k <= count - 2; k++){
                            if(currentBase2 + k > count - 1){
                                break;
                            }
                            ArrayList<Integer> selectedNumberList = new ArrayList<>();
                            selectedNumberList.add(currentBase);
                            selectedNumberList.add(currentBase2);
                            selectedNumberList.add(currentBase2 + k);
                            answerList.add(this.fetchNewAnswer(answer, stringList, numberList, selectedNumberList));
                        }
                    }
                }

            }else{
                ArrayList<Integer> selectedNumberList = new ArrayList<>();
                answerList.add(this.fetchNewAnswer(answer, stringList, numberList, this.fetchNumberListOtherThan(selectedNumberList, count)));
            }
        }
        return answerList;
    }

    private ArrayList<Integer> fetchNumberListOtherThan(ArrayList<Integer> number,int count){
        ArrayList<Integer> numberArray = new ArrayList<>();
        for(int i = 0; i < count; i++){
            if(!number.contains(i)){
                numberArray.add(i);
            }
        }
        return numberArray;
    }

    private String fetchNewAnswer(String answer,ArrayList<String> stringList,ArrayList<Integer> numberList,ArrayList<Integer> selectedNumberList){
        StringBuilder newAnswer = new StringBuilder();
        newAnswer.append(answer);
        for(int i = 0; i < selectedNumberList.size(); i++){
            Character replacedCharacter = fetchReplacedCharacter(stringList.get(selectedNumberList.get(i)));
            int number = numberList.get(selectedNumberList.get(i));
            newAnswer.setCharAt(number, replacedCharacter);
        }
        return newAnswer.toString();
    }

    private Character fetchReplacedCharacter(String character){
        HashMap<String, Character> nameDic = new HashMap<String, Character>() {
            {
                put("つ", 'っ');
                put("っ", 'つ');
                put("や", 'ゃ');
                put("ゃ", 'や');
                put("ゆ", 'ゅ');
                put("ゅ", 'ゆ');
                put("よ", 'ょ');
                put("ょ",'よ');
            }
        };
        return nameDic.get(character);
    }
}
