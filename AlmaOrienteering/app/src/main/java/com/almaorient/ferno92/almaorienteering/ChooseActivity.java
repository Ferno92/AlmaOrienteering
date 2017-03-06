package com.almaorient.ferno92.almaorienteering;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.Window;

/**
 * Created by lucas on 06/03/2017.
 */

public class ChooseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.choose_activity);

        AppCompatButton studenteButton = (AppCompatButton) findViewById(R.id.studente);
        AppCompatButton orientatiButton = (AppCompatButton) findViewById(R.id.orientati);

        studenteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ChooseActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        orientatiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(ChooseActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

    }
}
