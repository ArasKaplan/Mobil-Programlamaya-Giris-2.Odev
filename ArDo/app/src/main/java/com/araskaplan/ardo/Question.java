package com.araskaplan.ardo;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Question {
    String text;
    int correctanswer;
    Bitmap questionImage;
    int authorId;
    int question_id;

    ArrayList<String>answers=new ArrayList<String>();

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Question(String text, ArrayList<String> answers,int correctanswer,int authorId,int question_id) {
        this.text = text;
        this.answers = answers;
        this.correctanswer=correctanswer;
        this.authorId=authorId;
        this.question_id=question_id;

    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public Bitmap getQuestionImage() {
        return questionImage;
    }

    public void setQuestionImage(Bitmap questionImage) {
        this.questionImage = questionImage;
    }

    public int getCorrectanswer() {
        return correctanswer;
    }

    public void setCorrectanswer(int correctanswer) {
        this.correctanswer = correctanswer;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }
}
