package com.corp.srihari.deca;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class CardFragment extends Fragment implements View.OnClickListener {
    private AnimatorSet animIn, animOut;
    private View cardFront, cardBack;
    private TextView front_text, back_text;
    private ViewGroup rootView;
    private boolean onFront;
    private String front;
    private String back;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_card, container, false);
        findViews();
        loadAnim();
        changeCameraDistance();
        cardFront.setOnClickListener(this);
        onFront = true;

        front = getArguments().getString("term");
        back = getArguments().getString("definition");
        front_text.setText(front);
        back_text.setText(back);
        return rootView;
    }
    private void changeCameraDistance() {
        float scale = getResources().getDisplayMetrics().density * 8000;
        cardFront.setCameraDistance(scale);
        cardBack.setCameraDistance(scale);
    }
    private void findViews() {
        cardFront = rootView.findViewById(R.id.card_frame_front);
        cardBack = rootView.findViewById(R.id.card_frame_back);
        front_text = cardFront.findViewById(R.id.front_text);
        back_text = cardBack.findViewById(R.id.back_text);
    }
    private void loadAnim() {
        animIn = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.anim_in);
        animOut = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.anim_out);
    }
    public void flipCard() {
        if (onFront) {
            animOut.setTarget(cardFront);
            animIn.setTarget(cardBack);
            animOut.start();
            animIn.start();
            onFront = false;

        } else {
            animOut.setTarget(cardBack);
            animIn.setTarget(cardFront);
            animOut.start();
            animIn.start();
            onFront = true;
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.card_frame_front):
                flipCard();
        }
    }
}
