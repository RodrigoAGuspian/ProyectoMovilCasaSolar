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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.casasolarctpi.appsolar.R;
import com.casasolarctpi.appsolar.fragments.ConsultasFragment;
import com.casasolarctpi.appsolar.fragments.ContactanosFragment;
import com.casasolarctpi.appsolar.fragments.IndexFragment;
import com.casasolarctpi.appsolar.fragments.PerfilFragment;
import com.casasolarctpi.appsolar.fragments.TiempoRealFragment;
import com.casasolarctpi.appsolar.models.ChildClass;
import com.casasolarctpi.appsolar.models.Constants;
import com.casasolarctpi.appsolar.models.ExpandableListAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class MenuPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnClickListener {

    //Declaración de variables
    private ExpandableListView expPagina, expConsultas;
    private ExpandableListAdapter listAdapterExpandable, expaAdaperConsultas;
    private String[] listDataHeader;
    private TextView txtTitle;
    private HashMap<String, ChildClass[]> listDataChild, listaDataConsultas;
    private ChildClass [] listChildrenPaginas;
    private ChildClass [] listChildrenConsultas;
    public ConstraintLayout contentViewMenu, cLHome, cLHumedad, cLTemperatura, cLIrradiancia, cLContactanos, cLPerfil, cLCerrarSesion;
    private  DrawerLayout drawer;
    public static DatabaseReference reference;
    boolean bandera =true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
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
        inputListExpandable();
        createExpandableListView();

        if (bandera){
            getSupportFragmentManager().beginTransaction().replace(R.id.contentViewMenu,new IndexFragment()).commit();
            getSupportActionBar().setTitle(getResources().getString(R.string.inicio));
            txtTitle.setText(getResources().getString(R.string.inicio));
            bandera=false;
        }


        navigationView.setNavigationItemSelectedListener(this);



    }

    //Inicialización de vistas.

    private void inizialite() {
        contentViewMenu= findViewById(R.id.contentViewMenu);
        //expListView = findViewById(R.id.expandable_list);
        txtTitle = findViewById(R.id.txtTitle);
        drawer = findViewById(R.id.drawer_layout);
        expPagina = findViewById(R.id.expaPaginas);
        expConsultas = findViewById(R.id.expaConsultas);
        cLHome = findViewById(R.id.cLHome);
        cLHumedad = findViewById(R.id.cLHumedad);
        cLTemperatura = findViewById(R.id.cLTemperatura);
        cLIrradiancia = findViewById(R.id.cLIrradiancia);
        cLContactanos = findViewById(R.id.cLContactanos);
        cLPerfil = findViewById(R.id.cLPerfil);
        cLCerrarSesion = findViewById(R.id.cLCerrarSesion);

        cLHome.setOnClickListener(this);
        cLHumedad.setOnClickListener(this);
        cLTemperatura.setOnClickListener(this);
        cLIrradiancia.setOnClickListener(this);
        cLContactanos.setOnClickListener(this);
        cLPerfil.setOnClickListener(this);
        cLCerrarSesion.setOnClickListener(this);

        expPagina.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                setListViewHeight(parent,groupPosition);
                return false;
            }
        });



        expConsultas.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                setListViewHeight(parent,groupPosition);
                return false;
            }
        });
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
        listChildrenPaginas = new ChildClass[Constants.PAGES_LIST.length];

        for (int i=0;i<listChildrenPaginas.length;i++){
            listChildrenPaginas[i]= new ChildClass(Constants.PAGES_LIST[i],R.drawable.ic_link);
        }


        listChildrenConsultas = new ChildClass[Constants.LIST_QUERY.length];

        for (int i=0;i<listChildrenConsultas.length;i++){
            listChildrenConsultas[i]= new ChildClass(Constants.LIST_QUERY[i],R.drawable.ic_file_search);
        }

        listDataChild.put(listDataHeader[0], listChildrenPaginas);

        listaDataConsultas = new HashMap<>();
        listaDataConsultas.put(getResources().getString(R.string.consultas),listChildrenConsultas);



    }
    //Creación del ExpandableListView y agrego del click

    public void createExpandableListView(){
        listAdapterExpandable = new ExpandableListAdapter(this,new String[]{getResources().getString(R.string.paginas)},listDataChild);

        listAdapterExpandable.setOnChildClickListener(new ExpandableListAdapter.OnChildClickListener() {
            @Override
            public void childClick(int groupId, int childId) {
                Intent intent;
                Uri uri = Uri.parse(Constants.LIST_LINKS_CONOCENOS[childId]);
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                closeDrawer();

            }
        });



        expaAdaperConsultas = new ExpandableListAdapter(this,new  String[] {getResources().getString(R.string.consultas)},listaDataConsultas);
        expaAdaperConsultas.setOnChildClickListener(new ExpandableListAdapter.OnChildClickListener() {
            @Override
            public void childClick(int groupId, int childId) {
                ConsultasFragment.modoGraficar=childId;
                switch (childId){
                    case 0:
                        getSupportFragmentManager().beginTransaction().replace(R.id.contentViewMenu, new ConsultasFragment()).commit();
                        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.irradiancia_humedad);
                        txtTitle.setText(getResources().getString(R.string.irradiancia_humedad));
                        closeDrawer();
                        break;

                    case 1:
                        closeDrawer();
                        break;

                    case 2:
                        closeDrawer();
                        break;

                    case 3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.contentViewMenu, new ConsultasFragment()).commit();
                        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.irradiancia_temperatura);
                        txtTitle.setText(getResources().getString(R.string.irradiancia_temperatura  ));
                        closeDrawer();
                        break;

                    case 4:
                        getSupportFragmentManager().beginTransaction().replace(R.id.contentViewMenu, new ConsultasFragment()).commit();
                        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.humedad_temperatura);
                        txtTitle.setText(getResources().getString(R.string.humedad_temperatura));
                        closeDrawer();
                        break;
                }
            }
        });

        expConsultas.setAdapter(expaAdaperConsultas);
        expPagina.setAdapter(listAdapterExpandable);

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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cLHome:
                getSupportFragmentManager().beginTransaction().replace(R.id.contentViewMenu,new IndexFragment()).commit();
                Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.inicio));
                txtTitle.setText(getResources().getString(R.string.inicio));
                closeDrawer();
                break;

            case R.id.cLHumedad:
                TiempoRealFragment.modoGraficar=0;

                getSupportFragmentManager().beginTransaction().replace(R.id.contentViewMenu,new TiempoRealFragment()).commit();
                Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.dato1));
                txtTitle.setText(getResources().getString(R.string.dato1));
                try{
                    TiempoRealFragment.tiempoRealChart.clear();
                    TiempoRealFragment.entry1.clear();
                    TiempoRealFragment.tiempoRealChart.setVisibility(View.INVISIBLE);

                }catch (Exception ignored){

                }
                closeDrawer();
                break;

            case R.id.cLTemperatura:
                TiempoRealFragment.modoGraficar=1;
                getSupportFragmentManager().beginTransaction().replace(R.id.contentViewMenu,new TiempoRealFragment()).commit();
                Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.dato2));
                txtTitle.setText(getResources().getString(R.string.dato2));
                try{
                    TiempoRealFragment.tiempoRealChart.clear();
                    TiempoRealFragment.entry1.clear();
                    TiempoRealFragment.tiempoRealChart.setVisibility(View.INVISIBLE);


                }catch (Exception ignored) {

                }
                closeDrawer();
                break;

            case R.id.cLIrradiancia:
                TiempoRealFragment.modoGraficar=2;
                getSupportFragmentManager().beginTransaction().replace(R.id.contentViewMenu,new TiempoRealFragment()).commit();
                Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.dato3));
                txtTitle.setText(getResources().getString(R.string.dato3));
                try{
                    TiempoRealFragment.tiempoRealChart.clear();
                    TiempoRealFragment.entry1.clear();
                    TiempoRealFragment.tiempoRealChart.setVisibility(View.INVISIBLE);

                }catch (Exception ignored){

                }
                closeDrawer();
                break;

            case R.id.cLContactanos:
                getSupportFragmentManager().beginTransaction().replace(R.id.contentViewMenu,new ContactanosFragment()).commit();
                Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.contactanos));
                txtTitle.setText(getResources().getString(R.string.contactanos));
                closeDrawer();
                break;

            case R.id.cLPerfil:
                getSupportFragmentManager().beginTransaction().replace(R.id.contentViewMenu,new PerfilFragment()).commit();
                Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.perfil));
                txtTitle.setText(getResources().getString(R.string.perfil));
                closeDrawer();
                break;

            case R.id.cLCerrarSesion:
                closeDrawer();
                break;


        }
    }


    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }


}
