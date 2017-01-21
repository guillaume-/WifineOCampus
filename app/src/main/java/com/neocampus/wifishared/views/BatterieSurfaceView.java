package com.neocampus.wifishared.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.neocampus.wifishared.R;

import java.util.Locale;

/**
 * Created by Hirochi ☠ on 21/01/17.
 */

public class BatterieSurfaceView extends SurfaceView implements
        SurfaceHolder.Callback, Runnable, View.OnTouchListener{

    private SurfaceHolder holder;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    private Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    private int limiteBatterie = 0;
    private float MinBatterieH = 0;
    private float MaxBatterieH = 0;
    private Rect textBounds = new Rect();
    private int colorText, colorBackgroundOn, colorBackgroundOff;

    public BatterieSurfaceView(Context context) {
        super(context);
        onInit();
    }

    public BatterieSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        onInit();
    }

    public BatterieSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onInit();
    }


    private void onInit() {
        holder = getHolder();
        holder.setFormat(PixelFormat.TRANSPARENT);
        holder.addCallback(this);
        setOnTouchListener(this);

        paintText.setTextSize(80);
        paintText.setStyle(Paint.Style.FILL);
        colorText = ContextCompat.
                getColor(getContext(), R.color.colorBatterieText);
        colorBackgroundOn = ContextCompat.
                getColor(getContext(), R.color.colorBatterieBackgroundOn);
        colorBackgroundOff = ContextCompat.
                getColor(getContext(), R.color.colorBatterieBackgroundOff);
        paintText.setColor(Color.GRAY);
        paintText.setShadowLayer(2.0f, 0.0f, 2.0f, Color.BLACK);
        paint.setShadowLayer(15.0f, 0.0f, 2.0f, Color.parseColor("#c5cee7"));
        paintText.setTextAlign(Paint.Align.CENTER);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(3);
    }

    private void drawToCanvas(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        int SizeW = canvas.getWidth();
        int SizeH = canvas.getHeight();

        int HalfSizeW = SizeW / 2;
        int HalfSizeH = SizeH / 2;

        int QuartSizeW = HalfSizeW / 2;
        int QuartSizeH = HalfSizeH / 2;

        paint.setStyle(Paint.Style.STROKE);

        float MinBatterieW = QuartSizeW;
        float MaxBatterieW = QuartSizeW * 3;
        MinBatterieH = QuartSizeH * (1.0f/2.0f);
        MaxBatterieH = QuartSizeH * 3.5f;

        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        paint.setColor(colorBackgroundOn);
        Path path = new Path();
        path.moveTo(MinBatterieW, MinBatterieH);
        path.lineTo(MinBatterieW, MaxBatterieH);
        path.lineTo(MaxBatterieW, MaxBatterieH);
        path.lineTo(MaxBatterieW, MinBatterieH);
        path.lineTo(HalfSizeW + 100, MinBatterieH);
        path.lineTo(HalfSizeW + 100, QuartSizeH * (1.0f/4.0f));
        path.lineTo(HalfSizeW - 100, QuartSizeH * (1.0f/4.0f));
        path.lineTo(HalfSizeW - 100, MinBatterieH);
        canvas.drawPath(path, paint);

        paint.setStyle(Paint.Style.FILL);

        float BatterieW = MaxBatterieW - MinBatterieW;
        float BatterieH = MaxBatterieH - MinBatterieH;

        float SeulBatterieH = MaxBatterieH -
                (BatterieH * (limiteBatterie / 100.0f));

        paint.setColor(colorBackgroundOff);

        canvas.drawRect(MinBatterieW, MaxBatterieH,
                MaxBatterieW, SeulBatterieH, paint );

        int startX = 130;
        canvas.drawLine(startX, SeulBatterieH, SizeW - startX, SeulBatterieH, paint);
        canvas.drawCircle(SizeW - startX - 5, SeulBatterieH + 15, 20, paint);

        String batterie = String.format(Locale.FRANCE, "%d%%", limiteBatterie);
        paintText.getTextBounds(batterie, 0, batterie.length(), textBounds);
        paintText.setColor(limiteBatterie > 50 ? Color.WHITE : colorText);
        canvas.drawText(batterie, HalfSizeW+20, HalfSizeH - textBounds.centerY(), paintText);

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

    public void setLimiteBatterie(int limiteBatterie) {
        this.limiteBatterie = limiteBatterie;
        post(this);
    }

    public int getLimiteBatterie() {
        return limiteBatterie;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_MOVE:
                if(event.getY() > MinBatterieH
                        && event.getY() < MaxBatterieH) {
                    float ReplaceY = event.getY() - MinBatterieH;
                    float puissance = ReplaceY / (MaxBatterieH - MinBatterieH);
                    limiteBatterie = (int) ((1.0f - puissance) * 100);
                    post(this);
                }

                break;
        }
        return true;
    }
}
