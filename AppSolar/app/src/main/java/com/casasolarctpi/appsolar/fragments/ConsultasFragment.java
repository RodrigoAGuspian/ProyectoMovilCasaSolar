package com.casasolarctpi.appsolar.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.casasolarctpi.appsolar.R;
import com.casasolarctpi.appsolar.controllers.MenuPrincipal;
import com.casasolarctpi.appsolar.models.Constants;
import com.casasolarctpi.appsolar.models.CustomMarkerViewData2;
import com.casasolarctpi.appsolar.models.CustomMarkerViewDataMonth;
import com.casasolarctpi.appsolar.models.DatosCompletos;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static android.view.View.VISIBLE;
import static com.jaredrummler.materialspinner.MaterialSpinner.INVISIBLE;
import static com.jaredrummler.materialspinner.MaterialSpinner.OnItemSelectedListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultasFragment extends Fragment implements OnClickListener, OnDateSetListener{
    //Declaración de variables
    View view;
    Button btnConsulta1, btnConsulta2, btnConsulta3;
    BarChart barChart1, barChart2;
    LineChart lineChart1;
    TextView txtDate1, txtDate2;
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
    List<DatosCompletos>[] datosCompletosMes = new List[31];
    List<BarEntry> entriesBarWeek = new ArrayList<>();
    List<BarEntry> entriesBarWeek1 = new ArrayList<>();
    int month, yearM, numDias;
    public static int modoGraficar = 0;
    TextView txtTituloGrafica1, txtTituloGrafica2, txtTituloGrafica3;
    String datoInfo1;
    String datoInfo2;

    int colorDato1, colorDato2, colorDatoTexto1, colorDatoTexto2, modo1, modo2, yAxisMax1, yAxisMin1, yAxisMax2, yAxisMin2;

    public ConsultasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_consultas, container, false);
        iniziliate();
        inputValuesToChart();
        inputDataToSpinner();
        TabHost tabHost = view.findViewById(R.id.tabHost);
        setCustomTabHost(tabHost);
        return view;
    }

    private void setCustomTabHost(TabHost tabHost) {
        TabWidget tabWidget = tabHost.getTabWidget();

    }

    //Método para inicializar los valores de las consultas que el usuario a seleccionado
    private void inputValuesToChart() {

        switch (modoGraficar) {
            case 0:
                datoInfo1 = getResources().getString(R.string.dato3);
                datoInfo2 = getResources().getString(R.string.dato1);

                colorDato1 = getResources().getColor(R.color.colorGraficaPunto3);
                colorDato2 = getResources().getColor(R.color.colorGraficaPunto1);

                colorDatoTexto1 = getResources().getColor(R.color.colorGraficaLinea3);
                colorDatoTexto2 = getResources().getColor(R.color.colorGraficaLinea1);

                yAxisMax1 = 1000;
                yAxisMax2 = 120;
                yAxisMin1 = 0;
                yAxisMin2 = 0;

                modo1 = 2;
                modo2 = 0;

                txtTituloGrafica1.setText(R.string.titulo_irradiancia_humedad);
                txtTituloGrafica2.setText(R.string.titulo_irradiancia_humedad);
                txtTituloGrafica3.setText(R.string.titulo_irradiancia_humedad);

                break;

            case 1:
                datoInfo1 = getResources().getString(R.string.dato3);
                datoInfo2 = getResources().getString(R.string.dato4);

                colorDato1 = getResources().getColor(R.color.colorGraficaPunto3);
                colorDato2 = getResources().getColor(R.color.colorGraficaPunto4);

                colorDatoTexto1 = getResources().getColor(R.color.colorGraficaLinea3);
                colorDatoTexto2 = getResources().getColor(R.color.colorGraficaLinea4);

                yAxisMax1 = 1000;
                yAxisMax2 = 2;
                yAxisMin1 = 0;
                yAxisMin2 = 0;

                modo1 = 2;
                modo2 = 3;

                txtTituloGrafica1.setText(R.string.titulo_irradiancia_corriente);
                txtTituloGrafica2.setText(R.string.titulo_irradiancia_corriente);
                txtTituloGrafica3.setText(R.string.titulo_irradiancia_corriente);

                break;

            case 2:

                datoInfo1 = getResources().getString(R.string.dato3);
                datoInfo2 = getResources().getString(R.string.dato5);

                colorDato1 = getResources().getColor(R.color.colorGraficaPunto3);
                colorDato2 = getResources().getColor(R.color.colorGraficaPunto5);

                colorDatoTexto1 = getResources().getColor(R.color.colorGraficaLinea3);
                colorDatoTexto2 = getResources().getColor(R.color.colorGraficaLinea5);

                yAxisMax1 = 1000;
                yAxisMax2 = 10;
                yAxisMin1 = 0;
                yAxisMin2 = 0;

                modo1 = 2;
                modo2 = 4;

                txtTituloGrafica1.setText(R.string.titulo_irradiancia_voltaje);
                txtTituloGrafica2.setText(R.string.titulo_irradiancia_voltaje);
                txtTituloGrafica3.setText(R.string.titulo_irradiancia_voltaje);


                break;

            case 3:
                datoInfo1 = getResources().getString(R.string.dato3);
                datoInfo2 = getResources().getString(R.string.dato2);

                colorDato1 = getResources().getColor(R.color.colorGraficaPunto3);
                colorDato2 = getResources().getColor(R.color.colorGraficaPunto2);

                colorDatoTexto1 = getResources().getColor(R.color.colorGraficaLinea3);
                colorDatoTexto2 = getResources().getColor(R.color.colorGraficaLinea2);

                yAxisMax1 = 1000;
                yAxisMax2 = 50;
                yAxisMin1 = 0;
                yAxisMin2 = 0;

                modo1 = 2;
                modo2 = 1;

                txtTituloGrafica1.setText(R.string.titulo_irradiancia_temperatura);
                txtTituloGrafica2.setText(R.string.titulo_irradiancia_temperatura);
                txtTituloGrafica3.setText(R.string.titulo_irradiancia_temperatura);

                break;
            case 4:
                datoInfo1 = getResources().getString(R.string.dato1);
                datoInfo2 = getResources().getString(R.string.dato2);

                colorDato1 = getResources().getColor(R.color.colorGraficaPunto1);
                colorDato2 = getResources().getColor(R.color.colorGraficaPunto2);

                colorDatoTexto1 = getResources().getColor(R.color.colorGraficaLinea1);
                colorDatoTexto2 = getResources().getColor(R.color.colorGraficaLinea2);

                yAxisMax1 = 120;
                yAxisMax2 = 50;
                yAxisMin1 = 0;
                yAxisMin2 = 0;

                modo1 = 0;
                modo2 = 1;

                txtTituloGrafica1.setText(R.string.titulo_humedad_temperatura);
                txtTituloGrafica2.setText(R.string.titulo_humedad_temperatura);
                txtTituloGrafica3.setText(R.string.titulo_humedad_temperatura);

                break;

        }

    }

    //Métod para inicializar las vistas, hacer seleccionables los botones poner en modo invisible algunas vistas.
    private void iniziliate() {
        TabHost tabHost = view.findViewById(R.id.tabHost);
        tabHost.setup();

        //Tab 1
        TabHost.TabSpec spec = tabHost.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator(createTabView(getContext(),getResources().getString(R.string.dia)));
        tabHost.addTab(spec);

        //Tab 2
        spec = tabHost.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator(createTabView(getContext(),getResources().getString(R.string.semana)));
        tabHost.addTab(spec);

        //Tab 3
        spec = tabHost.newTabSpec("Tab Three");
        spec.setContent(R.id.tab3);
        spec.setIndicator(createTabView(getContext(),getResources().getString(R.string.mes)));
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

        lineChart1.setVisibility(INVISIBLE);
        barChart1.setVisibility(INVISIBLE);
        barChart2.setVisibility(INVISIBLE);

        txtDate1 = view.findViewById(R.id.txtConsulta1);
        txtDate2 = view.findViewById(R.id.txtConsulta2);

        mSMes = view.findViewById(R.id.spinnerMes);
        nPAnio = view.findViewById(R.id.nPAnio);

        txtTituloGrafica1 = view.findViewById(R.id.txtTituloChart);
        txtTituloGrafica2 = view.findViewById(R.id.txtTituloChart1);
        txtTituloGrafica3 = view.findViewById(R.id.txtTituloChart2);

        pBConsultas = view.findViewById(R.id.pBConsultas);

        txtTituloGrafica1.setVisibility(INVISIBLE);
        txtTituloGrafica2.setVisibility(INVISIBLE);
        txtTituloGrafica3.setVisibility(INVISIBLE);


    }

    private static View createTabView(final Context context, final String text) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_bg, null);
        TextView tv = view.findViewById(R.id.tabsText);
        tv.setText(text);
        return view;
    }

    //Métod para ingresar valores al spinner de la vista de mes.
    public void inputDataToSpinner() {
        mSMes.setItems(Constants.MESES);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        nPAnio.setMinValue(Constants.MIN_YEAR);
        nPAnio.setMaxValue(Constants.MAX_YEAR);
        nPAnio.setValue(year);


    }

    //Método para obtener los valores del día seleccionado.
    private void getDataDayOFFireBase(int year, int month, int dayOfMonth) {
        final List<DatosCompletos>[] datosCompletos = new List[]{new ArrayList<>()};
        DatabaseReference datosDia = MenuPrincipal.reference.child("datos").child("y" + year).child("m" + month).child("d" + dayOfMonth);

        datosDia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<DatosCompletos>> t = new GenericTypeIndicator<ArrayList<DatosCompletos>>() {
                };
                try {
                    showChartDay(dataSnapshot.getValue(t));

                } catch (Exception ignore) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    //Método para mostrar el DatePicker del día.
    public void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), this, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }

    //Método para saber que fecha ha sido seleccionada,
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        switch (mode) {
            case 1:
                btnConsulta1.setEnabled(false);
                txtTituloGrafica1.setVisibility(INVISIBLE);
                pBConsultas.setVisibility(VISIBLE);
                int realMonth = month + 1;
                fechaATexto = dayOfMonth + "-" + realMonth + "-" + year;
                Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
                dateToQuery = calendar.getTime();
                txtDate1.setText(getString(R.string.fecha)+": "+fechaATexto);
                //dateDay = new GregorianCalendar(year,month,dayOfMonth).getTime();
                getDataDayOFFireBase(year, realMonth, dayOfMonth);
                break;
        }
    }

    //Método pra mostrar la gráfica de la consulta por dia.
    private void showChartDay(List<DatosCompletos> datosCompletosList) {
        List<Entry> entries = new ArrayList<>();
        List<Entry> entries1 = new ArrayList<>();
        lineChart1.clearAnimation();
        lineChart1.clear();
        dataSets.clear();
        DatosCompletos datosCompletos;
        labelsChart = new ArrayList<>();


        float dato1;
        if (datosCompletosList != null) {
            for (int i = 0; i < datosCompletosList.size(); i++) {
                datosCompletos = datosCompletosList.get(i);
                labelsChart.add(datosCompletos.getHora());
                try {
                    dato1 = Float.parseFloat(datosCompletos.getIrradiancia());

                } catch (Exception ignore) {
                    dato1 = 0;
                }

                try {
                    switch (modoGraficar) {
                        case 0:
                            entries.add(new Entry(i, dato1));
                            entries1.add(new Entry(i, Float.parseFloat(datosCompletos.getHumedad())));
                            break;
                        case 1:
                            entries.add(new Entry(i, dato1));
                            entries1.add(new Entry(i, Float.parseFloat(datosCompletos.getCorrientePanel())));
                            break;
                        case 2:

                            entries.add(new Entry(i, dato1));
                            entries1.add(new Entry(i, Float.parseFloat(datosCompletos.getVoltajePanel())));
                            break;

                        case 3:

                            entries.add(new Entry(i, dato1));
                            entries1.add(new Entry(i, Float.parseFloat(datosCompletos.getTemperatura())));
                            break;
                        case 4:

                            entries.add(new Entry(i, Float.parseFloat(datosCompletos.getHumedad())));
                            entries1.add(new Entry(i, Float.parseFloat(datosCompletos.getTemperatura())));
                            break;
                    }


                } catch (Exception ignored) {

                }

            }
        } else {
            Toast.makeText(getContext(), getResources().getString(R.string.no_hay_datos), Toast.LENGTH_SHORT).show();
        }

        //Log.e("Datos",entries.toString()+"\n"+labelsChart.toString());

        if (entries.size() != 0) {
            txtTituloGrafica1.setVisibility(VISIBLE);
            LineDataSet lineDataSet = new LineDataSet(entries, datoInfo1);
            LineDataSet lineDataSet1 = new LineDataSet(entries1, datoInfo2);

            lineDataSet.setColor(colorDato1);
            lineDataSet1.setColor(colorDato2);

            lineDataSet.setValueTextColor(colorDatoTexto1);
            lineDataSet1.setValueTextColor(colorDatoTexto2);

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
            description.setText("");
            xAxis = lineChart1.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsChart));
            xAxis.setLabelRotationAngle(-10f);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            lineChart1.setDescription(description);
            lineChart1.setDrawMarkers(true);
            CustomMarkerViewData2 customMarkerView = new CustomMarkerViewData2(getContext(), R.layout.item_custom_marker, labelsChart, datoInfo1, datoInfo2, colorDato1, colorDato2);
            customMarkerView.setSizeList(labelsChart.size());
            lineChart1.setMarker(customMarkerView);
            lineChart1.setTouchEnabled(true);
            lineChart1.setVisibility(VISIBLE);
            btnConsulta1.setEnabled(true);
            lineChart1.invalidate();


        } else {
            Toast.makeText(getContext(), R.string.no_hay_datos, Toast.LENGTH_SHORT).show();
        }
        btnConsulta1.setEnabled(true);
        pBConsultas.setVisibility(View.INVISIBLE);

    }


    //Método para mostar el DatePicker de la consulta por semana.
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
                btnConsulta2.setEnabled(false);
                pBConsultas.setVisibility(VISIBLE);
                int mes = datePicker.getMonth()+1;
                txtTituloGrafica2.setVisibility(INVISIBLE);
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

                txtDate2.setText(getString(R.string.fecha)+": "+format.format(primerDia)+" "+getResources().getString(R.string.a)+" "+format.format(ultimoDia));

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

    //Método para saber cual es la semana seleccionada por el dia obtenido por el DatePicker de la semana
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

    //Método para obtención de los datos de la semana
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

    //Método para graficar los datos de la semana
    private void showChartWeek() {
        entriesBarWeek = new ArrayList<>();
        entriesBarWeek1 = new ArrayList<>();
        barChart1.clearAnimation();
        barChart1.clear();


        for (int i=0; i<7;i++){
            entriesBarWeek.add(new BarEntry(i,promedioDia(datosCompletosSemana[i],modo1)));
            entriesBarWeek1.add(new BarEntry(i,promedioDia(datosCompletosSemana[i],modo2)));
        }


        if(entriesBarWeek1.size()>0){
            txtTituloGrafica2.setVisibility(VISIBLE);
        }


        BarDataSet barDataSet = new BarDataSet(entriesBarWeek,datoInfo1);
        BarDataSet barDataSet1 = new BarDataSet(entriesBarWeek1,datoInfo2);
        barDataSet.setColor(colorDato1);
        barDataSet1.setColor(colorDato2);
        barDataSet.setBarShadowColor(colorDatoTexto1);
        barDataSet1.setBarShadowColor(colorDatoTexto2);
        final DecimalFormat decimalFormat = new DecimalFormat("####.##");
        barDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return decimalFormat.format(value);
            }
        });

        barDataSet1.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return decimalFormat.format(value);
            }
        });

        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        barDataSet1.setAxisDependency(YAxis.AxisDependency.RIGHT);

        List<IBarDataSet> dataBarSets = new ArrayList<>();
        dataBarSets.add(barDataSet);
        dataBarSets.add(barDataSet1);
        BarData data = new BarData(barDataSet,barDataSet1);
        Description description = new Description();
        description.setText(" ");
        data.setBarWidth(0.48f); // set custom bar width
        barChart1.setData(data);
        barChart1.groupBars(1, 0.04f, 0f);
        xAxis = barChart1.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(Constants.DIAS_DE_LA_SEMANA));
        xAxis.setCenterAxisLabels(true);
        xAxis.setLabelRotationAngle(-5f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum(7);



        YAxis yAxisLeft = barChart1.getAxisLeft();
        YAxis yAxisRight = barChart1.getAxisRight();
        yAxisLeft.setAxisMaximum(yAxisMax1);
        yAxisLeft.setAxisMinimum(yAxisMin1);
        yAxisRight.setAxisMaximum(yAxisMax2);
        yAxisRight.setAxisMinimum(yAxisMin2);
        barChart1.setVisibility(VISIBLE);
        barChart1.setDescription(description);
        pBConsultas.setVisibility(INVISIBLE);
        btnConsulta2.setEnabled(true);
        barChart1.invalidate(); // refresh


    }

    //Método para obtener el número de días de mes seleccionado en el MaterialSpinner
    private void getDataMonth() {
        pBConsultas.setVisibility(VISIBLE);
        barChart2.setVisibility(INVISIBLE);
        yearM = nPAnio.getValue();
        month = mSMes.getSelectedIndex();
        switch(month){
            case 0:
            case 2:
            case 6:
            case 7:
            case 9:
            case 11:
                numDias=31;
                break;
            case 3:
            case 5:
            case 8:
            case 10:
                numDias=30;
                break;
            case 1:
                if ( ((yearM%100 == 0) && (yearM%400 == 0)) ||
                        ((yearM%100 != 0) && (yearM%  4 == 0))   )
                    numDias=29;
                else
                    numDias=28;
            default:
        }

        int realMonth= month+1;
        datosCompletosMes = new List[numDias];


        for (int i=0; i<numDias;i++){
            getDataDayOFFireBaseDay(yearM,realMonth,i);

        }

    }

    //Método para la obtención de datos del mes por día
    private void getDataDayOFFireBaseDay(int yearM, int realMonth, final int i) {
        final int dias = i+1;
        DatabaseReference datosDia = MenuPrincipal.reference.child("datos").child("y"+yearM).child("m"+realMonth).child("d"+dias);
        datosDia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<DatosCompletos>> t = new GenericTypeIndicator<ArrayList<DatosCompletos>>() {};
                try {

                    datosCompletosMes[i] = dataSnapshot.getValue(t);
                }catch (Exception ignored){

                }
                if (i==numDias-1){
                    try {
                        showChartMonth();

                    }catch (Exception e){
                        Log.e("Error Grafica",e.getMessage());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "No hay conexión a internet", Toast.LENGTH_SHORT).show();
            }
        });


    }

    //Método para graficar los datos del mes.
    public void showChartMonth(){
        List<BarEntry> entry1 = new ArrayList<>();
        List<BarEntry> entry2 = new ArrayList<>();
        barChart2.clearAnimation();
        barChart2.clear();
        List<String> labelC = new ArrayList<>();
        XAxis xAxis1;
        labelC.add(" ");
        for (int i=0; i<datosCompletosMes.length;i++){
            entry1.add(new BarEntry(i+1,promedioDia(datosCompletosMes[i],modo1)));
            entry2.add(new BarEntry(i+1,promedioDia(datosCompletosMes[i],modo2)));
            labelC.add(Integer.toString(i+1));
        }

        if (entry1.size()!=0) {

            txtTituloGrafica3.setVisibility(VISIBLE);

            BarDataSet barDataSet = new BarDataSet(entry1,datoInfo1);
            BarDataSet barDataSet1 = new BarDataSet(entry2,datoInfo2);

            barDataSet.setColor(colorDato1);
            barDataSet1.setColor(colorDato2);

            barDataSet.setDrawValues(false);
            barDataSet1.setDrawValues(false);

            barDataSet.setValueTextColor(colorDatoTexto1);
            barDataSet1.setValueTextColor(colorDatoTexto2);


            barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            barDataSet1.setAxisDependency(YAxis.AxisDependency.RIGHT);

            List<IBarDataSet> dataBarSets = new ArrayList<>();
            dataBarSets.add(barDataSet);
            dataBarSets.add(barDataSet1);
            BarData data = new BarData(barDataSet,barDataSet1);
            data.setBarWidth(0.48f); // set custom bar width
            barChart2.setData(data);
            Description description = new Description();
            description.setText(" ");
            xAxis1 = barChart2.getXAxis();
            xAxis1.setCenterAxisLabels(true);
            xAxis1.setLabelRotationAngle(-10f);
            xAxis1.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis1.setValueFormatter(new IndexAxisValueFormatter(labelC));
            xAxis1.setAxisMaximum(datosCompletosMes.length);
            xAxis1.setLabelCount(2,true);

            YAxis yAxisLeft = barChart2.getAxisLeft();
            YAxis yAxisRight = barChart2.getAxisRight();
            yAxisLeft.setAxisMaximum(yAxisMax1);
            yAxisLeft.setAxisMinimum(yAxisMin1);
            yAxisRight.setAxisMaximum(yAxisMax2);
            yAxisRight.setAxisMinimum(yAxisMin2);
            yAxisRight.setAxisMinimum(yAxisMin2);

            barChart2.setDescription(description);
            barChart2.groupBars(1, 0.04f, 0f);
            barChart2.setTouchEnabled(true);
            barChart2.setVisibility(VISIBLE);
            barChart2.setMarker(new CustomMarkerViewDataMonth(getContext(),R.layout.item_custom_marker,labelC,datoInfo1,datoInfo2,colorDato1,colorDato2));
            barChart2.highlightValue(null);
            barChart2.invalidate();


        }else {
            Toast.makeText(getContext(), R.string.no_hay_datos, Toast.LENGTH_SHORT).show();
        }
        btnConsulta3.setEnabled(true);
        pBConsultas.setVisibility(View.INVISIBLE);


    }

    //Método para promediar los datos del diá.
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

            case 2:

                try {
                    for (int i=0;i<datosFiltrado.size();i++){
                        try {
                            acumulador+=Float.parseFloat(datosFiltrado.get(i).getIrradiancia());
                        }catch (Exception ignore){

                        }
                    }

                }catch (Exception ignore){

                }

                break;

            case 3:

                try {
                    for (int i=0;i<datosFiltrado.size();i++){
                        try {
                            acumulador+=Float.parseFloat(datosFiltrado.get(i).getCorrientePanel());
                        }catch (Exception ignore){

                        }
                    }

                }catch (Exception ignore){

                }

                break;


            case 4:

                try {
                    for (int i=0;i<datosFiltrado.size();i++){
                        try {
                            acumulador+=Float.parseFloat(datosFiltrado.get(i).getVoltajePanel());
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

    //Método para saber cual es el botón que está siendo seleccionado
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
                txtTituloGrafica3.setVisibility(INVISIBLE);
                mode=3;
                getDataMonth();
                btnConsulta3.setEnabled(false);
                break;
        }
    }


}
