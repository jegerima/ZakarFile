package com.example.jegerima.SIDWeb.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import com.example.jegerima.SIDWeb.MainActivity;
import com.example.jegerima.SIDWeb.database.DataBaseManagerAnnouncements;
import com.example.jegerima.SIDWeb.database.DataBaseManagerNotes;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.ScaleInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingLeftInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingRightInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.SimpleSwipeUndoAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.SwipeUndoAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.TimedUndoAdapter;

/**
 * Created by Cesar on 04/02/2015.
 */
public class SWEffects {

    //efectos para aparicion de los elementos en los listview
    public static void animation(DynamicListView list, BaseAdapter adapter, int bottom, int left_rigth, int alpha, int scale){
        if(bottom==1)adapter = new SwingBottomInAnimationAdapter(adapter);
        if(left_rigth==1)adapter = new SwingLeftInAnimationAdapter(adapter);
        if(left_rigth==-1)adapter = new SwingRightInAnimationAdapter(adapter);
        if(alpha==1)adapter = new AlphaInAnimationAdapter(adapter);
        if(scale==1)adapter = new ScaleInAnimationAdapter(adapter);

        ((AnimationAdapter)adapter).setAbsListView(list);
        list.setAdapter(adapter);
    }


    // funcion que permite que los elementos del listview se puedan elimitar deslizandolos hacia un lado
    public static void Swipe(DynamicListView list, final ArrayAdapter adapter){
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

    //igual q la funcion anterior, pero permite desahce y contiene animaciones
    public static void SwipeUndoAnimacion(Context context, DynamicListView list,final SWAdapter adapter, boolean auto_discard, int bottom, int left_right, int alpha, int scale){

        SwipeUndoAdapter swipeUndoAdapter = null;
        try {
            if(auto_discard)
                swipeUndoAdapter= new TimedUndoAdapter(adapter, context.getApplicationContext(),new OnDismissCallback() {
                    @Override
                    public void onDismiss(@NonNull ViewGroup viewGroup, @NonNull int[] reverseSortedPositions) {
                        undoAction(adapter,viewGroup,reverseSortedPositions);
                    }
                });
            else
                swipeUndoAdapter= new SimpleSwipeUndoAdapter(adapter, context.getApplicationContext(),new OnDismissCallback() {
                    @Override
                    public void onDismiss(@NonNull ViewGroup viewGroup, @NonNull int[] reverseSortedPositions) {
                        undoAction(adapter,viewGroup,reverseSortedPositions);
                    }
                });

            swipeUndoAdapter.setAbsListView(list);
            animation(list,swipeUndoAdapter,bottom,left_right,alpha,scale);
            list.enableSimpleSwipeUndo();


        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    //accion que se realiza luego de borrado el elemento del listview
    public static void undoAction(SWAdapter adapter ,@NonNull ViewGroup viewGroup,@NonNull int[] reverseSortedPositions){
        for (int position : reverseSortedPositions) {
            DataBaseManagerNotes dbNotes = new DataBaseManagerNotes(adapter.getContext());
            dbNotes.borrar(((String[])adapter.getItem(position))[0]);
            adapter.remove(adapter.getItem(position));
        }
    }

}
