package com.araskaplan.ardo;

import java.util.ArrayList;

public class Exam {
    String name;
    int difficulty;
    int time;
    int ppq;
    ArrayList<Question> questions;

    public Exam(String name, int difficulty, int time, int ppq) {
        this.name = name;
        this.difficulty = difficulty;
        this.time = time;
        this.ppq = ppq;
        this.questions=new ArrayList<Question>();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getPpq() {
        return ppq;
    }

    public void setPpq(int ppq) {
        this.ppq = ppq;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }
}
