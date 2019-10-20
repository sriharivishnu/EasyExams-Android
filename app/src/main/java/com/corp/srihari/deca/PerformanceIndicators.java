package com.corp.srihari.deca;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class PerformanceIndicators extends AppCompatActivity implements View.OnClickListener {
    private ImageButton homeButton;
    private PagerAdapter pagerAdapter;
    private ViewPager cards;
    private TabLayout threedots;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance_indicators);
        findViews();
        homeButton.setOnClickListener(this);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        cards.setAdapter(pagerAdapter);

    }
    private void findViews() {
        homeButton = (ImageButton) findViewById(R.id.homePI);
        cards = findViewById(R.id.cards_pager);
        threedots = findViewById(R.id.threedots);
        threedots.setupWithViewPager(cards, true);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.homePI):
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (cards.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            cards.setCurrentItem(cards.getCurrentItem() - 1);
        }

        return super.onKeyDown(keyCode, event);
    }
    private class PagerAdapter extends FragmentStatePagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            CardFragment cardFragment = new CardFragment();
            return cardFragment;
        }

        @Override
        public int getCount() {
            return 10;
        }
    }
}
