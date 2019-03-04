package com.casasolarctpi.appsolar.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.casasolarctpi.appsolar.R;

public class MainActivity extends AppCompatActivity {
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inizialite();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MenuPrincipal.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void inizialite() {
        btnLogin = findViewById(R.id.btnLogin);
    }


}
