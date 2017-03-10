package com.almaorient.ferno92.almaorienteering.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.almaorient.ferno92.almaorienteering.ChooseActivity;
import com.almaorient.ferno92.almaorienteering.MainActivity;
import com.almaorient.ferno92.almaorienteering.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

/**
 * Created by lucas on 06/03/2017.
 */

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText mEmailEdit;
    EditText mPwdEdit;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String TAG = "Sign in";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.login_activity);
        AppCompatButton signupButton = (AppCompatButton) findViewById(R.id.signup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });
        AppCompatButton loginButton = (AppCompatButton) findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mEmailEdit.getText().toString();
                String password = mPwdEdit.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password);
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    i.setFlags(FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        initView();
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
    }
}
