package com.casasolarctpi.appsolar.models;

public class DatoSemana {
    private int dia;
    private DatosTH datosTH;

    public DatoSemana(int dia, DatosTH datosTH) {
        this.dia = dia;
        this.datosTH = datosTH;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public DatosTH getDatosTH() {
        return datosTH;
    }

    public void setDatosTH(DatosTH datosTH) {
        this.datosTH = datosTH;
    }
}
