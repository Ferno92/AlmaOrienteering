package com.almaorient.ferno92.almaorienteering;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.airbnb.lottie.LottieAnimationView;
import com.almaorient.ferno92.almaorienteering.strutturaUnibo.Corso;
import com.almaorient.ferno92.almaorienteering.strutturaUnibo.Scuola;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static com.almaorient.ferno92.almaorienteering.R.id.listaagraria;

public class ElencoScuoleActivity extends BaseActivity {

    //private int mPosition = 0;
    private ArrayList<Scuola> mListaScuole = new ArrayList<>();
    private ListView mElenco;

    private void pressbutton(String key, final ListView listView) {
        String buttonIDname = key + "layout";
        int buttonID = getResources().getIdentifier(buttonIDname, "id", getPackageName());
        final RelativeLayout scuolaLayout = (RelativeLayout) findViewById(buttonID);

        int arrowButtonID = getResources().getIdentifier(key + "plus", "id", getPackageName());
        final ImageButton arrowButton = (ImageButton) scuolaLayout.findViewById(arrowButtonID);
        scuolaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listView.getVisibility() == View.GONE) {
                    listView.setVisibility(view.VISIBLE);

                } else {
                    listView.setVisibility(view.GONE);
                }
                float deg = arrowButton.getRotation() + 180F;
                arrowButton.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
            }

        });
    }

    private void onItemClick(ListView listView, final String key){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adattatore, View view, int pos, long l) {
                                    // recupero il titolo memorizzato nella riga tramite l'ArrayAdapter
                                    final String titoloriga = ((Corso) adattatore.getItemAtPosition(pos)).getNome();
                                    for(int j = 0; j < mListaScuole.size(); j++){
                                        if(mListaScuole.get(j).getScuolaId().equals(key)){
                                            for (int i = 0; i < mListaScuole.get(j).getListaCorsi().size(); i++) {
                                                ArrayList<Corso> listaCorsi = (ArrayList<Corso>) mListaScuole.get(j).getListaCorsi();
                                                if (listaCorsi.get(i).getNome().equals(titoloriga)) {
                                                    String codicecorsoagraria = listaCorsi.get(i).getScuolaId();
                                                    String url = listaCorsi.get(i).getUrl();
                                                    String tipo = listaCorsi.get(i).getTipo();
                                                    String campus = listaCorsi.get(i).getCampus();
                                                    String accesso = listaCorsi.get(i).getAccesso();
                                                    Long scuolaid = listaCorsi.get(i).getIdScuola();

                                                    richiamoPaginaInterna(titoloriga, codicecorsoagraria, url, key, tipo, campus, accesso,scuolaid);
                                                    break;
                                                }
                                            }
                                        }
                                    }

                                }
                            });
    }

    private void richiamoPaginaInterna(String nomecorso, String codicecorso, String url, String nomescuola, String tipo, String campus, String accesso,Long idscuola) {
        Intent nuovapagina = new Intent(this, DettagliCorsoActivity.class);
        nuovapagina.putExtra("Vocecliccata", nomecorso);
        nuovapagina.putExtra("Codicecorso", codicecorso);
        nuovapagina.putExtra("Sitocorso", url);
        nuovapagina.putExtra("Nomescuola", nomescuola);
        nuovapagina.putExtra("Tipocorso", tipo);
        nuovapagina.putExtra("Campus", campus);
        nuovapagina.putExtra("Accesso", accesso);
        nuovapagina.putExtra("IdScuola",idscuola);
        startActivity(nuovapagina);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.elenco_scuole);

        setTitle("Scuole");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        Query query = ref.child("corso");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap meMap = (HashMap) dataSnapshot.getValue();
                Iterator scuolaIterator = meMap.keySet().iterator();
                while (scuolaIterator.hasNext()) {
                    String key = (String) scuolaIterator.next();
                    ArrayList value = (ArrayList) meMap.get(key);

                    ArrayList<Corso> tempCorsoList = new ArrayList<Corso>();
                    for (int i = 0; i < value.size(); i++) {
                        HashMap corsoMap = (HashMap) value.get(i);
                        Iterator corsoIterator = corsoMap.keySet().iterator();
                        String codicedelcorso = "";
                        String nomecorso = "";
                        String sito = "";
                        String tipo = "";
                        String campus = "";
                        String accesso = "";
                        Long idscuola= null;
                        while (corsoIterator.hasNext()) {
                            String corsoKey = (String) corsoIterator.next();

                            switch (corsoKey) {
                                case "corso_codice":
                                    codicedelcorso = String.valueOf(corsoMap.get(corsoKey));
                                    break;
                                case "corso_descrizione":
                                    nomecorso = (String) corsoMap.get(corsoKey);
                                    break;
                                case "url":
                                    sito = (String) corsoMap.get(corsoKey);
                                    break;
                                case "tipologia":
                                    tipo = (String) corsoMap.get(corsoKey);
                                    break;
                                case "campus":
                                    campus = (String) corsoMap.get(corsoKey);
                                    break;
                                case "accesso":
                                    accesso = (String) corsoMap.get(corsoKey);
                                    break;
                                case "cod_scuola":
                                    idscuola= (Long) corsoMap.get(corsoKey);
                                    break;
                            }
                        }
                        Corso corso = new Corso(codicedelcorso, nomecorso, sito, tipo, campus, accesso, idscuola);
                        tempCorsoList.add(corso);
                    }
                    //Switch loop
                    switch (key) {
                        case "agraria":
                            Scuola scuolaAgraria = new Scuola(key, "Scuola di Agraria", tempCorsoList);
                            mListaScuole.add(scuolaAgraria);
                            mElenco = (ListView) findViewById(listaagraria);
                            // riempio la lista arrayadapter sua
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item, scuolaAgraria.getListaCorsi());

                            //inietto i dati
                            mElenco.setAdapter(adapter);

                            setListViewHeightBasedOnChildren(mElenco);

                            pressbutton(key,mElenco);

                            onItemClick(mElenco, key);
                            break;

                        case "economia":
                            Scuola scuolaEconomia = new Scuola(key, "Scuola di Economia, Management e Statistica", tempCorsoList);
                            mListaScuole.add(scuolaEconomia);
                            mElenco = (ListView) findViewById(R.id.listaeconomia);
                            // riempio la lista arrayadapter sua
                            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item, scuolaEconomia.getListaCorsi());

                            //inietto i dati
                            mElenco.setAdapter(adapter2);

                            setListViewHeightBasedOnChildren(mElenco);

                            pressbutton(key,mElenco);

                            onItemClick(mElenco, key);
                            break;

                        case "farmacia":
                            Scuola scuolaFarmacia = new Scuola(key, "Scuola di Farmacia, Biotecnologie e Scienze motorie", tempCorsoList);
                            mListaScuole.add(scuolaFarmacia);
                            mElenco = (ListView) findViewById(R.id.listafarmacia);
                            // riempio la lista arrayadapter sua
                            ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item, scuolaFarmacia.getListaCorsi());

                            //inietto i dati
                            mElenco.setAdapter(adapter3);

                            setListViewHeightBasedOnChildren(mElenco);

                            pressbutton(key,mElenco);

                            onItemClick(mElenco, key);
                            break;

                        case "giurisprudenza":
                            Scuola scuolaGiurisprudenza = new Scuola(key, "Scuola di Giurisprudenza", tempCorsoList);
                            mListaScuole.add(scuolaGiurisprudenza);
                            mElenco = (ListView) findViewById(R.id.listagiurisprudenza);
                            // riempio la lista arrayadapter sua
                            ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item, scuolaGiurisprudenza.getListaCorsi());

                            //inietto i dati
                            mElenco.setAdapter(adapter4);

                            setListViewHeightBasedOnChildren(mElenco);

                            pressbutton(key,mElenco);

                            onItemClick(mElenco, key);
                            break;

                        case "ingegneria":
                            Scuola scuolaIngegneria = new Scuola(key, "Scuola di Ingegneria e Architettura", tempCorsoList);
                            mListaScuole.add(scuolaIngegneria);
                            mElenco = (ListView) findViewById(R.id.listaingegneria);
                            // riempio la lista arrayadapter sua
                            ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item, scuolaIngegneria.getListaCorsi());

                            //inietto i dati
                            mElenco.setAdapter(adapter5);

                            setListViewHeightBasedOnChildren(mElenco);

                            pressbutton(key,mElenco);

                            onItemClick(mElenco, key);
                            break;

                        case "lettere":
                            Scuola scuolaLettere = new Scuola(key, "Scuola di Lettere e Beni culturali", tempCorsoList);
                            mListaScuole.add(scuolaLettere);
                            mElenco = (ListView) findViewById(R.id.listalettere);
                            // riempio la lista arrayadapter sua
                            ArrayAdapter<String> adapter6 = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item, scuolaLettere.getListaCorsi());

                            //inietto i dati
                            mElenco.setAdapter(adapter6);

                            setListViewHeightBasedOnChildren(mElenco);

                            pressbutton(key,mElenco);

                            onItemClick(mElenco, key);
                            break;

                        case "lingue":
                            Scuola scuolalingue = new Scuola(key, "Scuola di Lingue e Letterature, Traduzione e Interpretazione", tempCorsoList);
                            mListaScuole.add(scuolalingue);
                            mElenco = (ListView) findViewById(R.id.listalingue);
                            // riempio la lista arrayadapter sua
                            ArrayAdapter<String> adapter7 = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item, scuolalingue.getListaCorsi());

                            //inietto i dati
                            mElenco.setAdapter(adapter7);

                            setListViewHeightBasedOnChildren(mElenco);

                            pressbutton(key,mElenco);

                            onItemClick(mElenco, key);
                            break;

                        case "medicina":
                            Scuola scuolaMedicina = new Scuola(key, "Scuola di Medicina e Chirurgia", tempCorsoList);
                            mListaScuole.add(scuolaMedicina);
                            mElenco = (ListView) findViewById(R.id.listamedicina);
                            // riempio la lista arrayadapter sua
                            ArrayAdapter<String> adapter8 = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item, scuolaMedicina.getListaCorsi());

                            //inietto i dati
                            mElenco.setAdapter(adapter8);

                            setListViewHeightBasedOnChildren(mElenco);

                            pressbutton(key,mElenco);

                            onItemClick(mElenco, key);
                            break;

                        case "psicologia":
                            Scuola scuolaPsicologia = new Scuola(key, "Scuola di Psicologia", tempCorsoList);
                            mListaScuole.add(scuolaPsicologia);
                            mElenco = (ListView) findViewById(R.id.listapsicologia);
                            // riempio la lista arrayadapter sua
                            ArrayAdapter<String> adapter9 = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item, scuolaPsicologia.getListaCorsi());

                            //inietto i dati
                            mElenco.setAdapter(adapter9);

                            setListViewHeightBasedOnChildren(mElenco);

                            pressbutton(key,mElenco);

                            onItemClick(mElenco, key);
                            break;

                        case "scienze":
                            Scuola scuolaScienze = new Scuola(key, "Scuola di Scienze", tempCorsoList);
                            mListaScuole.add(scuolaScienze);
                            mElenco = (ListView) findViewById(R.id.listascienze);
                            // riempio la lista arrayadapter sua
                            ArrayAdapter<String> adapter10 = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item, scuolaScienze.getListaCorsi());

                            //inietto i dati
                            mElenco.setAdapter(adapter10);

                            setListViewHeightBasedOnChildren(mElenco);

                            pressbutton(key,mElenco);

                            onItemClick(mElenco, key);
                            break;

                        case "scienze_politiche":
                            Scuola scuolaScienze_politiche = new Scuola(key, "Scuola di Scienze Politiche", tempCorsoList);
                            mListaScuole.add(scuolaScienze_politiche);
                            mElenco = (ListView) findViewById(R.id.listascienze_politiche);
                            // riempio la lista arrayadapter sua
                            ArrayAdapter<String> adapter11 = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item, scuolaScienze_politiche.getListaCorsi());

                            //inietto i dati
                            mElenco.setAdapter(adapter11);

                            setListViewHeightBasedOnChildren(mElenco);

                            pressbutton(key,mElenco);

                            onItemClick(mElenco, key);
                            break;

                        default:
                            break;
                    }
                }
                Log.d("scuole: ", String.valueOf(mListaScuole.size()));
                LottieAnimationView spiderLoader = (LottieAnimationView)findViewById(R.id.spider_loader) ;
                spiderLoader.setVisibility(View.GONE);
                ScrollView scuoleScrollView = (ScrollView) findViewById(R.id.scuoleScrollView);
                scuoleScrollView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}