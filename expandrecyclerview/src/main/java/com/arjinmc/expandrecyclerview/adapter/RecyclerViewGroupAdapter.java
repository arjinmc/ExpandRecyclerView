package com.arjinmc.expandrecyclerview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView GroupAdapter
 * Created by Eminem Lo on 2017/9/11.
 * email: arjinmc@hotmail.com
 */

public class RecyclerViewGroupAdapter<E> extends RecyclerView.Adapter<RecyclerViewViewHolder> {

    private final String TAG = "RVGroupAdapter";

    private Context mContext;
    private RecyclerView mParent;
    private List<E> mDataList;
    private ArrayMap<Integer, Integer> mTypeLayoutIds;
    private RecyclerViewGroupTypeProcessor<E> mGroupTypeProcessor;
    private OnItemClickListener mOnItemClickListener;
    /**
     * default groupViewType is 0 if not set
     */
    private int mGroupViewType;

    /**
     * map for per group position
     */
    private ArrayMap<Integer, Integer> mGroupPositionMap;
    /**
     * map for per group item count
     */
    private ArrayMap<Integer, Integer> mGroupItemCountMap;
    /**
     * the count of groups
     */
    private int mGroupCount;
    /**
     * cache map for child's group position
     */
    private ArrayMap<Integer, Integer> mChildGroupPositionCacheMap;
    /**
     * cache map for child's position in its group
     */
    private ArrayMap<Integer, Integer> mChildItemPositionCacheMap;


    public RecyclerViewGroupAdapter(Context context, List<E> dataList
            , @LayoutRes int[] typeLayoutIds, RecyclerViewGroupTypeProcessor<E> groupTypeProcessor) {
        this(context, dataList, typeLayoutIds, null, groupTypeProcessor);
    }

    public RecyclerViewGroupAdapter(Context context, List<E> dataList
            , @LayoutRes int[] typeLayoutIds, Integer groupType, RecyclerViewGroupTypeProcessor<E> groupTypeProcessor) {
        mContext = context;
        if (groupType != null && groupType >= 0) {
            mGroupViewType = groupType;
        }
        mDataList = new ArrayList<>();
        if (dataList != null && !dataList.isEmpty()) {
            mDataList.addAll(dataList);
        }
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
            mGroupTypeProcessor.onBindGroupViewHolder(holder, getGroupPosition(position), mDataList.get(position));
        } else {
            int groupPosition = getGroupPosition(position);
            mGroupTypeProcessor.onBindItemViewHolder(holder, groupPosition
                    , getChildPositionInGroup(groupPosition, position), mDataList.get(position));
        }

        if (mOnItemClickListener != null) {
            final int lPosition = position;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getItemViewType(lPosition) == mGroupViewType) {
                        mOnItemClickListener.onClick(lPosition, getGroupPosition(lPosition), -1);
                    } else {
                        int groupPosition = getGroupPosition(lPosition);
                        mOnItemClickListener.onClick(lPosition, groupPosition, getChildPositionInGroup(groupPosition, lPosition));
                    }
                }
            });
        }

    }

    @Override
    public int getItemViewType(int position) {
        return mGroupTypeProcessor.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (mDataList == null) {
            return 0;
        } else {
            return mDataList.size();
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    /**
     * notifiy data set change
     */
    public void notifyDataChanged(List<E> dataList) {

        mDataList.clear();
        if (dataList != null && !dataList.isEmpty()) {
            mDataList.addAll(dataList);
        }
        notifyDataSetChanged();

        if (mGroupPositionMap != null) {
            mGroupPositionMap.clear();
        }
        if (mGroupItemCountMap != null) {
            mGroupItemCountMap.clear();
        }
        if (mChildGroupPositionCacheMap != null) {
            mChildGroupPositionCacheMap.clear();
        }
        if (mChildItemPositionCacheMap != null) {
            mChildItemPositionCacheMap.clear();
        }

        mGroupPositionMap = null;
        mGroupItemCountMap = null;
        mChildGroupPositionCacheMap = null;
        mChildItemPositionCacheMap = null;
        initGroup();
    }

    /**
     * get the item count of one group with groupPosition
     *
     * @param groupPosition
     * @return
     */
    public int getGroupItemCount(int groupPosition) {
        if (mGroupItemCountMap == null || mGroupItemCountMap.isEmpty()
                || mGroupItemCountMap.get(groupPosition) == null) {
            return -1;
        } else {
            return mGroupItemCountMap.get(groupPosition);
        }
    }

    /**
     * get the position in data list with groupPosition
     *
     * @param groupPosition
     * @return
     */
    public int getItemInDataListPosition(int groupPosition) {
        if (mGroupPositionMap == null)
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
        if (mGroupPositionMap == null) {
            return -1;
        }
        if (mGroupItemCountMap.get(groupPosition) == null) {
            return -1;
        }
        if (childPosition < 0 || childPosition >= getGroupItemCount(groupPosition)) {
            return -1;
        }
        return mGroupPositionMap.get(groupPosition) + 1 + childPosition;
    }

    /**
     * Check the position is not in the range of data
     *
     * @param position
     * @return
     */
    private boolean isBeyondDataSize(int position) {
        if (position < 0 || position >= mDataList.size()) {
            throw new IllegalArgumentException("The position is out of the range of data set");
        }
        return false;
    }

    /**
     * over write notifyItemChanged
     *
     * @param position
     * @param data
     */
    public void notifyDataChanged(int position, E data) {

        if (isBeyondDataSize(position) || data == null) {
            return;
        }

        mDataList.set(position, data);
        notifyItemChanged(position);
    }

    /**
     * over write notifyItemChanged
     *
     * @param groupPosition
     * @param childPosition
     * @param data
     */
    public void notifyDataChanged(int groupPosition, int childPosition, E data) {

        if (data == null) {
            return;
        }

        if (groupPosition < 0 || groupPosition >= mGroupCount) {
            throw new IllegalArgumentException("No this group position!");
        }

        if (childPosition < 0 || childPosition >= mGroupItemCountMap.get(groupPosition)) {
            throw new IllegalArgumentException("No this child position in this group!");
        }

        mDataList.set(getItemInDataListPosition(groupPosition, childPosition), data);
        notifyItemChanged(getItemInDataListPosition(groupPosition, childPosition));
    }

    /**
     * over write notifyDataRangeInserted
     *
     * @param position
     * @param data
     */
    public void notifyDataInserted(int position, E data) {

        if (data == null) {
            return;
        }
        if (position < 0 || position >= mDataList.size()) {
            throw new IllegalArgumentException("The position is out of the range of data set");
        }

        List<E> dataList = new ArrayList<>(1);
        dataList.add(data);
        notifyDataRangeInserted(position, dataList);
        initGroup();
    }

    /**
     * over write notifyDataRangeInserted
     *
     * @param groupPosition
     * @param childPosition
     * @param data
     */
    public void notifyDataInserted(int groupPosition, int childPosition, E data) {

        if (data == null) {
            return;
        }

        if (groupPosition < 0 || groupPosition >= mGroupCount) {
            throw new IllegalArgumentException("No this group position!");
        }

        if (childPosition < 0 || childPosition > mGroupItemCountMap.get(groupPosition)) {
            throw new IllegalArgumentException("No this child position in this group!");
        }

        List<E> dataList = new ArrayList<>(1);
        dataList.add(data);
        notifyDataRangeInserted(getItemInDataListPosition(groupPosition, childPosition), dataList);
        initGroup();

    }

    /**
     * over write notifyItemMoved
     *
     * @param fromPosition
     * @param toPosition
     */
    public void notifyDataMoved(int fromPosition, int toPosition) {

        if (isBeyondDataSize(fromPosition) || isBeyondDataSize(toPosition)) {
            throw new IllegalArgumentException("The position is out of the range of data set");
        }
        if (fromPosition == toPosition) {
            return;
        }
        E data = mDataList.get(fromPosition);
        if (fromPosition > toPosition) {
            mDataList.remove(fromPosition);
            mDataList.add(toPosition, data);
        } else {
            mDataList.add(toPosition, data);
            mDataList.remove(fromPosition);
        }

        notifyItemMoved(fromPosition, toPosition);
        initGroup();

    }

    /**
     * over write notifyItemMoved
     *
     * @param fromGroupPosition
     * @param fromChildPosition
     * @param toGroupPosition
     * @param toChildPosition
     */
    public void notifyDataMoved(int fromGroupPosition, int fromChildPosition
            , int toGroupPosition, int toChildPosition) {

        if (fromGroupPosition < 0 || fromGroupPosition >= mGroupCount
                || toGroupPosition < 0 || toGroupPosition >= mGroupCount) {
            throw new IllegalArgumentException("No this group position!");
        }

        if (fromChildPosition < 0 || fromChildPosition >= mGroupItemCountMap.get(fromGroupPosition)
                || toChildPosition < 0 || toChildPosition >= mGroupItemCountMap.get(toGroupPosition)) {
            throw new IllegalArgumentException("No this child position in this group!");
        }

        int fromPosition = getItemInDataListPosition(fromGroupPosition, fromChildPosition);
        int toPosition = getItemInDataListPosition(toGroupPosition, toChildPosition);

        if (isBeyondDataSize(fromPosition) || isBeyondDataSize(toPosition)) {
            throw new IllegalArgumentException("The position is out of the range of data set");
        }
        if (fromPosition == toPosition) {
            return;
        }
        E data = mDataList.get(fromPosition);
        if (fromPosition > toPosition) {
            mDataList.remove(fromPosition);
            mDataList.add(toPosition, data);
        } else {
            mDataList.add(toPosition, data);
            mDataList.remove(fromPosition);
        }

        notifyItemMoved(fromPosition, toPosition);
        initGroup();
    }

    /**
     * over write notifyItemRangeRemoved
     *
     * @param position
     */
    public void notifyDataRemoved(int position) {
        if (isBeyondDataSize(position)) {
            return;
        }
        mDataList.remove(position);
        notifyItemRangeRemoved(position, 1);
        initGroup();

    }

    /**
     * over write notifyItemRangeRemoved
     *
     * @param groupPosition
     * @param childPosition
     */
    public void notifyDataRemoved(int groupPosition, int childPosition) {

        if (groupPosition < 0 || groupPosition > mGroupCount) {
            throw new IllegalArgumentException("No this group position!");
        }

        if (childPosition < 0 || childPosition > mGroupItemCountMap.get(groupPosition)) {
            throw new IllegalArgumentException("No this child position in this group!");
        }
        mDataList.remove(getItemInDataListPosition(groupPosition, childPosition));
        notifyItemRangeRemoved(getItemInDataListPosition(groupPosition, childPosition), 1);
        initGroup();
    }

    /**
     * over write notifyItemRangeChanged
     *
     * @param startPosition
     * @param dataList
     */
    public void notifyDataRangeChanged(int startPosition, List<E> dataList) {

        if (dataList == null || dataList.isEmpty()) {
            return;
        }

        int dataSize = dataList.size();

        if (isBeyondDataSize(startPosition) || isBeyondDataSize(startPosition + dataSize)) {
            return;
        }

        for (int i = 0; i < dataSize; i++) {
            mDataList.set(startPosition + i, dataList.get(i));
        }
        notifyItemRangeChanged(startPosition, dataSize);
        initGroup();
    }

    /**
     * over write notifyItemRangeChanged
     *
     * @param startGroupPosition
     * @param startChildPosition
     * @param dataList
     */
    public void notifyDataRangeChanged(int startGroupPosition, int startChildPosition, List<E> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return;
        }

        int dataSize = dataList.size();

        if (startGroupPosition < 0 || startGroupPosition >= mGroupCount) {
            throw new IllegalArgumentException("No this group position!");
        }

        if (startChildPosition < 0 || startChildPosition >= mGroupItemCountMap.get(startGroupPosition)) {
            throw new IllegalArgumentException("No this child position in this group!");
        }

        for (int i = 0; i < dataSize; i++) {
            mDataList.set(getItemInDataListPosition(startGroupPosition, startChildPosition) + i, dataList.get(i));
        }

        notifyItemRangeChanged(getItemInDataListPosition(startGroupPosition, startChildPosition), dataSize);
        initGroup();

    }

    /**
     * over write notifyItemRangeInserted
     *
     * @param startPosition
     * @param dataList
     */
    public void notifyDataRangeInserted(int startPosition, List<E> dataList) {

        if (dataList == null || dataList.isEmpty()) {
            return;
        }

        int dataSize = dataList.size();

        if (isBeyondDataSize(startPosition) || isBeyondDataSize(startPosition + dataSize)) {
            return;
        }

        for (int i = 0; i < dataSize; i++) {
            mDataList.add(startPosition + i, dataList.get(i));
        }

        notifyItemRangeInserted(startPosition, dataSize);
        initGroup();
    }

    /**
     * over write notifyItemRangeInserted
     *
     * @param startGroupPosition
     * @param startChildPosition
     * @param dataList
     */
    public void notifyDataRangeInserted(int startGroupPosition, int startChildPosition, List<E> dataList) {

        if (dataList == null || dataList.isEmpty()) {
            return;
        }

        int dataSize = dataList.size();

        if (startGroupPosition < 0 || startGroupPosition >= mGroupCount) {
            throw new IllegalArgumentException("No this group position!");
        }

        if (startChildPosition < 0 || startChildPosition >= mGroupItemCountMap.get(startGroupPosition)) {
            throw new IllegalArgumentException("No this child position in this group!");
        }

        for (int i = 0; i < dataSize; i++) {
            mDataList.add(getItemInDataListPosition(startGroupPosition, startChildPosition) + i, dataList.get(i));
        }
        notifyItemRangeInserted(getItemInDataListPosition(startGroupPosition, startChildPosition), dataSize);
        initGroup();
    }

    /**
     * over write notifyItemRangeRemoved
     *
     * @param startPosition
     * @param itemCount
     */
    public void notifyDataRangeRemoved(int startPosition, int itemCount) {

        if (isBeyondDataSize(startPosition) || isBeyondDataSize(startPosition + itemCount - 1)) {
            return;
        }
        for (int i = itemCount; i > 0; i--) {
            mDataList.remove(startPosition + i);
        }
        notifyItemRangeRemoved(startPosition, itemCount);
        initGroup();
    }

    /**
     * over write notifyItemRangeRemoved
     *
     * @param startGroupPosition
     * @param startChildPosition
     * @param itemCount
     */
    public void notifyDataRangeRemoved(int startGroupPosition, int startChildPosition, int itemCount) {
        if (isBeyondDataSize(getItemInDataListPosition(startGroupPosition, startChildPosition))
                || isBeyondDataSize(getItemInDataListPosition(startGroupPosition, startChildPosition + itemCount - 1))) {
            return;
        }

        for (int i = itemCount; i > 0; i--) {
            mDataList.remove(getItemInDataListPosition(startGroupPosition, startChildPosition) + i);
        }

        notifyItemRangeRemoved(getItemInDataListPosition(startGroupPosition, startChildPosition), itemCount);
        initGroup();
    }

    /**
     * get group's item count
     *
     * @param groupPosition
     * @return
     */
    public int getItemCount(int groupPosition) {
        if (mGroupItemCountMap == null) {
            return -1;
        } else {
            return mGroupItemCountMap.get(groupPosition);
        }
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
                        mGroupItemCountMap.put(mGroupCount - 1, i - mGroupPositionMap.get(mGroupCount - 1) - 1);
                    }
                    mGroupCount++;
                }
            }
            mGroupItemCountMap.put(mGroupCount - 1, dataSize - 1 - lastGroupPosition);

        }

        mChildGroupPositionCacheMap = null;
        mChildItemPositionCacheMap = null;
    }

    /**
     * get the group position with layout position in the list
     *
     * @param position
     * @return
     */
    public int getGroupPosition(int position) {
        if (mGroupPositionMap == null || mGroupPositionMap.isEmpty()
                || mGroupItemCountMap == null || mGroupItemCountMap.isEmpty()) {
            return -1;
        }

        int keySize = mGroupPositionMap.size();
        for (int i = 0; i < keySize; i++) {

            if (position == 0) {
                return 0;
            } else if (mGroupPositionMap.get(mGroupPositionMap.keyAt(keySize - 1 - i)) <= position) {
                return keySize - 1 - i;
            }
        }

        return -1;
    }

    /**
     * find out the child position of its group
     *
     * @param groupPosition the group position of the group list
     * @param position      adapter item position
     * @return
     */
    private int getChildPositionInGroup(int groupPosition, int position) {

        if (groupPosition < 0 || position < 0) {
            return -1;
        }
        if (getItemViewType(position) == mGroupViewType) {
            return -1;
        }

        if (mChildItemPositionCacheMap == null) {
            mChildItemPositionCacheMap = new ArrayMap<>();
        }
        if (mDataList == null || mDataList.isEmpty()) {
            return -1;
        }
        int positionInGroup = 0;
        if (mChildItemPositionCacheMap.containsKey(position)) {
            return mChildItemPositionCacheMap.get(position);
        }

        int groupItemCount = mGroupItemCountMap.get(groupPosition);
        int groupRealPosition = mGroupPositionMap.get(groupPosition);
        if (groupItemCount == 0) {
            return -1;
        }
        positionInGroup = (position - groupRealPosition - 1) % groupItemCount;
        mChildItemPositionCacheMap.put(position, positionInGroup);

        return positionInGroup;

    }

    /**
     * find out the child position of its group
     *
     * @param position adapter item position
     * @return
     */
    public int getChildPositionInGroup(int position) {
        if (position < 0) {
            return -1;
        }
        int groupPosition = getGroupPosition(position);
        return getChildPositionInGroup(groupPosition, position);
    }

    /**
     * check if is last item in it's Group
     *
     * @param position
     * @return
     */
    public boolean isLastItemInGroup(int position) {
        if (mGroupItemCountMap == null || mGroupItemCountMap.isEmpty()) {
            return false;
        }

        int groupPosition = getGroupPosition(position);
        if (groupPosition != -1) {
            int groupItemCount = getGroupItemCount(groupPosition);
            if (groupItemCount != -1
                    && (groupItemCount + mGroupPositionMap.get(groupPosition) == position)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (mParent == null) {
            mParent = recyclerView;
        }

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

    /**
     * OnItemClickListener
     */
    public interface OnItemClickListener {

        void onClick(int position, int groupPosition, int childPosition);
    }
}


