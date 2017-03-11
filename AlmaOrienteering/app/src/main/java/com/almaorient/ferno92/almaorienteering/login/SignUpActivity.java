package com.almaorient.ferno92.almaorienteering.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.almaorient.ferno92.almaorienteering.MainActivity;
import com.almaorient.ferno92.almaorienteering.R;
import com.almaorient.ferno92.almaorienteering.strutturaUnibo.Scuola;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

/**
 * Created by luca.fernandez on 10/03/2017.
 */

public class SignUpActivity extends AppCompatActivity {
//    https://firebase.google.com/docs/analytics/android/properties
//    https://firebase.google.com/docs/auth/android/password-auth
//    https://firebase.google.com/docs/auth/android/manage-users

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String TAG = "Sign UP";
    private String BASE_EMAIL_TYPE = "@studio.unibo.it";
    EditText mEmailEdit;
    EditText mPwdEdit;
    Spinner mScuolaSpinner;
    Scuola mSelectedScuola;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.signup_activity);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        AppCompatButton signupButton = (AppCompatButton) findViewById(R.id.signup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewUser();
            }
        });

        initView();
        initScuolaArray();
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void initView(){
        mEmailEdit = (EditText) findViewById(R.id.email);
        mPwdEdit = (EditText) findViewById(R.id.pwd);
        mScuolaSpinner = (Spinner) findViewById(R.id.scuola_spinner);
    }

    private void initScuolaArray(){
        ArrayAdapter spinnerScuolaArrayAdapter = new ArrayAdapter(this, R.layout.spinner_item, new Scuola[]{
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
                new Scuola("politiche", "Scienze politiche")
        });
        mScuolaSpinner.setAdapter(spinnerScuolaArrayAdapter);
        mScuolaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSelectedScuola = (Scuola) mScuolaSpinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void createNewUser(){

        String email = mEmailEdit.getText().toString();
        String password = mPwdEdit.getText().toString();

        if(!email.isEmpty() && !password.isEmpty()){
            if(email.contains(this.BASE_EMAIL_TYPE)){
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "Authentication failed",
                                            Toast.LENGTH_SHORT).show();
                                }else{
                                    // Add user properties
                                    mFirebaseAnalytics.setUserProperty("Scuola", mSelectedScuola.getNome());
//                                    mFirebaseAnalytics.setUserProperty("Corso", mCorso);


                                    Toast.makeText(SignUpActivity.this, "Authentication succeded",
                                            Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                                    i.setFlags(FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        });
            }else{
                Toast.makeText(SignUpActivity.this, "L'email deve essere del tipo @studio.unibo.it",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(SignUpActivity.this, "Mancano dei dati obbligatori",
                    Toast.LENGTH_SHORT).show();
        }

    }
}

