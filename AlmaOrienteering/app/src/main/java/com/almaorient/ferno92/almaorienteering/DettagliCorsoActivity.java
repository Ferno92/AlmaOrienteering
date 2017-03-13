package com.almaorient.ferno92.almaorienteering;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DettagliCorsoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettagli_corso);

        TextView nomecorsoText = (TextView) findViewById(R.id.nomecorso);

        String datopassato = getIntent().getExtras().getString("Vocecliccata");
        nomecorsoText.setText(datopassato);
    }
}
