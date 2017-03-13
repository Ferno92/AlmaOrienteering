package com.almaorient.ferno92.almaorienteering;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import static android.R.drawable.btn_minus;
import static android.R.drawable.btn_plus;
import static com.almaorient.ferno92.almaorienteering.R.id.agrariaplus;
import static com.almaorient.ferno92.almaorienteering.R.id.plustre;

public class ElencoScuoleActivity extends AppCompatActivity {

    private void richiamoPaginaInterna(String datopassato) {
        Intent nuovapagina = new Intent(this, DettagliCorsoActivity.class);
        nuovapagina.putExtra("Vocecliccata", datopassato);
        startActivity(nuovapagina);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.elenco_scuole);

        // definisco un array di stringhe
        final String[] corsiagraria = new String[]{"Acquacoltura e igiene delle produzioni ittiche [L]",
                                                    "Biotecnologie animali [LM]",
                                                    "Economia e marketing nel sistema agro-industriale [L]"};

        // definisco un ArrayList
        final ArrayList<String> listp = new ArrayList<String>();
        for (int i = 0; i < corsiagraria.length; ++i) {
            listp.add(corsiagraria[i]);
        }
        // recupero la lista dal layout
        final ListView elencoagraria = (ListView) findViewById(R.id.listview1);

        // creo e istruisco l'adattatore
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listp);

        // inietto i dati
        elencoagraria.setAdapter(adapter);

        final String[] titoloriga = new String['0'];
        elencoagraria.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adattatore, View view, int pos, long l) {
                // recupero il titolo memorizzato nella riga tramite l'ArrayAdapter
                titoloriga[0] = (String) adattatore.getItemAtPosition(pos);
                // Log.d("List", "Ho cliccato sull'elemento con titolo" + titoloriga);
                richiamoPaginaInterna(titoloriga[0]);
            }
        });


        final Button agrariaplusButton = (Button) findViewById(agrariaplus);
        agrariaplusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (elencoagraria.getVisibility() == View.GONE)
                {
                    elencoagraria.setVisibility(view.VISIBLE);
                    //agrariaplusButton.setImageResource(btn_minus);

                } else {
                    elencoagraria.setVisibility(view.GONE);
                    //agrariaplusButton.setImageResource(btn_plus);
                }

            }

        });
    }
}
