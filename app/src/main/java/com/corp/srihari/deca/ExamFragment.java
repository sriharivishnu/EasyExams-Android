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

import static com.corp.srihari.deca.ExamActivity.answers;
import static com.corp.srihari.deca.ExamActivity.lines;
import static com.corp.srihari.deca.ExamActivity.mPager;
import static com.corp.srihari.deca.ExamActivity.questions;
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

        if (questions.get(questionNumber)[1].equals("A")) {
            firstResponse.setBackgroundColor(getContext().getColor(R.color.colorPrimary));
        }
        else if (questions.get(questionNumber)[1].equals("B")) {
            secondResponse.setBackgroundColor(getContext().getColor(R.color.colorPrimary));
        }
        else if (questions.get(questionNumber)[1].equals("C")) {
            thirdResponse.setBackgroundColor(getContext().getColor(R.color.colorPrimary));
        }
        else if (questions.get(questionNumber)[1].equals("D")) {
            fourthResponse.setBackgroundColor(getContext().getColor(R.color.colorPrimary));
        }

        return rootView;

    }
    private void selectedColourChange(Button input, String in) {
        String choice = questions.get(questionNumber)[1];
        if (!choice.equals("Z")) {
            if (choice.equals("A")) {
                firstResponse.setBackgroundColor(getContext().getColor(R.color.questionButtonColour));
            }
            else if (choice.equals("B")) {
                secondResponse.setBackgroundColor(getContext().getColor(R.color.questionButtonColour));
            }
            else if (choice.equals("C")) {
                thirdResponse.setBackgroundColor(getContext().getColor(R.color.questionButtonColour));
            }
            else if (choice.equals("D")) {
                fourthResponse.setBackgroundColor(getContext().getColor(R.color.questionButtonColour));
            }
        }
        input.setBackgroundColor(getContext().getColor(R.color.colorPrimary));
        questions.get(questionNumber)[1] = in;
    }
    private void delay() {
        final Handler handler = new Handler();
        final int currentItem = mPager.getCurrentItem();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mPager.getCurrentItem() < 99 && currentItem == mPager.getCurrentItem()) {
                    mPager.setCurrentItem(currentItem + 1);
                }
            }
        }, 1000);
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
