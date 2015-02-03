package com.example.jegerima.SIDWeb;

/**
 * Created by Jegerima on 22/01/2015.
 */
//import com.nhaarman.listviewanimations.appearance.*;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.jegerima.SIDWeb.adapters.TaskAdapter;
import com.example.jegerima.SIDWeb.database.DataBaseManagerNotes;
import com.example.jegerima.SIDWeb.views.TaskView;
import com.nhaarman.listviewanimations.appearance.simple.ScaleInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.TimedUndoAdapter;

import java.util.ArrayList;

public class PersonalTaskFragment extends Fragment {
    TaskAdapter dataAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View V = inflater.inflate(R.layout.fragment_personal_task, container, false);
        final DynamicListView ll=(DynamicListView)V.findViewById(R.id.list_personal_task);
        ArrayList<TaskView> list=new ArrayList<TaskView>();

        try {

            DataBaseManagerNotes dbApuntes = new DataBaseManagerNotes(this.getActivity());
            Cursor datos = dbApuntes.consultar();
            System.out.println(datos.getColumnCount());
            if (datos.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    TaskView nv= new TaskView(this.getActivity());
                    nv.setParams(datos.getString(0), datos.getString(1), " ", datos.getString(3), 0, 0);
                    System.out.println(ll.getId());
                    list.add(nv);
                } while(datos.moveToNext());
            }

        }catch (Exception e){
            System.out.println(e.toString());
        }



        dataAdapter = new TaskAdapter(this.getActivity(), list);
        ll.setAdapter(dataAdapter);

        //Swipe(ll,dataAdapter);
        //animacion(ll,dataAdapter);
        //SwipeUndo(ll,dataAdapter);
        SwipeUndoAnimacion(ll,dataAdapter);


        return V;


    }

    public void animacion(DynamicListView list, ArrayAdapter adapter){
        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(adapter);
        //AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(adapter);
        animationAdapter.setAbsListView(list);
        list.setAdapter(animationAdapter);

    }

    public void Swipe(DynamicListView list, final ArrayAdapter adapter){

        Toast.makeText(getActivity()," asdasdas",Toast.LENGTH_SHORT).show();
        System.out.println(getActivity().getClass());
        list.enableSwipeToDismiss(
                new OnDismissCallback() {
                    @Override
                    public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions) {
                        for (int position : reverseSortedPositions) {
                            adapter.remove(adapter.getItem(position));
                        }
                    }
                }
        );
        list.setAdapter(adapter);
    }

    public void SwipeUndo(DynamicListView list,final TaskAdapter adapter){
        TimedUndoAdapter swipeUndoAdapter = null;
        //SimpleSwipeUndoAdapter swipeUndoAdapter = null;
        try {
            swipeUndoAdapter= new TimedUndoAdapter(adapter, getActivity(),
                    new OnDismissCallback() {
                        @Override
                        public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions) {
                            for (int position : reverseSortedPositions) {
                                adapter.remove(adapter.getItem(position));
                            }

                        }
                    }
            );
            swipeUndoAdapter.setAbsListView(list);
            list.setAdapter(swipeUndoAdapter);
            list.enableSimpleSwipeUndo();


        }catch (Exception e){
            System.out.println(e.toString());
        }
    }


    public void SwipeUndoAnimacion(DynamicListView list,final TaskAdapter adapter){

        TimedUndoAdapter swipeUndoAdapter = null;
        //SimpleSwipeUndoAdapter swipeUndoAdapter = null;
        try {
            swipeUndoAdapter= new TimedUndoAdapter(adapter, getActivity().getApplicationContext(),new OnDismissCallback() {
                @Override
                public void onDismiss(@NonNull ViewGroup viewGroup, @NonNull int[] reverseSortedPositions) {
                    for (int position : reverseSortedPositions) {

                        adapter.remove(adapter.getItem(position));
                        System.out.println("ahoraaaaaa");
                    }
                }
            }
            );
            swipeUndoAdapter.setAbsListView(list);
            ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(swipeUndoAdapter);
            //AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(swipeUndoAdapter);
            animationAdapter.setAbsListView(list);
            list.setAdapter(animationAdapter);
            list.enableSimpleSwipeUndo();


        }catch (Exception e){
            System.out.println(e.toString());
        }
    }




}
