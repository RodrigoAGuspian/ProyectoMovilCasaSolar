package com.casasolarctpi.appsolar.models;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.casasolarctpi.appsolar.R;
import com.casasolarctpi.appsolar.fragments.IndexFragment;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

public class CustomMarkerView extends MarkerView {


    private TextView txtCustomMarker1, txtCustomMarker2;
    private String tipoDeDato = " ";
    private int colorDelDato = 0;

    public void setTipoDeDato(String tipoDeDato) {
        this.tipoDeDato = tipoDeDato;
    }

    public void setColorDelDato(int colorDelDato) {
        this.colorDelDato = colorDelDato;
    }

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public CustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        txtCustomMarker1 = findViewById(R.id.txtCustomMarker1);
        txtCustomMarker2 = findViewById(R.id.txtCustomMarker2);

    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        super.refreshContent(e, highlight);

        switch (highlight.getDataSetIndex() ){
            case 0:
                txtCustomMarker1.setText(getResources().getString(R.string.fecha)+": "+IndexFragment.labelsChart.get((int) e.getX()));
                txtCustomMarker2.setText(getResources().getString(R.string.voltaje)+": " + e.getY());
                txtCustomMarker2.setTextColor(getResources().getColor(R.color.colorGraficaPunto1));
                break;
            case 1:
                txtCustomMarker1.setText(getResources().getString(R.string.fecha)+": "+IndexFragment.labelsChart.get((int) e.getX()));
                txtCustomMarker2.setText(getResources().getString(R.string.irradiancia)+": " + e.getY());
                txtCustomMarker2.setTextColor(getResources().getColor(R.color.colorGraficaPunto2));
                break;
        }

    }

    @Override
    public MPPointF getOffsetForDrawingAtPoint(float posX, float posY) {

        Log.e("asd",""+posX);
        if (posX>1000){
            float tmp = posX-600;
            Log.e("asd1",""+tmp);
            return super.getOffsetForDrawingAtPoint(0, posY);

        }else {
            return super.getOffsetForDrawingAtPoint(posX, posY);
        }
    }
}
