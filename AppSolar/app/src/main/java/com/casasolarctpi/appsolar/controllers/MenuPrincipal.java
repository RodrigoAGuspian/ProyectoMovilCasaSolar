package com.casasolarctpi.appsolar.controllers;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.FontRequest;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.LayoutInflater;
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
import android.widget.ImageView;
import android.widget.Toast;


import com.casasolarctpi.appsolar.R;
import com.casasolarctpi.appsolar.fragments.AcercaDeFragment;
import com.casasolarctpi.appsolar.fragments.ContactanosFragment;
import com.casasolarctpi.appsolar.fragments.IndexFragment;
import com.casasolarctpi.appsolar.fragments.PerfilFragment;
import com.casasolarctpi.appsolar.models.ChartValues;
import com.casasolarctpi.appsolar.models.Constants;
import com.casasolarctpi.appsolar.models.ExpandableListAdapter;
import com.casasolarctpi.appsolar.models.RestClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MenuPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ExpandableListView expListView;
    ExpandableListAdapter listAdapterExpandable;
    String[] listDataHeader;
    HashMap<String, String []> listDataChild;
    String [] [] listChildren;

    ConstraintLayout contentViewMenu;

    DrawerLayout drawer;

    private MutableLiveData<List<ChartValues>> liveData;



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
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorPrimaryDark));
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
        navigationView.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
        inizialite();
        inputListExpandable();
        createExpandableListView();
        getSupportFragmentManager().beginTransaction().replace(R.id.contentViewMenu,new IndexFragment()).commit();


        navigationView.setNavigationItemSelectedListener(this);


    }

    private void inizialite() {
        contentViewMenu= findViewById(R.id.contentViewMenu);
        expListView = findViewById(R.id.expandable_list);
        drawer = findViewById(R.id.drawer_layout);
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
            getSupportFragmentManager().beginTransaction().replace(R.id.contentViewMenu,new IndexFragment()).commit();

        } else if (id == R.id.profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.contentViewMenu,new PerfilFragment()).commit();

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

    //Creaciónde y llamadado del ExpandableListView
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
            //expListView.expandGroup(i);
        }


    }




}
