package com.example.jegerima.SIDWeb.views;

/**
 * Created by Jegerima on 25/01/2015.
 */

import com.example.jegerima.SIDWeb.R;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Jegerima on 25/01/2015.
 */
public class NewsView extends FrameLayout {

    private TextView mTitulo;
    private TextView mMateria;
    private TextView mContenido;
    private TextView mFecha;
    private TextView mNMensajes;
    private ImageView mImagen;

    private Context mContext;
    private View mHeader;

    public NewsView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context,attrs,defStyle);
        initData(context);
    }

    public NewsView(Context context, AttributeSet attrs)
    {
        super(context,attrs);
        initData(context);
    }

    public NewsView(Context context)
    {
        super(context);
        initData(context);
    }

    private void initData(Context context){
        mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        mHeader = inflater.inflate(R.layout.view_news, this);
        mTitulo = (TextView)mHeader.findViewById(R.id.lblTitulo);
        mMateria = (TextView)mHeader.findViewById(R.id.lblMateria);
        mContenido = (TextView)mHeader.findViewById(R.id.lblContenido);
        mFecha = (TextView)mHeader.findViewById(R.id.lblFecha);
        mNMensajes = (TextView)mHeader.findViewById(R.id.lblNMensajes);
        mImagen = (ImageView)mHeader.findViewById(R.id.imgAnuncio);
    }

    public void setParams(String titulo, String materia, String contenido, String fecha, int mensajes){
        setTitulo(titulo);
        setMateria(materia);
        setContenido(contenido);
        setFecha(fecha);
        setMensajes(mensajes);
    }

    public TextView getmTitulo() {
        return mTitulo;
    }

    public TextView getmMateria() {
        return mMateria;
    }

    public TextView getmContenido() {
        return mContenido;
    }

    public TextView getmFecha() {
        return mFecha;
    }

    public TextView getmNMensajes() {
        return mNMensajes;
    }

    public ImageView getmImagen() {
        return mImagen;
    }

    public Context getmContext() {
        return mContext;
    }

    public View getmHeader() {
        return mHeader;
    }

    public void setTitulo(String titulo){
        mTitulo.setText(titulo);
    }
    public void setMateria(String materia)
    {
        mMateria.setText(materia);
    }

    public void setContenido(String contenido)
    {
        mContenido.setText(Html.fromHtml(contenido));
    }

    public void setFecha(String fecha)
    {
        mFecha.setText(fecha);
    }

    public void setMensajes(int mensajes)
    {
        if(mensajes>=0)
            if(mensajes==1)
                mNMensajes.setText(mensajes + " comentario");
            else
                mNMensajes.setText(mensajes + " comentarios");
        else
            mNMensajes.setText("0 comentarios");
    }
}

