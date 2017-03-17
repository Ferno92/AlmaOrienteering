package com.almaorient.ferno92.almaorienteering.strutturaUnibo;

/**
 * Created by luca.fernandez on 10/03/2017.
 */

public class Corso {
    public String mCodiceCorso;
    public String mNome;
    public String mUrl;
    public String mTipo;
    public String mCampus;

    public Corso(String id, String nome, String url, String tipo, String campus){
        this.mCodiceCorso = id;
        this.mNome = nome;
        this.mUrl = url;
        this.mTipo = tipo;
        this.mCampus = campus;
    }

    public String getNome(){
        return this.mNome;
    }

    public String getScuolaId(){
        return this.mCodiceCorso;
    }

    public String getUrl() { return this.mUrl; }

    public String getTipo(){
        return this.mTipo;
    }

    public String getCampus(){
        return this.mCampus;
    }

    public String toString()
    {
        return this.mNome;
    }
}
