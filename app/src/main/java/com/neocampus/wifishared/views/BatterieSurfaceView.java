package com.neocampus.wifishared.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
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
    private int colorContent, colorEmpty;

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
        float density = getContext().getResources().getDisplayMetrics().density;
        paintText.setTextSize(density * 30);
        paintText.setStyle(Paint.Style.FILL);
        colorText = ContextCompat.
                getColor(getContext(), R.color.colorBatterieText);
        colorContent = ContextCompat.
                getColor(getContext(), R.color.colorSurfaceText);
        colorEmpty = ContextCompat.
                getColor(getContext(), R.color.colorBatterieBackgroundOn);
        paintText.setColor(Color.GRAY);
        paintText.setShadowLayer(2.0f, 0.0f, 2.0f, Color.BLACK);
        paint.setShadowLayer(15.0f, 0.0f, 2.0f, Color.parseColor("#c5cee7"));
        paintText.setTextAlign(Paint.Align.LEFT);
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

        float ScaleStart = HalfSizeW - (SizeW / 3.5f);
        float ScaleEnd = HalfSizeW + (SizeW / 3.5f);
        float ScaleTop = HalfSizeH / 6.0f ;
        float ScaleBottom = SizeH - ScaleTop;
        float OvalSizeH = SizeH * 0.10f;
        float ChargeStart = HalfSizeW - ((ScaleEnd- ScaleStart) * 0.30f);
        float ChargeEnd = HalfSizeW + ((ScaleEnd- ScaleStart) * 0.30f);
        float ChargeSizeH = OvalSizeH * 0.80f;
        float ChargeScaleBottom = (ScaleTop - ((ScaleBottom - ScaleTop)*0.10f))+15;

        RectF ovalTop = new RectF(ScaleStart , ScaleTop,
                ScaleEnd, ScaleTop + OvalSizeH);

        RectF ovalBottom = new RectF(ScaleStart , ScaleBottom,
                ScaleEnd, ScaleBottom - OvalSizeH);

        RectF ovalChargeTop = new RectF(ChargeStart , ChargeScaleBottom,
                ChargeEnd, ChargeScaleBottom + ChargeSizeH);

        RectF ovalChargeBottom = new RectF(ChargeStart , ScaleTop,
                ChargeEnd, ScaleTop + ChargeSizeH);

        RectF ovalChargeCenter = new RectF(ChargeStart , ovalChargeTop.centerY(),
                ChargeEnd, ovalChargeBottom.centerY());

        RectF rectCenter = new RectF(ScaleStart , ovalTop.centerY(),
                ScaleEnd, ovalBottom.centerY());
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(colorEmpty);
        canvas.drawRect(rectCenter, paint);

        paint.setColor(colorContent);

        float MinPosX = ScaleStart - 10;
        float MaxPosX = ScaleEnd - 10;
        MinBatterieH = ovalTop.centerY();
        MaxBatterieH = ovalBottom.centerY();

        float Step = (MaxBatterieH - MinBatterieH) / 100.f;
        float ScalePosY = Step * limiteBatterie;

        RectF ovalValue = new RectF(ScaleStart , MaxBatterieH - (ScalePosY - (OvalSizeH / 2.0f)),
                ScaleEnd, MaxBatterieH - (ScalePosY + (OvalSizeH / 2.0f)));

        RectF rectValue = new RectF(ScaleStart , ovalValue.centerY(),
                ScaleEnd, ovalBottom.centerY());

        canvas.drawRect(rectValue, paint);

        paint.setColor(colorContent);
        canvas.drawOval(ovalBottom, paint);

        paint.setColor(Color.WHITE);
        canvas.drawOval(ovalValue, paint);

        paint.setColor(colorEmpty);
        canvas.drawOval(ovalTop, paint);
        canvas.drawRect(ovalChargeCenter, paint);
        canvas.drawOval(ovalChargeBottom, paint);
        canvas.drawOval(ovalChargeTop, paint);


        paint.setColor(colorContent);
        canvas.drawLine(MaxPosX + 40, ovalValue.centerY(), MaxPosX+10, ovalValue.centerY(), paint);
//        canvas.drawCircle(ScaleEnd + 25, ovalValue.centerY() + 9, 10, paint);


        paintText.setColor(colorEmpty);

        String batterie = String.format(Locale.FRANCE, "%d%%", limiteBatterie);
        paintText.getTextBounds(batterie, 0, batterie.length(), textBounds);
        paintText.setColor(limiteBatterie > 50 ? Color.WHITE : colorText);
        float x = (HalfSizeW - (textBounds.width() / 2f)) - textBounds.left;
        float y = (HalfSizeH + (textBounds.height() / 2f)) - textBounds.bottom;
        canvas.drawText(batterie, x, y, paintText);

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
