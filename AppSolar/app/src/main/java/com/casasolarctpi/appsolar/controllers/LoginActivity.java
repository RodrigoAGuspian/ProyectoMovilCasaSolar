package com.casasolarctpi.appsolar.controllers;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity implements OnClickListener {
    //Declaración de variables
    Button btnLogin;
    Button btnRegistrar;
    private FirebaseAuth mAuth;
    EditText txtEmail, txtContrasena;
    DatabaseReference databaseReference;
    TextView txtOlvidoLaContrasena;
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
        txtOlvidoLaContrasena = findViewById(R.id.txtOlvidoContrasena);
        txtOlvidoLaContrasena.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        findViewById(R.id.pbLogin).setVisibility(View.INVISIBLE);
    }


    //Método para inicializar la autentificación de Firebase
    private void inizialiteFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

    }

    //Método para darles acciones a los botones
    private void setOnClickButtons() {

        btnLogin.setOnClickListener(this);
        btnRegistrar.setOnClickListener(this);
        txtOlvidoLaContrasena.setOnClickListener(this);
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
                            findViewById(R.id.pbLogin).setVisibility(View.INVISIBLE);
                            Toast.makeText(LoginActivity.this, R.string.welcome, Toast.LENGTH_SHORT).show();
                        } else {
                            Log.w("Inicio de sesión", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, R.string.el_usuario_no_registrado,
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                            hideProgressDialog();
                        }


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

    //Método para reestablecer contraseña sin loguearse.
    private void reestablecerContrasena() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.item_reestablecer_contrasena);
        final EditText txtEmail = dialog.findViewById(R.id.txtEmail);
        final Button btnAceptar = dialog.findViewById(R.id.btnAceptar1);
        final Button btnCancelar = dialog.findViewById(R.id.btnCancelar1);

        btnAceptar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                btnAceptar.setEnabled(false);
                btnCancelar.setEnabled(false);
                dialog.setCancelable(false);

                FirebaseAuth auth = FirebaseAuth.getInstance();
                String emailAddress = txtEmail.getText().toString();

                auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("Reestablecer C afuera", "Email sent.");
                                    Toast.makeText(LoginActivity.this, R.string.se_le_ha_enviado_un_correo, Toast.LENGTH_SHORT).show();
                                    dialog.cancel();

                                }else {
                                    btnAceptar.setEnabled(false);
                                    btnCancelar.setEnabled(false);
                                    dialog.setCancelable(false);
                                    Toast.makeText(LoginActivity.this, R.string.a_ocurrido_un_error_afuera, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                dialog.cancel();
            }
        });

        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.setCancelable(true);
        dialog.show();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                String email = txtEmail.getText().toString();
                String contrasena  = txtContrasena.getText().toString();
                signIn(email,contrasena);
                break;

            case R.id.btnRegistrar:
                Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(intent);
                break;

            case R.id.txtOlvidoContrasena:
                reestablecerContrasena();
                break;
        }

    }
}
