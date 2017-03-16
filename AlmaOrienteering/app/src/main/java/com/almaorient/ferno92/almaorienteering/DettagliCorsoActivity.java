package com.almaorient.ferno92.almaorienteering;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.almaorient.ferno92.almaorienteering.PianoStudi.PianoStudiModel2;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;
import static android.R.attr.logo;
import static android.R.drawable.btn_minus;
import static android.R.drawable.btn_plus;
import static com.almaorient.ferno92.almaorienteering.R.id.agrariaplus;
import static com.almaorient.ferno92.almaorienteering.R.id.primoannolistview;
import static com.almaorient.ferno92.almaorienteering.R.id.primoannoplus;
import static com.almaorient.ferno92.almaorienteering.R.id.window;

public class DettagliCorsoActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_dettagli_corso);

        TextView nomecorsoText = (TextView) findViewById(R.id.nomecorso);
        TextView tipocorsoText = (TextView) findViewById(R.id.tipotxtview);
        TextView campuscorsoText = (TextView) findViewById(R.id.campustxtview);
        Button sitocorsobtn = (Button) findViewById(R.id.sitocorsobtn);

        String corso = getIntent().getExtras().getString("Vocecliccata");
        String scuola = getIntent().getExtras().getString("Nomescuola");
        final String corsocodice = getIntent().getExtras().getString("Codicecorso");
        final String urlcorso = getIntent().getExtras().getString("Sitocorso");
        String tipo = getIntent().getExtras().getString("Tipocorso");
        String campus = getIntent().getExtras().getString("Campus");

        nomecorsoText.setText(corso);
        tipocorsoText.setText(tipo);
        campuscorsoText.setText(campus);

        sitocorsobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(urlcorso));
                startActivity(i);
            }
        });

        final ListView esamiprimoanno = (ListView) findViewById(R.id.primoannolistview);
        final ListView esamisecondoanno = (ListView) findViewById(R.id.secondoannolistview);
        final ListView esamiterzoanno = (ListView) findViewById(R.id.terzoannolistview);


//PIANI DI STUDIO FIREBASE

        final ArrayList<PianoStudiModel2> mListaEsami1 = new ArrayList<PianoStudiModel2>();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
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
                for (int i=10; i<15; i++) {
                    listannotre.add(listtotcorso.get(i));
                }


                final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, listannouno);
                final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, listannodue);
                final ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, listannotre);


                //inietto i dati
                esamiprimoanno.setAdapter(adapter1);
                setListViewHeightBasedOnChildren(esamiprimoanno);
                esamisecondoanno.setAdapter(adapter2);
                setListViewHeightBasedOnChildren(esamisecondoanno);
                esamiterzoanno.setAdapter(adapter3);
                setListViewHeightBasedOnChildren(esamiterzoanno);
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
                    primoannoplusbtn.setImageResource(R.drawable.meno);

                } else {
                    esamiprimoanno.setVisibility(view.GONE);
                    primoannoplusbtn.setImageResource(R.drawable.piu);
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
                            final String urlesame = mListaEsami1.get(i).getIndirizzoWeb();
                            Intent a = new Intent(Intent.ACTION_VIEW, Uri.parse(urlesame));
                            startActivity(a);
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
                    secondoannoplusbtn.setImageResource(R.drawable.meno);

                } else {
                    esamisecondoanno.setVisibility(view.GONE);
                    secondoannoplusbtn.setImageResource(R.drawable.piu);
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
                            final String urlesame2 = mListaEsami1.get(i).getIndirizzoWeb();
                            Intent a = new Intent(Intent.ACTION_VIEW, Uri.parse(urlesame2));
                            startActivity(a);
                        }
                        break;
                    }
                }

            }

        });

        final ImageButton terzoannoplusbtn = (ImageButton) findViewById(R.id.terzoannoplus);
        terzoannoplusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (esamiterzoanno.getVisibility() == View.GONE) {
                    esamiterzoanno.setVisibility(view.VISIBLE);
                    terzoannoplusbtn.setImageResource(R.drawable.meno);

                } else {
                    esamiterzoanno.setVisibility(view.GONE);
                    terzoannoplusbtn.setImageResource(R.drawable.piu);
                }
            }

        });

        esamiterzoanno.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adattatore, View view, int pos, long l) {
                // recupero il titolo memorizzato nella riga tramite l'ArrayAdapter
                final String esamecliccato3 = (String) adattatore.getItemAtPosition(pos);
                for (int i=10; i<mListaEsami1.size(); i++) {
                    if (mListaEsami1.get(i).getNomeMateria().equals(esamecliccato3)) {
                        if(mListaEsami1.get(i).getIndirizzoWeb().contains("http://")) {
                            final String urlesame3 = mListaEsami1.get(i).getIndirizzoWeb();
                            Intent a = new Intent(Intent.ACTION_VIEW, Uri.parse(urlesame3));
                            startActivity(a);
                        }
                        break;
                    }
                }

            }

        });
//FINE PARTE PIANI DI STUDIO FIREBASE

    }
}
