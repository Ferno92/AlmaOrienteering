package com.almaorient.ferno92.almaorienteering;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.almaorient.ferno92.almaorienteering.firebaseDB.AulaMarker;
import com.almaorient.ferno92.almaorienteering.firebaseDB.AulaModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luca.fernandez on 07/03/2017.
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    List<AulaModel> mListaAule = new ArrayList<AulaModel>();
    private ClusterManager<AulaMarker> mClusterManager;
    ProgressDialog mProgress;
    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_activity);

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Loading");
        mProgress.setMessage("Stiamo cercando le aule più belle...");
        mProgress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        mProgress.show();

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
                initMap();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if(databaseError != null){

                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.mMap = map;
        this.setUpClusterer();
    }

    public void initMap(){
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void setUpClusterer() {

        // To dismiss the dialog
        mProgress.dismiss();
        this.mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(44.32, 11.78), 8.0f));
        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<AulaMarker>(this, this.mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        this.mMap.setOnCameraIdleListener(mClusterManager);
        this.mMap.setOnMarkerClickListener(mClusterManager);

        mClusterManager
                .setOnClusterClickListener(new ClusterManager.OnClusterClickListener<AulaMarker>() {
                    @Override
                    public boolean onClusterClick(Cluster<AulaMarker> cluster) {
                        LatLng latLng = null;
                        int i = 0;
                        Boolean isDifferent = false;
                        String nomiAule = "";
                        for(AulaMarker marker : cluster.getItems()){
                            if(i == 0){
                                latLng = marker.getPosition();
                            }
                            if(i != 0 && !marker.getPosition().equals(latLng)){
                                isDifferent = true;
                                break;
                            }else{
                                nomiAule += marker.getTitle() + "\r\n";
                            }
                            i++;
                        }
                        if(isDifferent){
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    cluster.getPosition(), (float) Math.floor(mMap
                                            .getCameraPosition().zoom + 1)), 300,
                                    null);
                        }else{
                            new AlertDialog.Builder(MapsActivity.this)
                                    .setTitle("Gruppo di aule")
                                    .setMessage(nomiAule)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }

                        return true;
                    }

                });

        // Add cluster items (markers) to the cluster manager.
        addMarkers();
    }

    private void addMarkers() {


        for(AulaModel aula : this.mListaAule){
            if(!aula.getLatitudine().isEmpty() && !aula.getLongitudine().isEmpty()){
                AulaMarker marker = new AulaMarker(Double.valueOf(aula.getLatitudine()), Double.valueOf(aula.getLongitudine()), aula.getNome(), aula.getPiano());
                mClusterManager.addItem(marker);
            }

        }

    }

    //    https://developers.google.com/maps/documentation/android-api/config
//    https://developers.google.com/maps/documentation/android-api/map
//    https://developers.google.com/maps/documentation/android-api/marker
}
