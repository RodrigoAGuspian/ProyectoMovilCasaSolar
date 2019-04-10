package com.casasolarctpi.appsolar.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import org.w3c.dom.Text;

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
public class TiempoRealFragment extends Fragment {

    //Declaración de variables
    public static LineChart tiempoRealChart;
    private TextView txtGrafica;
    private View view;
    public static List<Entry> entry1 = new ArrayList<>();
    private List<String> labelsChart = new ArrayList<>();
    private XAxis xAxis;
    private boolean bandera = false;
    List<DatosTiempoReal> datosTiempoRealList = new ArrayList<>();
    float valorMaximo, valorMinimo;
    public static int modoGraficar;
    private int colorGrafica, colorTextoGrafica;
    private String tipoDeDato;

    public TiempoRealFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tiempo_real, container, false);
        inizialite();
        inizialiteValues();
        inputDataFirebase();
        return view;
    }

    //Inicialización de vistas
    private void inizialite() {
        tiempoRealChart = view.findViewById(R.id.tiempoRealChart);
        txtGrafica = view.findViewById(R.id.txtTituloChartRT);

    }

    //Inicializar valores para los diferentes tiempos de datos
    private void inizialiteValues(){
        switch (modoGraficar){
            case 0:
                colorGrafica = getResources().getColor(R.color.colorGraficaLinea1);
                colorTextoGrafica = getResources().getColor(R.color.colorGraficaPunto1);
                tipoDeDato = getResources().getString(R.string.dato1);
                txtGrafica.setText(R.string.titulo_dato1);
                break;


            case 1:
                colorGrafica = getResources().getColor(R.color.colorGraficaLinea2);
                colorTextoGrafica = getResources().getColor(R.color.colorGraficaPunto2);
                tipoDeDato = getResources().getString(R.string.dato2);
                txtGrafica.setText(R.string.titulo_dato2);

                break;

            case 2:
                colorGrafica = getResources().getColor(R.color.colorGraficaLinea3);
                colorTextoGrafica = getResources().getColor(R.color.colorGraficaPunto3);
                tipoDeDato = getResources().getString(R.string.dato3);
                txtGrafica.setText(R.string.titulo_dato3);

                break;
        }
        txtGrafica.setTextColor(colorTextoGrafica);
        tiempoRealChart.setVisibility(View.INVISIBLE);
        bandera=false;
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
                    if (entry1.size()>0) {
                        inputValuesRealTime();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    //Método para el ingreso de los valores a la gráfica
    private void inputValuesChart() {
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

                switch (modoGraficar){
                    case 0:
                        dato=Float.parseFloat(datosTiempoRealList.get(i).getHumedad());
                        break;

                    case 1:
                        dato=Float.parseFloat(datosTiempoRealList.get(i).getTemperatura());
                        break;

                    case 2:
                        dato=Float.parseFloat(datosTiempoRealList.get(i).getIrradiancia());
                        break;

                }


                if (dato>valorMaximo){
                    valorMaximo = dato;
                }

                if (valorMinimo==0){
                    valorMinimo=dato;
                }
                if (dato<valorMinimo){
                    valorMinimo = dato;
                }

            }catch (Exception ignore){

            }
            entry1.add(new Entry(i,dato));
        }


        if (entry1.size()>0) {
            LineDataSet lineDataSet = new LineDataSet(entry1, tipoDeDato);
            lineDataSet.setColor(colorGrafica);
            lineDataSet.setValueTextColor(colorTextoGrafica);
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            lineDataSet.setDrawCircles(false);
            LineData data = new LineData(lineDataSet);
            data.setDrawValues(false);
            Description description = new Description();
            tiempoRealChart.setData(data);
            description.setText(getString(R.string.fecha_datos_tomados) + " " + datosTiempoRealList.get(0).getFechaActual());
            xAxis = tiempoRealChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsChart));
            xAxis.setLabelRotationAngle(-10f);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            YAxis yAxisLeft = tiempoRealChart.getAxisLeft();
            YAxis yAxisRight = tiempoRealChart.getAxisRight();

            if (valorMinimo > 10) {
                valorMinimo -= 0.2f;
            }


            yAxisLeft.setAxisMaximum(valorMaximo + 0.2f);
            yAxisRight.setAxisMaximum(valorMaximo + 0.2f);
            yAxisLeft.setAxisMinimum(valorMinimo);
            yAxisRight.setAxisMinimum(valorMinimo);
            valorMaximo = 0;
            valorMinimo = 0;

            tiempoRealChart.setDescription(description);
            tiempoRealChart.setDrawMarkers(true);
            CustomMarkerViewData1 customMarkerView = new CustomMarkerViewData1(getContext(), R.layout.item_custom_marker, labelsChart);
            customMarkerView.setTipoDelDato(tipoDeDato);
            customMarkerView.setSizeList(labelsChart.size());
            customMarkerView.setColorDelDato(colorGrafica);
            tiempoRealChart.setMarker(customMarkerView);
            tiempoRealChart.setTouchEnabled(true);
            tiempoRealChart.setVisibility(View.VISIBLE);
            tiempoRealChart.invalidate();

        }
    }

    //Método para el ingrese de daots a la gráfica a tiempo real
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
                switch (modoGraficar) {
                    case 0:
                        dato = Float.parseFloat(datosTiempoRealList.get(i).getHumedad());
                        break;

                    case 1:
                        dato = Float.parseFloat(datosTiempoRealList.get(i).getTemperatura());
                        break;

                    case 2:
                        dato = Float.parseFloat(datosTiempoRealList.get(i).getIrradiancia());
                        break;
                }

                if (dato>valorMaximo){
                    valorMaximo = dato;
                }

                if (valorMinimo==0){
                    valorMinimo=dato;
                }
                if (dato<valorMinimo){
                    valorMinimo = dato;
                }

            }catch (Exception ignore){

            }
            entry1.add(new Entry(i,dato));

        }

        if (entry1.size()>0){
            tiempoRealChart.notifyDataSetChanged();
            tiempoRealChart.invalidate();
            tiempoRealChart.setVisibility(View.VISIBLE);
        }
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsChart));

        YAxis yAxisLeft = tiempoRealChart.getAxisLeft();
        YAxis yAxisRight = tiempoRealChart.getAxisRight();

        if (valorMinimo>10){
            valorMinimo-=0.1f;
        }


        yAxisLeft.setAxisMaximum(valorMaximo+0.2f);
        yAxisRight.setAxisMaximum(valorMaximo+0.2f);
        yAxisLeft.setAxisMinimum(valorMinimo);
        yAxisRight.setAxisMinimum(valorMinimo);
        valorMaximo=0;
        valorMinimo=0;



    }


}
