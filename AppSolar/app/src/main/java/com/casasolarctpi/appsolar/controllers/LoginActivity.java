package com.casasolarctpi.appsolar.controllers;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.casasolarctpi.appsolar.R;
import com.casasolarctpi.appsolar.models.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    Button btnRegistrar;
    private FirebaseAuth mAuth;
    EditText txtEmail, txtContrasena;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        inizialite();
        inizialiteFirebase();
        setOnClickButtons();
    }

    private void inizialite() {
        btnLogin = findViewById(R.id.btnLogin);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        txtEmail = findViewById(R.id.txtEmail);
        txtContrasena = findViewById(R.id.txtContrasena);
        findViewById(R.id.pbLogin).setVisibility(View.INVISIBLE);
    }
    

    private void inizialiteFirebase() {
        mAuth = FirebaseAuth.getInstance();

    }

    private void setOnClickButtons() {

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString();
                String contrasena  = txtContrasena.getText().toString();
                signIn(email,contrasena);
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MenuPrincipal.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void signIn(String email, String password) {
        Log.d("Inicio de Sesión", "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();



        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Inicio de Sesión", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Inicio de sesión", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, R.string.el_usuario_no_registrado,
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private void showProgressDialog() {
        findViewById(R.id.pbLogin).setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);
        btnRegistrar.setEnabled(false);
        txtEmail.setEnabled(false);
        txtContrasena.setEnabled(false);
    }

    private void hideProgressDialog() {
        findViewById(R.id.pbLogin).setVisibility(View.INVISIBLE);
        btnLogin.setEnabled(true);
        btnRegistrar.setEnabled(true);
        txtEmail.setEnabled(true);
        txtContrasena.setEnabled(true);
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = txtEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            txtEmail.setError(getString(R.string.este_valor_requerido));
            valid = false;
        } else {
            txtContrasena.setError(null);
        }

        String password = txtContrasena.getText().toString();
        if (TextUtils.isEmpty(password)) {
            txtContrasena.setError(getString(R.string.este_valor_requerido));
            valid = false;
        } else {
            txtContrasena.setError(null);
        }

        return valid;
    }
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(LoginActivity.this,MenuPrincipal.class);
            startActivity(intent);
            finish();

        } else {
            
        }
    }



}
