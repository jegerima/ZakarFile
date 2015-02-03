package com.example.jegerima.SIDWeb.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyConnection {

    private final String SERVIDOR="200.126.23.138";
    private final String PUERTO="5432";
    private final String DB="canvas_production";
    private final String URL = "jdbc:postgresql://"+SERVIDOR+":"+PUERTO+"/"+DB;
    private final String USER = "pruebas";
    private final String PASSWORD = "pruebas2014";
    private boolean active = false;

    Connection con = null;
    Statement st = null;
    ResultSet rs = null;

    public ResultSet consulta(String query){
        try {
            System.out.println("ssssss");
            rs = st.executeQuery(query);
            System.out.println("dddddd");
/*
                if (rs.next()) {
                    System.out.println(rs.getString(1));
                }
*/
        } catch (Exception ex) {
            Logger lgr = Logger.getLogger(MyConnection.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            rs=null;
            System.out.println("murio aqui por "+ex.toString());
        }
        return rs;
    }

    public boolean getActive(){
        return active;
    }

    public MyConnection(){
        try{
            System.out.println("aqui llego");
            System.out.println(URL);
            Class.forName("org.postgresql.Driver");
            System.out.println("ojo");
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("aqui aquedo");
            st = con.createStatement();
            active=true;

        }catch(Exception e){
            System.out.println("inicio error");
            System.out.println(e.toString());;
            e.printStackTrace();
            System.out.println("fin error");
            active=false;
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {

                active=false;
                Logger lgr = Logger.getLogger(MyConnection.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }


    }

    public void close(){
        active=false;
        try {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }

        } catch (Exception ex) {
            System.out.println(ex.toString());

            active=false;
            Logger lgr = Logger.getLogger(MyConnection.class.getName());
            lgr.log(Level.WARNING, ex.getMessage(), ex);
        }

    }



}