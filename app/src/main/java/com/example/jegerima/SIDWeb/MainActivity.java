package com.example.jegerima.SIDWeb;
/**
 * Created by Jegerima on 20/01/2015.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;
import org.postgresql.Driver;


import com.example.jegerima.SIDWeb.database.DataBaseManagerCourses;
import com.example.jegerima.SIDWeb.database.DataBaseManagerNews;
import com.example.jegerima.SIDWeb.database.DataBaseManagerNotes;
import com.example.jegerima.SIDWeb.database.MyConnection;

import java.io.File;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks,SensorEventListener {


    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;


    private FragmentTabContainer FragmentTabInicio;
    private FragmentTabContainer FragmentTabCursos;
    private FragmentTabContainer FragmentTabTareas;
    //private Fragment FragmentHorarios;

    private Accelerometer accelerometer;

    //----------------------------------------------------------------------------------------------
    //Sobreescritura de Metodos necesarios para el funcionamiento del acelerometro.... realizacion de los calculos.
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(accelerometer.update(getApplicationContext(), event)){
            Intent intent = new Intent(this,PersonalTask.class);
            startActivity(intent);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
    //**********************************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Creación inserción de datos en la base de datos
        DataBaseManagerNews manager;
        manager= new DataBaseManagerNews(this);

        //manager.insertar("12346","IHM","16144","asdasdasasd",new Date(),"0");
        //manager.insertar("23456","IA","Pelaez");
        //manager.insertar("34567","Digitales","Ponguillo");
        //manager.insertar("34567","Micro","Marcia");


        //initFragmentTabInicio();
        initFragmentTabCursos();
        initFragmentTabTareas();

        mTitle = getTitle();
        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout)); // Set up the drawer.
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (position) {
            case 0:
                if (FragmentTabInicio == null) {
                    initFragmentTabInicio();
                    transaction.add(R.id.container, FragmentTabInicio);
                } else
                    transaction.replace(R.id.container, FragmentTabInicio);
                break;
            case 1:
                transaction.replace(R.id.container, FragmentTabCursos);
                break;
            case 2:
                transaction.replace(R.id.container, FragmentTabTareas);
                break;
            case 3:
                //fragmentManager.beginTransaction().replace(R.id.container, FragmentTabInicio).commit();
                break;
            case 4:
                logout();
                break;
        }
        onSectionAttached(position + 1);
        //transaction.addToBackStack(null);
        transaction.commit();
        Toast.makeText(this, mTitle + " " + position, Toast.LENGTH_SHORT).show();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override //Este es el listener para el menu de opciones
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_newTask) {
            Intent intent = new Intent(this,PersonalTask.class);
            startActivity(intent);
            return true;
        }if (id == R.id.action_settings) {


            return true;
        }
        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        if (id == R.id.action_camera) {

            //Si no existe se crea el directorio a guardar
            File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/SidWeb/Camara");
            directory.mkdirs();

            int IMAGE_CAPTURE = 102;
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //Se guarda la fecha para ponerla de nombre en la imagen
            String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
            //se defina guardar en la carpeta destinada con la fecha de nombre
            File mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/SidWeb/Camara/" + mydate + ".jpg");
            Uri imgUri = Uri.fromFile(mediaFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
            startActivityForResult(intent, IMAGE_CAPTURE);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logout() {


        DataBaseManagerNotes notes= new DataBaseManagerNotes(this);
        notes.vaciar();
        DataBaseManagerNews news= new DataBaseManagerNews(this);
        news.vaciar();
        DataBaseManagerCourses courses= new DataBaseManagerCourses(this);
        courses.vaciar();

        String user = "llaveUsuarioSIDWeb";
        SharedPreferences sharedpreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.putString(user, sharedpreferences.getString(user, null));
        editor.commit();


        //moveTaskToBack(true);
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        this.finish();
    }

    private void initFragmentTabInicio() {
        FragmentTabInicio = new FragmentTabContainer();
        Bundle bargs = new Bundle();
        bargs.putInt("section_number", 1);
        FragmentTabInicio.setArguments(bargs);



    }

    private void initFragmentTabCursos() {
        FragmentTabCursos = new FragmentTabContainer();
        Bundle bargs = new Bundle();
        bargs = new Bundle();
        bargs.putInt("section_number", 2);
        FragmentTabCursos.setArguments(bargs);
    }

    private void initFragmentTabTareas() {
        FragmentTabTareas = new FragmentTabContainer();
        Bundle bargs = new Bundle();
        bargs = new Bundle();
        bargs.putInt("section_number", 3);
        FragmentTabTareas.setArguments(bargs);
    }
    //***********************************************************************

    @Override
    protected void onResume() {
        super.onResume();
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (sensors.size() > 0) {
            accelerometer = new Accelerometer();
            sm.registerListener(this, sensors.get(0), SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    protected void onStop() {
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        sm.unregisterListener(this);
        super.onStop();
    }


    public Context prueba(){
        return this;
    }






}


