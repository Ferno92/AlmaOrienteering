package com.almaorient.ferno92.almaorienteering;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

public class ContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_main);

        AppCompatButton sistemaunivButton = (AppCompatButton) findViewById(R.id.sistemauniv);

        sistemaunivButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ContentActivity.this, SistemaUniversitarioActivity.class);
                startActivity(i);
            }
        });
    }
}
