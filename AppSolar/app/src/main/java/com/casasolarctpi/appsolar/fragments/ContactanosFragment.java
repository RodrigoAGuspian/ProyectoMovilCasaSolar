package com.casasolarctpi.appsolar.fragments;


import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.casasolarctpi.appsolar.R;
import com.casasolarctpi.appsolar.models.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactanosFragment extends Fragment {

    View view;
    TextView txtLinkMap;
    public ContactanosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contactanos, container, false);
        txtLinkMap = view.findViewById(R.id.txtLinkMap);
        txtLinkMap.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        txtLinkMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                Uri uri = Uri.parse(txtLinkMap.getText().toString());
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        return view;
    }

}
