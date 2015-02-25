package com.example.jegerima.SIDWeb;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jegerima.SIDWeb.database.DataBaseManagerAnnouncements;
import com.example.jegerima.SIDWeb.database.MyConnection;
import com.example.jegerima.SIDWeb.widget.AnimatedExpandableListView;
import com.example.jegerima.SIDWeb.widget.AnimatedGifImageView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends ActionBarActivity {

    private String AnuncioID;
    private String Titulo;
    private String Curso;
    private String CursoID;
    private String CursoCod;
    private String Contenido;
    private String Fecha;
    private String nComentarios;
    private ArrayList<String[]> listComentarios=new ArrayList<>();
    private AnimatedExpandableListView listView;
    private ExampleAdapter adapter;
    private AnimatedGifImageView cargando;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Intent intent = getIntent();
        this.AnuncioID = intent.getStringExtra("NewsID");
        Toast.makeText(this,this.AnuncioID,Toast.LENGTH_SHORT);
        //lista de comentarios
        listView = (AnimatedExpandableListView) findViewById(R.id.listViewComents);
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.
                if (listView.isGroupExpanded(groupPosition)) {
                    listView.collapseGroupWithAnimation(groupPosition);
                } else {
                    listView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }

        });
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
        cargando = (AnimatedGifImageView) findViewById(R.id.cargando);
        cargando.setAnimatedGif(R.drawable.cargando,AnimatedGifImageView.TYPE.FIT_CENTER);

        listComentarios = new ArrayList<String[]>();


        contenido.setMovementMethod(LinkMovementMethod.getInstance());
        DataBaseManagerAnnouncements dbNews=null;
        try {

            dbNews = new DataBaseManagerAnnouncements(this);
            Cursor datos = dbNews.consultar_anuncio(AnuncioID);
            if (datos.moveToFirst()) {
                titulo.setText(datos.getString(0));
                materia.setText(datos.getString(1));
                contenido.setText(Html.fromHtml(datos.getString(2).replaceAll("href=\"/", "href=\"https://www.sidweb.espol.edu.ec/")));
                fecha.setText(datos.getString(3));
                mensajes.setText("");//datos.getString(4));
            }
            ConsultaComentarios c=new ConsultaComentarios(this);
            c.execute();



        }catch (Exception e){
            System.out.println(e.toString());
        }finally {
            if(dbNews!=null)
                dbNews.close();
        }

    }
    public void consultar_comentarios() {
        MyConnection con = null;
        ResultSet rs = null;
        listComentarios.clear();

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
                    //list.add(new String[]{datos.getString(0), datos.getString(1), datos.getString(2), datos.getString(3),datos.getString(4),datos.getString(5)});
                    //Rs devuelve: id->comentario, mensaje, id->nombre, fecha de creado
                    listComentarios.add(new String[]{rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)});
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
        System.out.println(listComentarios.size());
        //Llena el activity


    }
    public class ConsultaComentarios extends AsyncTask<String, Void, String> {
        Context contexto;

        public ConsultaComentarios(Context c){
            contexto=c;
        }
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            consultar_comentarios();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            List<GroupItem> items = new ArrayList<GroupItem>();
            GroupItem item = new GroupItem();

            if(listComentarios.size()==0) item.title = "No hay comentarios ";
            else{
                item.title = "Ver ("+listComentarios.size()+") comentario(s)";

            }
            for(int j = 0; j < listComentarios.size(); j++) {
                ChildItem child = new ChildItem();
                //0:id->comentario, 1:mensaje, 2:id->estudiante,3:fecha de creado
                child.nombre = "Usuario: "+listComentarios.get(j)[2];
                child.mensaje = listComentarios.get(j)[1];
                child.publicacion = listComentarios.get(j)[3];
                item.items.add(child);

            }

            items.add(item);
            GroupItem item2 = new GroupItem();
            //items.add(item2);


            cargando.setVisibility(View.GONE);
            adapter = new ExampleAdapter(contexto);

            adapter.setData(items);


            listView.setAdapter(adapter);

            // In order to show animations, we need to use a custom click handler
            // for our ExpandableListView.

        }

        @Override
        protected void onCancelled() {

        }
    }

    //Clases internas para crear lista expandible

    private static class GroupItem {
        String title;
        List<ChildItem> items = new ArrayList<ChildItem>();
    }

    private static class ChildItem {
        String nombre;
        String mensaje;
        String publicacion;
    }

    private static class ChildHolder {
        TextView nombre;
        TextView mensaje;
        TextView publicaion;
    }

    private static class GroupHolder {
        TextView title;
    }

    /**
     * Adapter for our list of {@link GroupItem}s.
     */
    private class ExampleAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
        private LayoutInflater inflater;

        private List<GroupItem> items;

        public ExampleAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setData(List<GroupItem> items) {
            this.items = items;
        }

        @Override
        public ChildItem getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).items.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder holder;
            ChildItem item = getChild(groupPosition, childPosition);
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = inflater.inflate(R.layout.comments_list_item, parent, false);
                holder.nombre = (TextView) convertView.findViewById(R.id.usuario);
                holder.mensaje = (TextView) convertView.findViewById(R.id.mensaje);
                holder.publicaion = (TextView) convertView.findViewById(R.id.fecha_pub);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }

            holder.nombre.setText(item.nombre);
            holder.mensaje.setText(Html.fromHtml(item.mensaje.replaceAll("href=\"/", "href=\"https://www.sidweb.espol.edu.ec/")));
            holder.publicaion.setText(item.publicacion);

            holder.mensaje.setMovementMethod(LinkMovementMethod.getInstance());

            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return items.get(groupPosition).items.size();
        }

        @Override
        public GroupItem getGroup(int groupPosition) {
            return items.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHolder holder;
            GroupItem item = getGroup(groupPosition);
            if (convertView == null) {
                holder = new GroupHolder();
                convertView = inflater.inflate(R.layout.comments_group_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.textTitle);
                convertView.setTag(holder);

            } else {
                holder = (GroupHolder) convertView.getTag();
            }

            holder.title.setText(item.title);
            if(item.title==null) convertView.setVisibility(View.INVISIBLE);
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }

    }
}
