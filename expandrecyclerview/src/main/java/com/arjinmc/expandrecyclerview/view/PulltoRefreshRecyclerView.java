package com.arjinmc.expandrecyclerview.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Pull to refresh and load more View
 * Created by Eminem Lo on 2018/3/19.
 * email: arjinmc@hotmail.com
 */

public class PulltoRefreshRecyclerView extends LinearLayout {

    private final int STATUS_NORMAL = 0;
    private final int STATUS_PULL_REFRESH = 1;
    private final int STATUS_PULL_REFRESH_RELEASE = 2;
    private final int STATUS_PULL_LOAD_MORE = 3;
    private final int STATUS_PULL_LOAD_MORE_RELEASE = 4;

    public static final int MODE_BOTH = 0;
    public static final int MODE_PULL_REFRESH = 1;
    public static final int MODE_LOAD_MORE = 2;

    private View mHeadView;
    private View mBottomView;
    private View mContentView;

    private int mMode = MODE_BOTH;


    public PulltoRefreshRecyclerView(Context context) {
        super(context);
        init();
    }

    public PulltoRefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PulltoRefreshRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PulltoRefreshRecyclerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public View getHeadView() {
        return mHeadView;
    }

    public void setHeadView(View headView) {
        this.mHeadView = headView;
    }

    public View getBottomView() {
        return mBottomView;
    }

    public void setBottomView(View bottomView) {
        this.mBottomView = bottomView;
    }

    public View getContentView() {
        return mContentView;
    }

    public void setContentView(View contentView) {
        this.mContentView = contentView;
    }

    public int getMode() {
        return mMode;
    }

    public void setMode(int mode) {
        this.mMode = mode;
    }

    private void init() {
        this.setOrientation(LinearLayout.VERTICAL);
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public interface OnRefreshAndLoadMoreListener {
        void onRefresh();

        void onLoadMore();
    }

    @IntDef({MODE_BOTH, MODE_PULL_REFRESH, MODE_LOAD_MORE})
    public @interface ModeType {
    }
}
