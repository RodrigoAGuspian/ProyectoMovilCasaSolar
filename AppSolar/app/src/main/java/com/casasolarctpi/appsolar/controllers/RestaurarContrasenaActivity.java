package com.casasolarctpi.appsolar.controllers;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.casasolarctpi.appsolar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RestaurarContrasenaActivity extends AppCompatActivity implements OnClickListener {

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurar_contrasena);
        findViewById(R.id.btnCambiarContrasena).setOnClickListener(this);
        findViewById(R.id.btnReestablecerContrasena).setOnClickListener(this);
        inizialiteFirebase();
    }


    private void inizialiteFirebase(){
        user = FirebaseAuth.getInstance().getCurrentUser();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCambiarContrasena:
                cambioDeContraseña();
                break;
                
            case R.id.btnReestablecerContrasena:
                break;
        }
    }

    private void cambioDeContraseña() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.item_cambio_contrasena);
        final EditText txtContrasenaActual = dialog.findViewById(R.id.txtContrasenaActual);
        final EditText txtNuevaContrasena = dialog.findViewById(R.id.txtNuevaContrasena);
        final EditText txtConfirmarContrasenaNueva = dialog.findViewById(R.id.txtConfirmarContrasenaNueva);
        final Button btnAceptar = dialog.findViewById(R.id.btnAceptar1);
        final Button btnCancelar = dialog.findViewById(R.id.btnCancelar1);
        dialog.findViewById(R.id.pBCambioContra).setVisibility(View.INVISIBLE);

        btnAceptar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validar() && compararContrasenas()){
                    showProgressBar();
                    AuthCredential credential = EmailAuthProvider.getCredential(MenuPrincipal.userData.getEmail(), txtContrasenaActual.getText().toString());
                    assert user != null;
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                user.updatePassword(txtNuevaContrasena.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            dialog.cancel();
                                        }else {
                                            hideProgressBar();
                                            Toast.makeText(RestaurarContrasenaActivity.this, R.string.hay_un_error_actualizar_contra, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else {
                                hideProgressBar();
                                Toast.makeText(RestaurarContrasenaActivity.this, R.string.las_contrasena_actual_no_coincide, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            private void showProgressBar() {
                txtContrasenaActual.setEnabled(false);
                txtNuevaContrasena.setEnabled(false);
                txtConfirmarContrasenaNueva.setEnabled(false);
                btnAceptar.setEnabled(false);
                btnCancelar.setEnabled(false);
                dialog.findViewById(R.id.pBCambioContra).setVisibility(View.VISIBLE);
            }

            private void hideProgressBar() {
                txtContrasenaActual.setEnabled(true);
                txtNuevaContrasena.setEnabled(true);
                txtConfirmarContrasenaNueva.setEnabled(true);
                btnAceptar.setEnabled(true);
                btnCancelar.setEnabled(true);
                dialog.findViewById(R.id.pBCambioContra).setVisibility(View.INVISIBLE);
            }

            private boolean validar() {
                boolean valid = true;
                String password = txtContrasenaActual.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    txtContrasenaActual.setError(getString(R.string.este_valor_requerido));
                    valid = false;
                } else {
                    txtContrasenaActual.setError(null);
                }

                String password1 = txtNuevaContrasena.getText().toString();
                if (TextUtils.isEmpty(password1)) {
                    txtNuevaContrasena.setError(getString(R.string.este_valor_requerido));
                    valid = false;
                } else {
                    txtNuevaContrasena.setError(null);
                }

                String password2 = txtConfirmarContrasenaNueva.getText().toString();
                if (TextUtils.isEmpty(password2)) {
                    txtConfirmarContrasenaNueva.setError(getString(R.string.este_valor_requerido));
                    valid = false;
                } else {
                    txtConfirmarContrasenaNueva.setError(null);
                }

                if (!valid){
                    Toast.makeText(RestaurarContrasenaActivity.this, "", Toast.LENGTH_SHORT).show();
                }


                return valid;
            }

            private boolean compararContrasenas(){
                boolean valid = true;

                String password1 = txtNuevaContrasena.getText().toString();
                String password2 = txtConfirmarContrasenaNueva.getText().toString();

                if (password1.equals(password2)) {
                    valid = true;
                } else {
                    txtNuevaContrasena.setError(getString(R.string.contrasenas_no_coinciden));
                    txtConfirmarContrasenaNueva.setError(getString(R.string.contrasenas_no_coinciden));
                    Toast.makeText(RestaurarContrasenaActivity.this, R.string.contrasenas_no_coinciden, Toast.LENGTH_SHORT).show();
                    valid = false;
                }

                return valid;

            }
        });

        btnCancelar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }
}
