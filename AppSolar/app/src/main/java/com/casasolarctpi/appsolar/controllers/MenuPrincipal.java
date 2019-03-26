package com.casasolarctpi.appsolar.controllers;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.casasolarctpi.appsolar.R;
import com.casasolarctpi.appsolar.fragments.AcercaDeFragment;
import com.casasolarctpi.appsolar.fragments.ConsultasFragment;
import com.casasolarctpi.appsolar.fragments.ContactanosFragment;
import com.casasolarctpi.appsolar.fragments.HumedadFragment;
import com.casasolarctpi.appsolar.fragments.IndexFragment;
import com.casasolarctpi.appsolar.fragments.IrradianciaFragment;
import com.casasolarctpi.appsolar.fragments.PerfilFragment;
import com.casasolarctpi.appsolar.fragments.TemperaturaFragment;
import com.casasolarctpi.appsolar.models.Constants;
import com.casasolarctpi.appsolar.models.ExpandableListAdapter;
import com.casasolarctpi.appsolar.models.ItemMenu;
import com.casasolarctpi.appsolar.models.ItemSubMenu;
import com.casasolarctpi.appsolar.models.ListMenu;
import com.casasolarctpi.appsolar.models.MenuEAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thoughtbot.expandablerecyclerview.listeners.OnGroupClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Declaración de variables
    private ExpandableListView expListView;
    private ExpandableListAdapter listAdapterExpandable;
    private String[] listDataHeader;
    private HashMap<String, String []> listDataChild;
    private String [] [] listChildren;
    public ConstraintLayout contentViewMenu;
    private  DrawerLayout drawer;
    public static DatabaseReference reference;
    RecyclerView recyclerMenu;
    boolean bandera =true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.color_custmo_marker));
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.primaryDarkColor)));
        navigationView.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.primaryDarkColor)));
        inizialite();
        inizialiteFirebaseApp();
        //inputListExpandable();
        //createExpandableListView();

        if (bandera){
            createRecyclerMenu();
            getSupportFragmentManager().beginTransaction().replace(R.id.contentViewMenu,new IndexFragment()).commit();
            getSupportActionBar().setTitle(getResources().getString(R.string.inicio));
            Log.e("asd","asdasdasd");
            bandera=false;
        }



        navigationView.setNavigationItemSelectedListener(this);



    }

    //Inicialización de vistas.

    private void inizialite() {
        contentViewMenu= findViewById(R.id.contentViewMenu);
        //expListView = findViewById(R.id.expandable_list);
        drawer = findViewById(R.id.drawer_layout);
        recyclerMenu = findViewById(R.id.recyclerMenu);
    }
    //Conexión entre la app y FirebaseApp ,activar persistencia de la base de datos de Firebase y referenciar la instacia de la base de datos

    private void inizialiteFirebaseApp(){
        FirebaseApp.initializeApp(this);
        try {FirebaseDatabase.getInstance().setPersistenceEnabled(false);}catch (Exception e){}

        reference= FirebaseDatabase.getInstance().getReference();
    }
    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
//        tintMenuItemIcon(this,menu,R.id.,getResources().getColor(R.color.colorPrimaryDark));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.index) {


        } else if (id == R.id.profile) {


        }else if (id == R.id.item_Humedad){

        }else if (id == R.id.item_Temperatura){

        }else if (id == R.id.item_Irradiancia){

        }else if (id == R.id.consultas){

        }else if (id == R.id.logout){

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Llamado de listas e ingreso de HashMap para el expandablelistview con navigation drawer

    public void inputListExpandable(){
        listDataHeader = Constants.GROUP_LIST;
        listDataChild = new HashMap<>();
        listChildren = Constants.CHILDREN_LISTS;

        for (int i=0; i<listDataHeader.length; i++){
            listDataChild.put(listDataHeader[i],listChildren[i]);
        }



    }
    //Creación del ExpandableListView y agrego del click

    public void createExpandableListView(){
        listAdapterExpandable = new ExpandableListAdapter(this,listDataHeader,listDataChild);
        listAdapterExpandable.setOnChildClickListener(new ExpandableListAdapter.OnChildClickListener() {
            @Override
            public void childClick(int groupId, int childId) {
                Intent intent;
                if (groupId==0){
                    Uri uri = Uri.parse(Constants.LIST_LINKS_CONOCENOS[childId]);
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);


                }else {
                    switch (childId){
                        case 0:
                            getSupportFragmentManager().beginTransaction().replace(R.id.contentViewMenu,new ContactanosFragment()).commit();
                            break;
                        case 1:
                            getSupportFragmentManager().beginTransaction().replace(R.id.contentViewMenu,new AcercaDeFragment()).commit();
                            break;
                    }
                }
                try {
                    drawer.closeDrawer(GravityCompat.START);
                }catch (Exception ignored){
                    Log.e("Error en drawer",ignored.getMessage());
                }

            }
        });
        expListView.setAdapter(listAdapterExpandable);

        for (int i=0; i<listAdapterExpandable.getGroupCount(); i++){
            //expListView.expandGroup(i); // esta linea de código es para expandir los menús
        }


    }

    public void closeDrawer(){
        try {
            drawer.closeDrawer(GravityCompat.START);
        }catch (Exception ignored){
            Log.e("Error en drawer",ignored.getMessage());
        }


    }

    private void createRecyclerMenu() {

        ListMenu listMenu = new ListMenu();
        listMenu.inputList();
        final List<ItemMenu> list = listMenu.getItemMenus();
        recyclerMenu.setHasFixedSize(true);
        recyclerMenu.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        MenuEAdapter adapter = new MenuEAdapter(list);
        adapter.setmListener(new MenuEAdapter.OnClickListener() {
            @Override
            public void itemClick(String n) {

                if (n.equals(getResources().getString(R.string.inicio))){
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentViewMenu,new IndexFragment()).commit();
                    getSupportActionBar().setTitle(getResources().getString(R.string.inicio));
                    closeDrawer();
                }

                if (n.equals(getResources().getString(R.string.dato1))){
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentViewMenu,new HumedadFragment()).commit();
                    getSupportActionBar().setTitle(getResources().getString(R.string.dato1));
                    closeDrawer();
                }

                if (n.equals(getResources().getString(R.string.dato2))){
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentViewMenu,new TemperaturaFragment()).commit();
                    getSupportActionBar().setTitle(getResources().getString(R.string.dato2));
                    closeDrawer();
                }

                if (n.equals(getResources().getString(R.string.dato3))){
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentViewMenu,new IrradianciaFragment()).commit();
                    getSupportActionBar().setTitle(getResources().getString(R.string.dato3));
                    closeDrawer();
                }

                if (n.equals(getResources().getString(R.string.perfil))){
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentViewMenu,new PerfilFragment()).commit();
                    getSupportActionBar().setTitle(getResources().getString(R.string.perfil));
                    closeDrawer();
                }


            }
        });
        adapter.setmOnChildListener(new MenuEAdapter.OnChildListener() {
            @Override
            public void itemClick(String group, int child) {
                if (group.equals(getResources().getString(R.string.consultas))){
                    switch (child){
                        case 0:
                            closeDrawer();
                            break;

                        case 1:
                            closeDrawer();
                            break;

                        case 2:
                            closeDrawer();
                            break;

                        case 3:
                            closeDrawer();
                            break;

                        case 4:
                            getSupportFragmentManager().beginTransaction().replace(R.id.contentViewMenu, new ConsultasFragment()).commit();
                            getSupportActionBar().setTitle(R.string.consultas);
                            closeDrawer();

                            break;

                    }
                }

                if (group.equals(getResources().getString(R.string.paginas))){

                    Uri uri = Uri.parse(Constants.LIST_LINKS_CONOCENOS[child]);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }

                if (group.equals(getResources().getString(R.string.conocenos))){

                    switch (child){
                        case 0:
                            getSupportFragmentManager().beginTransaction().replace(R.id.contentViewMenu,new ContactanosFragment()).commit();
                            break;
                        case 1:
                            getSupportFragmentManager().beginTransaction().replace(R.id.contentViewMenu,new AcercaDeFragment()).commit();
                            break;
                    }
                    closeDrawer();
                }


            }
        });

        recyclerMenu.setAdapter(adapter);


    }



}
