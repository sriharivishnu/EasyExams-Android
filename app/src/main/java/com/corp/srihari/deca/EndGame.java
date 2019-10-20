package com.corp.srihari.deca;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EndGame extends AppCompatActivity {
    private ListView displayWrong;
    private MyAdapter listAdapter ;
    private DatabaseUtils databaseUtils;
    private TextView titleEnd;
    private ImageButton homeExam2,wrongAnswersButton, fullExam, newExam, post_button;
    private TextView scoreText;
    private int correctques;
    private String examName, selectedExam;
    public static ArrayList<Integer> questions;
    private ArrayList<Integer> scores;
    private Boolean savedScores;
    private Boolean posted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);
        titleEnd = (TextView) findViewById(R.id.titleEnd);
        examName = getIntent().getStringExtra("ExamName") + " Exam";
        selectedExam = getIntent().getStringExtra("ExamName");
        titleEnd.setText(examName);

        savedScores = false;
        posted = false;

        correctques = getIntent().getIntExtra("CORRECT_ANSWERS",0);
        if (correctques > 100) {
            correctques = correctques/2;
            posted = true;
        }
        questions = getIntent().getIntegerArrayListExtra("QUESTIONS");

        String display = "Score: "+Integer.toString(correctques) + "/100";

        scoreText = (TextView) findViewById(R.id.scoreText);
        scoreText.setText(display);
        homeExam2 = (ImageButton) findViewById(R.id.homeExam2);
        homeExam2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EndGame.this, MainActivity.class);
                startActivity(in);
                finish();
            }
        });

        wrongAnswersButton = (ImageButton) findViewById(R.id.wrongAnswersButton);
        buttonAnimation(wrongAnswersButton);

        fullExam = (ImageButton) findViewById(R.id.fullExamButton);
        buttonAnimation(fullExam);

        newExam = (ImageButton) findViewById(R.id.newExamButton);
        buttonAnimation(newExam);

        post_button = (ImageButton) findViewById(R.id.postButton);
        buttonAnimation(post_button);

    }
    public void buttonAnimation(final ImageButton button) {
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(button, "scaleX", 0.9f);
                        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(button, "scaleY", 0.9f);
                        scaleDownX.setDuration(0);
                        scaleDownY.setDuration(0);

                        AnimatorSet scaleDown = new AnimatorSet();
                        scaleDown.play(scaleDownX).with(scaleDownY);

                        scaleDown.start();

                        return true;
                    case MotionEvent.ACTION_UP:
                        buttonAction(button);
                        ObjectAnimator scaleDownX2 = ObjectAnimator.ofFloat(button, "scaleX", 1f);
                        ObjectAnimator scaleDownY2 = ObjectAnimator.ofFloat(button, "scaleY", 1f);
                        scaleDownX2.setDuration(0);
                        scaleDownY2.setDuration(0);

                        AnimatorSet scaleDown2 = new AnimatorSet();
                        scaleDown2.play(scaleDownX2).with(scaleDownY2);

                        scaleDown2.start();

                        return true;
                }
                return true;
            }
        });
    }
    private void buttonAction(ImageButton button) {
        switch(button.getId()) {
            case R.id.wrongAnswersButton:
                Intent intent = new Intent(EndGame.this, WrongAnswersActivity.class);
                startActivity(intent);
                break;
            case R.id.fullExamButton:
                Intent intent1 = new Intent(EndGame.this, FullExam.class);
                startActivity(intent1);
                break;
            case R.id.newExamButton:
                Intent in = new Intent (EndGame.this, ExamActivity.class);
                startActivity(in);
                finish();
                break;
            case R.id.postButton:
                requestPost();
                break;
        }
    }
    private void requestPost() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this).setMessage(R.string.post_message).setPositiveButton("Post", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!posted) {
                    saveScoresDatabase();
                    saveScoresLocal();
                }
                if (savedScores) {
                    Toast.makeText(getBaseContext(),"Scores Already Saved", Toast.LENGTH_LONG).show();
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
    private void saveScoresDatabase() {
        scores = new ArrayList<>();
        databaseUtils = new DatabaseUtils();
        DatabaseReference mReference = databaseUtils.getDatabaseInstance().getReference().child("Users").child(databaseUtils.getUserID()).child("Scores").child(examName);
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                scores = (ArrayList<Integer>) dataSnapshot.getValue();
                if (scores == null) {
                    scores = new ArrayList<>();
                }
                if (!savedScores) {
                    scores.add(getIntent().getIntExtra("CORRECT_ANSWERS",0));
                    databaseUtils.getDatabaseInstance().getReference().child("Users").child(databaseUtils.getUserID()).child("Scores").child(examName).setValue(scores);
                    //ExamClass exam = new ExamClass(questions);
                    //databaseUtils.getDatabaseInstance().getReference().child("Users").child(databaseUtils.getUserID()).child("Past Exams").child();
                    //databaseUtils.getDatabaseInstance().getReference().child("Users").child(databaseUtils.getUserID()).child("PastExams").child()
                    savedScores = true;
                    Toast.makeText(getBaseContext(), "Score Has Been Saved", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void saveScoresLocal() {
        int[] dataPoints;
        dataPoints = QuoteBank.getArray(this, selectedExam);
        if (dataPoints == null) {
            dataPoints = new int[1];
            dataPoints[0] = 0;
        }
        int[] dataPoints2 = new int[dataPoints.length + 1];
        for (int i = 0; i < dataPoints.length; i++) {
            dataPoints2[i] = dataPoints[i];
        }
        dataPoints2[dataPoints2.length - 1] = correctques;
        QuoteBank.saveArray(this, dataPoints2, selectedExam);
    }

}
