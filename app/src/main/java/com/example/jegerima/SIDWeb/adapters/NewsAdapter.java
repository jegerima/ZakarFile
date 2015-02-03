package com.example.jegerima.SIDWeb.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jegerima.SIDWeb.R;
import com.example.jegerima.SIDWeb.views.NewsView;
import com.example.jegerima.SIDWeb.views.TaskView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoAdapter;

import java.util.ArrayList;

/**
 * Created by Cesar on 03/02/2015.
 */

public class NewsAdapter extends ArrayAdapter<NewsView> implements UndoAdapter {
    Context context;

    public NewsAdapter(Context context, ArrayList<NewsView> users) {
        super(context, 0, users);
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        NewsView news = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_news, parent, false);
        }
        // Lookup view for data population
        TextView titulo = (TextView) convertView.findViewById(R.id.lblTitulo);
        TextView materia = (TextView) convertView.findViewById(R.id.lblMateria);
        TextView contenido = (TextView) convertView.findViewById(R.id.lblContenido);
        TextView fecha = (TextView) convertView.findViewById(R.id.lblFecha);
        TextView mensajes = (TextView) convertView.findViewById(R.id.lblNMensajes);
        // Populate the data into the template view using the data object
        titulo.setText(news.getmTitulo().getText());
        materia.setText(news.getmMateria().getText());
        contenido.setText(news.getmContenido().getText());
        fecha.setText(news.getmFecha().getText());
        mensajes.setText(news.getmNMensajes().getText());
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