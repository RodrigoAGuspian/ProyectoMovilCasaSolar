package com.casasolarctpi.appsolar.controllers;

import android.os.Bundle;
import android.provider.FontRequest;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;


import com.casasolarctpi.appsolar.R;
import com.casasolarctpi.appsolar.models.Constants;
import com.casasolarctpi.appsolar.models.ExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ExpandableListView expListView;
    ExpandableListAdapter listAdapterExpandable;
    String[] listDataHeader;
    HashMap<String, String []> listDataChild;

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
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        inizialite();
        inputListExpandable();
        createExpandableListView();


        navigationView.setNavigationItemSelectedListener(this);
    }

    private void inizialite() {
        expListView = findViewById(R.id.expandable_list);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Llamado de listas e ingreso de HashMap para el expandablelistview con navigation drawer
    public void inputListExpandable(){
        listDataHeader = Constants.LIST_GROUP;
        listDataChild = new HashMap<>();
        String [] [] listChilds = Constants.LISTS_CHILDS;

        for (int i=0; i<listDataHeader.length; i++){
            listDataChild.put(listDataHeader[i],listChilds[i]);
        }



    }

    //Creaciónde y llamadado del ExpandableListView
    public void createExpandableListView(){
        listAdapterExpandable = new ExpandableListAdapter(this,listDataHeader,listDataChild);
        expListView.setAdapter(listAdapterExpandable);

        for (int i=0; i<listAdapterExpandable.getGroupCount(); i++){
            expListView.expandGroup(i);
        }
    }


}
