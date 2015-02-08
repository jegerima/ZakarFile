package com.example.jegerima.SIDWeb;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jegerima.SIDWeb.database.DataBaseManagerTask;


public class TaskActivity extends ActionBarActivity {

    private String TareaID;
    private String Titulo;
    private String Curso;
    private String CursoID;
    private String CursoCod;
    private String Contenido;
    private String FechaInicial;
    private String FechaEntrega;
    private String FechaTope;
    private String Estado;
    private String nComentarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        Intent intent = getIntent();
        this.TareaID = intent.getStringExtra("NewsID");
        System.out.println("IDTask obtenido: "+TareaID);
        Toast.makeText(this, this.TareaID, Toast.LENGTH_SHORT);
        initData(TareaID);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task, menu);
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
        TextView contenido = (TextView) findViewById(R.id.txtContent);
        TextView fecha = (TextView) findViewById(R.id.lblFecha);
        TextView puntaje = (TextView) findViewById(R.id.lblPuntaje);

        DataBaseManagerTask dbNews=null;

        try
        {
            System.out.println("Extrayendo tarea...");
            dbNews = new DataBaseManagerTask(this);
            Cursor dato = dbNews.consultar(TareaID);
            if (dato.moveToFirst()) {
                //TITLE, COURSE_NAME,DESCRIPTION,STAR_DATE,FINAL_DATE,DEAD_LINE
                titulo.setText(dato.getString(0));
                materia.setText(dato.getString(1));
                contenido.setText(Html.fromHtml(dato.getString(2)));
                fecha.setText(dato.getString(3));
                puntaje.setText("10 de 10 puntos");
                System.out.println("Done");
            }
        }catch (Exception e)
        {
            System.out.println(e.toString());
        }finally {
            if(dbNews!=null)
                dbNews.close();
        }


    }
}
