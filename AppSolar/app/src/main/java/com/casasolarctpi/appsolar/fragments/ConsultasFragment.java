package com.casasolarctpi.appsolar.fragments;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DownloadManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.Toast;

import com.casasolarctpi.appsolar.R;
import com.casasolarctpi.appsolar.controllers.MenuPrincipal;
import com.casasolarctpi.appsolar.models.Constants;
import com.casasolarctpi.appsolar.models.CustomMarkerView;
import com.casasolarctpi.appsolar.models.DatoSemana;
import com.casasolarctpi.appsolar.models.DatosCompletos;
import com.casasolarctpi.appsolar.models.DatosTH;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultasFragment extends Fragment implements OnClickListener, OnDateSetListener {
    View view;
    Button btnConsulta1, btnConsulta2, btnConsulta3;
    BarChart barChart1, barChart2;
    LineChart lineChart1;
    EditText txtDate1, txtDate2;
    MaterialSpinner mSMes;
    NumberPicker nPAnio;
    Date dateToQuery;
    String fechaATexto;
    Dialog dialog;
    ProgressBar pBConsultas;
    int mode;
    private List<ILineDataSet> dataSets = new ArrayList<>();
    private XAxis xAxis;
    public static List<String> labelsChart = new ArrayList<>();
    final List<DatosCompletos>[] datosCompletosSemana = new List[7];
    List<BarEntry> entriesBarWeek = new ArrayList<>();
    List<BarEntry> entriesBarWeek1 = new ArrayList<>();


    public ConsultasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_consultas, container, false);
        iniziliate();
        inputDataToSpinner();
        return view;
    }

    private void iniziliate() {
        TabHost tabHost = view.findViewById(R.id.tabHost);
        tabHost.setup();

        //Tab 1
        TabHost.TabSpec spec = tabHost.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Día");
        tabHost.addTab(spec);

        //Tab 2
        spec = tabHost.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Semana");
        tabHost.addTab(spec);

        //Tab 3
        spec = tabHost.newTabSpec("Tab Three");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Mes");
        tabHost.addTab(spec);

        btnConsulta1 = view.findViewById(R.id.btnConsulta1);
        btnConsulta2 = view.findViewById(R.id.btnConsulta2);
        btnConsulta3 = view.findViewById(R.id.btnConsulta3);

        btnConsulta1.setOnClickListener(this);
        btnConsulta2.setOnClickListener(this);
        btnConsulta3.setOnClickListener(this);

        lineChart1 = view.findViewById(R.id.lineChart1);
        barChart1 = view.findViewById(R.id.barChart1);
        barChart2 = view.findViewById(R.id.barChart2);

        lineChart1.setVisibility(View.INVISIBLE);
        barChart1.setVisibility(View.INVISIBLE);
        barChart2.setVisibility(View.INVISIBLE);

        txtDate1 = view.findViewById(R.id.txtConsulta1);
        txtDate2 = view.findViewById(R.id.txtConsulta2);

        mSMes = view.findViewById(R.id.spinnerMes);
        nPAnio = view.findViewById(R.id.nPAnio);

        pBConsultas = view.findViewById(R.id.pBConsultas);


    }

    public void inputDataToSpinner(){
        mSMes.setItems(Constants.MESES);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        nPAnio.setMinValue(Constants.MIN_YEAR);
        nPAnio.setMaxValue(Constants.MAX_YEAR);
        nPAnio.setValue(year);



    }

    private void getDataDayOFFireBase(int year, int month, int dayOfMonth){
        final List<DatosCompletos>[] datosCompletos = new List[]{new ArrayList<>()};
        DatabaseReference datosDia = MenuPrincipal.reference.child("datos").child("y"+year).child("m"+month).child("d"+dayOfMonth);

        datosDia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<DatosCompletos>> t = new GenericTypeIndicator<ArrayList<DatosCompletos>>() {};
                try {
                    showChartDay(dataSnapshot.getValue(t));

                }catch (Exception ignore){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    public void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),this, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();


    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        switch (mode) {
            case 1:
                pBConsultas.setVisibility(View.VISIBLE);
                int realMonth = month + 1;
                fechaATexto = dayOfMonth + "-" + realMonth + "-" + year;
                Calendar calendar = new GregorianCalendar(year,month,dayOfMonth);
                dateToQuery = calendar.getTime();
                txtDate1.setText(fechaATexto);
                //dateDay = new GregorianCalendar(year,month,dayOfMonth).getTime();
                getDataDayOFFireBase(year,realMonth,dayOfMonth);
                break;
        }
    }

    private void showChartDay(List<DatosCompletos> datosCompletosList) {
        List<Entry> entries = new ArrayList<>();
        List<Entry> entries1 = new ArrayList<>();
        lineChart1.clear();
        dataSets.clear();
        DatosCompletos datosCompletos;
        labelsChart = new ArrayList<>();
        if (datosCompletosList!=null) {
            for (int i = 0; i < datosCompletosList.size(); i++) {
                datosCompletos = datosCompletosList.get(i);
                labelsChart.add(datosCompletos.getHora());
                try {
                    entries.add(new Entry(i, Float.parseFloat(datosCompletos.getHumedad())));
                    entries1.add(new Entry(i,Float.parseFloat(datosCompletos.getTemperatura())));
                }catch (Exception ignored){

                }

            }
        }else {
            Toast.makeText(getContext(), getResources().getString(R.string.no_hay_datos), Toast.LENGTH_SHORT).show();
        }

        //Log.e("Datos",entries.toString()+"\n"+labelsChart.toString());

        if (entries.size()!=0) {


            LineDataSet lineDataSet = new LineDataSet(entries, getResources().getString(R.string.dato1));
            LineDataSet lineDataSet1 = new LineDataSet(entries1, getResources().getString(R.string.dato2));

            lineDataSet.setColor(getContext().getResources().getColor(R.color.colorGraficaPunto1));
            lineDataSet1.setColor(getContext().getResources().getColor(R.color.colorGraficaPunto2));

            lineDataSet.setValueTextColor(getContext().getResources().getColor(R.color.colorGraficaLinea1));
            lineDataSet1.setValueTextColor(getContext().getResources().getColor(R.color.colorGraficaLinea2));

            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            lineDataSet1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

            lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            lineDataSet1.setAxisDependency(YAxis.AxisDependency.RIGHT);

            lineDataSet.setDrawCircles(false);
            lineDataSet1.setDrawCircles(false);
            lineDataSet.setFormSize(10f);
            lineDataSet1.setFormSize(10f);

            dataSets.add(lineDataSet);
            dataSets.add(lineDataSet1);
            LineData data = new LineData(dataSets);
            data.setDrawValues(false);
            lineChart1.setData(data);
            Description description = new Description();
            description.setText(getResources().getString(R.string.fecha_datos_tomados) + fechaATexto);
            xAxis = lineChart1.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsChart));
            xAxis.setLabelRotationAngle(-10f);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            lineChart1.setDescription(description);
            lineChart1.setDrawMarkers(true);
            CustomMarkerView customMarkerView = new CustomMarkerView(getContext(), R.layout.item_custom_marker, labelsChart);
            lineChart1.setMarker(customMarkerView);
            lineChart1.setTouchEnabled(true);
            lineChart1.setVisibility(View.VISIBLE);
            lineChart1.invalidate();


        }else {
            Toast.makeText(getContext(), R.string.no_hay_datos, Toast.LENGTH_SHORT).show();
        }
        pBConsultas.setVisibility(View.INVISIBLE);

    }

    private void searchDate(Date primerDia){
        Calendar tmpCalendar = new GregorianCalendar();
        tmpCalendar.setTime(primerDia);
        Date tmpDate = tmpCalendar.getTime();
        int realMonth;
        int dia;
        int anio;

        for (int i=0; i<7;i++){
            realMonth=tmpDate.getMonth()+1;
            dia = tmpCalendar.get(Calendar.DAY_OF_MONTH);
            anio = tmpCalendar.get(Calendar.YEAR);
            getDataDayOFFireBaseWeek(anio,realMonth,dia,i);
            tmpCalendar.add(Calendar.DAY_OF_MONTH,1);
            tmpDate=tmpCalendar.getTime();

        }

    }

    private void getDataDayOFFireBaseWeek(int year, int month, int dayOfMonth,final int contador){

        DatabaseReference datosDia = MenuPrincipal.reference.child("datos").child("y"+year).child("m"+month).child("d"+dayOfMonth);
        datosDia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<DatosCompletos>> t = new GenericTypeIndicator<ArrayList<DatosCompletos>>() {};
                datosCompletosSemana[contador] = dataSnapshot.getValue(t);
                if (contador==6){
                    showChartWeek();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void showDatePickerWeekDialog(){
        Toast.makeText(getContext(), R.string.mensaje_week, Toast.LENGTH_SHORT).show();
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.item_select_week);
        final DatePicker datePicker = dialog.findViewById(R.id.calendarioWeek);
        Button btnAceptar = dialog.findViewById(R.id.btnAceptar);
        final Button btnCancelar = dialog.findViewById(R.id.btnCancelar);


        btnAceptar.setOnClickListener(new OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int mes = datePicker.getMonth()+1;

                String fecha1 = datePicker.getDayOfMonth()+"-"+mes+"-"+datePicker.getYear();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                Date date = new Date();
                try {
                    date = dateFormat.parse(fecha1);
                } catch (ParseException e) {
                    e.printStackTrace();

                }

                Calendar calendar = new GregorianCalendar(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth());
                Calendar calendar1 = new GregorianCalendar(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth());

                calendar.setTime(date);


                calendar.setFirstDayOfWeek(Calendar.SUNDAY);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                calendar.set(Calendar.AM_PM, Calendar.AM);
                calendar.set(Calendar.HOUR, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);


                calendar1.setTime(date);
                calendar1.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                calendar1.set(Calendar.AM_PM, Calendar.PM);
                calendar1.set(Calendar.HOUR, 11);
                calendar1.set(Calendar.MINUTE, 59);
                calendar1.set(Calendar.SECOND, 59);

                Date primerDia = calendar.getTime();
                Date ultimoDia = calendar1.getTime();

                @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

                txtDate2.setText(format.format(primerDia)+" "+getResources().getString(R.string.a)+" "+format.format(ultimoDia));

                searchDate(primerDia);
                dialog.cancel();

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

    private void showChartWeek() {
        entriesBarWeek = new ArrayList<>();
        entriesBarWeek1 = new ArrayList<>();
        barChart1.clear();
        barChart1.clearAnimation();


        for (int i=0; i<7;i++){
            entriesBarWeek.add(new BarEntry(i,promedioDia(datosCompletosSemana[i],0)));
            entriesBarWeek1.add(new BarEntry(i,promedioDia(datosCompletosSemana[i],1)));
        }

        BarDataSet barDataSet = new BarDataSet(entriesBarWeek,getResources().getString(R.string.dato1));
        BarDataSet barDataSet1 = new BarDataSet(entriesBarWeek1,getResources().getString(R.string.dato2));
        barDataSet.setColor(getContext().getResources().getColor(R.color.colorGraficaPunto1));
        barDataSet1.setColor(getContext().getResources().getColor(R.color.colorGraficaPunto2));
        barDataSet.setBarShadowColor(getContext().getResources().getColor(R.color.colorGraficaLinea1));
        barDataSet1.setBarShadowColor(getContext().getResources().getColor(R.color.colorGraficaLinea2));
        List<IBarDataSet> dataBarSets = new ArrayList<>();
        dataBarSets.add(barDataSet);
        dataBarSets.add(barDataSet1);
        BarData data = new BarData(barDataSet,barDataSet1);
        data.setBarWidth(0.48f); // set custom bar width
        barChart1.setData(data);
        barChart1.groupBars(0, 0.04f, 0f);
        xAxis = barChart1.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(Constants.DIAS_DE_LA_SEMANA));
        xAxis.setCenterAxisLabels(true);
        xAxis.setLabelRotationAngle(-5f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(8);
        xAxis.setAxisMaximum(7);

        YAxis yAxisLeft = barChart1.getAxisLeft();
        YAxis yAxisRight = barChart1.getAxisRight();
        yAxisLeft.setAxisMaximum(120);
        yAxisLeft.setAxisMinimum(0);
        yAxisRight.setAxisMaximum(120);
        yAxisRight.setAxisMinimum(0);
        barChart1.setVisibility(View.VISIBLE);
        barChart1.invalidate(); // refresh


    }

    private float promedioDia(List<DatosCompletos> datosFiltrado, int modo) {
        float acumulador=0;

        switch (modo){
            case 0:
                try {
                    for (int i=0;i<datosFiltrado.size();i++){
                        try {
                            acumulador+=Float.parseFloat(datosFiltrado.get(i).getHumedad());
                        }catch (Exception ignore){

                        }
                    }

                }catch (Exception ignore){

                }

                break;

            case 1:

                try {
                    for (int i=0;i<datosFiltrado.size();i++){
                        try {
                            acumulador+=Float.parseFloat(datosFiltrado.get(i).getTemperatura());
                        }catch (Exception ignore){

                        }
                    }

                }catch (Exception ignore){

                }

                break;
        }

        try {
            return acumulador/datosFiltrado.size();

        }catch (Exception ignore){
            return 0f;

        }

    }

    public void showChartMonth(){

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnConsulta1:
                mode=1;
                showDatePickerDialog();
                break;

            case R.id.btnConsulta2:
                mode=2;
                showDatePickerWeekDialog();
                break;


            case R.id.btnConsulta3:
                mode=3;
                showChartMonth();
                break;
        }
    }

}
