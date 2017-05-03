package com.app.viewdraghelpertest;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by 知らないのセカイ on 2017/5/3.
 */

public class MyViewDragHelper extends LinearLayout {
    private ViewDragHelper myDrag;
    public MyViewDragHelper(Context context) {
        super(context);
    }
    public MyViewDragHelper(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyViewDragHelper(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        myDrag=ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child==contentView;
            }
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {

                final int newleft = Math.min(Math.max(left, -dragwidth), 0);
                return newleft;
            }
            private int draggx;

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                draggx=left;
                if (Btn_delete.getVisibility() == View.GONE) {
                    Btn_delete.setVisibility(VISIBLE);
                }
                if (changedView == contentView) {
                    Btn_delete.offsetLeftAndRight(dx);
                    invalidate();
                }

            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);

                Boolean settleOpenTo=false;
                if (xvel<-2.0){
                    settleOpenTo=true;
                }else if(draggx<=-dragwidth/2){
                    settleOpenTo=true;
                }
                final int settleDestx=settleOpenTo?-dragwidth:0;
                myDrag.smoothSlideViewTo(contentView, settleDestx, 0);

                ViewCompat.postInvalidateOnAnimation(MyViewDragHelper.this);


            }

        });
                }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return myDrag.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        myDrag.processTouchEvent(event);
        return true;
    }
    private TextView contentView;
    private Button Btn_delete;
    private int dragwidth;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = (TextView) getChildAt(0);
        Btn_delete = (Button) getChildAt(1);
        Btn_delete.setVisibility(View.GONE);

    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (myDrag.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        dragwidth=Btn_delete.getMeasuredWidth();
    }
}
