package com.example.jegerima.SIDWeb.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jegerima.SIDWeb.NewsActivity;
import com.example.jegerima.SIDWeb.R;
import com.example.jegerima.SIDWeb.TaskActivity;
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
        final String[] datos=(String[])getItem(position);
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

        //0 id_task; 1 id_course; 2 course_name; 3 task_title; 4 task_description; 5 desde; 6 hasta; 7 tope
        //TASK_ID, TITLE, COURSE_NAME,DESCRIPTION,STAR_DATE,FINAL_DATE,DEAD_LINE
        //voy seteando el contenido de cada uno
        titulo.setText(datos[1]);
        materia.setText(datos[2]);
        estado.setText("");
        fecha.setText(datos[4]);
        puntaje.setText("");

        final String id_task = datos[0];
        final String title_task=datos[1];

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(datos[0]+" "+datos[1]+" "+datos[2]+" "+datos[3]+" "+datos[4]+" "+datos[5]+" "+datos[6]);
                Intent intent = new Intent(getContext(), TaskActivity.class);
                intent.putExtra("NewsID", id_task);
                intent.putExtra("Titulo", title_task);
                getContext().startActivity(intent);
                System.out.println("TaskID: "+id_task);
            }
        });


        // Return the completed view to render on screen
        return convertView;
    }




}