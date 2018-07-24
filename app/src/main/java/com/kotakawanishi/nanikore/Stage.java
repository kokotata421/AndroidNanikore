package com.kotakawanishi.nanikore;

/**
 * Created by kota on 2017/05/14.
 */

class Stage {
    final private int[][] numberOfQuestion = {{5,6,7,5,6,7,5,6,7,8,9,10,5,6,7,5,7,8,9,10,10,10,10,10}, {5,6,7,8,9,10,5,6,7,8,9,10,5,6,7,5,6,7,5,6,7,8,9,10}, {6,7,5,6,7,8,9,10,5,6,7,8,9,10,5,6,7,5,6,5,6,5,5,5}};
    final private int[][] time = {{50,50,50,45,40,40,50,60,70,70,80,90,40,50,55,38,55,60,70,80,75,70,65,60}, {40,50,60,65,75,80,35,45,50,56,63,70,75,90,105,65,78,91,60,72,84,96,108,120}, {84,108,55,66,77,88,99,110,50,60,70,80,90,100,100,120,140,90,108,80,96,70,60,50}};
    private GameLevelSelectFragment.Level level;
    private int numberOfStage;
    private boolean clear;

    Stage(GameLevelSelectFragment.Level level, int number, boolean clear){
        this.level = level;
        this.numberOfStage = number;
        this.clear = clear;
    }

    Stage(int level, int number, boolean clear){
        switch (level){
            case 0:this.level = GameLevelSelectFragment.Level.EASY;
                   break;
            case 1:this.level = GameLevelSelectFragment.Level.NORMAL;
                   break;
            case 2:this.level = GameLevelSelectFragment.Level.HARD;
                   break;
        }
        this.numberOfStage = number;
        this.clear = clear;
    }

    Stage(GameLevelSelectFragment.Level level, int number){
        this.level = level;
        this.numberOfStage = number;
        this.clear = false;
    }

    static Stage nextLevel(Stage stage){
        switch(stage.level.getLevelNumber()){
        case 0:
            return new Stage(GameLevelSelectFragment.Level.NORMAL, 0);
        case 1:
            return new Stage(GameLevelSelectFragment.Level.HARD, 0);
            default:
                return null;
        }
    }

    static Stage nextStage(Stage stage){
        return new Stage(stage.level, stage.getNumberOfStage() + 1);
    }


    int getNumberOfQuestion(){
        return numberOfQuestion[level.ordinal()][numberOfStage];
    }

    int getTime(){
        return time[level.ordinal()][numberOfStage];
    }

    GameLevelSelectFragment.Level getLevel(){
        return this.level;
    }

    int getNumberOfStage(){
        return this.numberOfStage;
    }

    boolean getClear(){
        return this.clear;
    }
}
