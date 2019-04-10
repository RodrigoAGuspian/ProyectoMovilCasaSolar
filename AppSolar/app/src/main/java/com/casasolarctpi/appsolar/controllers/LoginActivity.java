package com.casasolarctpi.appsolar.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.casasolarctpi.appsolar.R;
import com.casasolarctpi.appsolar.models.TokenBroadcastReceiver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    //Declaración de variables
    Button btnLogin;
    Button btnRegistrar;
    private FirebaseAuth mAuth;
    EditText txtEmail, txtContrasena;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        inizialite();
        inizialiteFirebase();
        setOnClickButtons();
        TokenBroadcastReceiver mTokenReceiver = new TokenBroadcastReceiver() {
            @Override
            public void onNewToken(String token) {
                Log.d("TOKEN", "onNewToken:" + token);
            }
        };
        
    }

    //Método para inicializar las vistas
    private void inizialite() {
        btnLogin = findViewById(R.id.btnLogin);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        txtEmail = findViewById(R.id.txtEmail);
        txtContrasena = findViewById(R.id.txtContrasena);
        findViewById(R.id.pbLogin).setVisibility(View.INVISIBLE);
    }


    //Método para inicializar la autentificación de Firebase
    private void inizialiteFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

    }

    //Método para darles acciones a los botones
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
                Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    //Método para iniciar sesión
    private void signIn(String email, String password) {
        Log.d("Inicio de Sesión", "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Inicio de Sesión", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Log.w("Inicio de sesión", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, R.string.el_usuario_no_registrado,
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        hideProgressDialog();
                    }
                });
    }

    //Método para mostrar que la app está cargando
    private void showProgressDialog() {
        findViewById(R.id.pbLogin).setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);
        btnRegistrar.setEnabled(false);
        txtEmail.setEnabled(false);
        txtContrasena.setEnabled(false);
    }

    //Método para habilitar las vistas
    private void hideProgressDialog() {
        findViewById(R.id.pbLogin).setVisibility(View.INVISIBLE);
        btnLogin.setEnabled(true);
        btnRegistrar.setEnabled(true);
        txtEmail.setEnabled(true);
        txtContrasena.setEnabled(true);
    }

    //Método para validar el formulario
    private boolean validateForm() {
        boolean valid = true;

        String email = txtEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            txtEmail.setError(getString(R.string.este_valor_requerido));
            valid = false;
        } else {
            txtEmail.setError(null);
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
    //Mpetodo para ingresar a la pantalla de inicio.
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(LoginActivity.this,MenuPrincipal.class);
            startActivity(intent);
            finish();

        } else {
            
        }
    }



}
