package com.example.jegerima.SIDWeb;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jegerima.SIDWeb.database.DataBaseManagerAnnouncements;
import com.example.jegerima.SIDWeb.database.MyConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class NewsActivity extends ActionBarActivity {

    private String AnuncioID;
    private String Titulo;
    private String Curso;
    private String CursoID;
    private String CursoCod;
    private String Contenido;
    private String Fecha;
    private String nComentarios;
    private ArrayList<String> listComentarios;

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
        ListView comentarios = (ListView) findViewById(R.id.listViewComents);
        listComentarios = new ArrayList<String>();
        contenido.setMovementMethod(LinkMovementMethod.getInstance());
        DataBaseManagerAnnouncements dbNews=null;
        try {

            dbNews = new DataBaseManagerAnnouncements(this);
            Cursor datos = dbNews.consultar_anuncio(AnuncioID);
            if (datos.moveToFirst()) {
                titulo.setText(datos.getString(0));
                materia.setText(datos.getString(1));
                contenido.setText(Html.fromHtml(datos.getString(2)));
                fecha.setText(datos.getString(3));
                mensajes.setText(datos.getString(4));
            }
            ConsultaComentarios c=new ConsultaComentarios();
            c.execute();


        }catch (Exception e){
            System.out.println(e.toString());
        }finally {
            if(dbNews!=null)
                dbNews.close();
        }
    }
    public ArrayList<String> consultar_comentarios() {
        MyConnection con = null;
        ResultSet rs = null;
        int id_comentario;
        //insertar(String id,String code, String name,String term,String teacher)
        String q_comentarios = "select de.id,de.message,de.user_id,de.created_at " +
                "from discussion_entries de " +
                "join discussion_topics dc on dc.id=de.discussion_topic_id " +
                "where dc.id=$PV_ID$ and de.parent_id is null;";
        try {

            con = new MyConnection();
            System.out.println(q_comentarios.replace("$PV_ID$",AnuncioID));
            if (con.getActive()) {
                System.out.println(q_comentarios.replace("$PV_ID$",AnuncioID));
                rs = con.consulta(q_comentarios.replace("$PV_ID$",AnuncioID));
                while (rs.next()) {
                    id_comentario = Integer.parseInt(rs.getString(1));
                    System.out.println(id_comentario);
                }

                rs.close();
                rs = null;

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

                if (con != null)
                    con.close();
                if (rs != null)
                    rs.close();
            } catch (SQLException ex) {
                //Logger.getLogger(consulta.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    public class ConsultaComentarios extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            listComentarios=consultar_comentarios();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }

        @Override
        protected void onCancelled() {

        }
    }
}
