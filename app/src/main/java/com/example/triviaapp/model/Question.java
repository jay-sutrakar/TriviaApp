package com.example.triviaapp.model;

import androidx.annotation.NonNull;

public class Question {
    private String answer;
    private boolean answerTrue;

    public Question() {
    }

    public Question(String answer, boolean answerTrue) {
        this.answer = answer;
        this.answerTrue = answerTrue;
    }

    public boolean isAnswerTrue() {
        return answerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        this.answerTrue = answerTrue;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @NonNull
    @Override
    public String toString() {
        return "Question{"+
                "answer'"+answer+'\''+
                ", answerTrue=" +answerTrue+
                '}';
    }
}

