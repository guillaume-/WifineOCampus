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
 * Created by Hirochi ☠ on 19/01/17.
 */

public class DataSurfaceView extends SurfaceView implements
        SurfaceHolder.Callback, Runnable, View.OnTouchListener {

    public enum DATA_TYPE
    {
        DATA_GIGA,
        DATA_MEGA
    }

    private SurfaceHolder holder;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    private Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    private Rect textBounds = new Rect();
    private DATA_TYPE dateType = DATA_TYPE.DATA_GIGA;
    private float dataValue = 0.300f;
    private int colorContent, colorEmpty;
    private float MinDataHeight = 0;
    private float MaxDataHeight = 0;

    private final float GIGA = 49.0f, MEGA = 999.0f;

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
        setOnTouchListener(this);

        float density = getContext().getResources().getDisplayMetrics().density;
        paintText.setTextAlign(Paint.Align.LEFT);
        paintText.setTextSize(30*density);
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
        canvas.drawColor(Color.WHITE);

        int SizeW = canvas.getWidth();
        int SizeH = canvas.getHeight();

        int HalfSizeW = SizeW / 2;
        int HalfSizeH = SizeH / 2;

        float ScaleStart = HalfSizeW - (SizeW / 2.5f);
        float ScaleEnd = HalfSizeW + (SizeW / 2.5f);
        float ScaleTop = HalfSizeH / 6.0f ;
        float ScaleBottom = SizeH - ScaleTop;
        float OvalSizeH = SizeH * 0.10f;

        RectF ovalTop = new RectF(ScaleStart , ScaleTop,
                ScaleEnd, ScaleTop + OvalSizeH);

        RectF ovalBottom = new RectF(ScaleStart , ScaleBottom,
                ScaleEnd, ScaleBottom - OvalSizeH);

        RectF rectCenter = new RectF(ScaleStart , ovalTop.centerY(),
                ScaleEnd, ovalBottom.centerY());
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(colorEmpty);
        canvas.drawRect(rectCenter, paint);

        paint.setColor(colorContent);

        float MinPosX = ScaleStart - 10;
        float MaxPosX = ScaleEnd - 10;
        MinDataHeight = ovalTop.centerY();
        MaxDataHeight = ovalBottom.centerY();

        String level;
        float Step, ScalePosY, Value;
        switch (dateType)
        {
            case DATA_GIGA:
                Step = (MaxDataHeight - MinDataHeight) / GIGA;
                Value = (float) Math.round(dataValue);
                ScalePosY = Step * Value;
                level = String.format(Locale.FRANCE, "%d Go", (int)Value);
                break;
            default:
                Step = (MaxDataHeight - MinDataHeight) / MEGA;
                Value = (float)  Math.round(dataValue);
                ScalePosY = Step * dataValue;
                level = String.format(Locale.FRANCE, "%d Mo", (int)(Value));
                break;
        }

        RectF ovalValue = new RectF(ScaleStart , MaxDataHeight - (ScalePosY - (OvalSizeH / 2.0f)),
                ScaleEnd, MaxDataHeight - (ScalePosY + (OvalSizeH / 2.0f)));

        RectF rectValue = new RectF(ScaleStart , ovalValue.centerY(),
                ScaleEnd, ovalBottom.centerY());

        canvas.drawRect(rectValue, paint);

        paint.setColor(colorContent);
        canvas.drawOval(ovalBottom, paint);

        paint.setColor(Color.WHITE);
        canvas.drawOval(ovalValue, paint);

        paint.setColor(colorEmpty);
        canvas.drawOval(ovalTop, paint);

        paint.setColor(colorContent);
        switch (dateType) {
            case DATA_MEGA:
                canvas.drawLine(MaxPosX + 40, MaxDataHeight - ScalePosY, MaxPosX + 10, MaxDataHeight - ScalePosY, paint);
                canvas.drawLine(MaxPosX + 40, MaxDataHeight - ScalePosY, MaxPosX + 40, MaxDataHeight - ScalePosY + 20, paint);
                break;
            default:
                canvas.drawLine(MinPosX - 20, MaxDataHeight - ScalePosY, MinPosX + 10, MaxDataHeight - ScalePosY, paint);
                canvas.drawLine(MinPosX - 20, MaxDataHeight - ScalePosY, MinPosX - 20, MaxDataHeight - ScalePosY + 20, paint);
                break;
        }

        paintText.setColor(colorEmpty);

        paintText.getTextBounds(level, 0, level.length(), textBounds);
        float x = (HalfSizeW - (textBounds.width() / 2f)) - textBounds.left;
        float y = (HalfSizeH + (textBounds.height() / 2f)) - textBounds.bottom;
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

    public void setDateType(DATA_TYPE dateType) {
        this.dateType = dateType;
    }

    public DATA_TYPE getDateType() {
        return dateType;
    }

    public void setDataValue(float dataValue) {
        this.dataValue = dataValue;
        post(this);
    }

    public float getDataValue() {
        return dataValue;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_MOVE:
                if(event.getY() > MinDataHeight
                        && event.getY() < MaxDataHeight) {
                    float ReplaceY = event.getY() - MinDataHeight;
                    float data = ReplaceY / (MaxDataHeight - MinDataHeight);
                    switch (dateType)
                    {
                        case DATA_GIGA:
                            dataValue = GIGA - (GIGA * data);
                            break;
                        default:
                            dataValue = MEGA - (MEGA * data);
                            break;
                    }

                    post(this);
                }

                break;
        }
        return true;
    }
}
