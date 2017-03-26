package com.almaorient.ferno92.almaorienteering.firebaseDB;

/**
 * Created by ale96 on 25/03/2017.
 */

public class IndirizziModel {
    public String mCodice;
    public String mLatitudine;
    public String mLongitudine;


    public IndirizziModel(String codice, String latitudine, String longitudine){
        this.mCodice = codice;
        this.mLatitudine = latitudine;
        this.mLongitudine = longitudine;
    }

    public String getCodice(){
        return this.mCodice;
    }


    public String getLatitudine(){
        return this.mLatitudine;
    }
    public String getLongitudine(){
        return this.mLongitudine;
    }

//    public String toStringa ()
//    {
//        return this.mCodice;
//    }
}

