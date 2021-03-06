package com.example.jegerima.SIDWeb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jegerima.SIDWeb.MainActivity;
import com.example.jegerima.SIDWeb.R;

import java.util.ArrayList;

/**
 * Created by Cesar on 07/02/2015.
 */
public class SWMenuAdapter extends ArrayAdapter<String[]> {

    Context context;
    public static int OPEN=View.GONE;//estado statico que indica si esta abierto o cerrado el panel de materias

    public SWMenuAdapter(Context context,ArrayList<String[]> list) {
        super(context,0,list);
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String[] datos=(String[])getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.menu_item, parent, false);
        }

        TextView titulo = (TextView) convertView.findViewById(R.id.title);
        String d=datos[0];
        String op=datos[1];

        //en caso que op sea diferente de -1 significa que es una opcion normal, si es -1 es una materia
        if(!op.equalsIgnoreCase("-1"))
            ((TextView) convertView.findViewById(R.id.id)).setText(op);
        else {//al ser materia usamos el campo oculto de nuetro layout para recuperar el id del curso
            ((TextView) convertView.findViewById(R.id.id)).setText("-" + datos[2]);//<--
            d="-"+d;
            convertView.findViewById(R.id.title).setVisibility(OPEN);
            ((TextView) convertView.findViewById(R.id.title)).setTextSize(12);
            convertView.setPadding(0, 0, 0, 0);
            convertView.findViewById(R.id.title).setBackgroundColor(R.color.background_material_dark);
            //System.out.println("contexto: "+context.getClass());
        }

        titulo.setText(d);

        return convertView;
    }
}
