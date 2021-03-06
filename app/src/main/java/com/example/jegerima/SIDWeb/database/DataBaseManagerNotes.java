package com.example.jegerima.SIDWeb.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by USUARIO on 29-ene-15.
 */
public class DataBaseManagerNotes {

    public static final  String TABLE_NAME="apuntes";
    public static final String CN_ID = "_id";
    public static final String CN_TITULO = "titulo";
    public static final String CN_MATERIA_ID = "materia_id";
    public static final String CN_CONTENIDO = "contenido";
    public static final String CN_FECHA_INICIAL = "fechaincio";
    public static final String CN_FECHA_FINAL = "fechafinal";

    //Para FK
    public static final  String TABLE_FK="courses";
    public static final String FK_ID = "_id";



    private DbHelper helper;
    private SQLiteDatabase db;

    public static final String CREATE_TABLE = "create table "+TABLE_NAME+" ("
            + CN_ID + " integer primary key autoincrement,"
            + CN_TITULO +" text,"
            + CN_MATERIA_ID + " text not null,"
            + CN_CONTENIDO +" text,"
            + CN_FECHA_INICIAL +" TIMESTAMP NOT NULL,"
            + CN_FECHA_FINAL +" TIMESTAMP NULL,"
            + " FOREIGN KEY("+CN_MATERIA_ID+") REFERENCES "+TABLE_FK+"("+FK_ID+"));";



    public DataBaseManagerNotes(Context contexto){
        helper = new DbHelper(contexto);
        db = helper.getWritableDatabase();
    }
    public ContentValues generarContentValues(String titulo,String materia,String contenido,Date fecha_final){
        ContentValues valores = new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        valores.put(CN_TITULO,titulo);
        valores.put(CN_MATERIA_ID,materia);
        valores.put(CN_CONTENIDO,contenido);
        valores.put(CN_FECHA_INICIAL,dateFormat.format(date));
        if(fecha_final!=null)valores.put(CN_FECHA_FINAL,dateFormat.format(fecha_final));
        return valores;
    }
    public void insertar(String titulo,String materia,String contenido,Date fecha_final){
        //insert  into contactos
        db.insert(TABLE_NAME,null,generarContentValues(titulo,materia,contenido,fecha_final));
    }

    public Cursor consultar(){
        //insert  into contactos

        String QB=TABLE_NAME +
                " LEFT JOIN " + TABLE_FK + " ON " +
                TABLE_NAME+"."+CN_MATERIA_ID + " = " + TABLE_FK+"."+FK_ID;

        String[] campos = new String[] {TABLE_NAME+"."+CN_ID,CN_TITULO, TABLE_FK+"."+DataBaseManagerCourses.NAME,CN_CONTENIDO,CN_FECHA_FINAL};
        //String[] args = new String[] {"usu1"};

        //Cursor c = db.query(TABLE_NAME, campos, "usuario=?(where)", args(para el where), group by, having, order by, num);

        return db.query(QB, campos, null, null, null, null, CN_FECHA_FINAL);
    }

    public void vaciar(){
        db.delete(TABLE_NAME,null,null);
    }

    public void borrar(String id){
        db.delete(TABLE_NAME,"_id=?",new String[]{id});
    }

    public void close(){
        try {
            if(helper!=null){
                helper.close();
                helper=null;
            }

            if(db!=null){
                db.close();
                db=null;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
