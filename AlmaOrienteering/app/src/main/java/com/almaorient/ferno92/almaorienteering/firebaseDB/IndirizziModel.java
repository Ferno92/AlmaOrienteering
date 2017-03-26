package com.almaorient.ferno92.almaorienteering.firebaseDB;

/**
 * Created by ale96 on 25/03/2017.
 */

public class IndirizziModel {
    public String mCodice;
    public String mNome;
    public String mIndirizzo;
    public String mPiano;
    public String mLatitudine;
    public String mLongitudine;

    public IndirizziModel(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public IndirizziModel(String codice, String nome,String indirizzo, String piano,String latitudine, String longitudine){
        this.mCodice = codice;
        this.mNome = nome;
        this.mIndirizzo = indirizzo;
        this.mPiano = piano;
        this.mLatitudine = latitudine;
        this.mLongitudine = longitudine;
    }

    public String getCodice(){
        return this.mCodice;
    }

    public String getNome(){
        return this.mNome;
    }

    public String getIndirizzo(){
        return this.mIndirizzo;
    }

    public String getPiano(){
        return this.mPiano;
    }
    public String getLatitudine(){
        return this.mLatitudine;
    }
    public String getLongitudine(){
        return this.mLongitudine;
    }
}

