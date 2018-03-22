package com.arjinmc.expandrecyclerview.adapter;

/**
 * RecyclerViewSingleTypeProcessor
 * Created by Eminem Lo on 2017/8/18.
 * email: arjinmc@hotmail.com
 */

public abstract class RecyclerViewSingleTypeProcessor<E> {

    /**
     * Callback {@link android.support.v7.widget.RecyclerView.Adapter} onBindViewHolder
     *
     * @param holder   viewHolder
     * @param position the position of the item
     * @param object   the object of data list
     */
    public abstract void onBindViewHolder(RecyclerViewViewHolder holder, int position, E object);

}
