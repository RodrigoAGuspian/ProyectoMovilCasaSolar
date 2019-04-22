package com.casasolarctpi.appsolar.fragments;


import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.casasolarctpi.appsolar.R;
import com.casasolarctpi.appsolar.models.Constants;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactanosFragment extends Fragment {

    View view;
    TextView txtLinkMap;
    FrameLayout frameLayout;
    public ContactanosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        frameLayout = new FrameLayout(Objects.requireNonNull(getActivity()));
        LayoutInflater inflater1 = (LayoutInflater) Objects.requireNonNull(getActivity()).getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        view =inflater1.inflate(R.layout.fragment_contactanos, null);
        frameLayout.addView(view);
        inizialiteLink();

        return frameLayout;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        frameLayout.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) Objects.requireNonNull(getActivity()).getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        view =inflater.inflate(R.layout.fragment_contactanos, null);
        frameLayout.addView(view);
        inizialiteLink();


    }

    private void inizialiteLink() {
        txtLinkMap = view.findViewById(R.id.txtLinkMap);
        txtLinkMap.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        txtLinkMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                Uri uri = Uri.parse(getString(R.string.link_googlemaps));
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

}
