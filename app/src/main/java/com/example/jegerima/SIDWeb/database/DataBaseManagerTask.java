package com.example.jegerima.SIDWeb.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.postgresql.core.Query;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by USUARIO on 05-feb-15.
 */
public class DataBaseManagerTask {
    public static final  String TABLE_NAME = "task";
    public static final  String TASK_ID = "_id";
    public static final  String ID_COURSE = "course_id";
    public static final  String COURSE_NAME = "course_name";
    public static final  String TITLE = "title";
    public static final  String DESCRIPTION = "description";
    public static final  String STAR_DATE = "star_date";
    public static final  String FINAL_DATE = "final_date";
    public static final  String DEAD_LINE = "deadline";
    public static final  String SUBMITTED = "submitted";

    //Para FK
    public static final  String TABLE_FK="courses";
    public static final String FK_ID = "_id";

    private DbHelper helper;
    private SQLiteDatabase db;

    public static final String CREATE_TABLE = "create table "+TABLE_NAME+" ("
            + TASK_ID + " integer primary key,"
            + ID_COURSE + " text not null,"
            + COURSE_NAME + " text not null,"
            + TITLE + " text,"
            + DESCRIPTION + " text,"
            + STAR_DATE +" TIMESTAMP NOT NULL,"
            + FINAL_DATE +" TIMESTAMP NULL,"
            + DEAD_LINE +" TIMESTAMP NULL,"
            + SUBMITTED + " TIMESTAMP NULL,"

            + " FOREIGN KEY("+ID_COURSE+") REFERENCES "+TABLE_FK+"("+FK_ID+"));";

    public DataBaseManagerTask(Context contexto) {
        helper = new DbHelper(contexto);
        db = helper.getWritableDatabase();
    }
    public ContentValues generarContentValues(String id,String id_curso,String nombre_curso,String tiulo,String descripcion,Date desde,Date hasta,Date atraso,Date submitted){
        ContentValues valores = new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        valores.put(TASK_ID,id);
        valores.put(ID_COURSE,id_curso);
        valores.put(COURSE_NAME,nombre_curso);
        valores.put(TITLE,tiulo);
        valores.put(DESCRIPTION,descripcion);
        valores.put(STAR_DATE,dateFormat.format(desde));
        if(hasta!=null)valores.put(FINAL_DATE,dateFormat.format(hasta));
        if(atraso!=null)valores.put(DEAD_LINE,dateFormat.format(atraso));
        if(submitted!=null)valores.put(SUBMITTED,dateFormat.format(submitted));

        return valores;
    }
    public void insertar(String id,String id_curso,String nombre_curso,String titulo,String descripcion,Date desde,Date hasta,Date atraso,Date submitted){
        //insert  into contactos
        db.insert(TABLE_NAME,null,generarContentValues(id,id_curso,nombre_curso,titulo,descripcion,desde,hasta,atraso,submitted));
    }


    public Cursor consultar(String id){
        //insert  into contactos

        String[] campos = new String[] {TASK_ID, TITLE, COURSE_NAME,DESCRIPTION,STAR_DATE,FINAL_DATE,DEAD_LINE};
        //Cursor c = db.query(TABLE_NAME, campos, "usuario=?(where)", args(para el where), group by, having, order by, num);

        String[] args = new String[] {id};

        if(id==null)return db.query(TABLE_NAME, campos, null, null, null, null, STAR_DATE+" desc");
        return db.query(TABLE_NAME, campos, ID_COURSE+"=?", args, null, null, STAR_DATE+" desc");
    }
    public Cursor consultarAEntregar(){
        String[] campos = new String[] {TASK_ID, TITLE, COURSE_NAME,DESCRIPTION,STAR_DATE,FINAL_DATE,DEAD_LINE};
        Date d=new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String[] args = new String[] {dateFormat.format(d)};
        //Cursor c = db.query(TABLE_NAME, campos, "usuario=?(where)", args(para el where), group by, having, order by, num);
        return db.query(TABLE_NAME, campos, SUBMITTED+" IS NULL and (final_date>? or final_date is null) ", args, null, null, STAR_DATE+" desc");
    }
    public Cursor consultarAtrasadas(){
        String[] campos = new String[] {TASK_ID, TITLE, COURSE_NAME,DESCRIPTION,STAR_DATE,FINAL_DATE,DEAD_LINE};
        Date d=new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String[] args = new String[] {dateFormat.format(d)};
        //Cursor c = db.query(TABLE_NAME, campos, "usuario=?(where)", args(para el where), group by, having, order by, num);
        return db.query(TABLE_NAME, campos, SUBMITTED+" IS NULL and final_date<? ", args, null, null, STAR_DATE+" desc");
    }
    public Cursor consultarEntregadas(){
        String[] campos = new String[] {TASK_ID, TITLE, COURSE_NAME,DESCRIPTION,STAR_DATE,FINAL_DATE,DEAD_LINE};
        //Cursor c = db.query(TABLE_NAME, campos, "usuario=?(where)", args(para el where), group by, having, order by, num);
        return db.query(TABLE_NAME, campos, SUBMITTED+" IS NOT NULL", null, null, null, STAR_DATE+" desc");
    }


    public Cursor consultar_tarea(String id){
        //insert  into contactos

        String[] campos = new String[] { TITLE, COURSE_NAME,DESCRIPTION,STAR_DATE,FINAL_DATE,DEAD_LINE};
        String[] args = new String[] {id};
        //Cursor c = db.query(TABLE_NAME, campos, "usuario=?(where)", args(para el where), group by, having, order by, num);

        return db.query(TABLE_NAME, campos, TASK_ID+"=?", args, null, null, null);

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
