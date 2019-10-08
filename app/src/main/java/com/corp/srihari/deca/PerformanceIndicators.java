package com.corp.srihari.deca;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class PerformanceIndicators extends AppCompatActivity implements View.OnClickListener {
    private ImageButton homeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance_indicators);
        homeButton = (ImageButton) findViewById(R.id.homePI);
        homeButton.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.homePI):
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();

        }
    }
}
