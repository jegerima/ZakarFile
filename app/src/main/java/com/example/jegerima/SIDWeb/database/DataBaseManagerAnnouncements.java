package com.example.jegerima.SIDWeb.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by USUARIO on 05-feb-15.
 */
public class DataBaseManagerAnnouncements {
    public static final  String TABLE_NAME = "announcements";
    public static final  String ANNOUNCEMENTS_ID = "_id";
    public static final  String ID_COURSE = "course_id";
    public static final  String COURSE_NAME = "course_name";
    public static final  String TITLE = "title";
    public static final  String CONTENT = "content";
    public static final  String DATE = "date";
    public static final  String NUM_MSG = "num_msgs";

    //Para FK
    public static final  String TABLE_FK="cursos";
    public static final String FK_ID = "_id";

    private DbHelper helper;
    private SQLiteDatabase db;

    public static final String CREATE_TABLE = "create table "+TABLE_NAME+" ("
            + ANNOUNCEMENTS_ID + " integer primary key autoincrement,"
            + ID_COURSE + " text not null,"
            + COURSE_NAME +" text,"
            + TITLE +" text,"
            + CONTENT +" text,"
            + DATE +" TIMESTAMP NOT NULL,"
            + NUM_MSG +" text,"
            + " FOREIGN KEY("+ID_COURSE+") REFERENCES "+TABLE_FK+"("+FK_ID+"));";

    public DataBaseManagerAnnouncements(Context contexto) {
        helper = new DbHelper(contexto);
        db = helper.getWritableDatabase();
    }
    public ContentValues generarContentValues(String id,String id_curso,String nombre_curso,String titulo,String contenido,Date fecha,String num_msgs){
        ContentValues valores = new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        valores.put(ANNOUNCEMENTS_ID,id);
        valores.put(ID_COURSE,id_curso);
        valores.put(COURSE_NAME,nombre_curso);
        valores.put(TITLE,titulo);
        valores.put(CONTENT,contenido);
        valores.put(DATE,dateFormat.format(fecha));
        valores.put(NUM_MSG,num_msgs);

        return valores;
    }
    public void insertar(String id,String id_curso,String nombre_curso,String titulo,String contenido,Date fecha,String num_msgs){
        //insert  into contactos
        db.insert(TABLE_NAME,null,generarContentValues(id,id_curso,nombre_curso,titulo,contenido,fecha,num_msgs));
    }
    public Cursor consultar(){
        //insert  into contactos

        String[] campos = new String[] {TITLE, COURSE_NAME,CONTENT,DATE,NUM_MSG,ANNOUNCEMENTS_ID};
        //Cursor c = db.query(TABLE_NAME, campos, "usuario=?(where)", args(para el where), group by, having, order by, num);

        return db.query(TABLE_NAME, campos, null, null, null, null, null);
    }

    public Cursor consultar(String id){
        //insert  into contactos

        String[] campos = new String[] {TITLE, COURSE_NAME,CONTENT,DATE,NUM_MSG,ANNOUNCEMENTS_ID};
        String[] args = new String[] {id};
        //Cursor c = db.query(TABLE_NAME, campos, "usuario=?(where)", args(para el where), group by, having, order by, num);

        return db.query(TABLE_NAME, campos, ANNOUNCEMENTS_ID+"=?", args, null, null, null);

    }
    public void vaciar(){
        db.delete(TABLE_NAME,null,null);
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
