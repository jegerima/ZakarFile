package com.example.jegerima.SIDWeb.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jegerima.SIDWeb.R;
import com.example.jegerima.SIDWeb.views.TaskView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoAdapter;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Cesar on 01/02/2015.
 */
public class TaskAdapter extends ArrayAdapter<TaskView> implements UndoAdapter{
    Context context;

    public TaskAdapter(Context context, ArrayList<TaskView> users) {
        super(context, 0, users);
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TaskView task = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_personal_tasks, parent, false);
        }
        // Lookup view for data population
        TextView titulo = (TextView) convertView.findViewById(R.id.lblTitulo);
        TextView estado = (TextView) convertView.findViewById(R.id.lblEstado);
        TextView materia = (TextView) convertView.findViewById(R.id.lblMateria);
        TextView fecha = (TextView) convertView.findViewById(R.id.lblFecha);
        TextView puntaje = (TextView) convertView.findViewById(R.id.lblPuntaje);
        // Populate the data into the template view using the data object
        titulo.setText(task.getmTitulo().getText());
        estado.setText(task.getmEstado().getText());
        materia.setText(task.getmMateria().getText());
        fecha.setText(task.getmFecha().getText());
        puntaje.setText(task.getmPuntaje().getText());
        // Return the completed view to render on screen
        return convertView;
    }

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