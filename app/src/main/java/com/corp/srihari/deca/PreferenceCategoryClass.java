package com.corp.srihari.deca;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceCategory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by sriharivishnu on 2018-09-21.
 */

public class PreferenceCategoryClass extends PreferenceCategory {
    public PreferenceCategoryClass(Context context) {
        super(context);
    }

    public PreferenceCategoryClass(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PreferenceCategoryClass(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        TextView titleView = (TextView) view.findViewById(android.R.id.title);
        titleView.setTextColor(Color.BLACK);
        titleView.setTypeface(Typeface.DEFAULT_BOLD);
        titleView.setTextSize(17.0f);
    }
}

