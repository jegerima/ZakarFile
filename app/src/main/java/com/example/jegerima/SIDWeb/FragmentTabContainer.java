package com.example.jegerima.SIDWeb;

import android.app.ActionBar;
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
        Bundle bargs = new Bundle();
        SECTION_NUMBER = bd.getInt("section_number");
        System.out.println("Valor del bundle basico = " + SECTION_NUMBER);

        mRootView = inflater.inflate(R.layout.fragment_tabs,container, false);
        mTabHost = (FragmentTabHost)mRootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);


        switch(SECTION_NUMBER){
            case 1: //Inicio
                this.mTabHost.addTab(mTabHost.newTabSpec("fragmentA").setIndicator("Anuncios"), NewsFragment.class, null);
                this.mTabHost.addTab(mTabHost.newTabSpec("fragmentB").setIndicator("Tareas"), TasksFragment.class, null);
                this.mTabHost.addTab(mTabHost.newTabSpec("fragmentC").setIndicator("Apuntes"), PersonalTaskFragment.class, null);
                break;
            case 2: //Cursos


                bargs.putString("course_id",bd.getInt("course_id")+"");
                bargs.putString("tipo_busqueda","por_curso");

                this.mTabHost.addTab(mTabHost.newTabSpec("fragmentD").setIndicator("Anuncios"), NewsFragment.class, bargs);
                this.mTabHost.addTab(mTabHost.newTabSpec("fragmentE").setIndicator("Tareas"), TasksFragment.class, bargs);
                this.mTabHost.addTab(mTabHost.newTabSpec("fragmentF").setIndicator("Plan"), PlanningFragment.class,bargs );
                //System.out.println(bd.getInt("course_id"));
                break;
            case 3: //Tareas

                bargs=new Bundle();
                bargs.putString("tipo_busqueda","por_tareas_a_entregar");
                this.mTabHost.addTab(mTabHost.newTabSpec("fragmentG").setIndicator("A entregar"), TasksFragment.class,bargs );
                bargs=new Bundle();
                bargs.putString("tipo_busqueda","por_tareas_atrasadas");
                this.mTabHost.addTab(mTabHost.newTabSpec("fragmentH").setIndicator("Atrasadas"), TasksFragment.class, bargs);
                bargs=new Bundle();
                bargs.putString("tipo_busqueda","por_tareas_entregadas");
                this.mTabHost.addTab(mTabHost.newTabSpec("fragmentI").setIndicator("Entregadas"), TasksFragment.class, bargs);
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
