package com.corp.srihari.deca;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuestionBankActivity extends AppCompatActivity {
    private List<String> lines, answers;
    final QuoteBank mQuoteBank = new QuoteBank(this);
    private ExpandableListView listView;
    private ExpandableListAdapter mAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_bank);
        String exam = "";
        int i = getIntent().getIntExtra("QuestionSet",0);
        exam = getResources().getStringArray(R.array.examChoices)[i];
        if (i == 0) {
            lines = mQuoteBank.readLine("MarketingExamQuestions.txt");
            answers = mQuoteBank.readLine("MarketingExamAnswers.txt");
        }
        //Finance
        else if (i == 1) {
            lines = mQuoteBank.readLine("FinanceExamQuestions.txt");
            answers = mQuoteBank.readLine("FinanceExamAnswers.txt");
        }
        //Hospitality and Tourism
        else if (i == 2) {
            lines = mQuoteBank.readLine("HospitalityExamQuestions.txt");
            answers = mQuoteBank.readLine("HospitalityExamAnswers.txt");
        }
        //Business Management
        else if (i == 3) {
            lines = mQuoteBank.readLine("BusinessAdminQuestions.txt");
            answers = mQuoteBank.readLine("BusinessAdminAnswers.txt");
        }
        else if (i == 4) {
            lines = mQuoteBank.readLine("EntrepreneurshipExamQuestions.txt");
            answers = mQuoteBank.readLine("EntrepreneurshipExamAnswers.txt");
        }
        else {
            lines = mQuoteBank.getWrongAnswersQuestions();
            answers = mQuoteBank.getWrongAnswersAnswers();
        }
        ArrayList<String> q = new ArrayList<>();
        HashMap<String, ArrayList<String>> h = new HashMap<>();
        for (int j = 0; j<lines.size(); j+=5) {
            q.add(lines.get(j));
            ArrayList<String> temp = new ArrayList<>();
            for (int x = 1; x<5; x++) {
                temp.add(lines.get(j+x));
            }
            h.put(lines.get(j), temp);
        }
        mAdapter = new ExpandableListAdapter(this,q,h);
        listView = (ExpandableListView) findViewById(R.id.questionBankList);
        listView.setAdapter(mAdapter);

        toolbar = (Toolbar) findViewById(R.id.question_bank_toolbar);
        Drawable backArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        backArrow.setTint(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(backArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.setTitle(exam+" Questions");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
    }
}
