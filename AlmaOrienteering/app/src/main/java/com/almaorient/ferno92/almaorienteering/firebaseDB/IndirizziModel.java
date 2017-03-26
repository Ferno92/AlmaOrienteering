package com.almaorient.ferno92.almaorienteering.firebaseDB;

/**
 * Created by ale96 on 25/03/2017.
 */

public class IndirizziModel {
    public String mCodice;
    public Double mLatitudine;
    public Double mLongitudine;


    public IndirizziModel(String codice, Double latitudine, Double longitudine){
        this.mCodice = codice;
        this.mLatitudine = latitudine;
        this.mLongitudine = longitudine;
    }

    public String getCodice(){
        return this.mCodice;
    }


    public Double getLatitudine(){
        return this.mLatitudine;
    }
    public Double getLongitudine(){
        return this.mLongitudine;
    }

//    public String toStringa ()
//    {
//        return this.mCodice;
//    }
}

