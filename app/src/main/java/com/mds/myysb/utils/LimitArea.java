package com.mds.myysb.utils;

import com.github.mikephil.charting.components.LimitLine;

public class LimitArea extends LimitLine {

    private float mLineWidth = 0f;
    public LimitArea(float limit) {
        super(limit);
        // TODO Auto-generated constructor stub
    }

    public LimitArea(float f, String string) {
        // TODO Auto-generated constructor stub
        super(f,string);
    }

    public void setLineWidth(float width) {
        mLineWidth = width;
    }

    /**
     * returns the width of limit line
     *
     * @return
     */
    public float getLineWidth() {
        return mLineWidth;
    }

}
