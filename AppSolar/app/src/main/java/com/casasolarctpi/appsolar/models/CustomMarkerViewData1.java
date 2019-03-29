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

public class CustomMarkerViewData1 extends MarkerView {
    private TextView txtCustomMarker1, txtCustomMarker2;
    private int colorDelDato = 0;
    private String tipoDelDato = "";
    private List<String> labelsChart;

    public void setColorDelDato(int colorDelDato) {
        this.colorDelDato = colorDelDato;
    }

    public void setTipoDelDato(String tipoDelDato) {
        this.tipoDelDato = tipoDelDato;
    }

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */

    public CustomMarkerViewData1(Context context, int layoutResource, List<String> labelsChart) {
        super(context, layoutResource);
        this.labelsChart = labelsChart;
        txtCustomMarker1 = findViewById(R.id.txtCustomMarker1);
        txtCustomMarker2 = findViewById(R.id.txtCustomMarker2);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        super.refreshContent(e, highlight);
        txtCustomMarker1.setText(getResources().getString(R.string.hora)+": "+ labelsChart.get((int) e.getX()));
        txtCustomMarker2.setText(tipoDelDato+": " + e.getY());
        txtCustomMarker2.setTextColor(colorDelDato);

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
        posX=1;
        posY=0;
        canvas.translate(posX, posY);
        draw(canvas);
        canvas.translate(-posX, -posY);

        super.draw(canvas, posX, posY);
    }

}
