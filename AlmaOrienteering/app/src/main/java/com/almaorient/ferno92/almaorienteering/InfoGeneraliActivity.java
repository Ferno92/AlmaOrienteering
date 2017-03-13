package com.almaorient.ferno92.almaorienteering;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;

import static android.R.drawable.btn_minus;
import static android.R.drawable.btn_plus;
import static com.almaorient.ferno92.almaorienteering.R.id.plusdue;
import static com.almaorient.ferno92.almaorienteering.R.id.plusquattro;
import static com.almaorient.ferno92.almaorienteering.R.id.plustre;
import static com.almaorient.ferno92.almaorienteering.R.id.plusuno;
import static com.almaorient.ferno92.almaorienteering.R.id.webview2;

public class InfoGeneraliActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_generali);

        final WebView unoWebView = (WebView) findViewById(R.id.webview1);
        unoWebView.loadData(getString(R.string.hello), "text/html; charset=utf-8", "utf-8");
        final ImageButton primoplusImageButton = (ImageButton) findViewById(plusuno);
        primoplusImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (unoWebView.getVisibility() == View.GONE)
                {
                    unoWebView.setVisibility(view.VISIBLE);
                    primoplusImageButton.setImageResource(btn_minus);

                } else {
                    unoWebView.setVisibility(view.GONE);
                    primoplusImageButton.setImageResource(btn_plus);
                }

            }

        });

        final WebView dueWebView = (WebView) findViewById(R.id.webview2);
        dueWebView.loadData(getString(R.string.hello), "text/html; charset=utf-8", "utf-8");
        final ImageButton secondoplusImageButton = (ImageButton) findViewById(plusdue);
        secondoplusImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dueWebView.getVisibility() == View.GONE)
                {
                    dueWebView.setVisibility(view.VISIBLE);
                    secondoplusImageButton.setImageResource(btn_minus);

                } else {
                    dueWebView.setVisibility(view.GONE);
                    secondoplusImageButton.setImageResource(btn_plus);
                }

            }

        });

        final WebView treWebView = (WebView) findViewById(R.id.webview3);
        treWebView.loadData(getString(R.string.hello), "text/html; charset=utf-8", "utf-8");
        final ImageButton terzoplusImageButton = (ImageButton) findViewById(plustre);
        terzoplusImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (treWebView.getVisibility() == View.GONE)
                {
                    treWebView.setVisibility(view.VISIBLE);
                    terzoplusImageButton.setImageResource(btn_minus);

                } else {
                    treWebView.setVisibility(view.GONE);
                    terzoplusImageButton.setImageResource(btn_plus);
                }

            }

        });

        final WebView quattroWebView = (WebView) findViewById(R.id.webview4);
        quattroWebView.loadData(getString(R.string.sistemauniv), "text/html; charset=utf-8", "utf-8");
        final ImageView schemaImageView = (ImageView) findViewById(R.id.schema);
        final WebView quattro4WebView = (WebView) findViewById(R.id.webview44);
        quattro4WebView.loadData(getString(R.string.sistemauniv2), "text/html; charset=utf-8", "utf-8");
        final ImageButton quartoplusImageButton = (ImageButton) findViewById(plusquattro);
        quartoplusImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quattroWebView.getVisibility() == View.GONE)
                {
                    quattroWebView.setVisibility(view.VISIBLE);
                    quattro4WebView.setVisibility(view.VISIBLE);
                    schemaImageView.setVisibility(View.VISIBLE);
                    quartoplusImageButton.setImageResource(btn_minus);

                } else {
                    quattroWebView.setVisibility(view.GONE);
                    quattro4WebView.setVisibility(view.GONE);
                    schemaImageView.setVisibility(View.GONE);
                    quartoplusImageButton.setImageResource(btn_plus);
                }

            }

        });
    }
}
