package com.corp.srihari.deca;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sriharivishnu on 2018-09-08.
 */

public class ProfileFragment extends Fragment {
    private GraphView graph;
    private TextView highText, timesPlayedText, avgScoreText, graphText;
    private Button resetStats;
    private Spinner selectorExamStat;
    private AdapterChooseExam spinnerAdapter;
    private String selectedExam;
    private ImageButton nodata;
    public static int[] dataPoints;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        nodata = (ImageButton) view.findViewById(R.id.imageButton);
        dataPoints = QuoteBank.getArray(getContext(), selectedExam);
        if (dataPoints == null) {
            dataPoints = new int[1];
            dataPoints[0] = 0;
        }
        if (dataPoints.length == 1) {
            nodata.setVisibility(View.VISIBLE);
        }

        graph = (GraphView) view.findViewById(R.id.graph);
        GridLabelRenderer glr = graph.getGridLabelRenderer();
        glr.setPadding(32);
        glr.setVerticalAxisTitle("Score");

        graphText = (TextView) view.findViewById(R.id.graphTitle);
        nodata = (ImageButton) view.findViewById(R.id.imageButton);
        highText = (TextView) view.findViewById(R.id.highScoreText);
        timesPlayedText = (TextView) view.findViewById(R.id.examsWritten);
        avgScoreText = (TextView) view.findViewById(R.id.averageText);
        selectorExamStat = (Spinner) view.findViewById(R.id.profile_exam_choice);

        selectedExam = getResources().getStringArray(R.array.examChoices)[0];


        resetStats = (Button) view.findViewById(R.id.resetStats);
        resetStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

        spinnerAdapter = new AdapterChooseExam(getContext(), new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.examChoices))));
        selectorExamStat.setAdapter(spinnerAdapter);
        selectorExamStat.setOnItemSelectedListener(onItemClickListener);
        return view;
    }
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
    public static String getPoints(String name, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings_Money", MODE_PRIVATE);
        return sharedPreferences.getString(name, "");
    }

    public void loadandswitch(String exam) {
        graph.removeAllSeries();
        dataPoints = QuoteBank.getArray(getContext(), exam);
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

        // set manual X bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(highest);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(1);
        graph.getViewport().setMaxX(dataPoints.length);
        graph.addSeries(series);
    }
    public void resetStats() {
        QuoteBank.saveArray(getContext(), new int[0], selectedExam);
        getActivity().recreate();
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
    private void buttonAction(ImageButton button) {
        switch(button.getId()) {

        }
    }
    private ArrayList<String> strings(int[] a) {
        ArrayList<String> s = new ArrayList<String>();
        for (Integer d : a) {
            s.add(d.toString());
        }
        return s;
    }
}
