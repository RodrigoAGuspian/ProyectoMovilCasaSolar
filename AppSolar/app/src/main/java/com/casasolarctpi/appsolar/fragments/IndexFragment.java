package com.casasolarctpi.appsolar.fragments;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.casasolarctpi.appsolar.R;
import com.google.firebase.FirebaseApp;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndexFragment extends Fragment {
    //Declaración de variables
    private TextView txtInfoPrincipal;
    private View view;
    FrameLayout frameLayout;

    public IndexFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FirebaseApp.initializeApp(Objects.requireNonNull(getContext()));
        frameLayout = new FrameLayout(Objects.requireNonNull(getActivity()));
        LayoutInflater inflater1 = (LayoutInflater) Objects.requireNonNull(getActivity()).getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        view =inflater1.inflate(R.layout.fragment_index, null);
        inizialite();
        haveScroll();
        frameLayout.addView(view);
        return frameLayout;

    }

    //Método para crear ejecto scroll al texto principal
    private void haveScroll() {
        txtInfoPrincipal.setMovementMethod(new ScrollingMovementMethod());
    }

    //inicializacion de vistas
    private void inizialite() {
        txtInfoPrincipal = view.findViewById(R.id.txtInfoPrincipal);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        frameLayout.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) Objects.requireNonNull(getActivity()).getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        view =inflater.inflate(R.layout.fragment_index, null);
        frameLayout.addView(view);
        haveScroll();


    }


}
