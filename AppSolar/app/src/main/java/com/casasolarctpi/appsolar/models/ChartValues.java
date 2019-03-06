package com.casasolarctpi.appsolar.models;

import java.util.ArrayList;
import java.util.List;

public class ChartValues {
    private String _id;
    private String nombre;
    private List<DataValues> valores = new ArrayList<>();


    public ChartValues() {
    }

    public ChartValues(String _id, String nombre, List<DataValues> valores) {
        this._id = _id;
        this.nombre = nombre;
        this.valores = valores;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<DataValues> getValores() {
        return valores;
    }

    public void setValores(List<DataValues> valores) {
        this.valores = valores;
    }
}
