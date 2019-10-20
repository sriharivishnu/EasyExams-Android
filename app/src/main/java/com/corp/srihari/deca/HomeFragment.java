package com.corp.srihari.deca;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

/**
 * Created by sriharivishnu on 2018-09-08.
 */

public class HomeFragment extends Fragment {
    private ImageButton startExam;
    private ImageButton wrongExam;
    private ImageButton piButton;
    private ImageView mainLogo;
    private ImageButton settingsButton;
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        startExam = (ImageButton) view.findViewById(R.id.startExam);
        wrongExam = (ImageButton) view.findViewById(R.id.wrong_exam);
        piButton = (ImageButton) view.findViewById(R.id.piButton);
        mainLogo = (ImageView) view.findViewById(R.id.logoView);
        settingsButton = (ImageButton) view.findViewById(R.id.settingsButton);

        buttonAnimation(startExam);
        buttonAnimation(wrongExam);
        buttonAnimation(piButton);
        buttonAnimation(settingsButton);

        return view;
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
    private void goToSettings() {
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(intent);
    }
    private void buttonAction(ImageButton button) {
        switch(button.getId()) {
            case R.id.startExam:
                Intent intent = new Intent(getActivity(), ChooseExam.class);
                intent.putExtra("PI", false);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.wrong_exam:
                QuoteBank mQuotebank = new QuoteBank(getContext());
                if (!mQuotebank.isWrongAnswersEmpty()) {
                    Intent in = new Intent(getActivity(), ExamActivity.class);
                    in.putExtra("examType", 999);
                    in.putExtra("ExamName", "Wrong Answers Review");
                    startActivity(in);
                    getActivity().finish();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("No more Wrong Answers to Review!").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.create().show();
                }
                break;
            case R.id.settingsButton:
                goToSettings();
                break;
            case R.id.piButton:
                Intent in = new Intent(getActivity(), ChooseExam.class);
                in.putExtra("PI", true);
                startActivity(in);
                getActivity().finish();
//                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                builder.setTitle("BETA Feature").setMessage("This feature will be available in the next update!")
//                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.dismiss();
//                            }
//                        });
//                builder.create().show();

        }
    }
}
