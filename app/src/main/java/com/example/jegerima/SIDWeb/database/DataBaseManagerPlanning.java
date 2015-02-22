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
public class DataBaseManagerPlanning {
    public static final  String TABLE_NAME = "planning";
    public static final  String CAP_ID = "cap_id";
    public static final  String CAP_NAME = "cap_name";
    public static final  String ID_COURSE = "course_id";
    public static final  String PLAN_ID = "plan_id";
    public static final  String PLAN_TITLE = "plan_title";
    public static final  String CONTENT_ID = "content_id";
    public static final  String CONTENT_TYPE = "content_type";
    public static final  String URL = "url";

    //Para FK
    public static final  String TABLE_FK="courses";
    public static final String FK_ID = "_id";

    private DbHelper helper;
    private SQLiteDatabase db;

    public static final String CREATE_TABLE = "create table "+TABLE_NAME+" ("
            + CAP_ID + " text not null,"
            + CAP_NAME + " text not null,"
            + PLAN_ID +" text not null,"
            + PLAN_TITLE +" text not null,"
            + ID_COURSE +" integer not null,"
            + CONTENT_ID +" text not null,"
            + CONTENT_TYPE +" text not null,"
            + URL +" text,"
            + " FOREIGN KEY("+ID_COURSE+") REFERENCES "+TABLE_FK+"("+FK_ID+"));";

    public DataBaseManagerPlanning(Context contexto) {
        helper = new DbHelper(contexto);
        db = helper.getWritableDatabase();
    }
    public ContentValues generarContentValues(String cap_id,String cap_name,String plan_id,String plan_name,String id_course,String content_id,String content_type,String url){
        ContentValues valores = new ContentValues();
        valores.put(CAP_ID,cap_id);
        valores.put(CAP_NAME,cap_name);
        valores.put(PLAN_ID,plan_id);
        valores.put(PLAN_TITLE,plan_name);
        valores.put(ID_COURSE,id_course);
        valores.put(CONTENT_ID,content_id);
        valores.put(CONTENT_TYPE,content_type);
        valores.put(URL,url);

        return valores;
    }
    public void insertar(String cap_id,String cap_name,String plan_id,String plan_name,String id_course,String content_id,String content_type,String url){
        //insert  into contactos
        db.insert(TABLE_NAME,null,generarContentValues(cap_id,cap_name,plan_id,plan_name,id_course,content_id,content_type,url));
    }
/*
    public Cursor consultar(String id){
        //insert  into contactos

        String[] campos = new String[] {TITLE, COURSE_NAME,CONTENT,DATE,NUM_MSG,ANNOUNCEMENTS_ID};
        String[] args = new String[] {id};
        //Cursor c = db.query(TABLE_NAME, campos, "usuario=?(where)", args(para el where), group by, having, order by, num);
        System.out.println("id del curso: "+id);
        if(id==null)return db.query(TABLE_NAME, campos, null, null, null, null, null);
        return db.query(TABLE_NAME, campos, ID_COURSE+"=?", args, null, null, null);
    }
*/
/*
    public Cursor consultar_anuncio(String id){
        //insert  into contactos

        String[] campos = new String[] {TITLE, COURSE_NAME,CONTENT,DATE,NUM_MSG,ANNOUNCEMENTS_ID};
        String[] args = new String[] {id};
        //Cursor c = db.query(TABLE_NAME, campos, "usuario=?(where)", args(para el where), group by, having, order by, num);
        return db.query(TABLE_NAME, campos, ANNOUNCEMENTS_ID+"=?", args, null, null, null);

    }

    */

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
