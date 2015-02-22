package com.example.jegerima.SIDWeb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jegerima.SIDWeb.database.DataBaseManagerPlanning;
import com.example.jegerima.SIDWeb.database.DataBaseManagerTask;
import com.example.jegerima.SIDWeb.database.DataBaseManagerAnnouncements;
import com.example.jegerima.SIDWeb.database.DataBaseManagerCourses;

import com.example.jegerima.SIDWeb.database.MyConnection;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


public class LoginActivity extends Activity {


    private UserLoginTask mAuthTask = null;
    private int userId=0;
    private Activity act=this;

    // UI references.
    public static final String MyPREFERENCES = "SIDWebPrefs" ;
    SharedPreferences sharedpreferences;

    private String user="llaveUsuarioSIDWeb";
    private String user_log="llaveUsuarioLogSIDWeb";
    private EditText mUser;
    private EditText mPassword;
    private ProgressBar mProgress;
    private View mLoginFormView;
    private String log_bool;
    private Button mButton;
    private boolean is_log=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mUser = (EditText) findViewById(R.id.user);
        mProgress = (ProgressBar)findViewById(R.id.login_progress);
        mButton = (Button) findViewById(R.id.sign_in_button);
        mPassword = (EditText) findViewById(R.id.password);

        mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {

                    String user = mUser.getText().toString();
                    String pass = mPassword.getText().toString();

                    mAuthTask = new UserLoginTask(user, pass);
                    mAuthTask.execute(user, pass);
                    mAuthTask.cancel(true);

                    return true;
                }
                return false;
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (is_log) {
                    mAuthTask.cancel(true);
                    mButton.setText(getString(R.string.action_cancel2));
                    mButton.setEnabled(false);

                } else {

                    if(comprobar_campos())return;
                    String user = mUser.getText().toString();
                    String pass = mPassword.getText().toString();


                    mAuthTask = new UserLoginTask(user, pass);
                    mAuthTask.execute(user, pass);

                }
            }
        });
    }

    @Override
    protected void onResume() {

        super.onResume();
        sharedpreferences=getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(user_log))
        {
            sharedpreferences=getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            this.finish();

            System.out.println("existe usuario y es: "+user+": "+sharedpreferences.getString(user,null));
        }else if(sharedpreferences.contains(user)){
            mUser.setText(sharedpreferences.getString(user,null));
            mPassword.requestFocus();
        }
    }

    public void attemptLogin() {
        Toast o= Toast.makeText(getApplicationContext(), log_bool, Toast.LENGTH_SHORT);
        //o.show();
        if(log_bool.equalsIgnoreCase("true")){//
            SharedPreferences.Editor editor = sharedpreferences.edit();
            String u = mUser.getText().toString();
            editor.putString(user, u);
            editor.putBoolean(user_log, true);
            editor.commit();

            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            return;
        }

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        //alertDialog.setTitle("Reset...");
        alertDialog.setMessage("El usuario o contraseña ingresado es incorrecto.");
        alertDialog.setButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //here you can add functions
            } });
        alertDialog.show();

        // Reset errors.
        mUser.setEnabled(true);
        mPassword.setEnabled(true);
        mPassword.setText("");
        mButton.setText(getString(R.string.action_sign_in));

    }

    public boolean comprobar_campos(){
        mUser.setError(null);
        mPassword.setError(null);

        String email = mUser.getText().toString();
        String password = mPassword.getText().toString();
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            mPassword.setError(getString(R.string.error_field_required));
            mPassword.requestFocus();
            cancel = true;
        }
        if (TextUtils.isEmpty(email)) {
            mUser.setError(getString(R.string.error_field_required));
            mUser.requestFocus();
            cancel = true;
        }
        return cancel;

    }

    public String login(String usuario,String pass)
    {
        String NAMESPACE = "http://tempuri.org/";
        String URL="https://ws.espol.edu.ec/saac/wsandroid.asmx";
        String METHOD_NAME = "autenticacion";
        String SOAP_ACTION = "http://tempuri.org/autenticacion";
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("authUser",usuario);
        request.addProperty("authContrasenia",pass);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        SoapPrimitive response;

        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = (SoapPrimitive) envelope.getResponse();
            String bool = response.toString();
            System.out.println(bool);
            return bool;
        }
        catch (Exception e)
        {
            return "fail ";
        }
    }

     /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<String, Void, String> {

        private final String mEmail;
        private final String mPassword;


        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected void onPreExecute() {
            is_log=true;
            mUser.setEnabled(false);
            LoginActivity.this.mPassword.setEnabled(false);
            mButton.setText(getString(R.string.action_cancel));
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO: attempt authentication against a network service.
            mProgress.setProgress(0);
            log_bool = null;/*
            log_bool = login(params[0], params[1]);*/
            log_bool="true";
            if(log_bool.equalsIgnoreCase("true"));
                getCourses_Announcements_Task();

            for (int i = 0; i < 100; i++) {
                if (!this.isCancelled()) {
                    try {
                        Thread.sleep(20);
                        //System.out.println(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        System.out.println("una falla");
                    }
                    mProgress.setProgress(i);
                }
                else return "cancel";
            }
            mProgress.setProgress(0);
            return log_bool;
        }

        @Override
        protected void onPostExecute(String result) {
            log_bool=result;
            attemptLogin();
            System.out.println("si termino");
            if (log_bool.equalsIgnoreCase("true")) {
                finish();
            } else {
                LoginActivity.this.mPassword.setError(getString(R.string.error_incorrect_password));
                LoginActivity.this.mPassword.requestFocus();
            }

            is_log=false;
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            mProgress.setProgress(0);
            mUser.setEnabled(true);
            LoginActivity.this.mPassword.setEnabled(true);
            mButton.setText(getString(R.string.action_sign_in));
            mButton.setEnabled(true);
            is_log=false;
        }
    }

    private void getCourses_Announcements_Task(){

        MyConnection con = null;
        ResultSet rs=null;
        //insertar(String id,String code, String name,String term,String teacher)
        String q_curos=  "select c.id as course_id,c.course_code, c.name, er.name, c.id_teacher "+
                            "from users u " +
                            "join enrollments e on e.user_id=u.id "+
                            "join courses c on e.course_id=c.id "+
                            "join enrollment_terms er on c.enrollment_term_id=er.id "+
                            "where u.id=&PV_USER&;";
        //insertar(String id,String id_curso,String nombre_curso,String titulo,String contenido,Date fecha,String num_msgs)
        String q_anuncios=  "select dc.id,dc.context_id,c.name,dc.title,dc.message,dc.posted_at,0 "+
                            "from discussion_topics dc " +
                            "join courses c on (c.id=dc.context_id) "+
                            "where context_id in (select c.id " +
                            "from users u " +
                            "join enrollments e on e.user_id=u.id " +
                            "join courses c on e.course_id=c.id " +
                            "join enrollment_terms er on c.enrollment_term_id=er.id " +
                            "where u.id=&PV_USER&);";
        //insertar(String id,String id_curso,String nombre_curso,String tiulo,String descripcion,Date desde,Date hasta,Date atraso)
        /*String q_task= "select a.id, c.id, c.name, a.title,a.description,due_at desde ,unlock_at hasta,lock_at atraso " +
                            "from assignments a " +
                            "left join submissions s on a.id=s.assignment_id " +
                            "where context_id=16144 and submission_types not like '%quiz%';";
        */
                            //"select a.id, c.id, c.name, a.title,a.description,due_at desde ,unlock_at hasta,lock_at atraso "+
        String q_task =     "select a.id, c.id, c.name, a.title,a.description,COALESCE(due_at,'1901-01-01 11:22:33') desde ,unlock_at hasta,lock_at atraso,max(s.submitted_at),count(a.id) "+
                            "from assignments a "+
                            "left join submissions s on a.id=s.assignment_id "+
                            "left join courses c on a.context_id=c.id "+
                            "left join enrollments e on e.course_id=c.id "+
                            "where e.user_id=&PV_USER& and submission_types not like '%quiz%' "+
                            "group by a.id,c.id "+
                            "order by desde desc;";

        String q_user=      "select user_id "+
                            "from pseudonyms "+
                            "where unique_id='"+mUser.getText().toString()+"';";

        String q_plan=      "select cm.id CapID,cm.name, ct.id PlanId,ct.title,cm.context_id,ct.content_id,ct.content_type,ct.url " +
                            "from context_modules cm " +
                            "join content_tags ct on cm.id=ct.context_module_id " +
                            "where cm.workflow_state='active' and  " +
                            "cm.context_id in (select c.id  " +
                            "from users u  " +
                            "join enrollments e on e.user_id=u.id  " +
                            "join courses c on e.course_id=c.id  " +
                            "join enrollment_terms er on c.enrollment_term_id=er.id  " +
                            "where u.id=&PV_USER&);";

        try{
            con = new MyConnection();
            if(con.getActive()) {
                rs = con.consulta(q_user);
                while (rs.next()) {
                    userId=Integer.parseInt(rs.getString(1));
                    System.out.println(userId);
                }

                rs.close();
                rs=null;

                System.out.println(q_anuncios.replace("&PV_USER&", userId + ""));
                rs = con.consulta(q_curos.replace("&PV_USER&", userId + ""));
                DataBaseManagerCourses courses;
                courses= new DataBaseManagerCourses(this);

                while (rs.next()) {
                    //insertar(String id,String code, String name,Date term,String teacher)
                    courses.insertar(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5) );
                }

                rs.close();
                rs=null;

                rs=con.consulta(q_anuncios.replace("&PV_USER&", userId + ""));
                DataBaseManagerAnnouncements news;
                news= new DataBaseManagerAnnouncements(this);

                while (rs.next()) {
                    //insertar(String id,String id_curso,String nombre_curso,String titulo,String contenido,Date fecha,String num_msgs)
                    news.insertar(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getDate(6),rs.getString(7));
                }

                rs.close();
                rs=null;

                rs=con.consulta(q_task.replace("&PV_USER&", userId + ""));
                DataBaseManagerTask tasks;
                tasks=new DataBaseManagerTask(this);

                while (rs.next()){
                    System.out.println("NextTask: "+rs.getString(1));
                    //insertar(String id,String id_curso,String nombre_curso,String tiulo,String descripcion,Date desde,Date hasta,Date atraso)
                    //tasks.insertar(rs.getString(1),"16144","Ing software II",rs.getString(2),rs.getString(3),rs.getDate(4),rs.getDate(5),rs.getDate(6));
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = df.parse("2013-10-18 13:59:00");
                    tasks.insertar(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5), rs.getTimestamp(6),rs.getTimestamp(7),rs.getTimestamp(8),rs.getTimestamp(9));
                }
                rs.close();
                rs=null;

                rs=con.consulta(q_plan.replace("&PV_USER&", userId + ""));
                DataBaseManagerPlanning plan;
                plan=new DataBaseManagerPlanning(this);
                while (rs.next()) {
                    //insertar(String id,String id_curso,String nombre_curso,String titulo,String contenido,Date fecha,String num_msgs)
                    plan.insertar(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6),rs.getString(7),rs.getString(8));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {

                if(con!=null)
                    con.close();
                if(rs!=null)
                    rs.close();
            } catch (SQLException ex) {
                //Logger.getLogger(consulta.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


    }




}



