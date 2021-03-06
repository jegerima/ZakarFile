package com.example.jegerima.SIDWeb.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jegerima.SIDWeb.NewsActivity;
import com.example.jegerima.SIDWeb.R;
import com.example.jegerima.SIDWeb.views.NewsView;

import java.util.ArrayList;

/**
 * Created by Cesar on 03/02/2015.
 */

public class SWNewsAdapter extends SWAdapter {
    Context context;

    public SWNewsAdapter(Context context, ArrayList<String[]> users) {
        super(context, users);
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        //NewsView news = (NewsView)getItem(position);
        String[] datos=(String[])getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_news, parent, false);
        }

        //obtengo cada uno de los TextView del view de mis tareas
        TextView titulo = (TextView) convertView.findViewById(R.id.lblTitulo);
        TextView materia = (TextView) convertView.findViewById(R.id.lblMateria);
        TextView contenido = (TextView) convertView.findViewById(R.id.lblContenido);
        TextView fecha = (TextView) convertView.findViewById(R.id.lblFecha);
        TextView mensajes = (TextView) convertView.findViewById(R.id.lblNMensajes);

        //voy seteando el contenido de cada uno
        titulo.setText(datos[0]);
        materia.setText(datos[1]);
        contenido.setText(Html.fromHtml(datos[2]).toString());
        fecha.setText(datos[3]);
        mensajes.setText("");//datos[4]);
        final String id=datos[5];
        final String title=datos[0];
        // Return the completed view to render on screen



        convertView.findViewById(R.id.frmNews).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NewsActivity.class);
                intent.putExtra("NewsID", id);
                intent.putExtra("Titulo", title);
                getContext().startActivity(intent);
            }
        });


        return convertView;
    }
}