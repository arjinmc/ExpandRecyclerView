package com.arjinmc.expandrecyclerview.listener;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * OnRecyclerViewItemClickListener
 * Created by Eminem Lo on 2018/12/3.
 * email: arjinmc@hotmail.com
 */
public abstract class OnRecyclerViewItemClickListener extends GestureDetector.SimpleOnGestureListener
        implements RecyclerView.OnItemTouchListener {

    private RecyclerView mRecyclerView;
    private GestureDetectorCompat mGestureDetector;

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        init(rv);
        if (mGestureDetector != null) {
            mGestureDetector.onTouchEvent(e);
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        init(rv);
        if (mGestureDetector != null) {
            mGestureDetector.onTouchEvent(e);
        }
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (mRecyclerView == null) {
            return false;
        }
        View child = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
        if (child != null) {
            onItemClick(mRecyclerView.getChildViewHolder(child), mRecyclerView.getChildAdapterPosition(child));
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        if (mRecyclerView == null) {
            return;
        }
        View child = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
        if (child != null) {
            onItemLongClick(mRecyclerView.getChildViewHolder(child), mRecyclerView.getChildAdapterPosition(child));
        }
    }

    public abstract void onItemClick(RecyclerView.ViewHolder viewHolder, int position);

    public abstract void onItemLongClick(RecyclerView.ViewHolder viewHolder, int position);

    private void init(RecyclerView rv) {
        if (mRecyclerView == null) {
            mRecyclerView = rv;
            mGestureDetector = new GestureDetectorCompat(mRecyclerView.getContext(), this);
        }
    }
}
