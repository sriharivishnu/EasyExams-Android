package com.corp.srihari.deca;

/**
 * Created by Srihari Vishnu on 2018-03-03.
 */
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import static android.content.Context.ACCESSIBILITY_SERVICE;
import static com.corp.srihari.deca.ExamActivity.answers;
import static com.corp.srihari.deca.ExamActivity.lines;
import static com.corp.srihari.deca.ExamActivity.mPager;
import static com.corp.srihari.deca.ExamActivity.questions;
import static com.corp.srihari.deca.ExamActivity.answers;
import static com.corp.srihari.deca.ExamActivity.wrong;

public class ExamFragment extends Fragment implements View.OnClickListener{
    private TextView questionView, titleText;
    private Button firstResponse,secondResponse, thirdResponse, fourthResponse;
    private Random rand;
    private Chronometer timer;
    private CountDownTimer countDownTimer;
    private int n;
    private int questionNumber;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_exam,container,false);
        questionView = (TextView) rootView.findViewById(R.id.questionView);
        firstResponse = (Button) rootView.findViewById(R.id.firstResponse);
        secondResponse = (Button) rootView.findViewById(R.id.secondResponse);
        thirdResponse = (Button) rootView.findViewById(R.id.thirdResponse);
        fourthResponse = (Button) rootView.findViewById(R.id.fourthResponse);
        n = getArguments().getInt("questionToFind");
        questionNumber = getArguments().getInt("position");

        firstResponse.setOnClickListener(this);
        secondResponse.setOnClickListener(this);
        thirdResponse.setOnClickListener(this);
        fourthResponse.setOnClickListener(this);

        String text = (questionNumber+1) +". "+ lines.get(5*n).substring(3,lines.get(5*n).length());
        questionView.setText(text);
        firstResponse.setText(lines.get(5*n+1));
        secondResponse.setText(lines.get(5*n+2));
        thirdResponse.setText(lines.get(5*n+3));
        fourthResponse.setText(lines.get(5*n+4));

        if (!getArguments().getBoolean("Instant")) {
            if (questions.get(questionNumber)[1].equals("A")) {
                firstResponse.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            } else if (questions.get(questionNumber)[1].equals("B")) {
                secondResponse.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            } else if (questions.get(questionNumber)[1].equals("C")) {
                thirdResponse.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            } else if (questions.get(questionNumber)[1].equals("D")) {
                fourthResponse.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        } else {

            if (!questions.get(questionNumber)[1].equals("Z")) {
                setBothColours();
            }
        }

        return rootView;

    }
    private void selectedColourChange(Button input, String in) {
        String choice = questions.get(questionNumber)[1];
        // Changes previous selected button back to original colour
        if (!choice.equals("Z")) {
            if (choice.equals("A")) {
                firstResponse.setBackgroundColor(getResources().getColor(R.color.questionButtonColour));
            }
            else if (choice.equals("B")) {
                secondResponse.setBackgroundColor(getResources().getColor(R.color.questionButtonColour));
            }
            else if (choice.equals("C")) {
                thirdResponse.setBackgroundColor(getResources().getColor(R.color.questionButtonColour));
            }
            else if (choice.equals("D")) {
                fourthResponse.setBackgroundColor(getResources().getColor(R.color.questionButtonColour));
            }
        }
        //Sets the new answer as a different colour
        if (getArguments().getBoolean("Instant", false)) {
            String answer = answers.get(Integer.parseInt(questions.get(questionNumber)[0]));
            if ((answer).equals(in)) {
                input.setBackgroundColor(getResources().getColor(R.color.green));
            } else {
                input.setBackgroundColor(getResources().getColor(R.color.red));
                if (answer.equals("A")) {
                    firstResponse.setBackgroundColor(getResources().getColor(R.color.green));
                }
                else if (answer.equals("B")) {
                    secondResponse.setBackgroundColor(getResources().getColor(R.color.green));
                }
                else if (answer.equals("C")) {
                    thirdResponse.setBackgroundColor(getResources().getColor(R.color.green));
                }
                else if (answer.equals("D")) {
                    fourthResponse.setBackgroundColor(getResources().getColor(R.color.green));
                }
            }
        }
        else {
            input.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        questions.get(questionNumber)[1] = in;   //Mark the answer change in the answers array
    }
    private void delay() {
        final Handler handler = new Handler();
        final int duration;
        if (getArguments().getBoolean("Instant",false)) {
            duration = 3000;
        }
        else {
            duration = 1000;
        }
        final int currentItem = mPager.getCurrentItem();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mPager.getCurrentItem() < 99 && currentItem == mPager.getCurrentItem()) {
                    mPager.setCurrentItem(currentItem + 1);
                }
            }
        }, duration);
    }
    public void setBothColours() {
        if (questions.get(questionNumber)[1].equals("A")) {
            firstResponse.setBackgroundColor(getResources().getColor(R.color.red));
        }
        else if (questions.get(questionNumber)[1].equals("B")) {
            secondResponse.setBackgroundColor(getResources().getColor(R.color.red));
        }
        else if (questions.get(questionNumber)[1].equals("C")) {
            thirdResponse.setBackgroundColor(getResources().getColor(R.color.red));
        }
        else if (questions.get(questionNumber)[1].equals("D")) {
            fourthResponse.setBackgroundColor(getResources().getColor(R.color.red));
        }

        if (answers.get(Integer.parseInt(questions.get(questionNumber)[0])).equals("A")) {
            firstResponse.setBackgroundColor(getResources().getColor(R.color.green));
        }
        else if (answers.get(Integer.parseInt(questions.get(questionNumber)[0])).equals("B")) {
            secondResponse.setBackgroundColor(getResources().getColor(R.color.green));
        }
        else if (answers.get(Integer.parseInt(questions.get(questionNumber)[0])).equals("C")) {
            thirdResponse.setBackgroundColor(getResources().getColor(R.color.green));
        }
        else if (answers.get(Integer.parseInt(questions.get(questionNumber)[0])).equals("D")) {
            fourthResponse.setBackgroundColor(getResources().getColor(R.color.green));
        }
        firstResponse.setEnabled(false);
        secondResponse.setEnabled(false);
        thirdResponse.setEnabled(false);
        fourthResponse.setEnabled(false);
    }
    @Override
    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.firstResponse:
                selectedColourChange(firstResponse, "A");
                delay();
                break;
            case R.id.secondResponse:
                selectedColourChange(secondResponse, "B");
                delay();
                break;
            case R.id.thirdResponse:
                selectedColourChange(thirdResponse,"C");
                delay();
                break;
            case R.id.fourthResponse:
                selectedColourChange(fourthResponse,"D");
                delay();
                break;
        }

    }

}
