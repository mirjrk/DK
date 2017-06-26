package com.zesium.android.betting.ui.widgets;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Ivan Panic_2 on 11/22/2016.
 */

public class SmoothScrollRecyclerView extends RecyclerView {
    private Context context;

    public SmoothScrollRecyclerView(Context context) {
        super(context);
        this.context = context;
    }

    public SmoothScrollRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmoothScrollRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {

        velocityY *= 0.7;
        // velocityX *= 0.7; for Horizontal recycler view. comment velocityY line not require for Horizontal Mode.

        return super.fling(velocityX, velocityY);
    }
}
