package com.arjinmc.expandrecyclerview.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.HashMap;
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
        mDataList = dataList;
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
        mDataList = dataList;
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
        if (mDataList == null)
            return 0;
        else
            return mDataList.size();
    }
}
