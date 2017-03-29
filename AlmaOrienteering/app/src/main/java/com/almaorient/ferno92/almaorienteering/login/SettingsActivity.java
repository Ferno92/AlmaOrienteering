package com.almaorient.ferno92.almaorienteering.login;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.almaorient.ferno92.almaorienteering.BaseActivity;
import com.almaorient.ferno92.almaorienteering.R;

/**
 * Created by lucas on 29/03/2017.
 */

public class SettingsActivity extends BaseActivity {

    private String mUserName;
    private String mUserSurname;
    private String mCorso;
    private String mCorsoId;
    private String mScuola;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        mUserName = getIntent().getStringExtra("nome");
        mUserSurname = getIntent().getStringExtra("cognome");
        mCorso = getIntent().getStringExtra("corso");
        mCorsoId = getIntent().getStringExtra("corsoId");
//
//        i.putExtra("nome", mUserName);
//        i.putExtra("cognome", mUserSurname);
//        i.putExtra("corsoId", mCorsoId);
//        i.putExtra("corso", mCorso);
//        i.putExtra("scuola", mScuola);
    }
}
