package com.corp.srihari.deca;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;


import com.jjoe64.graphview.series.DataPoint;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

import java.util.List;

public class ExamActivity extends FragmentActivity implements View.OnClickListener {
    private QuoteBank mQuoteBank;
    public static List<String> answers;
    private TextView titleText;
    private Button endExam;
    private Random rand;
    private Chronometer timer;
    private CountDownTimer countDownTimer;
    private ImageButton homeExam;
    public static List<String[]> wrong;
    public static List<String> lines;
    public static ArrayList<String[]> questions;
    public static ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private String selectedExam;
    private int[] dataPoints;

    private boolean visited[];
    private boolean isInstantAnswer;
    private int examType;

    private int n;
    private int num;
    private int correct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        rand = new Random();

        examType = getIntent().getIntExtra("examType",0);
        if (examType == 999) {
            selectedExam = "Wrong Answers Review";
        }
        else {
            selectedExam = getResources().getStringArray(R.array.examChoices)[examType];
        }

        mQuoteBank = new QuoteBank(this);
        //Marketing
        if (examType == 0) {
            lines = mQuoteBank.readLine("MarketingExamQuestions.txt");
            answers = mQuoteBank.readLine("MarketingExamAnswers.txt");
        }
        //Finance
        else if (examType == 1) {
            lines = mQuoteBank.readLine("FinanceExamQuestions.txt");
            answers = mQuoteBank.readLine("FinanceExamAnswers.txt");
        }
        //Hospitality and Tourism
        else if (examType == 2) {
            lines = mQuoteBank.readLine("HospitalityExamQuestions.txt");
            answers = mQuoteBank.readLine("HospitalityExamAnswers.txt");
        }
        //Business Management
        else if (examType == 3) {
            lines = mQuoteBank.readLine("BusinessAdminQuestions.txt");
            answers = mQuoteBank.readLine("BusinessAdminAnswers.txt");
        }
        else {
            lines = mQuoteBank.getWrongAnswersQuestions();
            answers = mQuoteBank.getWrongAnswersAnswers();

            Log.d("Inexam", lines.toString());
            Log.d("Inexama", answers.toString());
        }
        isInstantAnswer = getIntent().getBooleanExtra("InstantFeedback", false);
        questions = new ArrayList<>();
        visited = new boolean[lines.size() / 5];
        num = 0;
        if (lines.size()/5 <100) {
            num = lines.size()/5;
        } else {
            num = 100;
        }
        for (int i = 0; i < num; i++) {
            n = rand.nextInt(lines.size() / 5);
            while (visited[n]) {
                n = rand.nextInt(lines.size() / 5);
            }
            visited[n] = true;
            questions.add(new String[] {Integer.toString(n), "Z"});
        }

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        titleText = (TextView) findViewById(R.id.titleText);
        titleText.setText(selectedExam + " Exam");
        homeExam = (ImageButton) findViewById(R.id.homeExam);
        homeExam.setOnClickListener(this);

        endExam = (Button) findViewById(R.id.endExam);
        endExam.setOnClickListener(this);

        correct = 0;
        wrong = new ArrayList<String[]>();
        timer = (Chronometer) findViewById(R.id.timer);
        timer.setCountDown(true);
        long seventymin = 1000*60*70;
        timer.setBase(SystemClock.elapsedRealtime()+seventymin);

        countDownTimer = new CountDownTimer(seventymin, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                finishGame();
            }
        };
        countDownTimer.start();
        timer.start();

    }

    private void finishGame () {
        if (!isInstantAnswer) {
            Intent intent = new Intent(ExamActivity.this, EndGame.class);
            QuoteBank mQuotebank = new QuoteBank(this);
            ArrayList<String> wrongNumbers = new ArrayList<>();
            ArrayList<String> rightAnswers = new ArrayList<>();
            Log.d("Num", Integer.toString(num));
            for (int x = 0; x < num; x++) {
                if (answers.get(Integer.parseInt(questions.get(x)[0])).equals(questions.get(x)[1])) {
                    correct += 1;

                    rightAnswers.add(questions.get(x)[0]);

                } else {
                    wrong.add(new String[]{questions.get(x)[0], questions.get(x)[1], Integer.toString(x)});
                    if (!questions.get(x)[1].equals("Z")) {
                        wrongNumbers.add(questions.get(x)[0]);
                    }
                }
            }
            dataPoints = QuoteBank.getArray(this, selectedExam);
            if (dataPoints == null) {
                dataPoints = new int[1];
                dataPoints[0] = 0;
            }
            int[] dataPoints2 = new int[dataPoints.length + 1];
            for (int i = 0; i < dataPoints.length; i++) {
                dataPoints2[i] = dataPoints[i];
            }
            dataPoints2[dataPoints2.length - 1] = correct;
            if (examType != 999) {
                QuoteBank.saveArray(this, dataPoints2, selectedExam);

                mQuotebank.saveWrongQuestions(wrongNumbers, lines, answers, new ArrayList<String>());
            } else {
                mQuotebank.saveWrongQuestions(new ArrayList<String>(), lines, answers, rightAnswers);
            }

            intent.putExtra("CORRECT_ANSWERS", correct);
            intent.putExtra("ExamName", selectedExam + " Exam");
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(ExamActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }

        return super.onKeyDown(keyCode, event);
    }
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle b = new Bundle();
            b.putInt("questionToFind",Integer.parseInt(questions.get(position)[0]));
            b.putInt("position", position);
            b.putBoolean("Instant", isInstantAnswer);
            ExamFragment examFragment = new ExamFragment();
            examFragment.setArguments(b);
            return examFragment;
        }

        @Override
        public int getCount() {
            return num;
        }
    }
    @Override
    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.homeExam:
                DialogFragment customAlert = new CustomAlert();
                customAlert.show(getFragmentManager(),"quit");
                break;
            case R.id.endExam:
                finishGame();
                break;
        }
    }
}
