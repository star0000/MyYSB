package com.mds.myysb.utils;

import com.github.mikephil.charting.utils.ValueFormatter;

import java.text.DecimalFormat;

public class MyValueFormatter implements ValueFormatter {

    private DecimalFormat mFormat;

    public MyValueFormatter(int digits) {
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < digits; i++) {
            if (i == 0)
                b.append(".");
            b.append("0");
        }
        mFormat = new DecimalFormat("###,###,###,##0" + b.toString());
    }
    @Override
    public String getFormattedValue(float value) {
        // TODO Auto-generated method stub
        return mFormat.format(value);
    }

}
