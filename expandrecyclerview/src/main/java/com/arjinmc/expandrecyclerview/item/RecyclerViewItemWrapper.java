package com.arjinmc.expandrecyclerview.item;

import com.arjinmc.expandrecyclerview.adapter.RecyclerViewViewHolder;

/**
 * wrapper for item of  RecyclerView
 * Created by Eminem Lo on 22/7/2020.
 * email: arjinmc@hotmail.com
 */
public abstract class RecyclerViewItemWrapper<E> {

    /**
     * OnBind view
     * Override for your real item view detail
     * user holder.getView(id) to find your view
     *
     * @param holder
     * @param position
     * @param object
     */
    public abstract void onBind(RecyclerViewViewHolder holder, int position, E object);

    /**
     * Get view type
     * Define your view type here
     *
     * @return
     */
    public abstract int getViewType();
}
