package com.casasolarctpi.appsolar.models;

public class DatosPromedio {
    private float hora = 0 , temperaturaPromedio = 0, humedadPromedio = 0, corrientePromedio = 0, irradianciaPromedio = 0, voltajePromedio = 0;

    public DatosPromedio() {
    }

    public float getHora() {
        return hora;
    }

    public void setHora(float hora) {
        this.hora = hora;
    }

    public float getTemperaturaPromedio() {
        return temperaturaPromedio;
    }

    public void setTemperaturaPromedio(float temperaturaPromedio) {
        this.temperaturaPromedio = temperaturaPromedio;
    }

    public float getHumedadPromedio() {
        return humedadPromedio;
    }

    public void setHumedadPromedio(float humedadPromedio) {
        this.humedadPromedio = humedadPromedio;
    }

    public float getCorrientePromedio() {
        return corrientePromedio;
    }

    public void setCorrientePromedio(float corrientePromedio) {
        this.corrientePromedio = corrientePromedio;
    }

    public float getIrradianciaPromedio() {
        return irradianciaPromedio;
    }

    public void setIrradianciaPromedio(float irradianciaPromedio) {
        this.irradianciaPromedio = irradianciaPromedio;
    }

    public float getVoltajePromedio() {
        return voltajePromedio;
    }

    public void setVoltajePromedio(float voltajePromedio) {
        this.voltajePromedio = voltajePromedio;
    }
}
