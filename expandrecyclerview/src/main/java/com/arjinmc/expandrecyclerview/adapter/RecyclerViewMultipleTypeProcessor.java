package com.arjinmc.expandrecyclerview.adapter;

/**
 * RecyclerViewMultipleTypeProcessor
 * Callback for the data and viewType you need to override with you pattern
 * Created by Eminem Lo on 2017/8/18.
 * email: arjinmc@hotmail.com
 */

public abstract class RecyclerViewMultipleTypeProcessor<T> {

    /**
     * Callback {@link android.support.v7.widget.RecyclerView.Adapter} onBindViewHolder
     *
     * @param holder   viewHolder
     * @param position the position of the item
     * @param object   the object of data list
     */
    public abstract void onBindViewHolder(RecyclerViewViewHolder holder, int position, T object);

    /**
     * Callback {@link android.support.v7.widget.RecyclerView.Adapter} getItemViewType
     *
     * @param position the position of the item
     */
    public abstract int getItemViewType(int position);
}
