package com.example.jegerima.SIDWeb.views;

/**
 * Created by Jegerima on 25/01/2015.
 */

import com.example.jegerima.SIDWeb.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Jegerima on 25/01/2015.
 */
public class TaskView extends FrameLayout {

    private TextView mTitulo;
    private TextView mMateria;
    private TextView mEstado;
    private TextView mFecha;
    private TextView mPuntaje;
    private ImageView mImagen;

    private Context mContext;
    private View mHeader;

    //private enum estado{'A Entegar',Entregado,Atrasado,};

    public TaskView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context,attrs,defStyle);
        initData(context);
    }

    public TaskView(Context context, AttributeSet attrs)
    {
        super(context,attrs);
        initData(context);
    }

    public TaskView(Context context)
    {
        super(context);
        initData(context);
    }

    private void initData(Context context){
        mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        mHeader = inflater.inflate(R.layout.view_tasks, this);
        mTitulo = (TextView)mHeader.findViewById(R.id.lblTitulo);
        mMateria = (TextView)mHeader.findViewById(R.id.lblMateria);
        mEstado = (TextView)mHeader.findViewById(R.id.lblEstado);
        mFecha = (TextView)mHeader.findViewById(R.id.lblFecha);
        mPuntaje = (TextView)mHeader.findViewById(R.id.lblPuntaje);
        mImagen = (ImageView)mHeader.findViewById(R.id.imgTarea);
    }

    public void setParams(String titulo, String materia, String estado, String fecha, int nota, int total){
        setTitulo(titulo);
        setMateria(materia);
        setEstado(estado);
        setFecha(fecha);
        setPuntaje(nota, total);
    }

    public void setTitulo(String titulo){
        mTitulo.setText(titulo);
    }

    public void setMateria(String materia)
    {
        mMateria.setText(materia);
    }

    public void setEstado(String contenido)
    {
        mEstado.setText(contenido);
    }

    public void setFecha(String fecha)
    {
        mFecha.setText(fecha);
    }

    public TextView getmTitulo() {
        return mTitulo;
    }

    public TextView getmMateria() {
        return mMateria;
    }

    public TextView getmEstado() {
        return mEstado;
    }

    public TextView getmFecha() {
        return mFecha;
    }

    public TextView getmPuntaje() {
        return mPuntaje;
    }

    public void setPuntaje(int nota, int total)
    {
        if(nota>=0 && total>=0)
            if(total==1)
                mPuntaje.setText(nota+" de "+total+" punto");
            else
                mPuntaje.setText(nota+" de "+total+" puntos");
        else

            mPuntaje.setText("0 de 0 puntos");
    }
}

