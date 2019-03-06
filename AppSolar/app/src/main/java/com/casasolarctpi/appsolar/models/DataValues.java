package com.casasolarctpi.appsolar.models;

public class DataValues {
    private String fecha_hora;
    private float voltaje;
    private float irradiancia;

    public DataValues() {
    }

    public DataValues(String fecha_hora, float voltaje, float irradiancia) {
        this.fecha_hora = fecha_hora;
        this.voltaje = voltaje;
        this.irradiancia = irradiancia;
    }

    public String getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(String fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    public float getVoltaje() {
        return voltaje;
    }

    public void setVoltaje(float voltaje) {
        this.voltaje = voltaje;
    }

    public float getIrradiancia() {
        return irradiancia;
    }

    public void setIrradiancia(float irradiancia) {
        this.irradiancia = irradiancia;
    }
}
