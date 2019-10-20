package com.corp.srihari.deca;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class ChooseExam extends AppCompatActivity {
    private AdapterChooseExam adapter;
    private ListView examChoices;
    private ArrayList<String> choices;
    private CheckBox instant;
    private boolean performance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_exam);
        Log.d("DSF", Arrays.toString(getResources().getStringArray(R.array.examChoices)));
        choices = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.examChoices)));
        examChoices = (ListView) findViewById(R.id.listViewChoose);
        instant = (CheckBox) findViewById(R.id.instantAnswerChecked);

        performance = getIntent().getBooleanExtra("PI", false);

        adapter = new AdapterChooseExam(this, choices);
        examChoices.setAdapter(adapter);

        if (performance) {
            instant.setVisibility(View.GONE);
        }

        examChoices.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>adapter, View v, int position, long something){
                Object item = adapter.getItemAtPosition(position);

                if (!performance) {
                    Intent intent = new Intent(ChooseExam.this, ExamActivity.class);
                    intent.putExtra("examType", position);
                    intent.putExtra("ExamName", choices.get(position));
                    intent.putExtra("InstantFeedback", instant.isChecked());
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(ChooseExam.this, PerformanceIndicators.class);
                    intent.putExtra("examType", position);
                    intent.putExtra("ExamName", choices.get(position));
                    startActivity(intent);
                }
            }
        });
    }
}
