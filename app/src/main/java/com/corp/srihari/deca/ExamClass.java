package com.corp.srihari.deca;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sriharivishnu on 2018-11-05.
 */

public class ExamClass {
    private ArrayList<String[]> questions;
    private ArrayList<String[]> wrong_answers;
    private HashMap<String, Object> timestampCreated;

    public ExamClass(ArrayList<String[]> questions, ArrayList<String[]> wrong_answers) {
        this.questions = questions;
        this.wrong_answers = wrong_answers;
        HashMap<String, Object> timestampCreated = new HashMap<>();
        timestampCreated.put("timestamp", ServerValue.TIMESTAMP);

    }
    public HashMap<String, Object> getTimestampCreated(){
        return timestampCreated;
    }
    public ArrayList<String[]> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<String[]> questions) {
        this.questions = questions;
    }

    public ArrayList<String[]> getWrong_answers() {
        return wrong_answers;
    }

    public void setWrong_answers(ArrayList<String[]> wrong_answers) {
        this.wrong_answers = wrong_answers;
    }

    @Exclude
    public long getTimestampCreatedLong(){
        return (long)timestampCreated.get("timestamp");
    }

}
