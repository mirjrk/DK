package com.zesium.android.betting.ui.widgets;

import android.content.Context;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zesium.android.betting.R;

/**
 * Created by Ivan Panic_2 on 11/17/2016.
 */

public class SportBetRadioButton {
    private final TextView tv1;
    private final TextView tv2;
    private RadioButton rb;
    private final View view;
    private RelativeLayout llTexts;

    public SportBetRadioButton(Context context, int layout) {
        view = View.inflate(context, layout, null);
        rb = (RadioButton) view.findViewById(R.id.radioButton1);
        tv1 = (TextView) view.findViewById(R.id.textView1);
        tv2 = (TextView) view.findViewById(R.id.textView2);
        llTexts = (RelativeLayout) view.findViewById(R.id.rl_text_parent);
    }

    public View getView() {
        return view;
    }

    public void setText2(String text) {
        tv2.setText(text);
    }

    public String getText1() {
        return tv1.getText().toString();
    }

    public void setText1(String text) {
        tv1.setText(text);
    }

    public void setChecked(boolean isChecked) {
        rb.setChecked(isChecked);
    }

    public RadioButton getRb() {
        return rb;
    }

    public void setRb(RadioButton rb) {
        this.rb = rb;
    }

    public RelativeLayout getLlTexts() {
        return llTexts;
    }

    public void setLlTexts(RelativeLayout llTexts) {
        this.llTexts = llTexts;
    }
}
