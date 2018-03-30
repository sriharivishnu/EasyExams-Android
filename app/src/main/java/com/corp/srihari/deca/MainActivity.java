package com.corp.srihari.deca;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navigation;
    private ImageButton startExam;
    private ImageButton piButton;
    private ImageButton learnButton;
    private ImageButton nodata;
    private ImageView mainLogo;
    private GraphView graph;
    private TextView highText, timesPlayedText, avgScoreText, graphText;
    private Button resetStats;
    private Spinner selectorExamStat;
    private AdapterChooseExam spinnerAdapter;
    private String selectedExam;

    public static int[] dataPoints;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startExam.setVisibility(View.VISIBLE);
                    piButton.setVisibility(View.VISIBLE);
                    learnButton.setVisibility(View.VISIBLE);
                    mainLogo.setVisibility(View.VISIBLE);
                    graph.setVisibility(View.GONE);
                    nodata.setVisibility(View.GONE);
                    graphText.setVisibility(View.GONE);
                    avgScoreText.setVisibility(View.GONE);
                    timesPlayedText.setVisibility(View.GONE);
                    highText.setVisibility(View.GONE);
                    resetStats.setVisibility(View.GONE);
                    selectorExamStat.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_settings:
                    startExam.setVisibility(View.GONE);
                    piButton.setVisibility(View.GONE);
                    learnButton.setVisibility(View.GONE);
                    mainLogo.setVisibility(View.GONE);
                    graph.setVisibility(View.GONE);
                    nodata.setVisibility(View.GONE);
                    graphText.setVisibility(View.GONE);
                    avgScoreText.setVisibility(View.GONE);
                    timesPlayedText.setVisibility(View.GONE);
                    highText.setVisibility(View.GONE);
                    resetStats.setVisibility(View.GONE);
                    selectorExamStat.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_profile:
                    if (dataPoints.length == 1) {
                        nodata.setVisibility(View.VISIBLE);
                    }
                    startExam.setVisibility(View.GONE);
                    piButton.setVisibility(View.GONE);
                    learnButton.setVisibility(View.GONE);
                    mainLogo.setVisibility(View.GONE);
                    nodata.setVisibility(View.GONE);

                    selectorExamStat.setVisibility(View.VISIBLE);

                    graph.setVisibility(View.VISIBLE);
                    loadandswitch(selectedExam);

                    graphText.setVisibility(View.VISIBLE);
                    avgScoreText.setVisibility(View.VISIBLE);
                    timesPlayedText.setVisibility(View.VISIBLE);
                    highText.setVisibility(View.VISIBLE);
                    resetStats.setVisibility(View.VISIBLE);
                    return true;
            }
            return false;
        }

    };
    private AdapterView.OnItemSelectedListener onItemClickListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (i>=getResources().getStringArray(R.array.examChoices).length) {

            } else {
                selectedExam = getResources().getStringArray(R.array.examChoices)[i];
                loadandswitch(selectedExam);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_home);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        startExam = (ImageButton) findViewById(R.id.startExam);
        piButton = (ImageButton) findViewById(R.id.PIButton);
        learnButton = (ImageButton) findViewById(R.id.learnButton);
        mainLogo = (ImageView) findViewById(R.id.logoView);
        graph = (GraphView) findViewById(R.id.graph);
        GridLabelRenderer glr = graph.getGridLabelRenderer();
        glr.setPadding(32);
        buttonAnimation(startExam);
        buttonAnimation(piButton);
        buttonAnimation(learnButton);

        dataPoints = getArray(this, selectedExam);
        if (dataPoints == null) {
            dataPoints = new int[1];
            dataPoints[0] = 0;
        }
        Log.d("ARRAY", Arrays.toString(dataPoints));

        graphText = (TextView) findViewById(R.id.graphTitle);
        nodata = (ImageButton) findViewById(R.id.imageButton);
        highText = (TextView) findViewById(R.id.highScoreText);
        timesPlayedText = (TextView) findViewById(R.id.examsWritten);
        avgScoreText = (TextView) findViewById(R.id.averageText);
        selectorExamStat = (Spinner) findViewById(R.id.profile_exam_choice);

        selectedExam = getResources().getStringArray(R.array.examChoices)[0];


        resetStats = (Button) findViewById(R.id.resetStats);
        resetStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.resetStatsTitle).setMessage(R.string.resetStatsMessage).setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        resetStats();
                    }
                }).setNegativeButton(R.string.negative_exam_message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();
            }
        });

        spinnerAdapter = new AdapterChooseExam(MainActivity.this, new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.examChoices))));
        selectorExamStat.setAdapter(spinnerAdapter);
        selectorExamStat.setOnItemSelectedListener(onItemClickListener);
    }
    public void resetStats() {
        saveArray(MainActivity.this, new int[0], selectedExam);
        recreate();
        navigation.setSelectedItemId(R.id.navigation_home);
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
    public static String getPoints(String name, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings_Money", MODE_PRIVATE);
        return sharedPreferences.getString(name, "");
    }
    public static void saveArray(Context ctx, int[] array, String examName) {
        String strArr = "";
        for (int i=0; i<array.length; i++) {
            strArr += array[i] + ",";
        }
        if (strArr.length() != 0) {
            strArr = strArr.substring(0, strArr.length() - 1);
        }

        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(ctx).edit();
        e.putString(examName, strArr);
        e.commit();
    }

    public static int[] getArray(Context ctx, String examName) {
        String[] strArr;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        String str = prefs.getString(examName, null);
        if (str == null || str.equals("")) {
            return null;
        }
        else {
            strArr = str.split(",");
        }
        int[] array = new int[strArr.length];
        for (int i=0; i<strArr.length; i++) {
            array[i] = Integer.parseInt(strArr[i]);
        }
        return array;
    }
    public void loadandswitch(String exam) {
        graph.removeAllSeries();
        dataPoints = getArray(MainActivity.this, exam);
        if (dataPoints == null) {
            dataPoints = new int[1];
            dataPoints[0] = 0;
        }
        if (dataPoints.length == 1) {
            nodata.setVisibility(View.VISIBLE);
        } else {
            nodata.setVisibility(View.GONE);
        }
        DataPoint[] s = new DataPoint[dataPoints.length];
        for (int i = 0; i< dataPoints.length; i++) {
            s[i] = new DataPoint(i, dataPoints[i]);
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(s);
        series.setDrawBackground(true);
        series.setDrawDataPoints(true);

        int highest = dataPoints[0];
        int sum =0;
        for (int i: dataPoints) {
            if (i>highest) {
                highest = i;
            }
            sum+=i;
        }

        double average = Math.round((1.0d * sum / (dataPoints.length-1))*100.0)/100.0;
        String textAverage = "Average Score: " + average;
        avgScoreText.setText(textAverage);
        String textHigh = "High Score: " + highest;
        highText.setText(textHigh);
        String textTimes = "Exams Written: " + (dataPoints.length-1);
        timesPlayedText.setText(textTimes);
        graph.addSeries(series);
    }
    private void buttonAction(ImageButton button) {
        switch(button.getId()) {
            case R.id.startExam:
                Intent intent = new Intent(MainActivity.this, ChooseExam.class);
                startActivity(intent);
                finish();
                break;
            case R.id.PIButton:
                Intent in = new Intent(MainActivity.this, PerformanceIndicators.class);
                startActivity(in);
                finish();
                break;
        }
    }
}
