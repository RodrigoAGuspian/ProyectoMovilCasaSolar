package com.casasolarctpi.appsolar.models;

public class UserData {
    private String email,primerN,segundoN,primerA,segundoA,institucion,pais,departamento,ciudad,tipoDeUso;

    public UserData() {
    }

    public UserData(String email, String primerN, String segundoN, String primerA, String segundoA, String institucion, String pais, String departamento, String ciudad, String tipoDeUso) {
        this.email = email;
        this.primerN = primerN;
        this.segundoN = segundoN;
        this.primerA = primerA;
        this.segundoA = segundoA;
        this.institucion = institucion;
        this.pais = pais;
        this.departamento = departamento;
        this.ciudad = ciudad;
        this.tipoDeUso = tipoDeUso;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPrimerN() {
        return primerN;
    }

    public void setPrimerN(String primerN) {
        this.primerN = primerN;
    }

    public String getSegundoN() {
        return segundoN;
    }

    public void setSegundoN(String segundoN) {
        this.segundoN = segundoN;
    }

    public String getPrimerA() {
        return primerA;
    }

    public void setPrimerA(String primerA) {
        this.primerA = primerA;
    }

    public String getSegundoA() {
        return segundoA;
    }

    public void setSegundoA(String segundoA) {
        this.segundoA = segundoA;
    }

    public String getInstitucion() {
        return institucion;
    }

    public void setInstitucion(String institucion) {
        this.institucion = institucion;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getTipoDeUso() {
        return tipoDeUso;
    }

    public void setTipoDeUso(String tipoDeUso) {
        this.tipoDeUso = tipoDeUso;
    }
}
