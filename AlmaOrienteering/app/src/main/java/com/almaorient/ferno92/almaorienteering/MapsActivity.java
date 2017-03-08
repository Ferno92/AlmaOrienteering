package com.almaorient.ferno92.almaorienteering;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.almaorient.ferno92.almaorienteering.firebaseDB.AulaModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luca.fernandez on 07/03/2017.
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    List<AulaModel> mListaAule = new ArrayList<AulaModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_activity);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();

        Query query = ref.child("aule").orderByKey();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    String codice = (String) data.child("aula_codice").getValue();
                    String nome = (String) data.child("aula_nome").getValue();
                    String indirizzo = (String) data.child("aula_indirizzo").getValue();
                    String piano = (String) data.child("aula_piano").getValue();
                    String latitudine = (String) data.child("latitude").getValue();
                    String longitudine = (String) data.child("longitude").getValue();

                    AulaModel aula = new AulaModel(codice, nome, indirizzo, piano, latitudine, longitudine);
                    mListaAule.add(aula);
                }
                Log.d("size lista aule", String.valueOf(mListaAule.size()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if(databaseError != null){

                }
            }
        });

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
    }

    //    https://developers.google.com/maps/documentation/android-api/config
//    https://developers.google.com/maps/documentation/android-api/map
//    https://developers.google.com/maps/documentation/android-api/marker
}
