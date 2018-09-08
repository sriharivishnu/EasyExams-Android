package com.corp.srihari.deca;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterChooseExam extends BaseAdapter {

    private Context context;
    private ArrayList<String> data;
    private static LayoutInflater inflater = null;

    public AdapterChooseExam(Context context, ArrayList<String> data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size()+1;
    }

    @Override
    public String getItem(int position) {
        // TODO Auto-generated method stub
        if (position<data.size()) {
            return data.get(position);
        } else {
            return "add";
        }
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stubp
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (position == data.size()) {
            vi = inflater.inflate(R.layout.row_add_exam,null);
        } else {
            vi = inflater.inflate(R.layout.row_choose_exam, null);
            TextView choice = (TextView) vi.findViewById(R.id.examChoice);
            choice.setText(data.get(position));
        }
        return vi;
    }
}
