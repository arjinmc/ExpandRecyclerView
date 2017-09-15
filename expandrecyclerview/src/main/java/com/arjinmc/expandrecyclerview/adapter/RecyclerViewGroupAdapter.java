package com.arjinmc.expandrecyclerview.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;

/**
 * RecyclerView GroupAdapter
 * Created by Eminem Lo on 2017/9/11.
 * email: arjinmc@hotmail.com
 */

public class RecyclerViewGroupAdapter<T> extends RecyclerView.Adapter<RecyclerViewViewHolder> {

    private final String TAG = "RVGroupAdapter";

    private Context mContext;
    private RecyclerView mParent;
    private List<T> mDataList;
    private Map<Integer, Integer> mTypeLayoutIds;
    private RecyclerViewGroupTypeProcessor<T> mGroupTypeProcessor;
    /**
     * default groupViewType is 0 if not set
     */
    private int mGroupViewType;

    /**
     * map for per group position
     */
    private Map<Integer, Integer> mGroupPositionMap;
    /**
     * map for per group item count
     */
    private Map<Integer, Integer> mGroupItemCountMap;
    /**
     * the count of groups
     */
    private int mGroupCount;
    /**
     * cache map for child's group position
     */
    private Map<Integer, Integer> mChildGroupPositionCacheMap;
    /**
     * cache map for child's position in its group
     */
    private Map<Integer, Integer> mChildItemPositionCacheMap;


    public RecyclerViewGroupAdapter(Context context, List<T> dataList
            , @LayoutRes int[] typeLayoutIds, RecyclerViewGroupTypeProcessor<T> groupTypeProcessor) {
        this(context, dataList, typeLayoutIds, null, groupTypeProcessor);
    }

    public RecyclerViewGroupAdapter(Context context, List<T> dataList
            , @LayoutRes int[] typeLayoutIds, Integer groupType, RecyclerViewGroupTypeProcessor<T> groupTypeProcessor) {
        mContext = context;
        if (groupType != null && groupType >= 0) mGroupViewType = groupType;
        mDataList = dataList;
        if (typeLayoutIds != null && typeLayoutIds.length != 0) {
            mTypeLayoutIds = new ArrayMap<>();
            int typeSize = typeLayoutIds.length;
            for (int i = 0; i < typeSize; i++) {
                mTypeLayoutIds.put(i, typeLayoutIds[i]);
            }
        }
        mGroupTypeProcessor = groupTypeProcessor;

        initGroup();
    }

    @Override
    public RecyclerViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // auto find the viewType and bind it to viewholder
        if (mTypeLayoutIds != null) {
            if (mTypeLayoutIds.containsKey(viewType)) {
                return new RecyclerViewViewHolder(LayoutInflater.from(mContext)
                        .inflate(mTypeLayoutIds.get(viewType), parent, false));
            } else {
                throw new RuntimeException(TAG + ":viewType not found");
            }
        } else {
            throw new RuntimeException(TAG + ":You should add one layout resource id at least");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerViewViewHolder holder, int position) {

        if (getItemViewType(position) == mGroupViewType) {
            mGroupTypeProcessor.onBindGroupViewHolder(holder, findGroupPosition(position), mDataList.get(position));
        } else {
            int groupPosition = findChildGroupPosition(position);
            mGroupTypeProcessor.onBindItemViewHolder(holder, groupPosition
                    , findChildPosition(groupPosition, position), mDataList.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mGroupTypeProcessor.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (mDataList == null)
            return 0;
        else
            return mDataList.size();
    }

    /**
     * notifiy data set change
     */
    public void notifyDataChanged() {

        notifyDataSetChanged();

        mGroupPositionMap.clear();
        mGroupItemCountMap.clear();
        mChildGroupPositionCacheMap.clear();
        mChildItemPositionCacheMap.clear();

        mGroupPositionMap = null;
        mGroupItemCountMap = null;
        mChildGroupPositionCacheMap = null;
        mChildItemPositionCacheMap = null;
        initGroup();
    }

    /**
     * get the item count of one group with groupPosition
     * @param groupPosition
     * @return
     */
    public int getGroupItemCount(int groupPosition){
        if(mGroupItemCountMap==null)
            return -1;
        else
            return mGroupItemCountMap.get(groupPosition);
    }

    /**
     * get the position in data list with groupPosition
     * @param groupPosition
     * @return
     */
    public int getItemInDataListPosition(int groupPosition){
        if(mGroupPositionMap==null)
            return -1;
        return mGroupPositionMap.get(groupPosition);
    }

    /**
     * get the position in data list with groupPosition and childPosition
     *
     * @param groupPosition
     * @param childPosition
     * @return
     */
    public int getItemInDataListPosition(int groupPosition, int childPosition) {
        if (mGroupPositionMap == null)
            return -1;
        return mGroupPositionMap.get(groupPosition) + 1 + childPosition;
    }

    /**
     * overwrite notifyItemChanged
     *
     * @param position
     */
    public void notifyDataChanged(int position) {
        notifyItemChanged(position);
    }

    /**
     * overwrite notifyItemChanged
     *
     * @param groupPosition
     * @param childPosition
     */
    public void notifyDataChanged(int groupPosition, int childPosition) {
        notifyItemChanged(getItemInDataListPosition(groupPosition, childPosition));
    }

    /**
     * overwrite notifyDataRangeInserted
     *
     * @param position
     */
    public void notifyDataInserted(int position) {
        notifyDataRangeInserted(position, 1);
    }

    /**
     * overwrite notifyDataRangeInserted
     *
     * @param groupPosition
     * @param childPosition
     */
    public void notifyDataInserted(int groupPosition, int childPosition) {
        notifyDataRangeInserted(getItemInDataListPosition(groupPosition, childPosition), 1);
    }

    /**
     * overwrite notifyItemMoved
     *
     * @param fromPosition
     * @param toPosition
     */
    public void notifyDataMoved(int fromPosition, int toPosition) {
        notifyItemMoved(fromPosition, toPosition);
    }

    /**
     * overwrite notifyItemMoved
     *
     * @param fromGroupPosition
     * @param fromChildPosition
     * @param toGroupPosition
     * @param toChildPosition
     */
    public void notifyDataMoved(int fromGroupPosition, int fromChildPosition
            , int toGroupPosition, int toChildPosition) {
        int fromPosition = getItemInDataListPosition(fromGroupPosition, fromChildPosition);
        int toPosition = getItemInDataListPosition(toGroupPosition, toChildPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    /**
     * overwrite notifyItemRangeRemoved
     *
     * @param position
     */
    public void notifyDataRemoved(int position) {
        notifyItemRangeRemoved(position, 1);
    }

    /**
     * overwrite notifyItemRangeRemoved
     *
     * @param groupPosition
     * @param childPosition
     */
    public void notifyDataRemoved(int groupPosition, int childPosition) {
        notifyItemRangeRemoved(getItemInDataListPosition(groupPosition, childPosition), 1);
    }

    /**
     * overwrite notifyItemRangeChanged
     *
     * @param startPosition
     * @param itemCount
     */
    public void notifyDataRangeChanged(int startPosition, int itemCount) {
        notifyItemRangeChanged(startPosition, itemCount);
    }

    /**
     * overwrite notifyItemRangeChanged
     *
     * @param startGroupPosition
     * @param startChildPosition
     * @param itemCount
     */
    public void notifyDataRangeChanged(int startGroupPosition, int startChildPosition, int itemCount) {
        notifyItemRangeChanged(getItemInDataListPosition(startGroupPosition, startChildPosition), itemCount);
    }

    /**
     * overwrite notifyItemRangeInserted
     *
     * @param startPosition
     * @param itemCount
     */
    public void notifyDataRangeInserted(int startPosition, int itemCount) {
        notifyItemRangeInserted(startPosition, itemCount);
    }

    /**
     * overwrite notifyItemRangeInserted
     *
     * @param startGroupPosition
     * @param startChildPosition
     * @param itemCount
     */
    public void notifyDataRangeInserted(int startGroupPosition, int startChildPosition, int itemCount) {
        notifyItemRangeInserted(getItemInDataListPosition(startGroupPosition, startChildPosition), itemCount);
    }

    /**
     * overwrite notifyItemRangeRemoved
     *
     * @param startPosition
     * @param itemCount
     */
    public void notifyDataRangeRemoved(int startPosition, int itemCount) {
        notifyItemRangeRemoved(startPosition, itemCount);
    }

    /**
     * overwrite notifyItemRangeRemoved
     *
     * @param startGroupPosition
     * @param startChildPosition
     * @param itemCount
     */
    public void notifyDataRangeRemoved(int startGroupPosition, int startChildPosition, int itemCount) {
        notifyItemRangeRemoved(getItemInDataListPosition(startGroupPosition, startChildPosition), itemCount);
    }

    /**
     * get group's item count
     *
     * @param groupPosition
     * @return
     */
    public int getItemCount(int groupPosition) {
        if (mGroupItemCountMap == null)
            return -1;
        else
            return mGroupItemCountMap.get(groupPosition);
    }

    /**
     * init group info for group count, position map and count map
     */
    private void initGroup() {
        mGroupPositionMap = new ArrayMap<>();
        mGroupItemCountMap = new ArrayMap<>();
        mGroupCount = 0;

        int lastGroupPosition = 0;

        if (mDataList != null && !mDataList.isEmpty()) {
            int dataSize = mDataList.size();
            for (int i = 0; i < dataSize; i++) {
                if (getItemViewType(i) == mGroupViewType) {
                    mGroupPositionMap.put(mGroupCount, i);
                    lastGroupPosition = i;

                    if (mGroupCount != 0) {
                        mGroupItemCountMap.put(mGroupCount - 1, i - mGroupPositionMap.get(mGroupCount - 1));
                    }
                    mGroupCount++;
                }
            }
            mGroupItemCountMap.put(mGroupCount - 1, dataSize - lastGroupPosition);
        }
    }

    /**
     * find out the group position for group list
     *
     * @param position adapter item position
     * @return
     */
    private int findGroupPosition(int position) {

        if (mGroupPositionMap == null || mGroupPositionMap.isEmpty())
            return -1;
        else {
            for (Integer key : mGroupPositionMap.keySet()) {
                if (mGroupPositionMap.get(key) == position) {
                    return key;
                }
            }
            return -1;
        }
    }

    /**
     * find out the child position of its group
     *
     * @param position adapter item position
     * @return
     */
    private int findChildGroupPosition(int position) {

        if (mChildGroupPositionCacheMap == null)
            mChildGroupPositionCacheMap = new ArrayMap<>();
        if (mDataList == null || mDataList.isEmpty()) return -1;
        int tempPosition = mDataList.size();
        int groupPosition = 0;

        if (mChildGroupPositionCacheMap.containsKey(position))
            return mChildGroupPositionCacheMap.get(position);
        for (int i = mGroupCount; i > 0; i--) {
            if (tempPosition <= position) {
                groupPosition = i;
                mChildGroupPositionCacheMap.put(position, groupPosition);
                break;
            }

            tempPosition -= mGroupItemCountMap.get(i - 1);
        }
        return groupPosition;
    }

    /**
     * find out the child position of its group
     *
     * @param groupPosition the group position of the group list
     * @param position      adapter item position
     * @return
     */
    private int findChildPosition(int groupPosition, int position) {

        if (mChildItemPositionCacheMap == null)
            mChildItemPositionCacheMap = new ArrayMap<>();
        if (mDataList == null || mDataList.isEmpty()) return -1;
        int positionInGroup = 0;
        if (mChildItemPositionCacheMap.containsKey(position))
            return mChildItemPositionCacheMap.get(position);

        int groupItemCount = mGroupItemCountMap.get(groupPosition);
        int groupRealPosition = mGroupPositionMap.get(groupPosition);
        positionInGroup = (position - groupRealPosition) % groupItemCount - 1;
        mChildItemPositionCacheMap.put(position, positionInGroup);

        return positionInGroup;

    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (mParent == null) mParent = recyclerView;

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup oldSizeLookup = gridLayoutManager.getSpanSizeLookup();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (recyclerView.getAdapter().getItemViewType(position) == mGroupViewType) {
                        return gridLayoutManager.getSpanCount();
                    }
                    if (oldSizeLookup != null) {
                        return oldSizeLookup.getSpanSize(position);
                    }
                    return 1;
                }
            });
        }
    }

//    @Override
//    public void onViewAttachedToWindow(RecyclerViewViewHolder holder) {
//        super.onViewAttachedToWindow(holder);
//
//        //recyclerview has a bug
//        if (mParent.getLayoutManager() instanceof StaggeredGridLayoutManager) {
//            StaggeredGridLayoutManager.LayoutParams slp =
//                    (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
//            if (holder.getItemViewType() == mGroupViewType) {
//                slp.setFullSpan(true);
//            }
//
//        }
//    }

}


