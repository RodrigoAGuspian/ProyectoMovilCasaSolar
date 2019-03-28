package com.casasolarctpi.appsolar.models;

import android.annotation.SuppressLint;
import android.content.Context;

import com.casasolarctpi.appsolar.R;
import com.casasolarctpi.appsolar.controllers.MenuPrincipal;
import com.casasolarctpi.appsolar.controllers.Splash;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    @SuppressLint("StaticFieldLeak")

    //Cosntante para el consumo de servicios web
    public static  final String BASE_URL="https://innovatec-server.herokuapp.com/";
    //Declaración de constantes
    public static final String [] GROUP_LIST = {Splash.context.getString(R.string.paginas),Splash.context.getString(R.string.conocenos)};
    public static final String [] PAGES_LIST = {"Sena","Sena Regional", "CTPI Cauca", "Sennova",};
    public static final String [] CONOCENOS_LIST = {Splash.context.getString(R.string.contactanos),Splash.context.getString(R.string.acerca_de),};
    //Vector el cual se encarga de la lista de las paginas y conocenos
    public static final String [] [] CHILDREN_LISTS = {PAGES_LIST,CONOCENOS_LIST};
    //Lista de los links de contáctanos
    public static final String [] LIST_LINKS_CONOCENOS={"http://www.sena.edu.co/es-co/Paginas/default.aspx","http://www.sena.edu.co/es-co/Paginas/default.aspx","https://ctpisenacauca.blogspot.com/","http://www.sena.edu.co/es-co/formacion/Paginas/tecnologia-innovacion.aspx"};

    //Lista de consultas
    public static final String [] LIST_QUERY={Splash.context.getResources().getString(R.string.irradiancia_humedad),
            Splash.context.getResources().getString(R.string.irradiancia_corriente),
            Splash.context.getResources().getString(R.string.irradiancia_voltaje),
            Splash.context.getResources().getString(R.string.irradiancia_temperatura),
            Splash.context.getResources().getString(R.string.humedad_temperatura)};

    //Lista de mese del año
    public static final String [] MESES={Splash.context.getString(R.string.enero),Splash.context.getString(R.string.febrero),Splash.context.getString(R.string.marzo),Splash.context.getString(R.string.abril),Splash.context.getString(R.string.mayo),
            Splash.context.getString(R.string.junio),Splash.context.getString(R.string.julio),Splash.context.getString(R.string.agosto),Splash.context.getString(R.string.septiembre),Splash.context.getString(R.string.octubre),Splash.context.getString(R.string.noviembre),Splash.context.getString(R.string.diciembre)};

    //Constante para año máximo y mínimo de la consulta
    public static final int MIN_YEAR = 2018;
    public static final int MAX_YEAR = 2099;

    //Días de la semana
    public static final String [] DIAS_DE_LA_SEMANA = {Splash.context.getString(R.string.domingo),Splash.context.getString(R.string.lunes),Splash.context.getString(R.string.martes),Splash.context.getString(R.string.miercoles), Splash.context.getString(R.string.jueves),Splash.context.getString(R.string.viernes),Splash.context.getString(R.string.sabado)};



}

