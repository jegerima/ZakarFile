package com.example.jegerima.SIDWeb;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jegerima.SIDWeb.database.DataBaseManagerCourses;
import com.example.jegerima.SIDWeb.database.DataBaseManagerNews;
import com.example.jegerima.SIDWeb.database.MyConnection;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class LoginActivity extends Activity {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
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


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
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
            log_bool = null;
            log_bool = login(params[0], params[1]);
            if(log_bool.equalsIgnoreCase("true"));
                getMaterias_Anuncios();

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

    private void getMaterias_Anuncios(){

        MyConnection con = null;
        ResultSet rs=null;
        String q_materias=  "select c.id as course_id, c.name, c.course_code "+
                            "from users u " +
                            "join enrollments e on e.user_id=u.id "+
                            "join courses c on e.course_id=c.id "+
                            "join enrollment_terms er on c.enrollment_term_id=er.id "+
                            "where u.id=&PV_USER&;";

        String q_anuncios=  "select dc.id,dc.title,dc.context_id,dc.message,dc.posted_at,0 "+
                            "from discussion_topics dc " +
                            "where context_id in (select c.id " +
                            "from users u " +
                            "join enrollments e on e.user_id=u.id " +
                            "join courses c on e.course_id=c.id " +
                            "join enrollment_terms er on c.enrollment_term_id=er.id " +
                            "where u.id=&PV_USER&);";

        String q_user=      "select user_id "+
                            "from pseudonyms "+
                            "where unique_id='"+mUser.getText().toString()+"';";

        try{
            con = new MyConnection();
            if(con.getActive()) {
                rs = con.consulta(q_user);
                while (rs.next()) {
                    userId=Integer.parseInt(rs.getString(1));
                }

                rs.close();
                rs=null;

                rs = con.consulta(q_materias.replace("&PV_USER&", userId + ""));
                DataBaseManagerCourses courses;
                courses= new DataBaseManagerCourses(this);

                while (rs.next()) {
                    courses.insertar(rs.getString(1),rs.getString(2),"" );
                }

                rs.close();
                rs=null;

                rs=con.consulta(q_anuncios.replace("&PV_USER&", userId + ""));
                DataBaseManagerNews news;
                news= new DataBaseManagerNews(this);

                while (rs.next()) {
                    news.insertar(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), new Date(), rs.getString(6));
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



