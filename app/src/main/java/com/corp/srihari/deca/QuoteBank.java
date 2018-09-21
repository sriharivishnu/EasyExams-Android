package com.corp.srihari.deca;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Srihari Vishnu on 2018-02-16.
 */

public class QuoteBank {

    private Context mContext;

    public QuoteBank(Context context) {
        this.mContext = context;
    }

    public List<String> readLine(String path) {
        List<String> mLines = new ArrayList<>();

        AssetManager am = mContext.getAssets();

        try {
            InputStream is = am.open(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;

            while ((line = reader.readLine()) != null)
                mLines.add(line);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mLines;
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
    public void saveArray(ArrayList<String> array, String examName) {
        String strArr = "";
        for (int i=0; i<array.size(); i++) {
            strArr += array.get(i) + "@";
        }
        if (strArr.length() != 0) {
            strArr = strArr.substring(0, strArr.length() - 1);
        }

        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
        e.putString(examName, strArr);
        e.commit();
    }

    public ArrayList<String> getStringArray(String examName) {
        String[] strArr;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String str = prefs.getString(examName, null);
        if (str == null || str.equals("")) {
            return null;
        }
        else {
            strArr = str.split("@");
        }
        return new ArrayList<>(Arrays.asList(strArr));
    }

    public boolean isWrongAnswersEmpty() {
        return getStringArray("WrongAnswersQuestions") == null;
    }

    public ArrayList<String> getWrongAnswersQuestions() {
        return getStringArray("WrongAnswersQuestions");
    }
    public ArrayList<String> getWrongAnswersAnswers() {
        return getStringArray("WrongAnswersAnswers");
    }
    public void clear() {
        saveArray(new ArrayList<String>(),"WrongAnswersQuestions");
        saveArray(new ArrayList<String>(), "WrongAnswersAnswers");
    }
    public void saveWrongQuestions(ArrayList<String> toBeAddedQuestions, List<String> lines, List<String> answers, ArrayList<String> toBeRemoved) {
        ArrayList<String> a = getWrongAnswersQuestions();
        if (a == null) {
            a = new ArrayList<>();
        }
        for (String s : toBeAddedQuestions) {
            a.add(lines.get(Integer.parseInt(s)*5));
            a.add(lines.get(Integer.parseInt(s)*5+1));
            a.add(lines.get(Integer.parseInt(s)*5+2));
            a.add(lines.get(Integer.parseInt(s)*5+3));
            a.add(lines.get(Integer.parseInt(s)*5+4));
        }

        ArrayList<String> b = getWrongAnswersAnswers();
        if (b == null) {
            b = new ArrayList<>();
        }
        for (String s : toBeAddedQuestions) {
            b.add(answers.get(Integer.parseInt(s)));
        }

        if (!toBeRemoved.isEmpty()) {
            ArrayList<String> temp= new ArrayList<>();
            int i = 0;
            for (int x = 0; x < a.size(); x+=5) {

                if (!toBeRemoved.get(i).equals(Integer.toString(x/5))) {
                    for (int y = 0; y< 5; y++) {
                        temp.add(a.get(x+y));
                    }
                } else {
                    if (i+1 < toBeRemoved.size()) {
                        i++;
                    }
                }
            }
            a = new ArrayList<>(temp);
            temp.clear();
            i = 0;
            for (int x = 0; x < b.size(); x++) {
                if (!toBeRemoved.get(i).equals(Integer.toString(x))) {
                    temp.add(b.get(x));
                } else {
                    if (i+1 < toBeRemoved.size()) {
                        i++;
                    }
                }
            }
            b = new ArrayList<>(temp);
            temp.clear();
        }

        saveArray(a,"WrongAnswersQuestions");
        saveWrongAnswers(b);
        Log.d("questions",a.toString());
        Log.d("questionsans", b.toString());
    }
    private void saveWrongAnswers(ArrayList<String> toBeAddedAnswers) {
        saveArray(toBeAddedAnswers,"WrongAnswersAnswers");
    }


}
