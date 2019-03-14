package com.casasolarctpi.appsolar.models;

public class DatosTH {
    private String fecha_dato,hora,humedad,temperatura;

    public DatosTH() {
    }

    public DatosTH(String fecha_dato, String hora, String humedad, String temperatura) {
        this.fecha_dato = fecha_dato;
        this.hora = hora;
        this.humedad = humedad;
        this.temperatura = temperatura;
    }

    public String getFecha_dato() {
        return fecha_dato;
    }

    public void setFecha_dato(String fecha_dato) {
        this.fecha_dato = fecha_dato;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getHumedad() {
        return humedad;
    }

    public void setHumedad(String humedad) {
        this.humedad = humedad;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }
}
