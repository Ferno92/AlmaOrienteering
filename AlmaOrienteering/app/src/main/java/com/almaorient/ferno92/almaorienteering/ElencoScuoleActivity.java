package com.almaorient.ferno92.almaorienteering;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.almaorient.ferno92.almaorienteering.PianoStudi.PianoStudiModel2;
import com.almaorient.ferno92.almaorienteering.strutturaUnibo.Corso;
import com.almaorient.ferno92.almaorienteering.strutturaUnibo.Scuola;
import com.almaorient.ferno92.almaorienteering.versus.StatCorsoModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static android.R.drawable.btn_minus;
import static android.R.drawable.btn_plus;
import static com.almaorient.ferno92.almaorienteering.R.id.agrarialayout;
import static com.almaorient.ferno92.almaorienteering.R.id.agrariaplus;
import static com.almaorient.ferno92.almaorienteering.R.id.economiaplus;
import static com.almaorient.ferno92.almaorienteering.R.id.farmaciaplus;
import static com.almaorient.ferno92.almaorienteering.R.id.giuriplus;

import static com.almaorient.ferno92.almaorienteering.R.id.plustre;
import static com.almaorient.ferno92.almaorienteering.R.id.top;
import static com.almaorient.ferno92.almaorienteering.R.id.topPanel;
import static com.almaorient.ferno92.almaorienteering.R.id.wrap_content;

public class ElencoScuoleActivity extends AppCompatActivity {

    private int mPosition = 0;
    private ArrayList<Scuola> mListaScuole = new ArrayList<>();
    private ListView mElencoAgraria;

    private void richiamoPaginaInterna(String nomecorso, String codicecorso, String url, String nomescuola, String tipo, String campus, String accesso) {
        Intent nuovapagina = new Intent(this, DettagliCorsoActivity.class);
        nuovapagina.putExtra("Vocecliccata", nomecorso);
        nuovapagina.putExtra("Codicecorso", codicecorso);
        nuovapagina.putExtra("Sitocorso", url);
        nuovapagina.putExtra("Nomescuola", nomescuola);
        nuovapagina.putExtra("Tipocorso", tipo);
        nuovapagina.putExtra("Campus", campus);
        nuovapagina.putExtra("Accesso", accesso);
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

        mElencoAgraria = (ListView) findViewById(R.id.listagraria);

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
                            }
                        }
                        Corso corso = new Corso(codicedelcorso, nomecorso, sito, tipo, campus, accesso);
                        tempCorsoList.add(corso);
                    }
                    //Switch loop
                    switch (key) {
                        case "agraria":
                            Scuola scuolaAgraria = new Scuola(key, "Scuola di Agraria", tempCorsoList);
                            mListaScuole.add(scuolaAgraria);
                            // riempio la lista arrayadapter sua
                            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, scuolaAgraria.getListaCorsi());

                            //inietto i dati
                            mElencoAgraria.setAdapter(adapter);
                            setListViewHeightBasedOnChildren(mElencoAgraria);

                            final ImageButton agrariaplusButton = (ImageButton) findViewById(agrariaplus);
                            agrariaplusButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (mElencoAgraria.getVisibility() == View.GONE) {
                                        mElencoAgraria.setVisibility(view.VISIBLE);
                                        agrariaplusButton.setImageResource(R.drawable.meno);

                                    } else {
                                        mElencoAgraria.setVisibility(view.GONE);
                                        agrariaplusButton.setImageResource(R.drawable.piu);
                                    }
                                }

                            });

                            mElencoAgraria.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adattatore, View view, int pos, long l) {
                                    // recupero il titolo memorizzato nella riga tramite l'ArrayAdapter
                                    final String titoloriga = ((Corso) adattatore.getItemAtPosition(pos)).getNome();
                                    for(int j = 0; j < mListaScuole.size(); j++){
                                        if(mListaScuole.get(j).getScuolaId().equals("agraria")){
                                            for (int i = 0; i < mListaScuole.get(j).getListaCorsi().size(); i++) {
                                                ArrayList<Corso> listaCorsi = (ArrayList<Corso>) mListaScuole.get(j).getListaCorsi();
                                                if (listaCorsi.get(i).getNome().equals(titoloriga)) {
                                                    String codicecorsoagraria = listaCorsi.get(i).getScuolaId();
                                                    String url = listaCorsi.get(i).getUrl();
                                                    String tipo = listaCorsi.get(i).getTipo();
                                                    String campus = listaCorsi.get(i).getCampus();
                                                    String accesso = listaCorsi.get(i).getAccesso();

                                                    richiamoPaginaInterna(titoloriga, codicecorsoagraria, url, "agraria", tipo, campus, accesso);
                                                    break;
                                                }
                                            }
                                        }
                                    }

                                }
                            });

                            break;
                        case "economia":
                            break;
                        case "farmacia":
                            break;
                        case "giurisprudenza":
                            break;
                        case "ingegneria":
                            break;
                        case "lettere":
                            break;
                        case "lingue":
                            break;
                        case "medicina":
                            break;
                        case "psicologia":
                            break;
                        case "scienze":
                            break;
                        case "scienze_politiche":
                            break;

                        default:
                            break;
                    }
                }
                Log.d("scuole: ", String.valueOf(mListaScuole.size()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//
//
//
//        final ListView elencoagraria = (ListView) findViewById(R.id.listagraria);
//
//        final ArrayList<Corso> mListaCorsiAgraria = new ArrayList<Corso>();
//
//        Query queryagraria =  ref.child("corso/agraria");
//
//        queryagraria.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    String codicedelcorso = (String) String.valueOf(data.child("corso_codice").getValue());
//                    String nomecorso = (String) data.child("corso_descrizione").getValue();
//                    String sito = (String) data.child("url").getValue();
//                    String tipo = (String) data.child("tipologia").getValue();
//                    String campus =(String) data.child("campus").getValue();
//                    String accesso =(String) data.child("accesso").getValue();
//
//                    Corso corso = new Corso(codicedelcorso, nomecorso, sito, tipo,campus, accesso);
//                    mListaCorsiAgraria.add(corso);
//                }
//
//
//                final ArrayList<String> listcorsiagraria = new ArrayList<String>();
//
//                for (int i=0; i<mListaCorsiAgraria.size(); i++) {
//                    listcorsiagraria.add(mListaCorsiAgraria.get(i).getNome());
//                }
//
//
//                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, listcorsiagraria);
//
//                //inietto i dati
//                elencoagraria.setAdapter(adapter);
//                setListViewHeightBasedOnChildren(elencoagraria);
//            }
//
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
//
//        final ImageButton agrariaplusButton = (ImageButton) findViewById(agrariaplus);
//        agrariaplusButton.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//              if (elencoagraria.getVisibility() == View.GONE) {
//               elencoagraria.setVisibility(view.VISIBLE);
//               agrariaplusButton.setImageResource(R.drawable.meno);
//
//        } else {
//             elencoagraria.setVisibility(view.GONE);
//             agrariaplusButton.setImageResource(R.drawable.piu);
//        }
//        }
//
//        });
//
//        elencoagraria.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> adattatore, View view, int pos, long l) {
//        // recupero il titolo memorizzato nella riga tramite l'ArrayAdapter
//          final String titoloriga = (String) adattatore.getItemAtPosition(pos);
//          for (int i=0; i<mListaCorsiAgraria.size(); i++) {
//              if (mListaCorsiAgraria.get(i).getNome().equals(titoloriga)) {
//                  String codicecorsoagraria = mListaCorsiAgraria.get(i).getScuolaId();
//                  String url = mListaCorsiAgraria.get(i).getUrl();
//                  String tipo = mListaCorsiAgraria.get(i).getTipo();
//                  String campus = mListaCorsiAgraria.get(i).getCampus();
//                  String accesso = mListaCorsiAgraria.get(i).getAccesso();
//
//                  richiamoPaginaInterna(titoloriga, codicecorsoagraria,url,"agraria", tipo, campus,accesso);
//                  break;
//              }
//          }
//
//
//        }
//        });
//
//        //ECONOMIA
//
//        final ListView elencoeconomia = (ListView) findViewById(R.id.listeconomia);
//
//        final ArrayList<Corso> mListaCorsiEconomia = new ArrayList<Corso>();
//
//        Query queryeconomia =  ref.child("corso/economia");
//
//        queryeconomia.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    String codicedelcorso = (String) String.valueOf(data.child("corso_codice").getValue());
//                    String nomecorso = (String) data.child("corso_descrizione").getValue();
//                    String sito = (String) data.child("url").getValue();
//                    String tipo = (String) data.child("tipologia").getValue();
//                    String campus =(String) data.child("campus").getValue();
//                    String accesso =(String) data.child("accesso").getValue();
//
//                    Corso corso = new Corso(codicedelcorso, nomecorso, sito, tipo, campus, accesso);
//                    mListaCorsiEconomia.add(corso);
//                }
//
//
//                final ArrayList<String> listcorsieconomia = new ArrayList<String>();
//
//                for (int i=0; i<mListaCorsiEconomia.size(); i++) {
//                    listcorsieconomia.add(mListaCorsiEconomia.get(i).getNome());
//                }
//
//
//                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, listcorsieconomia);
//
//                //inietto i dati
//                elencoeconomia.setAdapter(adapter);
//                setListViewHeightBasedOnChildren(elencoeconomia);
//            }
//
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
//
//
//        final ImageButton economiaplusButton = (ImageButton) findViewById(economiaplus);
//        economiaplusButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (elencoeconomia.getVisibility() == View.GONE) {
//                    elencoeconomia.setVisibility(view.VISIBLE);
//                    economiaplusButton.setImageResource(R.drawable.meno);
//
//                } else {
//                    elencoeconomia.setVisibility(view.GONE);
//                    economiaplusButton.setImageResource(R.drawable.piu);
//                }
//            }
//
//        });
//
//        elencoeconomia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adattatore, View view, int pos, long l) {
//                // recupero il titolo memorizzato nella riga tramite l'ArrayAdapter
//                final String titoloriga = (String) adattatore.getItemAtPosition(pos);
//                for (int i=0; i<mListaCorsiEconomia.size(); i++) {
//                    if (mListaCorsiEconomia.get(i).getNome().equals(titoloriga)) {
//                        String codicecorsoeconomia = mListaCorsiEconomia.get(i).getScuolaId();
//                        String url = mListaCorsiEconomia.get(i).getUrl();
//                        String tipo = mListaCorsiEconomia.get(i).getTipo();
//                        String campus = mListaCorsiEconomia.get(i).getCampus();
//                        String accesso = mListaCorsiAgraria.get(i).getAccesso();
//
//                        richiamoPaginaInterna(titoloriga, codicecorsoeconomia,url,"economia",tipo, campus,accesso);
//                        break;
//                    }
//                }
//
//
//            }
//        });
//
//        //FARMACIA
//
//        final ListView elencofarmacia = (ListView) findViewById(R.id.listafarmacia);
//
//        final ArrayList<Corso> mListaCorsiFarmacia = new ArrayList<Corso>();
//
//        Query queryfarmacia =  ref.child("corso/farmacia");
//
//        queryfarmacia.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    String codicedelcorso = (String) String.valueOf(data.child("corso_codice").getValue());
//                    String nomecorso = (String) data.child("corso_descrizione").getValue();
//                    String sito = (String) data.child("url").getValue();
//                    String tipo = (String) data.child("tipologia").getValue();
//                    String campus =(String) data.child("campus").getValue();
//                    String accesso =(String) data.child("accesso").getValue();
//
//                    Corso corso = new Corso(codicedelcorso, nomecorso, sito, tipo, campus, accesso);
//                    mListaCorsiFarmacia.add(corso);
//                }
//
//
//                final ArrayList<String> listcorsifarmacia = new ArrayList<String>();
//
//                for (int i=0; i<mListaCorsiFarmacia.size(); i++) {
//                    listcorsifarmacia.add(mListaCorsiFarmacia.get(i).getNome());
//                }
//
//
//                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, listcorsifarmacia);
//
//                //inietto i dati
//                elencofarmacia.setAdapter(adapter);
//                setListViewHeightBasedOnChildren(elencofarmacia);
//            }
//
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
//
//
//        final ImageButton farmaciaplusButton = (ImageButton) findViewById(farmaciaplus);
//        farmaciaplusButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (elencofarmacia.getVisibility() == View.GONE) {
//                    elencofarmacia.setVisibility(view.VISIBLE);
//                    farmaciaplusButton.setImageResource(R.drawable.meno);
//
//                } else {
//                    elencofarmacia.setVisibility(view.GONE);
//                    farmaciaplusButton.setImageResource(R.drawable.piu);
//                }
//            }
//
//        });
//
//        elencofarmacia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adattatore, View view, int pos, long l) {
//                // recupero il titolo memorizzato nella riga tramite l'ArrayAdapter
//                final String titoloriga = (String) adattatore.getItemAtPosition(pos);
//                for (int i=0; i<mListaCorsiFarmacia.size(); i++) {
//                    if (mListaCorsiFarmacia.get(i).getNome().equals(titoloriga)) {
//                        String codicecorsofarmacia = mListaCorsiFarmacia.get(i).getScuolaId();
//                        String url = mListaCorsiFarmacia.get(i).getUrl();
//                        String tipo = mListaCorsiFarmacia.get(i).getTipo();
//                        String campus = mListaCorsiFarmacia.get(i).getCampus();
//                        String accesso = mListaCorsiAgraria.get(i).getAccesso();
//                        richiamoPaginaInterna(titoloriga, codicecorsofarmacia,url,"farmacia",tipo, campus, accesso);
//                        break;
//                    }
//                }
//
//
//            }
//        });
//
//        //GIURISPRUDENZA
//
//        final ListView elencogiuri = (ListView) findViewById(R.id.listagiuri);
//
//        final ArrayList<Corso> mListaCorsiGiuri = new ArrayList<Corso>();
//
//        Query querygiuri =  ref.child("corso/giurisprudenza");
//
//        querygiuri.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    String codicedelcorso = (String) String.valueOf(data.child("corso_codice").getValue());
//                    String nomecorso = (String) data.child("corso_descrizione").getValue();
//                    String sito = (String) data.child("url").getValue();
//                    String tipo = (String) data.child("tipologia").getValue();
//                    String campus =(String) data.child("campus").getValue();
//                    String accesso =(String) data.child("accesso").getValue();
//
//                    Corso corso = new Corso(codicedelcorso, nomecorso, sito, tipo, campus, accesso);
//                    mListaCorsiGiuri.add(corso);
//                }
//
//
//                final ArrayList<String> listcorsigiuri = new ArrayList<String>();
//
//                for (int i=0; i<mListaCorsiGiuri.size(); i++) {
//                    listcorsigiuri.add(mListaCorsiGiuri.get(i).getNome());
//                }
//
//
//                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, listcorsigiuri);
//
//                //inietto i dati
//                elencogiuri.setAdapter(adapter);
//                setListViewHeightBasedOnChildren(elencogiuri);
//            }
//
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
//
//
//        final ImageButton giuriplusButton = (ImageButton) findViewById(giuriplus);
//        giuriplusButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (elencogiuri.getVisibility() == View.GONE) {
//                    elencogiuri.setVisibility(view.VISIBLE);
//                    giuriplusButton.setImageResource(R.drawable.meno);
//
//                } else {
//                    elencogiuri.setVisibility(view.GONE);
//                    giuriplusButton.setImageResource(R.drawable.piu);
//                }
//            }
//
//        });
//
//        elencogiuri.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adattatore, View view, int pos, long l) {
//                // recupero il titolo memorizzato nella riga tramite l'ArrayAdapter
//                final String titoloriga = (String) adattatore.getItemAtPosition(pos);
//                for (int i=0; i<mListaCorsiGiuri.size(); i++) {
//                    if (mListaCorsiGiuri.get(i).getNome().equals(titoloriga)) {
//                        String codicecorsogiuri = mListaCorsiGiuri.get(i).getScuolaId();
//                        String url = mListaCorsiGiuri.get(i).getUrl();
//                        String tipo = mListaCorsiGiuri.get(i).getTipo();
//                        String campus = mListaCorsiGiuri.get(i).getCampus();
//                        String accesso = mListaCorsiAgraria.get(i).getAccesso();
//                        richiamoPaginaInterna(titoloriga, codicecorsogiuri,url,"giurisprudenza", tipo, campus, accesso);
//                        break;
//                    }
//                }
//
//
//            }
//        });
    }
}