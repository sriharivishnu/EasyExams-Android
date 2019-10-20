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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PerformanceIndicators extends AppCompatActivity implements View.OnClickListener {
    String row_separator = "--ROW--";
    String term_separator = "--TERM_DEFINITION--";

    private QuoteBank quoteBank;

    private TextView titleText;
    private ImageButton homeButton;
    private PagerAdapter pagerAdapter;
    private ViewPager cards;
    private TabLayout threedots;

    private List<String> lines;
    private ArrayList<String> terms;
    private ArrayList<String> definitions;
    private Integer total_terms;
    private ArrayList<Integer> term_numbers;
    private String examName;
    private Integer examType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance_indicators);
        findViews();
        getData();

        homeButton.setOnClickListener(this);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        cards.setAdapter(pagerAdapter);

        titleText.setText(examName);

        total_terms = Collections.frequency(lines, row_separator);
        getTermsDefinitions();
        Collections.shuffle(term_numbers);

    }
    private void findViews() {
        homeButton = (ImageButton) findViewById(R.id.homePI);
        cards = findViewById(R.id.cards_pager);
        titleText = findViewById(R.id.titleTextPI);
        threedots = findViewById(R.id.threedots);
        threedots.setupWithViewPager(cards, true);
    }
    private void getData() {
        quoteBank = new QuoteBank(this);
        terms = new ArrayList<>();
        definitions = new ArrayList<>();

        examType = getIntent().getIntExtra("examType", 0);
        examName = getIntent().getStringExtra("ExamName");

        if (examType == 0) {
            lines = quoteBank.readLine("MarketingPI.txt");
        }
        else if (examType == 1) {
            lines = quoteBank.readLine("FinancePI.txt");
        }
        else if (examType == 2 || examType == 3) {
            lines = quoteBank.readLine("BusinessAdminPI.txt");
        }
        else if (examType == 4) {
            lines = quoteBank.readLine("EntrepreneurshipPI.txt");
        }
        else {
            Log.e("Exam Not Found", examName);
        }

    }

    private void getTermsDefinitions() {
        int term = 0;
        String builder = "";
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).equals(row_separator)) {
                term++;
                definitions.add(builder);
                builder = "";
            }
            else if (lines.get(i).equals(term_separator)) {
                terms.add(builder);
                builder = "";
            }
            else {
                builder += lines.get(i) + "\n";
            }
        }
        term_numbers = new ArrayList<>();
        for (int j = 0; j < total_terms; j++) {
            term_numbers.add(j);
        }
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
            Bundle b = new Bundle();
            b.putString("term",terms.get(term_numbers.get(position)));
            b.putString("definition", definitions.get(term_numbers.get(position)));
            CardFragment cardFragment = new CardFragment();
            cardFragment.setArguments(b);
            return cardFragment;
        }

        @Override
        public int getCount() {
            return 10;
        }
    }
}
