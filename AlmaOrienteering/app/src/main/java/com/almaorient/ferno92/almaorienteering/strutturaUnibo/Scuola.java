package com.almaorient.ferno92.almaorienteering.strutturaUnibo;

/**
 * Created by luca.fernandez on 10/03/2017.
 */

public class Scuola {
    public String mId;
    public String mNome;

    public Scuola(String id, String nome){
        this.mId = id;
        this.mNome = nome;
    }

    public String getNome(){
        return this.mNome;
    }

    public String getScuolaId(){
        return this.mId;
    }

    public String toString()
    {
        return this.mNome;
    }
}
