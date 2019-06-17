package com.casasolarctpi.appsolar.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.casasolarctpi.appsolar.R;
import com.casasolarctpi.appsolar.controllers.MenuPrincipal;
import com.casasolarctpi.appsolar.controllers.RestaurarContrasenaActivity;
import com.casasolarctpi.appsolar.models.Constants;
import com.casasolarctpi.appsolar.models.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment implements OnClickListener {
    EditText txtEmail, txtPrimerN, txtSegundoN, txtPrimerA, txtSegundoA;
    EditText txtInstitucion, txtPais, txtDepartamento, txtCiudad;
    TextView txtOlvidoLaContrasena;
    MaterialSpinner msTipoDeUso;
    private FirebaseAuth mAuth;
    UserData userData = new UserData();
    View view;
    FirebaseUser user;

    public PerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_perfil, container, false);
        inizialite();
        try {
            inizialiteFirebase();
            datosPrevios();
        }catch (Exception ignored){
            view.findViewById(R.id.btnActualizar).setVisibility(View.INVISIBLE);
            try {
                Toast.makeText(getContext(), getString(R.string.cargar_de_nuevo), Toast.LENGTH_SHORT).show();

            }catch (Exception ignored2){

            }
        }

        return view;
    }


    //Inicializar las vistas  y hacer el botón seleccionable.
    private void inizialite() {
        txtEmail = view.findViewById(R.id.txtEmail);
        txtPrimerN = view.findViewById(R.id.txtPrimerN);
        txtSegundoN = view.findViewById(R.id.txtSegundoN);
        txtPrimerA = view.findViewById(R.id.txtPrimerA);
        txtSegundoA = view.findViewById(R.id.txtSegundoA);
        txtInstitucion = view.findViewById(R.id.txtInstitucion);
        txtPais = view.findViewById(R.id.txtPais);
        txtDepartamento = view.findViewById(R.id.txtDepartamento);
        txtCiudad = view.findViewById(R.id.txtCiudad);
        msTipoDeUso = view.findViewById(R.id.msTipoDeUso);
        msTipoDeUso.setItems(Constants.LIST_TIPO_DE_USO);
        txtOlvidoLaContrasena = view.findViewById(R.id.txtOlvidoLaContrasena);
        txtOlvidoLaContrasena.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        txtOlvidoLaContrasena.setOnClickListener(this);
        view.findViewById(R.id.btnActualizar).setOnClickListener(this);
        view.findViewById(R.id.progressBarPerfil).setVisibility(View.INVISIBLE);

    }

    //Método para inicializar la autentificación de Firebase.
    private void inizialiteFirebase() {
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    //Método para cargar los datos del usuario.
    private void datosPrevios(){
        userData = MenuPrincipal.userData;
        txtEmail.setText(userData.getEmail());
        txtPrimerN.setText(userData.getPrimerN());
        txtSegundoN.setText(userData.getSegundoN());
        txtPrimerA.setText(userData.getPrimerA());
        txtSegundoA.setText(userData.getSegundoA());
        txtInstitucion.setText(userData.getInstitucion());
        txtPais.setText(userData.getPais());
        txtDepartamento.setText(userData.getDepartamento());
        txtCiudad.setText(userData.getCiudad());
        for (int i=0; i<msTipoDeUso.getItems().size();i++){
            if (msTipoDeUso.getItems().get(i).equals(userData.getTipoDeUso())){
                msTipoDeUso.setSelectedIndex(i);
            }
        }

    }

    //Método encargado de actualizar los datos.
    private void actualizarDatos(){
        if (validarCampos()){
            final Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.item_reautenticar);
            final EditText txtContrasena1 = dialog.findViewById(R.id.txtContrasena);
            final Button btnAceptar = dialog.findViewById(R.id.btnAceptar1);
            final Button btnCancelar = dialog.findViewById(R.id.btnCancelar1);
            dialog.findViewById(R.id.pBReautentificar).setVisibility(View.INVISIBLE);

            btnAceptar.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgressDialog();
                    if (validarCamposDialog()){

                        txtContrasena1.setEnabled(false);
                        btnAceptar.setEnabled(false);
                        btnCancelar.setEnabled(false);
                        dialog.findViewById(R.id.pBReautentificar).setVisibility(View.VISIBLE);

                        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), txtContrasena1.getText().toString());
                        assert user != null;
                        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){

                                    DatabaseReference nuevoUsuario = MenuPrincipal.reference.child("usuarios").child(MenuPrincipal.userKey);
                                    userData = nuevosDatosDelUsuario();
                                    MenuPrincipal.userData = userData;
                                    nuevoUsuario.setValue(nuevosDatosDelUsuario());


                                    Toast.makeText(getContext(), R.string.se_han_actualizado_datos, Toast.LENGTH_SHORT).show();
                                    if (!Objects.equals(user.getEmail(), txtEmail.getText().toString())){
                                        user.updateEmail(txtEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    hideProgressDialog();
                                                    dialog.findViewById(R.id.pBReautentificar).setVisibility(View.INVISIBLE);
                                                    dialog.cancel();
                                                }
                                            }
                                        });
                                    }else {
                                        hideProgressDialog();
                                        dialog.findViewById(R.id.pBReautentificar).setVisibility(View.INVISIBLE);
                                        dialog.cancel();
                                    }



                                }else {
                                    txtContrasena1.setEnabled(true);
                                    btnAceptar.setEnabled(true);
                                    btnCancelar.setEnabled(true);
                                    dialog.findViewById(R.id.pBReautentificar).setVisibility(View.INVISIBLE);
                                    hideProgressDialog();
                                    Toast.makeText(getContext(), R.string.la_contrasena_no_coincide, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
                private boolean validarCamposDialog() {
                    boolean valid = true;

                    String password = txtContrasena1.getText().toString();
                    if (TextUtils.isEmpty(password)) {
                        txtContrasena1.setError(getString(R.string.este_valor_requerido));
                        valid = false;
                    } else {
                        txtContrasena1.setError(null);
                    }

                    if (!valid){
                        Toast.makeText(getContext(), R.string.mensajes_campos_vacios, Toast.LENGTH_SHORT).show();
                    }

                    return valid;
                };
            });

            btnCancelar.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideProgressDialog();
                    dialog.cancel();
                }
            });

            dialog.show();
        }
    }

    private boolean validarCampos() {
        boolean valid =true;
        String email = txtEmail.getText().toString();
        if (TextUtils.isEmpty(email)){
            txtEmail.setError(getResources().getString(R.string.este_valor_requerido));
            valid=false;
        }else {
            txtEmail.setError(null);
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

    //Función que retorna un objeto UserData para la obtención de los datos previamente modificados.
    private UserData nuevosDatosDelUsuario(){
        UserData tmpUserData = new UserData();
        tmpUserData.setEmail(txtEmail.getText().toString());
        tmpUserData.setPrimerN(txtPrimerN.getText().toString());
        tmpUserData.setSegundoN(txtSegundoN.getText().toString());
        tmpUserData.setPrimerA(txtPrimerA.getText().toString());
        tmpUserData.setSegundoA(txtSegundoA.getText().toString());
        tmpUserData.setTipoDeUso(msTipoDeUso.getItems().get(msTipoDeUso.getSelectedIndex()).toString());
        tmpUserData.setInstitucion(txtInstitucion.getText().toString());
        tmpUserData.setPais(txtPais.getText().toString());
        tmpUserData.setDepartamento(txtDepartamento.getText().toString());
        tmpUserData.setCiudad(txtCiudad.getText().toString());
        return tmpUserData;
    }

    //Método para mostrar el progressDialog como señal de carga.
    private void showProgressDialog() {
        view.findViewById(R.id.progressBarPerfil).setVisibility(View.VISIBLE);
        view.findViewById(R.id.btnActualizar).setEnabled(false);
        txtEmail.setEnabled(false);
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
        view.findViewById(R.id.progressBarPerfil).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.btnActualizar).setEnabled(true);
        txtEmail.setEnabled(true);
        txtPrimerN.setEnabled(true);
        txtSegundoN.setEnabled(true);
        txtPrimerA.setEnabled(true);
        txtSegundoA.setEnabled(true);
        txtInstitucion.setEnabled(true);
        txtPais.setEnabled(true);
        txtDepartamento.setEnabled(true);
        txtCiudad.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnActualizar:
                actualizarDatos();
                break;

            case R.id.txtOlvidoLaContrasena:
                Intent intent = new Intent(getContext(), RestaurarContrasenaActivity.class);
                startActivity(intent);
                break;
        }

    }

}
