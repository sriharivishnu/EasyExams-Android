package com.corp.srihari.deca;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.Random;

import java.util.List;

public class ExamActivity extends FragmentActivity implements View.OnClickListener {
    private QuoteBank mQuoteBank;
    public static List<String> answers;
    private TextView titleText;
    private Button endExam;
    private Random rand;
    private TextView timer;
    private CountDownTimer countDownTimer;
    private ImageButton homeExam;
    private ImageButton helpButton;
    public static List<String[]> wrong;
    public static List<String> lines;
    public static ArrayList<String[]> questions;
    public static ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private String selectedExam;

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

        helpButton = (ImageButton) findViewById(R.id.infoButtonExam);
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
        else if (examType == 4) {
            lines = mQuoteBank.readLine("EntrepreneurshipExamQuestions.txt");
            answers = mQuoteBank.readLine("EntrepreneurshipExamAnswers.txt");
        }
        else {
            lines = mQuoteBank.getWrongAnswersQuestions();
            answers = mQuoteBank.getWrongAnswersAnswers();

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
        timer = (TextView) findViewById(R.id.timer);
        long seventymin = 1000*60*70;
        countDownTimer =  new CountDownTimer(seventymin, 1000) {

            public void onTick(long millisUntilFinished) {

                int totalTime = 60000; // in milliseconds i.e. 60 seconds
                String v = String.format("%02d", millisUntilFinished/totalTime);
                int va = (int)( (millisUntilFinished%totalTime)/1000);
                timer.setText(v+":"+String.format("%02d",va));
            }

            public void onFinish() {
                finishGame();
            }
        };
        countDownTimer.start();

    }

    private void finishGame () {
        if (!isInstantAnswer) {
            Intent intent = new Intent(ExamActivity.this, EndGame.class);
            QuoteBank mQuotebank = new QuoteBank(this);
            ArrayList<String> wrongNumbers = new ArrayList<>();
            ArrayList<String> rightAnswers = new ArrayList<>();
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
            if (examType != 999) {
                mQuotebank.saveWrongQuestions(wrongNumbers, lines, answers, new ArrayList<String>());
            } else {
                mQuotebank.saveWrongQuestions(new ArrayList<String>(), lines, answers, rightAnswers);
            }

            intent.putExtra("CORRECT_ANSWERS", correct);
            intent.putExtra("ExamName", selectedExam);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(ExamActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
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
