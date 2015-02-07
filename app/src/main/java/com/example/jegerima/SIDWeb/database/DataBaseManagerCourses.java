package com.example.jegerima.SIDWeb.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by USUARIO on 30-ene-15.
 */
public class DataBaseManagerCourses {
    public static final  String TABLE_NAME="courses";
    public static final String COURSE_ID = "_id";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String TERM = "term";
    public static final String TEACHER = "teacher";


    private DbHelper helper;
    private SQLiteDatabase db;

    public static final String CREATE_TABLE = "create table "+TABLE_NAME+" ("
            + COURSE_ID + " integer primary key,"
            + CODE + " text null,"
            + NAME + " text not null,"
            + TERM + " text null,"
            + TEACHER + " text);";

    public DataBaseManagerCourses(Context contexto) {
        helper = new DbHelper(contexto);
        db = helper.getWritableDatabase();
    }
    public ContentValues generarContentValues(String id,String code,String name,String term,String teacher){
        ContentValues valores = new ContentValues();
        valores.put(COURSE_ID,id);
        valores.put(CODE,code);
        valores.put(NAME,name);
        valores.put(TERM,term);
        valores.put(TEACHER,teacher);
        return valores;
    }
    public void insertar(String id,String code, String name,String term,String teacher){
        //insert  into contactos
        db.insert(TABLE_NAME,null,generarContentValues(id,code,name,term,teacher));
    }

    public Cursor consultar(){

        String[] campos = new String[] {COURSE_ID, NAME, TEACHER};
        //String[] args = new String[] {"usu1"};

        //Cursor c = db.query(TABLE_NAME, campos, "usuario=?(where)", args(para el where), group by, having, order by, num);


        return db.query(TABLE_NAME, campos, null, null, null, null, NAME);
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
