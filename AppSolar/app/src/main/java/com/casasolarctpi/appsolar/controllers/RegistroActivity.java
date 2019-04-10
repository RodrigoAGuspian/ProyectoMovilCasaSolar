package com.casasolarctpi.appsolar.controllers;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.casasolarctpi.appsolar.R;
import com.casasolarctpi.appsolar.models.Constants;
import com.casasolarctpi.appsolar.models.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.Objects;

public class RegistroActivity extends AppCompatActivity implements OnClickListener {
    //Declaración de variables
    EditText txtEmail, txtContrasena1, txtContrasena2, txtPrimerN, txtSegundoN, txtPrimerA, txtSegundoA;
    EditText txtInstitucion, txtPais, txtDepartamento,txtCiudad;
    MaterialSpinner msTipoDeUso;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.registro_usuario);
        inizialite();
        inizialiteFirebase();

    }

    //Inicializar las vistas  y hacer el botón seleccionable.
    private void inizialite() {
        txtEmail = findViewById(R.id.txtEmail);
        txtContrasena1 = findViewById(R.id.txtContrasena1);
        txtContrasena2 = findViewById(R.id.txtContrasena2);
        txtPrimerN = findViewById(R.id.txtPrimerN);
        txtSegundoN = findViewById(R.id.txtSegundoN);
        txtPrimerA = findViewById(R.id.txtPrimerA);
        txtSegundoA = findViewById(R.id.txtSegundoA);
        txtInstitucion = findViewById(R.id.txtInstitucion);
        txtPais = findViewById(R.id.txtPais);
        txtDepartamento = findViewById(R.id.txtDepartamento);
        txtCiudad = findViewById(R.id.txtCiudad);
        msTipoDeUso = findViewById(R.id.msTipoDeUso);
        msTipoDeUso.setItems(Constants.LIST_TIPO_DE_USO);
        findViewById(R.id.btnRegistrar).setOnClickListener(this);
        findViewById(R.id.progressBarRegistro).setVisibility(View.INVISIBLE);

    }

    //Método para inicializar la autentificación de Firebase.
    private void inizialiteFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegistrar:
                registarUsuario();
                break;
        }
    }

    //Método el cual hará los procesos para el registro de usuario..
    private void registarUsuario() {
        if (compareToPasswords()) {
            createAccount(txtEmail.getText().toString(),txtContrasena1.getText().toString());
        }else {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }

    }

    //Método encargado de crear la cuenta.
    private void createAccount(String email, String password) {
        Log.d("Create Account", "createAccount:" + email);
        showProgressDialog();

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               if (task.isSuccessful()) {
                   // Sign in success, update UI with the signed-in user's information
                   Log.d("Create Account", "createUserWithEmail:success");
                   FirebaseUser user = mAuth.getCurrentUser();
                   updateUI(user);
                   databaseReference.child("usuarios").push().setValue(datosCompletos());
               } else {
                   // If sign in fails, display a message to the user.
                   Log.w("Create user", "createUserWithEmail:failure", task.getException());
                   Toast.makeText(RegistroActivity.this, "Authentication failed.",
                           Toast.LENGTH_SHORT).show();
                   updateUI(null);
               }

               // [START_EXCLUDE]
               hideProgressDialog();
               // [END_EXCLUDE]
           }
       });
    }

    //Método para mostrar el progressDialog como señal de carga.
    private void showProgressDialog() {
        findViewById(R.id.progressBarRegistro).setVisibility(View.VISIBLE);
        findViewById(R.id.btnRegistrar).setEnabled(false);
        txtEmail.setEnabled(false);
        txtContrasena1.setEnabled(false);
        txtContrasena2.setEnabled(false);
        txtPrimerN.setEnabled(false);
        txtSegundoN.setEnabled(false);
        txtPrimerA.setEnabled(false);
        txtSegundoA.setEnabled(false);
        txtInstitucion.setEnabled(false);
        txtPais.setEnabled(false);
        txtDepartamento.setEnabled(false);
        txtCiudad.setEnabled(false);

    }

    //Método para ocultar el progressDialog como señal de terminación de carga.
    private void hideProgressDialog() {
        findViewById(R.id.progressBarRegistro).setVisibility(View.INVISIBLE);
        findViewById(R.id.btnRegistrar).setEnabled(true);
        txtEmail.setEnabled(true);
        txtContrasena1.setEnabled(true);
        txtContrasena2.setEnabled(true);
        txtPrimerN.setEnabled(true);
        txtSegundoN.setEnabled(true);
        txtPrimerA.setEnabled(true);
        txtSegundoA.setEnabled(true);
        txtInstitucion.setEnabled(true);
        txtPais.setEnabled(true);
        txtDepartamento.setEnabled(true);
        txtCiudad.setEnabled(true);
    }

    //Método que permite verificar que el usuario ha sido creado.
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Toast.makeText(this, R.string.mensaje_de_inicio, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegistroActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();

        } else {

        }

    }

    //Método para validar los campos requeridos.
    private boolean validar() {
        boolean valid =true;
        String email = txtEmail.getText().toString();
        if (TextUtils.isEmpty(email)){
            txtEmail.setError(getResources().getString(R.string.este_valor_requerido));
            valid=false;
        }else {
            txtEmail.setError(null);
        }

        String password1 = txtContrasena1.getText().toString();
        if (TextUtils.isEmpty(password1)){
            txtContrasena1.setError(getResources().getString(R.string.este_valor_requerido));
            valid=false;
        }else {
            txtContrasena1.setError(null);
        }

        String password2 = txtContrasena1.getText().toString();
        if (TextUtils.isEmpty(password2)){
            txtContrasena1.setError(getResources().getString(R.string.este_valor_requerido));
            valid=false;
        }else {
            txtContrasena1.setError(null);
        }


        String primerN = txtPrimerN.getText().toString();
        if (TextUtils.isEmpty(primerN)){
            txtPrimerN.setError(getResources().getString(R.string.este_valor_requerido));
            valid=false;
        }else {
            txtEmail.setError(null);
        }

        String primerA = txtPrimerA.getText().toString();
        if (TextUtils.isEmpty(primerA)){
            txtPrimerA.setError(getResources().getString(R.string.este_valor_requerido));
            valid=false;
        }else {
            txtEmail.setError(null);
        }


        String pais = txtPais.getText().toString();
        if (TextUtils.isEmpty(pais)){
            txtPais.setError(getResources().getString(R.string.este_valor_requerido));
            valid=false;
        }else {
            txtEmail.setError(null);
        }

        String departamento = txtDepartamento.getText().toString();
        if (TextUtils.isEmpty(departamento)){
            txtPrimerN.setError(getResources().getString(R.string.este_valor_requerido));
            valid=false;
        }else {
            txtEmail.setError(null);
        }

        String ciudad = txtCiudad.getText().toString();
        if (TextUtils.isEmpty(ciudad)){
            txtCiudad.setError(getResources().getString(R.string.este_valor_requerido));
            valid=false;
        }else {
            txtCiudad.setError(null);
        }

        if (msTipoDeUso.getSelectedIndex()<0){
            txtCiudad.setError(getResources().getString(R.string.este_valor_requerido));
            valid=false;
        }else {
            msTipoDeUso.setError(null);
        }


        return valid;
    }

    //Método para comparar que las contraseñas coincidan.
    private boolean compareToPasswords(){
        boolean valid = false;

        String password1 = txtContrasena1.getText().toString();
        String password2 = txtContrasena2.getText().toString();

        if (validar()){
            if (password1.length()<8){
                txtContrasena1.setError(getString(R.string.contrasena_muy_corta));
                Toast.makeText(this, R.string.contrasena_muy_corta_m2, Toast.LENGTH_SHORT).show();
                valid=false;
            }else {

                if (password1.equals(password2)) {
                    valid = true;
                } else {
                    txtContrasena1.setError(getString(R.string.contrasenas_no_coinciden));
                    txtContrasena2.setError(getString(R.string.contrasenas_no_coinciden));
                    Toast.makeText(this, R.string.contrasenas_no_coinciden, Toast.LENGTH_SHORT).show();
                    valid = false;
                }
            }

        }else {
            Toast.makeText(this, R.string.revise_datos, Toast.LENGTH_SHORT).show();
        }

        return valid;
    }

    //Función que devuelve un objeto UserData para cargar los datos de las vistas en ese dicho objeto.
    private UserData datosCompletos(){
        UserData data = new UserData();
        data.setCiudad(txtCiudad.getText().toString());
        data.setDepartamento(txtDepartamento.getText().toString());
        data.setEmail(txtEmail.getText().toString());
        data.setInstitucion(txtInstitucion.getText().toString());
        data.setPais(txtPais.getText().toString());
        data.setPrimerA(txtPrimerA.getText().toString());
        data.setPrimerN(txtPrimerN.getText().toString());
        data.setSegundoA(txtSegundoA.getText().toString());
        data.setSegundoN(txtSegundoN.getText().toString());
        int index = msTipoDeUso.getSelectedIndex();
        data.setTipoDeUso(msTipoDeUso.getItems().get(index).toString());
        data.setSegundoA(txtSegundoA.getText().toString());
        data.setSegundoA(txtSegundoA.getText().toString());

        return data;
    }
}
