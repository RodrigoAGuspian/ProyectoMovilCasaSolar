package com.casasolarctpi.appsolar.models;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    //Declaración de constantes
    public static final String [] GROUP_LIST = {"Páginas","Conócenos",};
    public static final String [] PAGES_LIST = {"Sena","Sena Regional", "CTPI Cauca", "Sennova",};
    public static final String [] CONOCENOS_LIST = {"Contáctanos","Acerca de",};
    //Vector el cual se encarga de la lista de las paginas y conocenos
    public static final String [] [] CHILDREN_LISTS = {PAGES_LIST,CONOCENOS_LIST};
    //Lista de los links de contáctanos
    public static final String [] LIST_LINKS_CONOCENOS={"http://www.sena.edu.co/es-co/Paginas/default.aspx","http://www.sena.edu.co/es-co/Paginas/default.aspx","https://ctpisenacauca.blogspot.com/","http://www.sena.edu.co/es-co/formacion/Paginas/tecnologia-innovacion.aspx"};

}

