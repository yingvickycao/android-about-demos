package com.hades.example.android.widget.custom_view.Xfermode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.hades.example.android.R;

public class PorterDuffModeExampleView_Source extends View {
    private static final String TAG = PorterDuffModeExampleView_Source.class.getSimpleName();

    private final Paint paint = new Paint();
    // 和Google 保持一致
    private Bitmap destinationImage = BitmapFactory.decodeResource(getResources(), R.drawable.ic_composite_dst);
    private Bitmap sourceImage = BitmapFactory.decodeResource(getResources(), R.drawable.ic_composite_src);

    // 和Google 保持一致
//    private Bitmap destinationImage = BitmapFactory.decodeResource(getResources(), R.drawable.ic_composite_dst_2);
//    private Bitmap sourceImage = BitmapFactory.decodeResource(getResources(), R.drawable.ic_composite_src_2);

    private Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC);


    public PorterDuffModeExampleView_Source(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 不加Layer，背景色是黑色
        int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), paint);

        canvas.drawBitmap(destinationImage, 0, 0, paint);
        paint.setXfermode(xfermode);
        canvas.drawBitmap(sourceImage, 0, 0, paint);
        paint.setXfermode(null);

        canvas.restoreToCount(saveCount);
    }
}
