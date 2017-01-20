package com.neocampus.wifishared.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewTreeObserver;

import com.neocampus.wifishared.R;

import java.util.Locale;

/**
 * Created by Hirochi ☠ on 19/01/17.
 */

public class DataSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder holder;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    private Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    private ViewTreeObserver observer;
    private Rect textBounds = new Rect();
    private float limiteData = 0.0f;

    public DataSurfaceView(Context context) {
        super(context);
        onInit();
    }

    public DataSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        onInit();
    }

    public DataSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onInit();
    }

    private void onInit() {
        holder = getHolder();
        holder.setFormat(PixelFormat.TRANSPARENT);
        holder.addCallback(this);
        setZOrderOnTop(true);

        paintText.setTextAlign(Paint.Align.LEFT);
        paintText.setTextSize(80);
        paintText.setStyle(Paint.Style.FILL);
        int colorText = ContextCompat.
                getColor(getContext(), R.color.colorSurfaceText);
        paintText.setColor(colorText);
        paint.setColor(colorText);
        setLayerType(LAYER_TYPE_SOFTWARE, paintText);
        paintText.setShadowLayer(4.0f, 0.0f, 2.0f, Color.BLACK);
        paint.setStrokeWidth(5);
    }

    private void drawToCanvas(Canvas canvas) {
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);

        int SizeW = canvas.getWidth();
        int SizeH = canvas.getHeight();

        int HalfSizeW = SizeW / 2;
        int HalfSizeH = SizeH / 2;

        int QuartSizeH = HalfSizeH / 2;

        int startX = 150;
        int startY = 200;
        canvas.drawLine(startX, SizeH - startY, SizeW - startX, SizeH - startY, paint);
        canvas.drawCircle(SizeW - startX, SizeH - startY + 20, 20, paint);
        String level;
        if(limiteData >= 1.0f) {
            level = String.format(Locale.FRANCE, "%.3f Go", limiteData);
        }
        else {
            level = String.format(Locale.FRANCE, "%d,0 Mo", (int)(limiteData * 1000.f));
        }
        paintText.getTextBounds(level, 0, level.length(), textBounds);
        float x = HalfSizeW - textBounds.width() / 2f - textBounds.left;
        float y = (QuartSizeH * 2.5f) + textBounds.height() / 2f - textBounds.bottom;
        canvas.drawText(level, x, y, paintText);

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

    public void setLimiteData(float limiteData) {
        this.limiteData = limiteData;
        post(this);
    }

    public float getLimiteData() {
        return limiteData;
    }
}
