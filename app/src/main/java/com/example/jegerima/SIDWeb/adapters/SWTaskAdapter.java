package com.example.jegerima.SIDWeb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jegerima.SIDWeb.R;
import com.example.jegerima.SIDWeb.views.TaskView;

import java.util.ArrayList;

/**
 * Created by Cesar on 01/02/2015.
 */
public class SWTaskAdapter extends SWAdapter{
    Context context;

    public SWTaskAdapter(Context context, ArrayList<String []> users) {
        super(context, users);
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String[] datos=(String[])getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_personal_tasks, parent, false);
        }

        //obtengo cada uno de los TextView del view de mis tareas
        TextView titulo = (TextView) convertView.findViewById(R.id.lblTitulo);
        TextView materia = (TextView) convertView.findViewById(R.id.lblMateria);
        TextView estado = (TextView) convertView.findViewById(R.id.lblEstado);
        TextView fecha = (TextView) convertView.findViewById(R.id.lblFecha);
        TextView puntaje = (TextView) convertView.findViewById(R.id.lblPuntaje);

        //voy seteando el contenido de cada uno
        titulo.setText(datos[0]);
        materia.setText(datos[1]);
        estado.setText("");
        fecha.setText(datos[3]);
        puntaje.setText("");
        final String id = datos[4];


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent intent = new Intent(getContext(), NewsActivity.class);
                intent.putExtra("NewsID", id);
                intent.putExtra("Titulo", title);
                getContext().startActivity(intent);
                */
                System.out.println(id);
            }
        });


        // Return the completed view to render on screen
        return convertView;
    }




}