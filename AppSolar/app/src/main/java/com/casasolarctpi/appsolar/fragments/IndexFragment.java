package com.casasolarctpi.appsolar.fragments;


import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.casasolarctpi.appsolar.R;
import com.casasolarctpi.appsolar.models.ChartValues;
import com.casasolarctpi.appsolar.models.Constants;
import com.casasolarctpi.appsolar.models.CustomMarkerView;
import com.casasolarctpi.appsolar.models.DataValues;
import com.casasolarctpi.appsolar.models.RestClient;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    TextView txtInfoPrincipal;
    LineChart lineChart;
    View view;
    List<Entry> entries = new ArrayList<>();
    List<Entry> entries1 = new ArrayList<>();
    List<ILineDataSet> dataSets = new ArrayList<>();
    public static List<String> labelsChart = new ArrayList<>();
    public static ChartValues chartValues = new ChartValues();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_index, container, false);
        iniziliate();
        haveScroll();
        loadData();
        return view;
    }

    private void haveScroll() {
        txtInfoPrincipal.setMovementMethod(new ScrollingMovementMethod());
    }

    private void iniziliate() {
        lineChart = view.findViewById(R.id.chart);
        txtInfoPrincipal = view.findViewById(R.id.txtInfoPrincipal);
    }


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
                        inputValues();
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

    public void inputValues() {
        final List<DataValues> valores = chartValues.getValores();
        Date date = new Date();
        DateFormat dateFormatView = new SimpleDateFormat("hh:mm:ss a");
        DateFormat dateFormatToTransform = new SimpleDateFormat("HH:mm:ss");


        for (int i = 0; i < valores.size(); i++) {
            try {
                date = dateFormatToTransform.parse(valores.get(i).getFecha_hora().substring(10,19));

            } catch (ParseException e) {
                e.printStackTrace();
            }

            labelsChart.add(dateFormatView.format(date));
            entries.add(new Entry(i, valores.get(i).getVoltaje()));
            entries1.add(new Entry(i, valores.get(i).getIrradiancia()));
        }

        LineDataSet lineDataSet = new LineDataSet(entries, "Voltaje");
        LineDataSet lineDataSet1 = new LineDataSet(entries1, "Irradiancia");

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
        description.setText("Fecha de los datos tomados: "+valores.get(0).getFecha_hora().substring(0,10));
        XAxis xAxis = lineChart.getXAxis();

        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsChart));
        xAxis.setLabelRotationAngle(-10f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.setDescription(description);
        lineChart.setDrawMarkers(true);
        CustomMarkerView customMarkerView = new CustomMarkerView(getContext(), R.layout.item_custom_marker);
        lineChart.setMarker(customMarkerView);
        lineChart.setTouchEnabled(true);
        lineChart.invalidate();

    }

}
