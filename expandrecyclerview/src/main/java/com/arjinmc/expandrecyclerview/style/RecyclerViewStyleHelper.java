package com.arjinmc.expandrecyclerview.style;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * <p>
 * RecyclerViewStyleHelper
 * transform {@link RecyclerView} to any style below these methods
 * </p>
 * Created by Eminem Lo on 2017/8/18.
 * email: arjinmc@hotmail.com
 */

public final class RecyclerViewStyleHelper {

    /**
     * transform to Horizontal LinearLayout
     *
     * @param recyclerView @param recyclerView {@link RecyclerView}
     * @param orientation  the orientation of LinearLayout
     */
    public static void toLinearLayout(RecyclerView recyclerView
            , @LinearLayoutCompat.OrientationMode int orientation) {
        if (recyclerView == null) {
            return;
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()
                , orientation, false));
    }

    /**
     * transform to ViewPager
     *
     * @param recyclerView {@link RecyclerView}
     * @param orientation  the orientation of Linearlayout
     */
    public static void toViewPager(RecyclerView recyclerView
            , @LinearLayoutCompat.OrientationMode int orientation) {
        if (recyclerView == null) {
            return;
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()
                , orientation, false));
        new PagerSnapHelper().attachToRecyclerView(recyclerView);
    }

    /**
     * @param recyclerView {@link RecyclerView}
     * @param spanCount    the count for column
     */
    public static void toGridView(RecyclerView recyclerView, int spanCount) {
        if (recyclerView == null) {
            return;
        }
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), spanCount));
    }

    /**
     * @param recyclerView {@link RecyclerView}
     * @param spanCount    the count for column
     * @param orientation  the orientation of Linearlayout
     */
    public static void toStaggeredGrid(RecyclerView recyclerView, int spanCount
            , @LinearLayoutCompat.OrientationMode int orientation) {
        if (recyclerView == null) {
            return;
        }
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(spanCount, orientation));
    }
}
