package com.almaorient.ferno92.almaorienteering;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.almaorient.ferno92.almaorienteering.firebaseDB.AulaMarker;
import com.almaorient.ferno92.almaorienteering.firebaseDB.AulaModel;
import com.almaorient.ferno92.almaorienteering.firebaseDB.IndirizziModel;
import com.almaorient.ferno92.almaorienteering.strutturaUnibo.Corso;
import com.almaorient.ferno92.almaorienteering.strutturaUnibo.Scuola;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.almaorient.ferno92.almaorienteering.R.id.map;

/**
 * Created by luca.fernandez on 07/03/2017.
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    List<AulaModel> mListaAule = new ArrayList<AulaModel>();

    private ClusterManager<AulaMarker> mClusterManager;
    ProgressDialog mProgress;
    GoogleMap mMap;

    Spinner mScuolaSpinner;
    Spinner mCorsoSpinner;
    Scuola mSelectedScuola;
    Corso mSelectedCorso;
    Integer mCount;

    public static final Scuola[] mScuolaadatt = new Scuola[]{
            new Scuola("", "Seleziona scuola"),
            new Scuola("agraria", "Agraria e Medicina veterinaria"),
            new Scuola("economia", "Economia, Mangement e Statistica"),
            new Scuola("farmacia", "Farmacia, Biotecnologie e Scienze motorie"),
            new Scuola("giurisprudenza", "Giurisprudenza"),
            new Scuola("ingegneria", "Ingegneria e architettura"),
            new Scuola("lettere", "Lettere e Beni culturali"),
            new Scuola("lingue", "Lingue e letterature, Traduzione e Interpretazione"),
            new Scuola("medicina", "Medicina e Chirurgia"),
            new Scuola("psicologia", "Psicologia e Scienze della formazione"),
            new Scuola("scienze", "Scienze"),
            new Scuola("scienze_politiche", "Scienze politiche")
    };

    private List<Corso> mListaCorsi = new ArrayList<Corso>();
    private ArrayList<IndirizziModel> mListaIndirizzi = new ArrayList<IndirizziModel>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_activity);

        mCount=0;

        initScuolaArray();

        mCorsoSpinner = (Spinner) findViewById(R.id.spinnercorso);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
//
        Query query2 = ref.child("aule").orderByKey();
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
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
                mProgress.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (databaseError != null) {

                }
            }
        });

    }

    private void initScuolaArray(){
        ArrayAdapter spinnerScuolaArrayAdapter = new ArrayAdapter(this, R.layout.spinner_item, this.mScuolaadatt);
        mScuolaSpinner = (Spinner) findViewById(R.id.spinnerscuola);
        mScuolaSpinner.setAdapter(spinnerScuolaArrayAdapter);
        final String callingactivity = getIntent().getExtras().getString("CallingActivity");
        if (callingactivity.equals("dettagliCorso")) {
            final Long idscuola = getIntent().getExtras().getLong("idscuola");
            mScuolaSpinner.setSelection((int) (long) idscuola);
            mProgress = new ProgressDialog(this);
            mProgress.setTitle("Loading");
            mProgress.setMessage("Stiamo caricando i dati");
            mProgress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            mProgress.show();
        }

        if (callingactivity.equals("main")) {
            mProgress = new ProgressDialog(this);
            mProgress.setTitle("Loading");
            mProgress.setMessage("Stiamo caricando i dati");
            mProgress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            mProgress.show();
        }

        initMap();

        mScuolaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSelectedScuola = (Scuola) mScuolaSpinner.getSelectedItem();

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference();

                mCount=mCount+1;

                if (mScuolaSpinner.getSelectedItemPosition()!=0) {

                    mCorsoSpinner.setClickable(true);

                    Query query = ref.child("corso/" + mScuolaadatt[mScuolaSpinner.getSelectedItemPosition()].getScuolaId());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            mListaCorsi.clear();
                            Corso vuoto = new Corso("","Seleziona un corso","","","","",null);
                            String callingactivity = getIntent().getExtras().getString("CallingActivity");
                            if (!callingactivity.equals("dettagliCorso")) {
                                mListaCorsi.add(vuoto);
                                initMap();
                            }

                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                String nome = (String) data.child("corso_descrizione").getValue();
                                String codicecorso = String.valueOf(data.child("corso_codice").getValue());

                                Corso corso = new Corso(codicecorso, nome, "", "", "", "",null);
                                mListaCorsi.add(corso);

                            }
                            initCorsoArray();
                            mCorsoSpinner.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            if (databaseError != null) {

                            }
                        }
                    });
                }
                else if (mCount==1 || callingactivity.equals("dettagliCorso")) {
                    mCorsoSpinner.setVisibility(View.GONE);
                    initMap();
                }
                else {
                    mCorsoSpinner.setSelection(0);
                    mCorsoSpinner.setClickable(false);
                    initMap();
//
//                    mCorsoSpinner.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Toast.makeText(
//                                    MapsActivity.this,
//                                    "Selezionare prima una scuola",
//                                    Toast.LENGTH_SHORT
//                            ).show();
//                        }
//                    });
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initCorsoArray(){
        ArrayAdapter spinnerCorsoArrayAdapter = new ArrayAdapter(this, R.layout.spinner_item, this.mListaCorsi);

        mCorsoSpinner.setAdapter(spinnerCorsoArrayAdapter);

        final String callingactivity = getIntent().getExtras().getString("CallingActivity");
        if (callingactivity.equals("dettagliCorso")) {
            final Long idscuola = getIntent().getExtras().getLong("idscuola");
            Integer codcorso = getIntent().getExtras().getInt("codcorso");
            mCorsoSpinner.setSelection(codcorso);
            mProgress.dismiss();
        }

        mCorsoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSelectedCorso = (Corso) mCorsoSpinner.getSelectedItem();
                if (!mSelectedCorso.getNome().equals("Seleziona un corso")) {
                    String codicecorso = mSelectedCorso.getScuolaId();

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference();

                    Query query2 = ref.child("mappe/" + mScuolaadatt[mScuolaSpinner.getSelectedItemPosition()].getScuolaId())
                            .orderByChild("corso_codice").equalTo(Integer.parseInt(codicecorso));
                    query2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //int i = 0;
                            mListaIndirizzi.clear();

                            if (callingactivity.equals("main")) {
                                mClusterManager.clearItems();
                            }
                            if (mMap != null) {
                                mMap.clear();
                            }
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                final String codicecorso = String.valueOf(data.child("corso_codice").getValue());

                                Double latitude = (Double) data.child("latitude").getValue();
                                Double longitude = (Double) data.child("longitude").getValue();
                                String corsolaurea = (String) data.child("Corso di laurea").getValue();
                                String indirizzo = (String) data.child("indirizzo").getValue();

                                IndirizziModel address = new IndirizziModel(codicecorso, latitude, longitude,corsolaurea,indirizzo);
                                mListaIndirizzi.add(address);

                                //i++;
                            }


                            //Log.d("size lista aule", String.valueOf(mListaAule.size()));

                            initMap();

                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            if (databaseError != null) {

                            }
                        }
                    });

                }
                else {
                    if (mMap != null) {
                        mMap.clear();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.mMap = map;
        String callingactivity = getIntent().getExtras().getString("CallingActivity");
//        if (!callingactivity.equals("dettagliCorso")) {
//            this.mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(44.496233, 11.354185), 13.0f));
//        }
        for (IndirizziModel indirizzi : this.mListaIndirizzi) {
            if (indirizzi.getLatitudine() != null) {
                map.addMarker(new MarkerOptions().position(new LatLng(indirizzi.getLatitudine(), indirizzi.getLongitudine()))
                .title(indirizzi.getCorso()).snippet(indirizzi.getIndirizzo()));
            }
            this.mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(indirizzi.getLatitudine(), indirizzi.getLongitudine()), 15.0f));

        }
        if (mScuolaSpinner.getSelectedItemPosition()==0 ) {
            setUpClusterer();
        }
        else if (mCorsoSpinner.getSelectedItemPosition()==0 && callingactivity.equals("main")){
            setUpClusterer();
        }

    }

    public void initMap(){

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(map);

        mapFragment.getMapAsync(this);

    }


////
    private void setUpClusterer() {
//
//        // To dismiss the dialog
//        mProgress.dismiss();
//
//
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
