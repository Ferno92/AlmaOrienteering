package com.almaorient.ferno92.almaorienteering.PianoStudi;

/**
 * Created by ale96 on 15/03/2017.
 */

public class PianoStudiModel2 {
        public String mCorsoCodice;
        public String mMateria;
        public String mUrl;

        public PianoStudiModel2(String codice_corso, String nome_materia,String indirizzo_web){
            this.mCorsoCodice = codice_corso;
            this.mMateria = nome_materia;
            this.mUrl = indirizzo_web;
        }

        public String getCodiceCorso(){
            return this.mCorsoCodice;
        }

        public String getNomeMateria(){ return this.mMateria; }

        public String getIndirizzoWeb(){ return this.mUrl; }
}
