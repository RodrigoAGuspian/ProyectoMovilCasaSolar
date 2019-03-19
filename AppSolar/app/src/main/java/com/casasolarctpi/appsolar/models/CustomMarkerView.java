package com.casasolarctpi.appsolar.models;

import android.content.Context;
import android.graphics.Canvas;
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

import java.util.List;

public class CustomMarkerView extends MarkerView {


    private TextView txtCustomMarker1, txtCustomMarker2;
    private List<String> labelsChart;


    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */

    public CustomMarkerView(Context context, int layoutResource, List<String> labelsChart) {
        super(context, layoutResource);
        this.labelsChart = labelsChart;
        txtCustomMarker1 = findViewById(R.id.txtCustomMarker1);
        txtCustomMarker2 = findViewById(R.id.txtCustomMarker2);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        super.refreshContent(e, highlight);

        switch (highlight.getDataSetIndex() ){
            case 0:
                txtCustomMarker1.setText(getResources().getString(R.string.fecha)+": "+labelsChart.get((int) e.getX()));
                txtCustomMarker2.setText(getResources().getString(R.string.dato1)+": " + e.getY());
                txtCustomMarker2.setTextColor(getResources().getColor(R.color.colorGraficaPunto1));
                break;
            case 1:
                txtCustomMarker1.setText(getResources().getString(R.string.fecha)+": "+labelsChart.get((int) e.getX()));
                txtCustomMarker2.setText(getResources().getString(R.string.dato2)+": " + e.getY());
                txtCustomMarker2.setTextColor(getResources().getColor(R.color.colorGraficaPunto2));
                break;
        }

    }

    @Override
    public MPPointF getOffsetForDrawingAtPoint(float posX, float posY) {
        return super.getOffsetForDrawingAtPoint(posX, posY);
    }

    @Override
    public void draw(Canvas canvas, float posX, float posY) {
        int tmp =getWidth();
        posX += getOffset().getX();

        // AVOID OFFSCREEN

        posX=0;
        posY=0;

        canvas.translate(posX, posY);
        draw(canvas);
        canvas.translate(-posX, -posY);

        super.draw(canvas, posX, posY);
    }
}
