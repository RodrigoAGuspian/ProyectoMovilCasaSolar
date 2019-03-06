package com.casasolarctpi.appsolar.models;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RestClient {
    @GET("api/chart")
    Call<ChartValues> getChartValues();
}
