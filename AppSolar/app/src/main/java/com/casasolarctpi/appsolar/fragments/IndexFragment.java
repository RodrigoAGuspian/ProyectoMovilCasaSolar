package com.casasolarctpi.appsolar.fragments;


import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.casasolarctpi.appsolar.R;
import com.casasolarctpi.appsolar.controllers.MenuPrincipal;
import com.casasolarctpi.appsolar.models.ChartValues;
import com.casasolarctpi.appsolar.models.Constants;
import com.casasolarctpi.appsolar.models.CustomMarkerView;
import com.casasolarctpi.appsolar.models.DatosTH;
import com.casasolarctpi.appsolar.models.RestClient;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    private ChartValues chartValues = new ChartValues();
    private List<ILineDataSet> dataSets = new ArrayList<>();
    private XAxis xAxis;
    public boolean bandera = false;
    List<DatosTH> datosTHList = new ArrayList<>();

    public static List<String> labelsChart = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FirebaseApp.initializeApp(Objects.requireNonNull(getContext()));
        view =inflater.inflate(R.layout.fragment_index, container, false);
        iniziliate();
        haveScroll();
        inputDataFirebase();
        return view;
    }

    //Método para crear ejecto scroll al texto principal
    private void haveScroll() {
        txtInfoPrincipal.setMovementMethod(new ScrollingMovementMethod());
    }

    //inicializacion de vistas
    private void iniziliate() {
        lineChart = view.findViewById(R.id.chart);
        txtInfoPrincipal = view.findViewById(R.id.txtInfoPrincipal);
    }


    //Método para cargar los datos del servicio web con RetroFit
    public void loadData(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().detectAll().build();
        StrictMode.setThreadPolicy(policy);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        RestClient restClient = retrofit.create(RestClient.class);
        Call<ChartValues> call = restClient.getChartValues();
        call.enqueue(new Callback<ChartValues>() {
            @Override
            public void onResponse(Call<ChartValues> call, Response<ChartValues> response) {
                Log.e("Datos",response.toString());
                switch (response.code()){
                    case 200:
                        chartValues= response.body();

                        Log.e("Datos",response.message());
                        break;
                    case 404:
                        Snackbar.make(view, R.string.no_se_encuentra_los_servicios,Snackbar.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ChartValues> call, Throwable t) {
                Log.e("Error",t.getMessage());
                Snackbar.make(view, R.string.error_de_red,Snackbar.LENGTH_SHORT).show();
            }
        });


    }


    //Método para el ingreso de datos desde de la base de datos  (Real time DataBase)de Firebase
    public void inputDataFirebase(){
        DatabaseReference datos = MenuPrincipal.reference.child("datos");
        //Query para limitar los datos
        Query ultimosDatos = datos.limitToLast(20);
        ultimosDatos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //GenericTypeIndicator<ArrayList<DatosTH>> t = new GenericTypeIndicator<ArrayList<DatosTH>>(){};
                datosTHList.clear();
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    datosTHList.add(child.getValue(DatosTH.class));
                }

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
    public void inputValuesChart() {
        for (int i = 0; i < datosTHList.size(); i++) {
            labelsChart.add(datosTHList.get(i).getHora());
            float dato1=0;
            float dato2=0;
            try {
                dato1=Float.parseFloat(datosTHList.get(i).getHumedad());
                dato2=Float.parseFloat(datosTHList.get(i).getTemperatura());

            }catch (Exception ignore){

            }
            entries.add(new Entry(i, dato1));
            entries1.add(new Entry(i, dato2));
        }

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
        lineChart.setData(data);
        Description description = new Description();
        description.setText("Fecha de los datos tomados: "+datosTHList.get(0).getFecha_dato());
        xAxis = lineChart.getXAxis();

        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsChart));
        xAxis.setLabelRotationAngle(-10f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.setDescription(description);
        lineChart.setDrawMarkers(true);
        CustomMarkerView customMarkerView = new CustomMarkerView(getContext(), R.layout.item_custom_marker);
        lineChart.setMarker(customMarkerView);
        lineChart.setTouchEnabled(true);
        //lineChart.invalidate();

    }


    //Método para el ingreso datos a la gráfica a tiempo real
    private void inputValuesRealTime() {
        entries.clear();
        entries1.clear();
        labelsChart.clear();
        for (int i = 0; i < datosTHList.size(); i++) {
            labelsChart.add(datosTHList.get(i).getHora());
            float dato1=0;
            float dato2=0;
            try {
                dato1=Float.parseFloat(datosTHList.get(i).getHumedad());
                dato2=Float.parseFloat(datosTHList.get(i).getTemperatura());

            }catch (Exception ignore){

            }
            entries.add(new Entry(i, dato1));
            entries1.add(new Entry(i, dato2));
            lineChart.notifyDataSetChanged();
            lineChart.invalidate();

        }
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsChart));

    }

}
