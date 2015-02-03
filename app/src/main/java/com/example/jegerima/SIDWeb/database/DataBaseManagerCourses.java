package com.example.jegerima.SIDWeb.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

/**
 * Created by USUARIO on 30-ene-15.
 */
public class DataBaseManagerCourses {
    public static final  String TABLE_NAME="cursos";
    public static final String CURSO_ID = "_id";
    public static final String CURSO_NOMBRE = "cursonombre";
    public static final String CURSO_PROFESOR = "cursoprofesor";


    private DbHelper helper;
    private SQLiteDatabase db;

    public static final String CREATE_TABLE = "create table "+TABLE_NAME+" ("
            + CURSO_ID + " integer primary key,"
            + CURSO_NOMBRE + " text not null,"
            + CURSO_PROFESOR + " text not null);";

    public DataBaseManagerCourses(Context contexto) {
        helper = new DbHelper(contexto);
        db = helper.getWritableDatabase();
    }
    public ContentValues generarContentValues(String id,String nombre_curso,String nombre_profesor){
        ContentValues valores = new ContentValues();

        valores.put(CURSO_ID,id);
        valores.put(CURSO_NOMBRE,nombre_curso);
        valores.put(CURSO_PROFESOR,nombre_profesor);
        return valores;
    }
    public void insertar(String id, String nombre_curso,String nombre_profesor){
        //insert  into contactos
        db.insert(TABLE_NAME,null,generarContentValues(id,nombre_curso,nombre_profesor));
    }

    public Cursor consultar(){

        String[] campos = new String[] {CURSO_ID,CURSO_NOMBRE,CURSO_PROFESOR};
        //String[] args = new String[] {"usu1"};

        //Cursor c = db.query(TABLE_NAME, campos, "usuario=?(where)", args(para el where), group by, having, order by, num);
        Cursor c = db.query(TABLE_NAME, campos, null, null, null, null, CURSO_NOMBRE);

        return c;
    }

    
    public void vaciar(){
        db.delete(TABLE_NAME,null,null);
    }
}
