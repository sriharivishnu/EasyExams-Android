package com.corp.srihari.deca;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> questions;
    private HashMap<String, ArrayList<String>> options;

    public ExpandableListAdapter(Context context, List<String> questions, HashMap<String, ArrayList<String>> options) {
        this.context = context;
        this.questions = questions;
        this.options = options;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return options.get(questions.get(listPosition)).get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.bank_item_row, null);
        }
        TextView option = (TextView) convertView.findViewById(R.id.firstb);

        option.setText(options.get(questions.get(listPosition)).get(expandedListPosition));

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return options.get(this.questions.get(listPosition)).size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return questions.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return questions.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.bank_question_row, null);
        }
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.questionb);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}