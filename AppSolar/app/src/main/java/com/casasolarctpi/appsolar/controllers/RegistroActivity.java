package com.casasolarctpi.appsolar.controllers;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.casasolarctpi.appsolar.R;
import com.casasolarctpi.appsolar.models.Constants;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.Objects;

public class RegistroActivity extends AppCompatActivity {
    EditText txtEmail, txtContrasena1, txtContrasena2;
    MaterialSpinner msTipoDeUso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.registro_usuario);
        inizialite();

    }

    private void inizialite() {
        msTipoDeUso = findViewById(R.id.msTipoDeUso);
        msTipoDeUso.setItems(Constants.LIST_TIPO_DE_USO);
        msTipoDeUso.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
