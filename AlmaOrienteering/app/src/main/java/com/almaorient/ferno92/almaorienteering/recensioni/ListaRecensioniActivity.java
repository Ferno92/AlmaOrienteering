package com.almaorient.ferno92.almaorienteering.recensioni;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.almaorient.ferno92.almaorienteering.BaseActivity;
import com.almaorient.ferno92.almaorienteering.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by lucas on 26/03/2017.
 */

public class ListaRecensioniActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_recensioni);

        String nomeCorso = getIntent().getExtras().getString("nome_corso");
        TextView corsoTextView = (TextView) findViewById(R.id.nome_corso);
        corsoTextView.setText(nomeCorso);

        ArrayList<RecensioniModel> recList = new ArrayList<RecensioniModel>();

        RecensioniModel[] recensioni = {
                new RecensioniModel("luca.fernandez@studio.unibo.it", "4.5", "Proprio bello questo corso, ho veramente scelto bene!", 15),
                new RecensioniModel("tizio.caio@studio.unibo.it", "1", "Ma chi me l'ha fatto fare", 14),
                new RecensioniModel("super.pippo@studio.unibo.it", "3", "Poteva andarmi meglio ma non mi lamento.. Yuk!!", 13),
                new RecensioniModel("paolo.rossi@studio.unibo.it", "4.5", "Bella", 12),
                new RecensioniModel("ale.lotti@studio.unibo.it", "5", "Sono super entusiasta, mi sto divertendo un sacco! I professori sono fantastici e ci seguono molto! Abbiamo anche dei tutor meravigliosi e starei ore e ore e ore a parlare di questo fantastico corso! Si, lo so sono un po' logorrroico ma che ci vuoi fare ;)", 15),
                new RecensioniModel("marco.mariotti@studio.unibo.it", "2", "Mah, niente di speciale..", 11),
                new RecensioniModel("donald.duck@studio.unibo.it", "2", "Io speravo ci fossero più donne..", 10),
                new RecensioniModel("son.goku@studio.unibo.it", "0.5", "Pessimo, se continua così mi ritiro!", 9),
        };
        //riempimento casuale della lista delle persone
//        Random r=new Random();
//        for(int i=0;i<100;i++){
//            recList.add(recensioni[r.nextInt(recensioni.length)]);
//        }
        float sum = 0;
        //TODO: magari ordiniamo la lista di recensioni che arrivano dalla query per quota
        for(int i = 0; i < recensioni.length; i++){
            sum += Float.parseFloat(recensioni[i].getVoto());
            recList.add(recensioni[i]);
        }
        float ratingMedio = round((sum / recensioni.length), 2);
        TextView media = (TextView)findViewById(R.id.media_rating);
        media.setText(String.valueOf(ratingMedio) + " / 5");

        //Questa è la lista che rappresenta la sorgente dei dati della listview
        //ogni elemento è una mappa(chiave->valore)
        ArrayList<HashMap<String, Object>> data=new ArrayList<HashMap<String,Object>>();


        for(int i=0;i<recList.size();i++){
            RecensioniModel p = recList.get(i);

            HashMap<String,Object> recMap=new HashMap<String, Object>();//creiamo una mappa di valori

            recMap.put("voto", p.getVoto());
            recMap.put("recensione", p.getRecensione());
//            recMap.put("quota", p.getQuota());
            data.add(recMap);  //aggiungiamo la mappa di valori alla sorgente dati
        }


        SimpleAdapter adapter = new SimpleAdapter(this, data , R.layout.recensioni_list_item,
                new String[]{"voto","recensione"}, //dai valori contenuti in queste chiavi
                new int[]{R.id.rating_recensione,R.id.testo_recensione}//agli id delle view
        );
        adapter.setViewBinder(new RecensioniBinder());
        ((ListView)findViewById(R.id.lista_recensioni)).setAdapter(adapter);
    }

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}
