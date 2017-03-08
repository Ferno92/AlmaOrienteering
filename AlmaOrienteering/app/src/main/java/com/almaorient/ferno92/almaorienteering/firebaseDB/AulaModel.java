package com.almaorient.ferno92.almaorienteering.firebaseDB;

/**
 * Created by luca.fernandez on 08/03/2017.
 */

public class AulaModel {
    public String mCodice;
    public String mNome;
    public String mIndirizzo;
    public String mPiano;
    public String mLatitudine;
    public String mLongitudine;

    public AulaModel(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public AulaModel(String codice, String nome,String indirizzo, String piano,String latitudine, String longitudine){
        this.mCodice = codice;
        this.mNome = nome;
        this.mIndirizzo = indirizzo;
        this.mPiano = piano;
        this.mLatitudine = latitudine;
        this.mLongitudine = longitudine;
    }
}
