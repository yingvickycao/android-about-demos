package com.hades.example.android.widget.custom_view.ball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hades.example.android.R;

/**
 * 小球跟着手指动
 * 难点：onTouchEvent 触摸事件；每隔0.1秒重新绘制一次
 */
public class FingerMovedBallSurfaceView extends SurfaceView {
    private static final String TAG = "FingerMovedBallSurfaceView";

    private float currentX = getResources().getDimension(R.dimen.size_15);
    private float currentY = getResources().getDimension(R.dimen.size_15);
    private float radius = getResources().getDimension(R.dimen.size_10);

    private int width = -1;
    private int height = -1;

    Paint paint;

    private Canvas canvas;
    private Thread thread;
    private SurfaceHolder surfaceHolder;

    public FingerMovedBallSurfaceView(Context context) {
        super(context);
        initViews();
    }

    public FingerMovedBallSurfaceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public FingerMovedBallSurfaceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    public FingerMovedBallSurfaceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews();
    }

    private void initViews() {
        if (null == paint) {
            // 创建画笔，颜色为红色
            paint = new Paint();
            paint.setColor(Color.GREEN);
            // 设置消除锯齿
            paint.setAntiAlias(true);
        }

        if (null == surfaceHolder) {
            surfaceHolder = getHolder();
            surfaceHolder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(@NonNull SurfaceHolder holder) {
                    Log.d(TAG, "surfaceCreated: SurfaceHolder@" + holder.hashCode() + ",width:" + width + ",height:" + height);
                    thread.start();
                }

                @Override
                public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int p_width, int p_height) {
                    Log.d(TAG, "surfaceChanged:width:" + p_width + ",height:" + p_height);
                    // 获取屏幕的宽和高
                    if (-1 == width) {
                        width = p_width;
                    }
                    if (-1 == height) {
                        height = p_height;
                    }
                }

                @Override
                public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                    Log.d(TAG, "surfaceDestroyed: SurfaceHolder@" + hashCode());

                    if (null != thread) {
                        // 停止绘制线程
                        thread.interrupt();
                        thread = null;
                    }
                }
            });
        }

        if (null == thread) {
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (null != thread && !thread.isInterrupted()) {
                        try {
                            myDraw();
                            Thread.sleep(100);
                        } catch (Exception exception) {
                            if (null != exception.getMessage()) {
                                Log.d(TAG, "run: " + exception.getMessage());
                            }
                        }
                    }
                    Log.d(TAG, "run:thread is interrupt ");
                }
            });
        }
    }

    private void initBallStartPosition() {
        if (-1 == currentX) {
            // onDraw 中获取宽度和高度，并1/2 长度作为小球的开始位置
            currentX = (float) getWidth() / 2;
        }
        if (-1 == currentY) {
            currentY = (float) getHeight() / 2;
        }
    }

    protected void myDraw() {
        //  获取canvas实例
        canvas = surfaceHolder.lockCanvas();
        Log.d(TAG, "myDraw: canvas@" + canvas.hashCode());

        // 设置画布的颜色
        canvas.drawColor(getResources().getColor(R.color.paint_bg, getContext().getTheme()));

        // Log.d(TAG, "myDraw: width=" + getWidth() + ",height=" + getHeight());
        // initBallStartPosition();

        reviseXY();

        // 绘制圆形小球
        canvas.drawCircle(currentX, currentY, radius, paint);

        //将画好的画布提交
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Log.d(TAG, "onTouchEvent: " + event.getAction());
        Log.d(TAG, "onTouchEvent: width=" + getWidth() + ",height=" + getHeight());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onTouchEvent: Action Down");
                // 记录触屏坐标，并把保存
                currentX = event.getX();
                currentY = event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "onTouchEvent: Action Move");
                // 记录触屏坐标，并把保存
                currentX = event.getX();
                currentY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "onTouchEvent: Action Up");
                // 记录触屏坐标，并把保存
                currentX = event.getX();
                currentY = event.getY();
                break;
        }
        return true;
    }

    // 纠正XY，不让小球跑到屏幕范围外
    private void reviseXY() {
        if (currentX <= radius) {
            currentX = radius;
        } else if (currentX >= (width - radius)) {
            currentX = width - radius;
        }

        if (currentY <= radius) {
            currentY = radius;
        } else if (currentY >= (height - radius)) {
            currentY = height - radius;
        }
    }
}
