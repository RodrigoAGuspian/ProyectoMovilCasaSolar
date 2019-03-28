package com.casasolarctpi.appsolar.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.casasolarctpi.appsolar.R;
import com.casasolarctpi.appsolar.controllers.MenuPrincipal;
import com.casasolarctpi.appsolar.models.CustomMarkerViewData1;
import com.casasolarctpi.appsolar.models.DatosTiempoReal;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class IrradianciaFragment extends Fragment {

    //Declaración de variables
    private LineChart irradianciaChart;
    private View view;
    private List<Entry> entry1 = new ArrayList<>();
    private List<String> labelsChart = new ArrayList<>();
    private XAxis xAxis;
    boolean bandera = false;
    List<DatosTiempoReal> datosTiempoRealList = new ArrayList<>();

    public IrradianciaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_irradiancia, container, false);
        inizialite();
        inputDataFirebase();
        return view;
    }

    //Inicialización de vistas
    private void inizialite() {
        irradianciaChart = view.findViewById(R.id.irradianciaChart);

    }

    //Método para el ingreso de datos desde de la base de datos  (Real time DataBase)de Firebase
    private void inputDataFirebase() {
        DatabaseReference datosTiempoReal = MenuPrincipal.reference.child("tiempoReal");
        //Query para limitar los datos
        datosTiempoReal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<DatosTiempoReal>> t = new GenericTypeIndicator<ArrayList<DatosTiempoReal>>(){};
                datosTiempoRealList = dataSnapshot.getValue(t);

                if (!bandera) {
                    inputValuesChart();
                    bandera=true;
                }else {
                    inputValuesRealTime();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    //Método para el ingreso de los valores a la gráfica
    private void inputValuesChart() {
        for (int i=0; i<datosTiempoRealList.size();i++){
            labelsChart.add(datosTiempoRealList.get(i).getHora());
            float dato1=0;
            try {
                dato1=Float.parseFloat(datosTiempoRealList.get(i).getIrradiancia());
            }catch (Exception ignore){

            }
            entry1.add(new Entry(i,dato1));
        }

        final Date[] date1 = {new Date(),new Date()};
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Collections.sort(datosTiempoRealList, new Comparator<DatosTiempoReal>() {
            @Override
            public int compare(DatosTiempoReal o1, DatosTiempoReal o2) {
                try {
                    date1[0] =dateFormat.parse(o1.getFechaActual1());
                    date1[1] =dateFormat.parse(o2.getFechaActual1());
                    if (date1[0].getTime() < date1[1].getTime()) {
                        return -1;
                    }
                    if (date1[0].getTime() > date1[1].getTime()) {
                        return 1;
                    }
                    return 0;

                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        LineDataSet lineDataSet = new LineDataSet(entry1,getResources().getString(R.string.dato3));
        lineDataSet.setColor(getContext().getResources().getColor(R.color.colorGraficaPunto3));
        lineDataSet.setCircleColor(getContext().getResources().getColor(R.color.colorGraficaPunto3));
        lineDataSet.setValueTextColor(getContext().getResources().getColor(R.color.colorGraficaPunto3));
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setDrawCircles(false);
        LineData data= new LineData(lineDataSet);
        data.setDrawValues(false);
        Description description = new Description();
        irradianciaChart.setData(data);
        description.setText(getString(R.string.fecha_datos_tomados)+" "+datosTiempoRealList.get(0).getFechaActual());
        xAxis = irradianciaChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsChart));
        xAxis.setLabelRotationAngle(-10f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis yAxisLeft = irradianciaChart.getAxisLeft();
        YAxis yAxisRight = irradianciaChart.getAxisRight();
        yAxisLeft.setAxisMaximum(1400);

        yAxisRight.setAxisMaximum(1400);
        yAxisLeft.setAxisMinimum(0);
        yAxisRight.setAxisMinimum(0);


        irradianciaChart.setDescription(description);
        irradianciaChart.setDrawMarkers(true);
        CustomMarkerViewData1 customMarkerView = new CustomMarkerViewData1(getContext(),R.layout.item_custom_marker,labelsChart);
        customMarkerView.setTipoDelDato(getResources().getString(R.string.dato3));
        customMarkerView.setColorDelDato(getResources().getColor(R.color.colorGraficaLinea3));
        irradianciaChart.setMarker(customMarkerView);
        irradianciaChart.setTouchEnabled(true);
        irradianciaChart.invalidate();

    }

    //Método para el ingrese de datos a la gráfica a tiempo real
    private void inputValuesRealTime() {
        entry1.clear();
        labelsChart.clear();

        final Date[] date1 = {new Date(),new Date()};
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Collections.sort(datosTiempoRealList, new Comparator<DatosTiempoReal>() {
            @Override
            public int compare(DatosTiempoReal o1, DatosTiempoReal o2) {
                try {
                    date1[0] =dateFormat.parse(o1.getFechaActual1());
                    date1[1] =dateFormat.parse(o2.getFechaActual1());
                    if (date1[0].getTime() < date1[1].getTime()) {
                        return -1;
                    }
                    if (date1[0].getTime() > date1[1].getTime()) {
                        return 1;
                    }
                    return 0;

                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        for (int i=0; i<datosTiempoRealList.size();i++){
            labelsChart.add(datosTiempoRealList.get(i).getHora());
            float dato=0;
            try {
                dato=Float.parseFloat(datosTiempoRealList.get(i).getIrradiancia());
            }catch (Exception ignore){

            }
            entry1.add(new Entry(i,dato));
            irradianciaChart.notifyDataSetChanged();
            irradianciaChart.invalidate();


        }
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsChart));

    }


}
