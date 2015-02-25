package com.example.jegerima.SIDWeb;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jegerima.SIDWeb.database.DataBaseManagerTask;
import com.example.jegerima.SIDWeb.fragmentsDialog.TextFragmentDialog;


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
        TextView contenido = (TextView) findViewById(R.id.txtContent);
        TextView fecha = (TextView) findViewById(R.id.lblFecha);
        TextView puntaje = (TextView) findViewById(R.id.lblPuntaje);
        contenido.setMovementMethod(LinkMovementMethod.getInstance());


        DataBaseManagerTask dbNews=null;

        try
        {
            System.out.println("Extrayendo tarea...");
            dbNews = new DataBaseManagerTask(this);
            Cursor dato = dbNews.consultar_tarea(TareaID);
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

        Button btn = (Button)findViewById(R.id.sbm_text);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("Ingrese el texto");
            }
        });

        btn = (Button)findViewById(R.id.sbm_url);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("Ingrese URL");
            }
        });

        btn = (Button)findViewById(R.id.sbm_gdrive);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilePickerUp();
            }
        });
    }

    public void showDialog(String msg)
    {
        FragmentManager fm = getSupportFragmentManager();
        TextFragmentDialog tfd = new TextFragmentDialog(4,msg);
        tfd.show(fm,"TAG");
    }

    public void FilePickerUp()
    {
        int request_code = 50;
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, request_code);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == 50 && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Toast.makeText(this,uri.toString(), Toast.LENGTH_SHORT).show();
                System.out.println(uri.toString());
            }
        }
    }
}
