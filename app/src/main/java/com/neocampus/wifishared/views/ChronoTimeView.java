package com.neocampus.wifishared.views;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neocampus.wifishared.R;

import java.util.List;
import java.util.Locale;

/**
 * Created by Hirochi ☠ on 25/01/17.
 */

public class ChronoTimeView extends LinearLayout implements
        KeyboardView.OnKeyboardActionListener, View.OnClickListener {

    private Animation anim;
    private Keyboard keyboard;
    private KeyboardView keyboardView;
    private TextView hoursLeftView, hoursRightView, minuteLeftView, minuteRightView;
    private int hours = 0, minute = 0, state = 0;


    public ChronoTimeView(Context context) {
        super(context);
    }

    public ChronoTimeView(Context context,
                          AttributeSet attrs) {
        super(context, attrs);
    }

    public ChronoTimeView(Context context,
                          AttributeSet attrs,
                          int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        onInit();
    }

    private void onInit() {
        anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(50); //You can manage the blinking time with this parameter
        anim.setStartOffset(300);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);


        hoursLeftView = (TextView) findViewById(R.id.hours_left);
        hoursRightView = (TextView) findViewById(R.id.hours_rigth);
        minuteLeftView = (TextView) findViewById(R.id.minute_left);
        minuteRightView = (TextView) findViewById(R.id.minute_right);
        keyboard = new Keyboard(getContext(), R.xml.keyboard);
        keyboardView= (KeyboardView)findViewById(R.id.keyboardview);

        keyboardView.setKeyboard( keyboard );

        // Do not show the preview balloons
        keyboardView.setPreviewEnabled(false);

        // Install the key handler
        keyboardView.setOnKeyboardActionListener(this);

        hoursLeftView.setOnClickListener(this);
        hoursRightView.setOnClickListener(this);
        minuteLeftView.setOnClickListener(this);
        minuteRightView.setOnClickListener(this);

        setKeyboard(state);
    }

    private void setKeyboard(int state)
    {
        List<Keyboard.Key> keys = keyboard.getKeys();
        for (Keyboard.Key key : keys) {
            key.on = false;
            if (state == 0 && key.codes[0] <= 2)
                key.on = true;
            else if(state == 1 && key.codes[0] <= 3)
                key.on = true;
            else if(state == 2)
                key.on = true;
            else if(state == 3 && key.codes[0] <= 5)
                key.on = true;
            else if(state == 4)
                key.on = true;
        }

        switch (state) {
            case 0:
                hoursRightView.setActivated(false);
                hoursLeftView.setActivated(true);
                minuteLeftView.setActivated(false);
                minuteRightView.setActivated(false);

                hoursLeftView.startAnimation(anim);
                hoursRightView.clearAnimation();
                minuteLeftView.clearAnimation();
                minuteRightView.clearAnimation();
                break;
            case 1:
            case 2:
                hoursLeftView.setActivated(false);
                hoursRightView.setActivated(true);
                minuteLeftView.setActivated(false);
                minuteRightView.setActivated(false);

                hoursLeftView.clearAnimation();
                hoursRightView.startAnimation(anim);
                minuteLeftView.clearAnimation();
                minuteRightView.clearAnimation();
                break;
            case 3:
                hoursLeftView.setActivated(false);
                hoursRightView.setActivated(false);
                minuteLeftView.setActivated(true);
                minuteRightView.setActivated(false);

                hoursLeftView.clearAnimation();
                hoursRightView.clearAnimation();
                minuteLeftView.startAnimation(anim);
                minuteRightView.clearAnimation();
                break;
            default:
                hoursLeftView.setActivated(false);
                hoursRightView.setActivated(false);
                minuteLeftView.setActivated(false);
                minuteRightView.setActivated(true);

                hoursLeftView.clearAnimation();
                hoursRightView.clearAnimation();
                minuteLeftView.clearAnimation();
                minuteRightView.startAnimation(anim);
                break;
        }
    }

    public void setHours(int newHours)
    {
        hours = newHours;
        hoursLeftView.setText(String.format(Locale.FRANCE, "%d", newHours / 10));
        hoursRightView.setText(String.format(Locale.FRANCE, "%d", newHours % 10));
    }

    public void setMinute(int newMinute) {
        minute = newMinute;
        minuteLeftView.setText(String.format(Locale.FRANCE, "%d", newMinute / 10));
        minuteRightView.setText(String.format(Locale.FRANCE, "%d", newMinute % 10));
    }

    public int getHours() {
        return hours;
    }

    public int getMinute() {
        return minute;
    }

    @Override
    public void onPress(int primaryCode) {
    }

    @Override
    public void onRelease(int primaryCode) {
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        if (primaryCode != -1) {
            if (state == 0 && primaryCode <= 2) {
                state = primaryCode == 2 ? 1 : 2;
                setKeyboard(state);
                if(state == 1 && (hours % 10) > 3) {
                    setHours((10 * primaryCode) + 3);
                }
                else {
                    setHours((10 * primaryCode) + hours % 10);
                }
            } else if (state == 1 && primaryCode <= 3) {
                state = 3;
                setKeyboard(state);
                setHours(primaryCode + (hours / 10) * 10);
            } else if (state == 2) {
                state = 3;
                setKeyboard(state);
                setHours(primaryCode + (hours / 10) * 10);
            } else if (state == 3 && primaryCode <= 5) {
                state = 4;
                setKeyboard(state);
                setMinute( (10 * primaryCode) + minute % 10);
            }
            else if (state == 4) {
                state = 0;
                setKeyboard(state);
                setMinute(primaryCode + (minute / 10) * 10);
            }
        }
        else {
            state = 0;
            setHours(0);
            setMinute(0);
            setKeyboard(state);
        }
    }

    @Override
    public void onText(CharSequence text) {
    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeUp() {
    }

    @Override
    public void onClick(View v) {
        if(hoursLeftView == v )
        {
            state = 0;
            setKeyboard(state);
        }
        else if(hoursRightView == v )
        {
            state = (hours / 10) > 1 ? 1 : 2;
            setKeyboard(state);
        }
        else if(minuteLeftView == v )
        {
            state = 3;
            setKeyboard(state);
        }
        else {
            state = 4;
            setKeyboard(state);
        }
        keyboardView.postInvalidate();
    }
}
