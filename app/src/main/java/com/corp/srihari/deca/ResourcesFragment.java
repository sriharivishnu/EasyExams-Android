package com.corp.srihari.deca;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sriharivishnu on 2018-09-08.
 */

public class ResourcesFragment extends Fragment {
    private ListView resources_listview;
    private AdapterChooseExam adapterChooseExam;
    public static ResourcesFragment newInstance() {
        ResourcesFragment fragment = new ResourcesFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resources,container,false);
        resources_listview = (ListView) view.findViewById(R.id.resources_listview);
        ArrayList<String> choices = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.examChoices)));
        adapterChooseExam = new AdapterChooseExam(getContext(),choices);
        resources_listview.setAdapter(adapterChooseExam);

        resources_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), QuestionBankActivity.class);
                intent.putExtra("QuestionSet", i);
                startActivity(intent);
            }
        });
        return view;
    }
}
