package com.almaorient.ferno92.almaorienteering.recensioni;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.almaorient.ferno92.almaorienteering.BaseActivity;
import com.almaorient.ferno92.almaorienteering.R;
import com.almaorient.ferno92.almaorienteering.login.CorsoSignUp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by lucas on 18/03/2017.
 */

public class RecensioniActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recensioni_activity);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        Query query = ref.child("users");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    String id = (String) data.child("userId").getValue();
                    if(id.equals(String.valueOf(mAuth.getCurrentUser().getEmail()))){
                        HashMap corsoMap = (HashMap) data.child("corso").getValue();
                        Iterator corsoIterator = corsoMap.keySet().iterator();
                        String nomeCorso = "";
                        while (corsoIterator.hasNext()) {
                            String corsoKey = (String) corsoIterator.next();
                            switch (corsoKey) {
                                case "id":

                                    break;
                                case "nome":
                                    nomeCorso = String.valueOf(corsoMap.get(corsoKey));
                                    break;
                                default:
                                    break;
                            }
                        }

                        TextView nomeCorsoTextView= (TextView) findViewById(R.id.nome_corso);
                        nomeCorsoTextView.setText(nomeCorso);
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
