package com.tzx.autoscrollverticalbannerview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by Tanzhenxing
 * Date: 2021/2/19 3:08 下午
 * Description: 实现垂直自动滚动效果
 */
public class AutoScrollVerticalBannerView extends LinearLayout implements Runnable {

    private float mBannerHeight = 0;
    private int mGap = 4000;
    private int mAnimDuration = 1000;
    private Adapter mAdapter;

    private int mPosition;

    private boolean isStarted;
    /**
     * 当前列表总条数大于界面展示可滑动的条数（count>showCount）
     * 是否可以滚动
     */
    private boolean canScroll;

    public AutoScrollVerticalBannerView(Context context) {
        this(context, null);
    }

    public AutoScrollVerticalBannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoScrollVerticalBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public void setGap(int mGap) {
        this.mGap = mGap;
    }

    public void setAnimDuration(int mAnimDuration) {
        this.mAnimDuration = mAnimDuration;
    }

    /**
     * bannerHeight banner的高度
     * animDuration 每次切换动画时间
     * gap banner切换时间
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        setOrientation(VERTICAL);
    }

    /**
     * 设置banner的数据
     */
    public void setAdapter(Adapter adapter) {
        if (adapter == null) {
            throw new RuntimeException("adapter must not be null");
        }
        if (mAdapter != null) {
            throw new RuntimeException("you have already set an Adapter");
        }
        this.mAdapter = adapter;
        mAdapter.setOnDataChangedListener(this);
        setupAdapter();
    }

    public boolean start() {
        if (mAdapter == null) {
            throw new RuntimeException("you must call setAdapter() before start");
        }

        if (!isStarted && canScroll) {
            isStarted = true;
            postDelayed(mRunnable, mGap);
            return true;
        }
        return false;
    }

    public void stop() {
        removeCallbacks(mRunnable);
        isStarted = false;
    }


    private void setupAdapter() {
        removeAllViews();
        canScroll = false;
        if (mAdapter.getCount() == 0
                || mAdapter.getShowBannerCount() <= 0
                || mAdapter.getScrollBannerCount() <= 0
                || (mAdapter.getScrollBannerCount() > mAdapter.getShowBannerCount())) {
            return;
        }
        if (mAdapter.getCount() <= mAdapter.getShowBannerCount()) {
            for (int i = 0; i < mAdapter.getCount(); i++) {
                View view = mAdapter.getView(this);
                mAdapter.bindView(view, i);
                addView(view);
            }
        } else if (mAdapter.getCount() == 1) {
            View view = mAdapter.getView(this);
            mAdapter.bindView(view, 0);
            addView(view);
        } else {
            canScroll = true;
            for (int i = 0; i < mAdapter.getShowBannerCount() + mAdapter.getScrollBannerCount(); i++) {
                View view = mAdapter.getView(this);
                mAdapter.bindView(view, i);
                addView(view);
            }
            mPosition = getChildCount() - 1;
            isStarted = false;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() > 0 && getChildAt(0).getMeasuredHeight() > 0) {
            int measuredHeight = getChildAt(0).getMeasuredHeight();
            this.mBannerHeight = measuredHeight;
            getLayoutParams().height = measuredHeight * Math.min(mAdapter.getShowBannerCount(), mAdapter.getCount());
        }
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).getLayoutParams().height = (int) mBannerHeight;
        }
    }

    @Override
    public void run() {
        setupAdapter();
    }


    private void performSwitch() {
        ObjectAnimator[] animators = new ObjectAnimator[getChildCount()];
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            float height = this.mBannerHeight * mAdapter.getScrollBannerCount();
            animators[i] = ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY() - height);
        }

        AnimatorSet set = new AnimatorSet();
        set.playTogether(animators);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                for (int i = 0; i < mAdapter.getScrollBannerCount(); i++) {
                    View removedView = getChildAt(0);
                    mPosition++;
                    mAdapter.bindView(removedView, mPosition);
                    removeViewAt(0);
                    addView(removedView);
                }
                for (int i = 0; i < getChildCount(); i++) {
                    getChildAt(i).setTranslationY(0);
                }
                //避免无限循环引起的溢出问题
                mPosition = mPosition % mAdapter.getCount();
            }

        });
        set.setDuration(mAnimDuration);
        set.start();
    }

    private AnimRunnable mRunnable = new AnimRunnable();

    private class AnimRunnable implements Runnable {
        @Override
        public void run() {
            performSwitch();
            postDelayed(this, mGap);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    interface OnDataChangedListener {
        void onChanged();
    }

    public static abstract class Adapter<T> {
        private int showBannerCount = 1;
        private int scrollBannerCount = 1;
        private List<T> mDatas;
        private Runnable mOnDataChangedListener;
        public Adapter(List<T> datas) {
            mDatas = datas;
        }
        /**
         * 设置banner填充的数据
         */
        public void setData(List<T> datas) {
            this.mDatas = datas;
            notifyDataChanged();
        }

        /**
         * @param listener  设置数据更新回调
         */
        void setOnDataChangedListener(Runnable listener) {
            mOnDataChangedListener = listener;
        }

        public int getCount() {
            return mDatas == null ? 0 : mDatas.size();
        }

        void notifyDataChanged() {
            mOnDataChangedListener.run();
        }

        public T getItem(int position) {
            return mDatas.get(position);
        }
        /**
         * 设置banner的样式
         */
        public abstract View getView(AutoScrollVerticalBannerView parent);

        /**
         * 设置banner的数据
         */
        public abstract void bindView(View view, T data);

        public void bindView(View view, int position) {
            bindView(view, getItem(position % getCount()));
        }

        public void setShowBannerCount(int showBannerCount) {
            this.showBannerCount = showBannerCount;
        }

        public void setScrollBannerCount(int scrollBannerCount) {
            this.scrollBannerCount = scrollBannerCount;
        }

        public int getShowBannerCount() {
            return showBannerCount;
        }

        public int getScrollBannerCount() {
            return scrollBannerCount;
        }
    }
}
















