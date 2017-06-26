package com.zesium.android.betting.ui.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;

/**
 * Created by Ivan Panic_2 on 6/28/2016.
 */
public class SFFontTextView extends AppCompatTextView {

    public SFFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SFFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SFFontTextView(Context context) {
        super(context);
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
}