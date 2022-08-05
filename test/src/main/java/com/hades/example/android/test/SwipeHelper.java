package com.hades.example.android.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public abstract class SwipeHelper extends ItemTouchHelper.SimpleCallback {
    private static final String TAG = "SwipeHelper";

    int buttonWidth;
    private RecyclerView recyclerView;
    private List<MyButton> buttonList;
    private GestureDetector gestureDetector;
    private int swipePosition = -1;
    private float swipeThreshold = 0.5f;
    private Map<Integer, List<MyButton>> buttonBuffer;
    private Queue<Integer> removeQueue;
    private Rect tRect = new Rect();

    public SwipeHelper(Context context, RecyclerView recyclerView, int buttonWidth) {
        super(0, ItemTouchHelper.LEFT);
        this.recyclerView = recyclerView;
        this.buttonList = new ArrayList<>();
        this.gestureDetector = new GestureDetector(context, gestureListener);
        this.recyclerView.setOnTouchListener(onTouchListener);
        this.buttonBuffer = new HashMap<>();
        this.buttonWidth = buttonWidth;

        removeQueue = new LinkedList<Integer>() {

            @Override
            public boolean add(Integer integer) {
                if (contains(integer))
                    return false;
                else
                    return super.add(integer);
            }
        };

        attachSwipe();
    }

    private void attachSwipe() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(this);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private synchronized void recoverSwipedItem() {
        while (!removeQueue.isEmpty()) {
            int pos = removeQueue.poll();
            if (pos > -1)
                recyclerView.getAdapter().notifyItemChanged(pos);
        }
    }


    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int pos = viewHolder.getAdapterPosition();
        if (swipePosition != pos) {
            removeQueue.add(swipePosition);
        }
        swipePosition = pos;
        if (buttonBuffer.containsKey(swipePosition)) {
            buttonList = buttonBuffer.get(swipePosition);
        } else {
            buttonList.clear();
        }
        buttonBuffer.clear();
        swipeThreshold = 0.5f * buttonList.size() * buttonWidth;
        recoverSwipedItem();
    }

    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        return swipeThreshold;
    }

    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        return 0.1f * defaultValue;
    }

    @Override
    public float getSwipeVelocityThreshold(float defaultValue) {
        return 5.0f * defaultValue;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        int pos = viewHolder.getAdapterPosition();
        float translationX = dX;
        View itemView = viewHolder.itemView;
        if (pos < 0) {
            swipePosition = pos;
            return;
        }
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

            if (dX < 0) {

                List<MyButton> buffer = new ArrayList<>();
                if (!buttonBuffer.containsKey(pos)) {
                    instantiateMyButton(viewHolder, buffer);
                    buttonBuffer.put(pos, buffer);
                } else {
                    buffer = buttonBuffer.get(pos);
                }
                translationX = dX * buffer.size() * buttonWidth / itemView.getWidth();
                drawButton(c, itemView, buffer, pos, translationX);
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, translationX, dY, actionState, isCurrentlyActive);
    }

    private void drawButton(Canvas c, View itemView, List<MyButton> buffer, int pos, float translationX) {
        float right = itemView.getRight();
        float dButtonWidth = -1 * translationX / buffer.size();
        for (MyButton button : buffer) {
            float left = right - dButtonWidth;
            button.onDraw(c, new RectF(left, itemView.getTop(), right, itemView.getBottom()), pos);
            right = left;
        }
    }

    public abstract void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List<MyButton> buffer);

    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            for (MyButton button : buttonList) {
                if (button.onClick(e.getX(), e.getY())) {
                    break;
                }
            }
            return true;
        }
    };

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Log.d(TAG, "onTouch: motionEvent:" + motionEvent);
            if (swipePosition < 0) {
                return false;
            }

            RecyclerView.ViewHolder swipeViewHolder = recyclerView.findViewHolderForAdapterPosition(swipePosition);

            if (swipeViewHolder != null) {
                View swipedItem = swipeViewHolder.itemView;

                tRect.top = 0;
                tRect.right = 0;
                tRect.bottom = 0;
                tRect.left = 0;

                swipedItem.getGlobalVisibleRect(tRect);

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN || motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    if (tRect.top < motionEvent.getRawY() && tRect.bottom > motionEvent.getRawY()) {
                        Log.d(TAG, "onTouch:Rect.top=" + tRect.top + ",motionEvent.getRawY=" + motionEvent.getRawY() + ",Rect.bottom=" + tRect.bottom);
                        gestureDetector.onTouchEvent(motionEvent);
                    } else {
                        removeQueue.add(swipePosition);
                        swipePosition = -1;
                        recoverSwipedItem();
                    }
                }
                return false;
            }
            return false;
        }
    };
}