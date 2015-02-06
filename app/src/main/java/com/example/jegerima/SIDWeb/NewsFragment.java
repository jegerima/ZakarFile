package com.example.jegerima.SIDWeb;

/**
 * Created by Jegerima on 22/01/2015.
 */
//import com.nhaarman.listviewanimations.appearance.*;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jegerima.SIDWeb.adapters.SWEffects;
import com.example.jegerima.SIDWeb.adapters.SWNewsAdapter;
import com.example.jegerima.SIDWeb.database.DataBaseManagerAnnouncements;

import com.example.jegerima.SIDWeb.views.NewsView;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;

import java.util.ArrayList;

public class NewsFragment extends Fragment {
    SWNewsAdapter dataAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View V = inflater.inflate(R.layout.fragment_news, container, false);
        final DynamicListView ll=(DynamicListView)V.findViewById(R.id.list_news);
        ArrayList<NewsView> list=new ArrayList<NewsView>();

        DataBaseManagerAnnouncements dbNews=null;
        try {

            dbNews = new DataBaseManagerAnnouncements(this.getActivity());
            Cursor datos = dbNews.consultar();
            if (datos.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    //y voy creando nuevos anuncios para luego irlos añadiendo a la lista
                    NewsView nv= new NewsView(this.getActivity());
                    nv.setParams(datos.getString(0), datos.getString(1), datos.getString(2), datos.getString(3),Integer.parseInt(datos.getString(4)));
                    list.add(nv);
                } while(datos.moveToNext());
            }

        }catch (Exception e){
            System.out.println(e.toString());
        }finally {
            if(dbNews!=null)
                dbNews.close();
        }


        //agrego el adaptador a mi ListView
        dataAdapter = new SWNewsAdapter(this.getActivity(), list);
        ll.setAdapter(dataAdapter);

        //agrego el efecto deseado
        SWEffects.animation(ll,dataAdapter,0,-1,1,0);//4 enteros del final son los efectos
                                                     //primero 1 efecto bottom
                                                     //segundo 1 para left -1 para right
                                                     //tercero 1 para efecto alpha
                                                     //cuarto 1 para efecto scale
        //SWEffects.Swipe(ll,dataAdapter);
        //SWEffects.SwipeUndoAnimacion(getActivity(), ll, dataAdapter,true,0,0,0,1);//el cuarto parametro es booleano y es para indicar si se desea auto desaparicion del undo


        return V;


    }


}