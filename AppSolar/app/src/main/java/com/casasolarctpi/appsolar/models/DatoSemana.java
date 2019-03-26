package com.casasolarctpi.appsolar.models;

import java.util.List;

public class DatoSemana {
    private int dia;
    private List<DatosCompletos> datosCompletos;

    public DatoSemana() {
    }

    public DatoSemana(int dia, List<DatosCompletos> datosCompletos) {
        this.dia = dia;
        this.datosCompletos = datosCompletos;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public List<DatosCompletos> getDatosCompletos() {
        return datosCompletos;
    }

    public void setDatosCompletos(List<DatosCompletos> datosCompletos) {
        this.datosCompletos = datosCompletos;
    }
}
