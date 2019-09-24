package com.arjinmc.recyclerviewdecoration.layout;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.arjinmc.recyclerviewdecoration.RecyclerViewGridItemDecoration;

/**
 * Grid with Diver LayoutManager
 * Created by Eminem Lo on 2019-09-24.
 * email: arjinmc@hotmail.com
 */
//public class GridDiverLayoutManager extends LinearLayoutManager {
public class GridDiverLayoutManager extends RecyclerView.LayoutManager {

    public static final int HORIZONTAL = RecyclerView.HORIZONTAL;

    public static final int VERTICAL = RecyclerView.VERTICAL;

    private Context mContext;
    /**
     * the column count for layout
     */
    private int mSpanCount;
    /**
     * the horizontal spacing
     */
    private int mHorizontalSpacing;
    /**
     * the vertical spacing
     */
    private int mVerticalSpacing;
    /**
     * the orientation for grid
     */
    private int mOrientation;

    final SparseIntArray mPreLayoutSpanSizeCache = new SparseIntArray();
    final SparseIntArray mPreLayoutSpanIndexCache = new SparseIntArray();
    private SpanSizeLookup mSpanSizeLookup = new DefaultSpanSizeLookup();
    /**
     * Right borders for each span.
     * <p>For <b>i-th</b> item start is {@link #mCachedBorders}[i-1] + 1
     * and end is {@link #mCachedBorders}[i].
     */
    int[] mCachedBorders;

    /**
     * @param context
     * @param spanCount
     * @param horizontalSpacing
     * @param verticalSpacing
     */
    public GridDiverLayoutManager(Context context, int spanCount, int horizontalSpacing, int verticalSpacing) {
//        super(context);
        this.mContext = context;
        this.mSpanCount = spanCount;
        this.mHorizontalSpacing = horizontalSpacing;
        this.mVerticalSpacing = verticalSpacing;
    }

    /**
     * @param context
     * @param spanCount
     * @param itemDecoration
     */
    public GridDiverLayoutManager(Context context, int spanCount, RecyclerViewGridItemDecoration itemDecoration) {
//        super(context);
        this.mContext = context;
        this.mSpanCount = spanCount;
        if (itemDecoration != null) {
            this.mHorizontalSpacing = itemDecoration.getHorizontalSpacing();
            this.mVerticalSpacing = itemDecoration.getVerticalSpacing();
        }
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        if (mOrientation == HORIZONTAL) {
            return new GridDiverLayoutManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            return new GridDiverLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public RecyclerView.LayoutParams generateLayoutParams(Context c, AttributeSet attrs) {
        return new GridDiverLayoutManager.LayoutParams(c, attrs);
    }

    @Override
    public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            return new GridDiverLayoutManager.LayoutParams((ViewGroup.MarginLayoutParams) lp);
        } else {
            return new GridDiverLayoutManager.LayoutParams(lp);
        }
    }

    @Override
    public void setMeasuredDimension(Rect childrenBounds, int wSpec, int hSpec) {
        if (mCachedBorders == null) {
            super.setMeasuredDimension(childrenBounds, wSpec, hSpec);
        }
        final int width, height;
        final int horizontalPadding = getPaddingLeft() + getPaddingRight();
        final int verticalPadding = getPaddingTop() + getPaddingBottom();
        if (mOrientation == VERTICAL) {
            final int usedHeight = childrenBounds.height() + verticalPadding;
            height = chooseSize(hSpec, usedHeight, getMinimumHeight());
            width = chooseSize(wSpec, mCachedBorders[mCachedBorders.length - 1] + horizontalPadding,
                    getMinimumWidth());
        } else {
            final int usedWidth = childrenBounds.width() + horizontalPadding;
            width = chooseSize(wSpec, usedWidth, getMinimumWidth());
            height = chooseSize(hSpec, mCachedBorders[mCachedBorders.length - 1] + verticalPadding,
                    getMinimumHeight());
        }
        setMeasuredDimension(width, height);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.getItemCount() == 0) {
            removeAndRecycleAllViews(recycler);
            return;
        }
        if (state.isPreLayout()) {
            cachePreLayoutSpanMapping();
        }
//        super.onLayoutChildren(recycler, state);
        clearPreLayoutSpanMappingCache();
    }

    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
    }

    private void cachePreLayoutSpanMapping() {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final GridDiverLayoutManager.LayoutParams lp = (GridDiverLayoutManager.LayoutParams) getChildAt(i).getLayoutParams();
            final int viewPosition = lp.getViewLayoutPosition();
            mPreLayoutSpanSizeCache.put(viewPosition, lp.getSpanSize());
            mPreLayoutSpanIndexCache.put(viewPosition, lp.getSpanIndex());
        }
    }

    private void clearPreLayoutSpanMappingCache() {
        mPreLayoutSpanSizeCache.clear();
        mPreLayoutSpanIndexCache.clear();
    }

    /**
     * Sets the source to get the number of spans occupied by each item in the adapter.
     *
     * @param spanSizeLookup {@link SpanSizeLookup} instance to be used to query number of spans
     *                       occupied by each item
     */
    public void setSpanSizeLookup(SpanSizeLookup spanSizeLookup) {
        mSpanSizeLookup = spanSizeLookup;
    }

    /**
     * A helper class to provide the number of spans each item occupies.
     * <p>
     * Default implementation sets each item to occupy exactly 1 span.
     *
     * @see this.setSpanSizeLookup(SpanSizeLookup)
     */
    public abstract static class SpanSizeLookup {

        final SparseIntArray mSpanIndexCache = new SparseIntArray();

        private boolean mCacheSpanIndices = false;

        /**
         * Returns the number of span occupied by the item at <code>position</code>.
         *
         * @param position The adapter position of the item
         * @return The number of spans occupied by the item at the provided position
         */
        public abstract int getSpanSize(int position);

        /**
         * Sets whether the results of {@link #getSpanIndex(int, int)} method should be cached or
         * not. By default these values are not cached. If you are not overriding
         * {@link #getSpanIndex(int, int)}, you should set this to true for better performance.
         *
         * @param cacheSpanIndices Whether results of getSpanIndex should be cached or not.
         */
        public void setSpanIndexCacheEnabled(boolean cacheSpanIndices) {
            mCacheSpanIndices = cacheSpanIndices;
        }

        /**
         * Clears the span index cache. GridLayoutManager automatically calls this method when
         * adapter changes occur.
         */
        public void invalidateSpanIndexCache() {
            mSpanIndexCache.clear();
        }

        /**
         * Returns whether results of {@link #getSpanIndex(int, int)} method are cached or not.
         *
         * @return True if results of {@link #getSpanIndex(int, int)} are cached.
         */
        public boolean isSpanIndexCacheEnabled() {
            return mCacheSpanIndices;
        }

        int getCachedSpanIndex(int position, int spanCount) {
            if (!mCacheSpanIndices) {
                return getSpanIndex(position, spanCount);
            }
            final int existing = mSpanIndexCache.get(position, -1);
            if (existing != -1) {
                return existing;
            }
            final int value = getSpanIndex(position, spanCount);
            mSpanIndexCache.put(position, value);
            return value;
        }

        /**
         * Returns the final span index of the provided position.
         * <p>
         * If you have a faster way to calculate span index for your items, you should override
         * this method. Otherwise, you should enable span index cache
         * ({@link #setSpanIndexCacheEnabled(boolean)}) for better performance. When caching is
         * disabled, default implementation traverses all items from 0 to
         * <code>position</code>. When caching is enabled, it calculates from the closest cached
         * value before the <code>position</code>.
         * <p>
         * If you override this method, you need to make sure it is consistent with
         * {@link #getSpanSize(int)}. GridLayoutManager does not call this method for
         * each item. It is called only for the reference item and rest of the items
         * are assigned to spans based on the reference item. For example, you cannot assign a
         * position to span 2 while span 1 is empty.
         * <p>
         * Note that span offsets always start with 0 and are not affected by RTL.
         *
         * @param position  The position of the item
         * @param spanCount The total number of spans in the grid
         * @return The final span position of the item. Should be between 0 (inclusive) and
         * <code>spanCount</code>(exclusive)
         */
        public int getSpanIndex(int position, int spanCount) {
            int positionSpanSize = getSpanSize(position);
            if (positionSpanSize == spanCount) {
                return 0; // quick return for full-span items
            }
            int span = 0;
            int startPos = 0;
            // If caching is enabled, try to jump
            if (mCacheSpanIndices && mSpanIndexCache.size() > 0) {
                int prevKey = findReferenceIndexFromCache(position);
                if (prevKey >= 0) {
                    span = mSpanIndexCache.get(prevKey) + getSpanSize(prevKey);
                    startPos = prevKey + 1;
                }
            }
            for (int i = startPos; i < position; i++) {
                int size = getSpanSize(i);
                span += size;
                if (span == spanCount) {
                    span = 0;
                } else if (span > spanCount) {
                    // did not fit, moving to next row / column
                    span = size;
                }
            }
            if (span + positionSpanSize <= spanCount) {
                return span;
            }
            return 0;
        }

        int findReferenceIndexFromCache(int position) {
            int lo = 0;
            int hi = mSpanIndexCache.size() - 1;

            while (lo <= hi) {
                final int mid = (lo + hi) >>> 1;
                final int midVal = mSpanIndexCache.keyAt(mid);
                if (midVal < position) {
                    lo = mid + 1;
                } else {
                    hi = mid - 1;
                }
            }
            int index = lo - 1;
            if (index >= 0 && index < mSpanIndexCache.size()) {
                return mSpanIndexCache.keyAt(index);
            }
            return -1;
        }

        /**
         * Returns the index of the group this position belongs.
         * <p>
         * For example, if grid has 3 columns and each item occupies 1 span, span group index
         * for item 1 will be 0, item 5 will be 1.
         *
         * @param adapterPosition The position in adapter
         * @param spanCount       The total number of spans in the grid
         * @return The index of the span group including the item at the given adapter position
         */
        public int getSpanGroupIndex(int adapterPosition, int spanCount) {
            int span = 0;
            int group = 0;
            int positionSpanSize = getSpanSize(adapterPosition);
            for (int i = 0; i < adapterPosition; i++) {
                int size = getSpanSize(i);
                span += size;
                if (span == spanCount) {
                    span = 0;
                    group++;
                } else if (span > spanCount) {
                    // did not fit, moving to next row / column
                    span = size;
                    group++;
                }
            }
            if (span + positionSpanSize > spanCount) {
                group++;
            }
            return group;
        }
    }

    /**
     * Default implementation for {@link SpanSizeLookup}. Each item occupies 1 span.
     */
    public static final class DefaultSpanSizeLookup extends SpanSizeLookup {

        @Override
        public int getSpanSize(int position) {
            return 1;
        }

        @Override
        public int getSpanIndex(int position, int spanCount) {
            return position % spanCount;
        }
    }

    /**
     * LayoutParams used by GridLayoutManager.
     * <p>
     * Note that if the orientation is {@link #VERTICAL}, the width parameter is ignored and if the
     * orientation is {@link #HORIZONTAL} the height parameter is ignored because child view is
     * expected to fill all of the space given to it.
     */
    public static class LayoutParams extends RecyclerView.LayoutParams {

        /**
         * Span Id for Views that are not laid out yet.
         */
        public static final int INVALID_SPAN_ID = -1;

        int mSpanIndex = INVALID_SPAN_ID;

        int mSpanSize = 0;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(RecyclerView.LayoutParams source) {
            super(source);
        }

        /**
         * Returns the current span index of this View. If the View is not laid out yet, the return
         * value is <code>undefined</code>.
         * <p>
         * Starting with RecyclerView <b>24.2.0</b>, span indices are always indexed from position 0
         * even if the layout is RTL. In a vertical GridLayoutManager, <b>leftmost</b> span is span
         * 0 if the layout is <b>LTR</b> and <b>rightmost</b> span is span 0 if the layout is
         * <b>RTL</b>. Prior to 24.2.0, it was the opposite which was conflicting with
         * {@link SpanSizeLookup#getSpanIndex(int, int)}.
         * <p>
         * If the View occupies multiple spans, span with the minimum index is returned.
         *
         * @return The span index of the View.
         */
        public int getSpanIndex() {
            return mSpanIndex;
        }

        /**
         * Returns the number of spans occupied by this View. If the View not laid out yet, the
         * return value is <code>undefined</code>.
         *
         * @return The number of spans occupied by this View.
         */
        public int getSpanSize() {
            return mSpanSize;
        }
    }
}
