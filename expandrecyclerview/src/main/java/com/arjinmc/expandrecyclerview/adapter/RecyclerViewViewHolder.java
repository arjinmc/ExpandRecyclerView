package com.arjinmc.expandrecyclerview.adapter;

import android.util.SparseArray;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Common ViewHolder for RecyclerView
 * Created by Eminem Lo on 2017/8/18.
 * email: arjinmc@hotmail.com
 */

public class RecyclerViewViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;

    public RecyclerViewViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
    }

    /**
     * use these method to getView with cache replace for findViewById
     *
     * @param viewId the resource id of View
     * @param <T>    the type of View
     * @return
     */
    public <T extends View> T getView(@IdRes int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

}
