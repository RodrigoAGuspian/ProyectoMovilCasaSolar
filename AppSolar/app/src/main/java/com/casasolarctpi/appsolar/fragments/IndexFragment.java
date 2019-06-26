package com.casasolarctpi.appsolar.fragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.casasolarctpi.appsolar.R;
import com.casasolarctpi.appsolar.controllers.MenuPrincipal;
import com.casasolarctpi.appsolar.models.AdapterSummaryList;
import com.casasolarctpi.appsolar.models.DatosCompletos;
import com.casasolarctpi.appsolar.models.DatosPromedio;
import com.casasolarctpi.appsolar.models.SummaryData;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndexFragment extends Fragment {
    //Declaración de variables
    private View view;
    Button btnLeerMas;
    DatosPromedio dataAverage = new DatosPromedio();
    RecyclerView recyclerView;
    Calendar calendar = Calendar.getInstance();
    String fecha;
    public IndexFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_index, container, false);
        FirebaseApp.initializeApp(Objects.requireNonNull(getContext()));
        inizialite();
        onListener();
        queryResume();
        insertData();
        return view;

    }

    //inicializacion de vistas
    private void inizialite() {
        btnLeerMas = view.findViewById(R.id.btnLeerMas);
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    private void queryResume(){
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int realMonth = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH)-1;
        fecha = day+"/"+realMonth+"/"+year;
        getDataDayOFFireBase(year,realMonth,day);

    }

    //Método para obtener los valores del día seleccionado.
    private void getDataDayOFFireBase(int year, int month, int dayOfMonth) {
        DatabaseReference datosDia = MenuPrincipal.reference.child("datos").child("y" + year).child("m" + month).child("d" + dayOfMonth);
        datosDia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<DatosCompletos>> t = new GenericTypeIndicator<ArrayList<DatosCompletos>>() {
                };
                try {
                    dataAverage = promedioDia(dataSnapshot.getValue(t));
                    insertData();


                } catch (Exception ignore) {


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("asd", "error");
            }

        });
    }

    private void onListener(){
        btnLeerMas.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
                dialog.setContentView(R.layout.item_mas_info);
                final TextView txtInfo = dialog.findViewById(R.id.txtInfoPrincipal);
                ScrollView scrollMasInfo = dialog.findViewById(R.id.scrollMasInfo);
                txtInfo.setMovementMethod(new ScrollingMovementMethod());

                scrollMasInfo.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        txtInfo.getParent().requestDisallowInterceptTouchEvent(false);
                        return false;
                    }
                });

                txtInfo.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        txtInfo.getParent().requestDisallowInterceptTouchEvent(true);

                        return false;
                    }
                });
                Button btnOK = dialog.findViewById(R.id.btnOk);
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.setCancelable(true);
                dialog.show();

            }
        });
    }

    //Método para promediar los datos del diá.
    private DatosPromedio promedioDia(List<DatosCompletos> datosFiltrado) {
        DatosPromedio acumulador= new DatosPromedio();
        DatosPromedio datosPromedioFinales= new DatosPromedio();
        int  acmH = 0;
        int contador = 0;

        List<DatosPromedio> datosPorHoras = new ArrayList<>(1);
        try {
            for (int i =0 ; i<datosFiltrado.size(); i++){
                DatosCompletos el1 = datosFiltrado.get(i);
                try {
                    acumulador.setIrradianciaPromedio(acumulador.getIrradianciaPromedio() + Float.parseFloat(el1.getIrradiancia()));
                    acumulador.setHumedadPromedio(acumulador.getHumedadPromedio() + Float.parseFloat(el1.getHumedad()));
                    acumulador.setCorrientePromedio(acumulador.getCorrientePromedio() + Float.parseFloat(el1.getCorrientePanel()));
                    acumulador.setVoltajePromedio(acumulador.getVoltajePromedio() + Float.parseFloat(el1.getVoltajePanel()));
                    acumulador.setTemperaturaPromedio(acumulador.getTemperaturaPromedio() + Float.parseFloat(el1.getTemperatura()));
                    contador++;
                }catch (Exception ignore1) {

                }
                try {
                    Date horaDato;
                    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
                    horaDato = timeFormat.parse(el1.getHora());
                    if (acmH == 0){
                        acmH = horaDato.getHours();
                    }
                    if (horaDato.getHours() == acmH){
                        acumulador.setHumedadPromedio(acumulador.getHumedadPromedio() / contador);
                        acumulador.setCorrientePromedio(acumulador.getCorrientePromedio() / contador);
                        acumulador.setVoltajePromedio(acumulador.getVoltajePromedio() / contador);
                        acumulador.setTemperaturaPromedio(acumulador.getTemperaturaPromedio() / contador);
                        acumulador.setPotenciaPromedio(acumulador.getVoltajePromedio() * acumulador.getCorrientePromedio());
                        datosPorHoras.add(acumulador);
                        acumulador = new DatosPromedio();
                        acmH++;
                        contador = 0;
                    } else {
                        if (horaDato.getHours() - 1 > acmH || acmH ==0){
                            acmH = horaDato.getHours() + 1;
                        }
                    }

                } catch (Exception e) {

                }

            }

            for ( DatosPromedio element : datosPorHoras ) {
                datosPromedioFinales.setIrradianciaPromedio(element.getIrradianciaPromedio() + datosPromedioFinales.getIrradianciaPromedio());
                datosPromedioFinales.setHumedadPromedio(element.getHumedadPromedio() + datosPromedioFinales.getHumedadPromedio());
                datosPromedioFinales.setCorrientePromedio(element.getCorrientePromedio() + datosPromedioFinales.getCorrientePromedio());
                datosPromedioFinales.setVoltajePromedio(element.getVoltajePromedio() + datosPromedioFinales.getVoltajePromedio());
                datosPromedioFinales.setTemperaturaPromedio(element.getTemperaturaPromedio() + datosPromedioFinales.getTemperaturaPromedio());
                datosPromedioFinales.setPotenciaPromedio(element.getPotenciaPromedio() + datosPromedioFinales.getPotenciaPromedio());

            }




        }catch (Exception ignore){

        }


        return datosPromedioFinales;

    }

    private void insertData(){
        List<SummaryData> summaryData = new ArrayList<>();

        summaryData.add(new SummaryData(getString(R.string.dia),fecha));
        summaryData.add(new SummaryData(getString(R.string.irradiancia_global_w_h),Float.toString(dataAverage.getIrradianciaPromedio())));
        summaryData.add(new SummaryData(getString(R.string.energia_dia),Float.toString(dataAverage.getPotenciaPromedio())));
        summaryData.add(new SummaryData(getString(R.string.horas_de_sol),Float.toString(dataAverage.getIrradianciaPromedio()/1000)));
        AdapterSummaryList adapterSummaryList = new AdapterSummaryList(summaryData);
        recyclerView.setAdapter(adapterSummaryList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setHasFixedSize(true);

    }







}
