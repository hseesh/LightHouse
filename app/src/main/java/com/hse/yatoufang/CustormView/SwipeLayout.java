package com.hse.yatoufang.CustormView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class SwipeLayout extends FrameLayout {
    private View contentView;
    private View deleteView;
    private int contentViewWidth;
    private int deleteViewWidth;
    private int contentViewHight;
    private ViewDragHelper viewDragHelper;
    private float x, y;

    public SwipeLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public SwipeLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SwipeLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = getChildAt(0);
        deleteView = getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        contentViewHight = contentView.getMeasuredHeight();
        contentViewWidth = contentView.getMeasuredWidth();
        deleteViewWidth = deleteView.getMeasuredWidth();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        contentView.layout(0, 0, contentViewWidth, contentViewHight);
        deleteView.layout(contentViewWidth, 0, contentViewWidth + deleteViewWidth, contentViewHight);
    }

    private void init() {
        viewDragHelper = ViewDragHelper.create(this, callback);

    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        return viewDragHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(event.getX() - x) > Math.abs(event.getY() - y)) {
                    requestDisallowInterceptTouchEvent(true);
                } else {
                    hideItem();
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == contentView || child == deleteView;
        }

        /**
         * Called when the drag state changes. See the <code>STATE_*</code> constants
         * for more information.
         *


         @Override public void onViewDragStateChanged(int state) {
         super.onViewDragStateChanged(state);
         }

         /**
          * Called when the child view is no longer being actively dragged.
          * The fling velocity is also supplied, if relevant. The velocity values may
          * be clamped to system minimums or maximums.

          * @param releasedChild The captured child view now being released
         * @param xvel          X velocity of the pointer as it left the screen in pixels per second.
         * @param yvel          Y velocity of the pointer as it left the screen in pixels per second.
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (contentView.getLeft() < -deleteViewWidth / 2) {
                viewDragHelper.smoothSlideViewTo(contentView, -deleteViewWidth, 0);//关闭
                ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
            } else {
                hideItem();
            }
        }


        /**
         * Return the magnitude of a draggable child view's horizontal range of motion in pixels.
         * This method should return 0 for views that cannot move horizontally.
         *
         * @param child Child view to check
         * @return range of horizontal motion in pixels
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return deleteViewWidth;
        }

        /**
         * Return the magnitude of a draggable child view's vertical range of motion in pixels.
         * This method should return 0 for views that cannot move vertically.
         *
         * @param child Child view to check
         * @return range of vertical motion in pixels
         */
        @Override
        public int getViewVerticalDragRange(View child) {
            return super.getViewVerticalDragRange(child);
        }

        /**
         * Restrict the motion of the dragged child view along the horizontal axis.
         * The default implementation does not allow horizontal motion; the extending
         * class must override this method and provide the desired clamping.
         *
         * @param child Child view being dragged
         * @param left  Attempted motion along the X axis
         * @param dx    Proposed change in position for left
         * @return The new clamped position for left
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {//控制滑动的边界
            if (child == contentView) {
                if (left > 0) left = 0;
                if (left < -deleteViewWidth) {
                    left = -deleteViewWidth;
                }
            } else if (child == deleteView) {
                if (left > contentViewWidth) {
                    left = contentViewWidth;
                }
                if (left < (contentViewWidth - deleteViewWidth)) {
                    left = contentViewWidth - deleteViewWidth;
                }
            }
            return left;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {//控制具体的滑动距离
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if (changedView == contentView) {
                deleteView.layout(deleteView.getLeft() + dx, deleteView.getTop() + dy, deleteView.getRight() + dx, deleteView.getBottom() + dy);
            } else if (changedView == deleteView) {
                contentView.layout(contentView.getLeft() + dx, contentView.getTop() + dy, contentView.getRight() + dx, contentView.getBottom() + dy);
            }

        }
    };

    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
        }
    }

    public void hideItem() {
        viewDragHelper.smoothSlideViewTo(contentView, 0, contentView.getTop());
        ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
    }


}
