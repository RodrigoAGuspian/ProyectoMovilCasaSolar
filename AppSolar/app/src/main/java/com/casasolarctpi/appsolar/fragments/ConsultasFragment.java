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
import com.casasolarctpi.appsolar.models.DatosTH;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultasFragment extends Fragment implements OnClickListener, OnDateSetListener {
    View view;
    TabHost tabHost;
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
    List<DatosTH> datosGenerales = new ArrayList<>();
    private List<ILineDataSet> dataSets = new ArrayList<>();
    private XAxis xAxis;
    List<DatosTH> datosTHList = new ArrayList<>();
    public static List<String> labelsChart = new ArrayList<>();


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
        inputDataOfFirebase();
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

    private void inputDataOfFirebase() {
        DatabaseReference datos = MenuPrincipal.reference.child("datos");
        datos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<DatosTH>> t = new GenericTypeIndicator<List<DatosTH>>() {};
                datosGenerales = dataSnapshot.getValue(t);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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

    public void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),this, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();


    }


    public void showDatePickerWeekDialog(){
        Toast.makeText(getContext(), R.string.mensaje_week, Toast.LENGTH_LONG).show();
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.item_select_week);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final DatePicker datePicker = dialog.findViewById(R.id.calendarioWeek);
        Button btnAceptar = dialog.findViewById(R.id.btnAceptar);
        final Button btnCancelar = dialog.findViewById(R.id.btnCancelar);


        btnAceptar.setOnClickListener(new OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int mes = datePicker.getMonth()+1;

                String fecha1 = datePicker.getDayOfMonth()+"-"+mes+"-"+datePicker.getYear();
                Calendar calendar = new GregorianCalendar(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth());
                Calendar calendar1 = new GregorianCalendar(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth());
                calendar.setFirstDayOfWeek(Calendar.SUNDAY);

                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                calendar.set(Calendar.AM_PM, Calendar.AM);
                calendar.set(Calendar.HOUR, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);


                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                calendar.set(Calendar.AM_PM, Calendar.PM);
                calendar.set(Calendar.HOUR, 11);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);

                Date primerDia = calendar.getTime();
                Date ultimoDia = calendar1.getTime();

                @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

                txtDate2.setText(format.format(primerDia)+" "+getResources().getString(R.string.a)+" "+format.format(ultimoDia));

                showChartWeek(primerDia,ultimoDia);
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
                showChartDay();
                break;
        }
    }

    private void showChartDay() {
        List<Entry> entries = new ArrayList<>();
        List<Entry> entries1 = new ArrayList<>();
        lineChart1.clear();
        dataSets.clear();
        String hora = "";
        String tmpHora = "";
        List<Float> datosMinuto  = new ArrayList<>();
        List<Float> datosMinuto1  = new ArrayList<>();
        int contador =0;
        for (int i=0;i<datosGenerales.size();i++){
            DatosTH datosTH = datosGenerales.get(i);
            if (datosTH.getFecha_dato().equals(fechaATexto)){
                hora = datosTH.getHora();

                if (tmpHora.length()<1){
                    tmpHora= datosTH.getHora();
                }
                if (hora.equals(tmpHora)){
                    datosMinuto.add(Float.parseFloat(datosTH.getHumedad()));
                    datosMinuto1.add(Float.parseFloat(datosTH.getTemperatura()));
                }else {
                    tmpHora= datosTH.getHora();
                    labelsChart.add(datosTH.getHora());
                    entries.add(new Entry(contador, promedio(datosMinuto)));
                    entries1.add(new Entry(contador, promedio(datosMinuto1)));
                    contador++;
                }

            }

            //Organizar codigo

        }

        //Log.e("Datos",entries.toString()+"\n"+labelsChart.toString());

        if (entries.size()!=0) {


            LineDataSet lineDataSet = new LineDataSet(entries, "Humedad");
            LineDataSet lineDataSet1 = new LineDataSet(entries1, "Temperatura");

            lineDataSet.setColor(getContext().getResources().getColor(R.color.colorGraficaPunto1));
            lineDataSet1.setColor(getContext().getResources().getColor(R.color.colorGraficaPunto2));

            lineDataSet.setCircleColor(getContext().getResources().getColor(R.color.colorGraficaLinea1));
            lineDataSet1.setCircleColor(getContext().getResources().getColor(R.color.colorGraficaLinea2));

            lineDataSet.setValueTextColor(getContext().getResources().getColor(R.color.colorGraficaLinea1));
            lineDataSet1.setValueTextColor(getContext().getResources().getColor(R.color.colorGraficaLinea2));

            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            lineDataSet1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

            dataSets.add(lineDataSet);
            dataSets.add(lineDataSet1);
            LineData data = new LineData(dataSets);
            data.setDrawValues(false);
            lineChart1.setData(data);
            Description description = new Description();
            description.setText("Fecha de los datos tomados: " + fechaATexto);
            xAxis = lineChart1.getXAxis();

            xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsChart));
            xAxis.setLabelRotationAngle(-10f);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            lineChart1.setDescription(description);
            lineChart1.setDrawMarkers(true);
            CustomMarkerView customMarkerView = new CustomMarkerView(getContext(), R.layout.item_custom_marker, labelsChart);
            lineChart1.setMarker(customMarkerView);
            lineChart1.setTouchEnabled(true);
            lineChart1.invalidate();


        }else {
            Toast.makeText(getContext(), R.string.no_hay_datos, Toast.LENGTH_SHORT).show();
        }
        pBConsultas.setVisibility(View.INVISIBLE);

    }

    private float promedio(List<Float> datosMinuto) {
        float acumulador=0;
        for (int i=0;i<datosMinuto.size();i++){
            acumulador+=datosMinuto.get(i);
        }
        try {
            return acumulador/datosMinuto.size();

        }catch (Exception ignore){
            return 0;

        }

    }


    private void showChartWeek(Date primerDia, Date ultimoDia) {

    }

    public void showChartMonth(){




    }

}
