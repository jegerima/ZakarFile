package com.example.jegerima.SIDWeb;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jegerima.SIDWeb.database.DataBaseManagerAnnouncements;

public class NewsActivity extends ActionBarActivity {

    private String AnuncioID;
    private String Titulo;
    private String Curso;
    private String CursoID;
    private String CursoCod;
    private String Contenido;
    private String Fecha;
    private String nComentarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Intent intent = getIntent();
        this.AnuncioID = intent.getStringExtra("NewsID");
        Toast.makeText(this,this.AnuncioID,Toast.LENGTH_SHORT);

        initData(this.AnuncioID );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initData(String id)
    {
        TextView titulo = (TextView) findViewById(R.id.lblTitulo);
        TextView materia = (TextView) findViewById(R.id.lblMateria);
        TextView contenido = (TextView) findViewById(R.id.lblContenido);
        TextView fecha = (TextView) findViewById(R.id.lblFecha);
        TextView mensajes = (TextView) findViewById(R.id.lblNMensajes);
/*
        DataBaseManagerNews dbNews=null;
        try
        {
            dbNews = new DataBaseManagerTask(this);
            Cursor dato = dbNews.consultar();
        }catch (Exception e)
        {
            System.out.println(e.toString());
        }finally {
            if(dbNews!=null)
                dbNews.close();
        }
*/
    }
}
