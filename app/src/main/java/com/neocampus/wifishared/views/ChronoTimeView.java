package com.neocampus.wifishared.views;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.view.View;
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
    private Keyboard keyboard;
    private KeyboardView keyboardView;
    private TextView hoursView, minuteView;
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
        hoursView = (TextView) findViewById(R.id.hours);
        minuteView = (TextView) findViewById(R.id.minute);
        keyboard = new Keyboard(getContext(), R.xml.keyboard);
        keyboardView= (KeyboardView)findViewById(R.id.keyboardview);

        keyboardView.setKeyboard( keyboard );

        // Do not show the preview balloons
        keyboardView.setPreviewEnabled(false);

        // Install the key handler
        keyboardView.setOnKeyboardActionListener(this);

        hoursView.setOnClickListener(this);
        minuteView.setOnClickListener(this);

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
        if(state < 3) {
            hoursView.setActivated(true);
            minuteView.setActivated(false);
        }
        else {
            hoursView.setActivated(false);
            minuteView.setActivated(true);
        }
    }

    public void setHours(int newHours)
    {
        hours = newHours;
        hoursView.setText(String.format(Locale.FRANCE, "%02d", newHours));
    }

    public void setMinute(int newMinute) {
        minute = newMinute;
        minuteView.setText(String.format(Locale.FRANCE,"%02d", newMinute));
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
                setHours(primaryCode);
            } else if (state == 1 && primaryCode <= 3) {
                state = 3;
                setKeyboard(state);
                setHours(hours * 10 + primaryCode);
            } else if (state == 2) {
                state = 3;
                setKeyboard(state);
                setHours(hours * 10 + primaryCode);
            } else if (state == 3 && primaryCode <= 5) {
                state = 4;
                setKeyboard(state);
                setMinute(primaryCode);
            }
            else if (state == 4) {
                state = 0;
                setKeyboard(state);
                setMinute(minute * 10 + primaryCode);
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
        if(hoursView == v)
        {
            state = 0;
            setKeyboard(state);
        }
        else {
            state = 3;
            setKeyboard(state);
        }
        keyboardView.postInvalidate();
    }
}
