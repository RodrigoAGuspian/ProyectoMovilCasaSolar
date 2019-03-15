package com.casasolarctpi.appsolar.models;

import android.annotation.SuppressLint;
import android.content.Context;

import com.casasolarctpi.appsolar.R;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static void setContext(Context context) {
        Constants.context = context;
    }

    //Cosntante para el consumo de servicios web
    public static  final String BASE_URL="https://innovatec-server.herokuapp.com/";
    //Declaración de constantes
    public static final String [] GROUP_LIST = {context.getString(R.string.paginas),context.getString(R.string.conocenos),};
    public static final String [] PAGES_LIST = {"Sena","Sena Regional", "CTPI Cauca", "Sennova",};
    public static final String [] CONOCENOS_LIST = {context.getString(R.string.contactanos),context.getString(R.string.acerca_de),};
    //Vector el cual se encarga de la lista de las paginas y conocenos
    public static final String [] [] CHILDREN_LISTS = {PAGES_LIST,CONOCENOS_LIST};
    //Lista de los links de contáctanos
    public static final String [] LIST_LINKS_CONOCENOS={"http://www.sena.edu.co/es-co/Paginas/default.aspx","http://www.sena.edu.co/es-co/Paginas/default.aspx","https://ctpisenacauca.blogspot.com/","http://www.sena.edu.co/es-co/formacion/Paginas/tecnologia-innovacion.aspx"};

    //Lista de mese del año
    public static final String [] MESES={context.getString(R.string.enero),context.getString(R.string.febrero),context.getString(R.string.marzo),context.getString(R.string.abril),context.getString(R.string.mayo),
            context.getString(R.string.junio),context.getString(R.string.julio),context.getString(R.string.agosto),context.getString(R.string.septiembre),context.getString(R.string.octubre),context.getString(R.string.noviembre),context.getString(R.string.diciembre)};

    //Constante para año máximo y mínimo de la consulta
    public static final int MIN_YEAR = 2018;
    public static final int MAX_YEAR = 2099;

}

