package com.neocampus.wifishared.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.neocampus.wifishared.R;

import java.util.List;

/**
 * Created by Hirochi ☠ on 25/01/17.
 */

public class KeyboardViews extends KeyboardView {

    private float density;
    private int colorEnable, colorDisable;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

    public KeyboardViews(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyboardViews(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        density = getContext().getResources().getDisplayMetrics().density;
        paint.setTextSize(25.0f * density);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setShadowLayer(2.0f, 0.0f, 2.0f, Color.BLACK);
        colorEnable = ContextCompat.
                getColor(getContext(), R.color.colorBatterieText);
        colorDisable = ContextCompat.
                getColor(getContext(), R.color.colorGrayTransparent);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDraw(Canvas canvas) {
        List<Keyboard.Key> keys = getKeyboard().getKeys();
        for(Keyboard.Key key: keys) {
            if(key.on) {
                if (key.pressed) {
                    canvas.drawCircle(key.x + (key.width / 2),
                            key.y + ((key.height - 30) / 2), 25.0f * density, paint);
                }
                if (key.icon != null) {
                    int width = key.icon.getIntrinsicWidth();
                    int height = key.icon.getIntrinsicWidth();
                    int left = key.x + ((key.width / 2) - (width / 2));
                    int top = key.y + ((key.height / 2) - (height / 2));
                    key.icon.setBounds(left, top, left + width, top + height);
                    key.icon.draw(canvas);
                }
                paint.setColor(colorEnable);
            }
            else {
                paint.setColor(colorDisable);
            }
            if (key.label != null) {
                canvas.drawText(key.label.toString(), key.x + (key.width / 2), key.y + (key.height / 2), paint);
            }
        }
    }

}
