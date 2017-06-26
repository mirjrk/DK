package com.zesium.android.betting.model.user;

/**
 * Created by Ivan Panic_2 on 7/1/2016.
 */
public class Question {
    private int Id;
    private String Question;
    private String LanguageId;
    private int TimeStamp;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getLanguageId() {
        return LanguageId;
    }

    public void setLanguageId(String languageId) {
        LanguageId = languageId;
    }

    public int getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        TimeStamp = timeStamp;
    }
}
