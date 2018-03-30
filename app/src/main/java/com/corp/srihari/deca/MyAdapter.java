package com.corp.srihari.deca;

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

public class MyAdapter extends BaseAdapter {

    Context context;
    ArrayList<ArrayList<String>> data;
    private static LayoutInflater inflater = null;

    public MyAdapter(Context context, ArrayList<ArrayList<String>> data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.d("TAG",data.toString());
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        vi = inflater.inflate(R.layout.row_wrong, null);
        TextView ques = (TextView) vi.findViewById(R.id.questionw);
        ques.setText(data.get(position).get(0));
        TextView optOne = (TextView) vi.findViewById(R.id.firstw);
        optOne.setText(data.get(position).get(1));
        TextView optTwo = (TextView) vi.findViewById(R.id.secondw);
        optTwo.setText(data.get(position).get(2));
        TextView optThree = (TextView) vi.findViewById(R.id.thirdw);
        optThree.setText(data.get(position).get(3));
        TextView optFour = (TextView) vi.findViewById(R.id.fourthw);
        optFour.setText(data.get(position).get(4));
        Log.d("POSITION",Integer.toString(position));
        if (data.get(position).get(6).equals("A")) {
            optOne.setTextColor(ContextCompat.getColor(context,R.color.red));
        }
        else if (data.get(position).get(6).equals("B")) {
            optTwo.setTextColor(ContextCompat.getColor(context,R.color.red));
        }
        else if (data.get(position).get(6).equals("C")) {
            optThree.setTextColor(ContextCompat.getColor(context,R.color.red));
        }
        else if (data.get(position).get(6).equals("D")) {
            optFour.setTextColor(ContextCompat.getColor(context,R.color.red));
        }

        if (data.get(position).get(5).equals("A")) {
            optOne.setTextColor(ContextCompat.getColor(context,R.color.green));
        }
        else if (data.get(position).get(5).equals("B")) {
            optTwo.setTextColor(ContextCompat.getColor(context,R.color.green));
        }
        else if (data.get(position).get(5).equals("C")) {
            Log.d("S",data.get(position).toString());
            optThree.setTextColor(ContextCompat.getColor(context,R.color.green));
        }
        else if (data.get(position).get(5).equals("D")) {
            optFour.setTextColor(ContextCompat.getColor(context,R.color.green));
        }


        return vi;
    }
}
