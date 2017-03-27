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
    public String mAccesso;
    public Long mIdScuola;

    public Corso(String id, String nome, String url, String tipo, String campus, String accesso, Long idscuola){
        this.mCodiceCorso = id;
        this.mNome = nome;
        this.mUrl = url;
        this.mTipo = tipo;
        this.mCampus = campus;
        this.mAccesso = accesso;
        this.mIdScuola=idscuola;
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

    public String getAccesso() { return this.mAccesso; }

    public Long getIdScuola() {return this.mIdScuola;}

    public String toString()
    {
        return this.mNome;
    }
}
