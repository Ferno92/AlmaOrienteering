package com.almaorient.ferno92.almaorienteering;

import android.content.ClipData;
import android.content.Intent;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.almaorient.ferno92.almaorienteering.PianoStudi.ExpandableListAdapter1;
import com.almaorient.ferno92.almaorienteering.PianoStudi.NewPianoStudiModel;
import com.almaorient.ferno92.almaorienteering.PianoStudi.PianoStudiModel2;
import com.almaorient.ferno92.almaorienteering.recensioni.ListaRecensioniActivity;
import com.almaorient.ferno92.almaorienteering.strutturaUnibo.Scuola;
import com.almaorient.ferno92.almaorienteering.versus.VersusActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static android.R.attr.data;
import static android.R.attr.logo;
import static android.R.drawable.btn_minus;
import static android.R.drawable.btn_plus;
import static com.almaorient.ferno92.almaorienteering.R.id.agrariaplus;
import static com.almaorient.ferno92.almaorienteering.R.id.default_activity_button;
import static com.almaorient.ferno92.almaorienteering.R.id.primoannolistview;
import static com.almaorient.ferno92.almaorienteering.R.id.primoannoplus;
import static com.almaorient.ferno92.almaorienteering.R.id.tab1;
import static com.almaorient.ferno92.almaorienteering.R.id.tab_host;
import static com.almaorient.ferno92.almaorienteering.R.id.terzoannolayout;
import static com.almaorient.ferno92.almaorienteering.R.id.window;

public class DettagliCorsoActivity extends BaseActivity {

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

    public static final Scuola[] mScuolaadatt = new Scuola[]{
            new Scuola("", "Seleziona scuola"),
            new Scuola("agraria", "Agraria e Medicina veterinaria"),
            new Scuola("economia", "Economia, Mangement e Statistica"),
            new Scuola("farmacia", "Farmacia, Biotecnologie e Scienze motorie"),
            new Scuola("giurisprudenza", "Giurisprudenza"),
            new Scuola("ingegneria", "Ingegneria e architettura"),
            new Scuola("lettere", "Lettere e Beni culturali"),
            new Scuola("lingue", "Lingue e letterature, Traduzione e Interpretazione"),
            new Scuola("medicina", "Medicina e Chirurgia"),
            new Scuola("psicologia", "Psicologia e Scienze della formazione"),
            new Scuola("scienze", "Scienze"),
            new Scuola("scienze_politiche", "Scienze politiche")
    };

    //////BROWSER
    private void richiamoBrowser(String url) {
        Intent browser = new Intent(this, EmbedBrowser.class);
        browser.putExtra("url", url);
        startActivity(browser);
    }

    private void statistiche(String scuola1, String scuola2, int corso1, int corso2) {
        Intent statistiche = new Intent(this, VersusActivity.class);
        statistiche.putExtra("scuola1", scuola1);
        statistiche.putExtra("scuola2", scuola2);
        statistiche.putExtra("pos1", corso1);
        statistiche.putExtra("pos2", corso2);
        startActivity(statistiche);
    }

    private void mappe(Long idscuola, Integer codcorso, String Activity) {
        Intent maps = new Intent(this, MapsActivity.class);
        maps.putExtra("idscuola", idscuola);
        maps.putExtra("codcorso", codcorso);
        maps.putExtra("CallingActivity",Activity);
        startActivity(maps);
    }

    TabHost tabhost;


    private ExpandableListView listView;
    private ExpandableListAdapter1 listAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettagli_corso);



        //TextView nomecorsoText = (TextView) findViewById(R.id.nomecorso);
        TextView tipocorsoText = (TextView) findViewById(R.id.tipotxtview);
        TextView campuscorsoText = (TextView) findViewById(R.id.campustxtview);
        TextView accessoText = (TextView) findViewById(R.id.tipoaccessoview);
        Button sitocorsobtn = (Button) findViewById(R.id.sitocorsobtn);
        Button recensioniCorsoButton = (Button) findViewById(R.id.button_recensioni);

        final String corso = getIntent().getExtras().getString("Vocecliccata");
        final String scuola = getIntent().getExtras().getString("Nomescuola");
        final String corsocodice = getIntent().getExtras().getString("Codicecorso");
        final String urlcorso = getIntent().getExtras().getString("Sitocorso");
        final String tipo = getIntent().getExtras().getString("Tipocorso");
        String campus = getIntent().getExtras().getString("Campus");
        String accesso = getIntent().getExtras().getString("Accesso");
        final Long scuolaid = getIntent().getExtras().getLong("IdScuola");


        Resources res = getResources();
        TabHost tab = (TabHost) findViewById(R.id.tab_host);
        tab.setup();
        TabHost.TabSpec spec;

//        tab.setup();

        //Tab 1
        spec=tab.newTabSpec("Tab 1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Info");
        tab.addTab(spec);

        //Tab 1
        spec=tab.newTabSpec("Tab 2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Descrizione");
        tab.addTab(spec);

        //Tab 1
        spec=tab.newTabSpec("Tab 3");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Piano didattico");
        tab.addTab(spec);

        //Tab 1
        spec=tab.newTabSpec("Tab 4");
        spec.setContent(R.id.tab4);
        spec.setIndicator("Sbocchi");
        tab.addTab(spec);


//        tab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
//            @Override
//            public void onTabChanged(String s) {
//
//            }
//        });

        tab.setCurrentTab(0);



        setTitle(corso);

        tipocorsoText.setText(tipo);
        campuscorsoText.setText(campus);
        accessoText.setText(accesso);


        sitocorsobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                richiamoBrowser(urlcorso);
            }
        });

        recensioniCorsoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent recensioniIntent = new Intent(getApplicationContext(), ListaRecensioniActivity.class);
                recensioniIntent.putExtra("nome_corso", corso);
                recensioniIntent.putExtra("scuola", scuola);
                recensioniIntent.putExtra("codice_corso", corsocodice);
                startActivity(recensioniIntent);
            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference();

        final Query posstats =  ref.child("statistiche/"+scuola).orderByChild("codice_corso").equalTo(Integer.parseInt(corsocodice));

        posstats.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data1 : dataSnapshot.getChildren()) {
                    final String pos = (String) String.valueOf(data1.getKey());

                    final Button buttonstats = (Button) findViewById(R.id.buttonstats);

                    buttonstats.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int a = Integer.parseInt(pos);
                            statistiche(scuola, scuola, a , 0);

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        final ImageButton descrplus = (ImageButton) findViewById(R.id.descrizioneplus);
        final TextView descrizione = (TextView) findViewById(R.id.introduziontetxt);
//        descrplus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (descrizione.getVisibility() == View.GONE) {
//                    descrizione.setVisibility(view.VISIBLE);
//                    descrplus.setImageResource(R.drawable.ic_expand_less);
//
//                } else {
//                    descrizione.setVisibility(view.GONE);
//                    descrplus.setImageResource(R.drawable.ic_expand_more);
//                }
//            }
//
//        });

        final Button maps = (Button) findViewById(R.id.mapsbutton);

        Query query4 = ref.child("corso/" + mScuolaadatt[(int) (long) scuolaid].getScuolaId()).orderByChild("corso_codice")
                .equalTo(Integer.parseInt(corsocodice));
        query4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //int i = 0;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    final Integer spinneridcorso = (Integer) Integer.parseInt(data.getKey());
                    final Integer spinneridcorso2 = spinneridcorso+1;
                    //Log.d("size lista aule", String.valueOf(mListaAule.size()));
                    //initMap();
                    maps.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mappe(scuolaid, spinneridcorso2, "dettagliCorso");
                        }

                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (databaseError != null) {

                }
            }
        });



        final ListView esamiprimoanno = (ListView) findViewById(R.id.primoannolistview);
        final ListView esamisecondoanno = (ListView) findViewById(R.id.secondoannolistview);
        final ListView esamiterzoanno = (ListView) findViewById(R.id.terzoannolistview);
        final ListView esamiquartoanno = (ListView) findViewById(R.id.quartoannolistview);
        final ListView esamiquintoanno = (ListView) findViewById(R.id.quintoannolistview);


//PIANI DI STUDIO FIREBASE

        final ArrayList<PianoStudiModel2> mListaEsami1 = new ArrayList<PianoStudiModel2>();



        Query query =  ref.child("esami/"+scuola);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String codicedelcorso = (String) String.valueOf(data.child("corso_codice").getValue());
                    String materia = (String) data.child("materia_descrizione").getValue();
                    String sito = (String) data.child("url").getValue();


                    PianoStudiModel2 corso = new PianoStudiModel2(codicedelcorso, materia, sito);
                    mListaEsami1.add(corso);

                }


                final ArrayList<String> listtotcorso = new ArrayList<String>();



                for (int i=0; i<mListaEsami1.size(); i++) {
                    if (mListaEsami1.get(i).getCodiceCorso().equals(corsocodice)) {
                        listtotcorso.add(mListaEsami1.get(i).getNomeMateria());
                    }
                }

                final ArrayList<String> listannouno = new ArrayList<String>();
                for (int i=0; i<5; i++) {
                        listannouno.add(listtotcorso.get(i));
                }

                final ArrayList<String> listannodue = new ArrayList<String>();
                for (int i=5; i<10; i++) {
                    listannodue.add(listtotcorso.get(i));
                }

                final ArrayList<String> listannotre = new ArrayList<String>();
                if (tipo.equals("Laurea") || tipo.equals("Laurea Magistrale a ciclo unico")) {

                    for (int i = 10; i < 15; i++) {
                        listannotre.add(listtotcorso.get(i));
                    }
                }

                final ArrayList<String> listannoquattro = new ArrayList<String>();
                final ArrayList<String> listannocinque = new ArrayList<String>();

                if(tipo.equals("Laurea Magistrale a ciclo unico")) {

                    for (int i = 15; i < 20; i++) {
                        listannoquattro.add(listtotcorso.get(i));
                    }

                    for (int i = 20; i < 25; i++) {
                        listannocinque.add(listtotcorso.get(i));
                    }
                }


                final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, listannouno);
                final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, listannodue);
                final ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, listannotre);
                final ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, listannoquattro);
                final ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, listannocinque);

                //inietto i dati
                esamiprimoanno.setAdapter(adapter1);
                setListViewHeightBasedOnChildren(esamiprimoanno);
                esamisecondoanno.setAdapter(adapter2);
                setListViewHeightBasedOnChildren(esamisecondoanno);
                esamiterzoanno.setAdapter(adapter3);
                setListViewHeightBasedOnChildren(esamiterzoanno);
                esamiquartoanno.setAdapter(adapter4);
                setListViewHeightBasedOnChildren(esamiquartoanno);
                esamiquintoanno.setAdapter(adapter5);
                setListViewHeightBasedOnChildren(esamiquintoanno);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        final ImageButton primoannoplusbtn = (ImageButton) findViewById(primoannoplus);
        primoannoplusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (esamiprimoanno.getVisibility() == View.GONE) {
                    esamiprimoanno.setVisibility(view.VISIBLE);
                    primoannoplusbtn.setImageResource(R.drawable.ic_expand_less);

                } else {
                    esamiprimoanno.setVisibility(view.GONE);
                    primoannoplusbtn.setImageResource(R.drawable.ic_expand_more);
                }
            }

        });

        esamiprimoanno.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adattatore, View view, int pos, long l) {
                // recupero il titolo memorizzato nella riga tramite l'ArrayAdapter
                final String esamecliccato = (String) adattatore.getItemAtPosition(pos);
                for (int i=0; i<mListaEsami1.size(); i++) {
                    if (mListaEsami1.get(i).getNomeMateria().equals(esamecliccato)) {
                        if(mListaEsami1.get(i).getIndirizzoWeb().contains("http://")) {
                            richiamoBrowser(mListaEsami1.get(i).getIndirizzoWeb());
                        }
                        break;
                    }
                }

            }

        });

        final ImageButton secondoannoplusbtn = (ImageButton) findViewById(R.id.secondoannoplus);
        secondoannoplusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (esamisecondoanno.getVisibility() == View.GONE) {
                    esamisecondoanno.setVisibility(view.VISIBLE);
                    secondoannoplusbtn.setImageResource(R.drawable.ic_expand_less);

                } else {
                    esamisecondoanno.setVisibility(view.GONE);
                    secondoannoplusbtn.setImageResource(R.drawable.ic_expand_more);
                }
            }

        });

        esamisecondoanno.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adattatore, View view, int pos, long l) {
                // recupero il titolo memorizzato nella riga tramite l'ArrayAdapter
                final String esamecliccato2 = (String) adattatore.getItemAtPosition(pos);
                for (int i=5; i<mListaEsami1.size(); i++) {
                    if (mListaEsami1.get(i).getNomeMateria().equals(esamecliccato2)) {
                        if(mListaEsami1.get(i).getIndirizzoWeb().contains("http://")) {
                            richiamoBrowser(mListaEsami1.get(i).getIndirizzoWeb());
                        }
                        break;
                    }
                }

            }

        });

        if (tipo.equals("Laurea" )|| tipo.equals("Laurea Magistrale a ciclo unico")) {
            RelativeLayout terzoanno = (RelativeLayout) findViewById(R.id.terzoannolayout);
            terzoanno.setVisibility(View.VISIBLE);


            final ImageButton terzoannoplusbtn = (ImageButton) findViewById(R.id.terzoannoplus);
            terzoannoplusbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (esamiterzoanno.getVisibility() == View.GONE) {
                        esamiterzoanno.setVisibility(view.VISIBLE);
                        terzoannoplusbtn.setImageResource(R.drawable.ic_expand_less);

                    } else {
                        esamiterzoanno.setVisibility(view.GONE);
                        terzoannoplusbtn.setImageResource(R.drawable.ic_expand_more);
                    }
                }

            });


            esamiterzoanno.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adattatore, View view, int pos, long l) {
                    // recupero il titolo memorizzato nella riga tramite l'ArrayAdapter
                    final String esamecliccato3 = (String) adattatore.getItemAtPosition(pos);
                    for (int i = 10; i < mListaEsami1.size(); i++) {
                        if (mListaEsami1.get(i).getNomeMateria().equals(esamecliccato3)) {
                            if (mListaEsami1.get(i).getIndirizzoWeb().contains("http://")) {
                                richiamoBrowser(mListaEsami1.get(i).getIndirizzoWeb());
                            }
                            break;
                        }
                    }

                }

            });
        }

        if (tipo.equals("Laurea Magistrale a ciclo unico")) {
            RelativeLayout quartoanno = (RelativeLayout) findViewById(R.id.quartoannolayout);
            quartoanno.setVisibility(View.VISIBLE);

            RelativeLayout quintoanno = (RelativeLayout) findViewById(R.id.quintoannolayout);
            quintoanno.setVisibility(View.VISIBLE);

            final ImageButton quartoannoplusbtn = (ImageButton) findViewById(R.id.quartoannoplus);
            quartoannoplusbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (esamiquartoanno.getVisibility() == View.GONE) {
                        esamiquartoanno.setVisibility(view.VISIBLE);
                        quartoannoplusbtn.setImageResource(R.drawable.ic_expand_less);

                    } else {
                        esamiquartoanno.setVisibility(view.GONE);
                        quartoannoplusbtn.setImageResource(R.drawable.ic_expand_more);
                    }
                }

            });

            esamiquartoanno.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adattatore, View view, int pos, long l) {
                    // recupero il titolo memorizzato nella riga tramite l'ArrayAdapter
                    final String esamecliccato4 = (String) adattatore.getItemAtPosition(pos);
                    for (int i = 15; i < mListaEsami1.size(); i++) {
                        if (mListaEsami1.get(i).getNomeMateria().equals(esamecliccato4)) {
                            if (mListaEsami1.get(i).getIndirizzoWeb().contains("http://")) {
                                richiamoBrowser(mListaEsami1.get(i).getIndirizzoWeb());
                            }
                            break;
                        }
                    }

                }

            });

            final ImageButton quintoannoplusbtn = (ImageButton) findViewById(R.id.quintoannoplus);
            quintoannoplusbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (esamiquintoanno.getVisibility() == View.GONE) {
                        esamiquintoanno.setVisibility(view.VISIBLE);
                        quintoannoplusbtn.setImageResource(R.drawable.ic_expand_less);

                    } else {
                        esamiquintoanno.setVisibility(view.GONE);
                        quintoannoplusbtn.setImageResource(R.drawable.ic_expand_more);
                    }
                }

            });

            esamiquintoanno.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adattatore, View view, int pos, long l) {
                    // recupero il titolo memorizzato nella riga tramite l'ArrayAdapter
                    final String esamecliccato5 = (String) adattatore.getItemAtPosition(pos);
                    for (int i = 20; i < mListaEsami1.size(); i++) {
                        if (mListaEsami1.get(i).getNomeMateria().equals(esamecliccato5)) {
                            if (mListaEsami1.get(i).getIndirizzoWeb().contains("http://")) {
                                richiamoBrowser(mListaEsami1.get(i).getIndirizzoWeb());
                            }
                            break;
                        }
                    }

                }

            });
        }


//FINE PARTE PIANI DI STUDIO FIREBASE

        ///PIANI STUDIO CON TABHOST

        final ArrayList <String> nomicurriculum = new ArrayList();
            ///mScuolaadatt[(int) (long) scuolaid].getScuolaId()+"/"+Integer.parseInt(corsocodice)+
        final Integer[] a = {0};
        final Query query5 = ref.child("piani_studio/economia/9202/curriculum");
        query5.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //int i = 0;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String curriculum = (String) data.child("nomecurr").getValue();

                    //prendo dal database i nomi dei diversi curriculum
//                    if(nomicurriculum.isEmpty() || !nomicurriculum.contains(curriculum)){
//                        nomicurriculum.add(curriculum);
//                    }
//
                }

//                TextView prova = (TextView) findViewById(R.id.prova);
//                prova.setText(String.valueOf(nomicurriculum.size()));

                for (int i=0; i<nomicurriculum.size(); i++){
                    int a=i+1;
//                    String buttonIDname = "curr" + String.valueOf(a);
//                    int buttonID = getResources().getIdentifier(buttonIDname, "id", getPackageName());
//                    final Button curr = (Button) findViewById(buttonID);
//                    curr.setVisibility(View.VISIBLE);
//                    curr.setText(nomicurriculum.get(i));
//                    curr.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            ExpandableListView curr1anno1 = (ExpandableListView) findViewById(R.id.curr1anno1);
//                            curr1anno1.setVisibility(View.VISIBLE);
//                        }
//                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (databaseError != null) {

                }
            }
        });




        final ArrayList <NewPianoStudiModel> elencoinsegnamenti = new ArrayList<>();

        //HashMap<Integer,>

        final Query query6 = ref.child("piani_studio/ingegneria/").child(corsocodice);
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList chiavi = (ArrayList) dataSnapshot.getValue();

                for (int i = 0; i < chiavi.size(); i++) {
                    HashMap meMap = (HashMap) chiavi.get(i);
                    Iterator elenco = meMap.keySet().iterator();

                    String nomeinsegnamento = "";
                    Integer padre = 0;
                    Integer radice = 0;
                    String url = "";

                    while (elenco.hasNext()) {
                        String esamiKey = (String) elenco.next();
                        switch (esamiKey) {
                            case NewPianoStudiModel.NOME_INSEGNAMENTO:
                                nomeinsegnamento = (String) meMap.get(esamiKey);
                                break;
                            case NewPianoStudiModel.PADRE:
                                padre = (Integer) (int) (long) meMap.get(esamiKey);
                                break;
                            case NewPianoStudiModel.RADICE:
                                radice = (Integer) (int) (long) meMap.get(esamiKey);
                                break;
                            case NewPianoStudiModel.URL:
                                url = (String) meMap.get(esamiKey);
                                break;
                            default:
                                break;
                        }
                    }
                    NewPianoStudiModel insegnamento = new NewPianoStudiModel(nomeinsegnamento, padre, radice, url);
                    elencoinsegnamenti.add(insegnamento);
                }

                ListView prova = (ListView) findViewById(R.id.listviewprova);
                ArrayAdapter<NewPianoStudiModel> provaadapter = new ArrayAdapter<NewPianoStudiModel>(getApplicationContext(), R.layout.simple_list_item_long_corsi, elencoinsegnamenti);

                prova.setAdapter(provaadapter);

                ArrayList <String> corsipadrititle = new ArrayList<String>();
                ArrayList <NewPianoStudiModel> corsipadri = new ArrayList<NewPianoStudiModel>();

                HashMap mappa = new HashMap<>();
                Integer b=-1;

                for (int i=0; i<elencoinsegnamenti.size();i++){
                    if (elencoinsegnamenti.get(i).getPadre()==0){
                        corsipadrititle.add(elencoinsegnamenti.get(i).getCorsoNome());
                        corsipadri.add(elencoinsegnamenti.get(i));
                        b=b+1;
                        List <String> listafigli = new ArrayList<String>();
                        for (int a=0; a<elencoinsegnamenti.size(); a++){
                            if (elencoinsegnamenti.get(a).getPadre().equals(elencoinsegnamenti.get(i).getRadice())){
                                listafigli.add(elencoinsegnamenti.get(a).getCorsoNome());
                                TextView prova2 = (TextView) findViewById(R.id.prova);
                                prova2.setText(String.valueOf(listafigli.size()));
                            }
                        }
                        mappa.put(corsipadrititle.get(b),listafigli);


                    }
                }

                listView = (ExpandableListView)findViewById(R.id.anno2);
                //initData();
                listAdapter = new ExpandableListAdapter1(getApplicationContext(),corsipadrititle,mappa);
                listView.setAdapter(listAdapter);


            }


//                    ExpandableListView anno1 = (ExpandableListView) findViewById(R.id.anno1);
//
//                    ArrayAdapter <NewPianoStudiModel> esami1 = new ArrayAdapter<NewPianoStudiModel>(getApplicationContext(),R.layout.simple_list_item,elencoinsegnamenti);
//
//                    anno1.setAdapter(esami1);

                //}




            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (databaseError != null) {

                }
            }
        });




    }

//    private void initData(ArrayList<String> Arraylist, List<String> list, HashMap hashmap) {
//        listDataHeader = Arraylist;
//        listHash = hashmap;
//
////        listDataHeader.add("EDMTDev");
////        listDataHeader.add("Android");
////        listDataHeader.add("Xamarin");
////        listDataHeader.add("UWP");
//
//        List<String> edmtDev = new ArrayList<>();
//        edmtDev.add("This is Expandable ListView");
//
//        List<String> androidStudio = new ArrayList<>();
//        androidStudio.add("Expandable ListView");
//        androidStudio.add("Google Map");
//        androidStudio.add("Chat Application");
//        androidStudio.add("Firebase ");
//
//        List<String> xamarin = new ArrayList<>();
//        xamarin.add("Xamarin Expandable ListView");
//        xamarin.add("Xamarin Google Map");
//        xamarin.add("Xamarin Chat Application");
//        xamarin.add("Xamarin Firebase ");
//
//        List<String> uwp = new ArrayList<>();
//        uwp.add("UWP Expandable ListView");
//        uwp.add("UWP Google Map");
//        uwp.add("UWP Chat Application");
//        uwp.add("UWP Firebase ");
//
//        listHash.put(listDataHeader.get(0),edmtDev);
//        listHash.put(listDataHeader.get(1),androidStudio);
//        listHash.put(listDataHeader.get(2),xamarin);
//        listHash.put(listDataHeader.get(3),uwp);
//    }
}





