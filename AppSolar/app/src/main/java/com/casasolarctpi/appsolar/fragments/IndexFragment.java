package com.casasolarctpi.appsolar.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.casasolarctpi.appsolar.R;
import com.casasolarctpi.appsolar.controllers.MenuPrincipal;
import com.casasolarctpi.appsolar.models.CustomMarkerView;
import com.casasolarctpi.appsolar.models.DatosTiempoReal;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndexFragment extends Fragment {


    public IndexFragment() {
        // Required empty public constructor
    }

    //Declaración de variables
    private TextView txtInfoPrincipal;
    private LineChart lineChart;
    private View view;
    private List<Entry> entries = new ArrayList<>();
    private List<Entry> entries1 = new ArrayList<>();
    private List<Entry> entries2 = new ArrayList<>();
    private List<ILineDataSet> dataSets = new ArrayList<>();
    private XAxis xAxis;
    public boolean bandera = false;
    public List<DatosTiempoReal> datosTiempoRealList = new ArrayList<>();
    public static List<String> labelsChart = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FirebaseApp.initializeApp(Objects.requireNonNull(getContext()));
        view =inflater.inflate(R.layout.fragment_index, container, false);
        inizialite();
        haveScroll();
        inizialiteDataReference();
        return view;
    }

    private void inizialiteDataReference() {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int anio = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        inputDataFirebase(anio,mes,dia);

    }

    //Método para crear ejecto scroll al texto principal
    private void haveScroll() {
        txtInfoPrincipal.setMovementMethod(new ScrollingMovementMethod());
    }

    //inicializacion de vistas
    private void inizialite() {
        lineChart = view.findViewById(R.id.chart);
        txtInfoPrincipal = view.findViewById(R.id.txtInfoPrincipal);
    }

    //Método para el ingreso de datos desde de la base de datos  (Real time DataBase)de Firebase
    public void inputDataFirebase(int anio, int mes, int dia){
        DatabaseReference datosTiempoReal = MenuPrincipal.reference.child("tiempoReal");
        //Query para limitar los datos
        datosTiempoReal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<DatosTiempoReal>> t = new GenericTypeIndicator<ArrayList<DatosTiempoReal>>(){};
                datosTiempoRealList = dataSnapshot.getValue(t);

                if (!bandera) {
                    try {
                        inputValuesChart();
                        bandera=true;
                    }catch (Exception e){

                    }

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
    public void inputValuesChart() {
        for (int i = 0; i < datosTiempoRealList.size(); i++) {
            labelsChart.add(datosTiempoRealList.get(i).getHora());
            float dato1 = 0;
            float dato2 = 0;
            float dato3 = 0;
            try {
                dato1 = Float.parseFloat(datosTiempoRealList.get(i).getHumedad());
                dato2 = Float.parseFloat(datosTiempoRealList.get(i).getTemperatura());
                dato3 = Float.parseFloat(datosTiempoRealList.get(i).getIrradiancia());

            } catch (Exception ignore) {

            }
            entries.add(new Entry(i, dato1));
            entries1.add(new Entry(i, dato2));
            entries2.add(new Entry(i, dato3));
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


        LineDataSet lineDataSet = new LineDataSet(entries, getResources().getString(R.string.dato1));
        LineDataSet lineDataSet1 = new LineDataSet(entries1, getResources().getString(R.string.dato2));
        LineDataSet lineDataSet2 = new LineDataSet(entries2, getResources().getString(R.string.dato3));


        lineDataSet.setColor(getResources().getColor(R.color.colorGraficaPunto1));
        lineDataSet1.setColor(getResources().getColor(R.color.colorGraficaPunto2));
        lineDataSet2.setColor(getResources().getColor(R.color.colorGraficaPunto3));


        lineDataSet.setValueTextColor(getResources().getColor(R.color.colorGraficaLinea1));
        lineDataSet1.setValueTextColor(getResources().getColor(R.color.colorGraficaLinea2));

        lineDataSet.setDrawCircles(false);
        lineDataSet1.setDrawCircles(false);
        lineDataSet2.setDrawCircles(false);

        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet2.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet1.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet2.setAxisDependency(YAxis.AxisDependency.RIGHT);

        dataSets.add(lineDataSet);
        dataSets.add(lineDataSet1);
        dataSets.add(lineDataSet2);
        LineData data = new LineData(dataSets);
        data.setDrawValues(false);
        lineChart.setData(data);
        Description description = new Description();
        description.setText(getString(R.string.fecha_datos_tomados)+" "+datosTiempoRealList.get(0).getFechaActual());
        xAxis = lineChart.getXAxis();

        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsChart));
        xAxis.setLabelRotationAngle(-10f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis yAxisLeft = lineChart.getAxisLeft();
        YAxis yAxisRight = lineChart.getAxisRight();

        yAxisLeft.setAxisMaximum(100);
        yAxisLeft.setAxisMinimum(-20);
        yAxisRight.setAxisMaximum(1000);
        yAxisRight.setAxisMinimum(0);

        lineChart.setDescription(description);
        lineChart.setDrawMarkers(true);
        CustomMarkerView customMarkerView = new CustomMarkerView(getContext(), R.layout.item_custom_marker,labelsChart);
        lineChart.setMarker(customMarkerView);
        lineChart.setTouchEnabled(true);
        lineChart.invalidate();

    }


    //Método para el ingreso datos a la gráfica a tiempo real
    private void inputValuesRealTime() {
        entries.clear();
        entries1.clear();
        entries2.clear();
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


        for (int i = 0; i < datosTiempoRealList.size(); i++) {
            labelsChart.add(datosTiempoRealList.get(i).getHora());
            float dato1=0;
            float dato2=0;
            float dato3=0;
            try {
                dato1=Float.parseFloat(datosTiempoRealList.get(i).getHumedad());
                dato2=Float.parseFloat(datosTiempoRealList.get(i).getTemperatura());
                dato3=Float.parseFloat(datosTiempoRealList.get(i).getIrradiancia());

            }catch (Exception ignore){

            }
            entries.add(new Entry(i, dato1));
            entries1.add(new Entry(i, dato2));
            entries2.add(new Entry(i, dato3));
            lineChart.notifyDataSetChanged();
            lineChart.invalidate();

        }
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsChart));

    }

}
