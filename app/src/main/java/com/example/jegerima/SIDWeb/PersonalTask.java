package com.example.jegerima.SIDWeb;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.support.v7.app.ActionBarActivity;

import com.example.jegerima.SIDWeb.Objetos.Lista;
import com.example.jegerima.SIDWeb.database.DataBaseManagerCourses;
import com.example.jegerima.SIDWeb.database.DataBaseManagerNotes;
import com.example.jegerima.SIDWeb.timedialogfragment.DatePickerFragment;
import com.example.jegerima.SIDWeb.timedialogfragment.TimePickerFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class PersonalTask extends ActionBarActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private DataBaseManagerNotes dbApuntes;
    private Cursor cursor;

    private EditText titulo;
    private Spinner materia;
    private EditText fecha_venc_apunte;
    private EditText hora_venc_apunte;
    //Para obtener fecha actual

    private EditText apuntes;
    //Para obtener fecha actual
    ContentValues valores = new ContentValues();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    Date date = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_task);
        dbApuntes = new DataBaseManagerNotes(this);

        titulo = (EditText) findViewById(R.id.titulo_apunte);
        materia = (Spinner) findViewById(R.id.materia_apunte);
        apuntes = (EditText) findViewById(R.id.contenido_apunte);
        materia = (Spinner) findViewById(R.id.materia_apunte);

        DataBaseManagerCourses courses=null;
        List<Lista> list = new ArrayList<Lista>();

        try {
            courses=new DataBaseManagerCourses(this);
            Cursor c = courses.consultar();
            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    list.add(new Lista(c.getString(0),c.getString(1)));
                } while(c.moveToNext());
            }

        }catch (Exception e){
            System.out.println(e.toString());
        }finally {
            if(courses!=null)
                courses.close();
        }



        ArrayAdapter<Lista> dataAdapter = new ArrayAdapter<Lista>(this,android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        materia.setAdapter(dataAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        fecha_venc_apunte = (EditText) findViewById(R.id.fecha_final_apunte);
        hora_venc_apunte = (EditText) findViewById(R.id.hora_final_apunte);


        fecha_venc_apunte.setText(dateFormat.format(date));
        fecha_venc_apunte.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Always use a TextKeyListener when clearing a TextView to prevent android
                    // warnings in the log
                    showDatePickerDialog(v);

                }
            }
        });

        hora_venc_apunte.setText(timeFormat.format(date));
        hora_venc_apunte.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Always use a TextKeyListener when clearing a TextView to prevent android
                    // warnings in the log
                    showTimePickerDialog(v);

                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



    private void guardar()
    {
        //
        // Obtenemos los datos del formulario
        //
        dbApuntes.insertar(titulo.getText().toString(),((Lista)materia.getSelectedItem()).getCodigo(),apuntes.getText().toString(),null);
        System.out.print(titulo.getText().toString()+((Lista)materia.getSelectedItem()).getCodigo()+apuntes.getText().toString());
        Toast.makeText(PersonalTask.this,"Apunte guardado",Toast.LENGTH_SHORT).show();

        setResult(RESULT_OK);
        finish();
    }

    private void cancelar()
    {
        setResult(RESULT_CANCELED, null);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_personal_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.guardar_apunte) {
            guardar();
            return true;
        }
        if (id == R.id.cancelar_apunte) {
            cancelar();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendario = Calendar.getInstance();
        calendario.set(Calendar.YEAR, year);
        calendario.set(Calendar.MONTH, monthOfYear);
        calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        Date d = new Date();
        d.setTime(calendario.getTimeInMillis());
        fecha_venc_apunte.setText(dateFormat.format(d));

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar hora = Calendar.getInstance();
        hora.set(Calendar.HOUR,hourOfDay);
        hora.set(Calendar.MINUTE,minute);
        Date d = new Date();
        d.setTime(hora.getTimeInMillis());
        hora_venc_apunte.setText(timeFormat.format(d));
    }
}
