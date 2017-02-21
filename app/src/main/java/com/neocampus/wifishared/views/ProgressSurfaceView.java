package com.neocampus.wifishared.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.neocampus.wifishared.R;


/**
 * ProgressSurfaceView permet d'afficher une animation d'écoulement
 */
public class ProgressSurfaceView extends SurfaceView implements
        SurfaceHolder.Callback, Runnable {

    private SurfaceHolder holder;
    private Paint   paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    private Paint   paintText = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    private Rect    textBounds = new Rect();
    private float   maxProgress = 30.0f;
    private int     valueProgress = (int) maxProgress;
    private int     vitessProgress = 10;
    private int     colorContent, colorEmpty;
    private boolean drawOk;

    public ProgressSurfaceView(Context context) {
        super(context);
        onInit();
    }

    public ProgressSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        onInit();
    }

    public ProgressSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onInit();
    }

    private void onInit() {
        holder = getHolder();
        holder.setFormat(PixelFormat.TRANSPARENT);
        holder.addCallback(this);
        float density = getContext().getResources().getDisplayMetrics().density;
        paintText.setTextAlign(Paint.Align.LEFT);
        paintText.setTextSize(density*30);
        paintText.setStyle(Paint.Style.FILL);
        colorContent = ContextCompat.
                getColor(getContext(), R.color.colorSurfaceText);
        colorEmpty = ContextCompat.
                getColor(getContext(), R.color.colorBatterieBackgroundOn);

        paintText.setShadowLayer(4.0f, 0.0f, 2.0f, Color.BLACK);
        paint.setShadowLayer(15.0f, 0.0f, 2.0f, Color.parseColor("#c5cee7"));
        paint.setStrokeWidth(5);
    }

    private void drawToCanvas(Canvas canvas) {
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);

        int SizeW = canvas.getWidth();
        int SizeH = canvas.getHeight();

        int HalfSizeW = SizeW / 2;
        int HalfSizeH = SizeH / 2;

        float ScaleStart = HalfSizeW - (SizeW / 4.0f);
        float ScaleEnd = HalfSizeW + (SizeW / 4.0f);
        float ScaleTop = HalfSizeH / 1.8f;
        float ScaleBottom = SizeH - ScaleTop;
        float OvalSizeH = SizeH * 0.10f;

        RectF ovalTop = new RectF(ScaleStart, ScaleTop,
                ScaleEnd, ScaleTop + OvalSizeH);

        RectF ovalBottom = new RectF(ScaleStart, ScaleBottom,
                ScaleEnd, ScaleBottom - OvalSizeH);

        RectF rectCenter = new RectF(ScaleStart, ovalTop.centerY(),
                ScaleEnd, ovalBottom.centerY());
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(colorEmpty);
        canvas.drawRect(rectCenter, paint);

        paint.setColor(colorContent);

        float MinProgressHeight = ovalTop.centerY();
        float MaxProgressHeight = ovalBottom.centerY();

        String level = "Patientez ...";
        float Step = (MaxProgressHeight - MinProgressHeight) / 1.f;
        float Value = valueProgress / maxProgress;
        float ScalePosY = Step * Value;

        RectF ovalValue = new RectF(ScaleStart, MaxProgressHeight - (ScalePosY - (OvalSizeH / 2.0f)),
                ScaleEnd, MaxProgressHeight - (ScalePosY + (OvalSizeH / 2.0f)));

        RectF rectValue = new RectF(ScaleStart, ovalValue.centerY(),
                ScaleEnd, ovalBottom.centerY());

        canvas.drawRect(rectValue, paint);

        paint.setColor(colorContent);
        canvas.drawOval(ovalBottom, paint);

        paint.setColor(Color.WHITE);
        canvas.drawOval(ovalValue, paint);

        paint.setColor(colorEmpty);
        canvas.drawOval(ovalTop, paint);

        paint.setColor(colorContent);
        paintText.setColor(colorEmpty);
        paintText.getTextBounds(level, 0, level.length(), textBounds);
        float x = (HalfSizeW - (textBounds.width() / 2f)) - textBounds.left;
        float y = (HalfSizeH + (textBounds.height() / 2f)) - textBounds.bottom;
        canvas.drawText(level, x, y, paintText);

        if (valueProgress > 0) {
            valueProgress--;
            postDelayed(this, vitessProgress);
        } else {
            Object o = getTag();
            if (o != null
                    && o instanceof Dialog) {
                ((Dialog) o).dismiss();
            }
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        drawOk = true;
        post(this);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        drawOk = false;
    }

    @Override
    public void run() {
        if (drawOk) {
            Canvas c = holder.lockCanvas(null);
            if (c != null) {
                drawToCanvas(c);
                if(drawOk) {
                    holder.unlockCanvasAndPost(c);
                }
            }
        }
    }
}
