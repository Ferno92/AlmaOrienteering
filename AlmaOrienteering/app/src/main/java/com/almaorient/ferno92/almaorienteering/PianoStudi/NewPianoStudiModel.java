package com.almaorient.ferno92.almaorienteering.PianoStudi;

/**
 * Created by ale96 on 29/03/2017.
 */

public class NewPianoStudiModel {

    public static final String CORSOCODICE = "corso_codice";
    public static final String CORSONOME = "materia_descrizione";
    public static final String PADRE = "componente_padre";
    public static final String RADICE = "componente_radice";
    public static final String URL = "url";

    //ecc ecc
    Integer mCorsoCodice;
    String mCorsoNome;
    Integer mPadre;
    Integer mRadice;
    String mUrl;


    public NewPianoStudiModel(Integer corsoCodice, String corsoNome, Integer padre, Integer radice,
                          String url, String ritardo, String erasmus, String stage, String soddisfazione){
        this.mCorsoCodice = corsoCodice;
        this.mCorsoNome = corsoNome;
        this.mPadre = padre;
        this.mRadice = radice;
        this.mUrl = url;

    }

    public Integer getCorsoCodice() {
        return mCorsoCodice;
    }

    public String getCorsoNome () {
        return mCorsoNome;
    }

    public Integer getPadre() {
        return mPadre;
    }

    public Integer getRadice() {
        return mRadice;
    }

    public String getUrl() {
        return mUrl;
    }

}
