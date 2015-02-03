package com.example.jegerima.SIDWeb;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jegerima on 25/01/2015.
 */
public class TasksFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //ListAdapter la = new ArrayAdapter<String>();

        return inflater.inflate(R.layout.fragment_tasks, container, false);


    }
}
