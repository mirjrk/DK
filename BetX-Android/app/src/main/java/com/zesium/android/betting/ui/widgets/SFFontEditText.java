package com.zesium.android.betting.ui.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

/**
 * Created by Ivan Panic_2 on 6/28/2016.
 */
public class SFFontEditText extends AppCompatEditText {

    public SFFontEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // init();
    }

    public SFFontEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        // init();
    }

    public SFFontEditText(Context context) {
        super(context);
        //  init();
    }

    @Override
    public void setTypeface(Typeface tf, int style) {
        if (style == Typeface.BOLD) {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/sanfranciscotextbold.TTF"));
        } else if (style == Typeface.ITALIC) {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/sanfranciscotextitalic.TTF"));
        } else {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/sanfranciscotextregular.TTF"));
        }
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font.ttf");
            setTypeface(tf);
        }
    }

}