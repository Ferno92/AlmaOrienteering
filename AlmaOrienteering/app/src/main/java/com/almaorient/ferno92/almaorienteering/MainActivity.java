package com.almaorient.ferno92.almaorienteering;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.almaorient.ferno92.almaorienteering.recensioni.RecensioniActivity;
import com.almaorient.ferno92.almaorienteering.versus.VersusSelectorActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RelativeLayout mapButton = (RelativeLayout) findViewById(R.id.mappa);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(i);
            }
        });

        RelativeLayout elencoscuoleButton = (RelativeLayout) findViewById(R.id.elencoscuole);
        elencoscuoleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ElencoScuoleActivity.class);
                startActivity(i);
            }
        });

        RelativeLayout infoButton = (RelativeLayout) findViewById(R.id.unibo);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, InfoGeneraliActivity.class);
                startActivity(i);
            }
        });

        RelativeLayout calendarButton = (RelativeLayout) findViewById(R.id.calendario);
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(i);
            }
        });
        RelativeLayout statButton = (RelativeLayout) findViewById(R.id.statistiche);
        statButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, VersusSelectorActivity.class);
                startActivity(i);
            }
        });
        RelativeLayout recensioniButton = (RelativeLayout) findViewById(R.id.recensioni);
        recensioniButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, RecensioniActivity.class);
                startActivity(i);
            }
        });

        if(this.mAuth.getCurrentUser() != null) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.setVisibility(View.VISIBLE);

            recensioniButton.setVisibility(View.VISIBLE);

            LinearLayout navigationHeader =(LinearLayout) navigationView.getHeaderView(0);
            TextView mailText = (TextView) navigationHeader.findViewById(R.id.logged_user_email);
            mailText.setText(String.valueOf(this.mAuth.getCurrentUser().getEmail()));
            Menu navigationMenu = (Menu)navigationView.getMenu();
            final MenuItem scuolaItem = (MenuItem) navigationMenu.findItem(R.id.nav_share);
            final MenuItem corsoItem = (MenuItem) navigationMenu.findItem(R.id.nav_send);


            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference();
            Query query = ref.child("users");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        String id = (String) data.child("userId").getValue();
                        if(id.equals(String.valueOf(mAuth.getCurrentUser().getEmail()))){
                            scuolaItem.setTitle((String) data.child("scuola").getValue());
                            corsoItem.setTitle((String) data.child("corso").getValue());
                        }
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }else{

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        if(this.mAuth.getCurrentUser() != null) {
            MenuItem logout = (MenuItem) menu.findItem(R.id.logout);
            logout.setVisible(true);
            MenuItem delete = (MenuItem) menu.findItem(R.id.delete_user);
            delete.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.logout){
            if(this.mAuth.getCurrentUser() != null){
                this.mAuth.signOut();
                Intent i = new Intent(MainActivity.this, ChooseActivity.class);
                i.setFlags(FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        }else if(id == R.id.delete_user){
            this.mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d("Delete:", "User account deleted.");
                        Intent i = new Intent(MainActivity.this, ChooseActivity.class);
                        i.setFlags(FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                }
            });

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else
            if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
