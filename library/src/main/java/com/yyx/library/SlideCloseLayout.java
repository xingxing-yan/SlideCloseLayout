package com.yyx.library;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2017/3/15.
 */

public class SlideCloseLayout extends FrameLayout {
    private enum Direction {
        UP_DOWN,
        LEFT_RIGHT,
        NONE
    }

    private Direction direction = Direction.NONE;
    private int previousY;
    private int previousX;
    private int baseLayoutPosition;
    private boolean isScrollingUp;
    private boolean isLocked = false;
    private LayoutScrollListener mScrollListener;
    private Drawable mBackground;

    public SlideCloseLayout(Context context) {
        this(context, null);
    }

    public SlideCloseLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideCloseLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setLayoutScrollListener(LayoutScrollListener listener) {
        mScrollListener = listener;
    }

    /**
     * 设置渐变的背景
     * @param drawable
     */
    public void setGradualBackground(Drawable drawable){
        this.mBackground = drawable;
    }

    public void lock() {
        isLocked = true;
    }

    public void unLock() {
        isLocked = false;
    }


    /**
     * 当垂直滑动时，拦截子view的滑动事件
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isLocked) {
            return false;
        } else {
            final int y = (int) ev.getRawY();
            final int x = (int) ev.getRawX();
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    previousX = x;
                    previousY = y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    int diffY = y - previousY;
                    int diffX = x - previousX;

                    if (Math.abs(diffX) + 50 < Math.abs(diffY)) {
                        return true;
                    }
                    break;
            }
            return false;
        }

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        if (!isLocked) {
            final int y = (int) ev.getRawY();
            final int x = (int) ev.getRawX();
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    previousX = x;
                    previousY = y;
                    baseLayoutPosition = (int) ev.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int diffY = y - previousY;
                    int diffX = x - previousX;

                    if (direction == Direction.NONE) {
                        if (Math.abs(diffX) > Math.abs(diffY)) {
                            direction = Direction.LEFT_RIGHT;
                        } else if (Math.abs(diffX) < Math.abs(diffY)) {
                            direction = Direction.UP_DOWN;
                        } else {
                            direction = Direction.NONE;
                        }
                    }

                    if (direction == Direction.UP_DOWN) {
                        isScrollingUp = diffY <= 0;
                        if (mBackground != null){
                            int alpha = (int) (255 * Math.abs(diffY * 1f)) / getHeight();
                            mBackground.setAlpha(255 - alpha);
                        }
                        this.setTranslationY(diffY);
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (direction == Direction.UP_DOWN) {
                        int height = this.getHeight();
                        if (Math.abs(getTranslationY()) > (height / 4)) {
                           scrollAnim();
                        } else {
                            ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(this, "translationY", this.getTranslationY(), 0);
                            positionAnimator.setDuration(100);
                            positionAnimator.start();
                            if (mBackground != null){
                                mBackground.setAlpha(255);
                            }
                        }
                        direction = Direction.NONE;
                        return true;
                    }

                    direction = Direction.NONE;

            }
            return true;
        }
        return false;
    }

    /**
     * 上下滑动的动画
     */
    private void scrollAnim(){
        final ObjectAnimator anim = ObjectAnimator.ofFloat(this, "translationY", getTranslationY(), isScrollingUp ? -getHeight() : getHeight());
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mBackground != null){
                    mBackground.setAlpha(0);
                }
                if (mScrollListener != null) {
                    mScrollListener.onLayoutClosed();
                }

            }
        });
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (mBackground != null){
                    int alpha = (int) (255 * Math.abs(getTranslationY() * 1f)) / getHeight();
                    mBackground.setAlpha(255 - alpha);
                }
            }
        });
        anim.setDuration(100);
        anim.start();

    }

    public interface LayoutScrollListener {
        //关闭布局
        void onLayoutClosed();
    }

}
