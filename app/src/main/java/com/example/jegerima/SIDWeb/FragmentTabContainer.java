package com.example.jegerima.SIDWeb;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.example.jegerima.SIDWeb.views.NewsView;

import java.util.ArrayList;

/**
 * Created by Jegerima on 22/01/2015.
 */

public class FragmentTabContainer extends Fragment {
    private FragmentTabHost mTabHost;
    private View mRootView;
    private int SECTION_NUMBER;

    //Mandatory Constructor
    public FragmentTabContainer() {

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        SECTION_NUMBER = bd.getInt("section_number");
        System.out.println("Valor del bundle basico = " + SECTION_NUMBER);

        mRootView = inflater.inflate(R.layout.fragment_tabs,container, false);
        mTabHost = (FragmentTabHost)mRootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        switch(SECTION_NUMBER){
            case 1: //Inicio
                this.mTabHost.addTab(mTabHost.newTabSpec("fragmentA").setIndicator("Anuncios"), NewsFragment.class, null);
                this.mTabHost.addTab(mTabHost.newTabSpec("fragmentB").setIndicator("Tareas"), TasksFragment.class, null);
                this.mTabHost.addTab(mTabHost.newTabSpec("fragmentC").setIndicator("Mis Tareas"), PersonalTaskFragment.class, null);
                break;
            case 2: //Cursos
                this.mTabHost.addTab(mTabHost.newTabSpec("fragmentD").setIndicator("Anuncios"), NewsFragment.class, null);
                this.mTabHost.addTab(mTabHost.newTabSpec("fragmentE").setIndicator("Tareas"), TasksFragment.class, null);
                this.mTabHost.addTab(mTabHost.newTabSpec("fragmentF").setIndicator("Planificacion"), NewsFragment.class, null);
                break;
            case 3: //Tareas
                this.mTabHost.addTab(mTabHost.newTabSpec("fragmentG").setIndicator("A entregar"), TasksFragment.class, null);
                this.mTabHost.addTab(mTabHost.newTabSpec("fragmentH").setIndicator("Atrasadas"), TasksFragment.class, null);
                this.mTabHost.addTab(mTabHost.newTabSpec("fragmentI").setIndicator("Entregadas"), TasksFragment.class, null);
                break;
        }
        return mRootView;
    }

    public int getSectionNumber()
    {
        return this.SECTION_NUMBER;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(this.SECTION_NUMBER);
    }
}
