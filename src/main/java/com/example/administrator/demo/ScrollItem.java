package com.example.administrator.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * ListView中可以水平滚动的item
 */
public class ScrollItem extends HorizontalScrollView{


    public ScrollItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ScrollItem(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        MainActivity.instance.setmTouchView(this);
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {

        if (MainActivity.instance.getmTouchView() == this) {
            MainActivity.instance.onScrollChanged(l,t,oldl,oldt);
        } else
            super.onScrollChanged(l, t, oldl, oldt);
    }
}
