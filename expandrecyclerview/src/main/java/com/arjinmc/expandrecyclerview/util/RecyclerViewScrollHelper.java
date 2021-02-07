package com.arjinmc.expandrecyclerview.util;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Helper for scroll recycler view;
 * Created by Eminem Lo on 13/1/2021.
 * email: arjinmc@hotmail.com
 */
public class RecyclerViewScrollHelper {

    /**
     * move to position as first visible item
     * @param layoutManager
     * @param position
     */
    public static void moveToPosition(LinearLayoutManager layoutManager, int position) {

        if (layoutManager == null || position < 0 || position > layoutManager.getItemCount() - 1) {
            return;
        }
        try {
            layoutManager.scrollToPositionWithOffset(position, 0);
            layoutManager.setStackFromEnd(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * move to position as first visible item
     * @param recyclerView
     * @param position
     */
    public static void moveToPosition(RecyclerView recyclerView, int position) {

        if (recyclerView == null || position < 0 || position > recyclerView.getAdapter().getItemCount() - 1) {
            return;
        }

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager == null) {
            return;
        }

        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            try {
                linearLayoutManager.scrollToPositionWithOffset(position, 0);
                linearLayoutManager.setStackFromEnd(true);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            try {
                throw new Exception("Only support LinearLayoutManager");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
