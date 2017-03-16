package com.almaorient.ferno92.almaorienteering.versus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.almaorient.ferno92.almaorienteering.R;
import com.almaorient.ferno92.almaorienteering.strutturaUnibo.Corso;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by lucas on 12/03/2017.
 */

public class VersusActivity extends AppCompatActivity {
    String mScuola1;
    String mScuola2;
    String mCorso1;
    String mCorso2;
    DatabaseReference mRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.versus_activity);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.mRef = database.getReference();

        this.mScuola1 = getIntent().getExtras().getString("scuola1");
        this.mScuola2 = getIntent().getExtras().getString("scuola2");
        this.mCorso1 = getIntent().getExtras().getString("corso1");
        this.mCorso2 = getIntent().getExtras().getString("corso2");
        if(mScuola1 == "tutte"){
            //recupera statistiche generali
        }else{
            Query query = mRef.child("statistiche").child(mScuola1).orderByKey();
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int i = 0;

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if(databaseError != null){

                    }
                }
            });
        }
        if(mScuola2 == "tutte"){
            //recupera statistiche generali
            Query query = mRef.child("statistiche").child(mScuola2).orderByKey();
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int i = 0;

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if(databaseError != null){

                    }
                }
            });
        }
    }
}
