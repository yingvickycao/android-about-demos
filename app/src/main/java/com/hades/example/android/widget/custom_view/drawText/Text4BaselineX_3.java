package com.hades.example.android.widget.custom_view.drawText;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.hades.example.android.R;

/**
 * View 的大小 是 800x400
 * 通过Top and bottom 得出的 Baseline Y
 */
public class Text4BaselineX_3 extends View {
    private static final String TAG = Text4BaselineX_3.class.getSimpleName();

    private Paint paint;
    private String text = getResources().getString(R.string.drawText_text);

    public Text4BaselineX_3(Context context, AttributeSet set) {
        super(context, set);
        paint = new Paint();

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(120);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.GRAY);
        paint.setColor(Color.BLACK);

        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        float center_X = getWidth() / 2;
        int center_Y = getHeight() / 2;

        float textWidth = getTextWidth(paint, text);
        float baseline_X = center_X - textWidth / 2;
        // baseline_X=151.5,text width=297.0
        Log.d(TAG, "onDraw:baseline_X=" + baseline_X + ",text width=" + textWidth);

        int baseline_Y = center_Y + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        canvas.drawText(text, baseline_X, baseline_Y, paint);
    }


    public static int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }
}