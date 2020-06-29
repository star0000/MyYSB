package com.mds.myysb.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;

public class YAxisRendererArea extends YAxisRenderer {

    public YAxisRendererArea(ViewPortHandler viewPortHandler, YAxis yAxis,
                             Transformer trans) {
        super(viewPortHandler, yAxis, trans);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void renderLimitLines(Canvas c) {

        List<LimitLine> limitLines = mYAxis.getLimitLines();

        if (limitLines == null || limitLines.size() <= 0)
            return;

        float[] pts = new float[2];
        float[] pts2 = new float[2];
        Path limitLinePath = new Path();
        float left,top,right,bottom;
        for (int i = 0; i < limitLines.size(); i++) {

            LimitLine l = limitLines.get(i);

            mLimitLinePaint.setStyle(Paint.Style.FILL);
            mLimitLinePaint.setColor(l.getLineColor());
            mLimitLinePaint.setPathEffect(l.getDashPathEffect());

            pts[1] = l.getLimit();
            pts2[1] = (float)(l.getLineWidth());

            mTrans.pointValuesToPixel(pts);
            mTrans.pointValuesToPixel(pts2);
            limitLinePath.moveTo(mViewPortHandler.contentLeft(), pts[1]);
            left = mViewPortHandler.contentLeft();
            right = mViewPortHandler.contentRight();
            bottom = pts[1];
            top = pts2[1];
            limitLinePath.addRect(left, top, right, bottom, Path.Direction.CCW);
            c.drawPath(limitLinePath, mLimitLinePaint);
            limitLinePath.reset();
            String label = l.getLabel();
            if (label != null && !label.equals("")) {

                float xOffset = Utils.convertDpToPixel(4f);
                float yOffset = l.getLineWidth() + Utils.calcTextHeight(mLimitLinePaint, label) / 2f;

                mLimitLinePaint.setStyle(l.getTextStyle());
                mLimitLinePaint.setPathEffect(null);
                mLimitLinePaint.setColor(l.getTextColor());
                mLimitLinePaint.setStrokeWidth(0.5f);
                mLimitLinePaint.setTextSize(l.getTextSize());

                if (l.getLabelPosition() == LimitLine.LimitLabelPosition.RIGHT_TOP) {

                    mLimitLinePaint.setTextAlign(Paint.Align.RIGHT);
                    c.drawText(label, mViewPortHandler.contentRight() - xOffset, pts[1] - yOffset, mLimitLinePaint);

                } else {
                    mLimitLinePaint.setTextAlign(Paint.Align.LEFT);
                    c.drawText(label, mViewPortHandler.offsetLeft() + xOffset, pts[1] - yOffset, mLimitLinePaint);
                }
            }
        }
    }

}
