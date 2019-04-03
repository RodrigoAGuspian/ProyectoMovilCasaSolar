package com.casasolarctpi.appsolar.controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.casasolarctpi.appsolar.R;

import java.util.Objects;

public class RegistroActivity extends AppCompatActivity {
    EditText txtEmail, txtContrasena1, txtContrasena2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.registro_usuario);

    }
}
