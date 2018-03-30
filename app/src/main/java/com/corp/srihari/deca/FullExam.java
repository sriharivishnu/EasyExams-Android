package com.corp.srihari.deca;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import static com.corp.srihari.deca.ExamActivity.answers;
import static com.corp.srihari.deca.ExamActivity.lines;
import static com.corp.srihari.deca.ExamActivity.questions;

public class FullExam extends AppCompatActivity {
    private ImageButton back;
    private ListView displayFullExam;
    private MyAdapter listAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_exam);

        back = (ImageButton) findViewById(R.id.backButtonFull);
        displayFullExam = (ListView) findViewById(R.id.displayFullExam);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ArrayList<ArrayList<String>> allQuestions = new ArrayList<ArrayList<String>>();
        ArrayList<String> temp = new ArrayList<String>();
        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i)[1].equals("Z")) {
                temp.add("(UNANSWERED) "+Integer.toString(i+1) +". "+ lines.get(Integer.parseInt(questions.get(i)[0]) * 5 ).substring(4));
            } else {
                temp.add(Integer.toString(i + 1) + ". " + lines.get(Integer.parseInt(questions.get(i)[0]) * 5).substring(4));
            }
            for (int j = 1; j<5; j++) {
                temp.add(lines.get(Integer.parseInt(questions.get(i)[0]) * 5 + j));
            }
            temp.add(answers.get(Integer.parseInt(questions.get(i)[0])));
            temp.add(questions.get(i)[1]);
            allQuestions.add(new ArrayList<>(temp));
            temp.clear();
        }
        listAdapter = new MyAdapter(this, allQuestions);
        displayFullExam.setAdapter(listAdapter);


    }
}
