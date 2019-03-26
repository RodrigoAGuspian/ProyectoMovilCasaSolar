package com.casasolarctpi.appsolar.models;

import android.content.Context;
import android.view.SubMenu;

import com.casasolarctpi.appsolar.R;
import com.casasolarctpi.appsolar.controllers.Splash;

import java.util.ArrayList;
import java.util.List;

public class ListMenu {
    private List<ItemMenu> itemMenus = new ArrayList<>();

    public ListMenu() {
    }

    public List<ItemMenu> getItemMenus() {
        return itemMenus;
    }

    public void setItemMenus(List<ItemMenu> itemMenus) {
        this.itemMenus = itemMenus;
    }

    public void inputList(){
        List<ItemSubMenu> itemsConsultasList = new ArrayList<>();
        List<ItemSubMenu> itemsConocenosList = new ArrayList<>();
        List<ItemSubMenu> itemsPaginasList = new ArrayList<>();

        itemsConsultasList.add(new ItemSubMenu(Splash.context.getResources().getString(R.string.irradiancia_humedad)));
        itemsConsultasList.add(new ItemSubMenu(Splash.context.getResources().getString(R.string.irradiancia_corriente)));
        itemsConsultasList.add(new ItemSubMenu(Splash.context.getResources().getString(R.string.irradiancia_voltaje)));
        itemsConsultasList.add(new ItemSubMenu(Splash.context.getResources().getString(R.string.irradiancia_temperatura)));
        itemsConsultasList.add(new ItemSubMenu(Splash.context.getResources().getString(R.string.humedad_temperatura)));

        itemsConocenosList.add(new ItemSubMenu(Splash.context.getResources().getString(R.string.contactanos)));
        itemsConocenosList.add(new ItemSubMenu(Splash.context.getResources().getString(R.string.acerca_de)));

        itemsPaginasList.add(new ItemSubMenu("Sena"));
        itemsPaginasList.add(new ItemSubMenu("Sena Regional"));
        itemsPaginasList.add(new ItemSubMenu("CTPI Cauca"));
        itemsPaginasList.add(new ItemSubMenu("Sennova"));

        itemMenus.add(new ItemMenu(Splash.context.getResources().getString(R.string.inicio),null));
        itemMenus.add(new ItemMenu(Splash.context.getResources().getString(R.string.dato1),null));
        itemMenus.add(new ItemMenu(Splash.context.getResources().getString(R.string.dato2),null));
        itemMenus.add(new ItemMenu(Splash.context.getResources().getString(R.string.dato3),null));
        itemMenus.add(new ItemMenu(Splash.context.getResources().getString(R.string.perfil),null));
        itemMenus.add(new ItemMenu(Splash.context.getResources().getString(R.string.consultas),itemsConsultasList));
        itemMenus.add(new ItemMenu(Splash.context.getResources().getString(R.string.conocenos),itemsConocenosList));
        itemMenus.add(new ItemMenu(Splash.context.getResources().getString(R.string.paginas),itemsPaginasList));
        itemMenus.add(new ItemMenu(Splash.context.getResources().getString(R.string.cerrar_sesion),null));

    }
}
