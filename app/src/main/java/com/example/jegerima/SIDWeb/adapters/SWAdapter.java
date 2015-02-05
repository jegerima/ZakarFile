package com.example.jegerima.SIDWeb.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.jegerima.SIDWeb.R;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoAdapter;

import java.util.ArrayList;

/**
 * Created by Cesar on 04/02/2015.
 */
public class SWAdapter  extends ArrayAdapter implements UndoAdapter {
    Context context;

    public SWAdapter(Context context, ArrayList users) {
        super(context, 0, users);
        this.context=context;
    }
    /*
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }*/

    @NonNull
    @Override
    public View getUndoView(final int position, final View convertView, @NonNull final ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.undo_view, parent, false);
            //view = Inflater.inflate(R.layout.view_tasks, parent, false);
        }
        return view;
    }

    @NonNull
    @Override
    public View getUndoClickView(@NonNull View view) {
        return view.findViewById(R.id.undo_section);
    }


    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public long getItemId(final int position) {
        return getItem(position).hashCode();
    }

}
