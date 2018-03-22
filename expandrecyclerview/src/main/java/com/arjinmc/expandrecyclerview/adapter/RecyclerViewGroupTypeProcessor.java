package com.arjinmc.expandrecyclerview.adapter;

/**
 * RecyclerViewMultipleTypeProcessor
 * Callback for the data and viewType you need to override with you pattern
 * Created by Eminem Lo on 2017/8/18.
 * email: arjinmc@hotmail.com
 */

public abstract class RecyclerViewGroupTypeProcessor<E> {

    /**
     * Callback {@link android.support.v7.widget.RecyclerView.Adapter} onBindViewHolder for group type
     *
     * @param holder        viewHolder
     * @param groupPosition the position of the group
     * @param object        the object of data list
     */
    public abstract void onBindGroupViewHolder(RecyclerViewViewHolder holder, int groupPosition, E object);

    /**
     * Callback {@link android.support.v7.widget.RecyclerView.Adapter} onBindViewHolder for item type
     *
     * @param holder        viewHolder
     * @param groupPosition the position of the group
     * @param itemPosition  the item position of the group
     * @param object        the object of data list
     */
    public abstract void onBindItemViewHolder(RecyclerViewViewHolder holder, int groupPosition, int itemPosition, E object);

    /**
     * Callback {@link android.support.v7.widget.RecyclerView.Adapter} getItemViewType
     *
     * @param position the position of the item
     */
    public abstract int getItemViewType(int position);
}
