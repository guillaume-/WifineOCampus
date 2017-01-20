package com.neocampus.wifishared.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.neocampus.wifishared.R;

/**
 * Created by Hirochi ☠ on 20/01/17.
 */

public class EchelleSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable{

    private SurfaceHolder holder;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    private int scrollHeight = 0;
    private float maxGigaoctets = 30.0f;
    private int surfaceMaxHeigth = 0, surfaceMinHeigth = 0;
    private int surfaceScale = 200;
    private int background_color;

    public EchelleSurfaceView(Context context) {
        super(context);
        onInit();
    }

    public EchelleSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        onInit();
    }

    public EchelleSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onInit();
    }

    private void onInit() {
        holder = getHolder();
        holder.setFormat(PixelFormat.TRANSPARENT);
        holder.addCallback(this);

        paint.setStyle(Paint.Style.FILL);

        paint.setTextSize(25);
        background_color = ContextCompat
                .getColor(getContext(), R.color.colorSurfaceBackground);
        int colorText = ContextCompat.
                getColor(getContext(), R.color.colorSurfaceText);
        paint.setColor(colorText);
        paint.setStrokeWidth(3);

    }

    private void drawToCanvas(Canvas canvas) {
        canvas.drawColor(background_color);

        int SizeH = canvas.getHeight();

        int startX = 200;
        surfaceScale = 200;
        int quantite = 0;
        surfaceMaxHeigth = SizeH - surfaceScale;
        surfaceMinHeigth =  (scrollHeight - surfaceScale);
        float step = (surfaceMaxHeigth - surfaceMinHeigth) / (maxGigaoctets * 10);
        int gigaStep = 0;
        for(float i = surfaceMaxHeigth; i >= surfaceMinHeigth; i-= step) {
            canvas.drawLine(startX - 30, i, startX, i, paint);
            if(gigaStep % 10 == 0){
                canvas.drawLine(startX - 50, i, startX, i, paint);
                canvas.drawText(quantite + "", startX - 130, i+8, paint);
                quantite += 1000;
            }
            gigaStep++;
        }
        canvas.drawLine(startX, SizeH, startX, surfaceMinHeigth - 100, paint);
    }

    public void setScrollHeight(int scrollHeight) {
        this.scrollHeight = scrollHeight;
        run();
    }

    public void setMaxEchelle(float maxGigaoctets) {
        this.maxGigaoctets = maxGigaoctets;
    }

    public int getSurfaceScale() {
        return surfaceScale;
    }

    public int getSurfaceMaxHeigth() {
        return surfaceMaxHeigth;
    }

    public int getSurfaceMinHeigth() {
        return surfaceMinHeigth;
    }

    public float getMaxGigaoctets() {
        return maxGigaoctets;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        post(this);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void run() {
        Canvas c = holder.lockCanvas(null);
        if(c != null) {
            drawToCanvas(c);
            holder.unlockCanvasAndPost(c);
        }
    }
}
