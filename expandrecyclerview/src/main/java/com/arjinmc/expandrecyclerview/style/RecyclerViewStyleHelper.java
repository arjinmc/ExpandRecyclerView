package com.arjinmc.expandrecyclerview.style;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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
     * Transform to Horizontal LinearLayout
     *
     * @param recyclerView @param recyclerView {@link RecyclerView}
     * @param orientation  the orientation of RecyclerView
     */
    public static void toLinearLayout(RecyclerView recyclerView
            , @RecyclerView.Orientation int orientation) {
        if (recyclerView == null) {
            return;
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()
                , orientation, false));
    }

    /**
     * Transform to ViewPager
     *
     * @param recyclerView {@link RecyclerView}
     * @param orientation  the orientation of RecyclerView
     */
    public static void toViewPager(RecyclerView recyclerView
            , @RecyclerView.Orientation int orientation) {
        if (recyclerView == null) {
            return;
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()
                , orientation, false));
        new PagerSnapHelper().attachToRecyclerView(recyclerView);
    }

    /**
     * Transform grid style
     *
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
     * Transform staggered grid style
     *
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

    /**
     * Transform linearlayout snap item style
     *
     * @param recyclerView {@link RecyclerView}
     * @param orientation  the orientation of Linearlayout
     */
    public static void toLinearSnap(RecyclerView recyclerView, @RecyclerView.Orientation int orientation) {
        if (recyclerView == null) {
            return;
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()
                , orientation, false));
        LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
        linearSnapHelper.attachToRecyclerView(recyclerView);
    }

}
