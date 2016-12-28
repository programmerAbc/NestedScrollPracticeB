package com.practice;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * Created by usera on 2016/12/27.
 */

public class MyLinearLayout extends LinearLayout {
    private static final String TAG = MyLinearLayout.class.getSimpleName();
    View topView;
    View buttonBar;
    View searchBar;
    View recyclerViewContainer;
    ImageButton qrcodeBtn;
    ImageButton personBtn;
    ImageButton checkDownloadBtn;
    ImageButton checkWifiBtn;
    ImageView topIv;
    RecyclerView recyclerView;
    int nestedScrollHeight = -1;
    int searchBarMeasuredWidth = -1;
    int searchBarTopMargin = -1;
    int searchBarBottomMargin = -1;
    Scroller scroller;

    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        scroller = new Scroller(getContext());
        topView = findViewById(R.id.topView);
        searchBar = findViewById(R.id.searchBar);
        buttonBar = findViewById(R.id.buttonBar);
        recyclerView = (RecyclerView) findViewById(R.id.scrollView);
        recyclerViewContainer = findViewById(R.id.scrollViewContainer);
        topIv = (ImageView) findViewById(R.id.topIv);
        personBtn = (ImageButton) findViewById(R.id.personBtn);
        checkDownloadBtn = (ImageButton) findViewById(R.id.checkDownloadBtn);
        qrcodeBtn = (ImageButton) findViewById(R.id.qrcodeBtn);
        checkWifiBtn = (ImageButton) findViewById(R.id.checkWifiBtn);
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(0, scroller.getCurrY());
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(TAG, "onMeasure: ===START===");
        if (searchBarMeasuredWidth == -1) {
            searchBarMeasuredWidth = searchBar.getMeasuredWidth();
        }
        Log.e(TAG, "onMeasure: recyclerViewContainer 1st measure:" + recyclerViewContainer.getMeasuredHeight());
        FrameLayout.LayoutParams searchBarLayoutParams = (FrameLayout.LayoutParams) searchBar.getLayoutParams();
        if (nestedScrollHeight < 0) {
            nestedScrollHeight = topView.getMeasuredHeight() - searchBar.getMeasuredHeight() - searchBarLayoutParams.topMargin / 2 - searchBarLayoutParams.bottomMargin / 2;
        }
        if (searchBarTopMargin < 0) {
            searchBarTopMargin = ((FrameLayout.LayoutParams) searchBar.getLayoutParams()).topMargin;
        }
        if (searchBarBottomMargin < 0) {
            searchBarBottomMargin = ((FrameLayout.LayoutParams) searchBar.getLayoutParams()).bottomMargin;
        }
        Log.e(TAG, "onMeasure: searchBar measured height" + searchBar.getMeasuredHeight());
        LinearLayout.LayoutParams scrollViewContainerLayoutParams = (LayoutParams) recyclerViewContainer.getLayoutParams();
        Log.e(TAG, "onMeasure: a=" + getMeasuredHeight() + "b=" + searchBar.getMeasuredHeight() + "c=" + searchBarLayoutParams.topMargin + "d=" + searchBarLayoutParams.bottomMargin);
        scrollViewContainerLayoutParams.height = getMeasuredHeight() - searchBar.getMeasuredHeight() - searchBarLayoutParams.topMargin - searchBarLayoutParams.bottomMargin;
        Log.e(TAG, "onMeasure: recyclerViewContainer layoutparams height" + scrollViewContainerLayoutParams.height);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(TAG, "onMeasure: recyclerViewContainer 2nd measure:" + recyclerViewContainer.getMeasuredHeight());
        Log.e(TAG, "onMeasure: ===END===");
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if ((dy > 0 && getScrollY() < nestedScrollHeight) || dy < 0 && getScrollY() > 0 && !ViewCompat.canScrollVertically(target, -1)) {
            consumed[1] = dy;
            scrollBy(0, dy);
        }
    }


    @Override
    public void scrollTo(int x, int y) {
        y = y < 0 ? 0 : y;
        y = y > nestedScrollHeight ? nestedScrollHeight : y;
        float scrollFactor = (float) y / nestedScrollHeight;
        buttonBar.setTranslationY(y);
        checkWifiBtn.setAlpha(1 - scrollFactor);
        topIv.setAlpha(1 - scrollFactor);
        topIv.setScaleY(1 + scrollFactor);
        topIv.setScaleX(1 + scrollFactor);
        Log.e(TAG, "scrollTo: a= " + personBtn.getMeasuredWidth() + " b= " + checkDownloadBtn.getMeasuredWidth() + " c= " + scrollFactor);
        Log.e(TAG, "scrollTo: width=" + (searchBarMeasuredWidth - (int) ((personBtn.getMeasuredWidth() + ((RelativeLayout.LayoutParams) personBtn.getLayoutParams()).rightMargin + checkDownloadBtn.getMeasuredWidth() + ((RelativeLayout.LayoutParams) checkDownloadBtn.getLayoutParams()).leftMargin) * scrollFactor)));
        searchBar.getLayoutParams().width = searchBarMeasuredWidth - (int) ((personBtn.getMeasuredWidth() + ((RelativeLayout.LayoutParams) personBtn.getLayoutParams()).rightMargin + checkDownloadBtn.getMeasuredWidth() + ((RelativeLayout.LayoutParams) checkDownloadBtn.getLayoutParams()).leftMargin) * scrollFactor);
        ((FrameLayout.LayoutParams) searchBar.getLayoutParams()).topMargin = searchBarTopMargin - (int) (searchBarTopMargin / 2 * scrollFactor);
        ((FrameLayout.LayoutParams) searchBar.getLayoutParams()).bottomMargin = searchBarBottomMargin - (int) (searchBarBottomMargin / 2 * scrollFactor);
        searchBar.requestLayout();
        super.scrollTo(x, y);
    }

    @Override
    public void onStopNestedScroll(View child) {
        super.onStopNestedScroll(child);
        if (getScrollY() > 0 && getScrollY() < nestedScrollHeight) {
            if (getScrollY() > nestedScrollHeight / 2) {
                scroller.startScroll(0, getScrollY(), 0, nestedScrollHeight - getScrollY(), 300);
                invalidate();
            } else {
                scroller.startScroll(0, getScrollY(), 0, -getScrollY(), 300);
                invalidate();
            }
        }
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (getScrollY() > 0 && getScrollY() < nestedScrollHeight) {
            return true;
        } else {
            return super.onNestedPreFling(target, velocityX, velocityY);
        }
    }
}
