package com.example.jegerima.SIDWeb;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jegerima.SIDWeb.adapters.SWEffects;
import com.example.jegerima.SIDWeb.adapters.SWNewsAdapter;
import com.example.jegerima.SIDWeb.adapters.SWTaskAdapter;
import com.example.jegerima.SIDWeb.database.DataBaseManagerAnnouncements;
import com.example.jegerima.SIDWeb.database.DataBaseManagerTask;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;

import java.util.ArrayList;

/**
 * Created by Jegerima on 25/01/2015.
 */
public class TasksFragment extends Fragment {
    Cursor datos;
    String id_course=null,tipoBusqueda="";
    ArrayList<String[]> list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SWTaskAdapter dataAdapter;
        Bundle bd = getArguments();
        list=new ArrayList<String[]>();

        // Inflate the layout for this fragment


        if(bd!=null){
            id_course=bd.getString("course_id");
            tipoBusqueda=bd.getString("tipo_busqueda");
        }


        //ListAdapter la = new ArrayAdapter<String>();
        View V = inflater.inflate(R.layout.fragment_tasks, container, false);
        final DynamicListView ll=(DynamicListView)V.findViewById(R.id.list_tasks);
        //ArrayList<NewsView> list=new ArrayList<NewsView>();


        DataBaseManagerTask dbTasks=null;
        try {
            dbTasks = new DataBaseManagerTask(this.getActivity());
            //Consulta la base dependiendo el tipo de busqueda
            switch (tipoBusqueda){
                case "por_curso":
                    datos = dbTasks.consultar(id_course);
                    break;
                case "por_tareas_a_entregar":
                    System.out.println("ENTRA//////////////////");
                    datos = dbTasks.consultarAEntregar();
                    break;
                case "por_tareas_atrasadas":
                    System.out.println("ENTRA ATRASADAS//////////////////");
                    datos = dbTasks.consultarAtrasadas();
                    break;
                case "por_tareas_entregadas":
                    System.out.println("ENTRA ENTREGADAS//////////////////");

                    datos = dbTasks.consultarEntregadas();
                    break;
                default:
                    datos = dbTasks.consultar(id_course);
                    break;
            }



            if (datos.moveToFirst()) {

                //Recorremos el cursor hasta que no haya más registros
                do {
                    //y voy creando nuevos anuncios para luego irlos añadiendo a la lista
                    list.add(new String[]{datos.getString(0), datos.getString(1), datos.getString(2), datos.getString(3),datos.getString(4),datos.getString(5), datos.getString(6)});

                } while(datos.moveToNext());
            }

        }catch (Exception e){
            System.out.println(e.toString());
        }finally {
            if(dbTasks!=null)
                dbTasks.close();
        }

        //agrego el adaptador a mi ListView
        dataAdapter = new SWTaskAdapter(this.getActivity(), list);
        ll.setAdapter(dataAdapter);

        //agrego el efecto deseado
        SWEffects.animation(ll, dataAdapter, 0, -1, 1, 0);//4 enteros del final son los efectos
        //primero 1 efecto bottom
        //segundo 1 para left -1 para right
        //tercero 1 para efecto alpha
        //cuarto 1 para efecto scale
        //SWEffects.Swipe(ll,dataAdapter);
        //SWEffects.SwipeUndoAnimacion(getActivity(), ll, dataAdapter,true,0,0,0,1);//el cuarto parametro es booleano y es para indicar si se desea auto desaparicion del undo

        //return inflater.inflate(R.layout.fragment_tasks, container, false);
        return V;

    }

}
