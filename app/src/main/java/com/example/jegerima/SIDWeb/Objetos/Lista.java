package com.example.jegerima.SIDWeb.Objetos;

/**
 * Created by Cesar on 01/02/2015.
 */
public class Lista {
    private String codigo;
    private String nombre;
    public Lista(String cod, String nom){
        this.codigo=cod;
        this.nombre=nom;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString(){
        return nombre;
    }
}
