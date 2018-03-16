package com.arjinmc.expandrecyclerview.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * BaseAdapter for {@link RecyclerView}
 * Attention!!!!!
 * viewType start from 0,default type is 0
 * automanual increasing by the count of layout res ids which you have added
 * </p>
 * Created by Eminem Lo on 2017/8/18.
 * email: arjinmc@hotmail.com
 */

public class RecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewViewHolder> {

    private final String TAG = "RecyclerViewAdapter";

    private Context mContext;
    private List<T> mDataList;
    private Map<Integer, Integer> mTypeLayoutIds;
    private RecyclerViewMultipleTypeProcessor<T> mMultipleTypeProcessor;
    private RecyclerViewSingleTypeProcessor<T> mSingleTypeProcessor;

    public RecyclerViewAdapter(Context context, List<T> dataList
            , @LayoutRes int[] typeLayoutIds, RecyclerViewMultipleTypeProcessor<T> multipleTypeProcessor) {
        mContext = context;
        mDataList = new ArrayList<>();
        if (dataList != null) {
            mDataList.addAll(dataList);
        }
        if (typeLayoutIds != null && typeLayoutIds.length != 0) {
            mTypeLayoutIds = new ArrayMap<>();
            int typeSize = typeLayoutIds.length;
            for (int i = 0; i < typeSize; i++) {
                mTypeLayoutIds.put(i, typeLayoutIds[i]);
            }
        }
        mMultipleTypeProcessor = multipleTypeProcessor;
    }

    public RecyclerViewAdapter(Context context, List<T> dataList
            , @LayoutRes int layoutId, RecyclerViewSingleTypeProcessor singleTypeProcessor) {
        mContext = context;
        mDataList = new ArrayList<>();
        if (dataList != null) {
            mDataList.addAll(dataList);
        }
        mTypeLayoutIds = new ArrayMap<>();
        mTypeLayoutIds.put(0, layoutId);
        mSingleTypeProcessor = singleTypeProcessor;
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
        // bind your data with this callback
        if (mMultipleTypeProcessor != null)
            mMultipleTypeProcessor.onBindViewHolder(holder, position, mDataList.get(position));
        else if (mSingleTypeProcessor != null)
            mSingleTypeProcessor.onBindViewHolder(holder, position, mDataList.get(position));

    }

    @Override
    public int getItemViewType(int position) {
        // make your view type rules with this callback,default type is 0
        if (mMultipleTypeProcessor != null)
            return mMultipleTypeProcessor.getItemViewType(position);
        return 0;
    }

    @Override
    public int getItemCount() {
        if (mDataList == null || mDataList.isEmpty())
            return 0;
        else
            return mDataList.size();
    }

    /**
     * Check the position is not in the range of data
     *
     * @param position
     * @return
     */
    private boolean isBeyondDataRange(int position) {
        if (position < 0 || position >= mDataList.size()) {
            throw new IllegalArgumentException("The position is out of the range of data set");
        }
        return false;
    }

    /**
     * over write notifyDataSetChanged
     *
     * @param dataList
     */
    public void notifyDataChanged(List<T> dataList) {
        mDataList.clear();
        if (dataList != null) {
            mDataList.addAll(dataList);
        }
        notifyDataSetChanged();
    }

    /**
     * over write for notifyItemChanged
     *
     * @param position
     * @param data
     */
    public void notifyDataItemChanged(int position, T data) {
        if (isBeyondDataRange(position) || data == null) {
            return;
        }

        mDataList.set(position, data);
        notifyItemChanged(position);

    }

    /**
     * over write notifyItemRangeChanged
     *
     * @param positionStart
     * @param dataList
     */
    public void notifyDataItemRangeChanged(int positionStart, List<T> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return;
        }
        int dataSize = dataList.size();
        if (isBeyondDataRange(positionStart) || isBeyondDataRange(positionStart + dataSize)) {
            return;
        }

        for (int i = 0; i < dataSize; i++) {
            mDataList.set(positionStart + i, dataList.get(i));
        }
        notifyItemRangeChanged(positionStart, dataSize);
    }

    /**
     * over write notifyItemInserted
     *
     * @param position
     * @param data
     */
    public void notifyDataItemInserted(int position, T data) {
        if (position < 0 || position > mDataList.size()) {
            throw new IllegalArgumentException("The position is out of the range of data set");
        }
        if (data == null) {
            return;
        }
        mDataList.add(position, data);
        notifyItemInserted(position);
    }

    /**
     * over write notifyItemRangeInserted
     *
     * @param positionStart
     * @param dataList
     */
    public void notifyDataItemRangeInserted(int positionStart, List<T> dataList) {

        if (positionStart < 0 || positionStart > mDataList.size()) {
            throw new IllegalArgumentException("The position is out of the range of data set");
        }

        if (dataList == null | dataList.isEmpty()) {
            return;
        }
        int dataSize = dataList.size();
        for (int i = 0; i < dataSize; i++) {
            mDataList.add(positionStart + i, dataList.get(i));
        }
        notifyItemRangeInserted(positionStart, dataSize);
    }

    /**
     * over write notifyItemRemoved
     *
     * @param position
     */
    public void notifyDataItemRemoved(int position) {
        if (isBeyondDataRange(position)) {
            return;
        }
        mDataList.remove(position);
        notifyItemRemoved(position);

    }

    /**
     * over write notifyItemRangeRemoved
     *
     * @param positionStart
     * @param itemCount
     */
    public void notifyDataItemRangeRemoved(int positionStart, int itemCount) {
        if (isBeyondDataRange(positionStart) || isBeyondDataRange(positionStart + itemCount)) {
            throw new IllegalArgumentException("The position is out of the range of data set");
        }
        for (int i = itemCount; i > 0; i--) {
            mDataList.remove(positionStart - 1 + i);
        }
        notifyItemRangeRemoved(positionStart, itemCount);
    }

    /**
     * over write notifyItemMoved
     *
     * @param fromPosition
     * @param toPosition
     */
    public void notifyDataItemMoved(int fromPosition, int toPosition) {
        if (isBeyondDataRange(fromPosition) || isBeyondDataRange(toPosition)) {
            throw new IllegalArgumentException("The position is out of the range of data set");
        }
        if (fromPosition == toPosition) {
            return;
        }
        T data = mDataList.get(fromPosition);
        if (fromPosition > toPosition) {
            mDataList.remove(fromPosition);
            mDataList.add(toPosition, data);
        } else {
            mDataList.add(toPosition, data);
            mDataList.remove(fromPosition);
        }

        notifyItemMoved(fromPosition, toPosition);
    }

}
