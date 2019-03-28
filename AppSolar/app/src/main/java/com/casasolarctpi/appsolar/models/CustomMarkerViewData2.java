package com.casasolarctpi.appsolar.models;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.TextView;

import com.casasolarctpi.appsolar.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.List;

public class CustomMarkerViewData2 extends MarkerView {
    private TextView txtCustomMarker1, txtCustomMarker2;
    private List<String> labelsChart;

    private String dato1, dato2;
    private int color1, color2;




    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */

    public CustomMarkerViewData2(Context context, int layoutResource, List<String> labelsChart, String dato1, String dato2, int color1, int color2) {
        super(context, layoutResource);
        this.labelsChart = labelsChart;
        this.dato1 = dato1;
        this.dato2 = dato2;
        this.color1 = color1;
        this.color2 = color2;
        txtCustomMarker1 = findViewById(R.id.txtCustomMarker1);
        txtCustomMarker2 = findViewById(R.id.txtCustomMarker2);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        super.refreshContent(e, highlight);

        switch (highlight.getDataSetIndex() ) {
            case 0:
                txtCustomMarker1.setText(getResources().getString(R.string.hora) + ": " + labelsChart.get((int) e.getX()));
                txtCustomMarker2.setText(dato1 + ": " + e.getY());
                txtCustomMarker2.setTextColor(color1);
                break;
            case 1:
                txtCustomMarker1.setText(getResources().getString(R.string.hora) + ": " + labelsChart.get((int) e.getX()));
                txtCustomMarker2.setText(dato2 + ": " + e.getY());
                txtCustomMarker2.setTextColor(color2);
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
