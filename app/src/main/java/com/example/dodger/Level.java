package com.example.dodger;

public class Level extends Thread {
    final private long levelTime;
    final private int levelNumber;
    private int levelGrade;
    private int damageTaken;

    public Level(int levelNumber, int levelTime) {
        this.levelNumber = levelNumber;
        this.levelTime = levelTime*1000;
        this.damageTaken = 0;
    }

    public int getLevelGrade() {
        return levelGrade;
    }

    public void setLevelGrade(int levelGrade) {
        this.levelGrade = levelGrade;
    }

    public void increaseDamageTaken() {
        damageTaken++;
    }

    public int getDamageTaken() {
        return damageTaken;
    }

    public void setDamageTaken(int damageTaken) {
        this.damageTaken = damageTaken;
    }

    public long getLevelTime() {
        return levelTime;
    }
}
