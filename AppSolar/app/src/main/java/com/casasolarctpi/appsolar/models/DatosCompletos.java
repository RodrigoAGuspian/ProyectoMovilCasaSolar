package com.casasolarctpi.appsolar.models;

public class DatosCompletos {
    private String hora ,temperatura ,humedad, corrienteBateria, corrienteCarga, corrientePanel, irradiancia, voltajeBateria, voltajeCarga, voltajePanel;

    public DatosCompletos() {
    }


    public DatosCompletos(String hora, String temperatura, String humedad, String corrienteBateria, String corrienteCarga, String corrientePanel, String irradiancia, String voltajeBateria, String voltajeCarga, String voltajePanel) {
        this.hora = hora;
        this.temperatura = temperatura;
        this.humedad = humedad;
        this.corrienteBateria = corrienteBateria;
        this.corrienteCarga = corrienteCarga;
        this.corrientePanel = corrientePanel;
        this.irradiancia = irradiancia;
        this.voltajeBateria = voltajeBateria;
        this.voltajeCarga = voltajeCarga;
        this.voltajePanel = voltajePanel;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    public String getHumedad() {
        return humedad;
    }

    public void setHumedad(String humedad) {
        this.humedad = humedad;
    }

    public String getCorrienteBateria() {
        return corrienteBateria;
    }

    public void setCorrienteBateria(String corrienteBateria) {
        this.corrienteBateria = corrienteBateria;
    }

    public String getCorrienteCarga() {
        return corrienteCarga;
    }

    public void setCorrienteCarga(String corrienteCarga) {
        this.corrienteCarga = corrienteCarga;
    }

    public String getCorrientePanel() {
        return corrientePanel;
    }

    public void setCorrientePanel(String corrientePanel) {
        this.corrientePanel = corrientePanel;
    }

    public String getIrradiancia() {
        return irradiancia;
    }

    public void setIrradiancia(String irradiancia) {
        this.irradiancia = irradiancia;
    }

    public String getVoltajeBateria() {
        return voltajeBateria;
    }

    public void setVoltajeBateria(String voltajeBateria) {
        this.voltajeBateria = voltajeBateria;
    }

    public String getVoltajeCarga() {
        return voltajeCarga;
    }

    public void setVoltajeCarga(String voltajeCarga) {
        this.voltajeCarga = voltajeCarga;
    }

    public String getVoltajePanel() {
        return voltajePanel;
    }

    public void setVoltajePanel(String voltajePanel) {
        this.voltajePanel = voltajePanel;
    }

}
