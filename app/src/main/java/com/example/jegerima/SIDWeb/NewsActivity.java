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

        initData(this.AnuncioID);
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

        switch(id){
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                finish();
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

        DataBaseManagerAnnouncements dbNews=null;
        try {

            dbNews = new DataBaseManagerAnnouncements(this);
            Cursor datos = dbNews.consultar(AnuncioID);
            if (datos.moveToFirst()) {
                titulo.setText(datos.getString(0));
                materia.setText(datos.getString(1));
                contenido.setText(Html.fromHtml(datos.getString(2)));
                fecha.setText(datos.getString(3));
                mensajes.setText(datos.getString(4));
            }

        }catch (Exception e){
            System.out.println(e.toString());
        }finally {
            if(dbNews!=null)
                dbNews.close();
        }
    }
}
