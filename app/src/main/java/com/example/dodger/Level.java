package com.example.dodger;

import androidx.annotation.NonNull;

public class Level extends Thread {
    final private long levelTime;
    final private int levelNumber;
    private int damageTaken;
    private String levelName;
    private String description;
    private Grade grade;

    public Level(int levelNumber, String levelName, String description, long levelTime) {
        this.levelNumber = levelNumber;
        this.levelName = levelName;
        this.description = description;
        this.levelTime = levelTime;
        this.damageTaken = 0;
        this.grade = Grade.NOT_PLAYED;
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

    public int getLevelNumber() {
        return levelNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public Grade getLevelGrade() {
        return grade;
    }

    public void setLevelGrade(Grade grade) {
        this.grade = grade;
    }

    public void calculateLevelGrade() {
        if (damageTaken == 0)
            grade = Grade.S;
        else if (damageTaken >= 1 && damageTaken <= 3)
            grade = Grade.A;
        else if (damageTaken >= 4 && damageTaken <= 8)
            grade = Grade.B;
        else if (damageTaken >= 9 && damageTaken <= 15)
            grade = Grade.C;
        else if (damageTaken >= 16 && damageTaken <= 25)
            grade = Grade.D;
        else
            grade = Grade.F;
    }
}
